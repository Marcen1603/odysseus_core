package de.uniol.inf.is.odysseus.neuralnetworks.strategy;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.INeuralNetwork;
import de.uniol.inf.is.odysseus.neuralnetworks.physicaloperator.NeuralNetworkLearnerPO;

/**
 * Defines a strategy for discarding chunks.
 * 
 * @author Jens Plümer
 *
 * Created at 30.06.16
 */
public interface IStrategy<M extends ITimeInterval>
{
    public int getMaxChunk  ();
    public int getMinChunk  ();
    public int getFFactor   ();
    
    /**
     * Determines if chunks need to be discarded (e.g. when the buffer capacity is reached). 
     * Is called by {@link NeuralNetworkLearnerPO}.
     */
    public boolean forgettingTriggered(final List<List<Tuple<M>>> chunkbuffer, final INeuralNetwork<M> network);
    /**
     * Implements the discarding mechanism. Is called by {@link NeuralNetworkLearnerPO}.
     * 
     * @param current chunk buffer
     * @return have to return the size of the adapted buffer
     */
    public List<List<Tuple<M>>> executeForgetting(final List<List<Tuple<M>>> chunkbuffer);
    /**
     * 
     * @return
     */
    public boolean conceptDriftAppeared(final List<List<Tuple<M>>> chunkbuffer, final INeuralNetwork<M> network);
    /**
     * 
     * @param chunkbuffer
     * @return
     */
    public List<List<Tuple<M>>> handleConceptDrift(final List<List<Tuple<M>>> chunkbuffer);
    
    public IStrategy<M> createInstance(final Map<String, String> options);
    public String getName();
    public void clear();

    

}
