package org.kitastic.utils.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;


public class CallbackRunner implements Runnable {
	public Callback callback;
	
	public CallbackRunner(Callback callback){
		this.callback = callback;
	}
	
	public void run() {
		callback.invoke();
	}
	

}
