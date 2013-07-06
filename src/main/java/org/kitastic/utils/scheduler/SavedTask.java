package org.kitastic.utils.scheduler;

public class SavedTask {
	public Integer taskId;
	public boolean isRepeating;
	public ScheduledTaskManager mother;
	public CallbackRunner callback;	
	public Object owner;
	
	public SavedTask(ScheduledTaskManager taskManager, Object sender, CallbackRunner callback, Integer taskId, Boolean isRepeating){
		this.owner = sender;
		this.callback = callback;
		this.taskId = taskId;
		this.isRepeating = isRepeating;
		this.mother = taskManager;
		
	}
	
	
	public void destroy(){
		this.mother.removeTask(this.taskId);
	}
	
	public void forceInvoke(){
		try {
			this.callback.run();
			}catch(Exception e){
			e.printStackTrace();
		}
		if(!this.isRepeating){
			this.destroy();
		}
	}
}
