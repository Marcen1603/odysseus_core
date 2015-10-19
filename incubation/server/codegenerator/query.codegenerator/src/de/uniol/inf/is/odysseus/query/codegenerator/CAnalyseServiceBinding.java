package de.uniol.inf.is.odysseus.query.codegenerator;


import de.uniol.inf.is.odysseus.query.codegenerator.ICAnalyse;

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
