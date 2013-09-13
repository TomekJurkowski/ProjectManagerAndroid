package com.example.projectmanager;

import com.example.projectmanager.models.Task;
import android.app.Activity;

import java.util.Calendar;
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
        public TextView taskDates;
        public TextView taskEstimatedTime;
        public TextView taskPriority;
        public TextView taskPhase;
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
            rowView = layoutInflater.inflate(R.layout.task_list_item, null, true);
            viewHolder = new ViewHolder();
            viewHolder.taskName = (TextView) rowView.findViewById(R.id.taskName);
            viewHolder.taskDescription = (TextView) rowView.findViewById(R.id.taskDescription);
            viewHolder.taskDates = (TextView) rowView.findViewById(R.id.taskDates);
            viewHolder.taskEstimatedTime = (TextView) rowView.findViewById(R.id.taskEstimatedTime);
            viewHolder.taskPriority = (TextView) rowView.findViewById(R.id.taskPriority);
            viewHolder.taskPhase = (TextView) rowView.findViewById(R.id.taskPhase);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        Task task = tasks.get(position);
        viewHolder.taskName.setText(task.getName());
        viewHolder.taskDescription.setText(task.getDescription());
        viewHolder.taskEstimatedTime.setText(task.getEstimatedTime());
        viewHolder.taskPriority.setText(task.getPriority());
        viewHolder.taskPhase.setText(task.getPhase());
        
        String start = milisecondsToDate(task.getStart());
        String end = milisecondsToDate(task.getEnd());

		viewHolder.taskDates.setText(start + " - " + end);
        
        return rowView;
    }
}