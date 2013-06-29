package org.kitastic.kit.playerkit;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;


import org.bukkit.entity.Entity;
import org.kitastic.Kitastic;
import org.kitastic.kit.genericKit;



public class kitair extends genericKit {

	public long lastUsed;
	public long lastFlown;
	
	public kitair(Player targetPlayer, Kitastic plugin) {
		super(targetPlayer, plugin);
		this.lastUsed = 0;
		this.handlerLists = Arrays.asList(PlayerInteractEvent.getHandlerList(),PlayerToggleSneakEvent.getHandlerList());
		this.lastFlown = 0;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getPlayer() == this.thisPlayer&&event.getAction().toString().startsWith("RIGHT")){
		if(this.thisPlayer.getItemInHand().getTypeId()!=369){
			return;
		}
		if(this.thisPlayer.getFoodLevel()<4){
			this.thisPlayer.sendMessage("You don't have enough energy for that!");
			return;
		}
			if((this.thisPlayer.getWorld().getFullTime() - this.lastUsed)>150){// fix iterator and put up 1 first
				Location pLoc = this.thisPlayer.getLocation();
				BlockIterator airCylBlocks = new BlockIterator(this.thisPlayer.getWorld(), pLoc.toVector(),pLoc.getDirection(), 0,15);
				List<Entity> entities =  this.thisPlayer.getNearbyEntities(25, 25, 25);

			    this.thisPlayer.getWorld().playSound(pLoc, Sound.BREATH, 3, 1);
				while (airCylBlocks.hasNext()) {
					Block thisBlock = airCylBlocks.next();
					Vector bVec = thisBlock.getLocation().toVector();
					for(Entity e : entities){
						Vector eVec = e.getLocation().toVector();
						if(eVec.isInSphere(bVec,4)){
							Vector noZ = pLoc.getDirection().normalize().multiply(new Vector(1,0,1));
							Vector addedZ = new Vector(noZ.getX(),.3,noZ.getZ()).normalize();
							Vector mult = addedZ.multiply(3);
							Vector dir0 = new Vector(mult.getX(),.5,mult.getZ());
							e.setVelocity(dir0);
						}
					}
			     }
				this.thisPlayer.setFoodLevel(this.thisPlayer.getFoodLevel()-4);
				this.lastUsed = this.thisPlayer.getWorld().getFullTime();
			}else{
				this.thisPlayer.sendMessage("Air recharging.");
			}
		}

	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSneak(PlayerToggleSneakEvent event){
		
		if(event.getPlayer() == this.thisPlayer&&event.isSneaking()){

			Block belowBlock = this.thisPlayer.getLocation().getBlock().getRelative(BlockFace.DOWN);
			double bY = belowBlock.getY();
			double pY = this.thisPlayer.getLocation().getY();
			
			if((pY-bY)>1&&(this.thisPlayer.getWorld().getFullTime()-this.lastFlown)>100){
				if(this.thisPlayer.getFoodLevel()>2){
					this.thisPlayer.setVelocity(this.thisPlayer.getLocation().getDirection().multiply(1.25));
					this.thisPlayer.setFallDistance(0);
					this.lastFlown = this.thisPlayer.getWorld().getFullTime();
					this.thisPlayer.setFoodLevel(this.thisPlayer.getFoodLevel()-3);		
					event.setCancelled(true);
				}else{
					this.thisPlayer.sendMessage("You don't have enough energy to fly!");
				}
			}
			
				
			
			
		}
	}
}


	
