package com.ups.orion;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConsigneeAdapter extends BaseAdapter
{
	int mPosition = -1;
	Context mContext;
	List<Map.Entry<String, Integer>> mConToPkgs;
	
	public ConsigneeAdapter(Context context, List<Map.Entry<String, Integer>> conToPkgs)
	{
		mContext = context;
		mConToPkgs = conToPkgs;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View pkgView = convertView;
		mPosition = position;
		
		if(pkgView == null)
		{
			// inflate a new view for the list item
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			pkgView = inflater.inflate(R.layout.consigneerow, parent, false);
		}
		
		Map.Entry<String, Integer> conPkgCount = mConToPkgs.get(mPosition); 
		
		if( conPkgCount != null )
		{
			// set text to view
			TextView tvConsignee = (TextView) pkgView.findViewById(R.id.consignee);
			TextView tvNumPkgs = (TextView) pkgView.findViewById(R.id.numpkgs);

			if (tvConsignee != null)
			{
				tvConsignee.setText(String.valueOf(conPkgCount.getKey()));
			}
			if (tvNumPkgs != null)
			{
				tvNumPkgs.setText(String.valueOf(conPkgCount.getValue()));
			}
		}
			
		return pkgView;
	}

	@Override
	public int getCount()
	{
		return mConToPkgs.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mConToPkgs.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
}
