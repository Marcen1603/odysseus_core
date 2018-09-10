package de.uniol.inf.is.odysseus.textprocessing.types;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TPRegistry {
	
	static Logger logger = LoggerFactory.getLogger(TPRegistry.class);

	static Map<String, ITextProcessing> processingTypes = new HashMap<String, ITextProcessing>();
	
	public static ITextProcessing getTextProcessingTypeByName(String processType)  
	{	
		if(processingTypes.containsKey(processType))
		{
			ITextProcessing textProcess = processingTypes.get(processType);
			
			if(textProcess.getType().equals(processType))		
			   return textProcess;
			else
				return null;
		}
		else
		{
			ITextProcessing textProcess = processingTypes.get(processType.toLowerCase());
			ITextProcessing newTextProcessType = textProcess.getInstance(processType.toLowerCase());
			processingTypes.put(processType.toLowerCase(), newTextProcessType);

			return newTextProcessType;
		}
	}
	
	
	public static void registerTextProcessingType(ITextProcessing textProcess) {
		if (!processingTypes.containsKey(textProcess.getType().toLowerCase())) {
			processingTypes.put(textProcess.getType().toLowerCase(), textProcess);
		} else {
			logger.debug("TextProceesingType: " + textProcess.getType().toLowerCase() + " already added");
		}
	}

	public static void unregisterTextProcessingType(ITextProcessing textProcess) {
		if (processingTypes.containsKey(textProcess.getType().toLowerCase())) {
			processingTypes.remove(textProcess.getType().toLowerCase());
		}
	}
}
