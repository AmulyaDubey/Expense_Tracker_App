package com.example.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<Expenditure>
{
    public UserAdapter(Context context, ArrayList<Expenditure> users)
    {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        Expenditure obj = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_view, parent, false);
        }
        // Lookup view for data population
        TextView mOption = (TextView) convertView.findViewById(R.id.mOption);
        TextView mAmt = (TextView) convertView.findViewById(R.id.mAmt);
        TextView mDate=(TextView)convertView.findViewById(R.id.mDate);
        // Populate the data into the template view using the data object
        mOption.setText(obj.getOption());
        mAmt.setText(obj.getAmt());
        mDate.setText(obj.getDate());
        // Return the completed view to render on screen
        return convertView;
    }
}


