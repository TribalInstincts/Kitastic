	

package org.kitastic;
     

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitastic.block.blockRestorer;
import org.kitastic.kit.KitManager;
import org.kitastic.kit.serverkit.hungerRegen;
import org.kitastic.player.OnlinePlayer;
import org.kitastic.server.ServerListener;
import org.kitastic.utils.Callback;
import org.kitastic.utils.MovementBroadcaster;
import org.kitastic.utils.CallbackRunner;
import org.kitastic.utils.ScheduledTaskManager;
import org.kitastic.utils.ZoneManager;

     
    public class Kitastic extends JavaPlugin implements Listener{
           
    		public DbManager db;
            public PluginManager pluginManager;
            public ServerListener playerListener;
            public Map<Player, OnlinePlayer> playerList;
            public ArrayList<Location> alteredLocations;
            public blockRestorer blockFixer;
            public MovementBroadcaster movementBroadcaster;
            public ZoneManager zoneManager;
            public KitManager km;
            public ScheduledTaskManager tm;

            
            @Override
            public void onEnable() {
            		this.db = new DbManager();
            		this.km = new KitManager(this);
            		this.alteredLocations = new ArrayList<Location>();
					this.playerList = new HashMap<Player, OnlinePlayer>();
                    this.pluginManager = this.getServer().getPluginManager();
                    this.playerListener = new ServerListener(this);
            		this.movementBroadcaster = new MovementBroadcaster(this);
            		this.zoneManager = new ZoneManager(this);
                    //this.getCommand("kit").setExecutor(this.commander);
                    Runnable hungerRegen = new hungerRegen(this.getServer().getWorlds());
                    this.getServer().getScheduler().scheduleSyncRepeatingTask(this, hungerRegen, 160, 160);
                    Bukkit.getLogger().info("PluginTemplate loaded!");
                    this.pluginManager.registerEvents(this, this);
                    this.blockFixer = new blockRestorer(this.getServer().getWorlds().get(0),this);
                    this.tm = new ScheduledTaskManager(this);
                    Callback callback = new Callback(this,"getTPS",null);
                    CallbackRunner runner = new CallbackRunner(callback);
            		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, runner,30, 30);

            }
     
            @Override
            public void onDisable() {
     
            }
            
            public void getTPS(){
        		//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tps");
            }
            
            public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
            	OnlinePlayer oP = null;
            	try{
        		oP = this.playerList.get(sender);
            	}catch(Exception e){
            		e.printStackTrace();
            		Bukkit.broadcastMessage(sender.getName());
            		Bukkit.broadcastMessage(this.playerList.toString());
            	}

            	String command = cmd.getName();
            	switch(command){
            		case("kit"):
                		if(args.length==1){
                			this.km.toggleKit(args[0], oP.player);
                		}else{
                			this.km.sayKits(oP);
            				sender.sendMessage(cmd.getDescription());
                			sender.sendMessage(cmd.getUsage());
                		}
            			break;
            		case("kitinfo"):
            			if(args.length!=1){
            				sender.sendMessage(cmd.getDescription());
            				sender.sendMessage(cmd.getUsage());
            				return true;
            			}
            			String description;
            			Class<?> acquiredClass;
            			try{
            			acquiredClass =  Class.forName("org.kitastic.kit.playerkit.kit"+args[0]);
            			Method getDesc = acquiredClass.getMethod("getDescription",null);
            			description = (String) getDesc.invoke(null,null);
            			sender.sendMessage(description);
            			}catch(Exception e){
            				e.printStackTrace();
            				km.sayKits(oP);
            				sender.sendMessage("No kit by that name. Try again.");
            				return true;
            			}
            			
            			break;
            		case("help"):
            			sender.sendMessage("Commands:\n\\kit [kitname] - select a kit.\n\\kitinfo [kitname] - get info about a kit.");
            			break;
            		default:
            			sender.sendMessage("Bad command. Try again plox.");
            			break;
            	}
            	return true;
            		
            }

    }

