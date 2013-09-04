package com.example.projectmanager;

import com.example.projectmanager.models.Project;
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
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        Project project = projects.get(position);
        viewHolder.projectName.setText(project.getName());
        viewHolder.projectDescription.setText(project.getDescription());
        
        return rowView;
    }
}