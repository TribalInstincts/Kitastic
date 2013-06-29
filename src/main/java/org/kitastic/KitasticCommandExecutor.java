package org.kitastic;



import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KitasticCommandExecutor implements CommandExecutor{
	Kitastic plugin;
	
	public KitasticCommandExecutor(Kitastic plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("kit")){ 
			this.plugin.playerList.get(sender).addKit(args[0]);
			return true;
    	} else {
    	sender.sendMessage("Bad command. Try again plox.");
    	return true;
    	}
    }

}
