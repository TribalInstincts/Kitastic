package org.kitastic.block;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class savedBlock {
	public Block referencedBlock;
	public int oldType;
	private Method revertCallback;
	private Object callbackObject;

	
	
	public savedBlock(Block referencedBlock, int oldType, Method revertCallback, Object callbackObject){
		this.referencedBlock = referencedBlock;
		this.oldType = oldType;
		this.revertCallback = revertCallback;
		this.callbackObject = callbackObject;
	}
	
	public savedBlock(Block referencedBlock, int oldType){
		this.referencedBlock = referencedBlock;
		this.oldType = oldType;
		this.revertCallback = null;
	}
	
	public boolean revert(){
		if(this.revertCallback == null){
			this.rawRevert();
			return true;
		}else{
			try {
				Boolean callbackResp = (Boolean) revertCallback.invoke(this.callbackObject, this);
				return callbackResp;
				
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
		return false;
	}
	public void rawRevert(){
		this.referencedBlock.setTypeId(this.oldType);
	}
}
