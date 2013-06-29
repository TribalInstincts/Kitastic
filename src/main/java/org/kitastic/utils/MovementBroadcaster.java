package org.kitastic.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.kitastic.Kitastic;
import org.kitastic.events.PlayerMoveFullBlockEvent;

public class MovementBroadcaster implements Listener{
	public Map<Player, Location> playerList;
	public Kitastic Plugin;
	
	public MovementBroadcaster(Kitastic plugin){

		this.playerList = new HashMap<Player, Location>();
		this.Plugin = plugin;
		this.Plugin.pluginManager.registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		Player mover = event.getPlayer();
		if(!playerList.keySet().contains(mover)){
			this.playerList.put(mover, event.getFrom());
		}
		Double moved = this.playerList.get(mover).distance(event.getTo());
		if(moved > 1){
			int movedBlocks = (int) (moved - (moved % 1));
			this.fireMovedBlock(movedBlocks, mover, event,this.playerList.get(mover));
			this.playerList.remove(mover);
			this.playerList.put(mover, event.getTo());

		}
	}
	
	public void fireMovedBlock(int movedBlocks, Player mover, PlayerMoveEvent originalEvent, Location originalFrom){
		PlayerMoveFullBlockEvent event = new PlayerMoveFullBlockEvent(mover, movedBlocks, originalFrom, originalEvent);
		Plugin.pluginManager.callEvent(event);		
	}
}
