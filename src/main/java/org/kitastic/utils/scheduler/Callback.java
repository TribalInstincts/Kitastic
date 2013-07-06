package org.kitastic.utils.scheduler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

public class Callback {
	private String methodName;
	private Object targetObject;
	public Method targetMethod;
	private Object[] perameters;
	public Callback(Object targetObject, String methodName, Object[] argsOrNull){
		this.targetMethod = this.getMethod(targetObject, methodName, argsOrNull);
		this.targetObject = targetObject;
		this.perameters = argsOrNull;
	}
	
	public void invoke(){
		try {
			this.targetMethod.invoke(this.targetObject,perameters);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			Bukkit.broadcastMessage("No such method: "+methodName);
			e.printStackTrace();
		}
	}
	
	public Method getMethod(Object targetObject, String methodName, Object[] perams){
		Method targetMethod = null;
		try {
			targetMethod = targetObject.getClass().getMethod(methodName, (Class<?>[]) perams);
		} catch (Exception e){
			Bukkit.broadcastMessage("No such method: "+methodName);
		}
		return targetMethod;
	}
	
	
	public Object getOnwer(){
		return this.targetObject;
	}
}
