package org.kitastic.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event; 
import org.bukkit.event.HandlerList; 
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.kitastic.utils.zones.Zone;

public class PlayerMoveInZoneEvent extends Event { 
	private static final HandlerList handlers = new HandlerList(); 
	private Player player;
	private PlayerMoveFullBlockEvent sourceEvent;
	private Zone zone;

	
	public PlayerMoveInZoneEvent(Player mover, Zone targetZone, PlayerMoveFullBlockEvent sourceEvent) { 
			this.player = mover;
			this.sourceEvent = sourceEvent;
			this.zone = targetZone;
	} 

	public Player getPlayer(){
		return this.player;
	}
	public PlayerMoveFullBlockEvent getSourceEvent(){
		return this.sourceEvent;
	}
	public Location getFrom(){
		return this.sourceEvent.getFrom();
	}
	public Location getTo(){
		return this.sourceEvent.getTo();
	}
	public Zone getZone(){
		return this.zone;
	}
	public HandlerList getHandlers() { 
			return handlers; 
	} 
	public static HandlerList getHandlerList() { 
			return handlers; 
		} 
}