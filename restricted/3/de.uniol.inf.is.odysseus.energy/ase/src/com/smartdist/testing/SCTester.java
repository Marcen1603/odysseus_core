/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.testing;

import com.smartdist.modelling.network.eightterminalelements.Line;
import com.smartdist.modelling.network.eightterminalelements.SCLine;

public class SCTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Line tempLine = new SCLine(0.138, 0.062, 0.5641, 0.0615, 50);

		System.out.println("Debug stop line" + tempLine.length);
		
	}

}
