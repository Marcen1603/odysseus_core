/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.cables;

import com.smartdist.modelling.network.eightterminalelements.Line;

public class PVC_Orange_Circular_150 extends Line {

	public PVC_Orange_Circular_150(double length) {
		super();

		this.serialResistivityA = 0.000153;
		this.serialResistivityB = 0.000153;
		this.serialResistivityC = 0.000153;
		this.serialResistivityN = 0.000153;

		this.serialInductivityA = 0.0000745 / 50.;
		this.serialInductivityB = 0.0000745 / 50.;
		this.serialInductivityC = 0.0000745 / 50.;
		this.serialInductivityN = 0.0000745 / 50.;

		this.length = length;

	}

}
