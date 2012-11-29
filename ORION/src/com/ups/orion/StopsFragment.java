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
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StopsFragment extends Fragment
{
	View mFragmentView;
	ListView mStopListView;
	ListView mConsigneeListView;
	List<Stop> mStops;
	int mStopIndex;
	
	StopAdapter stopAdapter;
	ConsigneeAdapter consigneeAdapter;
	ArrayList<Map<String,String>> mList;
	
	@Override
	// construct the view you want to show inside this fragment. Pass a reference back to that top level view
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
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
		
		// create a view by inflating the layout xml and putting it in the container
		mFragmentView = inflater.inflate(R.layout.stops_layout, container,false);
		
		// get and set up the listview to view stops
		mStopListView = (ListView) mFragmentView.findViewById(R.id.stoplist);
		mStopListView.setHorizontalScrollBarEnabled(true);
		mStopListView.setVerticalScrollBarEnabled(true);
		
		// set up highlighting of rows
		mStopListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mStopListView.setSelector(android.R.color.holo_blue_light);
		mStopListView.setFocusable(true);
		mStopListView.setSelected(true);
		
		// make a stopAdapter and attach it to the stop listview
		stopAdapter = new StopAdapter(getActivity(), mStops);
		mStopListView.setAdapter(stopAdapter);
		
		// set stop list view handler
		mStopListView.setOnItemClickListener(onStopClick);
		
		// get and set up the listview to view consignees
		mConsigneeListView = (ListView) mFragmentView.findViewById(R.id.consignee);
		mConsigneeListView.setHorizontalScrollBarEnabled(true);
		mConsigneeListView.setVerticalScrollBarEnabled(true);
		
		
		Stop stop = null;
		if(mStops != null)
		{
			stop = (Stop) stopAdapter.getItem(0);
		}

		consigneeAdapter = new ConsigneeAdapter(getActivity(), stop.packages);
		mConsigneeListView.setAdapter(consigneeAdapter);
		
		if(savedInstanceState != null)
		{
			mStopIndex = savedInstanceState.getInt("stopIndex");
		}
		else
		{
			mStopIndex = 0;
		}
		
//		View v =  mStopListView.getAdapter().getView(0, getView(), mStopListView); 
//		v.setBackgroundResource(android.R.color.holo_blue_light);
		
		return mFragmentView;
	}
	
	private OnItemClickListener onStopClick = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int iPosition, long lPosition)
		{
			// select pkgs from selected stop
			Stop stop = null;
			if (mStops != null)
			{
				stop = (Stop) stopAdapter.getItem(iPosition);
			}

			consigneeAdapter = new ConsigneeAdapter(getActivity(), stop.packages);
			mConsigneeListView.setAdapter(consigneeAdapter);
		}
	};

	public ListView getmStopListView()
	{
		return mStopListView;
	}
}





















