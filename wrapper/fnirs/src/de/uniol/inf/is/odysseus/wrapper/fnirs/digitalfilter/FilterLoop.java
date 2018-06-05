package de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter;


public class FilterLoop 
{
	private final int numPoles;
	private final double gain;
	private final double[] xCoeffs, yCoeffs;
	
	private final double[] xv;
	private final double[] yv;	

	public FilterLoop(DigitalFilter filter)
	{
		numPoles = filter.getNumPoles();
		xCoeffs = filter.getXCoefficients();
		yCoeffs = filter.getYCoefficients();
	    if (xCoeffs[numPoles] != 1.0)
	    	throw new IllegalArgumentException("x coefficient " + numPoles + " is " + xCoeffs[numPoles] + ", must be 1.0");
	    
		xv = new double[numPoles+1];
		yv = new double[numPoles+1]; 
	    
		gain = filter.getDCGain().magnitude();
	}

	public double filterStep(double input)
	{ 
    	for (int j=0; j < numPoles; j++)
    	{ 
    		xv[j] = xv[j+1];
    		yv[j] = yv[j+1];
    	}		
    	xv[numPoles] = yv[numPoles] = input / gain;
    	for (int j=0; j < numPoles; j++) 
    		yv[numPoles] += (xCoeffs[j] * xv[j]) + (yCoeffs[j] * yv[j]);
    	
    	return yv[numPoles];
	}	
}
