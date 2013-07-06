package org.kitastic.utils.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ManagedCallbackRunner extends CallbackRunner{
	public ScheduledTaskManager mother;
	public Integer taskId;
	public Boolean isRepeating;
	
	public ManagedCallbackRunner(ScheduledTaskManager mother, Boolean isRepeating, Callback callback) {
		super(callback);
		this.mother = mother;
		this.taskId = null;
		this.isRepeating = isRepeating;
		this.taskId = -1;
	}
	
	@Override
	public void run() {
		this.callback.invoke();

		if(!this.isRepeating){
			this.mother.removeTask(this.taskId);
		}
}

}
