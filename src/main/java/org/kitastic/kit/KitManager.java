package org.kitastic.kit;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kitastic.Kitastic;
import org.kitastic.player.OnlinePlayer;


public class KitManager {
	public Kitastic plugin;
	public Map<String,String>kitList;
	public Map<String,Integer>kitTypeMax;
	private Map<String,ArrayList<String>> kitTypeIndex;
	
	public KitManager(Kitastic plugin){
		this.kitTypeIndex = new HashMap<String,ArrayList<String>>();
		this.plugin = plugin;
		this.kitList = new HashMap<String,String>();
		this.kitTypeMax = new HashMap<String,Integer>();
		this.getKitList();
	}
	
	public void toggleKit(String requestedKitName, Player player){
		OnlinePlayer op = this.plugin.playerList.get(player);
		String returnString;
		if(op.kitList.contains(requestedKitName)){
			returnString = this.removeKitByName(requestedKitName, op);
		}else{
			returnString = this.addKitByName(requestedKitName, op);
		}
		player.sendMessage(this.sayKits(op)+returnString);
	}
	
	private String removeKitByName(String requestedKitName, OnlinePlayer targetPlayer){
		try{
			targetPlayer.kits.get(requestedKitName).unRegister();
			targetPlayer.kits.remove(requestedKitName);
			targetPlayer.kitList.remove(requestedKitName);
			return "You have removed "+requestedKitName+".";
		}catch(Exception e){
			e.printStackTrace();
			return "There was an error removing this kit";		
			}
	}
	
	private String addKitByName(String requestedKitName, OnlinePlayer targetPlayer){
		if(!this.kitList.containsKey(requestedKitName)){
			return "This kit does not exist";
		}
		
		if(!targetPlayer.availableKits.contains(requestedKitName)){
			return "You don't own this kit! Purchase it by typing /buy kit "+requestedKitName+".";
		}
		int kitsOfType = 0;
		String kitType = this.kitList.get(requestedKitName);
		for(String kit :targetPlayer.kitList){
			if(this.kitList.get(kit).equals(kitType)){
				kitsOfType ++;
			}
		}
		if(kitsOfType >= this.kitTypeMax.get(this.kitList.get(requestedKitName))){
			return "You can have a max of "+this.kitTypeMax.get(this.kitList.get(requestedKitName))+" "+kitType+" kits. Please remove one with /kit "+requestedKitName+" before adding another.";
		}
		
		try{
			Class<?> acquiredClass = Class.forName("org.kitastic.kit.playerkit.kit"+requestedKitName);
			Constructor<?> constructor = acquiredClass.getConstructors()[0];
			GenericKit newKit = (GenericKit) constructor.newInstance(targetPlayer.player, this.plugin);
			targetPlayer.kitList.add(requestedKitName);
			targetPlayer.kits.put(requestedKitName, newKit);
			return "You have enabled "+requestedKitName+". Go rape something.";
		}catch(Exception e){
			e.printStackTrace();
			return "Oopse. There was an error creating this kit. Yell at Tribal.";
		}
	}
	
	public String sayKits(OnlinePlayer player){
		String kitMsg = "";
		for(String type : this.kitTypeIndex.keySet()){
			kitMsg += "§l§4"+WordUtils.capitalize(type)+" kits:§f§r\n   ";
			ArrayList<String> kits = new ArrayList<String>();
			int charCount = 0;
			for(String kit : this.kitTypeIndex.get(type)){
				String color;
				if(player.kitList.contains(kit)){
					color = "§e";
				}else if(player.availableKits.contains(kit)){
					color = "§f";
				}else{
					color = "§7";
				}
				kits.add(color+WordUtils.capitalize(kit)+"§f");
				charCount += kit.length();
				if(charCount > 40){
					charCount = charCount % 40;
					kitMsg += "   "+this.implodeArray((String[]) kits.toArray()," - ")+"\n";
					kits.clear();
				}
			}
			kitMsg +=this.implodeArray((String[]) kits.toArray(new String[kits.size()])," - ")+"\n";
		}
		return kitMsg;
		
	}
	
	public ArrayList<String> getPlayerAllowedKits(Player targetPlayer){
		String query = "SELECT products.name FROM products " +
				"LEFT OUTER JOIN playerinventory ON products.product_id = playerinventory.product_id " +
				"LEFT OUTER JOIN players ON players.id = playerinventory.player_id WHERE";
		if(!this.plugin.playerList.get(targetPlayer).inPowerHour){
				query += " (players.name = '"+targetPlayer.getName()+"' or products.cost = 0) AND";
		}
				query += " products.type = 'playerkit';";

		ResultSet rows = this.plugin.db.doQuery(query);
		ArrayList<String>kits = new ArrayList<String>();
		try {
			while(rows.next()){
				kits.add(rows.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return kits;
	}
	
	private void getKitList(){
		String query = "SELECT products.name, products.meta_category, category_metadata.value FROM products " +
				"LEFT JOIN category_metadata ON products.meta_category = category_metadata.identifier " +
				"WHERE products.type = 'playerkit' " +
				"AND category_metadata.name = 'max'";
		ResultSet rows = this.plugin.db.doQuery(query);
		try {
			while(rows.next()){
				String name = rows.getString(1);
				String type = rows.getString(2);
				this.kitList.put(name, type);
				this.kitTypeMax.put(type, rows.getInt(3));
				if(!this.kitTypeIndex.containsKey(type)){
					this.kitTypeIndex.put(type, new ArrayList<String>());
				}
				this.kitTypeIndex.get(type).add(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String implodeArray(String[] inputArray, String glueString) {

		/** Output variable */
		String output = "";

		if (inputArray.length > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(inputArray[0]);

			for (int i=1; i<inputArray.length; i++) {
				sb.append(glueString);
				sb.append(inputArray[i]);
			}

			output = sb.toString();
		}

		return output;
		}
}
