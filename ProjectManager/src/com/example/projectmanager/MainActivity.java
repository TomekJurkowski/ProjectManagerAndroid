package com.example.projectmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectmanager.database.ProjectManagerDatabaseAdapter;
import com.example.projectmanager.models.Project;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	private static final int NUM_PAGES = 5;
	
	private static final int MENU = 0;
	private static final int CURRENT_PROJECTS = 1;
	private static final int HISTORY = 2;
	private static final int DESCRIPTION = 3;
	private static final int INFO = 4;
    
    private ProjectManagerDatabaseAdapter projectManagerDbAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each primary
		// sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
	    projectManagerDbAdapter = new ProjectManagerDatabaseAdapter(this.getApplicationContext());
	    projectManagerDbAdapter.open();
	}

    @Override
    protected void onDestroy() {
        if(projectManagerDbAdapter != null)
        	projectManagerDbAdapter.close();
        super.onDestroy();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment;
			switch (position) {
			case MENU:
				fragment = new MenuSectionFragment();
				break;
			case CURRENT_PROJECTS:
				fragment = new CurrentProjectSectionFragment();
				break;
			case HISTORY:
				fragment = new HistorySectionFragment();
				break;
			case DESCRIPTION:
				fragment = new DescriptionSectionFragment();
				break;
			case INFO:
				fragment = new InfoSectionFragment();
				break;
			default:
				fragment = new MenuSectionFragment();
				break;
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case MENU:
				return getString(R.string.title_section1).toUpperCase(l);
			case CURRENT_PROJECTS:
				return getString(R.string.title_section2).toUpperCase(l);
			case HISTORY:
				return getString(R.string.title_section3).toUpperCase(l);
			case DESCRIPTION:
				return getString(R.string.title_section4).toUpperCase(l);
			case INFO:
				return getString(R.string.title_section5).toUpperCase(l);
			}
			return null;
		}
	}
	
	/**
	 * Function that tries to start the activity and catches the error if the
	 * activity is not installed.
	 */
	private boolean StartActivity(Intent aIntent) {
	    try
	    {
	        startActivity(aIntent);
	        return true;
	    }
	    catch (ActivityNotFoundException e)
	    {
	        return false;
	    }
	}
	
	/**
	 * Function that tries to start the Play Market activity. It tries to open
	 * the url of Project Manager App in Play Market.
	 */	
	public void btnRateAppOnClick(View v) {
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.setData(Uri.parse("market://"));
	    if (StartActivity(intent) == false) {
	        intent.setData(Uri.parse("https://play.google.com/store/apps/"));
	        if (StartActivity(intent) == false) {
	            Toast.makeText(this, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
	        }
	    }
	}
	
	private static final int REQUEST_CODE = 1;
	
	/**
	 * Function that starts the NewProjectActivity.
	 */	
	public void btnNewProjectOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), NewProjectActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			mViewPager.setAdapter(mSectionsPagerAdapter);
		}
	}
	
	public static class MenuSectionFragment extends Fragment {
		
		public MenuSectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
			
			TextView titleTextView = (TextView) rootView.findViewById(R.id.section_label);
			titleTextView.setText(R.string.menu_title);
									
			return rootView;
		}
	}
	
//	public static class CurrentProjectSectionFragment extends Fragment {
//
//		public CurrentProjectSectionFragment() {
//		}
//		
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main_current_projects, container, false);
//			
//			TextView titleTextView = (TextView) rootView.findViewById(R.id.section_label);
//			if (true) {
//				titleTextView.setText(R.string.no_current_project);
//				titleTextView.setTextSize(30);
//			} else {
//				titleTextView.setText(R.string.current_projects_title);				
//			}
//						
//			return rootView;
//		}
//	}
	
//	public static class HistorySectionFragment extends Fragment {
//
//		public HistorySectionFragment() {
//		}
//		
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main_history, container, false);
//			
//			TextView titleTextView = (TextView) rootView.findViewById(R.id.section_label);
//			if (true) {
//				titleTextView.setText(R.string.no_finished_project);
//				titleTextView.setTextSize(30);
//			} else {
//				titleTextView.setText(R.string.finished_projects_title);				
//			}
//						
//			return rootView;
//		}
//	}

	public static class DescriptionSectionFragment extends Fragment {

		public DescriptionSectionFragment() {
		}
		
		public static final int STRING_BUFFER_SIZE = 2250;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_description,
					container, false);
			
			TextView titleTextView = (TextView) rootView.findViewById(R.id.section_label);
			titleTextView.setText(R.string.title_section4);
			
			Resources res = getResources();
			String[] strArray = res.getStringArray(R.array.desription_content);
			
			StringBuilder sb = new StringBuilder(STRING_BUFFER_SIZE);
			for(int i = 0; i < strArray.length; ++i) {
				sb.append(strArray[i]);
				sb.append(" ");
			}
			
			TextView contentTextView = (TextView) rootView.findViewById(R.id.section_text);
			contentTextView.setMaxHeight(rootView.getHeight()/3);
			contentTextView.setMovementMethod(new ScrollingMovementMethod());
			contentTextView.setText(sb.toString());
			
			strArray = res.getStringArray(R.array.desription_content_guidelines);
			
			sb = new StringBuilder(STRING_BUFFER_SIZE);
			for(int i = 0; i < strArray.length; ++i) {
				sb.append("~");
				sb.append(strArray[i]);
				sb.append("\n");
			}
			
			contentTextView = (TextView) rootView.findViewById(R.id.section_text_guidelines);
			contentTextView.setMaxHeight(rootView.getHeight()/3);
			contentTextView.setMovementMethod(new ScrollingMovementMethod());
			contentTextView.setText(sb.toString());
			
			
			return rootView;
		}
	}
	
	public static class InfoSectionFragment extends Fragment {

		public InfoSectionFragment() {
		}
		
		public static final int STRING_BUFFER_SIZE = 2250;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_info, container, false);
			
			TextView titleTextView = (TextView) rootView.findViewById(R.id.section_label);
			titleTextView.setText(R.string.title_section5);
			
			Resources res = getResources();
			String[] strArray = res.getStringArray(R.array.info_content);
			
			StringBuilder sb = new StringBuilder(STRING_BUFFER_SIZE);
			for(int i = 0; i < strArray.length; ++i) {
				sb.append(strArray[i]);
				sb.append(" ");
			}
			
			TextView contentTextView = (TextView) rootView.findViewById(R.id.section_text);
			contentTextView.setMaxHeight(rootView.getHeight()/3);
			contentTextView.setMovementMethod(new ScrollingMovementMethod());
			contentTextView.setText(sb.toString());
			
			return rootView;
		}
	}
	
	public static class CurrentProjectSectionFragment extends ListFragment {
	
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			parentActivity  = getActivity();
		    fillListViewData();
		}

	    @Override
	    public void onDestroy() {
	        if(projectManagerDatabaseAdapter != null)
	        	projectManagerDatabaseAdapter.close();
	        super.onDestroy();
	    }
	    
	    private ProjectManagerDatabaseAdapter projectManagerDatabaseAdapter;
	    private Cursor projectCurrentCursor;
	    private List<Project> currentProjects;
	    private ProjectsAdapter listCurrentAdapter;
	    private FragmentActivity parentActivity;
		
		/**
		 * Function basically responsible for filling the content of ListView lvCurrentProjects.
		 */
		private void fillListViewData() {
		    projectManagerDatabaseAdapter = new ProjectManagerDatabaseAdapter(
		    		parentActivity.getApplicationContext());
		    projectManagerDatabaseAdapter.open();
		    
		    getCurrentProjects();
		    listCurrentAdapter = new ProjectsAdapter(parentActivity, currentProjects);
	    	setListAdapter(listCurrentAdapter);
		}

		/**
		 * Function preparing the currentProjects list.
		 */
		private void getCurrentProjects() {
		    currentProjects = new ArrayList<Project>();
		    getUncompletedProjectsFromDb();
		    updateCurrentProjectList();
		}

		/**
		 * Function preparing a Cursor instance with Projects from database that are marked as uncompleted.
		 */		
		@SuppressWarnings("deprecation")
		private void getUncompletedProjectsFromDb() {
		    projectCurrentCursor = projectManagerDatabaseAdapter.getUncompletedProjects();
		    if(projectCurrentCursor != null) {
		        parentActivity.startManagingCursor(projectCurrentCursor);
		        projectCurrentCursor.moveToFirst();
		    }
		}	

		/**
		 * Function updating the currentProject List based on projectCurrentCursor content.
		 */
		private void updateCurrentProjectList() {
		    currentProjects.clear();
		    if(projectCurrentCursor != null && projectCurrentCursor.moveToFirst()) {
		        do {
		            long id = projectCurrentCursor.getLong(ProjectManagerDatabaseAdapter.ID_COLUMN);
		            String name = projectCurrentCursor.getString(ProjectManagerDatabaseAdapter.NAME_COLUMN);
		            String description = projectCurrentCursor.getString(ProjectManagerDatabaseAdapter.DESCRIPTION_COLUMN);
		            long start = projectCurrentCursor.getLong(ProjectManagerDatabaseAdapter.START_COLUMN);
		            long end = projectCurrentCursor.getLong(ProjectManagerDatabaseAdapter.END_COLUMN);
		            String phase = projectCurrentCursor.getString(ProjectManagerDatabaseAdapter.PHASE_COLUMN);
		            boolean completed = projectCurrentCursor.getInt(ProjectManagerDatabaseAdapter.COMPLETED_COLUMN) > 0 ? true : false;
		            currentProjects.add(new Project(id, name, description, start, end, phase, completed));
		        } while(projectCurrentCursor.moveToNext());
		    }
		}		

		/**
		 * Function basically responsible for updating the content of ListView lvCurrentProjects.
		 */
		@SuppressWarnings("deprecation")
		public void updateCurrentListViewData() {
		    projectCurrentCursor.requery();
		    updateCurrentProjectList();
		    listCurrentAdapter.notifyDataSetChanged();
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
            Toast.makeText(parentActivity, "You've just touched a current project;)", Toast.LENGTH_SHORT).show();
            updateCurrentListViewData();
	
		}
	}
	
	public static class HistorySectionFragment  extends ListFragment {

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			parentActivity  = getActivity();
		    fillListViewData();
		}
		
	    @Override
	    public void onDestroy() {
	        if(projectManagerDatabaseAdapter != null)
	        	projectManagerDatabaseAdapter.close();
	        super.onDestroy();
	    }
	    
	    private ProjectManagerDatabaseAdapter projectManagerDatabaseAdapter;
	    private Cursor projectHistoryCursor;
	    private List<Project> historyProjects;
	    private ProjectsAdapter listHistoryAdapter;
	    private FragmentActivity parentActivity;

		/**
		 * Function basically responsible for filling the content of ListView lvHistoryProjects.
		 */
		private void fillListViewData() {
		    projectManagerDatabaseAdapter = new ProjectManagerDatabaseAdapter(
		    		parentActivity.getApplicationContext());
		    projectManagerDatabaseAdapter.open();
		    
		    getHistoryProjects();
		    listHistoryAdapter = new ProjectsAdapter(parentActivity, historyProjects);
	    	setListAdapter(listHistoryAdapter);
		}

		/**
		 * Function preparing the historyProjects list.
		 */
		private void getHistoryProjects() {
		    historyProjects = new ArrayList<Project>();
		    getCompletedProjectsFromDb();
		    updateHistoryProjectList();
		}

		/**
		 * Function preparing a Cursor instance with Projects from database that are marked as completed.
		 */
		@SuppressWarnings("deprecation")
		private void getCompletedProjectsFromDb() {
		    projectHistoryCursor = projectManagerDatabaseAdapter.getCompletedProjects();
		    if(projectHistoryCursor != null) {
		        parentActivity.startManagingCursor(projectHistoryCursor);
		        projectHistoryCursor.moveToFirst();
		    }
		}

		/**
		 * Function updating the historyProject List based on projectHistoryCursor content.
		 */
		private void updateHistoryProjectList() {
		    historyProjects.clear();
		    if(projectHistoryCursor != null && projectHistoryCursor.moveToFirst()) {
		        do {
		            long id = projectHistoryCursor.getLong(ProjectManagerDatabaseAdapter.ID_COLUMN);
		            String name = projectHistoryCursor.getString(ProjectManagerDatabaseAdapter.NAME_COLUMN);
		            String description = projectHistoryCursor.getString(ProjectManagerDatabaseAdapter.DESCRIPTION_COLUMN);
		            long start = projectHistoryCursor.getLong(ProjectManagerDatabaseAdapter.START_COLUMN);
		            long end = projectHistoryCursor.getLong(ProjectManagerDatabaseAdapter.END_COLUMN);	            
		            String phase = projectHistoryCursor.getString(ProjectManagerDatabaseAdapter.PHASE_COLUMN);
		            boolean completed = projectHistoryCursor.getInt(ProjectManagerDatabaseAdapter.COMPLETED_COLUMN) > 0 ? true : false;
		            historyProjects.add(new Project(id, name, description, start, end, phase, completed));
		        } while(projectHistoryCursor.moveToNext());
		    }
		}

		/**
		 * Function basically responsible for updating the content of ListView lvHistoryProjects.
		 */
		@SuppressWarnings("deprecation")
		private void updateHistoryListViewData() {
		    projectHistoryCursor.requery();
		    updateHistoryProjectList();
		    listHistoryAdapter.notifyDataSetChanged();
		}
		
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
            Toast.makeText(parentActivity, "You've just touched a history project;)", Toast.LENGTH_SHORT).show();
            updateHistoryListViewData();
	
		}
	}
	
	/**
	 * Function that removes all projects marked as uncompleted (found in 'Current Projects' panel).
	 */	
	@SuppressWarnings("deprecation")
	public void btnClearCurrentProjects(View v) {
		if (projectManagerDbAdapter != null) {
			Cursor c = projectManagerDbAdapter.getUncompletedProjects();
			if (c != null) {
				startManagingCursor(c);
			}
			if(c != null && c.moveToFirst()) {
		        do {
		        	long id = c.getLong(ProjectManagerDatabaseAdapter.ID_COLUMN);
	                deleteMilestones(id);
	                projectManagerDbAdapter.deleteProject(id);
		        } while (c.moveToNext());
				mViewPager.setAdapter(mSectionsPagerAdapter);
				Toast.makeText(this, "Your current projects have been cleared.", Toast.LENGTH_SHORT).show();
		    } else {
		    	Toast.makeText(this, "You don't have any current projects.", Toast.LENGTH_SHORT).show();
		    }
		}
	}

	/**
	 * Function that removes all projects marked as completed (found in 'History' panel).
	 */	
	@SuppressWarnings("deprecation")
	public void btnClearHistory(View v) {
		if (projectManagerDbAdapter != null) {
			Cursor c = projectManagerDbAdapter.getCompletedProjects();
			if (c != null) {
				startManagingCursor(c);
			}
		    if(c != null && c.moveToFirst()) {
		        do {
		        	long id = c.getLong(ProjectManagerDatabaseAdapter.ID_COLUMN);
	                deleteMilestones(id);
		        	projectManagerDbAdapter.deleteProject(id);
		        } while (c.moveToNext());
				mViewPager.setAdapter(mSectionsPagerAdapter);
				Toast.makeText(this, "History has been cleared.", Toast.LENGTH_SHORT).show();
		    } else {
		    	Toast.makeText(this, "There is nothing in the History.", Toast.LENGTH_SHORT).show();
		    }
		}
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

}
