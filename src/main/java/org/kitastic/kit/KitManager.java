package org.kitastic.kit;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.kitastic.Kitastic;
import org.kitastic.player.onlinePlayer;


public class KitManager {
	public Kitastic plugin;
	public Map<String,String>kitList;
	public Map<String,Integer>kitTypeMax;
	public KitManager(Kitastic plugin){
		this.plugin = plugin;
		this.kitList = new HashMap<String,String>();
		this.kitTypeMax = new HashMap<String,Integer>();
		this.getKitList();
	}

	private boolean addKitByName(String requestedKitName, Player player){
		onlinePlayer targetPlayer = this.plugin.playerList.get(player);
		if(!this.kitList.containsKey(requestedKitName)){
			return false;
		}
		
		if(!targetPlayer.availableKits.contains(requestedKitName)){
			return false;
		}
		int kitsOfType = 0;
		String kitType = this.kitList.get(requestedKitName);
		for(String kit :targetPlayer.kitList){
			if(this.kitList.get(kit)==kitType){
				kitsOfType ++;
			}
		}
		if(kitsOfType >= this.kitTypeMax.get(requestedKitName)){
			return false;
		}
		
		try{
			Class<?> acquiredClass = Class.forName("org.kitastic.kit.playerkit.kit"+requestedKitName);
			Constructor<?> constructor = acquiredClass.getConstructors()[0];
			genericKit newKit = (genericKit) constructor.newInstance(targetPlayer.thisPlayer, this.plugin);
			targetPlayer.kitList.add(requestedKitName);
			targetPlayer.kits.put(requestedKitName, newKit);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<String> getPlayerAllowedKits(Player targetPlayer){
		String query = "SELECT products.name FROM products " +
				"LEFT OUTER JOIN playerinventory ON products.product_id = playerinventory.product_id " +
				"LEFT OUTER JOIN players ON players.id = playerinventory.product_id " +
				"WHERE (players.name = '"+targetPlayer.getName()+"' or products.cost = 0) " +
				"AND products.type = 'playerkit';";

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
		String query = "SELECT products.name, products.meta_category, metadata.value FROM products " +
				"LEFT JOIN metadata ON products.meta_category = metadata.category " +
				"WHERE products.type = 'playerkit' " +
				"AND metadata.name = 'max'";
		ResultSet rows = this.plugin.db.doQuery(query);
		try {
			while(rows.next()){
				this.kitList.put(rows.getString(1), rows.getString(2));
				this.kitTypeMax.put(rows.getString(2), rows.getInt(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
