package de.uniol.inf.is.odysseus.query.transformation.mainstructure.registry;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.query.transformation.mainstructure.ITransformationMainstructure;


public class QueryTransformationMainstructureRegistry {
	
	private static Logger LOG = LoggerFactory.getLogger(QueryTransformationMainstructureRegistry.class);
	
	static Map<String, ITransformationMainstructure> mainStructurMap = new HashMap<String, ITransformationMainstructure>();
	
	public static ITransformationMainstructure getOperatorTransformation(String programLanguage){
		if(mainStructurMap.containsKey(programLanguage.toLowerCase())){
			return mainStructurMap.get(programLanguage.toLowerCase());
		}else{
			LOG.debug("Mainstructure for "+programLanguage.toLowerCase() +" not found!");
			return null;
		}
	}
	
	public static void registerMainstructure(ITransformationMainstructure mainstructure){
		//Programmiersprache noch nicht vorhanden
		if(!mainStructurMap.containsKey(mainstructure.getProgramLanguage().toLowerCase())){
			mainStructurMap.put(mainstructure.getProgramLanguage().toLowerCase(),mainstructure);
			LOG.debug("Mainstructure for "+mainstructure.getProgramLanguage() +" added!");
		}else{
			LOG.debug("Mainstructure for "+mainstructure.getProgramLanguage()+" already added" );
		}
		
	}

	public static void unregisterMainstructure(ITransformationMainstructure mainstructure){
		if(mainStructurMap.containsKey(mainstructure.getProgramLanguage().toLowerCase())){
			mainStructurMap.remove(mainstructure);
			LOG.debug("Mainstructure for "+mainstructure.getProgramLanguage() +" removed!");
		}else{
			LOG.debug("Mainstructure for "+mainstructure.getProgramLanguage()+" not found in mainStructurMap" );
		}
	}
	
}
