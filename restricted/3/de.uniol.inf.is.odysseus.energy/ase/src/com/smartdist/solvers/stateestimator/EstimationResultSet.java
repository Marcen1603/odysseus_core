/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.solvers.stateestimator;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.util.Complex;

public class EstimationResultSet {

	public FourWireNetwork network = null;
	public double[][] jacobian = null;
	public double[][] pinv = null;
	public int rank = 0;
	public double[] singularValues = null;
	public double[][] leftSingularVectors = null;
	public double[][] rightSingularVectors = null;
	public Complex[] voltages = null;
	public double[] absCoverage = null;
	public double[] argCoverage = null;
	public double[][] principalAngles = null;
	public int usedIterrations = 0;
	public int allowedMaxIterations = 0;

}
