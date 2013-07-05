package org.kitastic.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.v1_5_R3.EnumArmorMaterial;
import net.minecraft.server.v1_5_R3.ItemArmor;
import net.minecraft.server.v1_5_R3.Material;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitastic.Kitastic;
import org.kitastic.kit.GenericKit;

public class OnlinePlayer {
	public Player player;
	public String name;
	public ArrayList<String> availableKits;
	public Map<String, GenericKit> kits; 
	public ArrayList<String> kitList;
	public Boolean inPowerHour;
	Kitastic Plugin;
	
	public OnlinePlayer(Player joined, Kitastic Plugin){
		this.player = joined;
		this.Plugin = Plugin;
		this.name = this.player.getName();
		this.kits = new HashMap<String,GenericKit>();
		this.kitList = new ArrayList<String>();
		this.inPowerHour = false;
		this.availableKits = new ArrayList<String>();
	}
	
	public ArrayList<String> getAvailableKits(){
		this.availableKits = this.Plugin.km.getPlayerAllowedKits(player);
		return this.availableKits;
	}
}
