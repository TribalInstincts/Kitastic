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
import org.kitastic.kit.GenericKit;



public class kitair extends GenericKit {

	public long lastUsed;
	public long lastFlown;
	
	public kitair(Player targetPlayer, Kitastic plugin) {
		super(targetPlayer, plugin);
		this.name = "Air";
		this.lastUsed = 0;
		this.handlerLists = Arrays.asList(PlayerInteractEvent.getHandlerList(),PlayerToggleSneakEvent.getHandlerList());
		this.lastFlown = 0;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		if(event.getPlayer() == this.player&&event.getAction().toString().startsWith("RIGHT")){
		if(this.player.getItemInHand().getTypeId()!=369){
			return;
		}
		if(this.player.getFoodLevel()<4){
			this.player.sendMessage("You don't have enough energy for that!");
			return;
		}
			if((this.player.getWorld().getFullTime() - this.lastUsed)>150){// fix iterator and put up 1 first
				Location pLoc = this.player.getLocation();
				BlockIterator airCylBlocks = new BlockIterator(this.player.getWorld(), pLoc.toVector(),pLoc.getDirection(), 0,15);
				List<Entity> entities =  this.player.getNearbyEntities(25, 25, 25);

			    this.player.getWorld().playSound(pLoc, Sound.BREATH, 3, 1);
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
				this.player.setFoodLevel(this.player.getFoodLevel()-4);
				this.lastUsed = this.player.getWorld().getFullTime();
			}else{
				this.player.sendMessage("Air recharging.");
			}
		}

	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onSneak(PlayerToggleSneakEvent event){
		
		if(event.getPlayer() == this.player&&event.isSneaking()){

			Block belowBlock = this.player.getLocation().getBlock().getRelative(BlockFace.DOWN);
			double bY = belowBlock.getY();
			double pY = this.player.getLocation().getY();
			
			if((pY-bY)>1&&(this.player.getWorld().getFullTime()-this.lastFlown)>100){
				if(this.player.getFoodLevel()>2){
					this.player.setVelocity(this.player.getLocation().getDirection().multiply(1.25));
					this.player.setFallDistance(0);
					this.lastFlown = this.player.getWorld().getFullTime();
					this.player.setFoodLevel(this.player.getFoodLevel()-3);		
					event.setCancelled(true);
				}else{
					this.player.sendMessage("You don't have enough energy to fly!");
				}
			}
			
				
			
			
		}
	}
	
	public static String getDescription(){
		return "§4Air elemental kit. Control the very skies with two unique mana-based abilities:\n" +
				"§eSpell:\n§fRight click with your blaze rod wand to hurl your enemies away, or into hazard.\n" +
				"§eMoevement:\n§fCrouch while in midair to ride a current of wind in the direction you face.";
	}
}


	
