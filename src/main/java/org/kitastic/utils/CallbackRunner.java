package org.kitastic.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class CallbackRunner implements Runnable {
    Method callback;
    Object callbackObject;
    Object[] perameters;
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

		
	}
	
	public CallbackRunner(Object callbackObject, Method callBack){
			this.callbackObject = callbackObject;
			this.callback = callBack;
			this.perameters = (Object[]) null;
	}
	
	public CallbackRunner(Object callbackObject, Method callback, Object[] perameters){
		this.callbackObject = callbackObject;
		this.callback = callback;
		this.perameters = perameters;
	}
}
