package org.kitastic.kit;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.kitastic.Kitastic;


public class genericKit implements Listener {
	public Player thisPlayer;
    public Kitastic Plugin;
	public List<HandlerList> handlerLists;
	public static String name;
	public static ArrayList<String> metaTypes;

	public genericKit(Player targetPlayer, Kitastic plugin){
		this.thisPlayer = targetPlayer;
		this.Plugin = plugin;
		this.Plugin.pluginManager.registerEvents(this, plugin);
	}
	
	public void unRegister(){
		for(HandlerList l : this.handlerLists){
			l.unregister(((Listener)this));
		}
	}	
	
	public static String getDescription(){
		return "";
	}
}
