package de.offis.gui.client;

import com.google.gwt.storage.client.Storage;

/**
 * HTML5 storage to save positions of modules and reload them when operator group is loaded.
 *
 * @author Alexander Funk
 * 
 */
public class OpsStorage {
	private static OpsStorage instance = new OpsStorage();
	private Storage storage = Storage.getLocalStorageIfSupported();
	
	private static final String SEPARATOR = ".";
	
	private static final String SCREEN_X = "ScaiOpsScreenX";
	private static final String SCREEN_Y = "ScaiOpsScreenY";
	private static final String SCREEN_WIDTH = "ScaiOpsScreenWidth";
	private static final String SCREEN_HEIGHT = "ScaiOpsScreenHeight";
	
	public static OpsStorage get(){
		if(instance == null){
			instance = new OpsStorage();			
		}
		
		return instance;
	}

	public void setModulePosition(String operatorGroup, String moduleId, double x, double y){
		if(storage == null){
			return;
		}
		
		storage.setItem(operatorGroup + SEPARATOR + moduleId + ".X", x+"");
		storage.setItem(operatorGroup + SEPARATOR + moduleId + ".Y", y+"");
	}
	
	public double getModuleX(String operatorGroup, String moduleId){
		if(storage == null){
			return 0;
		}
		
		String item = storage.getItem(operatorGroup + SEPARATOR + moduleId + ".X");
		
		return Double.parseDouble(item == null? "0" : item);
	}
	
	public double getModuleY(String operatorGroup, String moduleId){
		if(storage == null){
			return 0;
		}
		
		String item = storage.getItem(operatorGroup + SEPARATOR + moduleId + ".Y");
		
		return Double.parseDouble(item == null? "0" : item);
	}
	
	public void setScreenPositionAndSize(String operatorGroup, double x, double y, double width, double height){
		if(storage == null){
			return;
		}
		
		storage.setItem(operatorGroup + SEPARATOR + SCREEN_X, x+"");
		storage.setItem(operatorGroup + SEPARATOR + SCREEN_Y, y+"");
		storage.setItem(operatorGroup + SEPARATOR + SCREEN_WIDTH, width+"");
		storage.setItem(operatorGroup + SEPARATOR + SCREEN_HEIGHT, height+"");
	}
	
	public double getScreenX(String operatorGroup){
		if(storage == null){
			return 0;
		}
		
		String item = storage.getItem(operatorGroup + SEPARATOR + SCREEN_X);
		
		return Double.parseDouble(item == null? "0" : item);
	}
	
	public double getScreenY(String operatorGroup){
		if(storage == null){
			return 0;
		}
		
		String item = storage.getItem(operatorGroup + SEPARATOR + SCREEN_Y);
		
		return Double.parseDouble(item == null? "0" : item);
	}
	
	public double getScreenWidth(String operatorGroup){
		if(storage == null){
			return 0;
		}
		
		String item = storage.getItem(operatorGroup + SEPARATOR + SCREEN_WIDTH);
		
		return Double.parseDouble(item == null? "0" : item);
	}
	
	public double getScreenHeight(String operatorGroup){
		if(storage == null){
			return 0;
		}
		
		String item = storage.getItem(operatorGroup + SEPARATOR + SCREEN_HEIGHT);
		
		return Double.parseDouble(item == null? "0" : item);
	}
	
	public void clear(){
		storage.clear();
	}
	
	public boolean hasDataForOperatorGroup(String operatorGroup){
		for(int i = 0; i < storage.getLength() ; i++){
			if(storage.key(i).startsWith(operatorGroup)){
				return true;
			}
		}
		
		return false;
	}
}
