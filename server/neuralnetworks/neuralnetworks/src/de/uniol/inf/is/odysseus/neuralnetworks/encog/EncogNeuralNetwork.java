package de.uniol.inf.is.odysseus.neuralnetworks.encog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.directory.SchemaViolationException;

import org.encog.engine.network.activation.ActivationCompetitive;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.engine.network.activation.ActivationRamp;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationStep;
import org.encog.mathutil.rbf.RBFEnum;
import org.encog.ml.BasicML;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.data.folded.FoldedDataSet;
import org.encog.ml.factory.MLActivationFactory;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;
import org.encog.ml.train.BasicTraining;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.cross.CrossValidationKFold;
import org.encog.neural.networks.training.propagation.Propagation;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.manhattan.ManhattanPropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.neural.pattern.NeuralNetworkPattern;
import org.encog.neural.pattern.SOMPattern;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodBubble;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodFunction;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodRBF1D;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodSingle;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.INeuralNetwork;
import de.uniol.inf.is.odysseus.neuralnetworks.util.DataConverter;
import de.uniol.inf.is.odysseus.neuralnetworks.util.DataNormalizer;

/**
 * Implements {@link INeuralNetwork} with the Encog neural network library.
 * Supports classification, regression and clustering tasks. Current network 
 * types include feed forward networks and self organising maps.
 * 
 * @author Jens Plümer
 *
 * Created at 17.05.16
 */
public class EncogNeuralNetwork<M extends ITimeInterval> implements INeuralNetwork<M>
{

    private BasicML                         network;
    private NeuralNetworkPattern            structure;
    private SDFSchema                       schema;
    private SDFAttribute                    clazz;
    private Map<SDFAttribute, List<String>> nominals;
    private Map<String, Double[]>           encodedNominals;
    
    private int     kFolds;
    private int     clazzCount;
    private int     iterations     = 1000;
    private int     x_dimension;
    private int     y_dimension;
    private double  error          = 100.0;
    private double  supposed_error = 0.01;
    private double  learnrate;
    private double  nHigh;
    private double  nLow;
    private double  bias;
    private double  delta;
    private double startRate;
    private double endRate;
    private double startRadius;
    private double endRadius;
    private boolean multithreaded;
    private boolean crossvalidation;

    private Task                 task;
    private MLTrain              basictraining;
    private String               trainingMethod;
    private List<List<Tuple<M>>> buffer = new ArrayList<>(10000);
    private NeighborhoodFunction neighborhoodfunction;
    private BasicMLDataSet       trainingsdata;
    
    public EncogNeuralNetwork() {}
    public EncogNeuralNetwork<M> createInstance() { return new EncogNeuralNetwork<>(); }

    @Override
    public void init(final int inputNeurons,
                     final int outputNeurons,
                     final String type,
                     final SDFSchema schema,
                     final SDFAttribute clazz,
                     final Map<SDFAttribute, List<String>> nominals,
                     final Task mlProblem) throws Exception
    {
        this.schema          = schema;
        this.clazz           = clazz;
        this.nominals        = nominals;
        this.task            = mlProblem;
        this.kFolds          = Integer.parseInt(EncogOptions.getDefaultValue("k-folds"));//3;
        this.delta           = Double.parseDouble(EncogOptions.getDefaultValue("delta"));//0.1;
        this.learnrate       = Double.parseDouble(EncogOptions.getDefaultValue("learnrate"));//0.1;
        this.bias            = Double.parseDouble(EncogOptions.getDefaultValue("bias"));//1.0;
        this.crossvalidation = Boolean.parseBoolean(EncogOptions.getDefaultValue("crossvalidation"));//true;
        this.multithreaded   = Boolean.parseBoolean(EncogOptions.getDefaultValue("multithreaded"));//true;

        switch(task)
        {
        case CLASSIFICATION:
        case REGRESSION:
            switch(type.toLowerCase())
            {
            case MLMethodFactory.TYPE_FEEDFORWARD: // type = feedforward
                this.structure = new FeedForwardPattern();
                this.nLow  = 0.0;
                this.nHigh = 1.0;
                this.structure.setActivationFunction(new ActivationSigmoid());
                this.trainingMethod = MLTrainFactory.TYPE_BACKPROP;
                break;
            default: throw new IllegalArgumentException("given type "+type+ " is not a valid neural network "
                                                        + "type for encog within a classifciation/regression problem");
            }
            break;
        case CLUSTERING:
            switch(type.toLowerCase())
            {
            case MLMethodFactory.TYPE_SOM:       // type = som
                this.structure = new SOMPattern();
                this.neighborhoodfunction = new NeighborhoodRBF(RBFEnum.Gaussian, 10 , 10);
                this.trainingMethod = MLTrainFactory.TYPE_SOM_CLUSTER;
                this.startRate   = Double.parseDouble(EncogOptions.getDefaultValue("startRate"));
                this.endRate     = Double.parseDouble(EncogOptions.getDefaultValue("endRate"));
                this.startRadius = Double.parseDouble(EncogOptions.getDefaultValue("startRadius"));
                this.endRadius   = Double.parseDouble(EncogOptions.getDefaultValue("endRadius"));
                this.y_dimension = Integer.parseInt(EncogOptions.getDefaultValue("y_dimension"));
                this.x_dimension = Integer.parseInt(EncogOptions.getDefaultValue("x_dimension"));
            break;
            default: throw new IllegalArgumentException("given type "+type+ " is not a valid neural network "
                                                        + "type for encog within a clustering problem");
            }
            break;
        default: throw new IllegalAccessException("given task "+this.task.toString()+" is not a valid type:"
                                                  +"choose between " + Task.values());
        }
        this.structure.setInputNeurons (inputNeurons);
        this.structure.setOutputNeurons(outputNeurons);
    }

    @Override
    public void setOptions(Map<String, String> options, List<Integer> hiddenLayers) throws SchemaViolationException
    {
        // Add hidden layers if available
        if(hiddenLayers != null && !hiddenLayers.isEmpty()) 
        { for(int i : hiddenLayers) { this.structure.addHiddenLayer(i); }}
        if(options != null)
        {
            // Set further arguments:
            for(String i : options.keySet())
            {
                try
                {
                    switch(i.toLowerCase())
                    {
                    case "activation":
                        setActivationFunction(options, i);
                    break;
                    case "neighborhood":
                        setNeighborhoodFunction(options, i);
                    break;
                    case "multithreaded":
                        if (options.get(i.toLowerCase()).equalsIgnoreCase("false"))
                            this.multithreaded = false;
                    break;
                    case "learnrate":
                        this.learnrate = Double.parseDouble(options.get(i.toLowerCase()));
                        if(learnrate < 0)
                            throwIllegalArgumentException("LEARNRATE", learnrate);
                    break;
                    case "delta":
                        this.delta = Double.parseDouble(options.get(i.toLowerCase()));
                        if(delta < 0)
                            throwIllegalArgumentException("DELTA", delta);
                    break;
                    case "k-folds":
                        this.kFolds = Integer.parseInt(options.get(i.toLowerCase()));
                        if(kFolds < 1)
                            throwIllegalArgumentException("KFOLDS", kFolds);
                    break;
                    case "crossvalidation":
                        if (options.get(i.toLowerCase()).equalsIgnoreCase("false"))
                            crossvalidation = false;
                    break;
                    case "iterations":
                        this.iterations = Integer.parseInt(options.get(i.toLowerCase()));
                        if(iterations < 1)
                            throwIllegalArgumentException("ITERATIONS", iterations);
                    break;
                    case "error":
                        this.supposed_error = Double.parseDouble(options.get(i.toLowerCase()));
                        if(supposed_error < 0.0 || supposed_error > 1.0)
                            throwIllegalArgumentException("ERROR", supposed_error);
                    break;
                    case "bias":
                        this.bias = Double.parseDouble(options.get("bias"));
                    break;
                    case "x_dimension":
                        int x_val = Integer.parseInt(options.get(i.toLowerCase()));
                        if(x_val >= 0)
                            this.x_dimension = x_val;
                        else
                            throwIllegalArgumentException(i, options.get(i));
                    break;
                    case "y_dimension":
                        int y_val = Integer.parseInt(options.get(i.toLowerCase()));
                        if(y_val >= 0)
                            this.y_dimension = y_val;
                        else
                            throwIllegalArgumentException(i, options.get(i));
                    break;
                    case "training":
                        if(!task.equals(Task.CLUSTERING))
                        {
                            switch(options.get(i.toLowerCase()))
                            {
                            case MLTrainFactory.TYPE_BACKPROP: //backprop
                                this.trainingMethod = MLTrainFactory.TYPE_BACKPROP;
                            break;
                            case MLTrainFactory.TYPE_MANHATTAN: //manhatten
                                this.trainingMethod = MLTrainFactory.TYPE_MANHATTAN;
                            break;
                            case MLTrainFactory.TYPE_RPROP: //rprop
                                this.trainingMethod = MLTrainFactory.TYPE_RPROP;
                            break;
                            default:
                                throwIllegalArgumentException(i, options.get(i));
                            }
                        }
                    break;
                    case "startrate":
                        this.startRate = Double.parseDouble(options.get(i.toLowerCase()));
                    break;
                    case "endrate":
                        this.endRate = Double.parseDouble(options.get(i.toLowerCase()));
                    break;
                    case "startradius":
                        this.startRadius = Double.parseDouble(options.get(i.toLowerCase()));
                    break;
                    case "endradius":
                        this.endRadius = Double.parseDouble(options.get(i.toLowerCase()));
                    break;
                    default:
                        throwIllegalArgumentException(i, options.get(i));
                    }
                }
                catch (ParseException e) { throw new ParseException("Caused by "+i); }
            }
        }
        switch (task)
        {
        case CLASSIFICATION:
            network = (BasicNetwork) structure.generate();
            final String err1 = "Amount of classes does not match the number of output neurons: "
                            + "because classes must be encoded by the out-of-n method."
                            + "Please check your nominal definitions.";
            final String err2 = "No classes could be found in the nominal defintion: "
                            + "Please check your nominal definitions.";
            
            if(nominals != null)
            {
                clazzCount = nominals.get(clazz).size();
                if(clazzCount != ((BasicNetwork) network).getOutputCount())
                { throw new SchemaViolationException(err1); }
                encodedNominals = DataNormalizer.getInstance().encodeNominals(nominals, clazz, nLow, nHigh);
            }
            else { throw new SchemaViolationException(err2); }
            ((BasicNetwork)network).reset();
            ((BasicNetwork) network).setBiasActivation(bias);
        break;
        case REGRESSION:
            network = (BasicNetwork) structure.generate();
            clazzCount = 1;
            ((BasicNetwork) network).reset();
            ((BasicNetwork) network).setBiasActivation(bias);
        break;
        case CLUSTERING:
            network    = (SOM) this.structure.generate();
            clazzCount = ((SOM) network).getOutputCount();
            ((SOM) network).reset();
        break;
        }
    }

    @Override
    public INeuralNetwork<M> iterate()
    {
        basictraining.iteration();
        basictraining.finishTraining();
        error = basictraining.getError();
        if(task.equals(Task.CLUSTERING))
        {
            ((BasicTrainSOM) basictraining).autoDecay();
        }
        return this;
    }

    @Override
    public Object compute(Tuple<M> data)
    {
        if(data != null)
        {
            switch(task)
            {
            case CLUSTERING:     return cluster(data);
            case REGRESSION:     return regression(data);
            case CLASSIFICATION: return classify(data);
            }
        }
        return null;
    }

    public void reset()
    {
        switch(task)
        {
        case CLASSIFICATION:
        case REGRESSION:
            ((BasicNetwork) network).reset();
        break;
        case CLUSTERING:
            ((SOM) network).reset();
        break;
        }
    }

    public void removeTraingingssetFromBuffer(int index)     { buffer.remove(index); }
    public void addTrainingssetToBuffer(List<Tuple<M>> data) { buffer.add(data); }

    public void prepareTraining()
    {
        List<MLDataPair> trainingsset = new LinkedList<>();
        for(List<Tuple<M>> l : buffer)
            for(MLDataPair p : convertData(l).getData())
                trainingsset.add(p);
        this.trainingsdata            = new BasicMLDataSet(trainingsset);
        final FoldedDataSet folded    = new FoldedDataSet(this.trainingsdata);
        if(crossvalidation && !(Task.CLUSTERING.equals(task)))
        {
            basictraining =
                    new CrossValidationKFold(getTrainingsmethod(folded), kFolds);
        }
        else
        {
            basictraining = getTrainingsmethod(folded);
        }
    }

    public Task getMLTask()             { return task; }
    public int getIterations()          { return iterations; }
    public double getSupposedError()    { return supposed_error; }
    public double getCurrentError()     { return error; }
    public List<List<Tuple<M>>> getBuffer() { return buffer; }
    public String getName()             { return "Encog"; }

    @Override
    public int setBuffer(List<List<Tuple<M>>> buffer)
    {
        this.buffer = buffer;
        return buffer.size();
    }

    @Override
    public void clear()
    {
        buffer.clear();
        error = 100.0;
        trainingsdata = null;
        reset();
    }
    private void setActivationFunction(Map<String, String> options, String i)
    {
        MLActivationFactory factory = new MLActivationFactory();
        switch (options.get(i).toLowerCase())
        {
        case MLActivationFactory.AF_STEP:// step
            if (options.containsKey("low")
                && options.containsKey("center")
                && options.containsKey("high"))
            {
                double low = Double.parseDouble(options.get("low"));
                double center = Double.parseDouble(options.get("center"));
                double high = Double.parseDouble(options.get("high"));
                this.nLow = low;
                this.nHigh = high;
                this.structure.setActivationFunction(new ActivationStep(low, center, high));
            }
            else
                this.structure.setActivationFunction(new ActivationStep());
        break;
        case MLActivationFactory.AF_TANH:// tanh
            this.nLow = -1.0;
            this.nHigh = 1.0;
            this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_TANH));
        break;
        case MLActivationFactory.AF_LOG:// log
            this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_LOG));
        break;
        case MLActivationFactory.AF_SIGMOID:// ssigmoid
            this.nLow = 0.0;
            this.nHigh = 1.0;
            this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_SIGMOID));
        break;
        // case MLActivationFactory.AF_SSIGMOID://ssigmoid
        // this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_SSIGMOID));
        // break;
        case MLActivationFactory.AF_BIPOLAR:// biploar
            this.nLow = -1.0;
            this.nHigh = 1.0;
            this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_BIPOLAR));
        break;
        case MLActivationFactory.AF_COMPETITIVE:// comp
            this.nLow = -1.0;
            this.nHigh = 1.0;
            this.structure.setActivationFunction(new ActivationCompetitive());
        break;
        case MLActivationFactory.AF_GAUSSIAN:// gauss
            this.nLow = 0.0;
            this.nHigh = 1.0;
            this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_GAUSSIAN));
        break;
        case MLActivationFactory.AF_RAMP:// ramp
            if (options.containsKey("lowThreshold") && options.containsKey("highThreshold")
                    && options.containsKey("lowOutput") && options.containsKey("highOutput"))
            {
                double thresholdLow = Double.parseDouble(options.get("lowThreshold"));
                double thresholdHigh = Double.parseDouble(options.get("highThreshold"));
                double low = Double.parseDouble(options.get("lowOutput"));
                double high = Double.parseDouble(options.get("highOutput"));
                this.nHigh = high;
                this.nLow = low;
                ActivationFunction actFunc = new ActivationRamp(thresholdHigh, thresholdLow, high, low);
                this.structure.setActivationFunction(actFunc);
            }
            this.nHigh = 3;
            this.nLow = 2;
            this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_RAMP));
        break;
        case MLActivationFactory.AF_SIN:// sin
            this.nLow = -1.0;
            this.nHigh = 1.0;
            this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_SIN));
        break;
        case MLActivationFactory.AF_SOFTMAX:// softmax
            this.nLow = 0.0;
            this.nHigh = 1.0;
            this.structure.setActivationFunction(factory.create(MLActivationFactory.AF_SOFTMAX));
        break;
        default:
            throwIllegalArgumentException("ACTIVATION", options.get(i));
        }
    }
    private void setNeighborhoodFunction(Map<String, String> options, String i)
    {
    
        NeighborhoodFunction nf = null;
        RBFEnum t = RBFEnum.Gaussian;
        switch (options.get(i).toLowerCase())
        {
        case "bubble" :
            nf = new NeighborhoodBubble(1);
        break;
        case "rbf1d" :
            nf = new NeighborhoodRBF1D(t);
        break;
        case "single" :
            nf = new NeighborhoodSingle();
        break;
        case "rbf" :
        nf = new NeighborhoodRBF(t, x_dimension, y_dimension);
        break;
        }
        this.neighborhoodfunction = nf;
    }
    private void throwIllegalArgumentException(String name, Object obj)
    {
        if(EncogOptions.getInstance().contains(name))
        {
            throw new IllegalArgumentException("value of "+name.toLowerCase()+" is invalid:" + obj.toString()+ "\n"
                    +"try some of these values\n" + EncogOptions.getInstance().toString(name.toLowerCase()));
        }
        else
        {
            throw new IllegalArgumentException("argument "+name.toLowerCase()+" does not exist:\n"
                    + "here is a list of all arguments for the Encog implementation \n"
                    + EncogOptions.getInstance().toString());
        }
    }
    
    @SuppressWarnings("unchecked")
    private Object[] convertDataRegression(Tuple<M> data)
    {
        Object[] r = new Object[5];
        
        List<Tuple<M>> l = new ArrayList<>();
        l.add(data);
        BasicMLDataSet dataset = null;

        Object[] obj2 = DataConverter.getInstance().convert(l,
                                                           this.schema,
                                                           this.clazz,
                                                           this.clazzCount,
                                                           this.encodedNominals);
        double[][] tData = (double[][]) obj2[0];
        double[][] iData = (double[][]) obj2[1];
        tData = DataNormalizer.getInstance().normalise(tData,
                                                       (double) obj2[2],
                                                       (double) obj2[3],
                                                       this.nHigh,
                                                       this.nLow);

        iData = DataNormalizer.getInstance().normalise(iData,
                                                       (double) obj2[2],
                                                       (double) obj2[3],
                                                       this.nHigh,
                                                       this.nLow);
        
        dataset = new BasicMLDataSet(tData, iData);
        r[0] = dataset;
        r[1] = (double) obj2[2];//actualHigh
        r[2] = (double) obj2[3];//actualLow
        r[3] = this.nHigh;
        r[4] = this.nLow;
        return r;
    }
    
    private MLData convertData(Tuple<M> data)
    {
        List<Tuple<M>> l = new ArrayList<>();
        l.add(data);
        return convertData(l).get(0).getInput();
    }
    
    private BasicMLDataSet convertData(List<Tuple<M>> data)
    {
        BasicMLDataSet dataset = null;
        if (!data.isEmpty())
        {
    
            switch(task)
            {
            case CLUSTERING:
                @SuppressWarnings("unchecked")
                Object[] obj = DataConverter.getInstance().convertWithoutIdealData(data,
                                                                                   this.schema,
                                                                                   this.clazz,
                                                                                   this.clazzCount,
                                                                                   this.encodedNominals);
                double[][] cData = (double[][]) obj[0];
                dataset =  new BasicMLDataSet(cData, null);
            break;
            case REGRESSION:
            case CLASSIFICATION:
                @SuppressWarnings("unchecked")
                Object[] obj2 = DataConverter.getInstance().convert(data,
                                                                   this.schema,
                                                                   this.clazz,
                                                                   this.clazzCount,
                                                                   this.encodedNominals);
                double[][] tData = (double[][]) obj2[0];
                double[][] iData = (double[][]) obj2[1];
                tData = DataNormalizer.getInstance().normalise(tData,
                                                               (double) obj2[2],
                                                               (double) obj2[3],
                                                               this.nHigh,
                                                               this.nLow);
                if(task.equals(Task.REGRESSION))
                {
                    iData = DataNormalizer.getInstance().normalise(iData,
                                                               (double) obj2[2],
                                                               (double) obj2[3],
                                                               this.nHigh,
                                                               this.nLow);
                }
                dataset = new BasicMLDataSet(tData, iData);
            break;
            }
        }
        return dataset;
    }
    
    private int cluster(Tuple<M> data)
    {
        MLData input = convertData(data);
        int output = ((SOM) this.network).classify(input);
        return output;
    }
    
    private double regression(Tuple<M> data)
    {
        Object[] obj  = convertDataRegression(data);
        MLData input  = ((BasicMLDataSet) obj[0]).get(0).getInput();
        MLData output = ((BasicNetwork) this.network).compute(input);
        double result = DataNormalizer.getInstance().denormalise(output.getData(0), 
                                                                  (Double) obj[1], 
                                                                  (Double) obj[2], 
                                                                  (Double) obj[3], 
                                                                  (Double) obj[4]);
        return result;
    }
    
    private String classify(Tuple<M> data)
    {
        MLData input  = convertData(data);
        MLData output = ((BasicNetwork) this.network).compute(input);
        String clazz  = DataNormalizer.getInstance().decodeNominals(this.encodedNominals,
                                                                    output.getData(),
                                                                    this.nHigh ,
                                                                    this.nLow);
        return clazz;
    }
    
    private BasicTraining getTrainingsmethod(final FoldedDataSet folded)
    {
        final BasicTraining method;
        switch(trainingMethod)
        {
        case MLTrainFactory.TYPE_BACKPROP: //backprop
            method= new Backpropagation((BasicNetwork)
                    this.network,
                    folded,
                    this.learnrate,
                    this.delta);
            break;
        case MLTrainFactory.TYPE_MANHATTAN: //manhatten
            method=new ManhattanPropagation((BasicNetwork)
                    this.network,
                    folded,
                    this.learnrate);
            break;
        case MLTrainFactory.TYPE_RPROP: //rprop
            method= new ResilientPropagation((BasicNetwork)
                    this.network,
                    folded);
            break;
        case MLTrainFactory.TYPE_SOM_CLUSTER :
            method = new BasicTrainSOM((SOM) this.network,
                    this.learnrate,
                    folded,
                    this.neighborhoodfunction);
            ((BasicTrainSOM )method).setAutoDecay(iterations, startRate, endRate, startRadius, endRadius);
        break;
        default : method= new Backpropagation((BasicNetwork)
                            this.network,
                            folded,
                            this.learnrate,
                            this.delta);
        }
        switch(task)
        {
        case REGRESSION:
        case CLASSIFICATION:
            if(!multithreaded) ((Propagation) method).setThreadCount(1);
            else               ((Propagation) method).setThreadCount(0);
        break;
        case CLUSTERING:
//                if(!multithreaded) ((Propagation) method).setThreadCount(1);
//                else               ((Propagation) method).setThreadCount(0);
        break;
        }
        return method;
    }
}
