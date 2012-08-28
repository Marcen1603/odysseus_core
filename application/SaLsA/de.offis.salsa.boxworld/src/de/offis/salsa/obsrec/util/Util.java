package de.offis.salsa.obsrec.util;

import java.awt.Polygon;
import java.util.Map.Entry;

import de.offis.salsa.obsrec.TrackedObject;

public class Util {
	public static String getMaxAffinityType(TrackedObject to){
		Double affnity = -1.0;
		String type = null;
		
		for (Entry<String, Double> t : to.getTypeAffinitys().entrySet()) {
			if (type == null || t.getValue() > affnity) {
				type = t.getKey();
				affnity = t.getValue();
			}
		}
		
		return type;
	}
	
	public static Polygon getMaxAffinityPolygon(TrackedObject to){
		return to.getPolygons().get(Util.getMaxAffinityType(to));
	}
}
