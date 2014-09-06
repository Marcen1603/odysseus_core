package de.uniol.inf.is.odysseus.energy.ase.testCases;

import java.text.DecimalFormat;

import com.smartdist.modelconnecting.nodeconnectors.AbsoluteNodalPhaseCurrent;
import com.smartdist.modelconnecting.nodeconnectors.PMU;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToEarthVoltage;
import com.smartdist.modelconnecting.nodeconnectors.PhaseToPhaseVoltage;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseActivePower;
import com.smartdist.modelconnecting.nodeconnectors.SinglePhaseReactivePower;
import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.Node;
import com.smartdist.modelling.network.eightterminalelements.Line;
import com.smartdist.solvers.stateestimator.AdaptiveStateEstimator;
import com.smartdist.solvers.stateestimator.EstimationResultSet;
import com.smartdist.solvers.stateestimator.Inputs;
import com.smartdist.util.Complex;
import com.smartdist.util.SingularValueDecomposer;

public class LinEstimationPerformanceTest {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {

		
		boolean debug = false;
		
		/*
		 * In this exampel we want to create a simple, radial network with 5 Busbars aka Nodes.
		 */

		// Let's first create the network itself. There is nothing special to be defined here, so it is simply:
		FourWireNetwork network = new FourWireNetwork(50);
		
			// What we need to define network-wide is the nominal frequency of operation. This will be used later,
			// when the State Estimator actually generates the network model. So, we store it in a double variable for now.
			double nominalFrequency = 50; // 50 Hz
			
			// Now we can create our 5 Nodes, representing our 5 Busbars.
			// What we do have to specify here is the nominal voltage at the particular Busbar. Let's put 230V for all our nodes.
			Node busbar1 = new Node(230);
			Node busbar2 = new Node(230);
			Node busbar3 = new Node(230);
			Node busbar4 = new Node(230);
			Node busbar5 = new Node(230);
			
			// The last thing missing to assemble our network is the lines. Since we want to have a radial network with no
			// parallel branches, we need 4 lines. The lines here will use the default parameters, coded in Class Line(), package
			// com.smartdist.modelling.network
			Line line1 = new Line();
			Line line2 = new Line();
			Line line3 = new Line();
			Line line4 = new Line();
			
			// Now that we have a network, nodes and lines, we can assemble them to a topology
			// We can assemble it in any sequence. But when adding a line, its start and end node have to be part of the network already.
			// Let's just put in all the nodes, called Vertices in the network.
			network.addVertex(busbar1);
			network.addVertex(busbar2);
			network.addVertex(busbar3);
			network.addVertex(busbar4);
			network.addVertex(busbar5);
			
			// Now that all our busbars are in the network, let's wire them together. In Graph theory, our lines will be called Edges
			network.addEdge(line1, busbar1, busbar2); // Simple, isn't it? ;-)
			network.addEdge(line2, busbar2, busbar3);
			network.addEdge(line3, busbar3, busbar4);
			network.addEdge(line4, busbar4, busbar5);
		
		
		/*
		 * First mission accomplished. We've got a network! :-)
		 * 
		 * So, let's hook up some measurements to it. 
		 * 
		 * To make things comparable to a single phase NR, we're going to create an exactly determined case, 
		 * with Busbar 1 as the Reference node.
		 * 
		 * Measurements are linked to the network model by ModelConnectors, which are kept in a ConnectorContainer to keep them in order
		 */
		
		// ...create the ConnectorContainer to later store the connectors in 
		Inputs inputs = new Inputs();
		
			// add voltage and phase measurement to our first busbar. We can put them either as measured towards N or ground.
			// Towards N is more challenging for the algo. So, let's try with that. But you may change it.
			// voltage of N hast to be towards ground.
			PhaseToPhaseVoltage V1a = new PhaseToPhaseVoltage(network, busbar1, FourWireNetwork.phaseA, FourWireNetwork.phaseN, 230);
			PhaseToPhaseVoltage V1b = new PhaseToPhaseVoltage(network, busbar1, FourWireNetwork.phaseB, FourWireNetwork.phaseN, 230);
			PhaseToPhaseVoltage V1c = new PhaseToPhaseVoltage(network, busbar1, FourWireNetwork.phaseC, FourWireNetwork.phaseN, 230);
/*			PhaseToEarthVoltage V1a = new PhaseToEarthVoltage(network, busbar1, FourWireNetwork.phaseA, 230);
			PhaseToEarthVoltage V1b = new PhaseToEarthVoltage(network, busbar1, FourWireNetwork.phaseB, 230);
			PhaseToEarthVoltage V1c = new PhaseToEarthVoltage(network, busbar1, FourWireNetwork.phaseC, 230);
*/			
			PhaseToEarthVoltage V1n = new PhaseToEarthVoltage(network, busbar1, FourWireNetwork.phaseN, 0);
			// give reference angles...
			PMU angle1a = new PMU(network, busbar1, FourWireNetwork.phaseA, 0);
			PMU angle1b = new PMU(network, busbar1, FourWireNetwork.phaseB, 120);
			PMU angle1c = new PMU(network, busbar1, FourWireNetwork.phaseC, -120);
			
			// the phase of a zero voltage doesn't contain too much information. Instead we use our knowledge about the
			// symmetry we're creating and say neutral current at Busbar 1 is zero
			AbsoluteNodalPhaseCurrent I1n = new AbsoluteNodalPhaseCurrent(network, busbar1, FourWireNetwork.phaseN, 0);
			
			//starting point
			
			double P2 = 0;
			double P3 = 0;
			double P4 = 0;
			double P5 = 0;
			
			double P2StepRange = 25000;
			double P3StepRange = 25000;
			double P4StepRange = 25000;
			double P5StepRange = 25000;
			
			double Q2 = 0;
			double Q3 = 0;
			double Q4 = 0;
			double Q5 = 0;
			
			double Q2StepRange = 0;
			double Q3StepRange = 0;
			double Q4StepRange = 0;
			double Q5StepRange = 0;
			
			// add active power measurement to all three phases of the last four busbars
			SinglePhaseActivePower P2a = new SinglePhaseActivePower(network, busbar2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P2); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseActivePower P2b = new SinglePhaseActivePower(network, busbar2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P2); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseActivePower P2c = new SinglePhaseActivePower(network, busbar2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P2); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
			
			SinglePhaseActivePower P3a = new SinglePhaseActivePower(network, busbar3, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P3); // Let's assume 10kW absorption, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseActivePower P3b = new SinglePhaseActivePower(network, busbar3, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P3); // Let's assume 10kW absorption, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseActivePower P3c = new SinglePhaseActivePower(network, busbar3, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P3); // Let's assume 10kW absorption, current measured in phase A and voltage measured against phase N, the Neutral
	
			SinglePhaseActivePower P4a = new SinglePhaseActivePower(network, busbar4, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P4); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseActivePower P4b = new SinglePhaseActivePower(network, busbar4, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P4); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseActivePower P4c = new SinglePhaseActivePower(network, busbar4, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P4); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
			
			SinglePhaseActivePower P5a = new SinglePhaseActivePower(network, busbar5, FourWireNetwork.phaseA, FourWireNetwork.phaseN, P5); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseActivePower P5b = new SinglePhaseActivePower(network, busbar5, FourWireNetwork.phaseB, FourWireNetwork.phaseN, P5); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseActivePower P5c = new SinglePhaseActivePower(network, busbar5, FourWireNetwork.phaseC, FourWireNetwork.phaseN, P5); // Let's assume 0W, current measured in phase A and voltage measured against phase N, the Neutral
	
			
			// add reactive power measurement to all three phases of the last four busbars
			SinglePhaseReactivePower Q2a = new SinglePhaseReactivePower(network, busbar2, FourWireNetwork.phaseA, FourWireNetwork.phaseN, Q2); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseReactivePower Q2b = new SinglePhaseReactivePower(network, busbar2, FourWireNetwork.phaseB, FourWireNetwork.phaseN, Q2); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseReactivePower Q2c = new SinglePhaseReactivePower(network, busbar2, FourWireNetwork.phaseC, FourWireNetwork.phaseN, Q2); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
			
			SinglePhaseReactivePower Q3a = new SinglePhaseReactivePower(network, busbar3, FourWireNetwork.phaseA, FourWireNetwork.phaseN, Q3); // Let's assume 5kVar injection, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseReactivePower Q3b = new SinglePhaseReactivePower(network, busbar3, FourWireNetwork.phaseB, FourWireNetwork.phaseN, Q3); // Let's assume 5kVar injection, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseReactivePower Q3c = new SinglePhaseReactivePower(network, busbar3, FourWireNetwork.phaseC, FourWireNetwork.phaseN, Q3); // Let's assume 5kVar injection, current measured in phase A and voltage measured against phase N, the Neutral
	
			SinglePhaseReactivePower Q4a = new SinglePhaseReactivePower(network, busbar4, FourWireNetwork.phaseA, FourWireNetwork.phaseN, Q4); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseReactivePower Q4b = new SinglePhaseReactivePower(network, busbar4, FourWireNetwork.phaseB, FourWireNetwork.phaseN, Q4); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseReactivePower Q4c = new SinglePhaseReactivePower(network, busbar4, FourWireNetwork.phaseC, FourWireNetwork.phaseN, Q4); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
			
			SinglePhaseReactivePower Q5a = new SinglePhaseReactivePower(network, busbar5, FourWireNetwork.phaseA, FourWireNetwork.phaseN, Q5); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseReactivePower Q5b = new SinglePhaseReactivePower(network, busbar5, FourWireNetwork.phaseB, FourWireNetwork.phaseN, Q5); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
			SinglePhaseReactivePower Q5c = new SinglePhaseReactivePower(network, busbar5, FourWireNetwork.phaseC, FourWireNetwork.phaseN, Q5); // Let's assume 0Var, current measured in phase A and voltage measured against phase N, the Neutral
	
			// With active and reactive power at all phases we have 6 real valued inputs. But we have to determine
			// four complex values, which is equal to eight reel ones. So, let's put some additional information we've got about our
			// Neutrals
			AbsoluteNodalPhaseCurrent I2n = new AbsoluteNodalPhaseCurrent(network, busbar2, FourWireNetwork.phaseN, 0);
			AbsoluteNodalPhaseCurrent I3n = new AbsoluteNodalPhaseCurrent(network, busbar3, FourWireNetwork.phaseN, 0);
			AbsoluteNodalPhaseCurrent I4n = new AbsoluteNodalPhaseCurrent(network, busbar4, FourWireNetwork.phaseN, 0);
			AbsoluteNodalPhaseCurrent I5n = new AbsoluteNodalPhaseCurrent(network, busbar5, FourWireNetwork.phaseN, 0);
			PhaseToEarthVoltage V2n = new PhaseToEarthVoltage(network, busbar2, FourWireNetwork.phaseN, 0);
			PhaseToEarthVoltage V3n = new PhaseToEarthVoltage(network, busbar3, FourWireNetwork.phaseN, 0);
			PhaseToEarthVoltage V4n = new PhaseToEarthVoltage(network, busbar4, FourWireNetwork.phaseN, 0);
			PhaseToEarthVoltage V5n = new PhaseToEarthVoltage(network, busbar5, FourWireNetwork.phaseN, 0);
			
		double sigmaV = 2.;
		double sigmaI = 1.;
		double sigmaS = 10.;
		double sigmaD = 0.1;
			
		// ...sorting everything into the ConnectorContainer...
		inputs.addConnector(V1a, sigmaV);
		inputs.addConnector(V1b, sigmaV);
		inputs.addConnector(V1c, sigmaV);
		inputs.addConnector(V1n, sigmaV);
		inputs.addConnector(angle1a, sigmaD);
		inputs.addConnector(angle1b, sigmaD);
		inputs.addConnector(angle1c, sigmaD);
		inputs.addConnector(I1n, sigmaI);
		inputs.addConnector(P2a, sigmaS);
		inputs.addConnector(P2b, sigmaS);
		inputs.addConnector(P2c, sigmaS);
		inputs.addConnector(P3a, sigmaS);
		inputs.addConnector(P3b, sigmaS);
		inputs.addConnector(P3c, sigmaS);
		inputs.addConnector(P4a, sigmaS);
		inputs.addConnector(P4b, sigmaS);
		inputs.addConnector(P4c, sigmaS);
		inputs.addConnector(P5a, sigmaS);
		inputs.addConnector(P5b, sigmaS);
		inputs.addConnector(P5c, sigmaS);
		inputs.addConnector(Q2a, sigmaS);
		inputs.addConnector(Q2b, sigmaS);
		inputs.addConnector(Q2c, sigmaS);
		inputs.addConnector(Q3a, sigmaS);
		inputs.addConnector(Q3b, sigmaS);
		inputs.addConnector(Q3c, sigmaS);
		inputs.addConnector(Q4a, sigmaS);
		inputs.addConnector(Q4b, sigmaS);
		inputs.addConnector(Q4c, sigmaS);
		inputs.addConnector(Q5a, sigmaS);
		inputs.addConnector(Q5b, sigmaS);
		inputs.addConnector(Q5c, sigmaS);
		inputs.addConnector(I2n, sigmaI);
		inputs.addConnector(I3n, sigmaI);
		inputs.addConnector(I4n, sigmaI);
		inputs.addConnector(I5n, sigmaI);
		inputs.addConnector(V2n, sigmaV);
		inputs.addConnector(V3n, sigmaV);
		inputs.addConnector(V4n, sigmaV);
		inputs.addConnector(V5n, sigmaV);

		// Now it's time to wake our estimator...
		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();

		// ...and get our results
		double epsilon = 0.01; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 50; // maximum number of iterations

		EstimationResultSet result = estimator.estimateState(network, inputs, epsilon, maxIterations);
		//DevelopmentHelper.printEstimationResultToConsole(network, result);

		int numberOfExperiments = 1300;
		
		double[][] data = new double[numberOfExperiments][6];

		double[][] Jm = inputs.getJacobian(result.voltages);		
		double[][] pJm = SingularValueDecomposer.pinv(Jm);
		//DevelopmentHelper.printToConsole(pJm);	
		Complex[] linEstVoltages = new Complex[result.voltages.length];
		double[] DeltaV = new double[pJm.length];
		double[] DeltaM = new double[inputs.getConnectors().size()];

		long startTime = System.currentTimeMillis();

		
for (int experiment=0;experiment<numberOfExperiments;experiment++){
	
				
/*		double P2Step = P2StepRange*(java.lang.Math.random()-0.5);
		double P3Step = P3StepRange*(java.lang.Math.random()-0.5);
		double P4Step = P4StepRange*(java.lang.Math.random()-0.5);
		double P5Step = P5StepRange*(java.lang.Math.random()-0.5);
		
		double Q2Step = Q2StepRange*(java.lang.Math.random()-0.5);
		double Q3Step = Q3StepRange*(java.lang.Math.random()-0.5);
		double Q4Step = Q4StepRange*(java.lang.Math.random()-0.5);
		double Q5Step = Q5StepRange*(java.lang.Math.random()-0.5);
*/
		
		for (double upscale=5;upscale<=5;upscale+=1.0){
		
/*			double P2upscale = upscale;
			double P3upscale = upscale;
			double P4upscale = upscale;
			double P5upscale = upscale;
			
			double Q2upscale = upscale;
			double Q3upscale = upscale;
			double Q4upscale = upscale;
			double Q5upscale = upscale;
	
			
			P2a.setMyTrueValue(P2+(P2upscale*P2Step));
			P2b.setMyTrueValue(P2+(P2upscale*P2Step));
			P2c.setMyTrueValue(P2+(P2upscale*P2Step));
	
			P3a.setMyTrueValue(P3+(P3upscale*P3Step));
			P3b.setMyTrueValue(P3+(P3upscale*P3Step));
			P3c.setMyTrueValue(P3+(P3upscale*P3Step));
			
			P4a.setMyTrueValue(P4+(P4upscale*P4Step));
			P4b.setMyTrueValue(P4+(P4upscale*P4Step));
			P4c.setMyTrueValue(P4+(P4upscale*P4Step));
			
			P5a.setMyTrueValue(P5+(P5upscale*P5Step));
			P5b.setMyTrueValue(P5+(P5upscale*P5Step));
			P5c.setMyTrueValue(P5+(P5upscale*P5Step));
			
			Q2a.setMyTrueValue(Q2+(Q2upscale*Q2Step));
			Q2b.setMyTrueValue(Q2+(Q2upscale*Q2Step));
			Q2c.setMyTrueValue(Q2+(Q2upscale*Q2Step));
			
			Q3a.setMyTrueValue(Q3+(Q3upscale*Q3Step));
			Q3b.setMyTrueValue(Q3+(Q3upscale*Q3Step));
			Q3c.setMyTrueValue(Q3+(Q3upscale*Q3Step));
			
			Q4a.setMyTrueValue(Q4+(Q4upscale*Q4Step));
			Q4b.setMyTrueValue(Q4+(Q4upscale*Q4Step));
			Q4c.setMyTrueValue(Q4+(Q4upscale*Q4Step));
			
			Q5a.setMyTrueValue(Q5+(Q5upscale*Q5Step));
			Q5b.setMyTrueValue(Q5+(Q5upscale*Q5Step));
			Q5c.setMyTrueValue(Q5+(Q5upscale*Q5Step));
*/			
			
			
/*			DeltaM[inputs.getConnectors().indexOf(P2a)] = P2upscale*P2Step;
			DeltaM[inputs.getConnectors().indexOf(P2b)] = P2upscale*P2Step;
			DeltaM[inputs.getConnectors().indexOf(P2c)] = P2upscale*P2Step;
			
			DeltaM[inputs.getConnectors().indexOf(P3a)] = P3upscale*P3Step;
			DeltaM[inputs.getConnectors().indexOf(P3b)] = P3upscale*P3Step;
			DeltaM[inputs.getConnectors().indexOf(P3c)] = P3upscale*P3Step;
			
			DeltaM[inputs.getConnectors().indexOf(P4a)] = P4upscale*P4Step;
			DeltaM[inputs.getConnectors().indexOf(P4b)] = P4upscale*P4Step;
			DeltaM[inputs.getConnectors().indexOf(P4c)] = P4upscale*P4Step;
			
			DeltaM[inputs.getConnectors().indexOf(P5a)] = P5upscale*P5Step;
			DeltaM[inputs.getConnectors().indexOf(P5b)] = P5upscale*P5Step;
			DeltaM[inputs.getConnectors().indexOf(P5c)] = P5upscale*P5Step;
			
			DeltaM[inputs.getConnectors().indexOf(Q2a)] = Q2upscale*Q2Step;
			DeltaM[inputs.getConnectors().indexOf(Q2b)] = Q2upscale*Q2Step;
			DeltaM[inputs.getConnectors().indexOf(Q2c)] = Q2upscale*Q2Step;
			
			DeltaM[inputs.getConnectors().indexOf(Q3a)] = Q3upscale*Q3Step;
			DeltaM[inputs.getConnectors().indexOf(Q3b)] = Q3upscale*Q3Step;
			DeltaM[inputs.getConnectors().indexOf(Q3c)] = Q3upscale*Q3Step;
			
			DeltaM[inputs.getConnectors().indexOf(Q4a)] = Q4upscale*Q4Step;
			DeltaM[inputs.getConnectors().indexOf(Q4b)] = Q4upscale*Q4Step;
			DeltaM[inputs.getConnectors().indexOf(Q4c)] = Q4upscale*Q4Step;
			
			DeltaM[inputs.getConnectors().indexOf(Q5a)] = Q5upscale*Q5Step;
			DeltaM[inputs.getConnectors().indexOf(Q5b)] = Q5upscale*Q5Step;
			DeltaM[inputs.getConnectors().indexOf(Q5c)] = Q5upscale*Q5Step;
*/			
			for (int i=0;i<DeltaV.length;i++){
				for (int j=0;j<DeltaM.length;j++){
					DeltaV[i] += pJm[i][j]*DeltaM[j];
				}
			}
			
/*			for (int i=0;i<linEstVoltages.length;i++){
				linEstVoltages[i] = new Complex(result.voltages[i].getReal()+DeltaV[2*i],result.voltages[i].getImag()+DeltaV[2*i+1]);
			}			
			Complex[] expansionPoint = new Complex[result.voltages.length];
			Complex[] trueSolution = new Complex[result.voltages.length];
			Complex[] approxSolution = new Complex[result.voltages.length];
			Complex[] complexError = new Complex[result.voltages.length];
			double[] relativeError = new double[complexError.length];
			double[] relativeMissguess = new double[complexError.length];
			for (int i=0;i<linEstVoltages.length;i++){
				approxSolution[i] = new Complex(linEstVoltages[i].getReal(), linEstVoltages[i].getImag());
			}
*/						
		}
	}
		long endTime = System.currentTimeMillis();
		//DevelopmentHelper.printToConsole(linEstVoltages);
		DecimalFormat format = new DecimalFormat("##0.000");
		DecimalFormat format2 = new DecimalFormat("##0.0");

		System.out.println("average execution time (ms): " + format.format(((double)endTime-(double)startTime)/(double)numberOfExperiments));
				
	}

}
