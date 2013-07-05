package org.kitastic.kit.serverkit;

import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class hungerRegen implements Runnable {
	public List<World> worlds;
	public void run() {
		for(World w: this.worlds){
			for(Player p: w.getPlayers()){
				if(p.getFoodLevel()<20){
					p.setFoodLevel(p.getFoodLevel() +1);
				}
			}
		}
		
	}
	
	public hungerRegen(List<World> worlds){
		this.worlds = worlds;
	}

}
