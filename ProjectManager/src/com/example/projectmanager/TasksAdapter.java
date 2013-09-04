package com.example.projectmanager;

import com.example.projectmanager.models.Task;
import android.app.Activity;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TasksAdapter extends ArrayAdapter<Task> {
    private Activity context;
    private List<Task> tasks;
    
    public TasksAdapter(Activity context, List<Task> tasks) {
        super(context, R.layout.task_list_item, tasks);
        this.context = context;
        this.tasks = tasks;
    }
 
    static class ViewHolder {
        public TextView taskName;
        public TextView taskDescription;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.task_list_item, null, true);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) rowView.findViewById(R.id.taskName);
            viewHolder.taskDescription = (TextView) rowView.findViewById(R.id.taskDescription);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        Task task = tasks.get(position);
        viewHolder.taskName.setText(task.getName());
        viewHolder.taskDescription.setText(task.getDescription());
        
        return rowView;
    }
}