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
import org.kitastic.kit.GenericKit;
import org.kitastic.utils.scheduler.CallbackRunner;



public class kitearth extends GenericKit {

	public Map<Block, Integer> toFix;
	public ArrayList<savedBlock> toFixWall;
	public long waitUntil;
	public ArrayList<String> spawned;
	public int radius;
	public Method wallWalkMethod;
	public Method splashMethod;
	

	
	public kitearth(Player targetPlayer, Kitastic plugin) throws NoSuchMethodException, SecurityException {
		
		super(targetPlayer, plugin);
		this.name = "Earth";
		this.spawned = new ArrayList<String>();
		this.toFix = new HashMap<Block,Integer>();
		this.toFixWall = new ArrayList<savedBlock>();
		this.waitUntil = 0;
		this.player = targetPlayer;
		this.radius = 4;
		this.wallWalkMethod =  this.getClass().getDeclaredMethod("undoWallWalk", savedBlock.class);
		this.handlerLists = Arrays.asList(PlayerInteractEvent.getHandlerList(),EntityChangeBlockEvent.getHandlerList(),ItemSpawnEvent.getHandlerList(),PlayerMoveEvent.getHandlerList());

		
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){

			if(event.getPlayer() == this.player&&event.getAction().toString().startsWith("RIGHT")){
				if(this.player.getItemInHand().getTypeId()!=369){
					return;
				}
				long currentTime = this.player.getWorld().getFullTime();
				if(currentTime > this.waitUntil){
					if(this.player.getFoodLevel()<4){
						this.player.sendMessage("You don't have enough energy!");
						return;
					}
					this.waitUntil = currentTime + 250;
					Location pLoc = this.player.getLocation();
					this.player.getWorld().playSound(pLoc, Sound.NOTE_BASS_DRUM, 1, 0);
					this.player.setFoodLevel(this.player.getFoodLevel()-4);

					Entity fd = this.player.getWorld().spawnFallingBlock(event.getPlayer().getEyeLocation(), 3, (byte) 7);
					this.spawned.add(fd.getUniqueId().toString());
					fd.setVelocity(event.getPlayer().getLocation().getDirection().multiply(2));
				}else{
					this.player.sendMessage("Earth is recharging!");
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
		if(event.getPlayer() == this.player){
			Vector dir = event.getTo().subtract(event.getFrom()).multiply(100).toVector();
			if(this.player.isSneaking()&&this.player.getFoodLevel()>2&&dir.length()>0){
				BlockIterator bi = new BlockIterator(this.player.getWorld(), event.getFrom().toVector(), dir, 0, 2);
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
					this.player.setFoodLevel(this.player.getFoodLevel()-2);
					this.player.playSound(this.player.getLocation(), Sound.STEP_GRAVEL, 1, -5);
				}
			}
		}
	}
	

	
	public boolean undoWallWalk(savedBlock block){
		List<Location> badLocs = Arrays.asList(this.player.getLocation().getBlock().getLocation(),this.player.getEyeLocation().getBlock().getLocation());		
		if(!badLocs.contains(block.referencedBlock.getLocation())){
			block.rawRevert();
			return true;
		}
		return false;
	}
	
	public static String getDescription(){
		return "§4Earth elemental kit. Master the earth below you with two unique mana-based abilities:\n" +
				"§eSpell:\n§fRight click with your blaze rod wand to throw a mud bomb, ensnaring any enemy caught in it.\n" +
				"§eMoevement:\n§f" +
				"Crouch-walk into a wall and pass through it as if it were not there. Pitty the enemies who attempt to make chase.";
	}
}


	
