package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.util.ArrayList;
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
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
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
	private List<T> dataContainer = new ArrayList<T>();
	private PriorityQueue<T> waitingBuffer = new PriorityQueue<T>(1, new TimeIntervalComparator<IMetaAttributeContainer<ITimeInterval>>());

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
		// DEBUG
		WriteTransaction type = BrokerDictionary.getInstance().getWriteTypeForPort(this.identifier, port);
		System.err.println("Process from " + port + " " + type + ": " + object.toString() + "  (" + this + ")");
		// DEBUG
		if (type == WriteTransaction.Timestamp) {
			PointInTime time = object.getMetadata().getStart();
			TransactionTS trans = new TransactionTS(getOutgoingPortForIncoming(port), time);
			timestampList.offer(trans);
		} else {
			if (waiting) {
				if (port == waitingForPort) {
					System.out.println("Cyclic - received...");
					sweepArea.purgeElements(object, Order.LeftRight);
					sweepArea.insert(object);
					while (!this.waitingBuffer.isEmpty()) {
						// in den hauptspeicher schieben.
						// jedoch nur solche, wo man weiß, dass keine jüngeren
						// mehr kommen
						// also ein min-TS für jeden merken
					}
					waiting = false;
				} else {
					this.waitingBuffer.add(object);
				}
			} else {
				sweepArea.purgeElements(object, Order.LeftRight);
				sweepArea.insert(object);
			}
		}
		System.out.println(this.sweepArea.getSweepAreaAsString(PointInTime.getZeroTime()));
		if (!waiting) {
			List<PhysicalSubscription<ISink<? super T>>> destinations = getWritingToSinks();
			if (!timestampList.isEmpty()) {
				int nextPort = timestampList.poll().getOutgoingPort();
				PhysicalSubscription<ISink<? super T>> nextSub = getSinkSubscriptionForPort(nextPort);
				if (nextSub != null) {
					destinations.add(nextSub);
				}
			}

			for (PhysicalSubscription<ISink<? super T>> toSub : destinations) {
				int toPort = toSub.getSourceOutPort();
				for (T element : this.sweepArea) {
					transfer(element, toPort);
				}
				if (BrokerDictionary.getInstance().getReadTypeForPort(getIdentifier(), toPort) == ReadTransaction.Cyclic) {
					this.waitingForPort = getInPortForCycleOutPort(toPort);
					this.waiting = true;
					System.out.println("Cyclic - waiting...");
					return;
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

	protected void process_nextOLD(T object, int port) {
		WriteTransaction type = BrokerDictionary.getInstance().getWriteTypeForPort(this.identifier, port);
		System.err.println("Process from " + port + " " + type + ": " + object.toString() + "  (" + this + ")");
		if (type == WriteTransaction.Timestamp) {
			PointInTime time = object.getMetadata().getStart();
			TransactionTS trans = new TransactionTS(getOutgoingPortForIncoming(port), time);
			timestampList.offer(trans);
		} else {
			// sweepArea.purgeElements(object, Order.LeftRight);
			// sweepArea.insert(object);
			insertInDC(object);
		}
		// System.out.println(sweepArea.getSweepAreaAsString(PointInTime.currentPointInTime()));
		List<PhysicalSubscription<ISink<? super T>>> continuousDestinations = getWritingToSinks();
		// T nextObject = sweepArea.poll();
		if (!timestampList.isEmpty()) {
			int nextPort = timestampList.poll().getOutgoingPort();
			transferAll(nextPort);
		}

		// and to all continuous
		for (PhysicalSubscription<ISink<? super T>> sub : continuousDestinations) {
			transferAll(sub.getSourceOutPort());
		}
	}

	public void transferAll(int port) {
		for (T nextTuple : this.dataContainer) {
			transfer(nextTuple, port);
		}
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
	public void subscribeToSource(ISource<? extends T> source, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		super.subscribeToSource(source, sinkInPort, sourceOutPort, schema);
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

	public void insertInDC(T object) {
		RelationalTuple<ITimeInterval> tuple = (RelationalTuple<ITimeInterval>) object;
		int id = ((Integer) tuple.getAttribute(1)).intValue();
		T found = null;
		for (T t : this.dataContainer) {
			RelationalTuple<ITimeInterval> relTuple = (RelationalTuple<ITimeInterval>) t;
			int tId = ((Integer) relTuple.getAttribute(1)).intValue();
			if (tId == id) {
				found = t;
				break;
			}
		}
		if (found != null) {
			// if there is one, remove it first
			this.dataContainer.remove(found);
		}
		this.dataContainer.add(object);
	}
}