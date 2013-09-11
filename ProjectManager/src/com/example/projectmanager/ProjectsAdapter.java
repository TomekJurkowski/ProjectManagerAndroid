package com.example.projectmanager;

import com.example.projectmanager.models.Project;

import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProjectsAdapter extends ArrayAdapter<Project> {
    private Activity context;
    private List<Project> projects;
    
    public ProjectsAdapter(Activity context, List<Project> projects) {
        super(context, R.layout.project_list_item, projects);
        this.context = context;
        this.projects = projects;
    }
 
    static class ViewHolder {
        public TextView projectName;
        public TextView projectDescription;
        public TextView projectDates;
        public TextView projectPhase;
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
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.project_list_item, null, true);
            viewHolder = new ViewHolder();
            viewHolder.projectName = (TextView) rowView.findViewById(R.id.projectName);
            viewHolder.projectDescription = (TextView) rowView.findViewById(R.id.projectDescription);
            viewHolder.projectDates = (TextView) rowView.findViewById(R.id.projectDates);
            viewHolder.projectPhase = (TextView) rowView.findViewById(R.id.projectPhase);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        Project project = projects.get(position);
        viewHolder.projectName.setText(project.getName());
        viewHolder.projectDescription.setText(project.getDescription());
        viewHolder.projectPhase.setText(project.getPhase());
        
        String start = milisecondsToDate(project.getStart());
        String end = milisecondsToDate(project.getEnd());

		viewHolder.projectDates.setText(start + " - " + end);
        
        return rowView;
    }
}