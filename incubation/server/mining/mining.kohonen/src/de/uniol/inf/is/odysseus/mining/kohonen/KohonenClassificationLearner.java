/**
 * 
 */
package de.uniol.inf.is.odysseus.mining.kohonen;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import kohonen.LearningFunctionModel;
import kohonen.WTALearningFunction;
import kohonen.WTMLearningFunction;
import learningFactorFunctional.GaussFunctionalFactor;
import learningFactorFunctional.LearningFactorFunctionalModel;
import metrics.CityBlockMetric;
import metrics.EuclidesMetric;
import metrics.MetricModel;
import network.DefaultNetwork;
import network.NetworkModel;
import topology.GaussNeighbourhoodFunction;
import topology.HexagonalTopology;
import topology.MatrixTopology;
import topology.NeighbourhoodFunctionModel;
import topology.TopologyModel;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;

/**
 * Implements a ClassificationLearer that uses the Java Kohonen Neural Network
 * Library (JKNNL) to build a classifier.
 * 
 * @author Dennis Nowak
 * 
 */
public class KohonenClassificationLearner<M extends ITimeInterval> implements
		IClassificationLearner<M> {

	public static String[] METHODS = { "WTA", "WTM" };
	private String algorithm;

	private int cols = 10;
	private int rows = 10;
	private int radius = 10;

	private double[] maxWeight;
	private int dimensions;

	private int iterations = 100;

	private TopologyModel topology;
	private NetworkModel network;
	private MetricModel metric;
	private DataInput<M> learningData;
	private LearningFactorFunctionalModel function;
	private NeighbourhoodFunctionModel neighboorModel;
	private LearningFunctionModel learningFunction;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner
	 * #createInstance()
	 */
	@Override
	public IClassificationLearner<M> createInstance() {
		return new KohonenClassificationLearner<>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner
	 * #init(java.lang.String,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute, java.util.Map)
	 */
	@Override
	public void init(String algorithm, SDFSchema inputschema,
			SDFAttribute classAttribute,
			Map<SDFAttribute, List<String>> nominals) {
		algorithm = algorithm.toUpperCase();
		this.algorithm = algorithm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner
	 * #createClassifier(java.util.List)
	 */
	@Override
	public IClassifier<M> createClassifier(List<Tuple<M>> tuples) {
		this.learningData = new DataInput<M>(tuples);
		this.maxWeight = this.learningData.getMaxWeights();
		this.dimensions = maxWeight.length;
		network = new DefaultNetwork(dimensions, maxWeight, topology);
		function = new GaussFunctionalFactor(radius);
		neighboorModel = new GaussNeighbourhoodFunction(radius);
		switch (algorithm) {
		case "WTA":
			this.learningFunction = new WTALearningFunction(network,
					iterations, metric, learningData, function);
			break;
		case "WTM":
			this.learningFunction = new WTMLearningFunction(network,
					iterations, metric, learningData, function, neighboorModel);
			break;
		default:
			throw new IllegalArgumentException(
					"There is no classifier model called " + algorithm + "!");
		}
		learningFunction.learn();
		KohonenClassifier<M> classifier = new KohonenClassifier<>(network,
				metric);
		return classifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner
	 * #setOptions(java.util.Map)
	 */
	@Override
	public void setOptions(Map<String, String> options) {
		if (options.containsKey("rows")) {
			int rows = Integer.parseInt(options.get("rows"));
			this.rows = rows;
		}
		if (options.containsKey("cols")) {
			int cols = Integer.parseInt(options.get("cols"));
			this.cols = cols;
		}
		if (options.containsKey("radius")) {
			int radius = Integer.parseInt(options.get("radius"));
			this.radius = radius;
		}
		if (options.containsKey("iterations")) {
			int iterations = Integer.parseInt(options.get("iterations"));
			this.iterations = iterations;
		}
		if (options.containsKey("topology")) {
			switch (options.get("topology")) {
			case "matrix":
				this.topology = new MatrixTopology(rows, cols, radius);
				break;
			case "hexagonal":
				this.topology = new HexagonalTopology(rows, cols);
				break;
			}
		} else {
			this.topology = new MatrixTopology(rows, cols, radius);
		}
		if (options.containsKey("metric")) {
			switch (options.get("metric")) {
			case "euclides":
				this.metric = new EuclidesMetric();
				break;
			case "cityblock":
				this.metric = new CityBlockMetric();
				break;
			}
		} else {
			this.metric = new EuclidesMetric();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner
	 * #getName()
	 */
	@Override
	public String getName() {
		return "kohonen";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner
	 * #getAlgorithmNames()
	 */
	@Override
	public List<String> getAlgorithmNames() {
		return Arrays.asList(METHODS);
	}

}
