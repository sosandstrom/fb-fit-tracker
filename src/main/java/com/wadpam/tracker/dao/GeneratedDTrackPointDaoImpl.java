package com.wadpam.tracker.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import net.sf.mardao.core.CursorPage;
import net.sf.mardao.core.Filter;
import net.sf.mardao.core.dao.DaoImpl;
import net.sf.mardao.core.dao.TypeDaoImpl;
import net.sf.mardao.core.geo.DLocation;
import com.wadpam.tracker.domain.DTrackPoint;

/**
 * The DTrackPoint domain-object specific finders and methods go in this POJO.
 * 
 * Generated on 2014-02-17T10:18:57.703+0100.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class GeneratedDTrackPointDaoImpl extends TypeDaoImpl<DTrackPoint, java.lang.Long> 
	implements GeneratedDTrackPointDao {


    /** to list the property names for ManyToOne relations */
    @Override
    protected List<String> getBasicColumnNames() {
        return BASIC_NAMES;
    }

    /** to list the property names for ManyToOne relations */
    @Override
    protected List<String> getManyToOneColumnNames() {
        return MANY_TO_ONE_NAMES;
    }

    private final Map<String, DaoImpl> MANY_TO_ONE_DAOS = new TreeMap<String, DaoImpl>();

    /** Default constructor */
   public GeneratedDTrackPointDaoImpl() {
      super(DTrackPoint.class, java.lang.Long.class);
   }

   // ------ BEGIN DaoImpl overrides -----------------------------
   
   public String getPrimaryKeyColumnName() {
   		return COLUMN_NAME_ID;
   }
   
   public List<String> getColumnNames() {
        return COLUMN_NAMES;
   }

   @Override
   protected DaoImpl getManyToOneDao(String columnName) {
       return MANY_TO_ONE_DAOS.get(columnName);
   }

    @Override
    protected Object getDomainProperty(DTrackPoint domain, String name) {
        Object value;
        // simple key?
        if (COLUMN_NAME_ID.equals(name)) {
            value = domain.getId();
        }
        // parent key?
        else if (COLUMN_NAME_RACEKEY.equals(name)) {
            value = domain.getRaceKey();
        }
        // fields
        else if (COLUMN_NAME_CREATEDBY.equals(name)) {
            value = domain.getCreatedBy();
        }
        else if (COLUMN_NAME_CREATEDDATE.equals(name)) {
            value = domain.getCreatedDate();
        }
        else if (COLUMN_NAME_ELEVATION.equals(name)) {
            value = domain.getElevation();
        }
        else if (COLUMN_NAME_POINT.equals(name)) {
            value = domain.getPoint();
        }
        else if (COLUMN_NAME_TIMESTAMP.equals(name)) {
            value = domain.getTimestamp();
        }
        else if (COLUMN_NAME_UPDATEDBY.equals(name)) {
            value = domain.getUpdatedBy();
        }
        else if (COLUMN_NAME_UPDATEDDATE.equals(name)) {
            value = domain.getUpdatedDate();
        }
        // one-to-ones
        // many-to-ones
        // many-to-manys
        else {
            value = super.getDomainProperty(domain, name);
        }

        return value;
    }

    /**
     * Returns the class of the domain property for specified column
     * @param name
     * @return the class of the domain property
     */
    public Class getColumnClass(String name) {
        Class clazz;
        // simple key?
        if (COLUMN_NAME_ID.equals(name)) {
            clazz = java.lang.Long.class;
        }
        // parent key?
        else if (COLUMN_NAME_RACEKEY.equals(name)) {
            clazz = java.lang.Object.class;
        }
        // fields
        else if (COLUMN_NAME_CREATEDBY.equals(name)) {
            clazz = java.lang.String.class;
        }
        else if (COLUMN_NAME_CREATEDDATE.equals(name)) {
            clazz = java.util.Date.class;
        }
        else if (COLUMN_NAME_ELEVATION.equals(name)) {
            clazz = java.lang.Float.class;
        }
        else if (COLUMN_NAME_POINT.equals(name)) {
            clazz = net.sf.mardao.core.geo.DLocation.class;
        }
        else if (COLUMN_NAME_TIMESTAMP.equals(name)) {
            clazz = java.lang.Long.class;
        }
        else if (COLUMN_NAME_UPDATEDBY.equals(name)) {
            clazz = java.lang.String.class;
        }
        else if (COLUMN_NAME_UPDATEDDATE.equals(name)) {
            clazz = java.util.Date.class;
        }
        // one-to-ones
        // many-to-ones
        // many-to-manys
        else {
            throw new IllegalArgumentException("No such column " + name);
        }

        return clazz;
    }
      
    @Override
    protected void setDomainProperty(final DTrackPoint domain, final String name, final Object value) {
        // simple key?
        if (COLUMN_NAME_ID.equals(name)) {
            domain.setId((java.lang.Long) value);
        }
        // parent key?
        else if (COLUMN_NAME_RACEKEY.equals(name)) {
            domain.setRaceKey((java.lang.Object) value);
        }
        // fields
        else if (COLUMN_NAME_CREATEDBY.equals(name)) {
            domain.setCreatedBy((java.lang.String) value);
        }
        else if (COLUMN_NAME_CREATEDDATE.equals(name)) {
            domain.setCreatedDate((java.util.Date) value);
        }
        else if (COLUMN_NAME_ELEVATION.equals(name)) {
            domain.setElevation((java.lang.Float) value);
        }
        else if (COLUMN_NAME_POINT.equals(name)) {
            domain.setPoint((net.sf.mardao.core.geo.DLocation) value);
        }
        else if (COLUMN_NAME_TIMESTAMP.equals(name)) {
            domain.setTimestamp((java.lang.Long) value);
        }
        else if (COLUMN_NAME_UPDATEDBY.equals(name)) {
            domain.setUpdatedBy((java.lang.String) value);
        }
        else if (COLUMN_NAME_UPDATEDDATE.equals(name)) {
            domain.setUpdatedDate((java.util.Date) value);
        }
        // one-to-ones
        // many-to-ones
        // many-to-manys
        else {
            super.setDomainProperty(domain, name, value);
        }
    }

    @Override
    protected void setDomainStringProperty(final DTrackPoint domain, final String name, final Map<String, String> properties) {
        final String value = properties.get(name);
        Class clazz = getColumnClass(name);
        // many-to-ones
        setDomainProperty(domain, name, parseProperty(value, clazz));
    }

    /**
     * Overrides to substitute Entity properties with foreign keys
     */
    @Override
    protected void setCoreProperty(Object core, String name, Object value) {
        if (null == core || null == name) {
            return;
        }
        else if (null == value) {
            // do nothing in particular, will call super at end
        }
        super.setCoreProperty(core, name, value);
    }

    /** Default implementation returns null, overrides for raceKey parent */
    public String getParentKeyColumnName() {
        return COLUMN_NAME_RACEKEY;
    }

   // ------ END DaoImpl overrides -----------------------------

        public Object getParentKey(DTrackPoint domain) {
            return domain.getRaceKey();
        }

        public void setParentKey(DTrackPoint domain, Object raceKey) {
            domain.setRaceKey((Serializable) raceKey);
        }

	// ----------------------- parent finders -------------------------------

	/**
	 * query-by method for parent field raceKey
	 * @param raceKey the specified attribute
	 * @return an Iterable of DTrackPoints for the specified parent
	 */
	public final Iterable<DTrackPoint> queryByRaceKey(Object raceKey) {
            return queryIterable(false, 0, -1, raceKey, null, null, false, null, false);
	}
	
	/**
	 * query-key-by method for parent field raceKey
	 * @param raceKey the parent
	 * @return an Iterable of keys to the DTrackPoints with the specified parent
	 */
	public final Iterable<java.lang.Long> queryKeysByRaceKey(Object raceKey) {
            return queryIterableKeys(0, -1, raceKey, null, null, false, null, false);
	}

	/**
	 * query-page-by method for parent field raceKey
	 * @param raceKey the specified parent
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified raceKey
	 */
	public final CursorPage<DTrackPoint> queryPageByRaceKey(java.lang.Object raceKey,
            int pageSize, String cursorString) {
            return queryPage(false, pageSize, raceKey, null, null, false, null, false, cursorString);
        }


        /**
         * @return the simple key for specified DTrackPoint domain object
         */
        public Long getSimpleKey(DTrackPoint domain) {
            if (null == domain) {
                return null;
            }
            return domain.getId();
        }

        /**
         * @return the simple key for specified DTrackPoint domain object
         */
        public void setSimpleKey(DTrackPoint domain, Long id) {
            domain.setId(id);
        }

        public String getCreatedByColumnName() {
            return COLUMN_NAME_CREATEDBY;
        }

        public String getCreatedBy(DTrackPoint domain) {
            if (null == domain) {
                return null;
            }
            return domain.getCreatedBy();
        }

        public void _setCreatedBy(DTrackPoint domain, String creator) {
            domain.setCreatedBy(creator);
        }

        public String getUpdatedByColumnName() {
            return COLUMN_NAME_UPDATEDBY;
        }

        public String getUpdatedBy(DTrackPoint domain) {
            if (null == domain) {
                return null;
            }
            return domain.getUpdatedBy();
        }

        public void _setUpdatedBy(DTrackPoint domain, String updator) {
            domain.setUpdatedBy(updator);
        }

        public String getCreatedDateColumnName() {
            return COLUMN_NAME_CREATEDDATE;
        }

        public Date getCreatedDate(DTrackPoint domain) {
            if (null == domain) {
                return null;
            }
            return domain.getCreatedDate();
        }

        public void _setCreatedDate(DTrackPoint domain, Date date) {
            domain.setCreatedDate(date);
        }

        public String getUpdatedDateColumnName() {
            return COLUMN_NAME_UPDATEDDATE;
        }

        public Date getUpdatedDate(DTrackPoint domain) {
            if (null == domain) {
                return null;
            }
            return domain.getUpdatedDate();
        }

        public void _setUpdatedDate(DTrackPoint domain, Date date) {
            domain.setUpdatedDate(date);
        }

	// ----------------------- field finders -------------------------------
	/**
         * {@inheritDoc}
	 */
	public final Iterable<DTrackPoint> queryByCreatedBy(java.lang.String createdBy) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_CREATEDBY, createdBy);
            return queryIterable(false, 0, -1, null, null, null, false, null, false, filter);
	}
	
	/**
	 * query-key-by method for attribute field createdBy
	 * @param createdBy the specified attribute
	 * @return an Iterable of keys to the DTrackPoints with the specified attribute
	 */
	public final Iterable<java.lang.Long> queryKeysByCreatedBy(java.lang.String createdBy) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_CREATEDBY, createdBy);
            return queryIterableKeys(0, -1, null, null, null, false, null, false, filter);
	}

	/**
	 * query-page-by method for field createdBy
	 * @param createdBy the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified createdBy
	 */
	public final CursorPage<DTrackPoint> queryPageByCreatedBy(java.lang.String createdBy,
            int pageSize, String cursorString) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_CREATEDBY, createdBy);
            return queryPage(false, pageSize, null, null, null, false, null, false, cursorString, filter);
        }

	 
	/**
         * {@inheritDoc}
	 */
	public final Iterable<DTrackPoint> queryByCreatedDate(java.util.Date createdDate) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_CREATEDDATE, createdDate);
            return queryIterable(false, 0, -1, null, null, null, false, null, false, filter);
	}
	
	/**
	 * query-key-by method for attribute field createdDate
	 * @param createdDate the specified attribute
	 * @return an Iterable of keys to the DTrackPoints with the specified attribute
	 */
	public final Iterable<java.lang.Long> queryKeysByCreatedDate(java.util.Date createdDate) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_CREATEDDATE, createdDate);
            return queryIterableKeys(0, -1, null, null, null, false, null, false, filter);
	}

	/**
	 * query-page-by method for field createdDate
	 * @param createdDate the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified createdDate
	 */
	public final CursorPage<DTrackPoint> queryPageByCreatedDate(java.util.Date createdDate,
            int pageSize, String cursorString) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_CREATEDDATE, createdDate);
            return queryPage(false, pageSize, null, null, null, false, null, false, cursorString, filter);
        }

	 
	/**
         * {@inheritDoc}
	 */
	public final Iterable<DTrackPoint> queryByElevation(java.lang.Float elevation) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_ELEVATION, elevation);
            return queryIterable(false, 0, -1, null, null, null, false, null, false, filter);
	}
	
	/**
	 * query-key-by method for attribute field elevation
	 * @param elevation the specified attribute
	 * @return an Iterable of keys to the DTrackPoints with the specified attribute
	 */
	public final Iterable<java.lang.Long> queryKeysByElevation(java.lang.Float elevation) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_ELEVATION, elevation);
            return queryIterableKeys(0, -1, null, null, null, false, null, false, filter);
	}

	/**
	 * query-page-by method for field elevation
	 * @param elevation the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified elevation
	 */
	public final CursorPage<DTrackPoint> queryPageByElevation(java.lang.Float elevation,
            int pageSize, String cursorString) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_ELEVATION, elevation);
            return queryPage(false, pageSize, null, null, null, false, null, false, cursorString, filter);
        }

	 
	/**
         * {@inheritDoc}
	 */
	public final Iterable<DTrackPoint> queryByPoint(net.sf.mardao.core.geo.DLocation point) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_POINT, point);
            return queryIterable(false, 0, -1, null, null, null, false, null, false, filter);
	}
	
	/**
	 * query-key-by method for attribute field point
	 * @param point the specified attribute
	 * @return an Iterable of keys to the DTrackPoints with the specified attribute
	 */
	public final Iterable<java.lang.Long> queryKeysByPoint(net.sf.mardao.core.geo.DLocation point) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_POINT, point);
            return queryIterableKeys(0, -1, null, null, null, false, null, false, filter);
	}

	/**
	 * query-page-by method for field point
	 * @param point the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified point
	 */
	public final CursorPage<DTrackPoint> queryPageByPoint(net.sf.mardao.core.geo.DLocation point,
            int pageSize, String cursorString) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_POINT, point);
            return queryPage(false, pageSize, null, null, null, false, null, false, cursorString, filter);
        }

	 
	/**
         * {@inheritDoc}
	 */
	public final Iterable<DTrackPoint> queryByTimestamp(java.lang.Long timestamp) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_TIMESTAMP, timestamp);
            return queryIterable(false, 0, -1, null, null, null, false, null, false, filter);
	}
	
	/**
	 * query-key-by method for attribute field timestamp
	 * @param timestamp the specified attribute
	 * @return an Iterable of keys to the DTrackPoints with the specified attribute
	 */
	public final Iterable<java.lang.Long> queryKeysByTimestamp(java.lang.Long timestamp) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_TIMESTAMP, timestamp);
            return queryIterableKeys(0, -1, null, null, null, false, null, false, filter);
	}

	/**
	 * query-page-by method for field timestamp
	 * @param timestamp the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified timestamp
	 */
	public final CursorPage<DTrackPoint> queryPageByTimestamp(java.lang.Long timestamp,
            int pageSize, String cursorString) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_TIMESTAMP, timestamp);
            return queryPage(false, pageSize, null, null, null, false, null, false, cursorString, filter);
        }

	 
	/**
         * {@inheritDoc}
	 */
	public final Iterable<DTrackPoint> queryByUpdatedBy(java.lang.String updatedBy) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_UPDATEDBY, updatedBy);
            return queryIterable(false, 0, -1, null, null, null, false, null, false, filter);
	}
	
	/**
	 * query-key-by method for attribute field updatedBy
	 * @param updatedBy the specified attribute
	 * @return an Iterable of keys to the DTrackPoints with the specified attribute
	 */
	public final Iterable<java.lang.Long> queryKeysByUpdatedBy(java.lang.String updatedBy) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_UPDATEDBY, updatedBy);
            return queryIterableKeys(0, -1, null, null, null, false, null, false, filter);
	}

	/**
	 * query-page-by method for field updatedBy
	 * @param updatedBy the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified updatedBy
	 */
	public final CursorPage<DTrackPoint> queryPageByUpdatedBy(java.lang.String updatedBy,
            int pageSize, String cursorString) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_UPDATEDBY, updatedBy);
            return queryPage(false, pageSize, null, null, null, false, null, false, cursorString, filter);
        }

	 
	/**
         * {@inheritDoc}
	 */
	public final Iterable<DTrackPoint> queryByUpdatedDate(java.util.Date updatedDate) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_UPDATEDDATE, updatedDate);
            return queryIterable(false, 0, -1, null, null, null, false, null, false, filter);
	}
	
	/**
	 * query-key-by method for attribute field updatedDate
	 * @param updatedDate the specified attribute
	 * @return an Iterable of keys to the DTrackPoints with the specified attribute
	 */
	public final Iterable<java.lang.Long> queryKeysByUpdatedDate(java.util.Date updatedDate) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_UPDATEDDATE, updatedDate);
            return queryIterableKeys(0, -1, null, null, null, false, null, false, filter);
	}

	/**
	 * query-page-by method for field updatedDate
	 * @param updatedDate the specified attribute
         * @param pageSize the number of domain entities in the page
         * @param cursorString non-null if get next page
	 * @return a Page of DTrackPoints for the specified updatedDate
	 */
	public final CursorPage<DTrackPoint> queryPageByUpdatedDate(java.util.Date updatedDate,
            int pageSize, String cursorString) {
            final Filter filter = createEqualsFilter(COLUMN_NAME_UPDATEDDATE, updatedDate);
            return queryPage(false, pageSize, null, null, null, false, null, false, cursorString, filter);
        }

	 
	// ----------------------- one-to-one finders -------------------------

	// ----------------------- many-to-one finders -------------------------

	// ----------------------- many-to-many finders -------------------------

	// ----------------------- uniqueFields finders -------------------------

	// ----------------------- populate / persist method -------------------------

	/**
	 * Persist an entity given all attributes
	 */
	public DTrackPoint persist(Object raceKey,  	
		java.lang.Long id, 
		java.lang.Float elevation, 
		net.sf.mardao.core.geo.DLocation point, 
		java.lang.Long timestamp) {

            DTrackPoint domain = null;
            // if primaryKey specified, use it
            if (null != id) {
                    domain = findByPrimaryKey(raceKey, id);
            }
		
            // create new?
            if (null == domain) {
                    domain = new DTrackPoint();
                    // set parent
                    domain.setRaceKey((java.lang.Object) raceKey);
                    // generate Id?
                    if (null != id) {
                            domain.setId(id);
                    }
                    // fields
                    domain.setElevation(elevation);
                    domain.setPoint(point);
                    domain.setTimestamp(timestamp);
                    // one-to-ones
                    // many-to-ones
			
                    persist(domain);
            }
            return domain;
	}



}
