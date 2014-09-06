/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.modelling.network.cables;

import com.smartdist.modelling.network.eightterminalelements.Line;

public class XLPE_SDI_70 extends Line {

	public XLPE_SDI_70(double length) {
		super();

		this.serialResistivityA = 0.000342;
		this.serialResistivityB = 0.000342;
		this.serialResistivityC = 0.000342;
		this.serialResistivityN = 0.000342;

		this.serialInductivityA = 0.00000893 / 50.;
		this.serialInductivityB = 0.00000893 / 50.;
		this.serialInductivityC = 0.00000893 / 50.;
		this.serialInductivityN = 0.00000893 / 50.;

		this.length = length;

	}

}
