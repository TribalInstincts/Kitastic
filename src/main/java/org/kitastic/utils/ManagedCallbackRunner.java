package org.kitastic.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ManagedCallbackRunner extends CallbackRunner{
	public ScheduledTaskManager mother;
	public Integer taskId;
	public Boolean isRepeating;
	
	public ManagedCallbackRunner(ScheduledTaskManager mother, Boolean isRepeating, Object callbackObject, Method callBack, Object[] perameters) {
		super(callbackObject, callBack, perameters);
		this.mother = mother;
		this.taskId = taskId;
		this.isRepeating = isRepeating;
		this.taskId = -1;
	}
	
	@Override
	public void run() {
		
		try {
			callback.invoke(this.callbackObject,perameters);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!this.isRepeating){
			this.mother.removeTask(this.taskId);
		}
}

}
