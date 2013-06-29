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
import org.kitastic.kit.genericKit;

public class kitlauncher extends genericKit{
	public long lastused;
	public kitlauncher(Player targetPlayer, Kitastic plugin) throws IOException {
	
		super(targetPlayer, plugin);
		this.handlerLists = Arrays.asList(PlayerInteractEvent.getHandlerList(),EntityDamageEvent.getHandlerList(),PlayerRespawnEvent.getHandlerList());
		this.lastused = 0;
		ItemStack web = new ItemStack(30,5);
		this.thisPlayer.getInventory().addItem(web);
	}
	
	@EventHandler
	public void onUse(PlayerInteractEvent event){
		if(event.getPlayer() == this.thisPlayer&&event.getAction().toString().startsWith("RIGHT")){
			if(this.thisPlayer.getItemInHand().getTypeId()!=30){
				return;
			}
			if(this.lastused + 40 > thisPlayer.getWorld().getFullTime()){
				thisPlayer.sendMessage("Your web is still regenerating!");
				return;
			}
			Vector target = thisPlayer.getTargetBlock(null, 100).getLocation().toVector();
			if(thisPlayer.getLocation().toVector().distance(target)>30){
				thisPlayer.sendMessage("You're not man enough for that kind of distance!");
				return;
			}
			ItemStack web = this.thisPlayer.getInventory().getItemInHand();
			if(web.getAmount()<2){
				thisPlayer.getInventory().remove(web);
			}else{
				web.setAmount(web.getAmount()-1);
			}
			event.setCancelled(true);
			this.lastused = thisPlayer.getWorld().getFullTime();
			this.thisPlayer.teleport(this.thisPlayer.getLocation().add(new Location(this.thisPlayer.getWorld(),0,1.1,0)));
			Vector origin = thisPlayer.getLocation().toVector();
			Vector trueTarget = target.subtract(origin);
			thisPlayer.setVelocity(this.calculateVelocity(new Vector(0,0,0), trueTarget, 2));
		}
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent event){
		if(event.getEntity() instanceof Player&& event.getEntity()==this.thisPlayer&&event.getCause() == DamageCause.FALL){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){
		if(event.getPlayer() == this.thisPlayer){
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
			 currentForce += .08;
			 currentForce = (currentForce * 1.08);
		 }
		 return currentForce;
	 }

		
		public void unRegister(){
			for(HandlerList l : this.handlerLists){
				l.unregister(((Listener)this));
			}
		}	
		
		

}
