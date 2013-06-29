package org.kitastic.kit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class scheduledTask implements Runnable {
    Method callback;
    Object callbackObject;
	public void run() {
	
			try {
				callback.invoke(this.callbackObject,(Object[])null);
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

		
	}
	
	public scheduledTask(Object callbackObject, Method callBack){
			this.callbackObject = callbackObject;
			this.callback = callBack;

	}
}
