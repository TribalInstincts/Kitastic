	

    package org.kitastic;
     

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitastic.block.blockRestorer;
import org.kitastic.kit.scheduledTask;
import org.kitastic.player.onlinePlayer;
import org.kitastic.server.ServerListener;
import org.kitastic.server.hungerRegen;
     
    public class Kitastic extends JavaPlugin implements Listener{
           
            public PluginManager pluginManager;
            public ServerListener playerListener;
            public KitasticCommandExecutor commander;
            public Map<Player, onlinePlayer> playerList;
            public ArrayList<Location> alteredLocations;
            public blockRestorer blockFixer;
           
            @Override
            public void onEnable() {
            		this.alteredLocations = new ArrayList<Location>();
					this.playerList = new HashMap<Player, onlinePlayer>();
					this.commander = new KitasticCommandExecutor(this);
                    this.pluginManager = this.getServer().getPluginManager();
                    this.playerListener = new ServerListener(this);
                    this.getCommand("kit").setExecutor(this.commander);
                    Runnable hungerRegen = new hungerRegen(this.getServer().getWorlds());
                    this.getServer().getScheduler().scheduleSyncRepeatingTask(this, hungerRegen, 160, 160);
                    Bukkit.getLogger().info("PluginTemplate loaded!");
                    this.pluginManager.registerEvents(this, this);
                    this.blockFixer = new blockRestorer(this.getServer().getWorlds().get(0),this);
                    scheduledTask runner;
					try {
						runner = new scheduledTask(this, this.getClass().getMethod("getTPS", null));
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
            		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, runner,30, 30);
                   
            }
     
            @Override
            public void onDisable() {
     
            }
            
            public void getTPS(){
        		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tps");
            }

            



    }

