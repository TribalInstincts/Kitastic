package org.kitastic.kit.playerkit;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.kitastic.Kitastic;
import org.kitastic.kit.GenericKit;

public class kithopper extends GenericKit{
	public Vector v1;
	public Vector v2;
	public long t;
	public Random r;
	public kithopper(Player targetPlayer, Kitastic plugin) {
		super(targetPlayer, plugin);
		this.name = "Hopper";
		this.handlerLists = Arrays.asList(PlayerInteractEvent.getHandlerList(),PlayerMoveEvent.getHandlerList());
		v1 = new Vector(0,0,0);
		v2 = new Vector(0,0,0);
		r=new Random();
		ItemStack hopper = new ItemStack(154);
		this.player.getInventory().addItem(hopper);


	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event){
		 
		if(event.getPlayer() instanceof Player&&event.getPlayer()==this.player){
			if(this.player.getLocation().getY()-this.player.getLocation().getBlock().getRelative(BlockFace.DOWN).getY()>1&&player.getItemInHand().getTypeId()==154){
			Vector v1n = event.getTo().subtract(event.getFrom()).toVector();
			Vector newV = v1n.add(new Vector(0,.07,0));
			this.player.setVelocity(newV);
			this.player.playSound(this.player.getLocation(), Sound.BURP, 0.1f, -20);
			if(r.nextBoolean()){
				this.player.playSound(this.player.getLocation(), Sound.NOTE_BASS_DRUM, 0.05f, -5);
			}
			if(r.nextBoolean()){
				this.player.playSound(this.player.getLocation(), Sound.NOTE_SNARE_DRUM, 0.02f, -8);
			}
			
			}
		}
	}
	
	@EventHandler
	public void onDmg(EntityDamageEvent event){
		if(event.getEntity() instanceof Player&&event.getEntity()==this.player){
			event.setCancelled(true);
		}

	}

	@EventHandler
	public void onKick(PlayerKickEvent event){
		event.setCancelled(true);
		Bukkit.broadcastMessage(event.getReason());
	}
	
	public static String getDescription(){
		return "§4Item based kit:\n§fHold a hopper in your hands and jump to uncover the little-known secret of its original design. Spoilers: It's a jetpack.\nBe warned that there's " +
				"a reason it ended up being advertized as an over-priced item funnel... it's a sputtering low-thrust joke of a jetpack. §4No refunds.§f";
	}

}
