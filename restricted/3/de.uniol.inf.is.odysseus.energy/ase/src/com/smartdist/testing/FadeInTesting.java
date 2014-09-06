/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.testing;

import com.smartdist.modelconnecting.nodeconnectors.PMU;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToEarthVoltage;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToPhaseVoltage;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseReactivePower;
import com.smartdist.modelconnecting.nodeconnectors.StarConnectionImagPart;
import com.smartdist.modelconnecting.nodeconnectors.StarConnectionRealPart;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.eightterminalelements.Line;
import com.smartdist.modelling.network.fourterminalelements.Earthing;
import com.smartdist.solvers.stateestimator.AdaptiveStateEstimator;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.solvers.stateestimator.Inputs;
import com.smartdist.util.Complex;
import com.smartdist.util.DevelopmentHelper;

public class FadeInTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FourWireNetwork network = new FourWireNetwork(50.0);
		
		Node bus1 = new Node(230);
		Node bus2 = new Node(230);
		
		Line line1 = new Line();
		line1.length = 500;
		
		network.addVertex(bus1);
		network.addVertex(bus2);
		network.addEdge(line1, bus1, bus2);
		
		bus1.myFourTerminalElements.add(new Earthing(new Complex(1000.0,0.0)));
		
		Inputs inputs = new Inputs();
		
		double stdV = 5;
		double stdI = 10;
		double stdS = 100;
		double stdd = 0.5;
		
		inputs.addConnector(new PhaseToPhaseVoltage(network, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 230), stdV);
		inputs.addConnector(new PhaseToPhaseVoltage(network, bus1, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 230), stdV);
		inputs.addConnector(new PhaseToPhaseVoltage(network, bus1, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 230), stdV);
		inputs.addConnector(new PhaseToEarthVoltage(network, bus1, FourWireNetwork.phaseN,0), stdV);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseA, 0), stdd);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseB, 120), stdd);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseC, -120), stdd);
		
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 50000), stdS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0), stdS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, -5000), stdS);
		
		inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, -5000), stdS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0), stdS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, -50000), stdS);
		
		inputs.addConnector(new StarConnectionRealPart(network, bus2), stdI);
		inputs.addConnector(new StarConnectionImagPart(network, bus2), stdI);
		
		double epsilon = 0.0001; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 20; // maximum number of iterations
		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();
		EstimationResultSet result = estimator.estimateState(network, inputs, epsilon, maxIterations);

		System.out.println("Number of Iterations: " + result.usedIterrations + " of " + result.allowedMaxIterations + " allowed");
		DevelopmentHelper.printEstimationResultToConsole(network, result);
	}

}
