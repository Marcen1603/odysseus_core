/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelconnecting;

import com.smartdist.modelling.network.Node;
import com.smartdist.util.Complex;

public abstract class NodeConnector extends ModelConnector {
	public abstract Node myNode();

	public abstract double myTrueValue();

	public abstract double myTheroreticalValue(Complex[] assumedVoltages);

	public abstract double[] myJacobianRow(Complex[] assumedVoltages);

	public abstract void setMyTrueValue(double trueValue);
}
