javaUtils()::=<<
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;

public class Utils {
	
	public static boolean isOperatorConfigAvailable(String operator){
		File operatorConfigFile = new File(operator+".json");
		
		if(operatorConfigFile.exists()){
		
			return true;
		}else{
			return false;
		}
	}
	
	public static OptionMap getOperatorConfig(String operator){
		
		Gson gson = new Gson();
		
		Map<String,String> optionMap = new HashMap<String,String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(operator+".json"));
			optionMap = gson.fromJson(br, Map.class);  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

		return new OptionMap(optionMap);	
	}
	

}


>>