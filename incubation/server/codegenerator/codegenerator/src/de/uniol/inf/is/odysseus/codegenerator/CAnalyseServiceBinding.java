package de.uniol.inf.is.odysseus.codegenerator;

import de.uniol.inf.is.odysseus.codegenerator.ICAnalyse;

/**
 * OSGi-Binding for the analyse component
 * 
 * @author MarcPreuschaft
 *
 */
public class CAnalyseServiceBinding {
	
	private static ICAnalyse analyseComponent;
	
	/**
	 * bind a new analyse component (OSGI method)
	 * 
	 * @param serv
	 */
	public static void bindAnalyseComponent(ICAnalyse serv) {
		analyseComponent = serv;
	}
	
	
	/**
	 * unbind the analyse component (OSGI method)
	 * 
	 * @param serv
	 */
	public static void unbindAnalyseComponent(ICAnalyse serv) {
		analyseComponent = null;
	}
	
	/**
	 * return the registed analyse component
	 * 
	 * @return
	 */
	public static ICAnalyse getAnalyseComponent(){
		return analyseComponent;
	}

}
