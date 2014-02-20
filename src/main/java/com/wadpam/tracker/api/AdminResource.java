package com.wadpam.tracker.api;

import com.google.inject.Inject;
import com.wadpam.tracker.dao.DParticipantDao;
import com.wadpam.tracker.dao.DRaceDao;
import com.wadpam.tracker.dao.DSplitDao;
import com.wadpam.tracker.dao.DTrackPointDao;
import com.wadpam.tracker.domain.DParticipant;
import com.wadpam.tracker.domain.DSplit;
import com.wadpam.tracker.domain.TrackPoint;
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
    private DParticipantDao participantDao;
    
    @Inject
    private DRaceDao raceDao;
    
    @Inject
    private DSplitDao splitDao;
    
    @Inject
    private DTrackPointDao trackPointDao;
    
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
        DSplit split = splitDao.persist(raceKey, null, nearest.getAlt(), 
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
        long timestamp = Long.parseLong(body.getElapsedSeconds());

        DSplit courseSplit = splitDao.findByNameRaceKey(body.getName(), raceKey);
        DSplit split = splitDao.persist(participantKey, null, courseSplit.getElevation(), 
                body.getName(), courseSplit.getPoint(), 
                timestamp, courseSplit.getTimestamp());
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
}
