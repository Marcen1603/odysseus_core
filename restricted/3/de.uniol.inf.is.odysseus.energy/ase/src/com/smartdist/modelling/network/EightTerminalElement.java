/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network;

import com.smartdist.util.Complex;

public interface EightTerminalElement {

	public Complex[][] generateAdmittanceMatrix(double nominalFrequency);

}
