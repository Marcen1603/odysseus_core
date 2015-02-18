package de.uniol.inf.is.odysseus.trajectory.compare.textual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.javatuples.Triplet;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IHasTextualAttributes;


public class VectorTextualDistance implements ITextualDistance {
	
	private final static VectorTextualDistance INSTANCE = new VectorTextualDistance();
	
	private final static Map<String, Triplet<Map<String, Double>, Double ,Double>> ATTRIBUTES_DEFINITIONS = new HashMap<>();
	
	static {
		Triplet<Map<String, Double>, Double, Double> attrDefinition = 
				new Triplet<Map<String, Double>, Double, Double>(new HashMap<String, Double>(), 0d, 100d);
		attrDefinition.getValue0().put("independent", 0d);
		attrDefinition.getValue0().put("grouped", 100d);
		ATTRIBUTES_DEFINITIONS.put("travel_style", attrDefinition);
		
		attrDefinition = 
				new Triplet<Map<String, Double>, Double, Double>(null, 0d, null);
		ATTRIBUTES_DEFINITIONS.put("persons", attrDefinition);
		
		attrDefinition = 
				new Triplet<Map<String, Double>, Double, Double>(new HashMap<String, Double>(), 0d, 100d);
		attrDefinition.getValue0().put("bus", 0d);
		attrDefinition.getValue0().put("taxi", 50d);
		attrDefinition.getValue0().put("car", 100d);
		ATTRIBUTES_DEFINITIONS.put("transport", attrDefinition);
		
		attrDefinition = 
				new Triplet<Map<String, Double>, Double, Double>(null, 0d, 100d);
		ATTRIBUTES_DEFINITIONS.put("tollway", attrDefinition);
		
		attrDefinition = 
				new Triplet<Map<String, Double>, Double, Double>(null, 0d, 100d);
		ATTRIBUTES_DEFINITIONS.put("highway", attrDefinition);
	}
	
	public static VectorTextualDistance getInstance() {
		return INSTANCE;
	}
	
	private static double getValue(final String attr, final String value) {
		final Triplet<Map<String, Double>, Double ,Double> attrDefinition = 
				ATTRIBUTES_DEFINITIONS.get(attr);
		
		if(attrDefinition == null) {
			throw new IllegalArgumentException("Textual attribue \"" + attr + "\" is unknown.");
		}
		if(attrDefinition.getValue0() != null) {
			final Double nVal = attrDefinition.getValue0().get(value);
			if(nVal == null) {
				throw new IllegalArgumentException("Value " + value + " for textual attribue \"" + attr + "\" is not defined.");
			}
			return nVal;
		}
		final Double nVal = Double.parseDouble(value);
		
		if(attrDefinition.getValue1() != null && nVal < attrDefinition.getValue1()) {
			
		}
		if(attrDefinition.getValue2() != null && nVal > attrDefinition.getValue2()) {
			
		}
		return nVal;
	}
	
	private VectorTextualDistance() {}
	
	@Override
	public double getDistance(IHasTextualAttributes o1, IHasTextualAttributes o2) {
		// find simular attributes
		final List<Double> list1 = new ArrayList<>();
		final List<Double> list2 = new ArrayList<>();
		
		for(final String attr : o1.getTextualAttributes().keySet()) {
			final String valO2 = o2.getTextualAttributes().get(attr);
			if(valO2 != null) {
				final String valO1 = o1.getTextualAttributes().get(attr);
				
				list1.add(getValue(attr, valO1));
				list2.add(getValue(attr, valO2));
			}
		}
		
		final Iterator<Double> listIt1 = list1.iterator();
		final Iterator<Double> listIt2 = list2.iterator();
		
		double distance = 0;
		while(listIt1.hasNext()) {
			Math.pow(listIt1.next() - listIt2.next(), 2);
		}
		distance = Math.sqrt(distance);
		
		return distance;
	}
}
