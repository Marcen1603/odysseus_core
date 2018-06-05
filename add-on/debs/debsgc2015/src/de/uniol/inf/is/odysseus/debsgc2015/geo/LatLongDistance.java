package de.uniol.inf.is.odysseus.debsgc2015.geo;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class LatLongDistance extends AbstractFunction<Double> {

	private static final long serialVersionUID = -6304296903282263891L;
	static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE } };

	public LatLongDistance() {
		super("latLongDistance", 4, acceptedTypes, SDFDatatype.FLOAT);
	}

	@Override
	public Double getValue() {
		double lat1 = getNumericalInputValue(0);
		double lng1 = getNumericalInputValue(1);
		double lat2 = getNumericalInputValue(2);
		double lng2 = getNumericalInputValue(3);
		return distFrom(lat1, lng1, lat2, lng2);
	}
	
	public static final double distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371; // kilometers
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		return dist;
	}

}
