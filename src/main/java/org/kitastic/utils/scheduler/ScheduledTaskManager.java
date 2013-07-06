package org.kitastic.utils.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitScheduler;
import org.kitastic.Kitastic;

public class ScheduledTaskManager {
	
	
	private Kitastic plugin;
	public BukkitScheduler pluginScheduler;
	private Map<Integer,SavedTask> taskIds;

	public ScheduledTaskManager(Kitastic plugin){
		this.plugin = plugin;
		this.pluginScheduler = plugin.getServer().getScheduler();
		this.taskIds = new  HashMap<Integer,SavedTask>();
	}
	
	public SavedTask createRepeatingTask(Callback callback,Integer delay, Integer interval){
		ManagedCallbackRunner callbackRunner = new ManagedCallbackRunner(this, false, callback);
		Integer taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, callbackRunner, delay, interval);
		callbackRunner.taskId = taskId;
		SavedTask task =  new SavedTask(this, callback.getOnwer(), callbackRunner, taskId, true);
		this.taskIds.put(taskId, task);
		return task;
	}
	
	public SavedTask createDelayedTask(Callback callback,Integer delay){
		ManagedCallbackRunner callbackRunner = new ManagedCallbackRunner(this, false, callback);
		Integer taskId = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, callbackRunner, delay);
		callbackRunner.taskId = taskId;
		SavedTask task =  new SavedTask(this,callback.getOnwer(), callbackRunner, taskId, false);
		this.taskIds.put(taskId, task);
		return task;
	}

	public void removeTask(Integer taskId) {
		if(this.taskIds.containsKey(taskId)){
			if(this.pluginScheduler.isQueued(taskId)){
				this.pluginScheduler.cancelTask(taskId);
			}
			this.taskIds.remove(taskId);
		}
	}
	
	public void removeTask(SavedTask taskId) {
		taskId.destroy();
	}
	
	public void removeAll(Object taskCreator){
		ArrayList<Integer> taskList = new ArrayList<Integer>();
		for(Integer i : this.taskIds.keySet()){
			if(this.taskIds.get(i).owner.equals(taskCreator)){
				taskList.add(i);
			}
		}
		for(Integer t : taskList){
			this.removeTask(t);
		}
	}

}
