package de.offis.salsa.obsrec.objrules;

import java.awt.Polygon;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.TrackedObject.Type;
import de.offis.salsa.obsrec.TypeDetails;

public abstract class AbstractObjRule implements IObjectRule {

	private static HashMap<Type, AbstractObjRule> objRules = new HashMap<>();	
	
	protected void registerObjRule(AbstractObjRule objRule){
		AbstractObjRule.objRules.put(objRule.getType(), objRule);
	}
	
	public static Polygon getPredictedPolygon(List<Sample> segment, Type type){		
		return objRules.get(type).getPredictedPolygon(segment);
	}
	
	
	public static TypeDetails getTypeDetails(List<Sample> samplesObject){
		TypeDetails details = new TypeDetails();
		
		for(Entry<Type, AbstractObjRule> objRule : objRules.entrySet()){
			details.addTypeAffinity(objRule.getValue().getType(), objRule.getValue().getTypeAffinity(samplesObject));
		}
		
		return details;
	}
}
