package com.ups.orion;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	List<Stop> mStops;
	
	private static final String TAG = StopsFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create the adapter that will return a fragment for each of the three primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding tab.
		// We can also use ActionBar.Tab#select() to do this if we have a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
				{
					@Override
					public void onPageSelected(int position)
					{
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
		{
			// Create a tab with text corresponding to the page title defined by the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
//		System.setProperty("http.proxyHost", "localhost");
//		System.setProperty("http.proxyPort", "8888");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menuOptimize:
				Toast.makeText(MainActivity.this, "Optimize", Toast.LENGTH_SHORT).show();
				CheckNetwork();
				GetHtml();
				sendJson();
				return true;
		}
		
		return false;
	}

	protected void sendJson()
	{
		Thread t = new Thread()
        {
	        public void run() 
	        {
	            Looper.prepare(); //For Preparing Message Pool for the child Thread
	            
	            HttpClient client = new DefaultHttpClient();
	            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
	            HttpResponse response;
	            try
	            {
	            	// read json file
	            	BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.stopsismd)));
	                String line, results = "";
	                while( ( line = reader.readLine() ) != null)
	                {
	                    results += line;
	                }
	                reader.close();

	                // initialize the json array with the string parsed from the file
	                JSONArray json = new JSONArray(results);
	                
	            	URL url = new URL("http://10.231.145.251/weboptimizer/api/optstops"); // Shirin's machine
	            	
	            	HttpPost post = new HttpPost(url.toURI());
                    
                    StringEntity se = new StringEntity( json.toString());  
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);
                    
                    /*Checking response */
                    if(response!=null)
                    {
                        InputStream in = response.getEntity().getContent(); //Get the data
                        Log.d(TAG, in.toString());
                    }
	            }
	            catch(Exception e)
	            {
	                e.printStackTrace();
	            }
	            
	            Looper.loop(); //Loop in the message queue
	        }
        };
        
        t.start();      
    }

	private void GetHtml()
	{
		// for dev purposes we can relax the thread strict mode policy
		ThreadPolicy tp = ThreadPolicy.LAX;
		StrictMode.setThreadPolicy(tp);
		
		try
		{
			HttpClient client = new DefaultHttpClient();
			String getURL = "http://10.231.145.251/weboptimizer";
			
			// get portion
			HttpGet get = new HttpGet(getURL);
			HttpResponse responseGet = client.execute(get);
			HttpEntity resEntityGet = responseGet.getEntity();
			if (resEntityGet != null)
			{
				// do something with the response
				String response = EntityUtils.toString(resEntityGet);
				Log.d(TAG, response);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	// checks if we're connected to the network
	private void CheckNetwork()
	{
    	ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) 
        {
            // fetch data
        } 
        else 
        {
            // display error
        }
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
	{
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{
		FragmentManager mFragMgr;
		
		public SectionsPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int i)
		{
			if(i == 0)
			{
				Fragment fragment = new StopsFragment();
				
				return fragment;
			}
			else 
			{
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount()
		{
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			switch (position)
			{
			case 0:
				return getString(R.string.title_section1).toUpperCase();
			case 1:
				return getString(R.string.title_section2).toUpperCase();
			case 2:
				return getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment
	{
		public DummySectionFragment()
		{
		}

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			Bundle args = getArguments();
			textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
			return textView;
		}
	}
}
