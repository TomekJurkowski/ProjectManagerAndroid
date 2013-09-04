package com.example.projectmanager.models;


/**
 * Class reflecting an object held in database.
 */
public class Milestone {
    private long id;
    private String name;
    private String description;
    private long start;
    private long end;
    private String phase;
    private long projectId;

    public Milestone(long id, String name, String description,
    		long start, long end, String phase, long projectId) {
    	
        this.id = id;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.phase = phase;
        this.projectId = projectId;
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
    
    public long getProjectId() {
        return projectId;
    }
 
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }
}