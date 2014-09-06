/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network;

import com.smartdist.util.Complex;

public interface TuneableNetworkElement {

	double[] getTuneables();
	void setTunables(double[] tuneables);
	double[] getSensitivityAnalysisStepSize();
	Complex[][] getInfinitisimalAdmittanceChange(FourWireNetwork network, Complex[] expansionPoint, int tuneable);
}
