package de.uniol.inf.is.odysseus.p2p_new.distribute.physicaloperator;

import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.predicate.EqualsPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;

// TODO Check usage of PriorityQueues instead of SweepAreas with IStreamable as elements (see InputAreas) M.B.
// TODO javaDoc M.B.
public class DistributionMergePO<T extends IStreamObject<? extends ITimeInterval>> 
		extends AbstractPipe<T, T> {
	
	private Map<Integer, DefaultTISweepArea<T>> areas;
	
	public ITimeIntervalSweepArea<T> getSweepArea(int port) throws IndexOutOfBoundsException {
		
		return this.areas.get(port);
		
	}

	public DistributionMergePO() {
		
		super();
		this.areas = Maps.newHashMap();
		
	}

	public DistributionMergePO(DistributionMergePO<T> mergePO) {
		
		super(mergePO);
		this.areas = Maps.newHashMap();
		for(int port : mergePO.areas.keySet())
			this.areas.put(port, mergePO.areas.get(port).clone());
		
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		
		return new DistributionMergePO<T>(this);
		
	}

	@Override
	public OutputMode getOutputMode() {
		
		return OutputMode.INPUT;
		
	}
	
	private void initSweepAreas() {
		
		for(int port = 0; port < this.getInputPortCount(); port++) {
			
			DefaultTISweepArea<T> area  = new DefaultTISweepArea<T>();
			area.setQueryPredicate(EqualsPredicate.getInstance());
			this.areas.put(port, area);
			
		}
		
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		
		for(int port : this.areas.keySet())
			this.areas.get(port).clear();
		this.areas.clear();
		
		this.initSweepAreas();
		
	}
	
	@Override
	protected void process_close() {
		
		for(int port : this.areas.keySet())
			this.areas.get(port).clear();
		
	}
	
	@Override
	protected void process_done() {
		
		if(this.isOpen())
			this.process_close();
		
	}
	
	@Override
	protected boolean isDone() {
		
		for(int port = 0; port < this.getInputPortCount(); port++) {
			
			if(!this.getSubscribedToSource(port).isDone() || !this.areas.get(port).isEmpty())
				return false;
			
		}
		
		return true;
		
	}
	
	private void mergeElement(T object, int port, Order order) {
		
		// The maximal count of that object over all ports and on the arriving port
		int foundMax = 0;
		int foundOwnPort = 0;
		
		for(int p : this.areas.keySet()) {
			
			// Get all objects of this sweeparea which are equals to the given object
			Iterator<T> areaIter = this.areas.get(p).query(object, order);
			
			// Counting the elements
			int foundCounter = 0;
			while(areaIter.hasNext()) {
				
				T obj = areaIter.next();
				if(obj.getMetadata().equals(object.getMetadata())) {
					
					// The same object with the same TS
					foundCounter++;
					
				}
				
			}
			
			if(foundCounter > foundMax)
				foundMax = foundCounter;
			if(port == p)
				foundOwnPort = foundCounter;
			
		}
		
		if(foundMax == 0 || foundMax == foundOwnPort) {
			
			/*
			 * First appearance of that object -> transfer
			 * Else would mean, that the object appeared on a different port earlier -> drop
			 */
			this.transfer(object);
			
		}
		
	}

	@Override
	protected void process_next(T object, int port) {
		
		DefaultTISweepArea<T> currentArea = this.areas.get(port);
		
		// The order doesn't matter for equals predicate.
		Order order = Order.LeftRight;
		
		this.mergeElement(object, port, order);
		
		currentArea.insert(object);
		currentArea.purgeElements(object, order);

	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		
		// TODO Can't put an IPunctuation object into a DefaultTISweepArea<T>. M.B.
	    
	}

}