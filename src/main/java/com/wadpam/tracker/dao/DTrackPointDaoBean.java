package com.wadpam.tracker.dao;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.wadpam.tracker.domain.DRace;
import com.wadpam.tracker.domain.DTrackPoint;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.sf.mardao.core.geo.DLocation;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Implementation of Business Methods related to entity DTrackPoint.
 * This (empty) class is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2014-02-07T18:15:11.501+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DTrackPointDaoBean 
	extends GeneratedDTrackPointDaoImpl
		implements DTrackPointDao 
{
    final DRaceDao raceDao;

    @Inject
    public DTrackPointDaoBean(DRaceDao raceDao) {
        this.raceDao = raceDao;
        setMardaoParentDao((DRaceDaoBean) raceDao);
    }

    public DTrackPointDaoBean() {
        this.raceDao = null;
    }
    

    @Transactional
    @Override
    public void parseGpx(InputStream bis) {
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                /** 2014-01-25T09:01:35.000Z */
                final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
                
                private DTrackPoint trkpt;
                private StringBuilder text = new StringBuilder();
                private Object raceKey;
                private long startTime = 0l;

                @Override
                public void startDocument() throws SAXException {
                    SDF.setTimeZone(TimeZone.getTimeZone("GMT"));
                }
                
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    //LOGGER.info("startElement {} qName {}", localName, qName);
                    if ("trk".equalsIgnoreCase(qName)) {
                        DRace race = raceDao.persist(null, qName, null, null);
                        raceKey = raceDao.getPrimaryKey(race);
                    }
                    else if ("trkpt".equalsIgnoreCase(qName)) {
                        trkpt = new DTrackPoint();
                        float lat = Float.parseFloat(attributes.getValue("lat"));
                        float lon = Float.parseFloat(attributes.getValue("lon"));
                        trkpt.setPoint(new DLocation(lat, lon));
                        LOG.trace("Created trkpt for {}, {}", lat, lon);
                    }
                    else if ("ele".equalsIgnoreCase(qName) || "time".equalsIgnoreCase(qName)) {
                        text = new StringBuilder();
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    text.append(ch, start, length);
                }
                
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if ("ele".equalsIgnoreCase(qName)) {
                        trkpt.setElevation(Float.valueOf(text.toString()));
                    }
                    // there can be a time tag within <metadata>
                    else if ("time".equalsIgnoreCase(qName) && null != trkpt) {
                        try {
                            Date t = SDF.parse(text.toString());
                            //LOG.info("time {} parsed into {}", text.toString(), t);
                            if (0l  == startTime) {
                                startTime = t.getTime();
                            }
                            trkpt.setTimestamp(t.getTime() - startTime);
                        } catch (ParseException ex) {
                            LOG.warn("time", ex);
                        }
                    }
                    else if ("trkpt".equalsIgnoreCase(qName)) {
                        trkpt.setRaceKey(raceKey);
                        persist(trkpt);
                    }
                }

                
            };
            
            parser.parse(bis, handler);
        } catch (ParserConfigurationException ex) {
            LOG.error("Creating parser", ex);
        } catch (SAXException ex) {
            LOG.error("Parsing", ex);
        } catch (IOException ex) {
            LOG.error("Parsing", ex);
        }
    }
    

}
