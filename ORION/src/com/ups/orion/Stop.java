package com.ups.orion;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stop
{
    public int stopid;
    public String address;
    public double lat;
    public double lon;
    public double svctime;
    public int numpkgs;
    public String svc;
    public int rdo;
    public int odo;
    public int esstclicks;
    public int lsstclicks;
    public int eta;
    
    public List<Package> packages;
    public List<Map.Entry<String, Integer>> conToPkgCountPairs = new ArrayList<Map.Entry<String, Integer>>();
    
    public Stop()
    {
    	packages = new ArrayList<Package>();
    }
    
    // each unique consignee's pkg count will be set (i.e. populate a list of key, value pairs consisting of consignee, num pkgs)
    public void SetUniqueConsigneePkgCount()
    {
    	conToPkgCountPairs.clear();
    	
    	if( packages.size() > 0)
    	{
	    	// sort the pkgs on the default comparer - consignee
	    	Collections.sort(packages);
	    	
	    	int i = 0;
	    	int uniqueConPkgCount = 0;
	    	
	    	// prime the loop - get the first consignee
	    	String currCon = packages.get(i).consignee;
	    		    	
	    	while( i < packages.size())
	    	{
	    		while( i < packages.size() && packages.get(i).consignee.equalsIgnoreCase(currCon) )
		    	{
		    		uniqueConPkgCount++;
		    		i++;
		    	}
	
	    		/* it's a new con - last finish up with the old one */
	    		Map.Entry<String,Integer> entry = new AbstractMap.SimpleEntry<String, Integer>(currCon, uniqueConPkgCount);
		    	
	    		// add the consignee to the kv pair collection
	    		conToPkgCountPairs.add(entry);
		    	
	    		if( i < packages.size() )
	    		{
		    		currCon = packages.get(i).consignee;
		    		
		    		// reset the pkg count for the next con
		    		uniqueConPkgCount = 0;
	    		}
	    	}
    	}
    }
}
















