/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network;

import com.smartdist.util.Complex;

public interface FourTerminalElement {

	/**
	 * 
	 * @param nominalFrequency
	 *            nominal operational frequency of the power system
	 * @return has to return a 4-by-4 matrix of
	 *         {@link com.smartgrid.util.Complex} giving its admittance model in
	 *         physical domain. No per unit reference system used. rows and
	 *         columns in ascending order represent phase A, B, C and N
	 */
	public Complex[][] generateAdmittanceMatrix(double nominalFrequency);

}
