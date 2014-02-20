package com.wadpam.tracker.dao;

import java.util.HashMap;
import java.util.Map;
import net.sf.mardao.core.dao.DaoImpl;
import com.wadpam.tracker.domain.DTrackPoint;
import com.wadpam.tracker.domain.DRace;
import com.wadpam.tracker.domain.DSplit;
import com.wadpam.tracker.domain.DParticipant;

/**
 * Context to define the Dao beans.
 * This file is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2014-02-19T18:43:33.667+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DaoConfig {

    public static Map<Class, DaoImpl> createDaos() {
        final HashMap<Class, DaoImpl> map = new HashMap<Class, DaoImpl>();

        // first, create and map Dao instances:
        final DTrackPointDaoBean dTrackPointDao =
            new DTrackPointDaoBean();
        map.put(DTrackPoint.class, dTrackPointDao);
        final DRaceDaoBean dRaceDao =
            new DRaceDaoBean();
        map.put(DRace.class, dRaceDao);
        final DSplitDaoBean dSplitDao =
            new DSplitDaoBean();
        map.put(DSplit.class, dSplitDao);
        final DParticipantDaoBean dParticipantDao =
            new DParticipantDaoBean();
        map.put(DParticipant.class, dParticipantDao);

        // next, wire parents;
        dTrackPointDao.setMardaoParentDao(dRaceDao);
        dSplitDao.setMardaoParentDao(dRaceDao);

        // finally, wire many-to-ones;
	
        return map;
    }
}