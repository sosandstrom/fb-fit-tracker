package com.wadpam.tracker.extractor;

import com.wadpam.tracker.dao.DRaceDaoBean;
import com.wadpam.tracker.dao.DSplitDao;
import com.wadpam.tracker.domain.DParticipant;
import com.wadpam.tracker.domain.DRace;
import com.wadpam.tracker.domain.DSplit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scrapes splits from Vasaloppet's results page
 * @author osandstrom
 */
public class VasaloppsExtractor extends AbstractSplitsExtractor {

    @Override
    public Map<DSplit,DSplit> getPassedSplits(DRace race, 
        TreeMap<Long, DSplit> raceSplits, DParticipant participant) throws IOException {
        
        final HashMap<DSplit, DSplit> splitsMap = new HashMap<DSplit, DSplit>();
        
        URL url = new URL(race.getQueryUrl() + participant.getExtUserId());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        
        StringBuilder sb = new StringBuilder();
        String s;
        while (null != (s = br.readLine())) {
            sb.append(s);
            sb.append('\n');
        }
        br.close();
        conn.disconnect();
        final String page = sb.toString();
        
        final SimpleDateFormat sdf = DRaceDaoBean.getDateFormat(race.getTimeZone());
        final String raceDateTime = sdf.format(race.getStartDate());
        final String raceDate = raceDateTime.substring(0, 10);
        
        Pattern p;
        for (DSplit raceSplit : raceSplits.values()) {
            if (DSplitDao.NAME_START.equals(raceSplit.getName())) {
                p = Pattern.compile("<td class=\"desc\">Starttid</td>\\s*" +
                "<td class=\"f-__starttime last\">([\\d\\:]+)</td>");
            }
            else if (DSplitDao.NAME_FINISH.equals(raceSplit.getName())) {
                p = Pattern.compile("<td class=\"desc\">Mål</td>\\s*" +
                "<td class=\"time_day\">([\\d\\:]+)</td>");
            }
            else {
                p = Pattern.compile("<td class=\"desc\">" + raceSplit.getName() + "</td>\\s*" +
                "<td class=\"time_day\">([\\d\\:]+)</td>");
            }
            Matcher m = p.matcher(page);
            String time = null;
            if (m.find()) {
                time = m.group(1);
                String dateTime = raceDate + 'T' + time;
                try {
                    Date date = sdf.parse(dateTime);
                    final DSplit split = new DSplit();
                    split.setTimestamp(date.getTime());
                    split.setName(raceSplit.getName());
                    splitsMap.put(raceSplit, split);
                    
                    //LOGGER.debug("time is {} for pattern {}", dateTime, p);
                } catch (ParseException ex) {
                    LOGGER.warn("Could not parse dateTime {}", dateTime);
                }
            }
        }
        
        return splitsMap;
    }
    
}
