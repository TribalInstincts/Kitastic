package org.kitastic.kit.playerkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.kitastic.Kitastic;
import org.kitastic.block.savedBlock;
import org.kitastic.kit.genericKit;
import org.kitastic.kit.scheduledTask;

import java.lang.Runnable;
import java.lang.reflect.Method;



public class kitwater extends genericKit {

	public Map<Block, Integer> toFix;
	public long waitUntil;
	public Player thisPlayer;
	public ArrayList<Location> gyserLocs;
	public ArrayList<String> spawned;
	public double movedBlocks;
	private Boolean waterWalk;
	private long sneakTapped;

	
	public kitwater(Player targetPlayer, Kitastic plugin) {
		super(targetPlayer, plugin);
		this.spawned = new ArrayList<String>();
		this.thisPlayer = targetPlayer;
		this.waitUntil = 0;
		this.gyserLocs = new ArrayList<Location>();
		this.waterWalk = false;
		this.sneakTapped = 0;
//		this.handlerLists = Arrays.asList(PlayerInteractEvent.getHandlerList(),EntityChangeBlockEvent.getHandlerList(),ItemSpawnEvent.getHandlerList(),PlayerMoveEvent.getHandlerList(),PlayerToggleSneakEvent.getHandlerList());
		
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){

			if(event.getPlayer() == this.thisPlayer&&event.getAction().toString().startsWith("RIGHT")){
				if(this.thisPlayer.getItemInHand().getTypeId()!=369){
					return;
				}
				long currentTime = this.thisPlayer.getWorld().getFullTime();
				if(currentTime > this.waitUntil){
					if(this.thisPlayer.getFoodLevel()<4){
						this.thisPlayer.sendMessage("You don't have enough energy!");
						return;
					}
					this.waitUntil = currentTime + 250;
					Location pLoc = this.thisPlayer.getLocation();
					this.thisPlayer.getWorld().playSound(pLoc, Sound.NOTE_PLING, 1, 0);
					this.thisPlayer.setFoodLevel(this.thisPlayer.getFoodLevel()-4);

					Entity fd = this.thisPlayer.getWorld().spawnFallingBlock(event.getPlayer().getEyeLocation(), 9, (byte) 7);
					this.spawned.add(fd.getUniqueId().toString());
					fd.setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
				}else{
					this.thisPlayer.sendMessage("Water is recharging!");
				}
			}

	}
	
	@EventHandler
	public void onBlockLand(EntityChangeBlockEvent event) throws NoSuchMethodException, SecurityException{
		if(this.spawned.contains(event.getEntity().getUniqueId().toString())){
			this.gyser(event.getEntity());
			event.setCancelled(true);
			event.getBlock().setTypeId(0);
			
			
		}
	}
	
	@EventHandler
	public void one(ItemSpawnEvent event) throws NoSuchMethodException, SecurityException{
		List<Entity> ents = event.getEntity().getNearbyEntities(2, 2, 2);
		for(Entity e : ents){
			
			if(this.spawned.contains(e.getUniqueId().toString())){
				event.getEntity().remove();
				this.spawned.remove(e);
				this.gyser(e);
				return;
			}
		
		}
	}
	
	@EventHandler
	public void onMoveInWater(PlayerMoveEvent event){
		if(event.getPlayer() == this.thisPlayer){
			int inType = this.thisPlayer.getLocation().getBlock().getTypeId();

			
			if(this.waterWalk&&this.thisPlayer.getFoodLevel()>1&&(inType==8||inType==9)){
				Vector dir = event.getTo().subtract(event.getFrom()).toVector();
				this.movedBlocks += dir.length();
				thisPlayer.setVelocity(thisPlayer.getLocation().getDirection().multiply(.5));
				if(this.movedBlocks >= 6){
					this.movedBlocks = 0;
					thisPlayer.setFoodLevel(thisPlayer.getFoodLevel()-1);
				}
				
			}else{
				this.waterWalk = false;
			}
		}
	}
	
	@EventHandler
	public void onToggleSneak(PlayerToggleSneakEvent event){
		if(event.getPlayer() == this.thisPlayer){
			
			if(event.isSneaking()){
				if(!this.waterWalk){
					if(thisPlayer.getWorld().getFullTime()<this.sneakTapped+30){
						this.waterWalk = !this.waterWalk;
					}
				}else{
					this.waterWalk = false;
				}
			}
			this.sneakTapped = thisPlayer.getWorld().getFullTime();
		}
	}

	
	public void gyser(Entity e) throws NoSuchMethodException, SecurityException{
		Block targetBlock = e.getLocation().getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
		int z = targetBlock.getZ();
		int x = targetBlock.getX();
		int y = targetBlock.getY();
		int radius = 5;
		World thisWorld = this.thisPlayer.getWorld();
		Method gyserMethod = this.getClass().getMethod("removeGyser", savedBlock.class);
		ArrayList<Block> waterBlocks = new ArrayList<Block>();
		for(int iz = (z - radius);iz<=(z+radius);iz++){
			for(int ix = (x - radius);ix<=(x+radius);ix++){
				for(int iy = (y - radius);iy<=(y+radius);iy++){
					
					Block selectedBlock = thisWorld.getBlockAt(ix,iy,iz);
					savedBlock sphereBlock = new savedBlock(selectedBlock, selectedBlock.getTypeId(), gyserMethod, this);
					this.Plugin.blockFixer.queueBlock(200, sphereBlock);
					double distance = targetBlock.getLocation().distance(selectedBlock.getLocation());
					int sZ = selectedBlock.getZ();
					int sX = selectedBlock.getX();
					int sY = selectedBlock.getY();
					boolean adjacentToAir=false;
					for(int sZi = -1;sZi < 2;sZi++){
						for(int sXi = -1;sXi < 2;sXi++){
							if(thisWorld.getBlockAt(sX+sXi,sY,sZ+sZi).getTypeId()==0){
								adjacentToAir = true;
								break;
							}
						}
					}
					if(!adjacentToAir&&distance<radius-1){
						waterBlocks.add(selectedBlock);
					}
				}
			}
		}
		for(Block b : waterBlocks){
		    b.setTypeId(8);
		}
		List<Entity> nearbyEnts = e.getNearbyEntities(radius-1, radius-1, radius-1);
		if(nearbyEnts.size() > 0){
			Block gyserTip = targetBlock.getRelative(0, 5, 0);
			e.getWorld().playSound(e.getLocation(), Sound.EXPLODE, 1, 0);
			e.getWorld().playSound(e.getLocation(), Sound.SWIM, 1, 0);
			this.Plugin.blockFixer.queueBlock(40, new savedBlock(gyserTip, gyserTip.getTypeId()));
			gyserTip.setTypeId(8);
			for(Entity iEnt : nearbyEnts){
				if(iEnt instanceof LivingEntity){
					iEnt.setVelocity(new Vector(0,5,0));
				}
			}
			
		}
	}
	
	public boolean removeGyser(savedBlock block){
		block.rawRevert();
		return true;
	}
	

	
	public void undoLiquify(){
		for(Iterator<Block> i = this.toFix.keySet().iterator();i.hasNext();){
			Block thisBlock = i.next();
			thisBlock.setTypeId(this.toFix.get(thisBlock));
			this.Plugin.alteredLocations.remove(thisBlock.getLocation());
		}	
	}
	
	
}


	
