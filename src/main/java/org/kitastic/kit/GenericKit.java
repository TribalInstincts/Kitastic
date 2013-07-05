package org.kitastic.kit;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.kitastic.Kitastic;


public class GenericKit implements Listener {
	public Player player;
    public Kitastic Plugin;
	public List<HandlerList> handlerLists;
	public static String name;
	public static ArrayList<String> metaTypes;

	public GenericKit(Player targetPlayer, Kitastic plugin){
		this.player = targetPlayer;
		this.Plugin = plugin;
		this.Plugin.pluginManager.registerEvents(this, plugin);
		
	}
	
	public void unRegister(){
		for(HandlerList l : this.handlerLists){
			l.unregister(((Listener)this));
		}
	}	
	@EventHandler
	public static String getDescription(){
		return "";
	}
}
