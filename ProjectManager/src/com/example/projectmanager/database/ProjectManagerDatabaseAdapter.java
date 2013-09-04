package com.example.projectmanager.database;

import com.example.projectmanager.models.Project;
import com.example.projectmanager.models.Milestone;
import com.example.projectmanager.models.Task;
import android.content.Context;
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
 
    public static final String KEY_PROJECT_ID = "_id";
    public static final String PROJECT_ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int PROJECT_ID_COLUMN = 6;
    
    public static final String KEY_MILESTONE_ID = "_id";
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
            Log.d(DEBUG_TAG, "Table " + DB_PROJECT_TABLE+ " ver." + DB_VERSION + " created");
            Log.d(DEBUG_TAG, "Table " + DB_MILESTONE_TABLE+ " ver." + DB_VERSION + " created");
            Log.d(DEBUG_TAG, "Table " + DB_TASK_TABLE+ " ver." + DB_VERSION + " created");
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
	
    
    
    
    
}
