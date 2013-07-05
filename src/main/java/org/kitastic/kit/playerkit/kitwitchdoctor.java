package org.kitastic.kit.playerkit;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.kitastic.Kitastic;
import org.kitastic.kit.GenericKit;
import org.kitastic.utils.SavedTask;

public class kitwitchdoctor extends GenericKit{
	public Block totem;
	public SavedTask firer;
	public kitwitchdoctor(Player targetPlayer, Kitastic plugin) {
		super(targetPlayer, plugin);
    	ItemStack skull = new ItemStack(144);
    	this.player.getInventory().addItem(skull);
		
	}
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
    	ItemStack skull = new ItemStack(144);
    	this.player.getInventory().addItem(skull);
    }
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event){

			if(event.getPlayer() == this.player&&event.getAction().toString().startsWith("RIGHT")){
				
					Fireball fb = (Fireball) this.player.getWorld().spawnEntity(this.player.getLocation(), EntityType.FIREBALL);
					fb.setDirection(this.player.getLocation().getDirection());
					//this.totem = this.player.getTargetBlock(null, 4);
					//this.totem.setTypeId(144);
					//this.firer = this.Plugin.tm.createRepeatingTask(this, "fire", null, 60, 60);
					
				}
			
	}
	
	public void fire(){
		
		this.totem.getLocation();
	}
	public static String getDescription(){
		return "§4Earth elemental kit. Master the earth below you with two unique mana-based abilities:\n" +
				"§eSpell:\n§fRight click with your blaze rod wand to throw a mud bomb, ensnaring any enemy caught in it.\n" +
				"§eMoevement:\n§f" +
				"Crouch-walk into a wall and pass through it as if it were not there. Pitty the enemies who attempt to make chase.";
	}
}
