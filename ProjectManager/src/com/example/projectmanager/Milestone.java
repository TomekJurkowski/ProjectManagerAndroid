package com.example.projectmanager;


/**
 * Class reflecting an object held in database.
 */
public class Milestone {
    private long id;
    private long projectId;
    private String name;
    private String description;
    private long start;
    private long end;
    private String phase;
 
    public Milestone(long id, long projectId, String name, String description,
    		long start, long end, String phase) {
    	
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.phase = phase;
    }
 
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }
 
    public void setProjectId(long projectId) {
        this.projectId = projectId;
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
}