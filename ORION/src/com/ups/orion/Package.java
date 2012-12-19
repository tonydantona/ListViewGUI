package com.ups.orion;

public class Package implements Comparable<Package>
{
    public String consignee;
    public int earliest;
    public int commit;
    public int svclevel;
    public int pkgsToSameCon;
    
	@Override
	public int compareTo(Package rhs)
	{
		return this.consignee.compareTo(rhs.consignee);
	}
}
