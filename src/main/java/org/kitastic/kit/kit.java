package org.kitastic.kit;

import java.lang.reflect.Constructor;
import org.kitastic.kit.playerkit.*;
import org.bukkit.entity.Player;
import org.kitastic.Kitastic;

public class kit {
	public String kitName;
	public genericKit baseKit;
	public Boolean exists;
	Kitastic Plugin;
	
	public kit(String requestedKitName, Player targetPlayer, Kitastic Plugin){
		this.kitName = requestedKitName;
		this.Plugin = Plugin;
		try{
			Class<?> acquiredClass = Class.forName("org.kitastic.kit.playerkit.kit"+requestedKitName);
			Constructor<?> constructor = acquiredClass.getConstructors()[0];
			this.baseKit = (genericKit) constructor.newInstance(targetPlayer, this.Plugin);
			this.exists = true;
			
		}catch(Exception e){
			this.exists = false;
			targetPlayer.sendMessage("Error, bitch.");
			
		}

	}
	public void unRegister(){
		baseKit.unRegister();
		baseKit = null;
	}
	

}
