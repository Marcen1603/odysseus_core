package de.uniol.inf.is.odysseus.energy.ase.testCases;

import com.smartdist.modelconnecting.nodeconnectors.AbsoluteNodalPhaseCurrent;
import com.smartdist.modelconnecting.nodeconnectors.PMU;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToEarthVoltage;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToPhaseVoltage;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.eightterminalelements.Line;
import com.smartdist.solvers.stateestimator.AdaptiveStateEstimator;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.solvers.stateestimator.Inputs;
import com.smartdist.util.DevelopmentHelper;

public class TestSingularValueEstimation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		FourWireNetwork network = new FourWireNetwork(50);	
		Node bus1 = new Node(230);
		Node bus2 = new Node(230);		
		Line line1 = new Line();
		network.addVertex(bus1);
		network.addVertex(bus2);
		network.addEdge(line1, bus1, bus2);
		
		Inputs inputs = new Inputs();
		
		double stdVS = 10;
		double stdVV = 1;
		double stdVI = 0.1;
		double stdVd = 0.01;
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseA,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseB,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToPhaseVoltage(network,bus1,FourWireNetwork.phaseC,FourWireNetwork.phaseN,230), stdVV);
		inputs.addConnector(new PhaseToEarthVoltage(network,bus1,FourWireNetwork.phaseN,0.0), stdVV);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseA,0.0), stdVd);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseB,120.0), stdVd);
		inputs.addConnector(new PMU(network, bus1, FourWireNetwork.phaseC,-120.0), stdVd);
		inputs.addConnector(new AbsoluteNodalPhaseCurrent(network, bus1, FourWireNetwork.phaseN,0.0), stdVI);
		
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new SinglePhaseActivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 5000.0), stdVS);
		inputs.addConnector(new PhaseToEarthVoltage(network,bus2,FourWireNetwork.phaseN,0.0), stdVV);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseA, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseB, FourWireNetwork.phaseN,0.0),stdVS);
		//inputs.addConnector(new SinglePhaseReactivePower(network, bus2, FourWireNetwork.phaseC, FourWireNetwork.phaseN,0.0),stdVS);
		inputs.addConnector(new AbsoluteNodalPhaseCurrent(network, bus2, FourWireNetwork.phaseN,0.0), stdVI);

		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();
		
		// ...and get our results
		double epsilon = 0.0001; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 50; // maximum number of iterations

		EstimationResultSet result = estimator.estimateState(network, inputs, epsilon, maxIterations);
		
		System.out.println("Singular Values: ");
		for (int i=0;i<result.singularValues.length;i++){
			System.out.println(result.singularValues[i] + " \t ");
		}
		System.out.println();
		
/*		System.out.println("Length of Jacobian column vectors:");
		for (int i=0;i<result.jacobian[0].length;i++){
			double temp = 0;
			for (int j=0;j<result.jacobian.length;j++){
				temp += result.jacobian[j][i]*result.jacobian[j][i];
			}
			System.out.println(java.lang.Math.sqrt(temp));
		}
*/		
		// Et voil !!! :-)
		DevelopmentHelper.printEstimationResultToConsole(network, result);

		
		
	}

}
