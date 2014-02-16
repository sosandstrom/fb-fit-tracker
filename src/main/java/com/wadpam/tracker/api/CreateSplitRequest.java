/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.tracker.api;

/**
 *
 * @author osandstrom
 */
public class CreateSplitRequest {
    private String elapsedSeconds;
    private String latLong;
    private String name;

    public String getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(String elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
