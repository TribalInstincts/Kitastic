	

    package org.kitastic.server;
     
     


import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.kitastic.Kitastic;
import org.kitastic.player.onlinePlayer;
     
    public class ServerListener implements Listener{
            private Kitastic plugin;
           
            public ServerListener(Kitastic plugin) {
                    this.plugin = plugin;
                    plugin.pluginManager.registerEvents(this, plugin);
            }      
            
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event){
            	onlinePlayer newPlayer = new onlinePlayer(event.getPlayer(), this.plugin);
            	this.plugin.playerList.put(event.getPlayer(),newPlayer);
        		
        		if(!event.getPlayer().getInventory().contains(369)){
        			ItemStack wand = new ItemStack(369);
        			event.getPlayer().getInventory().addItem(wand);
        		}
            }
            
            @EventHandler
            public void onPlayerRespawn(PlayerRespawnEvent event){
        		
        		if(!event.getPlayer().getInventory().contains(369)){
        			ItemStack wand = new ItemStack(369);
        			event.getPlayer().getInventory().addItem(wand);
        		}
        		if(!event.getPlayer().getInventory().contains(272)){
        			ItemStack sword = new ItemStack(272);
        			event.getPlayer().getInventory().addItem(sword);
        		}
        		if(!event.getPlayer().getInventory().contains(261)){
        			ItemStack bow = new ItemStack(261);
        			event.getPlayer().getInventory().addItem(bow);
        		}
        		if(!event.getPlayer().getInventory().contains(262)){
        			ItemStack arrows = new ItemStack(262,64);
        			event.getPlayer().getInventory().addItem(arrows);
        		}
            }
            
        	@EventHandler(priority = EventPriority.HIGH)
        	public void onHungerChange(FoodLevelChangeEvent event){
        		if(event.getEntity() instanceof Player){
        			Player targetPlayer = (Player) event.getEntity();
        			int current = targetPlayer.getFoodLevel();
        			int changed = event.getFoodLevel();
        			if(current-changed==1){
        				event.setCancelled(true);
        			}
        			
        		}
        	}
        	
        	@EventHandler
        	public void onSprintInQuicksand(PlayerMoveEvent event){
        		if(event.getPlayer().getLocation().getBlock().getTypeId() == 88){
        			double bY = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().getY();
        			double pY = event.getPlayer().getLocation().getY();
        			if((pY-bY)>1){
        				event.getPlayer().setVelocity(new Vector(0,-2,0));
        			}
        		}
        	}

           
    }

