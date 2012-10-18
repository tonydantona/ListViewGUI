package com.ups.orion;

import java.util.ArrayList;
import java.util.List;

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
    
    List<Package> packages;
    
    public Stop()
    {
    	packages = new ArrayList<Package>();
    }
}
