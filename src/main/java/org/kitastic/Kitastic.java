	

    package org.kitastic;
     

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.kitastic.kit.genericKit;
import org.kitastic.kit.scheduledTask;
import org.kitastic.player.onlinePlayer;
import org.kitastic.server.ServerListener;
import org.kitastic.server.hungerRegen;
import org.kitastic.utils.MovementBroadcaster;
import org.kitastic.utils.ZoneManager;

import com.tribalinstincts.minecraft.nexus.modules.core.NexusCore;
import com.tribalinstincts.minecraft.nexus.modules.core.NexusPlayer;
import com.tribalinstincts.minecraft.nexus.modules.core.modules.ModuleType;
import com.tribalinstincts.minecraft.nexus.modules.core.modules.NexusModuleCommandException;
import com.tribalinstincts.minecraft.nexus.modules.core.modules.NexusModuleDependencyException;
import com.tribalinstincts.minecraft.nexus.modules.core.modules.TrapModule;
import com.tribalinstincts.minecraft.nexus.modules.traps.TrapManager;
     
    public class Kitastic extends JavaPlugin implements Listener{
           
            public PluginManager pluginManager;
            public ServerListener playerListener;
            public KitasticCommandExecutor commander;
            public Map<Player, onlinePlayer> playerList;
            public ArrayList<Location> alteredLocations;
            public blockRestorer blockFixer;
            public MovementBroadcaster movementBroadcaster;
            public ZoneManager zoneManager;

            
            @Override
            public void onEnable() {
            		this.alteredLocations = new ArrayList<Location>();
					this.playerList = new HashMap<Player, onlinePlayer>();
					this.commander = new KitasticCommandExecutor(this);
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
            
            public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        		onlinePlayer oP = this.playerList.get(sender);

            	String command = cmd.getName();
            	switch(command){
            		case("kit"):
                		if(args.length==1){
                			this.playerList.get(sender).addKit(args[0]);
                		}else{
                			oP.sayKits();
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
            				oP.sayKits();
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

