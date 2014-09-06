/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.testing;

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
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseReactivePower;
import com.smartdist.modelconnecting.nodeconnectors.StarConnectionImagPart;
import com.smartdist.modelconnecting.nodeconnectors.StarConnectionRealPart;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.fourterminalelements.Earthing;
import com.smartdist.solvers.stateestimator.AdaptiveStateEstimator;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.solvers.stateestimator.Inputs;
import com.smartdist.util.Complex;

public class ModelParameterSensitivityTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FourWireNetwork network = new FourWireNetwork(50);
		
		Node node1 = new Node(230);
		Node node2 = new Node(230);
		EightTerminalElementForSensitivityTesting line1 = new EightTerminalElementForSensitivityTesting();
		
		line1.length = 500;
		
		network.addVertex(node1);
		network.addVertex(node2);
		network.addEdge(line1, node1, node2);
		node1.myFourTerminalElements.add(new Earthing(new Complex(1000,0)));

		
		double stdV = 1;
//		double stdI = 5;
		double stdS = 50;
		double stdd = 0.1;
		Inputs inputs = new Inputs();
		
		inputs.addConnector(new PhaseToPhaseVoltage(network,node1,FourWireNetwork.phaseA, FourWireNetwork.phaseN,230.0), stdV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,node1,FourWireNetwork.phaseB, FourWireNetwork.phaseN,230.0), stdV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,node1,FourWireNetwork.phaseC, FourWireNetwork.phaseN,230.0), stdV);
		inputs.addConnector(new PMU(network,node1,FourWireNetwork.phaseA,0),stdd);
		inputs.addConnector(new PMU(network,node1,FourWireNetwork.phaseB,120),stdd);
		inputs.addConnector(new PMU(network,node1,FourWireNetwork.phaseC,-120),stdd);
		//inputs.addConnector(new AbsoluteNodalPhaseCurrent(network,node1,FourWireNetwork.phaseN,0), stdI);
		inputs.addConnector(new PhaseToEarthVoltage(network,node1,FourWireNetwork.phaseN,0), stdV);
		
		inputs.addConnector(new SinglePhaseActivePower(network,node2,FourWireNetwork.phaseA,FourWireNetwork.phaseN,5000.0), stdS);
		inputs.addConnector(new SinglePhaseActivePower(network,node2,FourWireNetwork.phaseB,FourWireNetwork.phaseN,5000.0), stdS);
		inputs.addConnector(new SinglePhaseActivePower(network,node2,FourWireNetwork.phaseC,FourWireNetwork.phaseN,5000.0), stdS);
		inputs.addConnector(new SinglePhaseReactivePower(network,node2,FourWireNetwork.phaseA,FourWireNetwork.phaseN,0), stdS);
		inputs.addConnector(new SinglePhaseReactivePower(network,node2,FourWireNetwork.phaseB,FourWireNetwork.phaseN,0), stdS);
		inputs.addConnector(new SinglePhaseReactivePower(network,node2,FourWireNetwork.phaseC,FourWireNetwork.phaseN,0), stdS);
		//inputs.addConnector(new AbsoluteNodalPhaseCurrent(network,node2,FourWireNetwork.phaseN,0), stdI);
		inputs.addConnector(new PhaseToEarthVoltage(network,node2,FourWireNetwork.phaseN,0), stdV);
		
		double epsilon = 0.0001; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 50; // maximum number of iterations
		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();

		EstimationResultSet result = estimator.estimateState(network, inputs, epsilon, maxIterations);
		
		//DevelopmentHelper.printEstimationResultToConsole(network, result);
		
		
		int tuneable = 0;
		Node bus = node1;
		int phase = FourWireNetwork.phaseC;
//		Complex[][] originalAdmittance = line1.generateAdmittanceMatrix(network.nominalFrequency);
		SinglePhaseActivePower P1a = new SinglePhaseActivePower(network, bus, phase, FourWireNetwork.phaseN,0);
		SinglePhaseReactivePower Q1a = new SinglePhaseReactivePower(network, bus, phase, FourWireNetwork.phaseN,0);
		StarConnectionImagPart INsIm = new StarConnectionImagPart(network,bus);
		StarConnectionRealPart INsRe = new StarConnectionRealPart(network,bus);
		DeltaConnectionImagPart INdIm = new DeltaConnectionImagPart(network,bus);
		DeltaConnectionRealPart INdRe = new DeltaConnectionRealPart(network,bus);
		AbsoluteNodalPhaseCurrent I1a = new AbsoluteNodalPhaseCurrent(network, bus, phase, 0);
		NodalPhaseCurrentSquared I21a = new NodalPhaseCurrentSquared(network, bus, phase, 0);
		AbsoluteEightTerminalElementPhaseCurrent I12a = new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus, phase, 0);
		SinglePhaseEightTerminalElementActivePower P12a = new SinglePhaseEightTerminalElementActivePower(network, line1, bus, phase, FourWireNetwork.phaseN, 0);
		SinglePhaseEightTerminalElementReactivePower Q12a = new SinglePhaseEightTerminalElementReactivePower(network, line1, bus, phase, FourWireNetwork.phaseN, 0);
		LambdaConnectionImagPart INlIm = new LambdaConnectionImagPart(network,bus);
		LambdaConnectionRealPart INlRe = new LambdaConnectionRealPart(network,bus);

		
		double originalValueP1a = P1a.myTheroreticalValue(result.voltages);
		double originalValueQ1a = Q1a.myTheroreticalValue(result.voltages);
		double originalValueINsIm = INsIm.myTheroreticalValue(result.voltages);
		double originalValueINsRe = INsRe.myTheroreticalValue(result.voltages);
		double originalValueINdIm = INdIm.myTheroreticalValue(result.voltages);
		double originalValueINdRe = INdRe.myTheroreticalValue(result.voltages);
		double originalValueI1a = I1a.myTheroreticalValue(result.voltages);
		double originalValueI21a = I21a.myTheroreticalValue(result.voltages);
		double originalValueI12a = I12a.myTheroreticalValue(result.voltages);
		double originalValueP12a = P12a.myTheroreticalValue(result.voltages);
		double originalValueQ12a = Q12a.myTheroreticalValue(result.voltages);
		double originalValueINlIm = INlIm.myTheroreticalValue(result.voltages);
		double originalValueINlRe = INlRe.myTheroreticalValue(result.voltages);

		
		for (int i=0;i<8;i++){
			for (int j=0;j<8;j++){
				Complex step = new Complex(0.0001,0);
				line1.dY[i][j].add(step);
//				Complex[][] analyticSensitivities = line1.getInfinitisimalAdmittanceChange(network, result.voltages, tuneable);
				
				double analyticSensitivityP1a = P1a.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityQ1a = Q1a.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityINsIm = INsIm.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityINsRe = INsRe.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityINdIm = INdIm.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityINdRe = INdRe.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityI1a = I1a.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityI21a = I21a.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityI12a = I12a.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityP12a = P12a.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityQ12a = Q12a.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityINlIm = INlIm.getModelSensitivity(network, result.voltages, line1, tuneable);
				double analyticSensitivityINlRe = INlRe.getModelSensitivity(network, result.voltages, line1, tuneable);
				
				network.generateAdmittanceMatrices();
				double changedValueP1a = P1a.myTheroreticalValue(result.voltages);
				double changedValueQ1a = Q1a.myTheroreticalValue(result.voltages);
				double changedValueINsIm = INsIm.myTheroreticalValue(result.voltages);
				double changedValueINsRe = INsRe.myTheroreticalValue(result.voltages);
				double changedValueINdIm = INdIm.myTheroreticalValue(result.voltages);
				double changedValueINdRe = INdRe.myTheroreticalValue(result.voltages);
				double changedValueI1a = I1a.myTheroreticalValue(result.voltages);
				double changedValueI21a = I21a.myTheroreticalValue(result.voltages);
				double changedValueI12a = I12a.myTheroreticalValue(result.voltages);
				double changedValueP12a = P12a.myTheroreticalValue(result.voltages);
				double changedValueQ12a = Q12a.myTheroreticalValue(result.voltages);
				double changedValueINlIm = INlIm.myTheroreticalValue(result.voltages);
				double changedValueINlRe = INlRe.myTheroreticalValue(result.voltages);

				line1.dY[i][j] = new Complex(0,0);
				System.out.println("element " + i + "," + j);
				System.out.println("Analytic Sensitivity P1a: " + analyticSensitivityP1a + ", SampledSensitivity P1a: " + (changedValueP1a-originalValueP1a));
				System.out.println("Analytic Sensitivity Q1a: " + analyticSensitivityQ1a + ", SampledSensitivity Q1a: " + (changedValueQ1a-originalValueQ1a));
				System.out.println("Analytic Sensitivity INsIm1: " + analyticSensitivityINsIm + ", SampledSensitivity INsIm1: " + (changedValueINsIm-originalValueINsIm));
				System.out.println("Analytic Sensitivity INsRe1: " + analyticSensitivityINsRe + ", SampledSensitivity INsRe1: " + (changedValueINsRe-originalValueINsRe));
				System.out.println("Analytic Sensitivity INdIm1: " + analyticSensitivityINdIm + ", SampledSensitivity INdIm1: " + (changedValueINdIm-originalValueINdIm));
				System.out.println("Analytic Sensitivity INdRe1: " + analyticSensitivityINdRe + ", SampledSensitivity INdRe1: " + (changedValueINdRe-originalValueINdRe));
				System.out.println("Analytic Sensitivity I1a: " + analyticSensitivityI1a + ", SampledSensitivity I1a: " + (changedValueI1a-originalValueI1a));
				System.out.println("Analytic Sensitivity I21a: " + analyticSensitivityI21a + ", SampledSensitivity I21a: " + (changedValueI21a-originalValueI21a));
				System.out.println("Analytic Sensitivity I12a: " + analyticSensitivityI12a + ", SampledSensitivity I12a: " + (changedValueI12a-originalValueI12a));
				System.out.println("Analytic Sensitivity P12a: " + analyticSensitivityP12a + ", SampledSensitivity P12a: " + (changedValueP12a-originalValueP12a));
				System.out.println("Analytic Sensitivity Q12a: " + analyticSensitivityQ12a + ", SampledSensitivity Q12a: " + (changedValueQ12a-originalValueQ12a));
				System.out.println("Analytic Sensitivity INlIm1: " + analyticSensitivityINlIm + ", SampledSensitivity INlIm1: " + (changedValueINlIm-originalValueINlIm));
				System.out.println("Analytic Sensitivity INlRe1: " + analyticSensitivityINlRe + ", SampledSensitivity INlRe1: " + (changedValueINlRe-originalValueINlRe));

			}
		}		
	}

}
