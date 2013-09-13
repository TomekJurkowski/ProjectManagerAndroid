package com.example.projectmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmanager.database.ProjectManagerDatabaseAdapter;
import com.example.projectmanager.models.Milestone;
import com.example.projectmanager.models.Task;

public class MilestoneActivity extends Activity {

	private static final int REQUEST_CODE = 1;
	
	private String start;
	private String end;
	
	private TextView name;
	private TextView description;
	private TextView date;
	private TextView phase;
	private ListView lvTasks;
	
	private Milestone milestone;
	private ProjectManagerDatabaseAdapter projectManagerDbAdapter;
    private Cursor taskCursor;
    private List<Task> tasks;
    private TasksAdapter listAdapter;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_milestone);
		// Show the Up button in the action bar.
		setupActionBar();
		
	    projectManagerDbAdapter = new ProjectManagerDatabaseAdapter(getApplicationContext());
	    projectManagerDbAdapter.open();

		Bundle b = getIntent().getExtras();
		long value = b.getLong("milestone_id");
		
		milestone = projectManagerDbAdapter.getMilestone(value);
	    if (milestone == null) {
			Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
			projectManagerDbAdapter.close();
			finish();
	    }
	    else {
	    	name = (TextView) findViewById(R.id.milestoneName);
	    	name.setText(milestone.getName());
	    	
	    	description = (TextView) findViewById(R.id.milestoneDescription);
	    	description.setText(milestone.getDescription());
	    	
	        start = milisecondsToDate(milestone.getStart());
	        end = milisecondsToDate(milestone.getEnd());	    	
	    	date = (TextView) findViewById(R.id.milestoneDate);
	    	date.setText(start + " - " + end);
	    	
	    	phase = (TextView) findViewById(R.id.milestonePhase);
	    	phase.setText(milestone.getPhase());
	    	
	    	lvTasks = (ListView) findViewById(R.id.lvTasks);
	    }
	    
	    initListView();
	}
	
    private String milisecondsToDate(long milisec) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisec);
        
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        String str = (day < 10) ? "0" : "";
        str += String.valueOf(day) + ".";
        str += (month < 10) ? "0" : "";
        str += String.valueOf(month) + ".";
        str += String.valueOf(year);
    	return str;
    }
    
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_project, menu);
		return false;
	}

	@Override
    protected void onDestroy() {
        if(projectManagerDbAdapter != null)
        	projectManagerDbAdapter.close();
        super.onDestroy();
    }
    
	/**
	 * Function that starts a new Activity to insert a new Task into the milestone.
	 */	
	public void btnAddTaskOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), NewTaskActivity.class);
        Bundle b = new Bundle();
        b.putLong("milestone_id", milestone.getId());
        b.putString("boundary", start + " - " + end);
        intent.putExtras(b);
        startActivityForResult(intent, REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			if (projectManagerDbAdapter != null) {
				projectManagerDbAdapter.close();
			}
			Intent intent = getIntent();
		    finish();
		    startActivity(intent);
		}
	}

	public void btnDeleteOnClick(View v) {
		LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
	    View popupView = layoutInflater.inflate(R.layout.are_you_sure_popup, null);  
	    final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	    Button btnYes = (Button) popupView.findViewById(R.id.yes);
    	btnYes.setOnClickListener(new Button.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			popupWindow.dismiss();
    			deleteMilestone();
    		}
    	});
		
	    Button btnNo = (Button) popupView.findViewById(R.id.no);
    	btnNo.setOnClickListener(new Button.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			popupWindow.dismiss();
    		}
    	});

    	popupWindow.setFocusable(true);
    	popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
	}
	
	private void deleteMilestone() {
		if (projectManagerDbAdapter != null) {
			deleteTasks(milestone.getId());
			projectManagerDbAdapter.deleteMilestone(milestone.getId());
			Toast.makeText(this, R.string.milestone_deleted, Toast.LENGTH_LONG).show();		
			projectManagerDbAdapter.close();
		} else {
			Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
		}
		
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();
	}
	
	/**
	 * Function expects an id of a milestone and it removes tasks from database that have
	 * attribute milestone_id equal to that id.
	 */	
	@SuppressWarnings("deprecation")
	private void deleteTasks(long milestoneId) {
		if (projectManagerDbAdapter != null) {
			Cursor c = projectManagerDbAdapter.getAllTasksFromMilestone(milestoneId);
			if (c != null) {
				startManagingCursor(c);
			}
		    if(c != null && c.moveToFirst()) {
		        do {
		        	long id = c.getLong(ProjectManagerDatabaseAdapter.ID_COLUMN);
		        	projectManagerDbAdapter.deleteTask(id);
		        } while (c.moveToNext());
		    }
		}
	}

	/**
	 * Function basically responsible initializing the ListView lvTasks object and
	 * filling  it with proper data.
	 */
	private void initListView() {
	    fillListViewData();
	    initListViewOnItemClick();
	}
	
	/**
	 * Function basically responsible for filling the content of ListView lvTasks.
	 */
	private void fillListViewData() {
	    getAllTasks();
	    listAdapter = new TasksAdapter(this, tasks);
	    lvTasks.setAdapter(listAdapter);
	}

	/**
	 * Function preparing the tasks list.
	 */
    private void getAllTasks() {
        tasks = new ArrayList<Task>();
        taskCursor = getAllEntriesFromDb();
        updateTaskList();
    }
 
	/**
	 * Function preparing a Cursor instance with tasks from database that have milestone_id field
	 * equal to milestone.getId().
	 */
    @SuppressWarnings("deprecation")
	private Cursor getAllEntriesFromDb() {
        taskCursor = projectManagerDbAdapter.getAllTasksFromMilestone(milestone.getId());
        if(taskCursor != null) {
            startManagingCursor(taskCursor);
            taskCursor.moveToFirst();
        }
        return taskCursor;
    }

	/**
	 * Function updating the tasks List based on taskCursor content.
	 */
    private void updateTaskList() {
        if(taskCursor != null && taskCursor.moveToFirst()) {
            do {
                long id = taskCursor.getLong(ProjectManagerDatabaseAdapter.ID_COLUMN);
	            String name = taskCursor.getString(ProjectManagerDatabaseAdapter.NAME_COLUMN);
	            String description = taskCursor.getString(ProjectManagerDatabaseAdapter.DESCRIPTION_COLUMN);
	            long start = taskCursor.getLong(ProjectManagerDatabaseAdapter.START_COLUMN);
	            long end = taskCursor.getLong(ProjectManagerDatabaseAdapter.END_COLUMN);	            
	            String phase = taskCursor.getString(ProjectManagerDatabaseAdapter.PHASE_COLUMN);
	            int priority = taskCursor.getInt(ProjectManagerDatabaseAdapter.PRIORITY_COLUMN);
	            int estimated = taskCursor.getInt(ProjectManagerDatabaseAdapter.ESTIMATED_TIME_COLUMN);
                long milestoneId= taskCursor.getLong(ProjectManagerDatabaseAdapter.MILESTONE_ID_COLUMN);
                tasks.add(new Task(id, name, description, start, end, phase, priority, estimated, milestoneId));
            } while(taskCursor.moveToNext());
        }
    }
 
    private void initListViewOnItemClick() {
        lvTasks.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {
//            	Task task = tasks.get(position);
            	Toast.makeText(getApplicationContext(), "You've just touched a task;)", Toast.LENGTH_SHORT).show();
                updateListViewData();
            }
        });
    }
 
	/**
	 * Function basically responsible for updating the content of ListView lvTasks.
	 */
    @SuppressWarnings("deprecation")
	private void updateListViewData() {
        taskCursor.requery();
        tasks.clear();
        updateTaskList();
        listAdapter.notifyDataSetChanged();
    }
}
