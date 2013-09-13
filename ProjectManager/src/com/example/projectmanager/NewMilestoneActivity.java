package com.example.projectmanager;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmanager.database.ProjectManagerDatabaseAdapter;
import com.example.projectmanager.models.Project;

public class NewMilestoneActivity extends Activity {
	private Spinner phaseSpinner;
	private DatePicker startDate;
	private DatePicker endDate;
	private EditText name;
	private EditText description;
	private TextView startTitle;
	private TextView endTitle;
	
	private String boundary;
	
	private Project project;
	private ProjectManagerDatabaseAdapter projectManagerDbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_milestone);
		// Show the Up button in the action bar.
		setupActionBar();
		
		name = (EditText) findViewById(R.id.new_milestone_name);
		description = (EditText) findViewById(R.id.new_milestone_description);		
		startDate = (DatePicker) findViewById(R.id.new_milestone_start);
		endDate = (DatePicker) findViewById(R.id.new_milestone_end);
		phaseSpinner = (Spinner) findViewById(R.id.spinner_phase);
		startTitle = (TextView) findViewById(R.id.new_milestone_start_title);
		endTitle = (TextView) findViewById(R.id.new_milestone_end_title);
		
		// Creating adapter for spinner
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(
                this, R.array.phases, R.layout.spinner_layout);
 
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // attaching data adapter to spinner
        phaseSpinner.setAdapter(dataAdapter);
        
		projectManagerDbAdapter = new ProjectManagerDatabaseAdapter(getApplicationContext());
	    projectManagerDbAdapter.open();
        
		Bundle b = getIntent().getExtras();
		long value = b.getLong("project_id");
		boundary = b.getString("boundary");
		
		startTitle.setText("Start Date\n(must be within " + boundary + "):");
		endTitle.setText("Start Date\n(must be within " + boundary + "):");
		
	    project = projectManagerDbAdapter.getProject(value);
	    if (project == null) {
			Toast.makeText(this, R.string.db_error, Toast.LENGTH_LONG).show();
			projectManagerDbAdapter.close();
			finish();
	    }
	}

	public void closeAndCreateMilestone() {
	    projectManagerDbAdapter.insertMilestone(name.getText().toString(), description.getText().toString(),
	    		startDate.getCalendarView().getDate(), endDate.getCalendarView().getDate(),
	    		phaseSpinner.getSelectedItem().toString(), project.getId());
		Toast.makeText(this, R.string.correct_dates, Toast.LENGTH_LONG).show();			
		projectManagerDbAdapter.close();
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		finish();
	}
	
	/**
	 * Function that verifies the data chosen by the user. It can either show a pop-up
	 * window with an information that some data are incorrect or (if all the data are
	 * proper) it will finish this activity and create the new project.
	 */	
	public void btnCreateNewMilestoneOnClick(View v) {
	    Calendar calStart = Calendar.getInstance();
	    calStart.set(Calendar.YEAR, startDate.getYear());
	    calStart.set(Calendar.MONTH, startDate.getMonth());
	    calStart.set(Calendar.DAY_OF_MONTH, startDate.getDayOfMonth());
	    Date start = calStart.getTime();
	    
	    Calendar calEnd = Calendar.getInstance();
	    calEnd.set(Calendar.YEAR, endDate.getYear());
	    calEnd.set(Calendar.MONTH, endDate.getMonth());
	    calEnd.set(Calendar.DAY_OF_MONTH, endDate.getDayOfMonth());
	    Date end = calEnd.getTime();
		
	    if (isBeyondBoundaries(calStart, calEnd)) {
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.date_out_of_range_popup_m, null);
		    final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		    Button btnOk = (Button) popupView.findViewById(R.id.dismiss);
	    	btnOk.setOnClickListener(new Button.OnClickListener() {
	    		@Override
	    		public void onClick(View v) {
	    			popupWindow.dismiss();
	    		}
	    	});

		    popupWindow.setFocusable(true);
	    	popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    	} else if (start.equals(end)) {
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.equal_date_popup_m, null);  
		    final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		    Button btnYes = (Button) popupView.findViewById(R.id.yes);
	    	btnYes.setOnClickListener(new Button.OnClickListener() {
	    		@Override
	    		public void onClick(View v) {
	    			popupWindow.dismiss();
	    			closeAndCreateMilestone();
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
		} else if (start.before(end)) {
			closeAndCreateMilestone();
		} else {
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.incorrect_date_popup_m, null);
		    final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		    Button btnOk = (Button) popupView.findViewById(R.id.dismiss);
	    	btnOk.setOnClickListener(new Button.OnClickListener() {
	    		@Override
	    		public void onClick(View v) {
	    			popupWindow.dismiss();
	    		}
	    	});

	    	popupWindow.setFocusable(true);
	    	popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		}

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
		getMenuInflater().inflate(R.menu.new_milestone, menu);
		return false;
	}

	/**
	 * Function that checks whether the date range selected by the user for the milestone doesn't
	 * go beyond the project time boundaries.
	 */
	private boolean isBeyondBoundaries(Calendar startM, Calendar endM) {
	    return isBeyondStartBoundaries(startM) || isBeyondEndBoundaries(endM);
	}
	
	/**
	 * Function that checks whether the start date selected by the user for the milestone doesn't
	 * go beyond the project time boundaries.
	 */
	private boolean isBeyondStartBoundaries(Calendar startM) {
	    Calendar startP = Calendar.getInstance();
	    startP.setTime(new Date(project.getStart()));
	    
	    if (startP.get(Calendar.YEAR) > startM.get(Calendar.YEAR)) {
	    	return true;
	    } else if (startP.get(Calendar.YEAR) < startM.get(Calendar.YEAR)) {
	    	return false;
	    }
	    
	    // startP.get(Calendar.YEAR) == startM.get(Calendar.YEAR)
	    
	    if (startP.get(Calendar.MONTH) > startM.get(Calendar.MONTH)) {
	    	return true;
	    } else if (startP.get(Calendar.MONTH) < startM.get(Calendar.MONTH)) {
	    	return false;
	    }

	    // startP.get(Calendar.YEAR) == startM.get(Calendar.YEAR)
	    // && startP.get(Calendar.MONTH) == startM.get(Calendar.MONTH)
	    
	    
	    if (startP.get(Calendar.DAY_OF_MONTH) > startM.get(Calendar.DAY_OF_MONTH)) {
	    	return true;
	    }
	    
	    return false;
	}
	
	/**
	 * Function that checks whether the end date selected by the user for the milestone doesn't
	 * go beyond the project time boundaries.
	 */
	private boolean isBeyondEndBoundaries(Calendar endM) {
	    Calendar endP = Calendar.getInstance();
	    endP.setTime(new Date(project.getEnd()));
	
	    if (endP.get(Calendar.YEAR) < endM.get(Calendar.YEAR)) {
	    	return true;
	    } else if (endP.get(Calendar.YEAR) > endM.get(Calendar.YEAR)) {
	    	return false;
	    }
	    
	    // endP.get(Calendar.YEAR) == endM.get(Calendar.YEAR)
	    
	    if (endP.get(Calendar.MONTH) < endM.get(Calendar.MONTH)) {
	    	return true;
	    } else if (endP.get(Calendar.MONTH) > endM.get(Calendar.MONTH)) {
	    	return false;
	    }

	    // endP.get(Calendar.YEAR) == endM.get(Calendar.YEAR)
	    // && endP.get(Calendar.MONTH) == endM.get(Calendar.MONTH)
	    
	    
	    if (endP.get(Calendar.DAY_OF_MONTH) < endM.get(Calendar.DAY_OF_MONTH)) {
	    	return true;
	    }

	    return false;	
	}
	
	
	
	
}
