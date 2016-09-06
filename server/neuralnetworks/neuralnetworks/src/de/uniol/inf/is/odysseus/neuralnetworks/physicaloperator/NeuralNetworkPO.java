package de.uniol.inf.is.odysseus.neuralnetworks.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.INeuralNetwork;

public class NeuralNetworkPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>>
{

    protected final IMetadataMergeFunction<M>      metadataMerge;
    
    private final List<Tuple<M>> BUFFER = new ArrayList<Tuple<M>>(100000);
    private final ITransferArea<Tuple<M>, Tuple<M>>transferFunction;
    private int networkAttribute;
    private boolean block = false;
    
    public NeuralNetworkPO(int                               indexOfNetwork, 
                           IMetadataMergeFunction<M>         metadataMerge, 
                           ITransferArea<Tuple<M>, Tuple<M>> transferFunction)
    {
        this.networkAttribute = indexOfNetwork;
        this.metadataMerge    = metadataMerge;
        this.transferFunction = transferFunction;
    }
    
    public NeuralNetworkPO(NeuralNetworkPO<M> op)
    {
        this.networkAttribute = op.networkAttribute;
        this.metadataMerge    = op.metadataMerge.clone();
        this.transferFunction = op.transferFunction.clone();
    }
    
    @Override
    protected void process_open() throws OpenFailedException
    {
        super.process_open();
        this.metadataMerge.init();
        this.transferFunction.init(this, getSubscribedToSource().size());
    }

    INeuralNetwork<M> network;
    
    @Override
    protected void process_next(Tuple<M> tuple, int port)
    {
    	synchronized(BUFFER)
    	{
    	    Iterator<Tuple<M>> iter;
    	    switch(port)
    	    {
    	    case 1:
    	        network = tuple.getAttribute(networkAttribute);
                iter = BUFFER.iterator();
                while(iter.hasNext())
                {
                    Tuple<M> element = iter.next();
                    computeAndTransfer(element);
                    iter.remove();
                }
    	    break;
    	    case 0: 
    	        BUFFER.add(tuple);
                if(!block && network != null)
                {
                    iter = BUFFER.iterator();
                    while(iter.hasNext())
                    {
                        Tuple<M> element = iter.next();
                        computeAndTransfer(element);
                        iter.remove();
                    }
                }
    	    break;
    	    }
    	}
    }
    
    protected void computeAndTransfer(Tuple<M> tuple) 
    {
        Object clazz      = network.compute(tuple);
        Tuple<M> newTuple = tuple.append(clazz);
        transfer(newTuple);
    }
    
    @Override
    protected void process_close()
    {
        synchronized (BUFFER)
        {
            if (!BUFFER.isEmpty())
            {
                Iterator<Tuple<M>> iter = BUFFER.iterator();
                while (iter.hasNext())
                {
                    Tuple<M> element = iter.next();
                    if(element != null)
                    {
                        computeAndTransfer(element);
                        iter.remove();
                    }
                }
            }
        }
    }
    
    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {}

    @Override
    public OutputMode getOutputMode() { return OutputMode.NEW_ELEMENT; }

}
