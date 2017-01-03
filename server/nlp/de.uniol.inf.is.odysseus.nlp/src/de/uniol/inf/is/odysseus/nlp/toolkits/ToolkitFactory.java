package de.uniol.inf.is.odysseus.nlp.toolkits;

import java.util.List;

public class ToolkitFactory {

	public static NLPToolkit get(String toolkit, List<String> information) {
		// TODO Auto-generated method stub
//		if(toolkit.equals("opennlp")){
//			return new OpenNLPToolkit(information);
//		}
		return new OpenNLPToolkit(information);
	}

}
