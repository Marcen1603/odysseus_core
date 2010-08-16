package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.physicaloperator.predicate.BrokerQueryPredicate;
import de.uniol.inf.is.odysseus.broker.physicaloperator.predicate.BrokerRemovePredicate;
import de.uniol.inf.is.odysseus.broker.physicaloperator.predicate.TimeIntervalComparator;
import de.uniol.inf.is.odysseus.broker.transaction.CycleSubscription;
import de.uniol.inf.is.odysseus.broker.transaction.QueuePortMapping;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * BrokerPO represents the physical implementation of the broker operator.
 * 
 * @param <T>
 *            the reading and writing type of a tuple
 * @author Dennis Geesen
 */
public class BrokerPO<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPipe<T, T> {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger("BrokerPO");

	/** The unique identifier or rather the name of the broker. */
	private String identifier;

	/** The SweepArea. */
	private BrokerSweepArea<T> sweepArea = new BrokerSweepArea<T>();

	/** The list for all waiting timestamps from queue streams. */
	private PriorityQueue<TransactionTS> timestampList = new PriorityQueue<TransactionTS>();

	/** The queue schema. */
	private SDFAttributeList queueSchema;	

	/**
	 * The waiting buffer caches all elements before it is their chronological
	 * turn.
	 */
	private PriorityQueue<T> waitingBuffer = new PriorityQueue<T>(1, new TimeIntervalComparator<IMetaAttributeContainer<ITimeInterval>>());

	/** Assigns each writing stream a tsmin to save the minimum timestamp. */
	private PointInTime tsmin[] = new PointInTime[0];

	/**
	 * The min is the minimum time of all writing streams except of cyclic
	 * streams.
	 */
	private PointInTime min = null;

	/** Sets debug outputs on or off. */
	private boolean printDebug = true;

	/** Indicates whether the content has changed. */
	private boolean contentChanged = false;

	/**
	 * Instantiates a new BrokerPO.
	 * 
	 * @param identifier
	 *            the name of the broker
	 */
	public BrokerPO(String identifier) {
		this.identifier = identifier;
		init();
	}

	/**
	 * Instantiates a new BrokerPO from an old one.
	 * 
	 * @param po
	 *            the old one
	 */
	public BrokerPO(BrokerPO<T> po) {
		this.identifier = po.getIdentifier();
		init();
	}

	/**
	 * Initiates the Broker.
	 */
	private void init() {
		// set to position 1 -> evaluate attribute "id"
		this.sweepArea.setQueryPredicate(new BrokerQueryPredicate<T>(1));
		this.sweepArea.setRemovePredicate(new BrokerRemovePredicate<T>(1));
	}

	/**
	 * Sets the queue schema.
	 * 
	 * @param queueSchema
	 *            the new queue schema
	 */
	public void setQueueSchema(SDFAttributeList queueSchema) {
		this.queueSchema = queueSchema;
	}

	/**
	 * Gets the queue schema.
	 * 
	 * @return the queue schema
	 */
	public SDFAttributeList getQueueSchema() {
		return this.queueSchema;
	}

	/**
	 * Gets the name of the broker.
	 * 
	 * @return the identifier
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe#process_next
	 * (java.lang.Object, int)
	 */
	protected void process_next(T object, int port) {
		printDebug("-----------------------------------------");
		// Determine the current transaction type
		WriteTransaction type = BrokerDictionary.getInstance().getWriteTypeForPort(this.identifier, port);
		// setting the minimum time for the current port
		this.setMinTS(port, object.getMetadata().getStart());
		// determin the minimum of all writing streams
		this.min = getMinimum();
		// printDebug("Minimun time is " + this.min);
		printDebug("Process from " + port + " " + type + ": " + object.toString() + "  (" + this + ")");
		printDebug("Minimum is: " + this.min);
		// if this is a timestamp tuple (from a queue stream)...
		if (type == WriteTransaction.Timestamp) {
			// ... save the request as an TransactionTS in the timestamp list
			PointInTime time = object.getMetadata().getStart();
			TransactionTS trans = new TransactionTS(getOutgoingPortForIncoming(port), time);
			timestampList.offer(trans);
			printDebug("Added to list: "+getNextCyclicTransactionList());
		} else {			
			// each incoming object will be put into the waiting buffer
			waitingBuffer.add(object);
		}
		contentChanged = false;
		// if the broker is not in waiting mode...		
		// ... and there is a valid minimum (each writing transaction has at
		// least one valid tuple or punctuation)
		if (min != null) {
			printDebug("Get all from waiting buffer <= " + min);
			// get all objects from waiting buffer...
			while (!waitingBuffer.isEmpty()) {
				T o = waitingBuffer.peek();
				// ... which are before or equal the minimum
				if (o.getMetadata().getStart().beforeOrEquals(min)) {
					contentChanged = true;
					T toInsert = waitingBuffer.poll();
					// remove the old version from SweepArea
					sweepArea.purgeElements(toInsert, Order.LeftRight);
					// insert the new version
					sweepArea.insert(toInsert);
				} else {
					break;
				}
			}
		}
		List<PhysicalSubscription<ISink<? super T>>> destinations = new ArrayList<PhysicalSubscription<ISink<? super T>>>();
		if (contentChanged) {
			// get all continuously reading subscriptions for output
			destinations = getWritingToSinks();
		}
		if (!timestampList.isEmpty()) {
			TransactionTS nextTs = timestampList.peek();
			// if there is a minimum (at least one valid timestamp from each
			// writing stream)
			// and if the next timestamp from a queue stream is before or
			// equal this minimum
			if (min != null && nextTs.getPointInTime().beforeOrEquals(min)) {
				// add the next timestamp transaction to the destinations
				timestampList.poll();
				int nextPort = nextTs.getOutgoingPort();
				PhysicalSubscription<ISink<? super T>> nextSub = getSinkSubscriptionForPort(nextPort);
				if (nextSub != null) {
					destinations.add(nextSub);						
				}
			}
		}
		printDebug("Next cyclic output: " + getNextCyclicTransactionList());
		// transfer the content of the the SweepArea to the destinations
		// (first ones are continuous)
		if (!this.sweepArea.isEmpty()) {
			for (PhysicalSubscription<ISink<? super T>> toSub : destinations) {
				int toPort = toSub.getSourceOutPort();
				// transfer the whole content to the subscription
				for (T element : this.sweepArea) {
					transfer(element, toPort);
				}
				// if (last) one is cycle then stop and wait
				if (BrokerDictionary.getInstance().getReadTypeForPort(getIdentifier(), toPort) == ReadTransaction.Cyclic) {
					// determine the port the broker has to wait for					
					return;
				} else if (BrokerDictionary.getInstance().getReadTypeForPort(getIdentifier(), toPort) == ReadTransaction.OneTime) {
					// broker don't have to wait, because it is one time
					// reading.
					// therefore the timestamp can directly removed
					this.timestampList.poll();
				}
			}
		}
		// If the sweep area is empty, then a punctuation must be send.
		// I think, this.min should be the punctuation of choice.
		// The punctuation must be send to all following operators, I think.

		else{
			if(this.min != null){
				this.sendPunctuation(new PointInTime(this.min));
			}
			
			// if min is null, you cannot send a punctuation, because
			// you don't know anything about the time progress
			// To ensure, that you can send a punctuation, all preceeding
			// operators have to send a punctuation.
		}
		

	}

	/**
	 * Gets the subscription for a reading port.
	 * 
	 * @param port
	 *            the port
	 * @return the sink subscription for port
	 */
	private PhysicalSubscription<ISink<? super T>> getSinkSubscriptionForPort(int port) {
		for (PhysicalSubscription<ISink<? super T>> sub : this.getSubscriptions()) {
			if (sub.getSourceOutPort() == port) {
				return sub;
			}
		}
		return null;
	}

	/**
	 * Gets all continuously reading subscriptions.
	 * 
	 * @return the sinks to write to
	 */
	public List<PhysicalSubscription<ISink<? super T>>> getWritingToSinks() {
		List<PhysicalSubscription<ISink<? super T>>> destinations = new ArrayList<PhysicalSubscription<ISink<? super T>>>();
		for (PhysicalSubscription<ISink<? super T>> sub : this.getSubscriptions()) {
			if (BrokerDictionary.getInstance().getReadTypeForPort(getIdentifier(), sub.getSourceOutPort()) == ReadTransaction.Continuous) {
				destinations.add(sub);
			}
		}
		return destinations;
	}

	/**
	 * Transfers the given object to the subscription on the given port.
	 * 
	 * @param object
	 *            the object to transfer
	 * @param sourceOutPort
	 *            the port of the destination
	 */
	protected void process_transfer(T object, int sourceOutPort) {
//		synchronized (this.getSubscriptions()) {
			for (PhysicalSubscription<ISink<? super T>> sink : this.getSubscriptions()) {
				if (sink.getSourceOutPort() == sourceOutPort) {
					sink.getTarget().process(object, sink.getSinkInPort(), !this.hasSingleConsumer());
				}
			}
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource#transfer
	 * (java.lang.Object, int)
	 */
	@Override
	public void transfer(T object, int sourceOutPort) {
		ReadTransaction type = BrokerDictionary.getInstance().getReadTypeForPort(this.identifier, sourceOutPort);
		printDebug("Transfer to " + sourceOutPort + " " + type + ": " + object.toString() + "  (" + this + ")");
		process_transfer(object, sourceOutPort);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe#getOutputMode
	 * ()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + " (" + this.identifier + ")";
	}

	/**
	 * Reorganize transactions and sets new cycles up.
	 * 
	 * @param cycles
	 *            the new cycles
	 */
	public void reorganizeTransactions(List<CycleSubscription> cycles) {
		LoggerFactory.getLogger("BrokerPO - reorganize").debug("Setting transaction types");
		for (CycleSubscription cycle : cycles) {
			LoggerFactory.getLogger("BrokerPO - reorganize").debug(cycle.toString() + " - change transaction type to cyclic");
			BrokerDictionary.getInstance().setReadTypeForPort(this.identifier, cycle.getOutgoingPort(), ReadTransaction.Cyclic);
			BrokerDictionary.getInstance().setWriteTypeForPort(this.identifier, cycle.getIncomingPort(), WriteTransaction.Cyclic);
		}		
	}

	/**
	 * Gets the reading port for a given writing port. The writing port is the
	 * queue port and the reading port is the assigned reading sink.
	 * 
	 * @param income
	 *            the writing port
	 * @return the the reading port
	 */
	private int getOutgoingPortForIncoming(int income) {
		for (QueuePortMapping mapping : BrokerDictionary.getInstance().getQueuePortMappings(this.identifier))
			if (mapping.getQueueWritingPort() == income) {
				return mapping.getDataReadingPort();
			}
		logger.warn("There is no reading sink for the writing queue port " + income);
		return 0;
	}

	/**
	 * Gets the content from the SweepArea.
	 * 
	 * @return the content
	 */
	public String getContent() {
		return this.sweepArea.getSweepAreaAsString(PointInTime.getZeroTime());
	}

	/**
	 * Prints a debug message.
	 * 
	 * @param message
	 *            the message
	 */
	private void printDebug(String message) {
		if (this.printDebug) {
			System.err.println(message);
		}
	}

	/**
	 * Calculates the current minimum of all writing streams. Previously it
	 * checks whether all writing streams have at least one timestamp. if not
	 * this method returns null.
	 * 
	 * @return the minimum or null
	 */
	private PointInTime getMinimum() {
		if (ensureTimes()) {
			PointInTime min = null;
			for (int i = 0; i < tsmin.length; i++) {
				if (tsmin[i] == null) {
					continue;
				}
				if (min == null || tsmin[i].before(min)) {
					min = tsmin[i];
				}
			}
			return min;
		} else {
			return null;
		}
	}

	/**
	 * Ensures that each writing stream has sent at least one timestamp (element
	 * or punctuation) except for cyclic transactions.
	 * 
	 * @return true, if successful
	 */
	public boolean ensureTimes() {
		for (PhysicalSubscription<ISource<? extends T>> sub : super.getSubscribedToSource()) {
			int port = sub.getSinkInPort();
			if (getMinTS(port) == null && (BrokerDictionary.getInstance().getWriteTypeForPort(this.getIdentifier(), port) != WriteTransaction.Cyclic)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets the minimum timestamp for a given port.
	 * 
	 * @param port
	 *            the port
	 * @return the minimum timestamp
	 */
	private PointInTime getMinTS(int port) {
		if (port < this.tsmin.length) {
			return this.tsmin[port];
		}
		return null;
	}

	/**
	 * Sets the minimum timestamp for a port.
	 * 
	 * @param port
	 *            the port
	 * @param time
	 *            the new timestamp
	 */
	private void setMinTS(int port, PointInTime time) {
		if (this.tsmin.length <= port) {
			this.tsmin = Arrays.copyOf(this.tsmin, port + 1);
		}
		if (BrokerDictionary.getInstance().getWriteTypeForPort(this.getIdentifier(), port) != WriteTransaction.Cyclic) {
			this.tsmin[port] = time;
		} else {
			this.tsmin[port] = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe#
	 * processPunctuation(de.uniol.inf.is.odysseus.base.PointInTime, int)
	 */
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		printDebug("Process punctuation: "+timestamp+" on port "+port);
		setMinTS(port, timestamp);
		PointInTime min = this.getMinimum();
		
		// if the sweepArea is empty, no data can be sent
		// in this case, send a punctuation, if possible
		// Maybe, this won't work. Perhaps, at arrival of
		// a punctuation, it should be checked, whether
		// sending data is possible or not.
		if(min != null && this.sweepArea.isEmpty()){
			this.sendPunctuation(min);
		}
	}

	/**
	 * Gets the a string representing the current list of waiting timestamp.
	 * 
	 * @return the current list as string
	 */
	public String getNextCyclicTransactionList() {
		String result = "Time(Port): ";
		result = result + " " + timestampList.toString();
		return result;
	}

	/**
	 * Checks if broker is in debug mode.
	 * 
	 * @return true, if debug mode is enabled
	 */
	public boolean isPrintDebug() {
		return printDebug;
	}

	/**
	 * Sets the debug mode on or off
	 * 
	 * @param printDebug
	 *            true for on or false for off
	 */
	public void setPrintDebug(boolean printDebug) {
		this.printDebug = printDebug;
	}
	
	@Override
	public BrokerPO<T> clone() {
		return new BrokerPO(this);
	}

}