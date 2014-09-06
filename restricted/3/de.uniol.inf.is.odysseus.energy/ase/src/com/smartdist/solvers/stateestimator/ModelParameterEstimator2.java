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
import com.smartdist.util.DevelopmentHelper;
import com.smartdist.util.SVDResultSet;
import com.smartdist.util.SingularValueDecomposer;

public class ModelParameterEstimator2 {

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
		
		double[] stdDevs = new double[numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			stdDevs[i] = inputs.stdDev.elementAt(i);
		}

		
		///////////////////////
		// h(x) sensitivity
		/////////////////////
		@SuppressWarnings("unused")
		double[][] hSensitivityConstV = new double[numberOfConnectors][numberOfTuneables];
		double[][][] modelSensitivities = new double[numberOfTuneables][numberOfConnectors][4*2*network.getVertexCount()];
		double[][][] inverses = new double[numberOfTuneables][2*4*network.getVertexCount()][numberOfConnectors];
		double[][][] correctionMatrices = new double[numberOfTuneables][numberOfConnectors][numberOfConnectors];

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

			for (int j=0;j<numberOfConnectors;j++){
				modelSensitivities[i][j] = inputs.getConnector(j).myJacobianRow(expansionPoint.voltages);
			}
			
			for (int j=0;j<numberOfConnectors;j++){
				for (int k=0;k<4*2*network.getVertexCount();k++){
					modelSensitivities[i][j][k] = modelSensitivities[i][j][k]/stdDevs[j];//sensitivityAnalysisStepSize[indexOfTuneable.elementAt(i)];
				}
			}
			elementOfTuneable.elementAt(i).setTunables(oldValues);
			inverses[i] = SingularValueDecomposer.pinv(modelSensitivities[i]);
			for (int j=0;j<numberOfConnectors;j++){
				for (int k=0;k<numberOfConnectors;k++){
					for (int l=0;l<4*2*network.getVertexCount();l++){
						correctionMatrices[i][j][k] += modelSensitivities[i][j][l]*inverses[i][l][k];//sensitivityAnalysisStepSize[indexOfTuneable.elementAt(i)];
					}
				}
			}

		}
		network.generateAdmittanceMatrices();
		
		double[] deratedMeanValues = new double[meanValues.length];
		double lengthMeansVector = 0;
		double lengthDeratedMeansVector = 0;
		for (int i=0;i<meanValues.length;i++){
			deratedMeanValues[i] = meanValues[i]/stdDevs[i];
			lengthMeansVector += meanValues[i]*meanValues[i];
			lengthDeratedMeansVector += deratedMeanValues[i]*deratedMeanValues[i];
		}
		lengthMeansVector = Math.sqrt(lengthMeansVector);
		lengthDeratedMeansVector = Math.sqrt(lengthDeratedMeansVector);
		
		
		double[][] hSensitivity = new double[numberOfConnectors][numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfConnectors;j++){
				for (int k=0;k<numberOfConnectors;k++){
					hSensitivity[j][i] += correctionMatrices[i][j][k]*meanValues[k]/stdDevs[k]/elementOfTuneable.elementAt(i).getSensitivityAnalysisStepSize()[indexOfTuneable.elementAt(i)];
				}
			}
		}
		double[] lengthsCorrections = new double[numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfConnectors;j++){
				lengthsCorrections[i] += hSensitivity[j][i]*hSensitivity[j][i];
			}
		}
		
		System.out.println("Length mean values: " + lengthMeansVector + " --- vector:");
		DevelopmentHelper.printToConsole(meanValues);
		System.out.println("Length derated mean values: " + lengthDeratedMeansVector + " --- vector:");
		DevelopmentHelper.printToConsole(deratedMeanValues);
		System.out.print("Projected mean vectors length :");
		for (int i=0;i<numberOfTuneables;i++) {
			System.out.print(lengthsCorrections[i] + " ");
		}
		System.out.println("--- vectors:");
		DevelopmentHelper.printToConsole(hSensitivity);
		
		
		SVDResultSet hSVD = SingularValueDecomposer.singuglarValueDecomposition(hSensitivity);
		double pInv[][] = SingularValueDecomposer.pinv(hSVD);
		
		

				
		double[] updatedTuneables = new double[numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfConnectors;j++){
				updatedTuneables[i] += pInv[i][j]*meanValues[j]/stdDevs[j];//*(lengthsCorrections[i]/lengthDeratedMeansVector);
			}
		}
		
		System.out.print("previous lengths: ");
		for (int i=0;i<tuneables.length;i++){
			double[] localTuneables = new double[localIndices.elementAt(i).length];
			for (int j=0;j<localTuneables.length;j++){
				double update = updatedTuneables[localIndices.elementAt(i)[j]];//*tuneables[i].getSensitivityAnalysisStepSize()[j];
				double oldvalue = elementOfTuneable.elementAt(i).getTuneables()[indexOfTuneable.elementAt(i)];
				localTuneables[j] = oldvalue+update;
			}
			tuneables[i].setTunables(localTuneables);
			System.out.print(((LengthTuneableLine)tuneables[i]).length + " ");
		}
		System.out.println();
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
