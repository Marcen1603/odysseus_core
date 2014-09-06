/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.testing;

import com.smartdist.modelconnecting.ModelConnector;
import com.smartdist.modelconnecting.eightterminalelementconnectors.AbsoluteEightTerminalElementPhaseCurrent;
import com.smartdist.modelconnecting.eightterminalelementconnectors.SinglePhaseEightTerminalElementActivePower;
import com.smartdist.modelconnecting.eightterminalelementconnectors.SinglePhaseEightTerminalElementReactivePower;
import com.smartdist.modelconnecting.nodeconnectors.AbsoluteNodalPhaseCurrent;
import com.smartdist.modelconnecting.nodeconnectors.DeltaConnectionImagPart;
import com.smartdist.modelconnecting.nodeconnectors.DeltaConnectionRealPart;
import com.smartdist.modelconnecting.nodeconnectors.LambdaConnectionImagPart;
import com.smartdist.modelconnecting.nodeconnectors.LambdaConnectionRealPart;
import com.smartdist.modelconnecting.nodeconnectors.NodalPhaseCurrentSquared;
import com.smartdist.modelconnecting.nodeconnectors.PMU;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToEarthVoltage;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToPhaseVoltage;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower_backup;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseReactivePower_backup;
import com.smartdist.modelconnecting.nodeconnectors.StarConnectionImagPart;
import com.smartdist.modelconnecting.nodeconnectors.StarConnectionRealPart;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.eightterminalelements.Line;
import com.smartdist.modelling.network.eightterminalelements.SCLine;
import com.smartdist.util.Complex;
import com.smartdist.util.DevelopmentHelper;

public class JacobianTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FourWireNetwork network = new FourWireNetwork(50);
//		Node bus0 = new Node(230);
		Node bus1 = new Node(230);
		Node bus2 = new Node(230);
//		Line line1 = new Line();
		Line line1 = new SCLine(0.138, 0.062, 0.5641, 0.0615, 50);
		line1.length = 1000;
		network.addVertex(bus1);
		network.addVertex(bus2);
		network.addEdge(line1,  bus1, bus2);
		network.generateAdmittanceMatrices();
		
		double centerV1 = 230;
		double minV2 = 220;
		int stepsV2 = 20;
		double stepV2 = 0.01;
		double minDelta2 =-10;
		int stepsDelta2 = 20;
		double stepDelta2 = 0.1;
		double[][] results = new double[stepsV2][stepsDelta2];

		ModelConnector[] toTest = new ModelConnector[16];
		toTest[0] = new AbsoluteNodalPhaseCurrent(network, bus1, FourWireNetwork.phaseA,0.0);
		toTest[1] = new SinglePhaseEightTerminalElementActivePower(network, line1, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0);
		toTest[2] = new SinglePhaseEightTerminalElementReactivePower(network, line1, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0);
		toTest[3] = new DeltaConnectionImagPart(network, bus1);
		toTest[4] = new DeltaConnectionRealPart(network, bus1);
		toTest[5] = new NodalPhaseCurrentSquared(network, bus1, FourWireNetwork.phaseA,0.0);
		toTest[6] = new PhaseToEarthVoltage(network, bus1, FourWireNetwork.phaseA,0.0);
		toTest[7] = new PhaseToPhaseVoltage(network, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0);
		toTest[8] = new PMU(network, bus1, FourWireNetwork.phaseA,0.0);
		toTest[9] = new SinglePhaseActivePower_backup(network, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0);
		toTest[10] = new SinglePhaseReactivePower_backup(network, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0);
		toTest[11] = new StarConnectionRealPart(network, bus1);
		toTest[12] = new StarConnectionImagPart(network, bus1);
		toTest[13] = new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus1, FourWireNetwork.phaseA,0.0);
		toTest[14] = new LambdaConnectionRealPart(network, bus1);
		toTest[15] = new LambdaConnectionImagPart(network, bus1);
		
		for (int test=0;test<toTest.length;test++){
			for (int countV=0;countV<stepsV2;countV++){
				for (int countD=0;countD<stepsDelta2;countD++){
					double v2=minV2+countV*stepV2;
					double v2n=countV*stepV2;
					double d2=minDelta2+countD*stepDelta2;
					Complex[] voltages = new Complex[8];
					voltages[0] = new Complex(centerV1*Math.cos(0./360.*2*Math.PI),centerV1*Math.sin(0./360.*2*Math.PI));
					voltages[1] = new Complex(centerV1*Math.cos(120./360.*2*Math.PI),centerV1*Math.sin(120./360.*2*Math.PI));
					voltages[2] = new Complex(centerV1*Math.cos(-120./360.*2*Math.PI),centerV1*Math.sin(-120./360.*2*Math.PI));
					voltages[3] = new Complex(20.0,0.0);
					voltages[4] = new Complex(v2*Math.cos((0.+d2)/360.*2*Math.PI),v2*Math.cos((0.+d2)/360.*2*Math.PI));
					voltages[5] = new Complex(v2*Math.cos((120.+d2)/360.*2*Math.PI),v2*Math.cos((110.+d2)/360.*2*Math.PI));
					voltages[6] = new Complex(v2*Math.cos((-120.+d2)/360.*2*Math.PI),v2*Math.cos((-120.+d2)/360.*2*Math.PI));
					voltages[7] = new Complex(v2n*java.lang.Math.cos(20.+d2), v2n*java.lang.Math.sin(20.+d2));
					
					double stepSize = 1;
					double[] sampledJacobian = new double[16];
	
					ModelConnector testConnector = toTest[test];
					
					
					double[] analyticJacobian = testConnector.myJacobianRow(voltages);
					
					double originalValue = testConnector.myTheroreticalValue(voltages);
					for (int i=0;i<8;i++){
						voltages[i].add(new Complex(stepSize,0.0));
						sampledJacobian[2*i+0] = (testConnector.myTheroreticalValue(voltages) - originalValue)/stepSize;
						voltages[i].sustract(new Complex(stepSize,0.0));
						voltages[i].add(new Complex(0.0,stepSize));
						sampledJacobian[2*i+1] = (testConnector.myTheroreticalValue(voltages) - originalValue)/stepSize;
						voltages[i].sustract(new Complex(0.0,stepSize));
					}
					if (test==9){
						DevelopmentHelper.printToConsole(sampledJacobian);
						DevelopmentHelper.printToConsole(analyticJacobian);	
						System.out.println("--------------");
					}
					double temp = 0;
					double tempLength = 0;
					for (int i=0;i<sampledJacobian.length;i++){
						temp += Math.pow((sampledJacobian[i]-analyticJacobian[i]),2);
						tempLength += Math.pow(sampledJacobian[i], 2);
					}
					results[countV][countD] = Math.sqrt(temp)/Math.sqrt(tempLength);
				}
				
			}
			double average = 0;
			for (int i=0;i<results.length;i++){
				for (int j=0;j<results[0].length;j++){
					average += results[i][j];
				}
			}
			average = average/(results.length*results[0].length);
			//DevelopmentHelper.printToConsole(results);
			System.out.println(toTest[test].toString() + ": " + average);
		}
	}

}
