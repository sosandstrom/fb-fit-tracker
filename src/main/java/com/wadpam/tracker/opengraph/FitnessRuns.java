package com.wadpam.tracker.opengraph;

/**
 *
 * @author osandstrom
 */
public class FitnessRuns extends StandardObject {
    
    private String course;
    private final String privacy = "{\"value\":\"SELF\"}";
    private final String start_time = "2014-02-02T11:34";
    private final int expires_in = 3600;
    
    public FitnessRuns() {
        //setType("fitness.runs");
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getStart_time() {
        return start_time;
    }

    public int getExpires_in() {
        return expires_in;
    }
    
    
}
