package com.wadpam.tracker.api;

import com.google.inject.Inject;
import com.wadpam.tracker.dao.DParticipantDao;
import com.wadpam.tracker.dao.DRaceDao;
import com.wadpam.tracker.dao.DRaceDaoBean;
import com.wadpam.tracker.dao.DSplitDao;
import com.wadpam.tracker.domain.DParticipant;
import com.wadpam.tracker.domain.DRace;
import com.wadpam.tracker.domain.DSplit;
import com.wadpam.tracker.domain.TrackPoint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author osandstrom
 */
@Path("pub")
@Produces(MediaType.APPLICATION_JSON)
public class PublicResource {
    
    static final Logger LOGGER = LoggerFactory.getLogger(PublicResource.class);

    @Inject
    private HttpServletRequest request;
    
    @Inject
    private DParticipantDao participantDao;
    
    @Inject
    private DRaceDao raceDao;
    
    @Inject
    private DSplitDao splitDao;
    
    @GET
    @Path("course/{keyString}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCourse(@PathParam("keyString") String splitKeyString) {
        // for participant splits, queryByParent is by queryByRaceKey():
        final Object splitKey = splitDao.getPrimaryKey(splitKeyString);
        final Object participantKey = splitDao.getParentKeyByPrimaryKey(splitKey);
        final DParticipant participant = participantDao.findByPrimaryKey(participantKey);
        final Iterable<DSplit> participantSplits = splitDao.queryByRaceKey(participantKey);
        
        // get the race splits via race id:
        final Object raceKey = raceDao.getPrimaryKey(null, participant.getRaceId());
        final DRace race = raceDao.findByPrimaryKey(raceKey);
        final Iterable<DSplit> raceSplits = splitDao.queryByRaceKey(raceKey);
        
        final StringBuilder sb = new StringBuilder()
                .append("<html>\n")
                .append("  <head prefix=\"og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# fitness: http://ogp.me/ns/fitness#\">\n");
        
        meta(sb, "fb:app_id", TrackerResource.APP_ID);
        meta(sb, "og:type", "fitness.course");
        meta(sb, "og:url", "https://broker-web.appspot.com/pub/course/" + splitKeyString);
        meta(sb, "og:title", race.getDisplayName());
        meta(sb, "og:image", "https://s-static.ak.fbcdn.net/images/devsite/attachment_blank.png");
        
        writeActivityDataPoints(sb, participant, participantSplits, raceKey, race, raceSplits);
        
        sb.append("  </head><body>\n</body></html>\n");
        return sb.toString();
    }
    
    private static void meta(StringBuilder sb, String property, String content) {
        sb.append("    <meta property=\"")
                .append(property)
                .append("\" content=\"")
                .append(content)
                .append("\" />\n");
    }

    @GET
    @Path("course/{raceId}/split")
    public Response getSplits(@PathParam("raceId") Long raceId) {
        final Object raceKey = raceDao.getPrimaryKey(null, raceId);
        TreeMap<Long, DSplit> splits = splitDao.mapByParentKey(raceKey);
        return Response.ok(splits.values()).build();
    }

    @GET
    @Path("course")
    public Response getRaces() {
        Iterable<DRace> races = raceDao.queryOpen(new Date());
        return Response.ok(races).build();
    }
    
    @GET
    @Path("race/{raceId}")
    public Response getRace(@PathParam("raceId") Long raceId) {
        final DRace dRace = raceDao.findByPrimaryKey(raceId);
        if (null == dRace) {
            return Response.status(Status.NOT_FOUND).build();
        }
        UpdateRaceRequest race = new UpdateRaceRequest();
        race.setDisplayName(dRace.getDisplayName());
        race.setTimeZone(dRace.getTimeZone());
        SimpleDateFormat sdf = DRaceDaoBean.getDateFormat(dRace.getTimeZone());
        race.setStartTime(null != dRace.getStartDate() ? 
                sdf.format(dRace.getStartDate()) : "");
        
        return Response.ok(race).build();
    }
    
    @GET
    @Path("timezone")
    public Response getTimezoneIDs() {
        return Response.ok(TimeZone.getAvailableIDs()).build();
    }

    public void writeActivityDataPoints(StringBuilder sb, 
            DParticipant participant, Iterable<DSplit> participantSplits, 
            Object raceKey, DRace race, Iterable<DSplit> raceSplits) {
        
        // find latest race timestamp (stored in trackPointId)
        final TreeMap<Long, DSplit> splitMap = new TreeMap<Long, DSplit>();
        for (DSplit split : participantSplits) {
            splitMap.put(split.getTimestamp(), split);
        }
        final long maxTimestamp = splitMap.isEmpty() ? 0L : splitMap.lastKey();
        LOGGER.debug("maxTimestamp is {}, splits.size={}", maxTimestamp, splitMap.size());
        
        // iterate track points until timestamp
        List<TrackPoint> points = raceDao.getTrack(race.getBlobKey());
        Iterator<TrackPoint> ptIterator = points.iterator();
        
        long prevRaceSplit = 0L, prevPartSplit = 0L, t = -1L, startTime = 0L;
        Iterable<DSplit> i = splitMap.isEmpty() ? raceSplits : splitMap.values();
        TrackPoint trkpt;
        boolean beforeStart = true;
        DSplit next = null;
        for (Iterator<DSplit> iterator = i.iterator(); iterator.hasNext(); ) {
            next = iterator.next();
            // LOGGER.debug("prevPartSplit={}, prevRaceSplit={}", prevPartSplit, prevRaceSplit);
            LOGGER.debug("next.timestamp {}, course.timestamp {}", next.getTimestamp(), next.getTrackPointId());
            
            // linear interpolation between splits
            double factor = ((double) (next.getTimestamp() - prevPartSplit)) / ((double) (next.getTrackPointId() - prevRaceSplit));
            LOGGER.debug("factor for split {} is {}", next.getName(), factor);
            
            do {
                trkpt = ptIterator.next();
                
                // have we reached the start split?
                if (beforeStart) {
                    beforeStart = trkpt.getT() < next.getTrackPointId();
                    startTime = next.getTimestamp();
                }
                
                // only output if on or past start split
                if (!beforeStart) {
                    t = prevPartSplit + Math.round(factor * (trkpt.getT() - prevRaceSplit));
                    PublicResource.writeActivityDataPoint(sb, trkpt, t);
                    if (maxTimestamp <= t) break;
                }
                
            } while (trkpt.getT() < next.getTrackPointId());
            LOGGER.debug("At split, t is {}, trkpt.T is {}", t, trkpt.getT());

            // tag split metrics with distance 
            meta(sb, "fitness:metrics:distance:value", Float.toString(next.getDistance()/1000.0f));
            meta(sb, "fitness:metrics:distance:units", "km");

            // and add a split
            meta(sb, "fitness:splits:values:units", "km");
            meta(sb, "fitness:splits:values:value", Float.toString(next.getDistance()/1000.0f));
            meta(sb, "fitness:splits:values:units", "s");
            meta(sb, "fitness:splits:values:value", Long.toString((next.getTimestamp()-startTime)/1000));
            if (10.0 < next.getDistance()) {
                meta(sb, "fitness:splits:values:units", "s/m");
                meta(sb, "fitness:splits:values:value", Float.toString(((next.getTimestamp()-startTime)/1000)/next.getDistance()));
            }
            
            prevPartSplit = next.getTimestamp();
            prevRaceSplit = next.getTrackPointId();
        }
        
        // tag course with distance 
        meta(sb, "fitness:distance:units", "km");
        meta(sb, "fitness:distance:value", Float.toString(next.getDistance()/1000.0f));
        // tag course with duration
        meta(sb, "fitness:duration:units", "s");
        meta(sb, "fitness:duration:value", Long.toString((prevPartSplit-startTime)/1000));
        // tag course with pace
        if (10.0 < next.getDistance()) {
            meta(sb, "fitness:pace:units", "s/m");
            meta(sb, "fitness:pace:value", Float.toString(((prevPartSplit-startTime)/1000)/next.getDistance()));
        }
    }

    public static void writeActivityDataPoint(StringBuilder sb, TrackPoint trkpt, long t) {
//        <meta property="fitness:metrics:location:latitude"  content="37.416382" />
//        <meta property="fitness:metrics:location:longitude" content="-122.152659" />
//        <meta property="fitness:metrics:location:altitude"  content="42" />
//        <meta property="fitness:metrics:timestamp" content="2011-01-26T00:00" />
//        <meta property="fitness:metrics:distance:value" content="0" />
//        <meta property="fitness:metrics:distance:units" content="mi" />
//        <meta property="fitness:metrics:pace:value" content="0" />
//        <meta property="fitness:metrics:pace:units" content="s/m" />
//        <meta property="fitness:metrics:calories" content="0" />
//        <meta property="fitness:metrics:custom_quantity:value" content="0" />
//        <meta property="fitness:metrics:custom_quantity:units" content="SOME_UNIT_URL" />

        meta(sb, "fitness:metrics:location:latitude", Float.toString(trkpt.getLat()));
        meta(sb, "fitness:metrics:location:longitude", Float.toString(trkpt.getLon()));
        meta(sb, "fitness:metrics:location:altitude", Float.toString(trkpt.getAlt()));
        meta(sb, "fitness:metrics:timestamp", DRaceDaoBean.SDF.format(new Date(t)));
    }
    
    @GET
    @Path("img")
    public Response redirectToCDN(@QueryParam("camId") String camId) throws URISyntaxException {
        URI uri = new URI("https://legend-passbook.appspot.com/images/pass.se.bassac.stamps/storeCard/logo.png?uid=123&camId=" + camId);
        
        return Response.status(302).location(uri).build();
    }
    
    @GET
    @Path("imgrecs")
    @Produces("image/png")
    public Response getImageWithCookie(@QueryParam("cookieValue") String cookieValue) throws IOException {
        InputStream in = getClass().getResourceAsStream("/theBluePumpkin.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte b[] = new byte[1024];
        int count;
        while (0 < (count = in.read(b))) {
            baos.write(b, 0, count);
        }
        in.close();
        return Response.ok(baos.toByteArray(), "image/png")
                .cookie(new NewCookie("pumpkin", cookieValue))
                .build();
    }
    
    @GET
    @Path("imgclick")
    public Response onImageClick(@CookieParam("pumpkin") String cookieValue) {
        LOGGER.info("cookie pumpkin has value {}", cookieValue);
        Map<String, String> body = new HashMap<String,String>();
        body.put("pumpkin", cookieValue);
        return Response.ok(body).build();
    }
}
