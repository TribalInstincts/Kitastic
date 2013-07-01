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

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.kitastic.Kitastic;
import org.kitastic.kit.kit;

public class onlinePlayer {
	public Player thisPlayer;
	public String name;
	public Map<String, kit> kits; 
	public Map<String, kit> elementalKit;
	public List<String> elementalList;
	public List<String> kitList;
	Kitastic Plugin;
	
	public onlinePlayer(Player joined, Kitastic Plugin){
		this.elementalList = Arrays.asList("earth","air","water");
		this.kitList = Arrays.asList("spider","hopper");
		this.thisPlayer = joined;
		this.Plugin = Plugin;
		this.name = this.thisPlayer.getName();
		this.kits = new HashMap<String,kit>();
		this.elementalKit = new HashMap<String,kit>();

	}
	
	public Boolean addKit(String kitName){
		kitName = kitName.toLowerCase();

		if(this.elementalKit.containsKey(kitName)){
			this.removeKit(kitName);
			this.sayKits();
			return true;
		}
		if(this.kits.containsKey(kitName)){
			this.removeKit(kitName);
			this.sayKits();
			return true;
		}
		
		
		if(this.kitList.contains(kitName)||this.elementalList.contains(kitName)){
			kit newKit = new kit(kitName, this.thisPlayer, this.Plugin );
			if(this.elementalList.contains(kitName)){
				if(this.elementalKit.size()>0){
					
					this.removeKit(this.elementalKit.keySet().toArray()[0].toString());
				}
					this.elementalKit.put(kitName, newKit);
			}else{
				this.kits.put(kitName, newKit);
			}
		}else{			
			this.thisPlayer.sendMessage("Don't mess with me! Kit does not exist!");
			return false;
		}

		this.sayKits();
		return true;
	}
	
	public Boolean removeKit(String kitName){
		if(this.elementalList.contains(kitName)){
			this.elementalKit.get(kitName).unRegister();
			this.thisPlayer.sendMessage(kitName+" removed.");
			this.elementalKit.remove(kitName);
			return true;
		}
		this.kits.get(kitName).unRegister();
		this.kits.remove(kitName);
		return true;
	}
	
	public void sayKits(){
		ArrayList<String> activeKits = new ArrayList<String>();
		for(Iterator<String> e = this.elementalKit.keySet().iterator(); e.hasNext(); ){
			activeKits.add(e.next());
		}
		
		for(Iterator<String> k = this.kits.keySet().iterator(); k.hasNext(); ){
			activeKits.add(k.next());
		}
		this.thisPlayer.sendMessage("Elemental Kits:");
		String color = "#3F3F3F ";
		String strKits = "§4Elemental Kits:\n";
		for(String kit : this.elementalList){
			if(activeKits.contains(kit)){
				color = "§6";
			}else{
				color = "§f";
			}
			strKits += (" "+color+kit+" ");
		}
		strKits += "\n§4Regular Kits:\n";
		for(String kit : this.kitList){
			if(activeKits.contains(kit)){
				color = "§e";
			}else{
				color = "§f ";
			}
			strKits += (" "+color+kit+" ");
		}
		this.thisPlayer.sendMessage(strKits);
	}
	
	public void getDbInfo(){
		
	}
}
