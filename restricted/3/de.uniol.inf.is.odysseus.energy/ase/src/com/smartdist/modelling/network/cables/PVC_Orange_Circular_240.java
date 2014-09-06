/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.cables;

import com.smartdist.modelling.network.eightterminalelements.Line;

public class PVC_Orange_Circular_240 extends Line {

	public PVC_Orange_Circular_240(double length) {
		super();

		this.serialResistivityA = 0.00000955;
		this.serialResistivityB = 0.00000955;
		this.serialResistivityC = 0.00000955;
		this.serialResistivityN = 0.00000955;

		this.serialInductivityA = 0.0000735 / 50.;
		this.serialInductivityB = 0.0000735 / 50.;
		this.serialInductivityC = 0.0000735 / 50.;
		this.serialInductivityN = 0.0000735 / 50.;

		this.length = length;
	}

}
