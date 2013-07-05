	

    package org.kitastic;
     

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
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
import org.kitastic.kit.KitManager;
import org.kitastic.kit.GenericKit;
import org.kitastic.kit.serverkit.hungerRegen;
import org.kitastic.player.OnlinePlayer;
import org.kitastic.server.ServerListener;
import org.kitastic.utils.MovementBroadcaster;
import org.kitastic.utils.CallbackRunner;
import org.kitastic.utils.ScheduledTaskManager;
import org.kitastic.utils.ZoneManager;

import com.tribalinstincts.minecraft.nexus.modules.core.NexusCore;
import com.tribalinstincts.minecraft.nexus.modules.core.NexusPlayer;
import com.tribalinstincts.minecraft.nexus.modules.core.modules.ModuleType;
import com.tribalinstincts.minecraft.nexus.modules.core.modules.NexusModuleCommandException;
import com.tribalinstincts.minecraft.nexus.modules.core.modules.NexusModuleDependencyException;
import com.tribalinstincts.minecraft.nexus.modules.core.modules.TrapModule;
import com.tribalinstincts.minecraft.nexus.modules.traps.TrapManager;
     
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
                    CallbackRunner runner;
					try {
						runner = new CallbackRunner(this, this.getClass().getMethod("getTPS", null));
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

