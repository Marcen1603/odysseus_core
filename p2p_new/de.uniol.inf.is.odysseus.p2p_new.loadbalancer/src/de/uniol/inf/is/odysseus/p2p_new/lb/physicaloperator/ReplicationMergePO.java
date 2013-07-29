package de.uniol.inf.is.odysseus.p2p_new.lb.physicaloperator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * A {@link ReplicationMergePO} can be used to realize a {@link ReplicationMergeAO}. <br />
 * The {@link ReplicationMergePO} uses a {@link PriorityQueue} and can handle {@link IPunctuations}.
 * @author Michael Brand
 */
public class ReplicationMergePO<T extends IStreamObject<? extends ITimeInterval>> 
		extends AbstractPipe<T, T> {
	
	/**
	 * The {@link PriorityQueue} to keep in mind which elements have been seen by this {@link ReplicationMergePO}.
	 */
	private PriorityQueue<IPair<IStreamable, Integer>> inputQueue;
	
	/**
	 * The comparator for pairs of {@link IStreamable} objects and ports. <br />
	 * The start timestamps of the {@link IStreamable} objects will be compaired.
	 */
	Comparator<IPair<IStreamable, Integer>> comp = new Comparator<IPair<IStreamable, Integer>>() {

		@Override
		public int compare(IPair<IStreamable, Integer> left,
				IPair<IStreamable, Integer> right) {
			
			PointInTime ts_l = getTS(left.getE1(), true);
			PointInTime ts_r = getTS(right.getE1(), true);
			
			return ts_l.compareTo(ts_r);
			
		}		
		
	};
	
	/**
	 * Delivers a timestamp of an {@link IStreamable} object.
	 * @param object The object.
	 * @param searchForStartTS true, if it is the start timestamp, which should be delivered; false, if it is the end timestamp.
	 * @return {@link IPunctuation#getTime()} - if <code>object</code> is a punctuation <br />
	 * {@link ITimeInterval#getStart()} - if <code>object</code> is no punctuation and <code>searchForStartTS == true</code> <br />
	 * {@link ITimeInterval#getEnd()} - if <code>object</code> is no punctuation and <code>searchForStartTS == false</code> <br />
	 */
	@SuppressWarnings("unchecked")
	private PointInTime getTS(IStreamable object, boolean searchForStartTS) {
		
		if(object.isPunctuation())
			return ((IPunctuation) object).getTime();
		else if(searchForStartTS)
			return ((T) object).getMetadata().getStart();
		else return ((T) object).getMetadata().getEnd();
				
	}

	/**
	 * Constructs a new {@link ReplicationMergePO}.
	 */
	public ReplicationMergePO() {
		
		super();
		this.inputQueue = new PriorityQueue<IPair<IStreamable, Integer>>(10, comp);
		
	}

	/**
	 * Constructs a new {@link ReplicationMergePO} as a copy of an existing one.
	 * @param mergePO The {@link ReplicationMergePO} to be copied.
	 */
	public ReplicationMergePO(ReplicationMergePO<T> mergePO) {
		
		super(mergePO);
		this.inputQueue = new PriorityQueue<IPair<IStreamable, Integer>>(10, comp);
		this.inputQueue.addAll(mergePO.inputQueue);
		
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		
		return new ReplicationMergePO<T>(this);
		
	}

	@Override
	public OutputMode getOutputMode() {
		
		return OutputMode.INPUT;
		
	}
	
	@Override
	protected synchronized void process_open() throws OpenFailedException {
		
		this.inputQueue.clear();
		
	}
	
	@Override
	protected synchronized void process_close() {
		
		this.inputQueue.clear();
		
	}
	
	@Override
	protected synchronized void process_done() {
		
		if(this.isOpen())
			this.inputQueue.clear();
		
	}
	
	@Override
	protected synchronized boolean isDone() {
		
		if(!this.inputQueue.isEmpty())
			return false;
		
		for(int port = 0; port < this.getInputPortCount(); port++) {
			
			if(!this.getSubscribedToSource(port).isDone())
				return false;
			
		}
		
		return true;
		
	}
	
	@Override
	protected synchronized void process_next(T object, int port) {
		
		this.purgeElements(this.getTS(object, true));
		this.mergeElement(object, port);
		this.inputQueue.add(new Pair<IStreamable, Integer>(object, port));

	}
	
	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		
		this.purgeElements(this.getTS(punctuation, true));
		this.mergeElement(punctuation, port);
		this.inputQueue.add(new Pair<IStreamable, Integer>(punctuation, port));
		
	}
	
	/**
	 * Removes all elements from the {@link inputQueue}, which have an end timestamp before or equal to <code>deadline</code>.
	 */
	private synchronized void purgeElements(PointInTime deadline) {
		
		boolean continuePeeking = true;
		
		while(!this.inputQueue.isEmpty() && continuePeeking) {
			
			IStreamable elem = this.inputQueue.peek().getE1();
			PointInTime endTS_elem = this.getTS(elem, false);
			if(endTS_elem.beforeOrEquals(deadline))
				this.inputQueue.poll();
			else continuePeeking = false;
			
		}
		
	}
	
	/**
	 * Transfers an object if and only if it was not transfered before (which input port doesn't matter). <br />
	 * As objects can be equal with equal meta data this method determines, if the number of objects equal to <code>object</code> transfered 
	 * from port <code>port</code> is the maximum count over all ports. Otherwise the object will not transfered.
	 * @param object The object to be merged
	 * @param port The port on which the object was arriving.
	 */
	@SuppressWarnings("unchecked")
	private synchronized void mergeElement(IStreamable object, int port) {
		
		// The maximal count of that object over all ports and on the arriving port
		int foundMax = 0;
		int foundOwnPort = 0;
		
		Map<Integer, Integer> countToPortMap = Maps.newHashMap();	// key = port, value = count
		Iterator<IPair<IStreamable, Integer>> queueIter = this.inputQueue.iterator();
		while(queueIter.hasNext()) {
			
			IPair<IStreamable, Integer> pair = queueIter.next();
			IStreamable elem = pair.getE1();
			Integer elemPort = pair.getE2();
			
			if((elem.isPunctuation() && !object.isPunctuation()) || (!elem.isPunctuation() && object.isPunctuation())) {
				
				// only one element is a punctuation
				continue;
				
			} else if(elem.isPunctuation() && object.isPunctuation() && !((IPunctuation) elem).getTime().equals(((IPunctuation) object).getTime())) {
				
				// not the same timestamps of the punctuations
				continue;
				
			} else if(!elem.isPunctuation() && !object.isPunctuation() && 
					!(((T) elem).equals((T) object) || !((T) elem).getMetadata().equals(((T) object).getMetadata()))) {
				
				// either the elements or the timestamps are not equal
				continue;
				
			} else if(countToPortMap.containsKey(elemPort)) {
				
				// port is already in the map
				countToPortMap.put(elemPort, countToPortMap.get(elemPort) + 1);
				
			} else {
				
				// port isn't in the map yet
				countToPortMap.put(elemPort, 1);
				
			}
			
			// Check for maximum
			if(countToPortMap.get(elemPort) > foundMax)
				foundMax = countToPortMap.get(elemPort);
			if(port == elemPort)
				foundOwnPort = countToPortMap.get(elemPort);
			
		}
		
		if(foundMax == 0 || foundMax == foundOwnPort) {
			
			/*
			 * First appearance of that object -> transfer
			 * Else would mean, that the object appeared on a different port earlier -> drop
			 */
			if(object.isPunctuation())
				this.sendPunctuation((IPunctuation) object);
			else this.transfer((T) object);
			
		}
		
	}

}