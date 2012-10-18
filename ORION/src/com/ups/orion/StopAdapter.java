package com.ups.orion;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StopAdapter extends BaseAdapter
{
	int mPosition = -1;
	Context mContext;
	List<Stop> mStops;
	
	public StopAdapter(Context context, List<Stop> stops)
	{
		mContext = context;
		mStops = stops;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View stopView = convertView;
		mPosition = position;
		
		if(stopView == null)
		{
			// inflate a new view for the list item
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			stopView = inflater.inflate(R.layout.row, parent, false);
		}
		
		Stop stop = mStops.get(mPosition); 
		
		if (stop != null)
		{
			// set text to view
			TextView tvSvc = (TextView) stopView.findViewById(R.id.svc);
			TextView tvAddress = (TextView) stopView.findViewById(R.id.address);
			TextView tvEarliest = (TextView) stopView.findViewById(R.id.earliest);
			TextView tvCommit = (TextView) stopView.findViewById(R.id.commit);
			TextView tvETA = (TextView) stopView.findViewById(R.id.eta);
			
			if (tvSvc != null)
			{
				tvSvc.setText(String.valueOf(stop.svc));
			}
			if (tvAddress != null)
			{
				tvAddress.setText(String.valueOf(stop.address));
			}
			if (tvEarliest != null)
			{
				tvEarliest.setText(String.valueOf(stop.esstclicks));
			}
			if (tvCommit != null)
			{
				tvCommit.setText(String.valueOf(stop.lsstclicks));
			}
			if (tvETA != null)
			{
				tvETA.setText(String.valueOf(stop.eta));
			}
		}

		return stopView;
	}
	
	@Override
	public int getCount()
	{
		return mStops.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mStops.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}


}
