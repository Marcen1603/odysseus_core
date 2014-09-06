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
import com.smartdist.util.SVDResultSet;
import com.smartdist.util.SingularValueDecomposer;

public class ModelParameterEstimator3 {

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

		int numberOfNodes = network.getVertexCount();
		
		///////////////////////
		// dh(x)/dp sensitivity matrix H_p(x)
		/////////////////////
		double[][] hSensitivity = new double[numberOfConnectors][numberOfTuneables];

		for (int i=0;i<numberOfTuneables;i++){
			double[] oldValues = elementOfTuneable.elementAt(i).getTuneables();
			double[] sensitivityAnalysisStepSize = elementOfTuneable.elementAt(i).getSensitivityAnalysisStepSize();
			double[] testValues = new double[oldValues.length];
			for (int j=0;j<testValues.length;j++){
				testValues[j] = oldValues[j];
			}
			double stepSize = sensitivityAnalysisStepSize[indexOfTuneable.elementAt(i)];
			testValues[indexOfTuneable.elementAt(i)] = oldValues[indexOfTuneable.elementAt(i)] + stepSize;
			elementOfTuneable.elementAt(i).setTunables(testValues);
			network.generateAdmittanceMatrices();

			for (int j=0;j<numberOfConnectors;j++){
				hSensitivity[j][i] = inputs.getConnector(j).myTheroreticalValue(expansionPoint.voltages)/stepSize;//.myJacobianRow(expansionPoint.voltages);
			}
			testValues[indexOfTuneable.elementAt(i)] = oldValues[indexOfTuneable.elementAt(i)];
			elementOfTuneable.elementAt(i).setTunables(testValues);
		}
		network.generateAdmittanceMatrices();
		
//		System.out.println("h(x) parameter sensitivity");
//		DevelopmentHelper.printToConsole(hSensitivity);
		
		
		/////////////////////////////////
		// System Jacobian H(x)
		////////////////////////////////
		double[][] jacobian = new double[numberOfConnectors][4*2*numberOfNodes];
		for (int i=0;i<numberOfConnectors;i++){
			jacobian[i] = inputs.getConnector(i).myJacobianRow(expansionPoint.voltages);
		}
		
//		System.out.println("system jacobian H(x)");
//		DevelopmentHelper.printToConsole(jacobian);

		
		///////////////////////////////
		// Left range of H(x)   U_{1,\dots,r}\cdot U_{1,\dots,r}^\dagger
		//////////////////////////////
		SVDResultSet jacobianSVD = SingularValueDecomposer.singuglarValueDecomposition(jacobian);
		double[][] leftRange = new double[numberOfConnectors][numberOfConnectors];
		for (int r=0;r<jacobianSVD.rank;r++){
			for (int i=0;i<numberOfConnectors;i++){
				for (int k=0;k<numberOfConnectors;k++){
					leftRange[i][k] += jacobianSVD.leftSingularVectors[i][r]*jacobianSVD.leftSingularVectors[k][r];
				}
			}
		}
		
//		System.out.println("Left range of H(x)");
//		DevelopmentHelper.printToConsole(leftRange);
		
		
		//////////////////////////////
		// residuals sensitivity S
		//////////////////////////////
		double[][] residualsSensitivity = new double[numberOfConnectors][numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			for (int j=0;j<numberOfConnectors;j++){
				residualsSensitivity[i][j] = - leftRange[i][j];
			}
		}
		for (int i=0;i<numberOfConnectors;i++){
			residualsSensitivity[i][i] += 1;
		}
		
//		System.out.println("Residuals Sensitivity S");
//		DevelopmentHelper.printToConsole(residualsSensitivity);
		

		
		////////////////////////////
		// model error sensitivity
		////////////////////////////
		double[][] modelErrorSensitivity = new double[numberOfConnectors][numberOfTuneables];
		for (int i=0;i<numberOfConnectors;i++){
			for (int j=0;j<numberOfTuneables;j++){
				for (int k=0;k<numberOfConnectors;k++){
					modelErrorSensitivity[i][j] += residualsSensitivity[i][k]*hSensitivity[k][j];
				}
			}
		}
		
//		System.out.println("Model Error Sensitivities");
//		DevelopmentHelper.printToConsole(modelErrorSensitivity);
		
		
		
		////////////////////////////
		// weighted model error sensitivity
		///////////////////////////
		double[][] weightedModelErrorSensitivity = new double[numberOfConnectors][numberOfTuneables];
		for (int i=0;i<numberOfConnectors;i++){
			for (int j=0;j<numberOfTuneables;j++){
				weightedModelErrorSensitivity[i][j] = modelErrorSensitivity[i][j]/stdDevs[i];
			}
		}
		
//		System.out.println("Weighted Error Model Sensitivity");
//		DevelopmentHelper.printToConsole(weightedModelErrorSensitivity);
		
		
		
		////////////////////////////
		// pseudo inverse of correction term
		////////////////////////////
		SVDResultSet correctionSVD = SingularValueDecomposer.singuglarValueDecomposition(weightedModelErrorSensitivity);
		double[][] pInv = SingularValueDecomposer.pinv(correctionSVD);
		
//		System.out.println("pseudo inverse of correction term");
//		DevelopmentHelper.printToConsole(pInv);
		
		
		
		////////////////////////////
		// weighted residuals
		///////////////////////////
		double[] weightedResiduals = new double[numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			weightedResiduals[i] = meanValues[i]/stdDevs[i];
		}
		
//		System.out.println("weighted residuals");
//		DevelopmentHelper.printToConsole(weightedResiduals);
		
		
		
		
		//////////////////////////
		// estimated model error
		/////////////////////////
		double[] modelError = new double[numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfConnectors;j++){
				modelError[i] += pInv[i][j]*weightedResiduals[j];
			}
		}
		
//		System.out.println("Estimated Model Error");
//		DevelopmentHelper.printToConsole(modelError);
		
		
		
		
		
				
//		double[] updatedTuneables = new double[numberOfTuneables];
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfConnectors;j++){
//				updatedTuneables[i] += 0;//pInv[i][j]*meanValues[j]/stdDevs[j];//*(lengthsCorrections[i]/lengthDeratedMeansVector);
//			}
//		}
		
		System.out.print("previous lengths: ");
		for (int i=0;i<tuneables.length;i++){
			double[] localTuneables = new double[localIndices.elementAt(i).length];
			for (int j=0;j<localTuneables.length;j++){
				double update = modelError[localIndices.elementAt(i)[j]];//*tuneables[i].getSensitivityAnalysisStepSize()[j];
				double oldvalue = elementOfTuneable.elementAt(i).getTuneables()[indexOfTuneable.elementAt(i)];
				localTuneables[j] = oldvalue-update;
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
