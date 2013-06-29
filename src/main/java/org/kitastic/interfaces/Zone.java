package org.kitastic.interfaces;


import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.kitastic.utils.Zonable;

public class Zone {
	public Vector loc1;
	public Vector loc2;
	public int zoneType;
	public String name;

	public Zone(Vector boxCorner1, Vector boxCorner2, String name){
		this.loc1 = boxCorner1;
		this.loc2 = boxCorner2;
		this.zoneType = 1;
		this.name = name;
	}
	public Zone(Vector center,double radius, String name){
		this.loc1 = center;
		this.loc2 = new Vector(0,radius,0);
		this.zoneType = 2;
		this.name = name;
	}
	
	public void setBox(Vector location1,Vector location2){
		this.loc1 = location1;
		this.loc2 = location2;
		this.zoneType = 1;
	}
	public void setSphere(Vector center,double radius){
		this.loc1 = center;
		this.loc2 = new Vector(0,radius,0);
		this.zoneType = 2;
	}
	
	public Boolean contains(Location loc){
		if(zoneType == 1&&loc.toVector().isInAABB(this.loc1, this.loc2)){
				return true;
		}
		if(zoneType == 2&&loc.toVector().isInSphere(this.loc1, this.loc2.length())){
				return true;
		}
		return false;
	}
}
