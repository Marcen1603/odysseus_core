/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelconnecting;

import com.smartdist.util.Complex;

public abstract class FourTerminalElementConnector extends ModelConnector {

	@Override
	public abstract double myTrueValue();

	@Override
	public abstract double myTheroreticalValue(Complex[] assumedVoltages);

	@Override
	public abstract double[] myJacobianRow(Complex[] assumedVoltages);

}
