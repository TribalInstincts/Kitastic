package org.kitastic.utils.movement;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kitastic.Kitastic;
import org.kitastic.events.PlayerMoveFullBlockEvent;

public class MovementBroadcaster implements Listener{
	public Kitastic Plugin;
	public Map<Player,Location> playerList;
	public double[] ttls;
	public int[] count;
	public DecimalFormat format;
	public String orig;

	
	public MovementBroadcaster(Kitastic plugin){
		this.playerList = new HashMap<Player,Location>();
		this.Plugin = plugin;
		this.Plugin.pluginManager.registerEvents(this, plugin);
		this.format = new DecimalFormat("#.###");
		
	}
	
	

	@EventHandler
	public void onPlayerMoveOrig(PlayerMoveEvent event){
		Player mover = event.getPlayer();
		if(!playerList.keySet().contains(mover)){
			this.playerList.put(mover, event.getFrom());
		}
		Location oldFrom = this.playerList.get(mover);
		Double moved = oldFrom.distance(event.getTo());
		if(moved > 1){
			int movedBlocks = (int) (moved - (moved % 1));
			this.fireMovedBlock(movedBlocks, mover, event,oldFrom);
			this.playerList.remove(mover);
			this.playerList.put(mover, event.getTo());
		}
	}
	

	
	public void fireMovedBlock(int movedBlocks, Player mover, PlayerMoveEvent originalEvent, Location originalFrom){
		PlayerMoveFullBlockEvent event = new PlayerMoveFullBlockEvent(mover, movedBlocks, originalFrom, originalEvent);
		Plugin.pluginManager.callEvent(event);		
	}
	
	@EventHandler
	public void onPlayerLogout(PlayerQuitEvent event){
		this.playerList.remove(event.getPlayer());
	}
}
