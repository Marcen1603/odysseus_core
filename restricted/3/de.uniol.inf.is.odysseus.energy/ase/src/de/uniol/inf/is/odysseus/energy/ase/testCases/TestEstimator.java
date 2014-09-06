package de.uniol.inf.is.odysseus.energy.ase.testCases;

import com.smartdist.modelconnecting.eightterminalelementconnectors.AbsoluteEightTerminalElementPhaseCurrent;
import com.smartdist.modelconnecting.nodeconnectors.AbsoluteNodalPhaseCurrent;
import com.smartdist.modelconnecting.nodeconnectors.PMU;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToEarthVoltage;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToPhaseVoltage;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseReactivePower;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.eightterminalelements.Line;
import com.smartdist.modelling.network.fourterminalelements.Earthing;
import com.smartdist.solvers.stateestimator.AdaptiveStateEstimator;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.solvers.stateestimator.Inputs;
import com.smartdist.solvers.stateestimator.LinearStateEstimator;
import com.smartdist.solvers.stateestimator.Observers;
import com.smartdist.util.Complex;
import com.smartdist.util.DevelopmentHelper;

public class TestEstimator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		FourWireNetwork network = new FourWireNetwork(50);
		
		Node busbar1 = new Node(230);
		Node busbar2 = new Node(230);
		busbar1.myFourTerminalElements.add(new Earthing(new Complex(0.01,0.01)));
		
		network.addVertex(busbar1);
		network.addVertex(busbar2);
		
		Line line1 = new Line();
		
		network.addEdge(line1, busbar1, busbar2);
		
		busbar1.myFourTerminalElements.add(new Earthing(new Complex(0.001,0)));
		
		Inputs modelInputs = new Inputs();
		
		modelInputs.addConnector(new PhaseToEarthVoltage(network, busbar1, FourWireNetwork.phaseA, 230), 1);
		modelInputs.addConnector(new PhaseToEarthVoltage(network, busbar1, FourWireNetwork.phaseB, 230), 1);
		modelInputs.addConnector(new PhaseToEarthVoltage(network, busbar1, FourWireNetwork.phaseC, 230), 1);
		modelInputs.addConnector(new PhaseToEarthVoltage(network, busbar1, FourWireNetwork.phaseN, 0), 1);
		
		modelInputs.addConnector(new PMU(network, busbar1, FourWireNetwork.phaseA, 0), 0.01);
		modelInputs.addConnector(new PMU(network, busbar1, FourWireNetwork.phaseB, 120), 0.01);
		modelInputs.addConnector(new PMU(network, busbar1, FourWireNetwork.phaseC, -120), 0.01);
		
		
		
		modelInputs.addConnector(new SinglePhaseActivePower(network, busbar2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 100000), 10);
		modelInputs.addConnector(new SinglePhaseActivePower(network, busbar2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 50000), 10);
		modelInputs.addConnector(new SinglePhaseActivePower(network, busbar2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 1000), 10);
		
		modelInputs.addConnector(new SinglePhaseReactivePower(network, busbar2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 0), 10);
		modelInputs.addConnector(new SinglePhaseReactivePower(network, busbar2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 0), 10);
		modelInputs.addConnector(new SinglePhaseReactivePower(network, busbar2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 0), 10);
		
		
		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();
		
		// ...and get our results
		double epsilon = 0.0001; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 50; // maximum number of iterations

		EstimationResultSet result = estimator.estimateState(network, modelInputs, epsilon, maxIterations);
		
		// Et voil !!! :-)
		DevelopmentHelper.printEstimationResultToConsole(network, result);
		
		Observers outputs = new Observers();
		outputs.addConnector(new PhaseToPhaseVoltage(network, busbar2, FourWireNetwork.phaseA, FourWireNetwork.phaseB, 0));
		outputs.addConnector(new AbsoluteEightTerminalElementPhaseCurrent(network, line1, busbar1, FourWireNetwork.phaseA,0));
		outputs.addConnector(new AbsoluteNodalPhaseCurrent(network, busbar1, FourWireNetwork.phaseA,0));
		outputs.addConnector(new PhaseToPhaseVoltage(network, busbar2, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0));
		outputs.addConnector(new PhaseToEarthVoltage(network, busbar2, FourWireNetwork.phaseA,0));
		
		outputs.calculateModelCoverages(result);
		
		System.out.println("V2ab: " + outputs.getConnectors().elementAt(0).myTheroreticalValue(result.voltages) + "  coverage: " + outputs.getModelCoverage().elementAt(0));
		System.out.println("I12a: " + outputs.getConnectors().elementAt(1).myTheroreticalValue(result.voltages) + "  coverage: " + outputs.getModelCoverage().elementAt(1));
		System.out.println("I1a: " + outputs.getConnectors().elementAt(2).myTheroreticalValue(result.voltages) + "  coverage: " + outputs.getModelCoverage().elementAt(2));
		System.out.println("V2an: " + outputs.getConnectors().elementAt(3).myTheroreticalValue(result.voltages) + "  coverage: " + outputs.getModelCoverage().elementAt(3));
		System.out.println("V2ag: " + outputs.getConnectors().elementAt(4).myTheroreticalValue(result.voltages) + "  coverage: " + outputs.getModelCoverage().elementAt(4));
		System.out.println();
		System.out.println("Number of Iterations: " + result.usedIterrations);
		
		
		
		/////////////////////////////
		// Linear Estimator
		/////////////////////////////
		
		// Instanciating and initializing the Estimator
		LinearStateEstimator linEstimator = new LinearStateEstimator(modelInputs, outputs, result);
		
		// To make things even faster, I chose to go back to primitive datatypes... 
		double[] linEstInputs = new double[modelInputs.numberOfConnectors()];
		double[] linEstOutputs = new double[outputs.numberOfConnectors()];
		// and have to copy them here.
		for (int i=0;i<linEstInputs.length;i++){
			linEstInputs[i] = modelInputs.getConnector(i).myTrueValue();
		}
		// !!!! this is where it happens:
		linEstOutputs = linEstimator.estimate(linEstInputs);
		
		// this is just an output
		DevelopmentHelper.printToConsole(linEstOutputs);
		

	
	}


}
