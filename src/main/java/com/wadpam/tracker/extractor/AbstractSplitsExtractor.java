package com.wadpam.tracker.extractor;

import com.google.common.base.Strings;
import com.wadpam.tracker.api.AdminResource;
import com.wadpam.tracker.api.CreateSplitRequest;
import com.wadpam.tracker.dao.DParticipantDao;
import com.wadpam.tracker.dao.DRaceDao;
import com.wadpam.tracker.dao.DSplitDao;
import com.wadpam.tracker.domain.DParticipant;
import com.wadpam.tracker.domain.DRace;
import com.wadpam.tracker.domain.DSplit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom
 * Date: 1/24/14 Time: 7:02 PM
 */
public abstract class AbstractSplitsExtractor {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSplitsExtractor.class);
    
    protected AdminResource adminResource;
    
    protected DParticipantDao participantDao;
    protected DRaceDao raceDao;
    protected DSplitDao splitDao;

    public void process(DRace race) {
        // query and add participants:
        Iterable<DParticipant> r = participantDao.queryByRaceId(race.getId());
        final ArrayList<DParticipant> registered = new ArrayList<DParticipant>();
        for (DParticipant participant : r) {
            if (!Strings.isNullOrEmpty(participant.getExtUserId())) {
                registered.add(participant);
            }
        }
        
        if (registered.isEmpty()) {
            return;
        }
        
        // query and map race splits
        final Object raceKey = raceDao.getPrimaryKey(race);
        final TreeMap<Long, DSplit> raceSplits = splitDao.mapByParentKey(raceKey);
        
        // now, process all properly registered participants
        for (DParticipant participant : registered) {
            try {
                final Map<DSplit,DSplit> splits = getPassedSplits(race, raceSplits, 
                        participant);
                if (null != splits && !splits.isEmpty()) {
                    updateSplits(race, participant, raceSplits, splits);
                }
            } catch (IOException ex) {
                LOGGER.warn("Getting splits", ex);
            }
        }
    }

    public abstract Map<DSplit,DSplit> getPassedSplits(DRace race, 
            TreeMap<Long, DSplit> raceSplits, DParticipant participant) throws IOException;

    private void updateSplits(DRace race, DParticipant participant, 
            TreeMap<Long, DSplit> raceSplits, Map<DSplit,DSplit> passedSplitsMap) {
        
        if (!passedSplitsMap.isEmpty()) {
            
            final Object participantKey = participantDao.getPrimaryKey(participant);
            final TreeMap<Long, DSplit> persistedSplits = splitDao.mapByParentKey(participantKey);
            
            // any new passed splits?
            if (persistedSplits.size() < passedSplitsMap.size()) {
                for (DSplit raceSplit : raceSplits.values()) {
                    DSplit passedSplit = passedSplitsMap.get(raceSplit);
                    if (!persistedSplits.containsKey(passedSplit.getTimestamp())) {
                        
                        CreateSplitRequest body = new CreateSplitRequest();
                        body.setName(passedSplit.getName());
                        body.setElapsedSeconds(Long.toString(passedSplit.getTimestamp()));
                        body.setRaceSplitId(raceSplit.getId());
                        LOGGER.info("Creating participant split {}", passedSplit);
                        adminResource.createParticipantSplit(participant.getId(), 
                                body);
                        return;
                    }
                    else {
                        // LOGGER.debug("Split {} already persisted.", passedSplit);
                    }
                }
            }
        }
    }
    
    public void setParticipantDao(DParticipantDao participantDao) {
        this.participantDao = participantDao;
    }

    public void setRaceDao(DRaceDao raceDao) {
        this.raceDao = raceDao;
    }

    public void setSplitDao(DSplitDao splitDao) {
        this.splitDao = splitDao;
    }

    public void setAdminResource(AdminResource adminResource) {
        this.adminResource = adminResource;
    }

}
