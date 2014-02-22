package com.wadpam.tracker.api;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.wadpam.mardao.oauth.dao.DConnectionDao;
import com.wadpam.mardao.oauth.dao.DOAuth2UserDao;
import static com.wadpam.tracker.api.TrackerResource.APP_ID;
import com.wadpam.tracker.dao.DParticipantDao;
import com.wadpam.tracker.dao.DRaceDao;
import com.wadpam.tracker.dao.DRaceDaoBean;
import com.wadpam.tracker.dao.DSplitDao;
import com.wadpam.tracker.dao.DTrackPointDao;
import com.wadpam.tracker.domain.DParticipant;
import com.wadpam.tracker.domain.DRace;
import com.wadpam.tracker.domain.DSplit;
import com.wadpam.tracker.domain.TrackPoint;
import com.wadpam.tracker.opengraph.FitnessRuns;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import net.sf.mardao.core.geo.DLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author osandstrom
 */
@Path("_adm")
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {
    
    static final Logger LOGGER = LoggerFactory.getLogger(AdminResource.class);
    
    @Inject
    private HttpServletRequest request;
    
    @Inject
    private DConnectionDao connectionDao;
    
    @Inject
    private DParticipantDao participantDao;
    
    @Inject
    private DRaceDao raceDao;
    
    @Inject
    private DSplitDao splitDao;
    
    @Inject
    private DTrackPointDao trackPointDao;
    
    @Inject
    private DOAuth2UserDao userDao;
    
    @POST
    @Path("course/{raceId}/split")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCourseSplit(@PathParam("raceId") Long raceId,
            CreateSplitRequest body) {
        final Object raceKey = raceDao.getPrimaryKey(null, raceId);
        long minTimestamp = (null != body.getElapsedSeconds() && 0 < body.getElapsedSeconds().length()) ?
                Long.parseLong(body.getElapsedSeconds()) : 0L;
        LOGGER.info("elapsedSeconds {} parses into {}", body.getElapsedSeconds(), minTimestamp);
        Float lat = null, lon = null;
        if (null != body.getLatLong() && 0 < body.getLatLong().length()) {
            String[] ll = body.getLatLong().split(",");
            if (2 == ll.length) {
                lat = Float.valueOf(ll[0]);
                lon = Float.valueOf(ll[1]);
            }
        }
        TrackPoint nearest = raceDao.findNearest(raceKey, minTimestamp, lat, lon);
        DSplit split = splitDao.persist(raceKey, null, 
                body.getDistance(), nearest.getAlt(), 
                body.getName(), new DLocation(nearest.getLat(), nearest.getLon()),
                nearest.getT(), nearest.getT());
        return Response.ok(split).build();
    }
    
    @POST
    @Path("participant/{participantId}/split")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createParticipantSplit(@PathParam("participantId") Long participantId,
            CreateSplitRequest body) {
        final Object participantKey = participantDao.getPrimaryKey(null, participantId);
        final DParticipant participant = participantDao.findByPrimaryKey(participantKey);
        final Object raceKey = raceDao.getPrimaryKey(null, participant.getRaceId());
        long timestamp = Strings.isNullOrEmpty(body.getElapsedSeconds()) ? 
                System.currentTimeMillis() : Long.parseLong(body.getElapsedSeconds());

        DSplit courseSplit = splitDao.findByPrimaryKey(raceKey, body.getRaceSplitId());
        DSplit split = splitDao.persist(participantKey, null, 
                courseSplit.getDistance(), courseSplit.getElevation(), 
                courseSplit.getName(), courseSplit.getPoint(), 
                timestamp, courseSplit.getTimestamp());
        
        // post / update fitness object
        upsertFitnessObject(participant, split);
        
        return Response.ok(split).build();
    }
    
    @DELETE
    @Path("course/{raceId}/split/{splitId}")
    public Response deleteSplit(@PathParam("raceId") Long raceId,
            @PathParam("splitId") Long splitId) {
        final Object raceKey = raceDao.getPrimaryKey(null, raceId);
        boolean found = splitDao.delete(raceKey, splitId);
        return Response.status(found ? Status.OK : Status.NO_CONTENT).build();
    }

    private void upsertFitnessObject(DParticipant participant, DSplit split) {
        
        // get most recent access token
        final Object userKey = userDao.getPrimaryKey(null, participant.getUserId());
        final String accessToken = connectionDao.getAccessToken(userKey, DConnectionDao.PROVIDER_ID_FACEBOOK);
        
        if (null != accessToken) {
            if (null == participant.getActionId()) {
                
                // stuff needed to create fitness run:
                final Object participantKey = participantDao.getPrimaryKey(participant);
                String participantKeyString = participantDao.getKeyString(participantKey);
                String courseUrl = "https://broker-web.appspot.com/pub/course/" + participantKeyString;
                final DRace race = raceDao.findByPrimaryKey(null, participant.getRaceId());

                String actionId = createFitnessRun(courseUrl, APP_ID, accessToken,
                        race.getDisplayName(), split.getTimestamp());
                participant.setActionId(actionId);
                participantDao.update(participant);
            }
            else {
                
            }
        }
    }

    private String createFitnessRun(String courseUrl, String appId, 
            String accessToken, String title, Long startTime) {
        FitnessRuns run = new FitnessRuns();
        run.setApp_id(appId);
        run.setTitle(title);
        run.setCourse(courseUrl);
        
        Map<String,String> response = TrackerResource.postStandardObject("/me/fitness.runs", 
                accessToken, run,
                ImmutableMap.builder().put("course", courseUrl)
                //.put("no_feed_story", "true")
                .put("start_time", DRaceDaoBean.SDF.format(new Date(startTime)))
                .put("expires_in", "28800") // 8h
                .put("live_text", "Send me cheers along the way by liking or commenting on this post.")
                .put("privacy", "{\"value\":\"SELF\"}")
                .build());
        return response.get("id");
    }

}
