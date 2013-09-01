package com.example.projectmanager;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class NewProjectActivity extends Activity {
	private Spinner phaseSpinner;
	private DatePicker startDate;
	private DatePicker endDate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		// Show the Up button in the action bar.
		setupActionBar();
		
		startDate = (DatePicker) findViewById(R.id.new_project_start);
		endDate = (DatePicker) findViewById(R.id.new_project_end);
		
		phaseSpinner = (Spinner) findViewById(R.id.spinner_phase);
		
		// Creating adapter for spinner
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(
                this, R.array.phases, R.layout.spinner_layout);
 
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // attaching data adapter to spinner
        phaseSpinner.setAdapter(dataAdapter);
	}

	public void closeAndCreateProject () {
		Toast.makeText(this, R.string.correct_dates, Toast.LENGTH_LONG).show();			
        finish();
	}
	
	/**
	 * Function that verifies the data chosen by the user. It can either show a pop-up
	 * window with an information that some data are incorrect or (if all the data are
	 * proper) it will finish this activity and create the new project.
	 */	
	public void btnCreateNewProjectOnClick(View v) {
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, startDate.getYear());
	    cal.set(Calendar.MONTH, startDate.getMonth());
	    cal.set(Calendar.DAY_OF_MONTH, startDate.getDayOfMonth());
	    Date start = cal.getTime();
	    
	    cal.set(Calendar.YEAR, endDate.getYear());
	    cal.set(Calendar.MONTH, endDate.getMonth());
	    cal.set(Calendar.DAY_OF_MONTH, endDate.getDayOfMonth());
	    Date end = cal.getTime();
		
		if (start.before(end)) {
			closeAndCreateProject();
		} else if (start.equals(end)) {
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.equal_date_popup, null);  
		    final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		    Button btnYes = (Button) popupView.findViewById(R.id.yes);
	    	btnYes.setOnClickListener(new Button.OnClickListener() {
	    		@Override
	    		public void onClick(View v) {
	    			popupWindow.dismiss();
	    			closeAndCreateProject();
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
		} else {
			LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    View popupView = layoutInflater.inflate(R.layout.incorrect_date_popup, null);  
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

}
