package com.example.projectmanager;

import com.example.projectmanager.database.ProjectManagerDatabaseAdapter;
import com.example.projectmanager.models.Project;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

	
	private ListView lvCurrentProjects;
	private ListView lvHistoryProjects;
	
    private void initUiElements() {
        lvCurrentProjects = (ListView) findViewById(R.id.projectCurrentListView);
        lvHistoryProjects = (ListView) findViewById(R.id.projectHistoryListView);
    }
    
    private ProjectManagerDatabaseAdapter projectManagerDatabaseAdapter;
    private Cursor projectCurrentCursor;
    private List<Project> currentProjects;
    private ProjectsAdapter listCurrentAdapter;
    
    private Cursor projectHistoryCursor;
    private List<Project> historyProjects;
    private ProjectsAdapter listHistoryAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each primary
		// sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

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
		
		initUiElements();
		initListViews();
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
	 * Function responsible for preparing ListViews lvCurrentProjects and lvHistoryProjects.	
	 */
	private void initListViews() {
	    fillListViewsData();
	    initListViewsOnItemClick();
	}
	
	/**
	 * Function basically responsible for filling the content of ListViews lvCurrentProjects
	 * and lvHistoryProjects.
	 */
	private void fillListViewsData() {
	    projectManagerDatabaseAdapter = new ProjectManagerDatabaseAdapter(getApplicationContext());
	    projectManagerDatabaseAdapter.open();
	    
	    getCurrentProjects();
	    listCurrentAdapter = new ProjectsAdapter(this, currentProjects);
	    lvCurrentProjects.setAdapter(listCurrentAdapter);
	    
	    getHistoryProjects();
	    listHistoryAdapter = new ProjectsAdapter(this, historyProjects);
	    lvHistoryProjects.setAdapter(listHistoryAdapter);
	}

	/**
	 * Function preparing the currentProjects list.
	 */
	private void getCurrentProjects() {
	    currentProjects = new ArrayList<Project>();
	    projectCurrentCursor = getCompletedProjectsFromDb();
	    updateCurrentProjectList();
	}
	 
	/**
	 * Function preparing the historyProjects list.
	 */
	private void getHistoryProjects() {
	    historyProjects = new ArrayList<Project>();
	    projectHistoryCursor = getUncompletedProjectsFromDb();
	    updateHistoryProjectList();
	}

	/**
	 * Function returning a Cursor instance with Projects from database that are marked as completed.
	 */
	private Cursor getCompletedProjectsFromDb() {
	    projectCurrentCursor = projectManagerDatabaseAdapter.getCompletedProjects();
	    if(projectCurrentCursor != null) {
	        startManagingCursor(projectCurrentCursor);
	        projectCurrentCursor.moveToFirst();
	    }
	    return projectCurrentCursor;
	}
	 
	/**
	 * Function returning a Cursor instance with Projects from database that are marked as uncompleted.
	 */
	private Cursor getUncompletedProjectsFromDb() {
	    projectHistoryCursor = projectManagerDatabaseAdapter.getUncompletedProjects();
	    if(projectHistoryCursor != null) {
	        startManagingCursor(projectHistoryCursor);
	        projectHistoryCursor.moveToFirst();
	    }
	    return projectHistoryCursor;
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
	 * Function responsible for creating a proper action for clicking a single project
	 * (either in 'Current Projects' or 'History' panels).
	 */
	private void initListViewsOnItemClick() {
	    lvCurrentProjects.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View v, int position,
	                long id) {
	            Project project = currentProjects.get(position);
	            Toast.makeText(getApplicationContext(), "You've just touched a current project;)", Toast.LENGTH_SHORT).show();
	            updateCurrentListViewData();
	        }
	    });
	    
	    lvHistoryProjects.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View v, int position,
	                long id) {
	            Project project = historyProjects.get(position);
	            Toast.makeText(getApplicationContext(), "You've just touched a history project;)", Toast.LENGTH_SHORT).show();
	            updateHistoryListViewData();
	        }
	    });
	}

	/**
	 * Function basically responsible for updating the content of ListView lvCurrentProjects.
	 */
	private void updateCurrentListViewData() {
	    projectCurrentCursor.requery();
	    updateCurrentProjectList();
	    listCurrentAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Function basically responsible for updating the content of ListView lvHistoryProjects.
	 */
	private void updateHistoryListViewData() {
	    projectHistoryCursor.requery();
	    updateHistoryProjectList();
	    listHistoryAdapter.notifyDataSetChanged();
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

	
	/**
	 * Function that starts the NewProjectActivity.
	 */	
	public void btnNewProjectOnClick(View v) {
        Intent intent = new Intent(getApplicationContext(), NewProjectActivity.class);
        startActivity(intent);
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
	
	public static class CurrentProjectSectionFragment extends Fragment {

		public CurrentProjectSectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_current_projects, container, false);
			
			TextView titleTextView = (TextView) rootView.findViewById(R.id.section_label);
			if (true) {
				titleTextView.setText(R.string.no_current_project);
				titleTextView.setTextSize(30);
			} else {
				titleTextView.setText(R.string.current_projects_title);				
			}
						
			return rootView;
		}
	}
	
	public static class HistorySectionFragment extends Fragment {

		public HistorySectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_history, container, false);
			
			TextView titleTextView = (TextView) rootView.findViewById(R.id.section_label);
			if (true) {
				titleTextView.setText(R.string.no_finished_project);
				titleTextView.setTextSize(30);
			} else {
				titleTextView.setText(R.string.finished_projects_title);				
			}
						
			return rootView;
		}
	}

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
	
}
