/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.cables;

import com.smartdist.modelling.network.eightterminalelements.Line;

public class PVC_Orange_Circular_120 extends Line {

	public PVC_Orange_Circular_120(double length) {

		this.serialResistivityA = 0.000188;
		this.serialResistivityB = 0.000188;
		this.serialResistivityC = 0.000188;
		this.serialResistivityN = 0.000188;

		this.serialInductivityA = 0.0000743 / 50.;
		this.serialInductivityB = 0.0000743 / 50.;
		this.serialInductivityC = 0.0000743 / 50.;
		this.serialInductivityN = 0.0000743 / 50.;

		this.length = length;

	}

}
