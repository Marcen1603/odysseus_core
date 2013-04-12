package de.uniol.inf.is.odysseus.debs2013.spatial;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class ShotOnGoalDetection extends AbstractFunction<Integer>{

	private static final long serialVersionUID = 8159082937727522598L;
		
	@Override
	public int getArity() {
		return 6;
	}
	
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
    	SDFDatatype.DOUBLE, 
    	SDFDatatype.LONG
	};

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return accTypes;
	}

	//1 = rechtes Tor, 0 = kein Torschuss, -1 = linkes Tor
	@Override
	public Integer getValue() {
		Double startX = ((Double) getInputValue(0));
		Double startY = ((Double) getInputValue(1));
		Double endX = ((Double) getInputValue(2));
		Double endY = ((Double) getInputValue(3));
		Long tsShot = (Long) getInputValue(4);
		Long ts = (Long) getInputValue(5);
		if((ts - tsShot) != 0) {
			double speedX = ((endX - startX) / (ts - tsShot)) * 1500000000000.0;
			double speedY = ((endY - startY) / (ts - tsShot)) * 1500000000000.0;
			if(speedX != 0) {
				double m = speedY / speedX;
				double b = startY - (m * startX);
				
				//rechtes Tor
				if(endX > startX) {
					double y = m * 33941 + b;
					if(y < 29898.5 && y > 22578.5) {
						endX = (endX + (speedX * 2));
						if(endX > 33941) {
							return 1;
						}
					} 
				} else { // linkes Tor
					double y = m * -33968 + b;
					if(y < 29880 && y > 22560) {
						endX = (endX + (speedX * 2));
						if(endX < -33968) {
							return -1;
						}
					} 
				}
			}
		} 
		return 0;
	 }

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.INTEGER;
	}

	@Override
	public String getSymbol() {
		return "IsShotOnGoal";
	}
}
