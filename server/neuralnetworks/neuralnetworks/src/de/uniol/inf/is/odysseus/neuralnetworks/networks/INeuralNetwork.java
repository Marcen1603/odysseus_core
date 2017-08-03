package de.uniol.inf.is.odysseus.neuralnetworks.networks;

import java.util.List;
import java.util.Map;

import javax.naming.directory.SchemaViolationException;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Defines a neural network.
 * 
 * @author Jens Plümer
 * 
 * Created at 17.05.16
 */
public interface INeuralNetwork<M extends ITimeInterval>
{
    
    /**
     * Defines the machine learning task that a neural network can perform. 
     * <pre>
     *  CLASSIFICATION
     *  REGRESSION
     *  CLUSTERING
     * </pre>
     */
    public enum Task
    {
        CLASSIFICATION,
        REGRESSION,
        CLUSTERING,
        ;
    }
    
    public INeuralNetwork<M> createInstance();
    public void init(final int inputNeurons,
                     final int outputNeurons, 
                     final String type, 
                     final SDFSchema schema, 
                     final SDFAttribute clazz, 
                     final Map<SDFAttribute, List<String>> nominals,
                     final Task task) 
                             throws Exception;
    public void setOptions(final Map<String, String> options, 
                           final List<Integer> hiddenLayers) 
                                   throws SchemaViolationException;
    
    /**
     * Is executed before the actual training starts. Prepare here
     * the network for the training phase (i.e. convert given 
     * training set to the supported data format). 
     */
    public void prepareTraining();
    /**
     * Executes a single training iteration and 
     * returns the trained neural network.
     */
    public INeuralNetwork<M> iterate();
    public void reset();
    /**
     * Computes a result for a given tuple. Can be a class (classification task),
     * a double value (regression task) or a cluster name (cluster task).
     * 
     * @param data Tuple
     * @return result
     */
    public Object compute(final Tuple<M> data);

    /**
     * Specifies the name of the used framework. Is used to
     * identify the used framework in the PQL request as follows:
     * <pre>
     * {@code
     *  learner = NEURALNETWORK_LEARNER(
     *                                   {
     *                                      ..
     *                                      framework = 'name';
     *                                      ..
     *                                   },
     *                                   source
     *                                  )
     * }
     *</pre> 
     * @return name String
     */
    public String getName();
    /**
     * Have to return the error from the last training iteration.
     * @return double Error
     */
    public double getCurrentError();
    public double getSupposedError();
    public Task getMLTask();
    public int getIterations();

    /**
     * Removes a chunk of training data form the training set buffer.
     * In general has to be implemented as follows:
     * <pre>
     * {@code
     * public void removeTraingingssetFromBuffer(final int index)
     * {
     *      buffer.remove(int);
     * }
     * </pre>
     * Otherwise there is no guarantee that the implemented network
     * will be operate as {@link NeuralNetworkLearnerPO} demands. 
     * @param index The index of the chunk to remove
     */
    public void removeTraingingssetFromBuffer(final int index);
    /**
     * Adds a chunk of training data to the training set buffer.
     * In general has to be implemented as follows:
     * <pre>
     * {@code
     * public void addTrainingssetToBuffer(final List<Tuple<M>> data)
     * {
     *      buffer.add(data);
     * }
     * </pre>
     * Otherwise there is no guarantee that the implemented network
     * will be operate as {@link NeuralNetworkLearnerPO} demands. 
     * @param data A chunk of training data 
     */
    public void addTrainingssetToBuffer(final List<Tuple<M>> data); 
    /**
     * Sets a new buffer with a training set.
     * In general has to be implemented as follows:
     * <pre>
     * {@code
     * public void setBuffer(List<List<Tuple<M>>> buffer)
     * {
     *      this.buffer = buffer;
     *      return buffer.size();
     * }
     * </pre>
     * Otherwise there is no guarantee that the implemented network
     * will be operate as {@link NeuralNetworkLearnerPO} demands. 
     * @param buffer
     * @return size of the buffer
     */
    public int setBuffer(List<List<Tuple<M>>> buffer);
    /**
     * Return the current buffer.
     * <pre>
     * {@code
     * public List<List<Tuple<M>>> getBuffer()
     * {
     *      return this.buffer;
     * }
     * </pre>
     * Otherwise there is no guarantee that the implemented network
     * will be operate as {@link NeuralNetworkLearnerPO} demands. 
     * @param buffer
     * @return size of the buffer
     */
    public List<List<Tuple<M>>> getBuffer();
    /**
     * Clears all memory allocated to this class.
     */
    public void clear();
    
}
