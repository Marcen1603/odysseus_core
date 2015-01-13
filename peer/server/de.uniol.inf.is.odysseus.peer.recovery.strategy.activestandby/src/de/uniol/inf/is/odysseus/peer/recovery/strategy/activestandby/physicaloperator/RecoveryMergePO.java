package de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.distribute.modify.replication.physicaloperator.ReplicationMergePO;
import de.uniol.inf.is.odysseus.peer.recovery.strategy.activestandby.logicaloperator.RecoveryMergeAO;

/**
 * The recovery merge operator is an extension of the replication merge
 * operator. Untill one replica fails, it works as an replication merge. After
 * recovering at least one replica, the recovery merge will only process
 * elements from an input port, which replica has never been recovered, or which
 * has been recovered earliest.
 * 
 * @author Michael Brand
 *
 */
public class RecoveryMergePO<T extends IStreamObject<? extends ITimeInterval>>
		extends ReplicationMergePO<T> {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(RecoveryMergePO.class);

	/**
	 * A sorted list of the input ports. A lower index means that a port was
	 * last recovered earlier.
	 */
	private final List<Integer> mSortedPorts = Lists.newArrayList();

	private void initSortedPorts(int numInputPorts) {
		for (int inPort = 0; inPort < numInputPorts; inPort++) {
			this.mSortedPorts.add(new Integer(inPort));
		}
	}

	private void initSortedPorts(List<Integer> otherList) {
		for (Integer inPort : otherList) {
			this.mSortedPorts.add(new Integer(inPort));
		}
	}

	private void updateSortedPorts(int port) {
		this.mSortedPorts.remove(new Integer(port));
		this.mSortedPorts.add(new Integer(port));
	}

	/**
	 * True, if at least one replica has been recovered.
	 */
	private boolean mReplicaRecovered = false;

	/**
	 * The input port to use, if {@link #mReplicaRecovered} is true.
	 */
	private int mPortToUse = 0;

	/**
	 * Sets the input of a given port as recovered.
	 * 
	 * @param port
	 *            The input port.
	 */
	public void setInputAsRecovered(int port) {
		updateSortedPorts(port);
		this.mPortToUse = this.mSortedPorts.get(0);
		this.mReplicaRecovered = true;
		LOG.debug("Set port {} as recovered", port);
	}

	/**
	 * Creates a new recovery merge operator from it's logical operator.
	 */
	public RecoveryMergePO(RecoveryMergeAO mergeAO) {
		super();
		initSortedPorts(mergeAO.getNumberOfInputs());
	}

	/**
	 * Creates a new recovery merge operator as a copy of an existing one.
	 * 
	 * @param mergePO
	 *            The mrecovery merge operator to copy.
	 */
	public RecoveryMergePO(RecoveryMergePO<T> mergePO) {
		super(mergePO);
		initSortedPorts(mergePO.mSortedPorts);
		this.mPortToUse = mergePO.mPortToUse;
		this.mReplicaRecovered = mergePO.mReplicaRecovered;
	}

	@Override
	public AbstractPipe<T, T> clone() {

		return new RecoveryMergePO<T>(this);

	}

	@Override
	protected void process_next(T object, int port) {
		if (!this.mReplicaRecovered) {
			super.process_next(object, port);
		} else if (port == this.mPortToUse) {
			this.inputQueue.add(new Pair<IStreamable, Integer>(object, port));
		} else {
			LOG.debug("Discarding {} from port {}", object, port);
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (!this.mReplicaRecovered || port == this.mPortToUse) {
			super.processPunctuation(punctuation, port);
		} else {
			LOG.debug("Discarding {} from port {}", punctuation, port);
		}
	}

}