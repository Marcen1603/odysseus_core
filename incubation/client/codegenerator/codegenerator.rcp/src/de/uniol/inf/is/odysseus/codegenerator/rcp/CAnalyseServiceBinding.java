package de.uniol.inf.is.odysseus.codegenerator.rcp;


import de.uniol.inf.is.odysseus.codegenerator.ICAnalyse;

public class CAnalyseServiceBinding {
	
	private static ICAnalyse analyseComponent;
	
	public static void bindAnalyseComponent(ICAnalyse serv) {
		analyseComponent = serv;
	
	}
	
	public static void unbindAnalyseComponent(ICAnalyse serv) {
		analyseComponent = null;
	}
	
	
	public static ICAnalyse getAnalyseComponent(){
		return analyseComponent;
	}

}
