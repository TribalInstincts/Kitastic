package org.kitastic.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event; 
import org.bukkit.event.HandlerList; 
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class PlayerMoveFullBlockEvent extends Event { 
	private static final HandlerList handlers = new HandlerList(); 
	private Player player;
	private PlayerMoveEvent sourceEvent;
	private Location originalFrom;
	private int movedBlocks;
	
	public PlayerMoveFullBlockEvent(Player mover, int movedBlocks, Location originalFrom, PlayerMoveEvent sourceEvent) { 
			this.player = mover;
			this.movedBlocks = movedBlocks;
			this.sourceEvent = sourceEvent;
			this.originalFrom = originalFrom;
	} 

	public Player getPlayer(){
		return this.player;
	}
	public PlayerMoveEvent getSourceEvent(){
		return this.sourceEvent;
	}
	public int getBlocksMoved(){
		return this.movedBlocks;
	}
	public Location getFrom(){
		return this.originalFrom;
	}
	public Location getTo(){
		return this.sourceEvent.getTo();
	}
	public double getDistance(){
		return originalFrom.distance(sourceEvent.getTo());
	}
	
	public HandlerList getHandlers() { 
			return handlers; 
	} 
	public static HandlerList getHandlerList() { 
			return handlers; 
		} 
}