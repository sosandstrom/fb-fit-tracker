package com.wadpam.tracker.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;

import net.sf.mardao.core.domain.AbstractLongEntity;

/**
 * Created with IntelliJ IDEA.
 *
 * @author osandstrom
 * Date: 1/24/14 Time: 6:40 PM
 */
@Entity
public class DRace extends AbstractLongEntity {

  @Basic
  private String displayName;

  @Basic
  private String queryUrl;

  @Basic
  private String extractorClassname;

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getQueryUrl() {
    return queryUrl;
  }

  public void setQueryUrl(String queryUrl) {
    this.queryUrl = queryUrl;
  }

  public String getExtractorClassname() {
    return extractorClassname;
  }

  public void setExtractorClassname(String extractorClassname) {
    this.extractorClassname = extractorClassname;
  }
}
