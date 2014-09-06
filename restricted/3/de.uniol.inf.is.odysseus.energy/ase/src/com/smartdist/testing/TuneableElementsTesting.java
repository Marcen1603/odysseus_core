/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.testing;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.eightterminalelements.LengthTuneableLine;
import com.smartdist.util.Complex;
import com.smartdist.util.DevelopmentHelper;

public class TuneableElementsTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FourWireNetwork network = new FourWireNetwork(50);
		LengthTuneableLine line1 = new LengthTuneableLine();
		line1.length=100;
		double stepSize = 1.0;
		
		Complex[][] originalAdmittanceMatrix = line1.generateAdmittanceMatrix(50);
		Complex[][] analyticSensitivity = line1.getInfinitisimalAdmittanceChange(network, null, 0);
		line1.length += stepSize;
		Complex[][] perturbatedAdmittanceMatrix = line1.generateAdmittanceMatrix(50);
		Complex[][] sampledSensitivity = new Complex[8][8];
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				sampledSensitivity[i][j] = (new Complex(perturbatedAdmittanceMatrix[i][j])).sustract(originalAdmittanceMatrix[i][j]).divide(stepSize);
			}
		}
		System.out.println("Analytic Sensitivity:");
		DevelopmentHelper.printToConsole(analyticSensitivity);
		System.out.println("Sampled Sensitivity:");
		DevelopmentHelper.printToConsole(sampledSensitivity);
		
		double[][] relativeErrors = new double[8][8];
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				relativeErrors[i][j] = new Complex(analyticSensitivity[i][j]).sustract(sampledSensitivity[i][j]).getAbs()/sampledSensitivity[i][j].getAbs();
			}
		}
		System.out.println("Relative Sensitivity Error:");
		DevelopmentHelper.printToConsole(relativeErrors);
		
	}

}
