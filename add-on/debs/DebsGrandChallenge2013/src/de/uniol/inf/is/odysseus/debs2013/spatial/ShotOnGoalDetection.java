package de.uniol.inf.is.odysseus.debs2013.spatial;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ShotOnGoalDetection extends AbstractFunction<Integer>{

	private static final long serialVersionUID = 8159082937727522598L;
	
	public ShotOnGoalDetection() {
		this("IsShotOnGoal");
	}

	public ShotOnGoalDetection(String symbol) {
		super(symbol,6,accTypes,SDFDatatype.INTEGER);
	}


	public static final SDFDatatype[] accTypes1 = new SDFDatatype[] {
    	SDFDatatype.LONG,
    	SDFDatatype.INTEGER
	};
	
	 public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{accTypes1,accTypes1,accTypes1,accTypes1,accTypes1,accTypes1};


	//1 = rechtes Tor, 0 = kein Torschuss, -1 = linkes Tor
	@Override
	public Integer getValue() {
		Integer startX = ((Integer) getInputValue(1));
		Integer startY = ((Integer) getInputValue(0));
		Integer endX = ((Integer) getInputValue(3));
		Integer endY = ((Integer) getInputValue(2));
		Long tsShot = (Long) getInputValue(4);
		Long ts = (Long) getInputValue(5);
		if((ts - tsShot) != 0) {
//			double speedX = ((double) (endX - startX) / (ts - tsShot)) * 1500000000000.0;
//			double speedY = ((double) (endY - startY) / (ts - tsShot)) * 1500000000000.0;
			double speedX = endX - startX;
			double speedY = endY - startY;
			if(speedX != 0) {
				double m = speedY / speedX;
				double b = startY - (m * startX);
				
				//rechtes Tor
				if(endX > startX) {
					double y = m * 33941 + b;
					if(y < 29898.5 && y > 22578.5) {
						endX = (int) (startX + ((speedX  / (ts - tsShot)) * 1500000000000.0 * 2));
						if(endX > 33941) {
							return 1;
						}
					} 
				} else { // linkes Tor
					double y = m * -33968 + b;
					if(y < 29880 && y > 22560) {
						endX = (int) (startX + ((speedX  / (ts - tsShot)) * 1500000000000.0 * 2));
						if(endX < -33968) {
							return -1;
						}
					} 
				}
			}
		} 
		return 0;
	 }

}
