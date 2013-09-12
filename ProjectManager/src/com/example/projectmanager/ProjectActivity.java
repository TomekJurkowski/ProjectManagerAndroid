package com.example.projectmanager;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmanager.database.ProjectManagerDatabaseAdapter;
import com.example.projectmanager.models.Project;

public class ProjectActivity extends Activity {

	private static final int REQUEST_CODE = 1;
	
	private TextView name;
	private TextView description;
	private TextView date;
	private TextView phase;
	
	private Project project;
	private ProjectManagerDatabaseAdapter projectManagerDbAdapter;
	
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
	    	
	        String start = milisecondsToDate(project.getStart());
	        String end = milisecondsToDate(project.getEnd());	    	
	    	date = (TextView) findViewById(R.id.projectDate);
	    	date.setText(start + " - " + end);
	    	
	    	phase = (TextView) findViewById(R.id.projectPhase);
	    	phase.setText(project.getPhase());
	    	
	    }
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
        intent.putExtras(b);
        startActivityForResult(intent, REQUEST_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			Toast.makeText(this, "TRALALALALA", Toast.LENGTH_LONG).show();
		}
	}

}
