package com.example.stephen.searchviewtest2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by stephen on 17-6-16.
 */

public class NewAdapter extends ArrayAdapter<School> {
    private int resourceId;
    public NewAdapter(Context context, int textResourceId, List object){
        super(context,textResourceId,object);
        resourceId=textResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        School school=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name=(TextView)view.findViewById(R.id.name);
        name.setText(school.getName());
        return view;
    }
}
