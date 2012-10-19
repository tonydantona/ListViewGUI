package com.ups.orion;

import java.util.List;

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
	List<Package> mPkgs;
	
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
		
		Package pkg = mPkgs.get(mPosition); 
		
		if (pkg != null)
		{
			// set text to view
			TextView tvSvc = (TextView) pkgView.findViewById(R.id.chkboxpkg);
			TextView tvConsignee = (TextView) pkgView.findViewById(R.id.consignee);
			TextView tvNumPkgs = (TextView) pkgView.findViewById(R.id.numpkgs);
			
			if (tvSvc != null)
			{
				tvSvc.setText(String.valueOf(pkg.svclevel));
			}
			if (tvConsignee != null)
			{
				tvConsignee.setText(String.valueOf(pkg.consignee));
			}
			if (tvNumPkgs != null)
			{
				tvNumPkgs.setText(String.valueOf(3));
			}
		}

		return pkgView;
	}

	public ConsigneeAdapter(Context context, List<Package> pkgs)
	{
		mContext = context;
		mPkgs = pkgs;
	}
	
	@Override
	public int getCount()
	{
		return mPkgs.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mPkgs.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
}
