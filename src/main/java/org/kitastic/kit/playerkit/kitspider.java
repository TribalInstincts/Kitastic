package org.kitastic.kit.playerkit;

import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.kitastic.Kitastic;
import org.kitastic.events.PlayerChangeZoneEvent;
import org.kitastic.events.PlayerMoveFullBlockEvent;
import org.kitastic.events.PlayerMoveInZoneEvent;
import org.kitastic.kit.GenericKit;
import org.kitastic.utils.Zone;

public class kitspider extends GenericKit{
	public long lastused;
	public kitspider(Player targetPlayer, Kitastic plugin){
	
		super(targetPlayer, plugin);
		this.name = "Spider";
		this.handlerLists = Arrays.asList(PlayerInteractEvent.getHandlerList(),EntityDamageEvent.getHandlerList(),PlayerRespawnEvent.getHandlerList());
		this.lastused = 0;
		ItemStack web = new ItemStack(30,5);
		this.player.getInventory().addItem(web);
	}
	
	@EventHandler
	public void onUse(PlayerInteractEvent event){
		if(event.getPlayer() == this.player&&event.getAction().toString().startsWith("RIGHT")){
			if(this.player.getItemInHand().getTypeId()!=30){
				return;
			}
			event.setCancelled(true);
			if(this.lastused + 120 > player.getWorld().getFullTime()){
				player.sendMessage("Your web is still regenerating!");
				return;
			}
			Vector target = player.getTargetBlock(null, 100).getLocation().toVector();
			if(player.getLocation().toVector().distance(target)>30){
				player.sendMessage("You're not man enough for that kind of distance!");
				return;
			}
			ItemStack web = this.player.getInventory().getItemInHand();
			if(web.getAmount()<2){
				player.getInventory().remove(web);
			}else{
				web.setAmount(web.getAmount()-1);
			}
			
			this.lastused = player.getWorld().getFullTime();
			this.player.teleport(this.player.getLocation().add(new Location(this.player.getWorld(),0,1.1,0)));
			Vector origin = player.getLocation().toVector();
			Vector trueTarget = target.subtract(origin);
			player.setVelocity(this.calculateVelocity(new Vector(0,0,0), trueTarget, 2));
		}

	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent event){
		if(event.getEntity() instanceof Player&& event.getEntity()==this.player&&event.getCause() == DamageCause.FALL){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		if(event.getPlayer() == this.player){
			ItemStack web = new ItemStack(30,5);
			event.getPlayer().getInventory().addItem(web);
		}
	}
	
	 public Vector calculateVelocity(Vector from, Vector to, int gain)
	 {
		 
		 Vector nf = new Vector(0,0,0);
		 Vector nt = to.subtract(from);
		 Vector flat = new Vector(nt.getX(),0,nt.getZ());
		 double flatDist = nt.length();
		 Vector midPoint = nf.midpoint(nt);
		 double forceV = this.iterateGrav(Math.abs(midPoint.getY()*2)+gain);
		 double forceH = this.iterateTrajectory(flatDist);
		 Vector vVector = new Vector(0,forceV,0);
		 Vector hVector = flat.normalize().multiply(forceH);
		 Vector launchVector = hVector.add(vVector);
		 return launchVector;
	 }
	 
	 public double iterateTrajectory(double dist){
		 double currentDist = 0;
		 double currentForce = .2;
		 while(currentDist < dist){
			 currentDist += currentForce;
			 currentForce = (currentForce * 1.08);
		 }
		 return currentForce;
	 }
	 
	 public double iterateGrav(double dist){
		 double currentDist = 0;
		 double currentForce = 0;
		 while(currentDist < dist){
			 currentDist += currentForce;
			 currentForce += .05;
			 currentForce = (currentForce * 1.08);
		 }
		 return currentForce;
	 }

		
		public void unRegister(){
			for(HandlerList l : this.handlerLists){
				l.unregister(((Listener)this));
			}
		}	
		
		public static String getDescription(){
			return "§4Item based kit:\n§fRight click with a Spider's web in your hands and take on the web-slinging powers of an arachnid.\nSafely fling yourself to even " +
					"the most unreachable locations, and never worry about fall damage again.";
		}
		
		@EventHandler
		public void onChangeZone(PlayerChangeZoneEvent event){
			this.player.sendMessage(event.getChangeType().toString()+" zone "+event.getZone().name+". ");
		}
		@EventHandler 
		public void onMoveInZone(PlayerMoveInZoneEvent event){
			this.player.sendMessage("Moved in zone "+event.getZone().name+". ");

		}
		
}
