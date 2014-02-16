/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.tracker.api;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.wadpam.mardao.social.NetworkTemplate;
import static com.wadpam.tracker.api.PublicResource.LOGGER;
import com.wadpam.tracker.dao.DRaceDao;
import com.wadpam.tracker.dao.DRaceDaoBean;
import com.wadpam.tracker.dao.DTrackPointDao;
import com.wadpam.tracker.dao.DTrackPointDaoBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author osandstrom
 */
public class UploadServlet extends HttpServlet {
    
    public static final String NAME_BLOBKEY = "key";

    private final DRaceDao raceDao = new DRaceDaoBean();
    private final DTrackPointDao trackPointDao = new DTrackPointDaoBean(raceDao);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String uploadUrl = blobstoreService.createUploadUrl("/_admin/UploadServlet/callback");
        LOGGER.info("GET uploadUrl={}", uploadUrl);
        Map<String, String> entity = ImmutableMap.of("uploadUrl", uploadUrl);
        resp.setContentType(MediaType.APPLICATION_JSON);
        final OutputStream out = resp.getOutputStream();
        NetworkTemplate.MAPPER.writeValue(out, entity);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("POST {}", req.getRequestURI());
        if (req.getRequestURI().endsWith("callback")) {
            BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
            for (BlobKey blobKey : blobs.get("gpxFile")) {
                LOGGER.info("uploaded {}", blobKey.getKeyString());

                // schedule task for 10 minutes Blob processing:
                Queue defQ = QueueFactory.getDefaultQueue();
                defQ.add(TaskOptions.Builder.withUrl("/_admin/UploadServlet/parse").param(NAME_BLOBKEY, blobKey.getKeyString()));
            }
            resp.setContentType(MediaType.APPLICATION_JSON);
            final OutputStream out = resp.getOutputStream();
            NetworkTemplate.MAPPER.writeValue(out, blobs);
        }
        else if (null != req.getParameter(NAME_BLOBKEY)) {
            // parse GPX file from blobstore:
            BlobKey blobKey = new BlobKey(req.getParameter(NAME_BLOBKEY));
            InputStream bis = new BlobstoreInputStream(blobKey);

            trackPointDao.parseGpx(bis);
            bis.close();
        }
    }

}
