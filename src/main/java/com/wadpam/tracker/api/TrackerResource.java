package com.wadpam.tracker.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.wadpam.mardao.oauth.web.OAuth2Filter;
import com.wadpam.mardao.social.NetworkTemplate;
import com.wadpam.tracker.dao.DParticipantDao;
import com.wadpam.tracker.dao.DRaceDao;
import com.wadpam.tracker.domain.DParticipant;
import com.wadpam.tracker.opengraph.FitnessCourse;
import com.wadpam.tracker.opengraph.FitnessRuns;
import com.wadpam.tracker.opengraph.RequestObject;
import com.wadpam.tracker.opengraph.StandardObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author osandstrom
 */
@Path("api/tracker")
@Produces(MediaType.APPLICATION_JSON)
public class TrackerResource {
    public static final String APP_ID = "255653361131262";
    
    static final Logger LOGGER = LoggerFactory.getLogger(TrackerResource.class);
    
    @Inject
    private HttpServletRequest request;
    
    private final DParticipantDao participantDao;
    private final DRaceDao raceDao;
    
    @Inject
    public TrackerResource(DParticipantDao participantDao, DRaceDao raceDao) {
        this.participantDao = participantDao;
        this.raceDao = raceDao;
    }
    
    @GET
    public Response createFitnessRun(@QueryParam("raceId") Long raceId) {
        String courseKeyString = Long.toHexString(System.currentTimeMillis());
        String courseUrl = "https://broker-web.appspot.com/public/course/" + courseKeyString;
                // createCourse(APP_ID, 0.0);
        
        String actionId = createFitnessRun(courseUrl, APP_ID);
        Long userId = OAuth2Filter.getUserId(request);
        DParticipant participant = participantDao.persist(actionId, raceId, userId);
        
        return Response.ok(participant).build();
    }

    private String createCourse(String appId, double distance) {
        FitnessCourse course = new FitnessCourse();
        course.setApp_id(appId);
        course.setTitle("Long run started.");
        Map<String,String> response = postStandardObject("/me/objects/fitness.course", course,
                ImmutableMap.of("fitness%3Ametrics%3Alocation%3Alatitude", 55.3f,
                "fitness%3Ametrics%3Alocation%3Alongitude", 15.3f,
                "fitness%3Ametrics%3Adistance%3Avalue", 30.2f,
                "fitness%3Ametrics%3Adistance%3Aunits", "km"));
        return "https://graph.facebook.com/me/objects/fitness.course/" + response.get("id");
    }

    private String createFitnessRun(String courseUrl, String appId) {
//        StringBuilder sb = new StringBuilder();
//        
//        append(sb, OAuth2Filter.NAME_ACCESS_TOKEN, getAccessToken());
//        append(sb, "course", courseUrl);

        FitnessRuns run = new FitnessRuns();
        run.setApp_id(appId);
        run.setTitle("Heading out for a Long run");
        run.setCourse(courseUrl);
        
        Map<String,String> response = postStandardObject("/me/fitness.runs", run,
                ImmutableMap.builder().put("course", courseUrl)
                .put("no_feed_story", "true")
                .put("start_time", "2014-02-02T13:32")
                .put("expires_in", "3660")
                .put("live_text", "Send me cheers along the way by liking or commenting on this post.")
                .put("privacy", "{\"value\":\"SELF\"}")
                .build());
        return response.get("id");
    }

    private void appendActivityDataPoints(StringBuilder sb, double distance) {
        
    }

    private void appendStandardProperties(StringBuilder sb, String appId) {
        append(sb, "fb:app_id", appId);
        append(sb, "og:type", "fitness.course");
        append(sb, "og:url", "https://broker-web.appspot.com/courses/start.html");
        append(sb, "og:title", "Sample Course");
        append(sb, "og:image", "https://s-static.ak.fbcdn.net/images/devsite/attachment_blank.png");
        append(sb, "og:description", "Some description");
    }

    private void appendOtherProperties(StringBuilder sb) {
        append(sb, "fitness:distance:units", "km");
        append(sb, "fitness:distance:value", "12.84");
        append(sb, "fitness:duration:units", "min");
        append(sb, "fitness:duration:value", "23.45");
        append(sb, "fitness:metrics:location:latitude", "55.34916");
        append(sb, "fitness:metrics:location:longitude", "12.57098");
        append(sb, "fitness:metrics:location:altitude", "12");
        append(sb, "fitness:metrics:timestamp", "2014-01-31T20:25");
        
    }
    
    public static void append(Map<String,Object> map, String property, String content) {
        map.put(property, content);
    }

    public static void append(StringBuilder sb, String property, String content) {
        if (0 < sb.length()) {
            sb.append('&');
        }
        try {
            sb.append(property.replace(":", "%3A"))
                    .append('=')
                    .append(URLEncoder.encode(content, "UTF-8"));
        } catch (UnsupportedEncodingException willNeverHappen) {
        }
    }
    
    private String getAccessToken() {
        return (String) request.getAttribute(OAuth2Filter.NAME_ACCESS_TOKEN);
    }

    private Map<String, String> postStandardObject(String path, StandardObject course, Map... extras) {
        RequestObject request = new RequestObject();
        request.setAccess_token(getAccessToken());
        try {
            request.setObject(NetworkTemplate.MAPPER.writeValueAsString(course));
        } catch (JsonProcessingException ex) {
            LOGGER.error("Converting StandardObject", ex);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("access_token", getAccessToken());
        params.put("object", request.getObject());
        if (null != extras && 0 < extras.length) {
            params.putAll(extras[0]);
        }
        
        NetworkTemplate template = new NetworkTemplate();
        Map<String, String> requestHeaders = ImmutableMap.of(NetworkTemplate.CONTENT_TYPE, NetworkTemplate.MIME_FORM);
        Map<String,String> response = template.post("https://graph.facebook.com" + path, requestHeaders, params, Map.class);
        return response;
    }

}
