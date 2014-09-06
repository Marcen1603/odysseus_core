/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */

package com.smartdist.modelconnecting;

import com.smartdist.util.Complex;

public abstract class EightTerminalElementConnector extends ModelConnector {
	public abstract double myTrueValue();

	public abstract double myTheroreticalValue(Complex[] assumedVoltages);

	public abstract double[] myJacobianRow(Complex[] assumedVoltages);
}
