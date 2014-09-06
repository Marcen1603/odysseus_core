/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.cables;

import com.smartdist.modelling.network.eightterminalelements.Line;

public class XLPE_SDI_120 extends Line {

	public XLPE_SDI_120(double length) {
		super();

		this.serialResistivityA = 0.000197;
		this.serialResistivityB = 0.000197;
		this.serialResistivityC = 0.000197;
		this.serialResistivityN = 0.000197;

		this.serialInductivityA = 0.0000844 / 50.;
		this.serialInductivityB = 0.0000844 / 50.;
		this.serialInductivityC = 0.0000844 / 50.;
		this.serialInductivityN = 0.0000844 / 50.;

		this.length = length;

	}

}
