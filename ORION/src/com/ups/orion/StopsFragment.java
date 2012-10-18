package com.ups.orion;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class StopsFragment extends Fragment
{
	View mFragmentView;
	ListView mStopListView;
	ListView mConsigneeListView;
	List<Stop> mStops;
	int mStopIndex;
	
	SimpleAdapter mAdapter;
	ArrayList<Map<String,String>> mList;
	
	@Override
	// construct the view you want to show inside this fragment. Pass a reference back to that top level view
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
//		getActivity().requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 

		// create a view by inflating the layout xml and putting it in the container
		mFragmentView = inflater.inflate(R.layout.stops_layout, container,false);
		
//		getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.your_header); 
		
		InputStream jsonStops = getResources().openRawResource(R.raw.stops);
		JsonStopsReader jReader = new JsonStopsReader();
		
		try
		{
			mStops = jReader.readJsonStream(jsonStops);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// get a reference to list view contained within the fragment
		// note: listview needed an android id so header layout could find it
		mStopListView = (ListView) mFragmentView.findViewById(android.R.id.list);
		mStopListView.setHorizontalScrollBarEnabled(true);

		View header = inflater.inflate(R.layout.header, null);
		mStopListView.addHeaderView(header);

		StopAdapter stopAdapter = new StopAdapter(getActivity(), mStops);

		mStopListView.setAdapter(stopAdapter);
//		mConsigneeView.setAdapter(pkgAdapter);
		
		if(savedInstanceState != null)
		{
			mStopIndex = savedInstanceState.getInt("stopIndex");
		}
		else
		{
			mStopIndex = 0;
		}
		
		return mFragmentView;
	}
}
