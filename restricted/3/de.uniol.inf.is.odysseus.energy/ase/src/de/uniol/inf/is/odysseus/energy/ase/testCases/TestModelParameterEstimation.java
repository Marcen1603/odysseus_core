package de.uniol.inf.is.odysseus.energy.ase.testCases;

import com.smartdist.modelconnecting.nodeconnectors.PMU;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToEarthVoltage;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToPhaseVoltage;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseReactivePower;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.TuneableEightTerminalElement;
import com.smartdist.modelling.network.eightterminalelements.LengthTuneableLine;
import com.smartdist.modelling.network.fourterminalelements.Earthing;
import com.smartdist.solvers.stateestimator.AdaptiveStateEstimator;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.solvers.stateestimator.Inputs;
import com.smartdist.solvers.stateestimator.ModelParameterEstimator;
import com.smartdist.util.Complex;
import com.smartdist.util.DevelopmentHelper;

public class TestModelParameterEstimation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FourWireNetwork network = new FourWireNetwork(50);	
		Node bus1 = new Node(230);
		Node bus2 = new Node(230);
		Node bus3 = new Node(230);
		Node bus4 = new Node(230);
		LengthTuneableLine line1 = new LengthTuneableLine();
		LengthTuneableLine line2 = new LengthTuneableLine();
		LengthTuneableLine line3 = new LengthTuneableLine();
		network.addVertex(bus1);
		network.addVertex(bus2);
		network.addVertex(bus3);
		network.addVertex(bus4);
		//bus4 = bus1;
		network.addEdge(line1, bus1, bus2);
		network.addEdge(line2, bus2, bus3);
		network.addEdge(line3, bus3, bus4);
		line1.length=1000;
		line2.length=1200;
		line3.length=800;
		bus1.myFourTerminalElements.add(new Earthing(new Complex(1000,0)));
		
		Inputs inputs = new Inputs();
		
		double stdVS = 100;
		double stdVV = 2;
		//double stdVI = 10;
		double stdVd = 0.05;
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToEarthVoltage(network,bus1,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseA,0.0), stdVd);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseB,120.0), stdVd);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseC,-120.0), stdVd);
		//inputs.addConnector(new NodalPhaseCurrentSquared(network, bus1, FourWireNetwork.phaseN,0.0), stdVI);
		
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 5500.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new PhaseToEarthVoltage(network,bus2,FourWireNetwork.phaseN,0.0), stdVV);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new NodalPhaseCurrentSquared(network, bus2, FourWireNetwork.phaseN,0.0), stdVI);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);				
		
		inputs.addConnector(new SinglePhaseActivePower(network, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new PhaseToEarthVoltage(network,bus3,FourWireNetwork.phaseN,0.0), stdVV);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new NodalPhaseCurrentSquared(network, bus3, FourWireNetwork.phaseN,0.0), stdVI);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);		

		inputs.addConnector(new SinglePhaseActivePower(network, bus4, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus4, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 500.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus4, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new PhaseToEarthVoltage(network,bus4,FourWireNetwork.phaseN,0.0), stdVV);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new NodalPhaseCurrentSquared(network, bus4, FourWireNetwork.phaseN,0.0), stdVI);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);		

		
		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();
		
		// ...and get our results
		double epsilon = 0.0001; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 50; // maximum number of iterations

		EstimationResultSet result = estimator.estimateState(network, inputs, epsilon, maxIterations);

		System.out.println("Results before mistuning:");
		DevelopmentHelper.printEstimationResultToConsole(network, result);
		
		
		
		///////////////////////
		// snapshot results

		inputs.addConnector(new SinglePhaseActivePower(network, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus1, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus1, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0), stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus1, FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0), stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus1, FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0), stdVS);		
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseA,FourWireNetwork.phaseB,400), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseB,FourWireNetwork.phaseC,400), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus2,FourWireNetwork.phaseC,FourWireNetwork.phaseA,400), stdVV);

		inputs.addConnector(new PMU(network, bus2, FourWireNetwork.phaseA,0.0), stdVd);
		inputs.addConnector(new PMU(network, bus2, FourWireNetwork.phaseB,120.0), stdVd);
		inputs.addConnector(new PMU(network, bus2, FourWireNetwork.phaseC,-120.0), stdVd);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus1, FourWireNetwork.phaseA, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus1, FourWireNetwork.phaseB, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus1, FourWireNetwork.phaseC, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus2, FourWireNetwork.phaseA, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus2, FourWireNetwork.phaseB, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus2, FourWireNetwork.phaseC, 0), stdVI);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);

		inputs.addConnector(new PMU(network, bus3, FourWireNetwork.phaseA,0.0), stdVd);
		inputs.addConnector(new PMU(network, bus3, FourWireNetwork.phaseB,120.0), stdVd);
		inputs.addConnector(new PMU(network, bus3, FourWireNetwork.phaseC,-120.0), stdVd);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus3, FourWireNetwork.phaseA, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus3, FourWireNetwork.phaseB, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus3, FourWireNetwork.phaseC, 0), stdVI);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseA,FourWireNetwork.phaseB,400), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseB,FourWireNetwork.phaseC,400), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus3,FourWireNetwork.phaseC,FourWireNetwork.phaseA,400), stdVV);

		inputs.addConnector(new PMU(network, bus4, FourWireNetwork.phaseA,0.0), stdVd);
		inputs.addConnector(new PMU(network, bus4, FourWireNetwork.phaseB,120.0), stdVd);
		inputs.addConnector(new PMU(network, bus4, FourWireNetwork.phaseC,-120.0), stdVd);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus4, FourWireNetwork.phaseA, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus4, FourWireNetwork.phaseB, 0), stdVI);
//		inputs.addConnector(new NodalPhaseCurrentSquared(network, bus4, FourWireNetwork.phaseC, 0), stdVI);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
		inputs.addConnector(new SinglePhaseReactivePower(network, bus4, FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseA,FourWireNetwork.phaseB,400), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseB,FourWireNetwork.phaseC,400), stdVV);
//		inputs.addConnector(new PhaseToPhaseVoltage(network,bus4,FourWireNetwork.phaseC,FourWireNetwork.phaseA,400), stdVV);
//		
		
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus1, FourWireNetwork.phaseA, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus1, FourWireNetwork.phaseB, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus1, FourWireNetwork.phaseC, 0.0),stdVI);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line1, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line1, bus1, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line1, bus1, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line1, bus1, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line1, bus1, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line1, bus1, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus2, FourWireNetwork.phaseA, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus2, FourWireNetwork.phaseB, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line1, bus2, FourWireNetwork.phaseC, 0.0),stdVI);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line1, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line1, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line1, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line1, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line1, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line1, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);

		
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line2, bus2, FourWireNetwork.phaseA, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line2, bus2, FourWireNetwork.phaseB, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line2, bus2, FourWireNetwork.phaseC, 0.0),stdVI);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line2, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line2, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line2, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line2, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line2, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line2, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line2, bus3, FourWireNetwork.phaseA, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line2, bus3, FourWireNetwork.phaseB, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line2, bus3, FourWireNetwork.phaseC, 0.0),stdVI);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line2, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line2, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line2, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line2, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line2, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line2, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);

		
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line3, bus3, FourWireNetwork.phaseA, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line3, bus3, FourWireNetwork.phaseB, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line3, bus3, FourWireNetwork.phaseC, 0.0),stdVI);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line3, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line3, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line3, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line3, bus3, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line3, bus3, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line3, bus3, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line3, bus4, FourWireNetwork.phaseA, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line3, bus4, FourWireNetwork.phaseB, 0.0),stdVI);
//		inputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line3, bus4, FourWireNetwork.phaseC, 0.0),stdVI);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line3, bus4, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line3, bus4, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementActivePower(network, line3, bus4, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line3, bus4, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line3, bus4, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0.0),stdVS);
//		inputs.addConnector(new SinglePhaseEightTerminalElementReactivePower(network, line3, bus4, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0.0),stdVS);
		
		

		
		
		double[] correctValues = new double[inputs.numberOfConnectors()];
		
		for (int i=0;i<inputs.numberOfConnectors();i++){
			correctValues[i] = inputs.getConnector(i).myTheroreticalValue(result.voltages);
			inputs.getConnector(i).setMyTrueValue(correctValues[i]);
		}
		
		// Mistune line 1:
		line1.length = 1000;//1100;//1000;//1100;
		line2.length = 1000;//1100;//1000;//700;
		line3.length = 1000;//1100;//1000;//500;
		
		for (int k=0;k<1000000;k++){
			result.network.generateAdmittanceMatrices();
			result = estimator.estimateState(network, inputs, epsilon, maxIterations);
	
			//System.out.println("Results after tuning:");
			//DevelopmentHelper.printEstimationResultToConsole(network, result);
			
			////////////////
			// calculate deviation
			
			double[] meanValues = new double[inputs.numberOfConnectors()];
			for (int i=0;i<inputs.numberOfConnectors();i++){
				meanValues[i] = inputs.getConnector(i).myTheroreticalValue(result.voltages)-inputs.getConnector(i).myTrueValue();
				//meanValues[i] = inputs.getConnector(i).myTheroreticalValue(result.voltages)-correctValues[i];
			}
			
			
			ModelParameterEstimator parameterEstimator = new ModelParameterEstimator();
			TuneableEightTerminalElement[] tuneables= new TuneableEightTerminalElement[3];
			tuneables[0] = line1;
			tuneables[1] = line2;
			tuneables[2] = line3;
			EstimationResultSet expansionPoint = result;
			parameterEstimator.tuneModel(network, tuneables, expansionPoint, inputs, meanValues);
			
			double meanMeans = 0;
			for (int i=0;i<meanValues.length;i++) meanMeans += meanValues[i]*meanValues[i];
			System.out.println("mean means values: " + Math.sqrt(meanMeans));
			
			System.out.println("new length line 1: " + line1.length);
			System.out.println("new length line 2: " + line2.length);
			System.out.println("new length line 3: " + line3.length);
		}
				
	}

}
