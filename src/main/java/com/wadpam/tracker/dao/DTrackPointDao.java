package com.wadpam.tracker.dao;

import java.io.InputStream;

/**
 * Business Methods interface for entity DTrackPoint.
 * This interface is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2014-02-07T18:15:11.501+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public interface DTrackPointDao extends GeneratedDTrackPointDao {

	void parseGpx(InputStream bis);
	
}
