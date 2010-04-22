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

public class BrokerPO<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPipe<T, T> {
	private Logger logger = LoggerFactory.getLogger("BrokerPO");
	private String identifier;
	private BrokerSweepArea<T> sweepArea = new BrokerSweepArea<T>();
	private PriorityQueue<TransactionTS> timestampList = new PriorityQueue<TransactionTS>();
	private SDFAttributeList queueSchema;
	private List<CycleSubscription> cycles = new ArrayList<CycleSubscription>();
	private volatile boolean waiting = false;
	private int waitingForPort = -1;
	private PriorityQueue<T> waitingBuffer = new PriorityQueue<T>(1, new TimeIntervalComparator<IMetaAttributeContainer<ITimeInterval>>());

	private int incomingCounter = 0;
	private PointInTime tsmin[] = new PointInTime[0];
	private PointInTime min = null;

	private boolean printDebug = true;

	public BrokerPO(String identifier) {
		this.identifier = identifier;
		init();
	}

	public BrokerPO(BrokerPO<T> po) {
		this.identifier = po.getIdentifier();
		init();
	}

	private void init() {
		// set to position 1 -> evaluate attribute "id"
		this.sweepArea.setQueryPredicate(new BrokerQueryPredicate<T>(1));
		this.sweepArea.setRemovePredicate(new BrokerRemovePredicate<T>(1));
	}

	public void setQueueSchema(SDFAttributeList queueSchema) {
		this.queueSchema = queueSchema;
	}

	public SDFAttributeList getQueueSchema() {
		return this.queueSchema;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	protected void process_next(T object, int port) {
		WriteTransaction type = BrokerDictionary.getInstance().getWriteTypeForPort(this.identifier, port);
		this.setMinTS(port, object.getMetadata().getStart());
		this.min = getMinimum();
		printDebug("Minimun time is " + this.min);
		printDebug("Process from " + port + " " + type + ": " + object.toString() + "  (" + this + ")");
		if (type == WriteTransaction.Timestamp) {
			PointInTime time = object.getMetadata().getStart();
			TransactionTS trans = new TransactionTS(getOutgoingPortForIncoming(port), time);
			timestampList.offer(trans);
		} else {
			if (waiting) {
				if (port == waitingForPort) {
					printDebug("Cyclic - received...");
					incomingCounter++;
					if (incomingCounter == 10) {
						waiting = false;
						incomingCounter = 0;
						printDebug("Cyclic - everything back...");
					}
				}
			}
			waitingBuffer.add(object);
		}
		if (!waiting) {
			if (min != null) {
				printDebug("Get all from waiting buffer <= " + min);
				while (!waitingBuffer.isEmpty()) {
					T o = waitingBuffer.peek();
					if (o.getMetadata().getStart().beforeOrEquals(min)) {
						// printDebug("From buffer to SA: " + o);
						T toInsert = waitingBuffer.poll();
						sweepArea.purgeElements(toInsert, Order.LeftRight);
						sweepArea.insert(toInsert);
						// System.out.println(sweepArea.getSweepAreaAsString(PointInTime.getZeroTime()));
					} else {
						break;
					}
				}
			}
			// get all subscriptions for output
			List<PhysicalSubscription<ISink<? super T>>> destinations = getWritingToSinks();
			if (!timestampList.isEmpty()) {
				TransactionTS nextTs = timestampList.peek();
				if (nextTs.getPointInTime().beforeOrEquals(min)) {
					int nextPort = timestampList.poll().getOutgoingPort();
					PhysicalSubscription<ISink<? super T>> nextSub = getSinkSubscriptionForPort(nextPort);
					if (nextSub != null) {
						destinations.add(nextSub);
					}
				}
			}

			// print to output (first ones are continuous)
			if (!this.sweepArea.isEmpty()) {
				for (PhysicalSubscription<ISink<? super T>> toSub : destinations) {
					int toPort = toSub.getSourceOutPort();
					for (T element : this.sweepArea) {
						transfer(element, toPort);
					}
					// if (last) one is cycle -> stop and wait
					if (BrokerDictionary.getInstance().getReadTypeForPort(getIdentifier(), toPort) == ReadTransaction.Cyclic) {
						this.waitingForPort = getInPortForCycleOutPort(toPort);
						this.waiting = true;
						System.out.println("Cyclic - waiting for port " + waitingForPort + "...");
						return;
					}
				}
			}

		}

	}

	private int getInPortForCycleOutPort(int port) {
		for (CycleSubscription cs : this.cycles) {
			if (cs.getOutgoingPort() == port) {
				return cs.getIncomingPort();
			}
		}
		logger.warn("There is no cycle with outgoing port " + port);
		return -1;
	}

	private PhysicalSubscription<ISink<? super T>> getSinkSubscriptionForPort(int port) {
		for (PhysicalSubscription<ISink<? super T>> sub : this.subscriptions) {
			if (sub.getSourceOutPort() == port) {
				return sub;
			}
		}
		return null;
	}

	public List<PhysicalSubscription<ISink<? super T>>> getWritingToSinks() {
		List<PhysicalSubscription<ISink<? super T>>> destinations = new ArrayList<PhysicalSubscription<ISink<? super T>>>();
		for (PhysicalSubscription<ISink<? super T>> sub : this.getSubscriptions()) {
			if (BrokerDictionary.getInstance().getReadTypeForPort(getIdentifier(), sub.getSourceOutPort()) == ReadTransaction.Continuous) {
				destinations.add(sub);
			}
		}
		return destinations;
	}

	protected void process_transfer(T object, int sourceOutPort) {
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sink : this.subscriptions) {
				if (sink.getSourceOutPort() == sourceOutPort) {
					sink.getTarget().process(object, sink.getSinkInPort(), !this.hasSingleConsumer());
				}
			}
		}
	}

	@Override
	public void transfer(T object, int sourceOutPort) {
		ReadTransaction type = BrokerDictionary.getInstance().getReadTypeForPort(this.identifier, sourceOutPort);
		System.err.println("Transfer to " + sourceOutPort + " " + type + ": " + object.toString() + "  (" + this + ")");
		process_transfer(object, sourceOutPort);
	};

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public String toString() {
		return super.toString() + " (" + this.identifier + ")";
	}

	public void reorganizeTransactions(List<CycleSubscription> cycles) {
		LoggerFactory.getLogger("BrokerPO - reorganize").debug("Setting transaction types");
		for (CycleSubscription cycle : cycles) {
			LoggerFactory.getLogger("BrokerPO - reorganize").debug(cycle.toString() + " - change transaction type to cyclic");
			BrokerDictionary.getInstance().setReadTypeForPort(this.identifier, cycle.getOutgoingPort(), ReadTransaction.Cyclic);
			BrokerDictionary.getInstance().setWriteTypeForPort(this.identifier, cycle.getIncomingPort(), WriteTransaction.Cyclic);
		}
		this.cycles = cycles;
	}

	public int getOutgoingPortForIncoming(int income) {
		for (QueuePortMapping mapping : BrokerDictionary.getInstance().getQueuePortMappings(this.identifier))
			if (mapping.getQueueWritingPort() == income) {
				return mapping.getDataReadingPort();
			}
		logger.warn("There is no cycle with incoming port " + income);
		return 0;
	}

	public String getContent() {
		return this.sweepArea.getSweepAreaAsString(PointInTime.getZeroTime());
	}

	public void printDebug(String message) {
		if (this.printDebug) {
			System.err.println(message);
		}
	}

	public PointInTime getMinimum() {
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

	public boolean ensureTimes() {
		for (PhysicalSubscription<ISource<? extends T>> sub : super.getSubscribedToSource()) {
			int port = sub.getSinkInPort();
			if (getMinTS(port) == null && (BrokerDictionary.getInstance().getWriteTypeForPort(this.getIdentifier(), port) != WriteTransaction.Cyclic)) {
				return false;
			}
		}
		return true;
	}

	private PointInTime getMinTS(int port) {
		if (port < this.tsmin.length) {
			return this.tsmin[port];
		}
		return null;
	}

	public void setMinTS(int port, PointInTime time) {
		if (this.tsmin.length <= port) {
			this.tsmin = Arrays.copyOf(this.tsmin, port + 1);
		}
		if (BrokerDictionary.getInstance().getWriteTypeForPort(this.getIdentifier(), port) != WriteTransaction.Cyclic) {
			this.tsmin[port] = time;
		} else {
			this.tsmin[port] = null;
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		System.out.println("Process Punctuation in Broker: " + timestamp + " from " + port);
		setMinTS(port, timestamp);
		// super.processPunctuation(timestamp, port);
	}
}