package com.example.projectmanager;


/**
 * Class reflecting an object held in database.
 */
public class Task {
    private long id;
    private long milestoneId;
    private String name;
    private String description;
    private long start;
    private long end;
    private int priority;
    private String phase;
    private int estimatedTime;
    
    private static final int DEFAULT_PRIORITY = 50;
    
    public Task(long id, long milestoneId, String name, String description,
    		long start, long end, int priority, String phase, int estimatedTime) {
    	
        this.id = id;
        this.milestoneId = milestoneId;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        
        this.phase = phase;
        this.estimatedTime = estimatedTime;
    }
 
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }

    public long getMilestoneId() {
        return milestoneId;
    }
 
    public void setMilestoneId(long milestoneId) {
        this.milestoneId = milestoneId;
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

    public int getPriority() {
        return priority;
    }
 
    /**
     * Attribute priority can range from 1 to 100, where 1 is the highest priority
     * and 100 is the lowest priority. If the argument passed to this setter does
     * not meet the priority requirements, then priority is assigned with default
     * value DEFAULT_PRIORITY.
     */
    public void setPriority(int priority) {
    	if (priority >= 1 && priority <= 100) {
            this.priority = priority;    		
    	} else {
            this.priority = DEFAULT_PRIORITY;
    	}
    }
        
    public String getPhase() {
        return phase;
    }
 
    public void setPhase(String phase) {
        this.phase = phase;
    }
    
    public int getEstimatedTime() {
        return estimatedTime;
    }
 
    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
