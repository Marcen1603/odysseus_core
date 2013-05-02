/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.MigrationMarkerPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationEventSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.IMigrationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration.exception.MigrationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Uses the oldPlan and the newPlan during a plan migration as inputs. Routes
 * the tuples from newPlan to /dev/null until it processes punctuations from all
 * sources used in the plans. If all punctuations from both plans are processed
 * it routes all tuples from oldPlan to /dev/null and tuples from newPlan are
 * the results.
 * 
 * @author Merlin Wasmann
 * 
 */
public class MigrationRouterPO<R extends IStreamObject<? extends ITimeInterval>>
		extends AbstractPipe<R, R> implements IMigrationEventSource {

	public static final Logger LOG = LoggerFactory
			.getLogger(MigrationRouterPO.class);

	// left is old and right is new
	private Map<ISource<?>, Pair<IPunctuation, IPunctuation>> sourcesToPunctuations;
	private int inPortOld;
	private int inPortNew;
	private boolean useOld;
	private ITimeIntervalSweepArea<R>[] areas;
	private R lastSend = null;

	private Set<IMigrationListener> listener;

	public MigrationRouterPO(List<ISource<?>> sources, int inPortOld,
			int inPortNew, ITimeIntervalSweepArea<R>[] areas) {
		if (sources == null || sources.isEmpty() || inPortOld == inPortNew) {
			throw new IllegalArgumentException(
					(inPortOld == inPortNew ? "Input ports of old and new plan are equal."
							: "Sources are null or empty."));
		}
		this.sourcesToPunctuations = new HashMap<ISource<?>, Pair<IPunctuation, IPunctuation>>();
		for (ISource<?> source : sources) {
			this.sourcesToPunctuations.put(source,
					new Pair<IPunctuation, IPunctuation>(null, null));
		}
		this.inPortOld = inPortOld;
		this.inPortNew = inPortNew;
		this.useOld = true;
		this.listener = new HashSet<IMigrationListener>();
		this.areas = areas;
	}

	public MigrationRouterPO(Set<ISource<?>> sources, int inPortOld,
			int inPortNew, ITimeIntervalSweepArea<R>[] areas) {
		if (sources == null || sources.isEmpty() || inPortOld == inPortNew) {
			throw new IllegalArgumentException(
					(inPortOld == inPortNew ? "Input ports of old and new plan are equal."
							: "Sources are null or empty."));
		}
		this.sourcesToPunctuations = new HashMap<ISource<?>, Pair<IPunctuation, IPunctuation>>();
		for (ISource<?> source : sources) {
			this.sourcesToPunctuations.put(source,
					new Pair<IPunctuation, IPunctuation>(null, null));
		}
		this.inPortOld = inPortOld;
		this.inPortNew = inPortNew;
		this.useOld = true;
		this.listener = new HashSet<IMigrationListener>();
		this.areas = areas;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		boolean finished = false;
		if (!useOld) {
			finished = isFinished(object, port);
		}
		if (port == this.inPortOld && !finished) {
			lastSend = object;
			LOG.debug("transfer old {}", object);
			transfer(object);
		}

		// wait until the new plan has caught up on the old plan
		if (finished
				&& port == this.inPortNew
				&& lastSend != null
				&& !lastSend.equals(object)
				&& lastSend.getMetadata().getStart()
						.beforeOrEquals(object.getMetadata().getStart())) {
			if(port == this.inPortNew) {
				LOG.debug("transfer new {}", object);
				transfer(object);
			}
			fireMigrationFinishedEvent(this);
		}
	}
	
	private boolean isFinished(R object, int port) {
		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		synchronized (this.areas[otherport]) {
			this.areas[otherport].purgeElements(object, order);
		}
		Iterator<R> qualifies;
		synchronized (this.areas) {
			synchronized (this.areas[otherport]) {
				qualifies = this.areas[otherport].queryCopy(object, order);
			}
			synchronized (this.areas[port]) {
				this.areas[port].insert(object);
			}
		}
		R elem = null;
		while (qualifies.hasNext()) {
			elem = qualifies.next();
			if (elem.equals(object)) {
				// migration is finished
				return true;
			}
		}
		return false;
	}

	private void process_migrationMarkerPunctuation(
			MigrationMarkerPunctuation p, int port) throws MigrationException {
		Pair<IPunctuation, IPunctuation> pair = this.sourcesToPunctuations
				.get(p.getSource());
		if (pair == null) {
			throw new MigrationException("Source "
					+ p.getSource().getClass().getSimpleName() + " ("
					+ p.getSource().hashCode()
					+ ") is not known to the RouterPO (" + hashCode() + ")");
		}
		if (port == inPortOld) {
			pair.setE1(p);
		} else if (port == inPortNew) {
			pair.setE2(p);
		}
		if (pair.getE2() != null && pair.getE1() != null) {
			// this source is satisfied.
			this.sourcesToPunctuations.remove(p.getSource());
			LOG.debug("Source: " + p.getSource().getName() + " is satisfied");
		}
		// are all sources satisfied?
		if (this.sourcesToPunctuations.isEmpty()) {
			useOld = false;
			LOG.debug("All sources are satisfied");
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (punctuation instanceof MigrationMarkerPunctuation) {
			// gotcha
			try {
				process_migrationMarkerPunctuation(
						(MigrationMarkerPunctuation) punctuation, port);
			} catch (MigrationException ex) {
				LOG.error("Processing migration marker punctuation failed", ex);
				fireMigrationFailedEvent(this, ex);
			}
		} else {
			sendPunctuation(punctuation);
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		for (int i = 0; i < 2; i++) {
			this.areas[i].clear();
			this.areas[i].init();
		}
	}

	@Override
	protected synchronized void process_close() {
		this.areas[0].clear();
		this.areas[1].clear();
	}

	@Override
	protected synchronized void process_done() {
		if (isOpen()) {
			this.areas[0].clear();
			this.areas[1].clear();
		}
	}

	@Override
	protected boolean isDone() {
		try {
			if (getSubscribedToSource(0).isDone()) {
				return getSubscribedToSource(1).isDone() || areas[0].isEmpty();
			}
			return getSubscribedToSource(1).isDone()
					&& getSubscribedToSource(0).isDone() && areas[1].isEmpty();
		} catch (ArrayIndexOutOfBoundsException e) {
			return true;
		}
	}

	@Override
	public AbstractPipe<R, R> clone() {
		MigrationRouterPO<R> clone = new MigrationRouterPO<R>(
				this.sourcesToPunctuations.keySet(), this.inPortOld,
				this.inPortNew, this.areas);
		Map<ISource<?>, Pair<IPunctuation, IPunctuation>> stp = new HashMap<ISource<?>, Pair<IPunctuation, IPunctuation>>();
		stp.putAll(this.sourcesToPunctuations);
		clone.setSourcesToPunctuations(stp);
		return clone;
	}

	public ITimeIntervalSweepArea<R>[] getAreas() {
		return this.areas;
	}

	public Set<ISource<?>> getSources() {
		return this.sourcesToPunctuations.keySet();
	}

	public void setSourcesToPunctuations(
			Map<ISource<?>, Pair<IPunctuation, IPunctuation>> stp) {
		this.sourcesToPunctuations = stp;
	}

	@Override
	public void addMigrationListener(IMigrationListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("IMigrationListener is null.");
		}
		this.listener.add(listener);
	}

	@Override
	public void removeMigrationListener(IMigrationListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("IMigrationListener is null.");
		}
		this.listener.remove(listener);
	}

	@Override
	public void fireMigrationFinishedEvent(IMigrationEventSource sender) {
		for (IMigrationListener listener : this.listener) {
			listener.migrationFinished(sender);
		}
	}

	@Override
	public IPhysicalQuery getPhysicalQuery() {
		return (IPhysicalQuery) getOwner().get(0);
	}

	@Override
	public boolean hasPhysicalQuery() {
		return getOwner().get(0) != null;
	}

	@Override
	public void fireMigrationFailedEvent(IMigrationEventSource sender,
			Throwable ex) {
		for (IMigrationListener listener : this.listener) {
			listener.migrationFailed(sender, ex);
		}
	}
}
