package com.example.projectmanager.database;

import com.example.projectmanager.models.Project;
import com.example.projectmanager.models.Milestone;
import com.example.projectmanager.models.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProjectManagerDatabaseAdapter {
	private static final String DEBUG_TAG = "SqLiteProjectManager";
	 
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    private static final String DB_PROJECT_TABLE = "project";
    private static final String DB_MILESTONE_TABLE = "milestone";
    private static final String DB_TASK_TABLE = "task";
    
    /**
     * The following static final class members describe columns for tables
     * project, milestone and task.
     */
    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    
    public static final String KEY_NAME = "name";
    public static final String NAME_OPTIONS = "TEXT NOT NULL";
    public static final int NAME_COLUMN = 1;
    
    public static final String KEY_DESCRIPTION = "description";
    public static final String DESCRIPTION_OPTIONS = "TEXT";
    public static final int DESCRIPTION_COLUMN = 2;
    
    public static final String KEY_START = "start";
    public static final String START_OPTIONS = "INTEGER";
    public static final int START_COLUMN = 3;
    
    public static final String KEY_END = "end";
    public static final String END_OPTIONS = "INTEGER";
    public static final int END_COLUMN = 4;
    
    public static final String KEY_PHASE = "phase";
    public static final String PHASE_OPTIONS = "TEXT NOT NULL";
    public static final int PHASE_COLUMN = 5;
    
    public static final String KEY_PRIORITY = "priority";
    public static final String PRIORITY_OPTIONS = "INTEGER DEFAULT 50";
    public static final int PRIORITY_COLUMN = 6;
    
    public static final String KEY_ESTIMATED_TIME = "estimated_time";
    public static final String ESTIMATED_TIME_OPTIONS = "INTEGER";
    public static final int ESTIMATED_TIME_COLUMN = 7;
    
    public static final String KEY_COMPLETED = "completed";
    public static final String COMPLETED_OPTIONS = "INTEGER DEFAULT 0";
    public static final int COMPLETED_COLUMN = 6;
 
    public static final String KEY_PROJECT_ID = "project_id";
    public static final String PROJECT_ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int PROJECT_ID_COLUMN = 6;
    
    public static final String KEY_MILESTONE_ID = "milestone_id";
    public static final String MILESTONE_ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int MILESTONE_ID_COLUMN = 8;
    
    /**
     * The following static final strings are sql statement to create and drop tables.
     */    
    private static final String DB_CREATE_PROJECT_TABLE =
            "CREATE TABLE " + DB_PROJECT_TABLE + "( " +
            KEY_ID + " " + ID_OPTIONS + ", " +
            KEY_NAME + " " + NAME_OPTIONS + ", " +
            KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS + ", " +
            KEY_START + " " + START_OPTIONS + ", " +
            KEY_END + " " + END_OPTIONS + ", " +
            KEY_PHASE + " " + PHASE_OPTIONS + ", " +
            KEY_COMPLETED + " " + COMPLETED_OPTIONS +
            ");";

    private static final String DB_CREATE_MILESTONE_TABLE =
            "CREATE TABLE " + DB_PROJECT_TABLE + "( " +
            KEY_ID + " " + ID_OPTIONS + ", " +
            KEY_NAME + " " + NAME_OPTIONS + ", " +
            KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS + ", " +
            KEY_START + " " + START_OPTIONS + ", " +
            KEY_END + " " + END_OPTIONS + ", " +
            KEY_PHASE + " " + PHASE_OPTIONS + ", " +
            KEY_PROJECT_ID + " " + PROJECT_ID_OPTIONS +
            ");";
    
    private static final String DB_CREATE_TASK_TABLE =
            "CREATE TABLE " + DB_TASK_TABLE + "( " +
            KEY_ID + " " + ID_OPTIONS + ", " +
            KEY_NAME + " " + NAME_OPTIONS + ", " +
            KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS + ", " +
            KEY_START + " " + START_OPTIONS + ", " +
            KEY_END + " " + END_OPTIONS + ", " +
            KEY_PHASE + " " + PHASE_OPTIONS + ", " +
            KEY_PRIORITY + " " + PRIORITY_OPTIONS +
            KEY_ESTIMATED_TIME + " " + ESTIMATED_TIME_OPTIONS +
            KEY_MILESTONE_ID + " " + MILESTONE_ID_OPTIONS +
            ");";
    
    private static final String DROP_PROJECT_TABLE =
            "DROP TABLE IF EXISTS " + DB_PROJECT_TABLE;
    private static final String DROP_MILESTONE_TABLE =
            "DROP TABLE IF EXISTS " + DB_MILESTONE_TABLE;
    private static final String DROP_TASK_TABLE =
            "DROP TABLE IF EXISTS " + DB_TASK_TABLE;
 
    
    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;
 
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
 
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_PROJECT_TABLE);
            db.execSQL(DB_CREATE_MILESTONE_TABLE);
            db.execSQL(DB_CREATE_TASK_TABLE);
 
            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_PROJECT_TABLE + " ver." + DB_VERSION + " created");
            Log.d(DEBUG_TAG, "Table " + DB_MILESTONE_TABLE + " ver." + DB_VERSION + " created");
            Log.d(DEBUG_TAG, "Table " + DB_TASK_TABLE + " ver." + DB_VERSION + " created");
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	db.execSQL(DROP_TASK_TABLE);
            db.execSQL(DROP_MILESTONE_TABLE);
            db.execSQL(DROP_PROJECT_TABLE);
 
            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_TASK_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "Table " + DB_MILESTONE_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "Table " + DB_PROJECT_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");
 
            onCreate(db);
        }
    }
	
    public ProjectManagerDatabaseAdapter(Context context) {
        this.context = context;
    }
 
    public ProjectManagerDatabaseAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }
 
    public void close() {
        dbHelper.close();
    }
    
    /**
     * Function responsible for inserting a new row into the project table in database.
     */
    public long insertProject(String name, String description, long start, long end,
    		String phase) {
    	
        ContentValues newProjectValues = new ContentValues();
        newProjectValues.put(KEY_NAME, name);
        newProjectValues.put(KEY_DESCRIPTION, description);
        newProjectValues.put(KEY_START, start);
        newProjectValues.put(KEY_END, end);
        newProjectValues.put(KEY_PHASE, phase);
        // We cannot create a completed Project - declaring a Project 'completed'
        // can only be managed on an already existing one.
        newProjectValues.put(KEY_COMPLETED, 0);
        return db.insert(DB_PROJECT_TABLE, null, newProjectValues);
    }

    /**
     * Function responsible for inserting a new row into the milestone table in database.
     */
    public long insertMilestone(String name, String description, long start, long end,
    		String phase, long projectId) {
    	
        ContentValues newMilestoneValues = new ContentValues();
        newMilestoneValues.put(KEY_NAME, name);
        newMilestoneValues.put(KEY_DESCRIPTION, description);
        newMilestoneValues.put(KEY_START, start);
        newMilestoneValues.put(KEY_END, end);
        newMilestoneValues.put(KEY_PHASE, phase);
        newMilestoneValues.put(KEY_PROJECT_ID, projectId);
        return db.insert(DB_PROJECT_TABLE, null, newMilestoneValues);
    }
    
    /**
     * Function responsible for inserting a new row into the task table in database.
     */
    public long insertTask(String name, String description, long start, long end,
    		String phase, int priority, int estimatedTime, long milestoneId) {
    	
        ContentValues newTaskValues = new ContentValues();
        newTaskValues.put(KEY_NAME, name);
        newTaskValues.put(KEY_DESCRIPTION, description);
        newTaskValues.put(KEY_START, start);
        newTaskValues.put(KEY_END, end);
        newTaskValues.put(KEY_PHASE, phase);
        newTaskValues.put(KEY_PRIORITY, priority);
        newTaskValues.put(KEY_ESTIMATED_TIME, estimatedTime);
        newTaskValues.put(KEY_MILESTONE_ID, milestoneId);
        return db.insert(DB_TASK_TABLE, null, newTaskValues);
    }
 
    /**
     * Function responsible for updating a single row from the project table in database
     * that corresponds to the project passed as an parameter.
     */
    public boolean updateProject(Project project) {
        return updateProject(project.getId(), project.getName(), project.getDescription(),
        		project.getStart(), project.getEnd(), project.getPhase(), project.isCompleted());
    }

    /**
     * Function responsible for updating a single row from the project table in database
     * that has the attribute _id equal to id passed as first parameter with values
     * passed name, description, start, end, phase and completed.
     */
    public boolean updateProject(long id, String name, String description, long start,
    		long end, String phase, boolean completed) {
    	
        String where = KEY_ID + "=" + id;
        ContentValues updateProjectValues = new ContentValues();
        updateProjectValues.put(KEY_NAME, name);
        updateProjectValues.put(KEY_DESCRIPTION, description);
        updateProjectValues.put(KEY_START, start);
        updateProjectValues.put(KEY_END, end);
        updateProjectValues.put(KEY_PHASE, phase);
        updateProjectValues.put(KEY_COMPLETED, (completed ? 1 : 0));
        return db.update(DB_PROJECT_TABLE, updateProjectValues, where, null) > 0;
    }

    /**
     * Function responsible for updating a single row from the milestone table in database
     * that corresponds to the milestone passed as an parameter.
     */
    public boolean updateMilestone(Milestone milestone) {
        return updateMilestone(milestone.getId(), milestone.getName(), milestone.getDescription(),
        		milestone.getStart(), milestone.getEnd(), milestone.getPhase(), milestone.getProjectId());
    }

    /**
     * Function responsible for updating a single row from the milestone table in database
     * that has the attribute _id equal to id passed as first parameter with values
     * passed name, description, start, end, phase and projectId.
     */
    public boolean updateMilestone(long id, String name, String description, long start,
    		long end, String phase, long projectId) {
    	
        String where = KEY_ID + "=" + id;
        ContentValues updateMilestoneValues = new ContentValues();
        updateMilestoneValues.put(KEY_NAME, name);
        updateMilestoneValues.put(KEY_DESCRIPTION, description);
        updateMilestoneValues.put(KEY_START, start);
        updateMilestoneValues.put(KEY_END, end);
        updateMilestoneValues.put(KEY_PHASE, phase);
        updateMilestoneValues.put(KEY_PROJECT_ID, projectId);
        return db.update(DB_MILESTONE_TABLE, updateMilestoneValues, where, null) > 0;
    }
    
    /**
     * Function responsible for updating a single row from the task table in database
     * that corresponds to the task passed as an parameter.
     */
    public boolean updateTask(Task task) {
        return updateTask(task.getId(), task.getName(), task.getDescription(), task.getStart(),
        		task.getEnd(), task.getPhase(), task.getPriority(), task.getEstimatedTime(),
        		task.getMilestoneId());
    }

    /**
     * Function responsible for updating a single row from the task table in database
     * that has the attribute _id equal to id passed as first parameter with values
     * passed name, description, start, end, phase, priority, estimatedTime and milestoneId.
     */
    public boolean updateTask(long id, String name, String description, long start,
    		long end, String phase, int priority, int estimatedTime, long milestoneId) {
    	
        String where = KEY_ID + "=" + id;
        ContentValues updateTaskValues = new ContentValues();
        updateTaskValues.put(KEY_NAME, name);
        updateTaskValues.put(KEY_DESCRIPTION, description);
        updateTaskValues.put(KEY_START, start);
        updateTaskValues.put(KEY_END, end);
        updateTaskValues.put(KEY_PHASE, phase);
        updateTaskValues.put(KEY_MILESTONE_ID, milestoneId);
        return db.update(DB_TASK_TABLE, updateTaskValues, where, null) > 0;
    }    
    
    /**
     * Function responsible for deleting a single row from the project table in database
     * by the id.
     */
    public boolean deleteProject(long id) {
        String where = KEY_ID + "=" + id;
        return db.delete(DB_PROJECT_TABLE, where, null) > 0;
    }
    
    /**
     * Function responsible for deleting a single row from the milestone table in database
     * by the id.
     */
    public boolean deleteMilestone(long id) {
        String where = KEY_ID + "=" + id;
        return db.delete(DB_MILESTONE_TABLE, where, null) > 0;
    }
    
    /**
     * Function responsible for deleting a single row from the task table in database
     * by the id.
     */
    public boolean deleteTask(long id) {
        String where = KEY_ID + "=" + id;
        return db.delete(DB_TASK_TABLE, where, null) > 0;
    }
    
    
}
