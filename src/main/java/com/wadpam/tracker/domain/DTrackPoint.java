package com.wadpam.tracker.domain;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import net.sf.mardao.core.GeoLocation;
import net.sf.mardao.core.Parent;

import net.sf.mardao.core.domain.AbstractLongEntity;
import net.sf.mardao.core.geo.DLocation;

/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom
 * Date: 1/24/14 Time: 6:40 PM
 */
@Entity
public class DTrackPoint extends AbstractLongEntity {

  @Parent(kind="DRace")
  private Object raceKey;
    
  @Basic
  //@GeoLocation
  private DLocation point;

  @Basic
  private Long timestamp;

  @Basic
  private Float elevation;

    public Object getRaceKey() {
        return raceKey;
    }

    public void setRaceKey(Object raceKey) {
        this.raceKey = raceKey;
    }

    public DLocation getPoint() {
        return point;
    }

    public void setPoint(DLocation point) {
        this.point = point;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Float getElevation() {
        return elevation;
    }

    public void setElevation(Float elevation) {
        this.elevation = elevation;
    }

}
