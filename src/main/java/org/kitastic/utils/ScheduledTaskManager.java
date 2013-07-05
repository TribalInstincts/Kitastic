package org.kitastic.utils;

import java.lang.reflect.Method;
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
	
	public SavedTask createRepeatingTask(Object sender, String methodName, Object[] perameters,Integer delay, Integer interval){
		ManagedCallbackRunner callbackRunner = this.createCallback(sender, methodName, true, perameters);
		Integer taskId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, callbackRunner, delay, interval);
		callbackRunner.taskId = taskId;
		SavedTask task =  new SavedTask(this, sender, callbackRunner, taskId, true);
		this.taskIds.put(taskId, task);
		return task;
	}
	
	public SavedTask createDelayedTask(Object sender, String methodName, Object[] perameters,Integer delay){
		ManagedCallbackRunner callbackRunner = this.createCallback(sender, methodName, false, perameters);
		Integer taskId = this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, callbackRunner, delay);
		callbackRunner.taskId = taskId;
		SavedTask task =  new SavedTask(this, sender, callbackRunner, taskId, false);
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
	
	private ManagedCallbackRunner createCallback(Object sender, String methodName,Boolean isRepeating,Object[] perameters){
		Method callbackMethod;
		try {
			callbackMethod = sender.getClass().getMethod(methodName, (Class<?>[]) perameters);
			return new ManagedCallbackRunner(this, isRepeating, sender, callbackMethod, perameters);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
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
