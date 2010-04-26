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
 * @author Dennis Geesen
 * @param <T> the reading and writing type of a tuple
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
	
	/** The cycles detected for this broker. */
	private List<CycleSubscription> cycles = new ArrayList<CycleSubscription>();
	
	/** Determines if the broker is waiting or not. */
	private volatile boolean waiting = false;
	
	/** The port the broker is waiting for (usually a writing port of a cycle). */
	private int waitingForPort = -1;
	
	/** The waiting buffer caches all elements before it is their chronological turn. */
	private PriorityQueue<T> waitingBuffer = new PriorityQueue<T>(1, new TimeIntervalComparator<IMetaAttributeContainer<ITimeInterval>>());

	/** Assigns each writing stream a tsmin to save the minimum timestamp. */
	private PointInTime tsmin[] = new PointInTime[0];
		
	/** The min is the minimum time of all writing streams except of cyclic streams. */
	private PointInTime min = null;

	/** The last {@link TransactionTS} which has been delivered. */
	private TransactionTS lastTransactionTS = null;

	/** Sets debug outputs on or off. */
	private boolean printDebug = true;

	/**
	 * Instantiates a new BrokerPO.
	 *
	 * @param identifier the name of the broker
	 */
	public BrokerPO(String identifier) {
		this.identifier = identifier;
		init();
	}

	/**
	 * Instantiates a new BrokerPO from an old one.
	 *
	 * @param po the old one
	 */
	public BrokerPO(BrokerPO<T> po) {
		this.identifier = po.getIdentifier();
		init();
	}

	/**
	 * Initiates the Broker 
	 */
	private void init() {
		// set to position 1 -> evaluate attribute "id"
		this.sweepArea.setQueryPredicate(new BrokerQueryPredicate<T>(1));
		this.sweepArea.setRemovePredicate(new BrokerRemovePredicate<T>(1));
	}

	/**
	 * Sets the queue schema.
	 *
	 * @param queueSchema the new queue schema
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe#process_next(java.lang.Object, int)
	 */
	protected void process_next(T object, int port) {
		// Determine the current transaction type
		WriteTransaction type = BrokerDictionary.getInstance().getWriteTypeForPort(this.identifier, port);
		// setting the minimum time for the current port
		this.setMinTS(port, object.getMetadata().getStart());
		// determin the minimum of all writing streams  
		this.min = getMinimum();
		// printDebug("Minimun time is " + this.min);
		printDebug("Process from " + port + " " + type + ": " + object.toString() + "  (" + this + ")");
		// if this is a timestamp tuple (from a queue stream)...
		if (type == WriteTransaction.Timestamp) {
			//... save the request as an TransactionTS in the timestamp list
			PointInTime time = object.getMetadata().getStart();
			TransactionTS trans = new TransactionTS(getOutgoingPortForIncoming(port), time);
			timestampList.offer(trans);
		} else {
			//... if not from timestamp tuple, check whether the broker is in waiting mode
			if (waiting) {
				// if the current port is equal to port the broker is waiting for
				if (port == waitingForPort) {					
					printDebug("Cyclic - received...");
					// check if the last request in the timestamp list is equal to the last one the broker executed
					TransactionTS peek = timestampList.peek();
					if (peek.isPortAndTimeEqual(lastTransactionTS)) {
						// check if it comes from the cycle the broker waited for.
						if (getInPortForCycleOutPort(peek.getOutgoingPort()) == port) {
							// remove the request from timestamp list, because the answer arrived
							TransactionTS toRemove = timestampList.poll();
							printDebug("Removed timestamp " + toRemove + " from list - waited for: " + port);
							printDebug(getNextCyclicTransactionList());
							// if the next one does not belong to the current request, everything is back and the broker can leave the waiting mode
							if(!timestampList.peek().isPortAndTimeEqual(lastTransactionTS)){
								waiting = false;
								printDebug("Cyclic - everything back...");
							}
						}
					} else {
						// if the last request is not equal to the last one, there should be all back, because there are no more according requests
						waiting = false;
						printDebug("Cyclic - everything should be back - because there are no more timestamp i am waiting for...");
					}
				}
			}
			// each incoming object will be put into the waiting buffer
			waitingBuffer.add(object);
		}
		// if the broker is not in waiting mode...
		if (!waiting) {
			//... and there is a valid minimum (each writing transaction has at least one valid tuple or punctuation)
			if (min != null) {
				printDebug("Get all from waiting buffer <= " + min);
				// get all objects from waiting buffer... 
				while (!waitingBuffer.isEmpty()) {
					T o = waitingBuffer.peek();
					//... which are before or equal the minimum
					if (o.getMetadata().getStart().beforeOrEquals(min)) {
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
			// get all continuously reading subscriptions for output 
			List<PhysicalSubscription<ISink<? super T>>> destinations = getWritingToSinks();
			if (!timestampList.isEmpty()) {
				TransactionTS nextTs = timestampList.peek();
				// if there is a minimum (at least one valid timestamp from each writing stream)
				// and if the next timestamp from a queue stream is before or equal this minimum
				if (min != null && nextTs.getPointInTime().beforeOrEquals(min)) {
					// add the next timestamp transaction to the destinations
					TransactionTS tts = timestampList.peek();
					int nextPort = tts.getOutgoingPort();
					PhysicalSubscription<ISink<? super T>> nextSub = getSinkSubscriptionForPort(nextPort);
					if (nextSub != null) {
						destinations.add(nextSub);
						this.lastTransactionTS = tts;
					}
				}
			}
			printDebug("Next cyclic output: " + getNextCyclicTransactionList());
			// transfer the content of the the SweepArea to the destinations (first ones are continuous)
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
						this.waitingForPort = getInPortForCycleOutPort(toPort);
						// switch to waiting mode
						this.waiting = true;
						System.out.println("Cyclic - waiting for port " + waitingForPort + "...");
						return;
					}
				}
			}

		}

	}

	/**
	 * Gets the writing port (end of a cycle) for cyclic reading port (start of a cycle).
	 *
	 * @param port the port to look for.
	 * @return the writing port or -1 if there is no cycle for the given reading port 
	 */
	private int getInPortForCycleOutPort(int port) {
		for (CycleSubscription cs : this.cycles) {
			if (cs.getOutgoingPort() == port) {
				return cs.getIncomingPort();
			}
		}
		logger.warn("There is no cycle with outgoing port " + port);
		return -1;
	}

	/**
	 * Gets the subscription for a reading port.
	 *
	 * @param port the port
	 * @return the sink subscription for port
	 */
	private PhysicalSubscription<ISink<? super T>> getSinkSubscriptionForPort(int port) {
		for (PhysicalSubscription<ISink<? super T>> sub : this.subscriptions) {
			if (sub.getSourceOutPort() == port) {
				return sub;
			}
		}
		return null;
	}

	/**
	 * Gets all continuously reading subscriptions
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
	 * Transfers the given object to the subscription on the given port 
	 *
	 * @param object the object to transfer
	 * @param sourceOutPort the port of the destination
	 */
	protected void process_transfer(T object, int sourceOutPort) {
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sink : this.subscriptions) {
				if (sink.getSourceOutPort() == sourceOutPort) {
					sink.getTarget().process(object, sink.getSinkInPort(), !this.hasSingleConsumer());
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource#transfer(java.lang.Object, int)
	 */
	@Override
	public void transfer(T object, int sourceOutPort) {
		ReadTransaction type = BrokerDictionary.getInstance().getReadTypeForPort(this.identifier, sourceOutPort);
		System.err.println("Transfer to " + sourceOutPort + " " + type + ": " + object.toString() + "  (" + this + ")");
		process_transfer(object, sourceOutPort);
	};

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe#getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + " (" + this.identifier + ")";
	}

	/**
	 * Reorganize transactions and sets new cycles up.
	 *
	 * @param cycles the new cycles
	 */
	public void reorganizeTransactions(List<CycleSubscription> cycles) {
		LoggerFactory.getLogger("BrokerPO - reorganize").debug("Setting transaction types");
		for (CycleSubscription cycle : cycles) {
			LoggerFactory.getLogger("BrokerPO - reorganize").debug(cycle.toString() + " - change transaction type to cyclic");
			BrokerDictionary.getInstance().setReadTypeForPort(this.identifier, cycle.getOutgoingPort(), ReadTransaction.Cyclic);
			BrokerDictionary.getInstance().setWriteTypeForPort(this.identifier, cycle.getIncomingPort(), WriteTransaction.Cyclic);
		}
		this.cycles = cycles;
	}

	/**
	 * Gets the reading port for a given writing port.
	 * The writing port is the queue port and the reading port is the assigned reading sink.
	 *
	 * @param income the writing port
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
	 * @param message the message
	 */
	private void printDebug(String message) {
		if (this.printDebug) {
			System.err.println(message);
		}
	}

	/**
	 * Calculates the current minimum of all writing streams.
	 * Previously it checks whether all writing streams have at least one timestamp.
	 * if not this method returns null.
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
	 * Ensures that each writing stream has sent at least one timestamp (element or punctuation) 
	 * except for cyclic transactions
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
	 * Gets the minimum timestamp for a given port
	 *
	 * @param port the port
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
	 * @param port the port
	 * @param time the new timestamp
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe#processPunctuation(de.uniol.inf.is.odysseus.base.PointInTime, int)
	 */
	@Override
	public void processPunctuation(PointInTime timestamp, int port) { 
		setMinTS(port, timestamp);
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
}