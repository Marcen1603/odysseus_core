/*
 * 
 * This work is copyright.
 * 
 * Copyright 2012-2014 Dr. Olav Krause
 * 
 */


package com.smartdist.solvers.stateestimator;

import java.util.Vector;

import com.smartdist.modelling.network.FourWireNetwork;
import com.smartdist.modelling.network.TuneableEightTerminalElement;
import com.smartdist.modelling.network.eightterminalelements.LengthTuneableLine;
import com.smartdist.util.Complex;
import com.smartdist.util.SVDResultSet;
import com.smartdist.util.SingularValueDecomposer;

public class ModelParameterEstimator {

	public void tuneModel(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, EstimationResultSet expansionPoint, Inputs inputs, double[] meanValues){
		
		Vector<Integer> indexOfTuneable = new Vector<Integer>();
		Vector<TuneableEightTerminalElement> elementOfTuneable = new Vector<TuneableEightTerminalElement>();
		Vector<int[]> localIndices = new Vector<int[]>();
		int overallIndex = 0;
		for (int i=0;i<tuneables.length;i++){
			double[] tempTuneables = tuneables[i].getTuneables();
			int[] indexMatrix = new int[tempTuneables.length];
			for (int j=0;j<tempTuneables.length;j++){
				indexOfTuneable.add(j);
				elementOfTuneable.add(tuneables[i]);
				indexMatrix[j] = overallIndex++;
			}
			localIndices.add(indexMatrix);
		}
		int numberOfTuneables = indexOfTuneable.size();
		
		Observers testOutputs = new Observers();
		int numberOfConnectors = inputs.numberOfConnectors();
		for (int i=0;i<numberOfConnectors;i++){
			testOutputs.addConnector(inputs.getConnector(i));
		}
		
		// base case
		double[] initialOutputs = new double[numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			initialOutputs[i] = testOutputs.getConnector(i).myTheroreticalValue(expansionPoint.voltages);
		}
		
		///////////////////////
		// h(x) sensitivity with constant voltage profile
		/////////////////////
		double[][] hSensitivityConstV = new double[numberOfConnectors][numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			double[] oldValues = elementOfTuneable.elementAt(i).getTuneables();
			double[] sensitivityAnalysisStepSize = elementOfTuneable.elementAt(i).getSensitivityAnalysisStepSize();
			double[] testValues = new double[oldValues.length];
			for (int j=0;j<testValues.length;j++){
				testValues[j] = oldValues[j];
			}
			testValues[indexOfTuneable.elementAt(i)] = oldValues[indexOfTuneable.elementAt(i)] + sensitivityAnalysisStepSize[indexOfTuneable.elementAt(i)];
			elementOfTuneable.elementAt(i).setTunables(testValues);
			//double[] adjustedOutputs = new double[numberOfConnectors];
			network.generateAdmittanceMatrices();
			for (int j=0;j<numberOfConnectors;j++){
				/////////////////////////////////////////////////////
				hSensitivityConstV[j][i] = (initialOutputs[j] - testOutputs.getConnector(j).myTheroreticalValue(expansionPoint.voltages));//sensitivityAnalysisStepSize[indexOfTuneable.elementAt(i)];
			}	
			elementOfTuneable.elementAt(i).setTunables(oldValues);
		}
		network.generateAdmittanceMatrices();
		
		///////////////////////
		// h(x) sensitivity with constant nodal currents
		/////////////////////
		double[][] hSensitivityConstI = new double[numberOfConnectors][numberOfTuneables];		
		double[][] voltageSensitivity = new double[2*4*network.getVertexCount()][numberOfTuneables];
		Complex[] initialCurrents = new Complex[4*network.getVertexCount()];
		Complex[][] initialAdmittanceMatrix = network.networkAdmittanceMatrix;
		Complex[] initialVoltages = expansionPoint.voltages;
		int numberOfNodes = network.getVertexCount();
		for (int i=0;i<4*numberOfNodes;i++){
			initialCurrents[i] = new Complex(0,0);
			for (int j=0;j<4*numberOfNodes;j++){
				initialCurrents[i].add(new Complex(initialAdmittanceMatrix[i][j]).multiply(initialVoltages[j]));
			}
		}
		Complex[][] tempAdmittanceMatrix = null;
		Complex[][] tempInverse = null;
		Complex[] tempCurrents = new Complex[4*numberOfNodes];
		Complex[] tempVoltages = new Complex[4*numberOfNodes];
		for (int i=0;i<numberOfTuneables;i++){
			double[] oldValues = elementOfTuneable.elementAt(i).getTuneables();
			double[] sensitivityAnalysisStepSize = elementOfTuneable.elementAt(i).getSensitivityAnalysisStepSize();
			double[] testValues = new double[oldValues.length];
			for (int j=0;j<testValues.length;j++){
				testValues[j] = oldValues[j];
			}
			testValues[indexOfTuneable.elementAt(i)] = oldValues[indexOfTuneable.elementAt(i)] + sensitivityAnalysisStepSize[indexOfTuneable.elementAt(i)];
			elementOfTuneable.elementAt(i).setTunables(testValues);
			network.generateAdmittanceMatrices();
			
			// Here's where the music is
			tempAdmittanceMatrix = network.networkAdmittanceMatrix;
			for (int k=0;k<4*numberOfNodes;k++){
				tempCurrents[k] = new Complex(0,0);
				for (int j=0;j<4*numberOfNodes;j++){
					tempCurrents[k].add(new Complex(tempAdmittanceMatrix[k][j]).multiply(initialVoltages[j]));
				}
			}
			tempInverse = SingularValueDecomposer.pinv(tempAdmittanceMatrix);
			for (int j=0;j<4*numberOfNodes;j++){
				tempVoltages[j] = new Complex(0,0);
				for (int k=0;k<tempCurrents.length;k++){
					tempVoltages[j].add(new Complex(tempInverse[j][k]).multiply(new Complex(tempCurrents[k]).sustract(initialCurrents[k])));
				}
			}
			for (int j=0;j<4*numberOfNodes;j++){
				//////////////////////////////////////////////////
				voltageSensitivity[2*j+0][i] = tempVoltages[j].getReal();//sensitivityAnalysisStepSize[indexOfTuneable.elementAt(i)];
				voltageSensitivity[2*j+1][i] = tempVoltages[j].getImag();//sensitivityAnalysisStepSize[indexOfTuneable.elementAt(i)];
			}
			elementOfTuneable.elementAt(i).setTunables(oldValues);			
		}
		double[][] jacobian = inputs.getJacobian(initialVoltages);
		for (int i=0;i<numberOfConnectors;i++){
			for (int j=0;j<numberOfTuneables;j++){
				for (int k=0;k<2*4*numberOfNodes;k++){
					hSensitivityConstI[i][j] += jacobian[i][k]*voltageSensitivity[k][j];
				}
			}
		}
		double[] stdDevs = new double[numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			stdDevs[i] = inputs.stdDev.elementAt(i);
		}
		double[][] hSensitivity = new double[numberOfConnectors][numberOfTuneables];
		for (int i=0;i<numberOfConnectors;i++){
			for (int j=0;j<numberOfTuneables;j++){
				hSensitivity[i][j]=(hSensitivityConstV[i][j]+hSensitivityConstI[i][j])/stdDevs[i];
			}
		}		
		
		SVDResultSet hSVD = SingularValueDecomposer.singuglarValueDecomposition(hSensitivity);
		double pInv[][] = SingularValueDecomposer.pinv(hSVD);
		
		///////////////////
		// Debugging stuff
//			double[][] selfMapping = new double[numberOfConnectors][numberOfConnectors];
//			for (int i=0;i<numberOfConnectors;i++){
//				for (int j=0;j<numberOfConnectors;j++){
//					for (int k=0;k<numberOfTuneables;k++){
//						selfMapping[i][j] += hSensitivity[i][k]+pInv[k][j];
//					}
//				}
//			}
//			double[] mappedMeans = new double[numberOfConnectors];
//			for (int i=0;i<numberOfConnectors;i++){
//				for (int j=0;j<numberOfConnectors;j++){
//					mappedMeans[i] = selfMapping[i][j]*meanValues[j];
//				}
//			}
//			DevelopmentHelper.printToConsole(meanValues);
//			DevelopmentHelper.printToConsole(mappedMeans);
//			double tempLength = 0;
//			for (int i=0;i<numberOfConnectors;i++){
//				tempLength += meanValues[i]*meanValues[i];
//			}
//			System.out.println("Length means: " + Math.sqrt(tempLength));
//			tempLength = 0;
//			for (int i=0;i<numberOfConnectors;i++){
//				tempLength += mappedMeans[i]*mappedMeans[i];
//			}
//			System.out.println("Length mapped means: " + Math.sqrt(tempLength));
		//
		///////////////////
		
		double[] coverage = new double[numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<hSVD.rank;j++){
				coverage[i] += Math.pow(hSVD.rightSingularVectors[i][j],2);
			}
			coverage[i] = Math.sqrt(coverage[i]);
			
			////////////////
			//// OUTPUT
			///////////////
			//System.out.println("coverage tuneable " + i + ": " + coverage[i]);
		}
				
		double[] updatedTuneables = new double[numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfConnectors;j++){
				updatedTuneables[i] += pInv[i][j]*meanValues[j]/stdDevs[j];
			}
		}
		
		System.out.print("previous lengths: ");
		//double maxAdjustment = 10.0;
		for (int i=0;i<tuneables.length;i++){
			double[] localTuneables = new double[localIndices.elementAt(i).length];
			for (int j=0;j<localTuneables.length;j++){
				double update = updatedTuneables[localIndices.elementAt(i)[j]]*tuneables[i].getSensitivityAnalysisStepSize()[j];
				double oldvalue = elementOfTuneable.elementAt(i).getTuneables()[indexOfTuneable.elementAt(i)];
//				if (Math.abs(update)>maxAdjustment*Math.abs(oldvalue)){
//					update = Math.signum(update)*maxAdjustment*Math.abs(oldvalue);
//				}
				localTuneables[j] = oldvalue+update;
			}
			tuneables[i].setTunables(localTuneables);
			System.out.print(((LengthTuneableLine)tuneables[i]).length + " ");
		}
		//System.out.println();
		network.generateAdmittanceMatrices();	
	}
	
	public void tuneModel(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, Inputs inputs, double[][] timeSeries, int iterations){
		int numberOfSamples = timeSeries[0].length;
		double[][] deviations = new double[timeSeries.length][timeSeries[0].length];
		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();
		EstimationResultSet result = null;
		
		double epsilon = 0.0001; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 20; // maximum number of iterations
		//double singularValueThreshold = 1e-8; // minimum value for a singular value to be considered significant
		
		for (int iter=0;iter<iterations;iter++){
			for (int i=0;i<numberOfSamples;i++){
				for (int j=0;j<inputs.numberOfConnectors();j++){
					inputs.getConnector(j).setMyTrueValue(timeSeries[j][i]);
				}
				result = estimator.estimateState(network, inputs, epsilon, maxIterations);
				
				////////////////
				// output
				//DevelopmentHelper.printEstimationResultToConsole(network, result);
				for (int j=0;j<inputs.numberOfConnectors();j++){
					deviations[j][i] = inputs.getConnector(j).myTrueValue() - inputs.getConnector(j).myTheroreticalValue(result.voltages);
				}
			}
			//DevelopmentHelper.printToConsole(deviations);
			double[] meanValues = new double[inputs.numberOfConnectors()];
			for (int i=0;i<inputs.numberOfConnectors();i++){
				for (int j=0;j<numberOfSamples;j++){
					meanValues[i] += -deviations[i][j];
				}
				meanValues[i] = meanValues[i]/numberOfSamples;
			}
			//DevelopmentHelper.printToConsole(meanValues);
			
			double averageMean = 0.0;
			for (int i=0;i<inputs.numberOfConnectors();i++){
				averageMean += meanValues[i]*meanValues[i];
			}
			averageMean = Math.sqrt(averageMean);
			System.out.println(" -- average mean: " + averageMean);
			
			////////////////////////////////////////////////
			EstimationResultSet expansionPoint = result;  // TO BE THOUGHT ABOUT !!!
			////////////////////////////////////////////////
			
			this.tuneModel(network, tuneables, expansionPoint, inputs, meanValues);
		}
	}
}
