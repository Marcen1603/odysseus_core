package de.uniol.inf.is.odysseus.debs2013.spatial;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class DEBSDistance extends AbstractFunction<Integer>{

	private static final long serialVersionUID = 8159082937727522598L;

		
	@Override
	public int getArity() {
		return 6;
	}
	
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
    	SDFDatatype.LONG
	};

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return accTypes;
	}

	@Override
	public Integer getValue() {
		final Integer x = ((Integer) getInputValue(0));
		final Integer y = ((Integer) getInputValue(1));
		final Integer z = ((Integer) getInputValue(2));
		final Integer playerX = ((Integer) getInputValue(3));
		final Integer playerY = ((Integer) getInputValue(4));
		final Integer playerZ = ((Integer) getInputValue(5));
		//Manhattan; return (Math.abs(x - playerX) + Math.abs(y - playerY) + Math.abs(z - playerZ));
		return (int) (Math.sqrt(Math.pow((x - playerX), 2) + Math.pow((y - playerY), 2) + Math.pow((z - playerZ), 2)));
	 }

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.INTEGER;
	}

	@Override
	public String getSymbol() {
		return "DEBSDistance";
	}
}
