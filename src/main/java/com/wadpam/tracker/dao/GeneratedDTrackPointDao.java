package com.wadpam.tracker.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.core.dao.Dao;
import com.wadpam.tracker.domain.DTrackPoint;
import net.sf.mardao.core.geo.DLocation;

/**
 * DAO interface with finder methods for DTrackPoint entities.
 *
 * Generated on 2014-02-19T18:43:33.667+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public interface GeneratedDTrackPointDao extends Dao<DTrackPoint, java.lang.Long> {

	/** Column name for primary key attribute is "id" */
	static final String COLUMN_NAME_ID = "id";

	/** Column name for parent raceKey is "raceKey" */
	static final String COLUMN_NAME_RACEKEY = "raceKey";


	/** Column name for field createdBy is "createdBy" */
	static final String COLUMN_NAME_CREATEDBY = "createdBy";
	/** Column name for field createdDate is "createdDate" */
	static final String COLUMN_NAME_CREATEDDATE = "createdDate";
	/** Column name for field elevation is "elevation" */
	static final String COLUMN_NAME_ELEVATION = "elevation";
	/** Column name for field point is "point" */
	static final String COLUMN_NAME_POINT = "point";
	/** Column name for field timestamp is "timestamp" */
	static final String COLUMN_NAME_TIMESTAMP = "timestamp";
	/** Column name for field updatedBy is "updatedBy" */
	static final String COLUMN_NAME_UPDATEDBY = "updatedBy";
	/** Column name for field updatedDate is "updatedDate" */
	static final String COLUMN_NAME_UPDATEDDATE = "updatedDate";

	/** The list of attribute names */
	static final List<String> COLUMN_NAMES = Arrays.asList(		COLUMN_NAME_CREATEDBY,
		COLUMN_NAME_CREATEDDATE,
		COLUMN_NAME_ELEVATION,
		COLUMN_NAME_POINT,
		COLUMN_NAME_TIMESTAMP,
		COLUMN_NAME_UPDATEDBY,
		COLUMN_NAME_UPDATEDDATE);
	/** The list of Basic attribute names */
	static final List<String> BASIC_NAMES = Arrays.asList(		COLUMN_NAME_CREATEDBY,
		COLUMN_NAME_CREATEDDATE,
		COLUMN_NAME_ELEVATION,
		COLUMN_NAME_POINT,
		COLUMN_NAME_TIMESTAMP,
		COLUMN_NAME_UPDATEDBY,
		COLUMN_NAME_UPDATEDDATE);
	/** The list of attribute names */
	static final List<String> MANY_TO_ONE_NAMES = Arrays.asList();


	// ----------------------- parent finder -------------------------------
	/**
	 * query-by method for parent field raceKey
	 * @param raceKey the specified attribute
	 * @return an Iterable of DTrackPoints with the specified parent
	 */
	Iterable<DTrackPoint> queryByRaceKey(Object raceKey);
		
	/**
	 * query-keys-by method for parent field raceKey
	 * @param raceKey the specified attribute
	 * @return an Iterable of DTrackPoints with the specified parent
	 */
	Iterable<java.lang.Long> queryKeysByRaceKey(Object raceKey);

	/**
	 * query-page-by method for parent field raceKey
	 * @param raceKey the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified raceKey (parent)
	 */
	CursorPage<DTrackPoint> queryPageByRaceKey(java.lang.Object raceKey,
            int pageSize, String cursorString);

	// ----------------------- field finders -------------------------------
	/**
	 * query-by method for field createdBy
	 * @param createdBy the specified attribute
	 * @return an Iterable of DTrackPoints for the specified createdBy
	 */
	Iterable<DTrackPoint> queryByCreatedBy(java.lang.String createdBy);
		
	/**
	 * query-keys-by method for field createdBy
	 * @param createdBy the specified attribute
	 * @return an Iterable of DTrackPoints for the specified createdBy
	 */
	Iterable<java.lang.Long> queryKeysByCreatedBy(java.lang.String createdBy);

	/**
	 * query-page-by method for field createdBy
	 * @param createdBy the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified createdBy
	 */
	CursorPage<DTrackPoint> queryPageByCreatedBy(java.lang.String createdBy,
            int pageSize, String cursorString);


	/**
	 * query-by method for field createdDate
	 * @param createdDate the specified attribute
	 * @return an Iterable of DTrackPoints for the specified createdDate
	 */
	Iterable<DTrackPoint> queryByCreatedDate(java.util.Date createdDate);
		
	/**
	 * query-keys-by method for field createdDate
	 * @param createdDate the specified attribute
	 * @return an Iterable of DTrackPoints for the specified createdDate
	 */
	Iterable<java.lang.Long> queryKeysByCreatedDate(java.util.Date createdDate);

	/**
	 * query-page-by method for field createdDate
	 * @param createdDate the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified createdDate
	 */
	CursorPage<DTrackPoint> queryPageByCreatedDate(java.util.Date createdDate,
            int pageSize, String cursorString);


	/**
	 * query-by method for field elevation
	 * @param elevation the specified attribute
	 * @return an Iterable of DTrackPoints for the specified elevation
	 */
	Iterable<DTrackPoint> queryByElevation(java.lang.Float elevation);
		
	/**
	 * query-keys-by method for field elevation
	 * @param elevation the specified attribute
	 * @return an Iterable of DTrackPoints for the specified elevation
	 */
	Iterable<java.lang.Long> queryKeysByElevation(java.lang.Float elevation);

	/**
	 * query-page-by method for field elevation
	 * @param elevation the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified elevation
	 */
	CursorPage<DTrackPoint> queryPageByElevation(java.lang.Float elevation,
            int pageSize, String cursorString);


	/**
	 * query-by method for field point
	 * @param point the specified attribute
	 * @return an Iterable of DTrackPoints for the specified point
	 */
	Iterable<DTrackPoint> queryByPoint(net.sf.mardao.core.geo.DLocation point);
		
	/**
	 * query-keys-by method for field point
	 * @param point the specified attribute
	 * @return an Iterable of DTrackPoints for the specified point
	 */
	Iterable<java.lang.Long> queryKeysByPoint(net.sf.mardao.core.geo.DLocation point);

	/**
	 * query-page-by method for field point
	 * @param point the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified point
	 */
	CursorPage<DTrackPoint> queryPageByPoint(net.sf.mardao.core.geo.DLocation point,
            int pageSize, String cursorString);


	/**
	 * query-by method for field timestamp
	 * @param timestamp the specified attribute
	 * @return an Iterable of DTrackPoints for the specified timestamp
	 */
	Iterable<DTrackPoint> queryByTimestamp(java.lang.Long timestamp);
		
	/**
	 * query-keys-by method for field timestamp
	 * @param timestamp the specified attribute
	 * @return an Iterable of DTrackPoints for the specified timestamp
	 */
	Iterable<java.lang.Long> queryKeysByTimestamp(java.lang.Long timestamp);

	/**
	 * query-page-by method for field timestamp
	 * @param timestamp the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified timestamp
	 */
	CursorPage<DTrackPoint> queryPageByTimestamp(java.lang.Long timestamp,
            int pageSize, String cursorString);


	/**
	 * query-by method for field updatedBy
	 * @param updatedBy the specified attribute
	 * @return an Iterable of DTrackPoints for the specified updatedBy
	 */
	Iterable<DTrackPoint> queryByUpdatedBy(java.lang.String updatedBy);
		
	/**
	 * query-keys-by method for field updatedBy
	 * @param updatedBy the specified attribute
	 * @return an Iterable of DTrackPoints for the specified updatedBy
	 */
	Iterable<java.lang.Long> queryKeysByUpdatedBy(java.lang.String updatedBy);

	/**
	 * query-page-by method for field updatedBy
	 * @param updatedBy the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified updatedBy
	 */
	CursorPage<DTrackPoint> queryPageByUpdatedBy(java.lang.String updatedBy,
            int pageSize, String cursorString);


	/**
	 * query-by method for field updatedDate
	 * @param updatedDate the specified attribute
	 * @return an Iterable of DTrackPoints for the specified updatedDate
	 */
	Iterable<DTrackPoint> queryByUpdatedDate(java.util.Date updatedDate);
		
	/**
	 * query-keys-by method for field updatedDate
	 * @param updatedDate the specified attribute
	 * @return an Iterable of DTrackPoints for the specified updatedDate
	 */
	Iterable<java.lang.Long> queryKeysByUpdatedDate(java.util.Date updatedDate);

	/**
	 * query-page-by method for field updatedDate
	 * @param updatedDate the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified updatedDate
	 */
	CursorPage<DTrackPoint> queryPageByUpdatedDate(java.util.Date updatedDate,
            int pageSize, String cursorString);


		  
	// ----------------------- one-to-one finders -------------------------

	// ----------------------- many-to-one finders -------------------------
	
	// ----------------------- many-to-many finders -------------------------

	// ----------------------- uniqueFields finders -------------------------
	
	
	// ----------------------- populate / persist method -------------------------

	/**
	 * Persist an entity given all attributes
	 */
	DTrackPoint persist(Object raceKey,  	
		java.lang.Long id, 
		java.lang.Float elevation, 
		net.sf.mardao.core.geo.DLocation point, 
		java.lang.Long timestamp);	

}
