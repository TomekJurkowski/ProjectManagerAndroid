package com.example.projectmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.projectmanager.models.Project;

public class ProjectActivity extends Activity {

	private static final int REQUEST_CODE = 1;
	
	private String start;
	private String end;
	
	private TextView name;
	private TextView description;
	private TextView date;
	private TextView phase;
	private ListView lvMilestones;
	
	private Project project;
	private ProjectManagerDatabaseAdapter projectManagerDbAdapter;
    private Cursor milestoneCursor;
    private List<Milestone> milestones;
    private MilestonesAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project);
		// Show the Up button in the action bar.
		setupActionBar();
		
	    projectManagerDbAdapter = new ProjectManagerDatabaseAdapter(getApplicationContext());
	    projectManagerDbAdapter.open();

		Bundle b = getIntent().getExtras();
		long value = b.getLong("project_id");
		
	    project = projectManagerDbAdapter.getProject(value);
	    if (project == null) {
			Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
			projectManagerDbAdapter.close();
			finish();
	    }
	    else {
	    	name = (TextView) findViewById(R.id.projectName);
	    	name.setText(project.getName());
	    	
	    	description = (TextView) findViewById(R.id.projectDescription);
	    	description.setText(project.getDescription());
	    	
	        start = milisecondsToDate(project.getStart());
	        end = milisecondsToDate(project.getEnd());	    	
	    	date = (TextView) findViewById(R.id.projectDate);
	    	date.setText(start + " - " + end);
	    	
	    	phase = (TextView) findViewById(R.id.projectPhase);
	    	phase.setText(project.getPhase());
	    	
	    	lvMilestones = (ListView) findViewById(R.id.lvMilestones);
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
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_project, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Function that marks the project as finished (it will now appear in "History" panel).
	 */	
	public void btnMoveToHistoryOnClick(View v) {
		project.setCompleted(true);
		if (projectManagerDbAdapter != null) {
			projectManagerDbAdapter.updateProject(project);
			Toast.makeText(this, R.string.project_moved_to_history, Toast.LENGTH_LONG).show();
			projectManagerDbAdapter.close();
		} else {
			Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
		}
		
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	/**
	 * Function that starts a new Activity to insert a new Milestone into the project.
	 */	
	public void btnAddMilestoneOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), NewMilestoneActivity.class);
        Bundle b = new Bundle();
        b.putLong("project_id", project.getId());
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
    			deleteProject();
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
	
	private void deleteProject() {
		if (projectManagerDbAdapter != null) {
			deleteMilestones(project.getId());
			projectManagerDbAdapter.deleteProject(project.getId());
			Toast.makeText(this, R.string.project_deleted, Toast.LENGTH_LONG).show();		
			projectManagerDbAdapter.close();
		} else {
			Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
		}
		
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();
	}
	
	/**
	 * Function expects an id of a project and it removes milestones from database that have
	 * attribute project_id equal to that id.
	 */	
	@SuppressWarnings("deprecation")
	private void deleteMilestones(long projectId) {
		if (projectManagerDbAdapter != null) {
			Cursor c = projectManagerDbAdapter.getAllTasksFromMilestone(projectId);
			if (c != null) {
				startManagingCursor(c);
			}
			if(c != null && c.moveToFirst()) {
		        do {
		        	long id = c.getLong(ProjectManagerDatabaseAdapter.ID_COLUMN);
		        	deleteTasks(id);
		        	projectManagerDbAdapter.deleteMilestone(id);
		        } while (c.moveToNext());
		    }
		}
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
	 * Function basically responsible initializing the ListView lvHistoryProjects object and
	 * filling  it with proper data.
	 */
	private void initListView() {
	    fillListViewData();
	    initListViewOnItemClick();
	}
	
	/**
	 * Function basically responsible for filling the content of ListView lvMilestones.
	 */
	private void fillListViewData() {
	    getAllMilestones();
	    listAdapter = new MilestonesAdapter(this, milestones);
	    lvMilestones.setAdapter(listAdapter);
	}
	
	/**
	 * Function preparing the milestones list.
	 */
    private void getAllMilestones() {
        milestones = new ArrayList<Milestone>();
        milestoneCursor = getAllEntriesFromDb();
        updateMilestoneList();
    }
 
	/**
	 * Function preparing a Cursor instance with Milestones from database that have project_id field
	 * equal to project.getId().
	 */
    @SuppressWarnings("deprecation")
	private Cursor getAllEntriesFromDb() {
        milestoneCursor = projectManagerDbAdapter.getAllMilestonesFromProject(project.getId());
        if(milestoneCursor != null) {
            startManagingCursor(milestoneCursor);
            milestoneCursor.moveToFirst();
        }
        return milestoneCursor;
    }
    
	/**
	 * Function updating the milestones List based on milestoneCursor content.
	 */
    private void updateMilestoneList() {
        if(milestoneCursor != null && milestoneCursor.moveToFirst()) {
            do {
                long id = milestoneCursor.getLong(ProjectManagerDatabaseAdapter.ID_COLUMN);
	            String name = milestoneCursor.getString(ProjectManagerDatabaseAdapter.NAME_COLUMN);
	            String description = milestoneCursor.getString(ProjectManagerDatabaseAdapter.DESCRIPTION_COLUMN);
	            long start = milestoneCursor.getLong(ProjectManagerDatabaseAdapter.START_COLUMN);
	            long end = milestoneCursor.getLong(ProjectManagerDatabaseAdapter.END_COLUMN);	            
	            String phase = milestoneCursor.getString(ProjectManagerDatabaseAdapter.PHASE_COLUMN);
                long projectId= milestoneCursor.getLong(ProjectManagerDatabaseAdapter.PROJECT_ID_COLUMN);
                milestones.add(new Milestone(id, name, description, start, end, phase, projectId));
            } while(milestoneCursor.moveToNext());
        }
    }
 
    @Override
    protected void onDestroy() {
        if(projectManagerDbAdapter != null)
        	projectManagerDbAdapter.close();
        super.onDestroy();
    }
    
    private void initListViewOnItemClick() {
        lvMilestones.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position,
                    long id) {
            	Milestone milestone = milestones.get(position);
            	Toast.makeText(getApplicationContext(), "You've just touched a milestone;)", Toast.LENGTH_SHORT).show();
                updateListViewData();
            }
        });
    }
 
	/**
	 * Function basically responsible for updating the content of ListView lvMilestones.
	 */
    @SuppressWarnings("deprecation")
	private void updateListViewData() {
        milestoneCursor.requery();
        milestones.clear();
        updateMilestoneList();
        listAdapter.notifyDataSetChanged();
    }
}
