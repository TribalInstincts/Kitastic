package org.kitastic.utils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitastic.Kitastic;
import org.kitastic.events.PlayerChangeZoneEvent;
import org.kitastic.events.PlayerMoveFullBlockEvent;
import org.kitastic.events.PlayerMoveInZoneEvent;
import org.kitastic.events.ZoneChangeType;
import org.kitastic.interfaces.Zone;
/*watches onmove and fires enterszone and exitszone events. onlineplayers listen for them, and add/remove to zones accordingly. Kits can then check their
player's zone list at specific times, or watch the register to the zone events theirselves. */
public class ZoneManager implements Listener{
	public Kitastic Plugin;
	public ArrayList<Zone> zoneList;
	public ZoneManager(Kitastic Plugin){
		this.Plugin = Plugin;
		this.Plugin.pluginManager.registerEvents(this, Plugin);
		this.zoneList = new ArrayList<Zone>();
	}
	
	public void addZone(Zone newZone){
		if(!this.zoneList.contains(newZone)){
			this.zoneList.add(newZone);
		}
		
	}
	public void removeZone(Zone oldZone){
		if(this.zoneList.contains(oldZone)){
			this.zoneList.remove(oldZone);
		}
	}
	
	@EventHandler
	public void listenforMovement(PlayerMoveFullBlockEvent event){
		if(zoneList.size() > 0){
			Location movedTo = event.getTo();
			Location movedFrom = event.getFrom();
			for(Zone nextZone: zoneList){
				Boolean from = nextZone.contains(movedFrom);
				Boolean to = nextZone.contains(movedTo);
				if(to||from){

					if(to&&!from){
						this.fireChangedZone(event,nextZone, ZoneChangeType.ENTER);
					}
					this.fireMovedInZone(event, nextZone);
					if(from&&!to){
						this.fireChangedZone(event,nextZone, ZoneChangeType.EXIT);
					}
					

				}
				
			}
			
		}
	}
	
	private void fireChangedZone(PlayerMoveFullBlockEvent originalEvent, Zone targetZone, ZoneChangeType changeType){
		PlayerChangeZoneEvent event = new PlayerChangeZoneEvent(originalEvent.getPlayer(),  targetZone, changeType, originalEvent);
		Plugin.pluginManager.callEvent(event);		
	}
	
	private void fireMovedInZone(PlayerMoveFullBlockEvent originalEvent, Zone targetZone){
		PlayerMoveInZoneEvent event = new PlayerMoveInZoneEvent(originalEvent.getPlayer(),  targetZone, originalEvent);
		Plugin.pluginManager.callEvent(event);		
	}

}


