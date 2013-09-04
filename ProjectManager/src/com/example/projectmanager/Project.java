package com.example.projectmanager;


/**
 * Class reflecting an object held in database.
 */
public class Project {
    private long id;
    private String name;
    private String description;
    private long start;
    private long end;
    private String phase;
    private boolean completed;
 
    public Project(long id, String name, String description, long start,
    		long end, String phase, boolean completed) {

    	this.id = id;
    	this.name = name;
    	this.description = description;
    	this.start = start;
    	this.end = end;
    	this.phase = phase;
    	this.completed = completed;
    }
 
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
     
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }

    public long getStart() {
        return start;
    }
 
    public void setStart(long start) {
        this.start = start;
    }
    
    public long getEnd() {
        return end;
    }
 
    public void setEnd(long end) {
        this.end = end;
    }

    public String getPhase() {
        return phase;
    }
 
    public void setPhase(String phase) {
        this.phase = phase;
    }

    public boolean isCompleted() {
        return completed;
    }
 
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}