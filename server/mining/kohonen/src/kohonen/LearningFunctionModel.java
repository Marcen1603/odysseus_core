package kohonen;

import learningFactorFunctional.LearningFactorFunctionalModel;
import metrics.MetricModel;
import network.NetworkModel;

public interface LearningFunctionModel {

	/**
	 * Return metrics
	 * @return metrics
	 * @see MetricModel
	 */
	public abstract MetricModel getMetrics();

	/**
	 * Set metrics
	 * @param metrics metrics
	 */
	public abstract void setMetrics(MetricModel metrics);

	/**
	 * Set network model
	 * @param networkModel network model
	 */
	public abstract void setNetworkModel(NetworkModel networkModel);

	/**
	 * Return network model
	 * @return network model
	 */
	public abstract NetworkModel getNetworkModel();

	/**
	 * Set max interation
	 * @param maxIteration max interation
	 */
	public abstract void setMaxIteration(int maxIteration);

	/**
	 * Return maximal number of iteration
	 * @return maximal number of iteration
	 */
	public abstract int getMaxIteration();

	/**
	 * Set reference to learning data
	 * @param learningData reference to learning data
	 */
	public abstract void setLearningData(LearningDataModel learningData);

	/**
	 * Return reference to learning data
	 * @return reference to learning data
	 */
	public abstract LearningDataModel getLearningData();

	/**
	 * Set functional learning factor model
	 * @param functionalModel functional learning factor model
	 */
	public abstract void setFunctionalModel(
			LearningFactorFunctionalModel functionalModel);

	/**
	 * Return function model
	 * @return function model
	 */
	public abstract LearningFactorFunctionalModel getFunctionalModel();

	/**
	 * Start learning process
	 */
	public abstract void learn();

}