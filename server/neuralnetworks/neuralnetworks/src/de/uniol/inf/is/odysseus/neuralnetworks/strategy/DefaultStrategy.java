package de.uniol.inf.is.odysseus.neuralnetworks.strategy;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.INeuralNetwork;

/**
 * Implements {@link IStrategy}. This strategy is called when the maximum
 * capacity of the buffer is reached. Only the oldest chunks are deposed. The
 * amount of the discarding can be controlled with {@link DefaultStrategy#FFACTOR}.  
 * 
 * @author Jens Plümer
 *
 * Created at 30.06.16
 */
public class DefaultStrategy<M extends ITimeInterval> implements IStrategy<M>
{

    protected int MAX_CHUNK         = 100;
    protected int MIN_CHUNK         = 1;
    /**
     * Defines the number of chunks that have to be discarded. 
     */
    protected int FFACTOR           = 1;
    
    public DefaultStrategy() {} 
    
    public DefaultStrategy(Map<String, String> options)
    {
        if(options != null)
        {    
            for(Entry<String, String> e : options.entrySet())
            {
                switch(e.getKey().toLowerCase())
                {
                case "max_chunk":   MAX_CHUNK = Integer.parseInt(e.getValue());  break;
                case "min_chunk":   MIN_CHUNK = Integer.parseInt(e.getValue());  break;
                case "ffactor":       FFACTOR = Integer.parseInt(e.getValue());  break;
                }
            }
            if(MIN_CHUNK < 1 || MIN_CHUNK > MAX_CHUNK) throw new IllegalArgumentException("MIN_CHUNK of " + MIN_CHUNK +" is invalid");
            if(MAX_CHUNK < 1) throw new IllegalArgumentException("MAX_CHUNK of " + MAX_CHUNK +" is invalid");
            if(FFACTOR < 1) throw new IllegalArgumentException("FFACTOR of " + FFACTOR +" is invalid");
        }
    }

    
    @Override
    public int getMaxChunk()  { return MAX_CHUNK; }
    @Override
    public int getMinChunk()  { return MIN_CHUNK; }
    @Override
    public int getFFactor()   { return FFACTOR; }
    
    @Override
    public boolean forgettingTriggered(final List<List<Tuple<M>>> chunkbuffer, final INeuralNetwork<M> network)
    {
        return chunkbuffer.size() == MAX_CHUNK;
    }

    @Override
    public List<List<Tuple<M>>> executeForgetting(final List<List<Tuple<M>>> chunkbuffer)
    {
        for(int i = 0; i < FFACTOR; i++)
        {
            if(!chunkbuffer.isEmpty()) chunkbuffer.remove(0); 
        }
        return chunkbuffer;
    }

    @Override
    public String getName() { return "default"; }

    @Override
    public IStrategy<M> createInstance(Map<String, String> options)
    {
        return new DefaultStrategy<>(options);
    }
    
    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append("strategy "+getName()+" with following options:\n");
        b.append("MAX_CHUNK: " + getMaxChunk()+"\n");
        b.append("MIN_CHUNK: " + getMinChunk()+"\n");
        b.append("FFACTOR: " + getFFactor()+"\n");
        return b.toString();
    }


    @Override
    public void clear() {}


    @Override
    public boolean conceptDriftAppeared(List<List<Tuple<M>>> chunkbuffer, INeuralNetwork<M> network)
    {
        return false;
    }


    @Override
    public List<List<Tuple<M>>> handleConceptDrift(List<List<Tuple<M>>> chunkbuffer)
    {
        throw new IllegalStateException("there is no explicit concept drift handling for this strategy");
    }

}
