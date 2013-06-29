package org.kitastic.kit.playerkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.lang.Runnable;
import java.lang.reflect.Method;
import org.kitastic.Kitastic;
import org.kitastic.block.savedBlock;
import org.kitastic.kit.genericKit;
import org.kitastic.kit.scheduledTask;



public class kitearth extends genericKit {

	public Map<Block, Integer> toFix;
	public ArrayList<savedBlock> toFixWall;
	public long waitUntil;
	public Player thisPlayer;
	public ArrayList<String> spawned;
	public int radius;
	public Method wallWalkMethod;
	public Method splashMethod;
	

	
	public kitearth(Player targetPlayer, Kitastic plugin) throws NoSuchMethodException, SecurityException {
		
		super(targetPlayer, plugin);
		this.spawned = new ArrayList<String>();
		this.toFix = new HashMap<Block,Integer>();
		this.toFixWall = new ArrayList<savedBlock>();
		this.waitUntil = 0;
		this.thisPlayer = targetPlayer;
		this.radius = 4;
		this.wallWalkMethod =  this.getClass().getDeclaredMethod("undoWallWalk", savedBlock.class);
		this.handlerLists = Arrays.asList(PlayerInteractEvent.getHandlerList(),EntityChangeBlockEvent.getHandlerList(),ItemSpawnEvent.getHandlerList(),PlayerMoveEvent.getHandlerList());

		
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
					this.thisPlayer.getWorld().playSound(pLoc, Sound.NOTE_BASS_DRUM, 1, 0);
					this.thisPlayer.setFoodLevel(this.thisPlayer.getFoodLevel()-4);

					Entity fd = this.thisPlayer.getWorld().spawnFallingBlock(event.getPlayer().getEyeLocation(), 3, (byte) 7);
					this.spawned.add(fd.getUniqueId().toString());
					fd.setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
				}else{
					this.thisPlayer.sendMessage("Earth is recharging!");
				}
			}

	}
	
	@EventHandler
	public void onBlockLand(EntityChangeBlockEvent event){
		if(this.spawned.contains(event.getEntity().getUniqueId().toString())){
			this.doSplash(event.getEntity());
			event.setCancelled(true);
			event.getBlock().setTypeId(0);
			
			
		}
	}
	
	@EventHandler
	public void one(ItemSpawnEvent event){
		List<Entity> ents = event.getEntity().getNearbyEntities(2, 2, 2);
		for(Entity e : ents){
			
			if(this.spawned.contains(e.getUniqueId().toString())){
				event.getEntity().remove();
				this.spawned.remove(e);
				this.doSplash(e);
				return;
			}
		
		}
	}

	private void doSplash(Entity fb){
			
			Location fbLoc = fb.getLocation();
			fb.getWorld().playSound(fbLoc, Sound.STEP_GRAVEL, 2, 0);
			fb.getWorld().playSound(fbLoc, Sound.SWIM, 1, -2);
			
			
			this.spawned.remove(fb);
			int x = (int) fbLoc.getX();
			int y = (int) fbLoc.getY();
			int z = (int) fbLoc.getZ();
			List<Integer> types = Arrays.asList(0,18,8,9,10,31,32,37,38,39,40,78,83,106,111,127);
			for(int iz = (z - radius);iz<=(z+radius);iz++){
				for(int ix = (x - radius);ix<=(x+radius);ix++){
					for(int iy = (y - radius);iy<=(y+radius);iy++){
						Block selectedBlock = fb.getWorld().getBlockAt(ix,iy,iz);
						int typeID = selectedBlock.getTypeId();
						double Distance = fbLoc.distance(selectedBlock.getLocation());
						if(Distance<=((double)this.radius+.5)){
							if(!types.contains(typeID)){
								if(types.contains(selectedBlock.getRelative(BlockFace.UP,1).getTypeId())){
										this.Plugin.blockFixer.queueBlock(200, new savedBlock(selectedBlock, selectedBlock.getTypeId()));
								    	selectedBlock.setTypeId(88);
								    	Block downBlock = selectedBlock.getRelative(BlockFace.DOWN);
										this.Plugin.blockFixer.queueBlock(200, new savedBlock(downBlock, downBlock.getTypeId()));
								    	downBlock.setTypeId(79);
								}
							}
						}
						
					}
				}
			}
	}
	
	@EventHandler
	public void onMoveIntoWall(PlayerMoveEvent event){
		if(event.getPlayer() == this.thisPlayer){
			Vector dir = event.getTo().subtract(event.getFrom()).multiply(100).toVector();
			if(this.thisPlayer.isSneaking()&&this.thisPlayer.getFoodLevel()>2&&dir.length()>0){
				BlockIterator bi = new BlockIterator(this.thisPlayer.getWorld(), event.getFrom().toVector(), dir, 0, 2);
				boolean blocksBroken = false;
				while (bi.hasNext()) {
					Block thisBlock = bi.next();
					Block topBlock = thisBlock.getRelative(BlockFace.UP);
					
					if(thisBlock.getTypeId()!=0){
						this.Plugin.blockFixer.queueBlock(30, new savedBlock(thisBlock, thisBlock.getTypeId(), this.wallWalkMethod ,this));
						thisBlock.setTypeId(0);
						blocksBroken = true;
					}
					if(topBlock.getTypeId()!=0){
						this.Plugin.blockFixer.queueBlock(30, new savedBlock(topBlock, topBlock.getTypeId(),this.wallWalkMethod,this));
						topBlock.setTypeId(0);
						blocksBroken = true;
					}
				}
				if(blocksBroken){
					this.thisPlayer.setFoodLevel(this.thisPlayer.getFoodLevel()-2);
					this.thisPlayer.playSound(this.thisPlayer.getLocation(), Sound.STEP_GRAVEL, 1, -5);
				}
			}
		}
	}
	

	
	public boolean undoWallWalk(savedBlock block){
		List<Location> badLocs = Arrays.asList(this.thisPlayer.getLocation().getBlock().getLocation(),this.thisPlayer.getEyeLocation().getBlock().getLocation());		
		if(!badLocs.contains(block.referencedBlock.getLocation())){
			block.rawRevert();
			return true;
		}
		return false;
	}
}


	
