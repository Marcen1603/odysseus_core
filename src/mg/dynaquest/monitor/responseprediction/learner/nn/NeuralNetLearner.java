package mg.dynaquest.monitor.responseprediction.learner.nn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import mg.dynaquest.monitor.responseprediction.learner.AbstractLearner;
import mg.dynaquest.monitor.responseprediction.learner.Attribute;
import mg.dynaquest.monitor.responseprediction.preprocessor.IMetadataPreprocessor;
import mg.dynaquest.monitor.responseprediction.preprocessor.IOfflineListener;
import mg.dynaquest.monitor.responseprediction.preprocessor.IOnlineListener;
import mg.dynaquest.monitor.responseprediction.preprocessor.PlanOperator;
import mg.dynaquest.monitor.responseprediction.preprocessor.PreprocessedMetadata;

import org.joone.engine.FullSynapse;
import org.joone.engine.Layer;
import org.joone.engine.LinearLayer;
import org.joone.engine.Monitor;
import org.joone.engine.SigmoidLayer;
import org.joone.engine.Synapse;
import org.joone.helpers.factory.JooneTools;
import org.joone.net.NeuralNet;

/**
 * A learner based on a multilayer perceptron implementation from the JOONE
 * project.
 * 
 * @author Jonas Jacobi
 */
public class NeuralNetLearner extends AbstractLearner implements
		IOnlineListener, IOfflineListener {
	private static final long serialVersionUID = 3229204294559716449L;
	
	/**
	 * Method of error calculation. Either use mean squared error
	 * or the median of the squared errors.
	 * @author Jonas Jacobi
	 */
	public static enum ErrorCalculationType {MEAN, MEDIAN};

	private Map<String, NeuralNet> nets;

	private Map<String, ArrayList<double[]>> oldInputs;

	private Map<String, ArrayList<double[]>> oldOutputs;

	private int epochs;

	private double learningRate;

	private double momentum;

	private int batchSize;

	private long lastTime;

	private int hiddenNeurons;

	private ErrorCalculationType errorType;

	/**
	 * Constructor
	 * 
	 * @param preprocessor
	 *            the preprocessor
	 * @param learningRate
	 *            the learning rate used in the neuronal nets
	 * @param momentum
	 *            the momentum used in the neural net
	 * @param epochs
	 *            the number of epochs the neural net gets trained on each
	 *            training pattern
	 * @param batchSize
	 *            the batchsize used for batch training (use 1 for online
	 *            learning)
	 * @param hiddenNeurons
	 *            the number of hidden neurons in the neural nets
	 * @param errorType determines which kind of method is used to calculate
	 * 			  the errors in training
	 */
	public NeuralNetLearner(IMetadataPreprocessor preprocessor,
			double learningRate, double momentum, int epochs, int batchSize,
			int hiddenNeurons, ErrorCalculationType errorType) {
		super(preprocessor);

		this.learningRate = learningRate;
		this.momentum = momentum;
		this.epochs = epochs;
		this.batchSize = batchSize;
		this.hiddenNeurons = hiddenNeurons;
		this.errorType = errorType;

		this.nets = new HashMap<String, NeuralNet>();
		this.oldInputs = new HashMap<String, ArrayList<double[]>>();
		this.oldOutputs = new HashMap<String, ArrayList<double[]>>();
	}

	@SuppressWarnings("unchecked")
	public void initLearner(Map<PlanOperator, PreprocessedMetadata> data) {
		for (Entry<String, Map<PlanOperator, PreprocessedMetadata>> testPlans : plansBySource(
				data).entrySet()) {
			try {
				String source = testPlans.getKey();
				this.nets.put(source, createNet());
				this.oldInputs.put(source, new ArrayList<double[]>());
				this.oldOutputs.put(source, new ArrayList<double[]>());

				train(source, testPlans.getValue().values());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Train a neural net on some data
	 * @param source the source the neural net is trained for
	 * @param data the trainingdata
	 */
	@SuppressWarnings("unchecked")
	private void train(String source, Collection<PreprocessedMetadata> data) {
		Attribute<?, Double>[] attributes = getAttributes();
		Attribute<?, Integer> classAttribute = getClassAttribute();
		double[][] inputData = new double[data.size()][attributes.length];
		double[][] desiredData = new double[data.size()][classAttribute
				.getClassValues().length];
		int i = 0;
		for (PreprocessedMetadata curValue : data) {
			for (int j = 0; j < attributes.length; ++j) {
				inputData[i][j] = attributes[j].classify(curValue);
			}

			desiredData[i][classAttribute.classify(curValue)] = 1;

			++i;
		}
		NeuralNet curNet = this.nets.get(source);

		int validationCount = this.epochs / 10;
		NeuralNet[] nets = new NeuralNet[10];

		List<double[]> inputs = this.oldInputs.get(source);
		inputs.clear();
		for (double[] curInput : inputData) {
			inputs.add(curInput);
		}

		List<double[]> outputs = this.oldOutputs.get(source);
		outputs.clear();
		for (double[] curOutput : desiredData) {
			outputs.add(curOutput);
		}

		double[][] testInputs = this.oldInputs.get(source).toArray(
				new double[0][0]);
		double[][] testOutputs = this.oldOutputs.get(source).toArray(
				new double[0][0]);
		curNet.removeAllListeners();
		Monitor m = curNet.getMonitor();
		m.setValidation(true);
		double minRMSE = Double.MAX_VALUE;

		for (i = 0; i < 10; ++i) {
			curNet.removeAllInputs();
			curNet.removeAllOutputs();
			m.setValidation(false);
			m.setLearning(true);
			JooneTools.train(curNet, inputData, desiredData, validationCount,
					0, 0, null, false);

			nets[i] = curNet.cloneNet();
			curNet.removeAllInputs();
			curNet.removeAllOutputs();
			m.setValidation(true);
			m.setLearning(false);
			// double rmse = medianSquareError(curNet, testInputs, testOutputs);
			double rmse = getError(curNet, testInputs, testOutputs);
			if (rmse < minRMSE) {
				minRMSE = rmse;
			}
		}

		this.nets.put(source, nets[nets.length - 1]);

		this.lastTime = System.currentTimeMillis();
	}

	/**
	 * Calculate the error for a number of classifications.
	 * The method of the calculation is determined by {@link ErrorCalculationType}.
	 * @param curNet
	 * @param testInputs
	 * @param testOutputs
	 * @return
	 */
	private double getError(NeuralNet curNet, double[][] testInputs, double[][] testOutputs) {
		switch(this.errorType) {
		case MEAN:
			return meanSquaredError(curNet, testInputs, testOutputs);
		case MEDIAN:
			return medianSquareError(curNet, testInputs, testOutputs);
		}
		return -1;
	}

	/**
	 * Calculate the mean squared error for a number of classifications.
	 * @param curNet the net to test
	 * @param testInputs the inputs
	 * @param testOutputs the desired outputs
	 * @return the mean squared error
	 */
	private double meanSquaredError(NeuralNet curNet, double[][] testInputs,
			double[][] testOutputs) {
		return JooneTools.test(curNet, testInputs, testOutputs);
	}

	/**
	 * Calculate the median of the squared errors for a number of classifications.
	 * @param net the net to test
	 * @param testInputs the inputs
	 * @param testOutputs the desired outputs
	 * @return the median of the squared errors
	 */
	private double medianSquareError(NeuralNet net, double[][] testInputs,
			double[][] testOutputs) {
		ArrayList<Double> mses = new ArrayList<Double>();
		for (int i = 0; i < testInputs.length; ++i) {
			mses.add(JooneTools.test(net, new double[][] { testInputs[i] },
					testOutputs));
		}
		Collections.sort(mses);
		int size = mses.size();
		return size % 2 == 0 ? (mses.get(size / 2) + mses.get(size / 2 - 1)) / 2
				: mses.get(size / 2);
	}

	private void connect(Layer layer1, Synapse synapse, Layer layer2) {
		layer1.addOutputSynapse(synapse);
		layer2.addInputSynapse(synapse);
	}

	@Override
	public double[] classify(String source, Map<Attribute, Object> values)
			throws Exception {
		NeuralNet nnet = nets.get(source);
		double[][] inputData = new double[1][getAttributes().length];
		for (int i = 0; i < getAttributes().length; ++i) {
			inputData[0][i] = (Double) values.get(getAttributes()[i]);
		}
		nnet.getMonitor().setValidation(true);
		nnet.getMonitor().setLearning(false);
		nnet.resetInput();
		nnet.removeAllInputs();
		nnet.removeAllOutputs();

		double[] out = JooneTools.interrogate(nnet, inputData[0]);
		// weight outputs/normalize to 0..1
		double sum = 0;
		for (int i = 0; i < out.length; ++i) {
			sum += out[i];
		}

		for (int i = 0; i < out.length; ++i) {
			out[i] /= sum;
		}
		return out;
	}

	public void processedRequest(String source, PreprocessedMetadata request) {
		ArrayList<PreprocessedMetadata> requests = new ArrayList<PreprocessedMetadata>(
				1);
		requests.add(request);
		train(source, requests);
	}

	public void requestCountReached() {
		List<PlanOperator> plans = preprocessor.getPlans(lastTime);
		try {
			Map<PlanOperator, PreprocessedMetadata> metadata = preprocessor
					.getMetadata(plans);

			Map<String, Map<PlanOperator, PreprocessedMetadata>> data = plansBySource(metadata);
			for (Entry<String, Map<PlanOperator, PreprocessedMetadata>> curSource : data
					.entrySet()) {
				String source = curSource.getKey();
				if (!this.nets.containsKey(source)) {
					this.nets.put(source, createNet());
					this.oldInputs.put(source, new ArrayList<double[]>());
					this.oldOutputs.put(source, new ArrayList<double[]>());
				}
				train(source, curSource.getValue().values());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private NeuralNet createNet() {
		Attribute<?, Double>[] attributes = getAttributes();
		Attribute<?, Integer> classAttribute = getClassAttribute();

		LinearLayer inputLayer = new LinearLayer();
		SigmoidLayer hiddenLayer = new SigmoidLayer();
		SigmoidLayer outputLayer = new SigmoidLayer();

		inputLayer.setRows(attributes.length);
		hiddenLayer.setRows(this.hiddenNeurons);
		outputLayer.setRows(classAttribute.getClassValues().length);

		FullSynapse synIH = new FullSynapse();
		FullSynapse synHO = new FullSynapse();

		this.connect(inputLayer, synIH, hiddenLayer);
		this.connect(hiddenLayer, synHO, outputLayer);

		NeuralNet nnet = new NeuralNet();

		nnet.addLayer(inputLayer, NeuralNet.INPUT_LAYER);
		nnet.addLayer(hiddenLayer, NeuralNet.HIDDEN_LAYER);
		nnet.addLayer(outputLayer, NeuralNet.OUTPUT_LAYER);
		nnet.randomize(0.05);

		Monitor m = nnet.getMonitor();
		m.setSupervised(true);
		m.setSingleThreadMode(false);
		m.setLearningRate(this.learningRate);
		m.setMomentum(this.momentum);
		m.setSingleThreadMode(false);
		if (batchSize > 1) {
			m.addLearner(0, "org.joone.engine.BatchLearner");
			m.setLearningMode(0);
			m.setBatchSize(this.batchSize);
		}
		m.setValidation(false);
		m.setTrainingDataForValidation(true);
		return nnet;
	}
}
