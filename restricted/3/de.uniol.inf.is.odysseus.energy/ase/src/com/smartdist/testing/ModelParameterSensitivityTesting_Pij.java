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
import com.smartdist.modelconnecting.nodeconnectors.PMU;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToEarthVoltage;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToPhaseVoltage;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseReactivePower;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.eightterminalelements.LengthTuneableLine;
import com.smartdist.modelling.network.eightterminalelements.Line;
import com.smartdist.modelling.network.fourterminalelements.Earthing;
import com.smartdist.solvers.stateestimator.AdaptiveStateEstimator;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.solvers.stateestimator.Inputs;
import com.smartdist.util.Complex;

public class ModelParameterSensitivityTesting_Pij {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FourWireNetwork network = new FourWireNetwork(50);
		
		Node dummy1 = new Node(230);
		Node dummy2 = new Node(230);
		Node node1 = new Node(230);
		Node node2 = new Node(230);
		Line dummyLine1 = new Line();
		Line dummyLine2 = new Line();
		LengthTuneableLine line1 = new LengthTuneableLine();
		
		dummyLine1.length = 500;
		line1.length = 500;
		dummyLine2.length = 500;
		
		network.addVertex(dummy1);
		network.addVertex(node1);
		network.addVertex(node2);
		network.addVertex(dummy2);
		
		network.addEdge(dummyLine1, dummy1, node1);
		network.addEdge(line1, node1, node2);
		network.addEdge(dummyLine2, node2, dummy2);
		
		dummy1.myFourTerminalElements.add(new Earthing(new Complex(1000,0)));

		
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
//		StarConnectionImagPart INlIm = new StarConnectionImagPart(network,bus);
//		StarConnectionRealPart INlRe = new StarConnectionRealPart(network,bus);
//		DeltaConnectionImagPart INdIm = new DeltaConnectionImagPart(network,bus);
//		DeltaConnectionRealPart INdRe = new DeltaConnectionRealPart(network,bus);
//		AbsoluteNodalPhaseCurrent I1a = new AbsoluteNodalPhaseCurrent(network, bus, phase, 0);
//		NodalPhaseCurrentSquared I21a = new NodalPhaseCurrentSquared(network, bus, phase, 0);
		AbsoluteEightTerminalElementPhaseCurrent I12a = new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus, phase, 0);
		SinglePhaseEightTerminalElementActivePower P12a = new SinglePhaseEightTerminalElementActivePower(network, line1, bus, phase, FourWireNetwork.phaseN, 0);
		SinglePhaseEightTerminalElementReactivePower Q12a = new SinglePhaseEightTerminalElementReactivePower(network, line1, bus, phase, FourWireNetwork.phaseN, 0);
		
		
		double stepSize = 10.0;
		
		for (double length=1.0;length<=1000;length+=stepSize){

			double originalValueP1a = P1a.myTheroreticalValue(result.voltages);
			double originalValueQ1a = Q1a.myTheroreticalValue(result.voltages);
//			double originalValueINlIm = INlIm.myTheroreticalValue(result.voltages);
//			double originalValueINlRe = INlRe.myTheroreticalValue(result.voltages);
//			double originalValueINdIm = INdIm.myTheroreticalValue(result.voltages);
//			double originalValueINdRe = INdRe.myTheroreticalValue(result.voltages);
//			double originalValueI1a = I1a.myTheroreticalValue(result.voltages);
//			double originalValueI21a = I21a.myTheroreticalValue(result.voltages);
			double originalValueI12a = I12a.myTheroreticalValue(result.voltages);
			double originalValueP12a = P12a.myTheroreticalValue(result.voltages);
			double originalValueQ12a = Q12a.myTheroreticalValue(result.voltages);

			
			double analyticSensitivityP1a = P1a.getModelSensitivity(network, result.voltages, line1, tuneable);
			double analyticSensitivityQ1a = Q1a.getModelSensitivity(network, result.voltages, line1, tuneable);
//			double analyticSensitivityINlIm = INlIm.getModelSensitivity(network, result.voltages, line1, tuneable);
//			double analyticSensitivityINlRe = INlRe.getModelSensitivity(network, result.voltages, line1, tuneable);
//			double analyticSensitivityINdIm = INdIm.getModelSensitivity(network, result.voltages, line1, tuneable);
//			double analyticSensitivityINdRe = INdRe.getModelSensitivity(network, result.voltages, line1, tuneable);
//			double analyticSensitivityI1a = I1a.getModelSensitivity(network, result.voltages, line1, tuneable);
//			double analyticSensitivityI21a = I21a.getModelSensitivity(network, result.voltages, line1, tuneable);
			double analyticSensitivityI12a = I12a.getModelSensitivity(network, result.voltages, line1, tuneable);
			double analyticSensitivityP12a = P12a.getModelSensitivity(network, result.voltages, line1, tuneable);
			double analyticSensitivityQ12a = Q12a.getModelSensitivity(network, result.voltages, line1, tuneable);
			
			line1.length = length;
			network.generateAdmittanceMatrices();
			double changedValueP1a = P1a.myTheroreticalValue(result.voltages);
			double changedValueQ1a = Q1a.myTheroreticalValue(result.voltages);
//			double changedValueINlIm = INlIm.myTheroreticalValue(result.voltages);
//			double changedValueINlRe = INlRe.myTheroreticalValue(result.voltages);
//			double changedValueINdIm = INdIm.myTheroreticalValue(result.voltages);
//			double changedValueINdRe = INdRe.myTheroreticalValue(result.voltages);
//			double changedValueI1a = I1a.myTheroreticalValue(result.voltages);
//			double changedValueI21a = I21a.myTheroreticalValue(result.voltages);
			double changedValueI12a = I12a.myTheroreticalValue(result.voltages);
			double changedValueP12a = P12a.myTheroreticalValue(result.voltages);
			double changedValueQ12a = Q12a.myTheroreticalValue(result.voltages);

			System.out.println("Length: " + length );
			System.out.println("Analytic Sensitivity P1a: " + analyticSensitivityP1a + ", SampledSensitivity P1a: " + (changedValueP1a-originalValueP1a)/stepSize);
			System.out.println("Analytic Sensitivity Q1a: " + analyticSensitivityQ1a + ", SampledSensitivity Q1a: " + (changedValueQ1a-originalValueQ1a)/stepSize);
//			System.out.println("Analytic Sensitivity INlIm1: " + analyticSensitivityINlIm + ", SampledSensitivity INlIm1: " + (changedValueINlIm-originalValueINlIm)/stepSize);
//			System.out.println("Analytic Sensitivity INlRe1: " + analyticSensitivityINlRe + ", SampledSensitivity INlRe1: " + (changedValueINlRe-originalValueINlRe)/stepSize);
//			System.out.println("Analytic Sensitivity INdIm1: " + analyticSensitivityINdIm + ", SampledSensitivity INdIm1: " + (changedValueINdIm-originalValueINdIm)/stepSize);
//			System.out.println("Analytic Sensitivity INdRe1: " + analyticSensitivityINdRe + ", SampledSensitivity INdRe1: " + (changedValueINdRe-originalValueINdRe)/stepSize);
//			System.out.println("Analytic Sensitivity I1a: " + analyticSensitivityI1a + ", SampledSensitivity I1a: " + (changedValueI1a-originalValueI1a)/stepSize);
//			System.out.println("Analytic Sensitivity I21a: " + analyticSensitivityI21a + ", SampledSensitivity I21a: " + (changedValueI21a-originalValueI21a)/stepSize);
			System.out.println("Analytic Sensitivity I12a: " + analyticSensitivityI12a + ", SampledSensitivity I12a: " + (changedValueI12a-originalValueI12a)/stepSize);
			System.out.println("Analytic Sensitivity P12a: " + analyticSensitivityP12a + ", SampledSensitivity P12a: " + (changedValueP12a-originalValueP12a)/stepSize);
			System.out.println("Analytic Sensitivity Q12a: " + analyticSensitivityQ12a + ", SampledSensitivity Q12a: " + (changedValueQ12a-originalValueQ12a)/stepSize);
		}		
	}

}
