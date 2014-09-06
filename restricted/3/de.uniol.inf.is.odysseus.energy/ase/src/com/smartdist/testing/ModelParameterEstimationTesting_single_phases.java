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
import com.smartdist.modelconnecting.nodeconnectors.LambdaConnectionImagPart;
import com.smartdist.modelconnecting.nodeconnectors.LambdaConnectionRealPart;
import com.smartdist.modelconnecting.nodeconnectors.PMU;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToPhaseVoltage;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseReactivePower;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.TuneableEightTerminalElement;
import com.smartdist.modelling.network.eightterminalelements.SinglePhaseLengthTuneableLine;
import com.smartdist.modelling.network.fourterminalelements.Earthing;
import com.smartdist.solvers.stateestimator.AdaptiveStateEstimator;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.solvers.stateestimator.Inputs;
import com.smartdist.solvers.stateestimator.ModelParameterEstimator_extreme;
import com.smartdist.util.Complex;

public class ModelParameterEstimationTesting_single_phases {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		FourWireNetwork network = new FourWireNetwork(50);	
		Node bus1 = new Node(230);
		Node bus2 = new Node(230);
		Node bus3 = new Node(230);
		Node bus4 = new Node(230);
		Node bus5 = new Node(230);
		Node bus6 = new Node(230);
		Node bus7 = new Node(230);
		Node bus8 = new Node(230);
		Node bus9 = new Node(230);
		
		Node[] buses = new Node[9];
		buses[0] = bus1;
		buses[1] = bus2;
		buses[2] = bus3;
		buses[3] = bus4;
		buses[4] = bus5;
		buses[5] = bus6;
		buses[6] = bus7;
		buses[7] = bus8;
		buses[8] = bus9;
		
		SinglePhaseLengthTuneableLine line1 = new SinglePhaseLengthTuneableLine();
		SinglePhaseLengthTuneableLine line2 = new SinglePhaseLengthTuneableLine();
		SinglePhaseLengthTuneableLine line3 = new SinglePhaseLengthTuneableLine();
		SinglePhaseLengthTuneableLine line4 = new SinglePhaseLengthTuneableLine();
		SinglePhaseLengthTuneableLine line5 = new SinglePhaseLengthTuneableLine();
		SinglePhaseLengthTuneableLine line6 = new SinglePhaseLengthTuneableLine();
		SinglePhaseLengthTuneableLine line7 = new SinglePhaseLengthTuneableLine();
		SinglePhaseLengthTuneableLine line8 = new SinglePhaseLengthTuneableLine();
		
		SinglePhaseLengthTuneableLine[] lines = new SinglePhaseLengthTuneableLine[8];
		lines[0] = line1;
		lines[1] = line2;
		lines[2] = line3;
		lines[3] = line4;
		lines[4] = line5;
		lines[5] = line6;
		lines[6] = line7;
		lines[7] = line8;
		
		network.addVertex(bus1);
		network.addVertex(bus2);
		network.addVertex(bus3);
		network.addVertex(bus4);
		network.addVertex(bus5);
		network.addVertex(bus6);
		network.addVertex(bus7);
		network.addVertex(bus8);
		network.addVertex(bus9);

		network.addEdge(line1, bus1, bus2);
		network.addEdge(line2, bus2, bus3);
		network.addEdge(line3, bus3, bus4);
		network.addEdge(line4, bus4, bus5);
		network.addEdge(line5, bus5, bus6);
		network.addEdge(line6, bus6, bus7);
		network.addEdge(line7, bus7, bus8);
		network.addEdge(line8, bus8, bus9);

//		line1.length = 500;
//		line2.length = 600;
//		line3.length = 400;
//		line4.length = 450;
//		line5.length = 500;
//		line6.length = 600;
//		line7.length = 400;
//		line8.length = 450;
		
		line1.lengthA=500;
		line1.lengthA=550;
		line1.lengthC=400;
		line1.lengthN=500;
		line2.lengthA=650;
		line2.lengthB=700;
		line2.lengthC=600;
		line2.lengthN=650;
		line3.lengthA=400;
		line3.lengthB=450;
		line3.lengthC=350;
		line3.lengthN=400;
		line4.lengthA=450;
		line4.lengthB=500;
		line4.lengthC=400;
		line4.lengthN=450;
		line5.lengthA=500;
		line5.lengthB=550;
		line5.lengthC=450;
		line5.lengthN=500;
		line6.lengthA=600;
		line6.lengthB=650;
		line6.lengthC=550;
		line6.lengthN=600;
		line7.lengthA=400;
		line7.lengthB=450;
		line7.lengthC=350;
		line7.lengthN=400;
		line8.lengthA=450;
		line8.lengthB=500;
		line8.lengthC=400;
		line8.lengthN=450;

		bus1.myFourTerminalElements.add(new Earthing(new Complex(1000,0)));
		
		Inputs inputs = new Inputs();
		
		double stdVS = 100;
		double stdVV = 2;
		double stdVI = 50;
		double stdVd = 0.05;
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseA,0.0), stdVd);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseB,120.0), stdVd);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseC,-120.0), stdVd);
		//inputs.addConnector(new NodalPhaseCurrentSquared(network, bus1, FourWireNetwork.phaseN,0.0), stdVI);

//		inputs.addConnector(new PhaseToEarthVoltage(network,bus1,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus1), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus1), stdVI);
		
		
		///////////////
		// 
		//  Node 2
		//
		double P2 = 500.0;
		ModelConnector P2a = new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P2);
		ModelConnector P2b = new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P2);
		ModelConnector P2c = new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P2);	
		inputs.addConnector(P2a, stdVS);
		inputs.addConnector(P2b, stdVS);
		inputs.addConnector(P2c, stdVS);

		double Q2 = 0.0;
		ModelConnector Q2a = new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN,Q2);
		ModelConnector Q2b = new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN,Q2);
		ModelConnector Q2c = new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN,Q2);
		inputs.addConnector(Q2a,stdVS);
		inputs.addConnector(Q2b,stdVS);
		inputs.addConnector(Q2c,stdVS);

//		inputs.addConnector(new PhaseToEarthVoltage(network,bus2,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus2), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus2), stdVI);

		
		///////////////
		// 
		//  Node 3
		//
		double P3 = -500.0;
		ModelConnector P3a = new SinglePhaseActivePower(network, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P3);
		ModelConnector P3b = new SinglePhaseActivePower(network, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P3);
		ModelConnector P3c = new SinglePhaseActivePower(network, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P3);
		inputs.addConnector(P3a, stdVS);
		inputs.addConnector(P3b, stdVS);
		inputs.addConnector(P3c, stdVS);
		
		double Q3 = 0.0;
		ModelConnector Q3a = new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN,Q3);
		ModelConnector Q3b = new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN,Q3);
		ModelConnector Q3c = new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN,Q3);
		inputs.addConnector(Q3a,stdVS);
		inputs.addConnector(Q3b,stdVS);
		inputs.addConnector(Q3c,stdVS);
		
//		inputs.addConnector(new PhaseToEarthVoltage(network,bus3,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus3), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus3), stdVI);

		
		///////////////
		// 
		//  Node 4
		//		
		double P4 = 500.0;
		ModelConnector P4a = new SinglePhaseActivePower(network, bus4, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P4);
		ModelConnector P4b = new SinglePhaseActivePower(network, bus4, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P4);
		ModelConnector P4c = new SinglePhaseActivePower(network, bus4, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P4);	
		inputs.addConnector(P4a, stdVS);
		inputs.addConnector(P4b, stdVS);
		inputs.addConnector(P4c, stdVS);
		
		double Q4 = 0.0;
		ModelConnector Q4a = new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseA, FourWireNetwork.phaseN,Q4);
		ModelConnector Q4b = new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseB, FourWireNetwork.phaseN,Q4);
		ModelConnector Q4c = new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseC, FourWireNetwork.phaseN,Q4);
		inputs.addConnector(Q4a,stdVS);
		inputs.addConnector(Q4b,stdVS);
		inputs.addConnector(Q4c,stdVS);
		
//		inputs.addConnector(new PhaseToEarthVoltage(network,bus4,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus4), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus4), stdVI);

		
		///////////////
		// 
		//  Node 5
		//		
		double P5 = -500.0;
		ModelConnector P5a = new SinglePhaseActivePower(network, bus5, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P5);
		ModelConnector P5b = new SinglePhaseActivePower(network, bus5, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P5);
		ModelConnector P5c = new SinglePhaseActivePower(network, bus5, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P5);	
		inputs.addConnector(P5a, stdVS);
		inputs.addConnector(P5b, stdVS);
		inputs.addConnector(P5c, stdVS);
		
		double Q5 = 0.0;
		ModelConnector Q5a = new SinglePhaseReactivePower(network, bus5, FourWireNetwork.phaseA, FourWireNetwork.phaseN,Q5);
		ModelConnector Q5b = new SinglePhaseReactivePower(network, bus5, FourWireNetwork.phaseB, FourWireNetwork.phaseN,Q5);
		ModelConnector Q5c = new SinglePhaseReactivePower(network, bus5, FourWireNetwork.phaseC, FourWireNetwork.phaseN,Q5);
		inputs.addConnector(Q5a,stdVS);
		inputs.addConnector(Q5b,stdVS);
		inputs.addConnector(Q5c,stdVS);
		
//		inputs.addConnector(new PhaseToEarthVoltage(network,bus5,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus5), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus5), stdVI);


		///////////////
		// 
		//  Node 6
		//		
		double P6 = 500.0;
		ModelConnector P6a = new SinglePhaseActivePower(network, bus6, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P6);
		ModelConnector P6b = new SinglePhaseActivePower(network, bus6, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P6);
		ModelConnector P6c = new SinglePhaseActivePower(network, bus6, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P6);	
		inputs.addConnector(P6a, stdVS);
		inputs.addConnector(P6b, stdVS);
		inputs.addConnector(P6c, stdVS);
		
		double Q6 = 0.0;
		ModelConnector Q6a = new SinglePhaseReactivePower(network, bus6, FourWireNetwork.phaseA, FourWireNetwork.phaseN,Q6);
		ModelConnector Q6b = new SinglePhaseReactivePower(network, bus6, FourWireNetwork.phaseB, FourWireNetwork.phaseN,Q6);
		ModelConnector Q6c = new SinglePhaseReactivePower(network, bus6, FourWireNetwork.phaseC, FourWireNetwork.phaseN,Q6);
		inputs.addConnector(Q6a,stdVS);
		inputs.addConnector(Q6b,stdVS);
		inputs.addConnector(Q6c,stdVS);
		
//		inputs.addConnector(new PhaseToEarthVoltage(network,bus6,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus6), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus6), stdVI);

		
		///////////////
		// 
		//  Node 7
		//		
		double P7 = -500.0;
		ModelConnector P7a = new SinglePhaseActivePower(network, bus7, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P7);
		ModelConnector P7b = new SinglePhaseActivePower(network, bus7, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P7);
		ModelConnector P7c = new SinglePhaseActivePower(network, bus7, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P7);	
		inputs.addConnector(P7a, stdVS);
		inputs.addConnector(P7b, stdVS);
		inputs.addConnector(P7c, stdVS);
		
		double Q7 = 0.0;
		ModelConnector Q7a = new SinglePhaseReactivePower(network, bus7, FourWireNetwork.phaseA, FourWireNetwork.phaseN,Q7);
		ModelConnector Q7b = new SinglePhaseReactivePower(network, bus7, FourWireNetwork.phaseB, FourWireNetwork.phaseN,Q7);
		ModelConnector Q7c = new SinglePhaseReactivePower(network, bus7, FourWireNetwork.phaseC, FourWireNetwork.phaseN,Q7);
		inputs.addConnector(Q7a,stdVS);
		inputs.addConnector(Q7b,stdVS);
		inputs.addConnector(Q7c,stdVS);
		
//		inputs.addConnector(new PhaseToEarthVoltage(network,bus7,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus7), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus7), stdVI);

		
		///////////////
		// 
		//  Node 8
		//		
		double P8 = 500.0;
		ModelConnector P8a = new SinglePhaseActivePower(network, bus8, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P8);
		ModelConnector P8b = new SinglePhaseActivePower(network, bus8, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P8);
		ModelConnector P8c = new SinglePhaseActivePower(network, bus8, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P8);	
		inputs.addConnector(P8a, stdVS);
		inputs.addConnector(P8b, stdVS);
		inputs.addConnector(P8c, stdVS);
		
		double Q8 = 0.0;
		ModelConnector Q8a = new SinglePhaseReactivePower(network, bus8, FourWireNetwork.phaseA, FourWireNetwork.phaseN,Q8);
		ModelConnector Q8b = new SinglePhaseReactivePower(network, bus8, FourWireNetwork.phaseB, FourWireNetwork.phaseN,Q8);
		ModelConnector Q8c = new SinglePhaseReactivePower(network, bus8, FourWireNetwork.phaseC, FourWireNetwork.phaseN,Q8);
		inputs.addConnector(Q8a,stdVS);
		inputs.addConnector(Q8b,stdVS);
		inputs.addConnector(Q8c,stdVS);
		
//		inputs.addConnector(new PhaseToEarthVoltage(network,bus8,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus8), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus8), stdVI);

		
		///////////////
		// 
		//  Node 9
		//		
		double P9 = -500.0;
		ModelConnector P9a = new SinglePhaseActivePower(network, bus9, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P9);
		ModelConnector P9b = new SinglePhaseActivePower(network, bus9, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P9);
		ModelConnector P9c = new SinglePhaseActivePower(network, bus9, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P9);	
		inputs.addConnector(P9a, stdVS);
		inputs.addConnector(P9b, stdVS);
		inputs.addConnector(P9c, stdVS);
		
		double Q9 = 0.0;
		ModelConnector Q9a = new SinglePhaseReactivePower(network, bus9, FourWireNetwork.phaseA, FourWireNetwork.phaseN,Q9);
		ModelConnector Q9b = new SinglePhaseReactivePower(network, bus9, FourWireNetwork.phaseB, FourWireNetwork.phaseN,Q9);
		ModelConnector Q9c = new SinglePhaseReactivePower(network, bus9, FourWireNetwork.phaseC, FourWireNetwork.phaseN,Q9);
		inputs.addConnector(Q9a,stdVS);
		inputs.addConnector(Q9b,stdVS);
		inputs.addConnector(Q9c,stdVS);
		
//		inputs.addConnector(new PhaseToEarthVoltage(network,bus9,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new LambdaConnectionRealPart(network, bus9), stdVI);
		inputs.addConnector(new LambdaConnectionImagPart(network, bus9), stdVI);

		
		
		
		
		
		
		//////////////////
		//
		// Generate snapshots
		//
		int timeSteps = 10;
		double noiseLevelP = 1000.;
		double noiseLevelQ = 500.0;

		double epsilon = 0.0001; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 20; // maximum number of iterations

		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();
		
		EstimationResultSet[] resultSets = new EstimationResultSet[timeSteps];
		double tempP2, tempP3, tempP4, tempP5, tempP6, tempP7, tempP8, tempP9, tempQ2, tempQ3, tempQ4, tempQ5, tempQ6, tempQ7, tempQ8, tempQ9 = 0.0;
		double phaseInsymmetry = 0.0;//5;//1.5;//2.5;//1;
		
		for (int i=0;i<timeSteps;i++){
			tempP2 = P2 + noiseLevelP*Math.random();
			tempP3 = P3 + noiseLevelP*Math.random();
			tempP4 = P4 + noiseLevelP*Math.random();
			tempP5 = P5 + noiseLevelP*Math.random();
			tempP6 = P6 + noiseLevelP*Math.random();
			tempP7 = P7 + noiseLevelP*Math.random();
			tempP8 = P8 + noiseLevelP*Math.random();
			tempP9 = P9 + noiseLevelP*Math.random();
			
			tempQ2 = Q2 + noiseLevelQ*Math.random();
			tempQ3 = Q3 + noiseLevelQ*Math.random();
			tempQ4 = Q4 + noiseLevelQ*Math.random();
			tempQ5 = Q5 + noiseLevelQ*Math.random();
			tempQ6 = Q6 + noiseLevelQ*Math.random();
			tempQ7 = Q7 + noiseLevelQ*Math.random();
			tempQ8 = Q8 + noiseLevelQ*Math.random();
			tempQ9 = Q9 + noiseLevelQ*Math.random();
			
			P2a.setMyTrueValue(tempP2*(1+phaseInsymmetry*Math.random()));
			P2b.setMyTrueValue(tempP2*(1+phaseInsymmetry*Math.random()));
			P2c.setMyTrueValue(tempP2*(1+phaseInsymmetry*Math.random()));
			P3a.setMyTrueValue(tempP3*(1+phaseInsymmetry*Math.random()));
			P3b.setMyTrueValue(tempP3*(1+phaseInsymmetry*Math.random()));
			P3c.setMyTrueValue(tempP3*(1+phaseInsymmetry*Math.random()));
			P4a.setMyTrueValue(tempP4*(1+phaseInsymmetry*Math.random()));
			P4b.setMyTrueValue(tempP4*(1+phaseInsymmetry*Math.random()));
			P4c.setMyTrueValue(tempP4*(1+phaseInsymmetry*Math.random()));
			P5a.setMyTrueValue(tempP5*(1+phaseInsymmetry*Math.random()));
			P5b.setMyTrueValue(tempP5*(1+phaseInsymmetry*Math.random()));
			P5c.setMyTrueValue(tempP5*(1+phaseInsymmetry*Math.random()));
			P6a.setMyTrueValue(tempP6*(1+phaseInsymmetry*Math.random()));
			P6b.setMyTrueValue(tempP6*(1+phaseInsymmetry*Math.random()));
			P6c.setMyTrueValue(tempP6*(1+phaseInsymmetry*Math.random()));
			P7a.setMyTrueValue(tempP7*(1+phaseInsymmetry*Math.random()));
			P7b.setMyTrueValue(tempP7*(1+phaseInsymmetry*Math.random()));
			P7c.setMyTrueValue(tempP7*(1+phaseInsymmetry*Math.random()));
			P8a.setMyTrueValue(tempP8*(1+phaseInsymmetry*Math.random()));
			P8b.setMyTrueValue(tempP8*(1+phaseInsymmetry*Math.random()));
			P8c.setMyTrueValue(tempP8*(1+phaseInsymmetry*Math.random()));
			P9a.setMyTrueValue(tempP9*(1+phaseInsymmetry*Math.random()));
			P9b.setMyTrueValue(tempP9*(1+phaseInsymmetry*Math.random()));
			P9c.setMyTrueValue(tempP9*(1+phaseInsymmetry*Math.random()));
			
			Q2a.setMyTrueValue(tempQ2*(1+phaseInsymmetry*Math.random()));
			Q2b.setMyTrueValue(tempQ2*(1+phaseInsymmetry*Math.random()));
			Q2c.setMyTrueValue(tempQ2*(1+phaseInsymmetry*Math.random()));
			Q3a.setMyTrueValue(tempQ3*(1+phaseInsymmetry*Math.random()));
			Q3b.setMyTrueValue(tempQ3*(1+phaseInsymmetry*Math.random()));
			Q3c.setMyTrueValue(tempQ3*(1+phaseInsymmetry*Math.random()));
			Q4a.setMyTrueValue(tempQ4*(1+phaseInsymmetry*Math.random()));
			Q4b.setMyTrueValue(tempQ4*(1+phaseInsymmetry*Math.random()));
			Q4c.setMyTrueValue(tempQ4*(1+phaseInsymmetry*Math.random()));
			Q5a.setMyTrueValue(tempQ5*(1+phaseInsymmetry*Math.random()));
			Q5b.setMyTrueValue(tempQ5*(1+phaseInsymmetry*Math.random()));
			Q5c.setMyTrueValue(tempQ5*(1+phaseInsymmetry*Math.random()));
			Q6a.setMyTrueValue(tempQ6*(1+phaseInsymmetry*Math.random()));
			Q6b.setMyTrueValue(tempQ6*(1+phaseInsymmetry*Math.random()));
			Q6c.setMyTrueValue(tempQ6*(1+phaseInsymmetry*Math.random()));
			Q7a.setMyTrueValue(tempQ7*(1+phaseInsymmetry*Math.random()));
			Q7b.setMyTrueValue(tempQ7*(1+phaseInsymmetry*Math.random()));
			Q7c.setMyTrueValue(tempQ7*(1+phaseInsymmetry*Math.random()));
			Q8a.setMyTrueValue(tempQ8*(1+phaseInsymmetry*Math.random()));
			Q8b.setMyTrueValue(tempQ8*(1+phaseInsymmetry*Math.random()));
			Q8c.setMyTrueValue(tempQ8*(1+phaseInsymmetry*Math.random()));
			Q9a.setMyTrueValue(tempQ9*(1+phaseInsymmetry*Math.random()));
			Q9b.setMyTrueValue(tempQ9*(1+phaseInsymmetry*Math.random()));
			Q9c.setMyTrueValue(tempQ9*(1+phaseInsymmetry*Math.random()));
			
			resultSets[i] = estimator.estimateState(network, inputs, epsilon, maxIterations);	
			System.out.println(resultSets[i].usedIterrations);
		}
		
		Inputs modelEstimationInputs = new Inputs();
		
		boolean Vi = true;
		boolean Vij = true;
		boolean di = false;
		boolean Ii = true;
		boolean Pi = true;
		boolean Qi = true;
		
		boolean Pij = false;
		boolean Pji = false;
		boolean Qij = false;
		boolean Qji = false;
		boolean Iij = true;
		boolean Iji = true;
		
		boolean[] nodes = new boolean[9];
		nodes[0] = true;
		nodes[1] = true;
		nodes[2] = true;
		nodes[3] = true;
		nodes[4] = true;
		nodes[5] = true;
		nodes[6] = true;
		nodes[7] = true;
		nodes[8] = true;
		
		boolean[] branches = new boolean[8];
		branches[0] = true;
		branches[1] = true;
		branches[2] = true;
		branches[3] = true;
		branches[4] = true;
		branches[5] = true;
		branches[6] = true;
		branches[7] = true;
				
		for (int i=0;i<buses.length;i++){
			if (nodes[i]){
				if ((di)){
					modelEstimationInputs.addConnector(new PMU(network, buses[i], FourWireNetwork.phaseA,0.0), stdVd);
					modelEstimationInputs.addConnector(new PMU(network, buses[i], FourWireNetwork.phaseB,120.0), stdVd);
					modelEstimationInputs.addConnector(new PMU(network, buses[i], FourWireNetwork.phaseC,-120.0), stdVd);
				}
				if (Vi){
					modelEstimationInputs.addConnector(new PhaseToPhaseVoltage(network,buses[i],FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
					modelEstimationInputs.addConnector(new PhaseToPhaseVoltage(network,buses[i],FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
					modelEstimationInputs.addConnector(new PhaseToPhaseVoltage(network,buses[i],FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);					
				}
				if (Vij){
					modelEstimationInputs.addConnector(new PhaseToPhaseVoltage(network,buses[i],FourWireNetwork.phaseA,FourWireNetwork.phaseB,400), stdVV);
					modelEstimationInputs.addConnector(new PhaseToPhaseVoltage(network,buses[i],FourWireNetwork.phaseB,FourWireNetwork.phaseC,400), stdVV);
					modelEstimationInputs.addConnector(new PhaseToPhaseVoltage(network,buses[i],FourWireNetwork.phaseC,FourWireNetwork.phaseA,400), stdVV);					
				}
				if ((Pi)){
					modelEstimationInputs.addConnector(new SinglePhaseActivePower(network, buses[i], FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseActivePower(network, buses[i], FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseActivePower(network, buses[i], FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);					
				}
				if ((Qi)){
					modelEstimationInputs.addConnector(new SinglePhaseReactivePower(network, buses[i], FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseReactivePower(network, buses[i], FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseReactivePower(network, buses[i], FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);				
				}
				if (Ii){
					modelEstimationInputs.addConnector(new AbsoluteNodalPhaseCurrent(network, buses[i], FourWireNetwork.phaseA, 0), stdVI);
					modelEstimationInputs.addConnector(new AbsoluteNodalPhaseCurrent(network, buses[i], FourWireNetwork.phaseB, 0), stdVI);
					modelEstimationInputs.addConnector(new AbsoluteNodalPhaseCurrent(network, buses[i], FourWireNetwork.phaseC, 0), stdVI);					
				}
//				modelEstimationInputs.addConnector(new PhaseToEarthVoltage(network,buses[i],FourWireNetwork.phaseN,0.0), stdVV);
//				modelEstimationInputs.addConnector(new AbsoluteNodalPhaseCurrent(network, buses[i], FourWireNetwork.phaseN, 0), stdVI);					
				modelEstimationInputs.addConnector(new LambdaConnectionRealPart(network, buses[i]), 3*stdVI);
				modelEstimationInputs.addConnector(new LambdaConnectionImagPart(network, buses[i]), 3*stdVI);
			}
		}
		
		for (int i=0;i<lines.length;i++){
			if (branches[i]){
				if (Pij){
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
				}
				if (Pji){
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
				}
				if (Qij){
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
				}
				if (Qji){
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
					modelEstimationInputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
				}
				if (Iij){
					modelEstimationInputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseA, 0.0),stdVS);
					modelEstimationInputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseB, 0.0),stdVS);
					modelEstimationInputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, lines[i], network.getSource(lines[i]), FourWireNetwork.phaseC, 0.0),stdVS);
				}
				if (Iji){
					modelEstimationInputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseA, 0.0),stdVS);
					modelEstimationInputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseB, 0.0),stdVS);
					modelEstimationInputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, lines[i], network.getDest(lines[i]), FourWireNetwork.phaseC, 0.0),stdVS);
				}
			}
		}
		
		
		
		double[][] timeSeries = new double[modelEstimationInputs.numberOfConnectors()][timeSteps];
		for (int i=0;i<timeSteps;i++){
			for (int j=0;j<modelEstimationInputs.numberOfConnectors();j++){
				timeSeries[j][i] = modelEstimationInputs.getConnector(j).myTheroreticalValue(resultSets[i].voltages);
			}
		}
		
		//ModelParameterEstimatorSecondTry modelEstimator = new ModelParameterEstimatorSecondTry();
		ModelParameterEstimator_extreme modelEstimator = new ModelParameterEstimator_extreme();

		TuneableEightTerminalElement[] tuneables = new TuneableEightTerminalElement[8];
		tuneables[0] = line1;
		tuneables[1] = line2;
		tuneables[2] = line3;
		tuneables[3] = line4;
		tuneables[4] = line5;
		tuneables[5] = line6;
		tuneables[6] = line7;
		tuneables[7] = line8;
		
		double startLength = 400;
		
		line1.lengthA = startLength;
		line1.lengthB = startLength;
		line1.lengthC = startLength;
		line1.lengthN = startLength;

		line2.lengthA = startLength;
		line2.lengthB = startLength;
		line2.lengthC = startLength;
		line2.lengthN = startLength;

		line3.lengthA = startLength;
		line3.lengthB = startLength;
		line3.lengthC = startLength;
		line3.lengthN = startLength;

		line4.lengthA = startLength;
		line4.lengthB = startLength;
		line4.lengthC = startLength;
		line4.lengthN = startLength;

		line5.lengthA = startLength;
		line5.lengthB = startLength;
		line5.lengthC = startLength;
		line5.lengthN = startLength;

		line6.lengthA = startLength;
		line6.lengthB = startLength;
		line6.lengthC = startLength;
		line6.lengthN = startLength;

		line7.lengthA = startLength;
		line7.lengthB = startLength;
		line7.lengthC = startLength;
		line7.lengthN = startLength;

		line8.lengthA = startLength;
		line8.lengthB = startLength;
		line8.lengthC = startLength;
		line8.lengthN = startLength;

		

		
		modelEstimator.tuneModel(network, tuneables, modelEstimationInputs, timeSeries, 20);
	
//		System.out.println("New length line 1: " + line1.length);
//		System.out.println("New length line 2: " + line2.length);
//		System.out.println("New length line 3: " + line3.length);
//		System.out.println("New length line 4: " + line4.length);
//		System.out.println("New length line 5: " + line5.length);
//		System.out.println("New length line 6: " + line6.length);
//		System.out.println("New length line 7: " + line7.length);
//		System.out.println("New length line 8: " + line8.length);
		
		System.out.println("New length line 1, A: " + line1.lengthA + ", B: " + line1.lengthB + ", C:" + line1.lengthC + ", N:" + line1.lengthN);
		System.out.println("New length line 2, A: " + line2.lengthA + ", B: " + line2.lengthB + ", C:" + line2.lengthC + ", N:" + line2.lengthN);
		System.out.println("New length line 3, A: " + line3.lengthA + ", B: " + line3.lengthB + ", C:" + line3.lengthC + ", N:" + line3.lengthN);
		System.out.println("New length line 4, A: " + line4.lengthA + ", B: " + line4.lengthB + ", C:" + line4.lengthC + ", N:" + line4.lengthN);
		System.out.println("New length line 5, A: " + line5.lengthA + ", B: " + line5.lengthB + ", C:" + line5.lengthC + ", N:" + line5.lengthN);
		System.out.println("New length line 6, A: " + line6.lengthA + ", B: " + line6.lengthB + ", C:" + line6.lengthC + ", N:" + line6.lengthN);
		System.out.println("New length line 7, A: " + line7.lengthA + ", B: " + line7.lengthB + ", C:" + line7.lengthC + ", N:" + line7.lengthN);
		System.out.println("New length line 8, A: " + line8.lengthA + ", B: " + line8.lengthB + ", C:" + line8.lengthC + ", N:" + line8.lengthN);
				
	}

}
