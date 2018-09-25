package de.uniol.inf.is.odysseus.server.replication.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.server.replication.logicaloperator.ReplicationMergeAO;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * A {@link ReplicationMergePO} can be used to realize a
 * {@link ReplicationMergeAO}. <br />
 * The {@link ReplicationMergePO} uses a {@link PriorityQueue} and can handle
 * {@link IPunctuations}. <br />
 * This operator is intended to be used, if the {@link ITrust} metadata is used.
 * This operator transfers the elements with the highest trust (one gets
 * transferred, other replicas discarded). The alternative
 * {@link ReplicationMergePO} ignores the trust values and transfers the first
 * element it gets.
 *
 * @author Michael Brand
 */
public class ReplicationMergeWithTrustPO<T extends IStreamObject<? extends IMetaAttribute>>
		extends ReplicationMergePO<T> {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ReplicationMergeWithTrustPO.class);

	/**
	 * The current highest trust.
	 */
	private double highestTrust = 0;

	/**
	 * All ports with the current highest trust.
	 */
	private List<Integer> portsWithHighestTrust = new ArrayList<>();

	/**
	 * The current trust values.
	 */
	private Map<Integer, Double> trustPerPort = new HashMap<>();

	/**
	 * Creates a new {@link ReplicationMergeWithTrustPO}.
	 */
	public ReplicationMergeWithTrustPO() {
		super();
	}

	/**
	 * Creates a new {@link ReplicationMergeWithTrustPO} as a copy of an
	 * existing one.
	 *
	 * @param mergePO
	 *            The {@link ReplicationMergeWithTrustPO} to be copied.
	 */
	public ReplicationMergeWithTrustPO(ReplicationMergeWithTrustPO<T> mergePO) {
		super(mergePO);
		this.highestTrust = mergePO.highestTrust;
		synchronized (portsWithHighestTrust) {
			this.portsWithHighestTrust.addAll(mergePO.portsWithHighestTrust);
		}
		synchronized (trustPerPort) {
			this.trustPerPort.putAll(mergePO.trustPerPort);
		}
	}

	/**
	 * Only ports with highest trust are allowed to set youngest TS.
	 */
	@Override
	protected boolean precheck(IStreamable object, int port) {
		if (!super.precheck(object, port)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		double trust = ((ITrust) ((T) object).getMetadata()).getTrust();
		synchronized (trustPerPort) {
			trustPerPort.put(port, trust);
		}

		synchronized (portsWithHighestTrust) {
			if (trust > highestTrust) {
				highestTrust = trust;
				portsWithHighestTrust.clear();
				logger.debug("New highest trust: {}", trust);
			}

			if (trust == highestTrust) {
				if (!portsWithHighestTrust.contains(port)) {
					portsWithHighestTrust.add(port);
					logger.debug("New port with highest trust: {}", port);
				}
				return true;
			} else {
				// trust too low
				if (portsWithHighestTrust.contains(port)) {
					portsWithHighestTrust.remove((Integer) port);
					// cast needed to avoid calling remove(int index)
					if (portsWithHighestTrust.isEmpty()) {
						// highest trust changes
						updateHighestTrust();
						updateAfterChangeOfHighestTrust(getTS(object, true));
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Discarded {} with trust {}", object, trust);
				}
				return false;
			}
		}
	}

	/**
	 * Updates {@link #highestTrust} and {@link #portsWithHighestTrust} based on
	 * {@link #trustPerPort}.
	 */
	private void updateHighestTrust() {
		synchronized (trustPerPort) {
			highestTrust = trustPerPort.values().stream().max((a, b) -> Double.compare(a, b)).orElse(0.0);
			logger.debug("New highest trust: {}", highestTrust);
			portsWithHighestTrust.clear();
			trustPerPort.keySet().stream().filter(k -> trustPerPort.get(k) == highestTrust)
					.forEach(k -> portsWithHighestTrust.add(k));
			if (logger.isDebugEnabled()) {
				portsWithHighestTrust.forEach(port -> logger.debug("New port with highest trust: {}", port));
			}
		}
	}

	/**
	 * Determine all elements with ts >= object.ts in inputQueue from a port
	 * with new highest trust and transfer them.
	 */
	private void updateAfterChangeOfHighestTrust(PointInTime threshold) {
		List<IStreamable> objectsToTransfer = new ArrayList<>();
		int portToUse = portsWithHighestTrust.iterator().next();
		Iterator<IPair<IStreamable, Integer>> queueIter = this.inputQueue.iterator();

		while (queueIter.hasNext()) {
			IPair<IStreamable, Integer> pair = queueIter.next();
			IStreamable object = pair.getE1();
			Integer elemPort = pair.getE2();
			if (elemPort != portToUse || getTS(object, true).before(threshold)) {
				continue;
			}
			objectsToTransfer.add(object);
		}

		objectsToTransfer.forEach(obj -> transferOrSendPunctuation(obj, 0));
	}

}
