package com.wadpam.tracker.api;

import com.google.inject.Inject;
import com.wadpam.tracker.dao.DParticipantDao;
import com.wadpam.tracker.dao.DRaceDao;
import com.wadpam.tracker.dao.DSplitDao;
import com.wadpam.tracker.dao.DTrackPointDao;
import com.wadpam.tracker.dao.DTrackPointDaoBean;
import com.wadpam.tracker.domain.DParticipant;
import com.wadpam.tracker.domain.DRace;
import com.wadpam.tracker.domain.DSplit;
import com.wadpam.tracker.domain.DTrackPoint;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    
    @Inject
    private DTrackPointDao trackPointDao;
    
    @GET
    @Path("course/{keyString}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCourse(@PathParam("keyString") String participantKeyString) {
        final StringBuilder sb = new StringBuilder()
                .append("<html>\n")
                .append("  <head prefix=\"og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# fitness: http://ogp.me/ns/fitness#\">\n");
        
        meta(sb, "fb:app_id", TrackerResource.APP_ID);
        meta(sb, "og:type", "fitness.course");
        meta(sb, "og:url", "https://broker-web.appspot.com/public/course/" + participantKeyString);
        meta(sb, "og:title", "The Course Title");
        meta(sb, "og:image", "https://s-static.ak.fbcdn.net/images/devsite/attachment_blank.png");
        
        meta(sb, "fitness:distance:value", "0");
        meta(sb, "fitness:distance:units", "km");
        
        // for participant splits, queryByParent is by queryByRaceKey():
        final Object participantKey = participantDao.getPrimaryKey(participantKeyString);
        final DParticipant participant = participantDao.findByPrimaryKey(participantKey);
        final Iterable<DSplit> participantSplits = splitDao.queryByRaceKey(participantKey);
        
        // get the race splits via race id:
        final Object raceKey = raceDao.getPrimaryKey(null, participant.getRaceId());
        
        trackPointDao.writeActivityDataPoints(sb, participant, participantSplits, raceKey);
        
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
        Iterable<DSplit> splits = splitDao.queryByRaceKey(raceKey);
        return Response.ok(splits).build();
    }

    @GET
    @Path("course")
    public Response getRaces() {
        Iterable<DRace> races = raceDao.queryAll();
        return Response.ok(races).build();
    }

    public static void writeActivityDataPoint(StringBuilder sb, DTrackPoint trkpt, long t) {
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

        meta(sb, "fitness:metrics:location:latitude", Float.toString(trkpt.getPoint().getLatitude()));
        meta(sb, "fitness:metrics:location:longitude", Float.toString(trkpt.getPoint().getLongitude()));
        meta(sb, "fitness:metrics:location:altitude", Float.toString(trkpt.getElevation()));
        meta(sb, "fitness:metrics:timestamp", DTrackPointDaoBean.SDF.format(new Date(t)));
    }
    
    
}
