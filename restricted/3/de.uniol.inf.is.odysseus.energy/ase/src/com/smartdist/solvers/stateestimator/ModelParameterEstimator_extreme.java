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
import com.smartdist.util.SVDResultSet;
import com.smartdist.util.SingularValueDecomposer;

public class ModelParameterEstimator_extreme {

	
	public double[] determineModelError(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, EstimationResultSet expansionPoint, Inputs inputs, double[] meanValues){
		
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
			for (int j=0;j<numberOfConnectors;j++){
				hSensitivity[j][i] = inputs.getConnector(j).getModelSensitivity(network, expansionPoint.voltages,
						elementOfTuneable.elementAt(i), indexOfTuneable.elementAt(i))/stdDevs[j];
			}
		}
		
//		System.out.println("h(x) parameter sensitivity");
//		DevelopmentHelper.printToConsole(hSensitivity);
	
		
//		SVDResultSet hSensitivitySVD = SingularValueDecomposer.singuglarValueDecomposition(hSensitivity);
//		double[][] hSensitivityPInv = SingularValueDecomposer.pinv(hSensitivitySVD);

//		System.out.println("hSensitivities pseudo-inverse");
//		DevelopmentHelper.printToConsole(hSensitivityPInv);
		
//		double[][] hSensitivityInterdependencies = new double[numberOfTuneables][numberOfTuneables];
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfTuneables;j++){
//				for (int k=0;k<numberOfConnectors;k++){
//					hSensitivityInterdependencies[i][j] += hSensitivityPInv[i][k]*hSensitivity[k][j];
//				}
//			}
//		}
		
//		System.out.println("h-sensitivity interdependencies");
//		DevelopmentHelper.printToConsole(hSensitivityInterdependencies);
		
//		SVDResultSet hSensitivityInterdependeciesSVD = SingularValueDecomposer.singuglarValueDecomposition(hSensitivityInterdependencies);
//		double[][] hSensitivitiyInterdependenciesPInv = SingularValueDecomposer.pinv(hSensitivityInterdependeciesSVD);
		
//		System.out.println("pseudo-inverse of h-sensitivity interdependencies");
//		DevelopmentHelper.printToConsole(hSensitivitiyInterdependenciesPInv);
		
		
		
		
		/////////////////////////////////
		// System Jacobian H(x)
		////////////////////////////////
		double[][] jacobian = new double[numberOfConnectors][4*2*numberOfNodes];
		for (int i=0;i<numberOfConnectors;i++){
			jacobian[i] = inputs.getConnector(i).myJacobianRow(expansionPoint.voltages);
		}
		
		//////////////////////////
		//
		//    Relative dangerous step
		//
			for (int i=0;i<numberOfConnectors;i++){
				for (int j=0;j<4*2*numberOfNodes;j++){
					jacobian[i][j] = jacobian[i][j]/stdDevs[i];
				}
			}
		//
		//    hope all went well
		//
		//////////////////////
	
		
		
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
			residualsSensitivity[i][i] += 1;//stdDevs[i];
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
		
		SVDResultSet modelErrorSensitivitySVD = SingularValueDecomposer.singuglarValueDecomposition(modelErrorSensitivity);
		double[][] modelErrorSensitivityPInv = SingularValueDecomposer.pinv(modelErrorSensitivitySVD);
		
		
		
		
		////////////////////////
		//
		// Mal überprüfen, ob wir normieren müssen
		//
		
//		double[][] parameterInterdependencies = new double[numberOfTuneables][numberOfTuneables];
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfTuneables;j++){
//				for (int k=0;k<numberOfConnectors;k++){
//					parameterInterdependencies[i][j] += modelErrorSensitivityPInv[i][k]*modelErrorSensitivity[k][j];
//				}
//			}
//			//parameterInterdependencies[i][i] += 1;
//		}
//		System.out.println("Parameter Interdepencies");
//		DevelopmentHelper.printToConsole(parameterInterdependencies);
		
		//
		// sieht nicht so aus.
		//
		////////////////////////
		
//		SVDResultSet parameterInterdependeciesSVD = SingularValueDecomposer.singuglarValueDecomposition(parameterInterdependencies);
//		double[][] parameterInterdependenciesPInv = SingularValueDecomposer.pinv(parameterInterdependeciesSVD);
		
//		System.out.println("pseudo-inverse of parameter interdependecies");
//		DevelopmentHelper.printToConsole(parameterInterdependenciesPInv);
		
		
		
		
//		double[][] parameterPropagation = new double[numberOfTuneables][numberOfTuneables];
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfTuneables;j++){
//				for (int k=0;k<numberOfConnectors;k++){
//					parameterPropagation[i][j] += hSensitivityPInv[i][k]*modelErrorSensitivity[k][j];
//				}
//			}
//		}
		
//		System.out.println("Parameter Propagation");
//		DevelopmentHelper.printToConsole(parameterPropagation);
		
		
		
//		SVDResultSet parameterPropagationSVD = SingularValueDecomposer.singuglarValueDecomposition(parameterPropagation);
//		double[][] parameterPropagationPInv = SingularValueDecomposer.pinv(parameterPropagationSVD);
				
//		System.out.println("Pinv of Parameter Propagation");
//		DevelopmentHelper.printToConsole(parameterPropagationPInv);
		
		
		
//		double[][] intendedParameterChange = new double[numberOfTuneables][numberOfConnectors];
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfConnectors;j++){
//				for (int k=0;k<numberOfConnectors;k++){
//					intendedParameterChange[i][j] += hSensitivityPInv[i][k]*residualsSensitivity[k][j];
//				}
//			}
//		}
		
//		System.out.println("intended Parameter Change matrix");
//		DevelopmentHelper.printToConsole(intendedParameterChange);
		
		
//		double[][] correctionMatrix = new double[numberOfTuneables][numberOfConnectors];
//		
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfConnectors;j++){
//				for (int k=0;k<numberOfTuneables;k++){
//					correctionMatrix[i][j] += parameterPropagationPInv[i][k]*intendedParameterChange[k][j];
//				}
//			}
//		}
		
		
//		System.out.println("Overall Model Parameter Correction Matrix");
//		DevelopmentHelper.printToConsole(correctionMatrix);
		
		
		////////////////////////////
		// weighted residuals
		///////////////////////////
		double[] weightedResiduals = new double[numberOfConnectors];
		for (int i=0;i<numberOfConnectors;i++){
			weightedResiduals[i] = meanValues[i]/stdDevs[i];		// <<<<<<<<<<<<<<<<<<<<< Dangerous Step !!!
		}
		
//		System.out.println("weighted residuals");
//		DevelopmentHelper.printToConsole(weightedResiduals);
		
		
		
		//////////////////////////
		// estimated model error
		/////////////////////////
		double[] modelError = new double[numberOfTuneables];
		for (int i=0;i<numberOfTuneables;i++){
			for (int j=0;j<numberOfConnectors;j++){
				modelError[i] += modelErrorSensitivityPInv[i][j]*weightedResiduals[j];
			}
		}
		
//		System.out.println("Estimated Model Error");
//		DevelopmentHelper.printToConsole(modelError);
		
//		double[] weightedModelErrors = new double[numberOfTuneables];
//		for (int i=0;i<numberOfTuneables;i++){
//			for (int j=0;j<numberOfTuneables;j++){
//				weightedModelErrors[i] += hSensitivitiyInterdependenciesPInv[i][j]*modelError[j];
//			}
//			//weightedModelErrors[i] += modelError[i];
//		}
		
		return modelError;
//		return weightedModelErrors;

	}
	
	
	
	
	
	@SuppressWarnings("unused")
	public void tuneModel2(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, double[] modelError){		
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
		for (int i=0;i<tuneables.length;i++){
			double[] localTuneables = new double[localIndices.elementAt(i).length];
			for (int j=0;j<localTuneables.length;j++){
				double update = modelError[localIndices.elementAt(i)[j]];//*tuneables[i].getSensitivityAnalysisStepSize()[j];
				double oldvalue = elementOfTuneable.elementAt(i).getTuneables()[indexOfTuneable.elementAt(i)];
				System.out.print("\t" + (oldvalue-update));
				localTuneables[j] = oldvalue-update;
			}
			tuneables[i].setTunables(localTuneables);
			//System.out.print(((LengthTuneableLine)tuneables[i]).length + " ");
		}
		System.out.println();
		network.generateAdmittanceMatrices();	
	}
	
	
	
	
	
	
	
	
	
	
	public void tuneModel(FourWireNetwork network, TuneableEightTerminalElement[] tuneables, Inputs inputs, double[][] timeSeries, int iterations){
		int numberOfSamples = timeSeries[0].length;
		@SuppressWarnings("unused")
		double[][] deviations = new double[timeSeries.length][timeSeries[0].length];
		AdaptiveStateEstimator estimator = new AdaptiveStateEstimator();
		EstimationResultSet result = null;
		
		double epsilon = 0.0001; // maximum norm of voltage improvement vector for which convergence is assumed
		int maxIterations = 20; // maximum number of iterations
		//double singularValueThreshold = 1e-8; // minimum value for a singular value to be considered significant
		
		for (int iter=0;iter<iterations;iter++){
			boolean[] converged = new boolean[numberOfSamples];
			int numberOfConverged = 0;
			double[][] modelError = new double[numberOfSamples][8];/// <<<-------- Hier UNBEDINGT noch die Gesamtanzahl der tuneables einsetzen!!!
			double[] errors = new double[inputs.numberOfConnectors()];
			double[][] allErrors = new double[numberOfSamples][inputs.numberOfConnectors()];
			System.out.print("No. iterations: ");
			for (int i=0;i<numberOfSamples;i++){
				for (int j=0;j<inputs.numberOfConnectors();j++){
					inputs.getConnector(j).setMyTrueValue(timeSeries[j][i]);
				}
				result = estimator.estimateState(network, inputs, epsilon, maxIterations);
				if (result.usedIterrations<result.allowedMaxIterations){
					converged[i] = true;
					numberOfConverged++;
					for (int j=0;j<inputs.numberOfConnectors();j++){
						errors[j] = inputs.getConnector(j).myTrueValue() - inputs.getConnector(j).myTheroreticalValue(result.voltages);
						allErrors[i][j] = errors[j];
					}
					modelError[i] = this.determineModelError(network, tuneables, result, inputs, errors);
					//DevelopmentHelper.printEstimationResultToConsole(network, result);
				}else{
					converged[i] = false;
					//DevelopmentHelper.printEstimationResultToConsole(network, result);
				}
				
				////////////////
				// output
				//DevelopmentHelper.printEstimationResultToConsole(network, result);
//				for (int j=0;j<inputs.numberOfConnectors();j++){
//					deviations[j][i] = inputs.getConnector(j).myTrueValue() - inputs.getConnector(j).myTheroreticalValue(result.voltages);
//				}
//				
				System.out.print(result.usedIterrations + ", ");
			}
			double[] averageError = new double[inputs.numberOfConnectors()];
			double[] averageModelError = new double[modelError[0].length];
			for (int i=0;i<numberOfSamples;i++){
				if (converged[i]){//(true){//(converged[i]){
					for (int j=0;j<modelError[0].length;j++){
						averageModelError[j] += -modelError[i][j];
					}
					for (int j=0;j<inputs.numberOfConnectors();j++){
						averageError[j] += allErrors[i][j];
					}
				}
			}
			for (int i=0;i<modelError[0].length;i++){
				averageModelError[i] = averageModelError[i]/numberOfConverged;//numberOfSamples;//numberOfConverged;
			}
			for (int i=0;i<inputs.numberOfConnectors();i++){
				averageError[i] = averageError[i]/numberOfConverged;
			}
			
			//this.tuneModel2(network, tuneables, this.determineModelError(network, tuneables, result, inputs, averageError));
			
			//DevelopmentHelper.printToConsole(averageModelError);
			this.tuneModel2(network, tuneables, averageModelError);
			
			
			
			
		}
	}
}
