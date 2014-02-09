package com.wadpam.tracker.api;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("nosec")
public class CourseResource {
    
    static final Logger LOGGER = LoggerFactory.getLogger(CourseResource.class);
    
    @Inject
    private HttpServletRequest request;
    
    @GET
    @Path("course/{keyString}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCourse(@PathParam("keyString") String keyString) {
        final StringBuilder sb = new StringBuilder()
                .append("<html>\n")
                .append("  <head prefix=\"og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# fitness: http://ogp.me/ns/fitness#\">\n");
        
        meta(sb, "fb:app_id", TrackerResource.APP_ID);
        meta(sb, "og:type", "fitness.course");
        meta(sb, "og:url", "https://broker-web.appspot.com/public/course/" + keyString);
        meta(sb, "og:title", "The Course Title");
        meta(sb, "og:image", "https://s-static.ak.fbcdn.net/images/devsite/attachment_blank.png");
        
        meta(sb, "fitness:distance:value", "0");
        meta(sb, "fitness:distance:units", "km");
        
        sb.append("  </head><body>\n</body></html>\n");
        return sb.toString();
    }
    
    private void meta(StringBuilder sb, String ogtype, String fitnesscourse) {
        sb.append("    <meta property=\"")
                .append(ogtype)
                .append("\" content=\"")
                .append(fitnesscourse)
                .append("\" />\n");
    }
    
    @GET
    @Path("upload")
    public Response getUploadUrl() {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String uploadUrl = blobstoreService.createUploadUrl("/nosec/uploadCallback");
        LOGGER.info("GET uploadUrl={}", uploadUrl);
        Map<String, String> entity = ImmutableMap.of("uploadUrl", uploadUrl);
        return Response.ok(entity).build();
    }
    
    @POST
    @Path("uploadCallback")
    public Response uploadCallback() {
        LOGGER.info("POST uploadCallback");
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        for (BlobKey blobKey : blobs.get("gpxFile")) {
            LOGGER.info("uploaded {}", blobKey.getKeyString());
        }
        return Response.ok().build();
    }
    
    @GET
    @Path("uploadCallback")
    public Response uploadCallbackGet() {
        LOGGER.info("GET uploadCallback");
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        for (BlobKey blobKey : blobs.get("gpxFile")) {
            LOGGER.info("uploaded {}", blobKey.getKeyString());
        }
        return Response.ok().build();
    }
}
