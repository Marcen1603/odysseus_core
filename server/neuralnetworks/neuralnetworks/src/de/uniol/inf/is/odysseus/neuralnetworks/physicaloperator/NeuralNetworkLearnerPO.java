package de.uniol.inf.is.odysseus.neuralnetworks.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.neuralnetworks.networks.INeuralNetwork;
import de.uniol.inf.is.odysseus.neuralnetworks.strategy.IStrategy;

/**
 * 
 * @author Jens Plümer
 * 
 * Created at 17.05.16
 */
public class NeuralNetworkLearnerPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>>
{
    
    private final List<Tuple<M>>         BUFFER_TUPLE = new ArrayList<>(100000);
    private final List<PointInTime>      BUFFER_TIME  = new ArrayList<>(100000);
    private final IStrategy<M> STRATEGY;
    private final int                    MAX_ITERATION;
    private final int                    MIN_CHUNK;
    private final double                 SUPPOSED_ERROR;
   
    private INeuralNetwork<M>            network;
    private boolean                      conceptDrift;
    private int                          counter;

    public NeuralNetworkLearnerPO(INeuralNetwork<M> network, IStrategy<M> strategy)
    {
        this.STRATEGY       = strategy;
        this.MIN_CHUNK      = STRATEGY.getMinChunk();
        this.network        = network;
        this.MAX_ITERATION  = network.getIterations();
        this.SUPPOSED_ERROR = network.getSupposedError();
        this.counter  = 0;
    }

    protected void process_next(Tuple<M> tuple, int port)
    {
        final PointInTime t_s = tuple.getMetadata().getStart();
        final PointInTime t_e = tuple.getMetadata().getEnd();
        PointInTime minStart  = null;
        // Save the end time stamp of a tuple
        if (!BUFFER_TIME.contains(t_s)) { BUFFER_TIME.add(t_s); }
        if (!BUFFER_TIME.contains(t_e)) { BUFFER_TIME.add(t_e); }
        Collections.sort(BUFFER_TIME);
        BUFFER_TUPLE.add(tuple);
        int removeUntil = 0;
        for (int i = 1; i < BUFFER_TIME.size(); i++)
        {
            final PointInTime end   = BUFFER_TIME.get(i);
            final PointInTime start = BUFFER_TIME.get(i - 1);;
            switch(i) { case 1: minStart = start; break; }
            if (end.beforeOrEquals(t_s))
            {
                synchronized (BUFFER_TUPLE)
                {
                    // Get all elements in the same window
                    Iterator<Tuple<M>> iter = BUFFER_TUPLE.iterator();
                    List<Tuple<M>> chunk    = new ArrayList<Tuple<M>>(10000);
                    while (iter.hasNext())
                    {
                        Tuple<M> element = iter.next();
                        if (element.getMetadata().getStart().beforeOrEquals(end) && start.before(element.getMetadata().getEnd()))
                        {
                            chunk.add(element);
                        }
                    }
                    // Add collected elements as training set to the network
                    network.addTrainingssetToBuffer(chunk);
                    counter++;
                    // If a concept drift appeared, deal with it
                    handleConceptDrift();
                    //If enough chunks are preserved;
                    if(counter >= MIN_CHUNK)
                    {
                        removeUntil = i;
                        /*
                         * Reset the weight matrix of the current network,
                         * prepare the added training data and set up the
                         * training state. Following by the actual training
                         * phase: 
                         */
                        network.reset();
                        network.prepareTraining();
                        int epochs    = 0;
                        double curErr = 1.0;
                        double time   = System.currentTimeMillis();
                        while (curErr > SUPPOSED_ERROR)
                        {
                            
                            network = network.iterate();
                            curErr  = network.getCurrentError();
                            epochs++;
                            if (epochs >= MAX_ITERATION) break;
                        }
                        double timeafter = System.currentTimeMillis();
                        //Construct network tuple and transfer it
                        @SuppressWarnings("unchecked")
                        M tupleInfo = (M) tuple.getMetadata().clone();
                        Tuple<M> newTuple = new Tuple<M>(5, false);
                        newTuple.setAttribute(0, network);
                        newTuple.setAttribute(1, new Double(curErr));
                        newTuple.setAttribute(2, new Integer(epochs));
                        newTuple.setAttribute(3, new Double(timeafter - time));
                        newTuple.setAttribute(4, new Boolean(conceptDrift));
                        conceptDrift = false;
                        newTuple.setMetadata(tupleInfo);
                        newTuple.getMetadata().setStartAndEnd(minStart, end);
                        transfer(newTuple);
                        handleForgetting();
                    }
                    //Send punctuation to avoid blocking behaviour 
                    sendPunctuation(Heartbeat.createNewHeartbeat(t_s));
                }
            }
            else
            {
                break;
            }
        }
        clearBuffers(removeUntil, t_s);
    }
    
    private void handleForgetting()
    {
        //If the given strategy permits a termination of chunks 
        if (STRATEGY.forgettingTriggered(network.getBuffer(), network))
        {
            /*
             * Execute the given strategy; set the processed chunk buffer
             * to the new chunk buffer of the network and update chunk_counter
             * by the given value.
             */
            int sBefore = network.getBuffer().size();
            counter -= (sBefore - network.setBuffer(STRATEGY.executeForgetting(network.getBuffer())));
        }  
    }

    private void handleConceptDrift()
    {
        if(STRATEGY.conceptDriftAppeared(network.getBuffer(), network))
        {
            network.setBuffer(STRATEGY.handleConceptDrift(network.getBuffer()));
            conceptDrift = true;
        }
    }

    private void clearBuffers(int until, PointInTime t_s) 
    {
        if(until != 0)
        {
            // Clear
            Iterator<PointInTime> iter3 = BUFFER_TIME.iterator();
            int k = 0;
            while(iter3.hasNext())
            {
                iter3.next();
                iter3.remove();
                if(k == until) break;
                k++;
            }
            //Remove all tuples form BUFFER_TUPLE that does not belong to the current window
            Iterator<Tuple<M>> iter2 = BUFFER_TUPLE.iterator();
            while (iter2.hasNext())
            {
                if (iter2.next().getMetadata().getEnd().before(t_s))
                {
                    iter2.remove();
                }
            }
        } 
    }
    
    @Override
    public void processPunctuation(IPunctuation punctuation, int port)
    {
        sendPunctuation(punctuation);
    }

    @Override
    protected void process_close() 
    {
        synchronized (this)
        {            
            BUFFER_TUPLE.clear();
            BUFFER_TIME.clear();
            network.clear();
    //        network = null;
            STRATEGY.clear();
        }
    }
    
    @Override
    public OutputMode getOutputMode() { return OutputMode.NEW_ELEMENT; }

}
