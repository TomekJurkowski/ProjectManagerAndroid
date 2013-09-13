package com.example.projectmanager;

import com.example.projectmanager.models.Milestone;

import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MilestonesAdapter extends ArrayAdapter<Milestone> {
    private Activity context;
    private List<Milestone> milestones;
    
    public MilestonesAdapter(Activity context, List<Milestone> milestones) {
        super(context, R.layout.milestone_list_item, milestones);
        this.context = context;
        this.milestones = milestones;
    }
 
    static class ViewHolder {
        public TextView milestoneName;
        public TextView milestoneDescription;
        public TextView milestoneDates;
        public TextView milestonePhase;
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
            rowView = layoutInflater.inflate(R.layout.milestone_list_item, null, true);
            viewHolder = new ViewHolder();
            viewHolder.milestoneName = (TextView) rowView.findViewById(R.id.milestoneName);
            viewHolder.milestoneDescription = (TextView) rowView.findViewById(R.id.milestoneDescription);
            viewHolder.milestoneDates = (TextView) rowView.findViewById(R.id.milestoneDates);
            viewHolder.milestonePhase = (TextView) rowView.findViewById(R.id.milestonePhase);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        Milestone milestone = milestones.get(position);
        viewHolder.milestoneName.setText(milestone.getName());
        viewHolder.milestoneDescription.setText(milestone.getDescription());
        viewHolder.milestonePhase.setText(milestone.getPhase());
        
        String start = milisecondsToDate(milestone.getStart());
        String end = milisecondsToDate(milestone.getEnd());

		viewHolder.milestoneDates.setText(start + " - " + end);

        return rowView;
    }
}