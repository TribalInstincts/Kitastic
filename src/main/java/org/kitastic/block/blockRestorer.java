package org.kitastic.block;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.kitastic.Kitastic;
import org.kitastic.utils.Callback;
import org.kitastic.utils.CallbackRunner;

public class blockRestorer {
	public Map<Long,ArrayList<savedBlock>> timeStamps;
	public ArrayList<Location> blockIndex;
	public World targetWorld;
	public Kitastic Plugin;
	
	public blockRestorer(World targetWorld, Kitastic Plugin){
		this.timeStamps = new HashMap<Long,ArrayList<savedBlock>>();
		this.blockIndex = new ArrayList<Location>();
		this.targetWorld = targetWorld;
		this.Plugin = Plugin;
			Callback callback = new Callback(this, "restore",null);
			this.Plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.Plugin, new CallbackRunner(callback), 30, 30);
	}
	
	public boolean queueBlock(long timeDelay, savedBlock blockReference){
		long timeToRestore = targetWorld.getFullTime() + timeDelay;
		if(this.blockIndex.contains(blockReference.referencedBlock.getLocation())){
			return false;
		}
		if(!this.timeStamps.containsKey(timeToRestore)){
			this.timeStamps.put(timeToRestore, new ArrayList<savedBlock>());
		}
		this.timeStamps.get(timeToRestore).add(blockReference);
		this.blockIndex.add(blockReference.referencedBlock.getLocation());
		return true;
	}
	
	public void restore(){
		long currentTime = this.targetWorld.getFullTime();
		ArrayList<Long> fixedTimes = new ArrayList<Long>();
		for(Iterator<Long> time = timeStamps.keySet().iterator();time.hasNext();){
			long indexTime = time.next();
			if(currentTime < indexTime){
				continue;
			}
			ArrayList<savedBlock>fixedBlocks = new ArrayList<savedBlock>();
			ArrayList<savedBlock> blocks = this.timeStamps.get(indexTime);
			for(savedBlock block : blocks ){
				if(block.revert()){
					fixedBlocks.add(block);
					this.blockIndex.remove(block.referencedBlock.getLocation());

				}
			}
			for(savedBlock fixedBlock : fixedBlocks){
				blocks.remove(fixedBlock);
			}
			if(blocks.size()<1){
				fixedTimes.add(indexTime);
			}
			
		}
		for(long fixedTime : fixedTimes){
			this.timeStamps.remove(fixedTime);
		}
	}
}
