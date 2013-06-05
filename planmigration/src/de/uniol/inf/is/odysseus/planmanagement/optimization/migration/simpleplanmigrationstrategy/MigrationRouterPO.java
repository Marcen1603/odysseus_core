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

package de.uniol.inf.is.odysseus.planmanagement.optimization.migration.simpleplanmigrationstrategy;

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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
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
	private boolean punctuationsReceived;
	private boolean onlyNew;
	private boolean finished;
	private ITimeIntervalSweepArea<R>[] areas;
	private R lastSend = null;
	
	// Evaluation
	private boolean printNextTuple = false;

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
		this.punctuationsReceived = false;
		this.onlyNew = false;
		this.finished = false;
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
		this.punctuationsReceived = false;
		this.onlyNew = false;
		this.finished = false;
		this.listener = new HashSet<IMigrationListener>();
		this.areas = areas;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		if(isPrintNextTuple()) {
			// Evaluation
			LOG.debug("FIRST TUPLE WITH PARALLEL EXECUTION {} TIMESTAMP {}", object, System.currentTimeMillis());
			setPrintNextTuple(false);
		}
		
		// check if new has caught up on old.
		if (punctuationsReceived) {
			finished = true;
		}
		
		// happens if process_next is called while the migration is already finished but the router is not yet removed.
		if(this.onlyNew && port == this.inPortNew) {
			LOG.debug("Migration finished processing last tuples");
			transfer(object);
			return;
		}

		// transfer old objects if the port is correct. or check if element is newer than the lastSend-Element from the old plan.
		if (port == this.inPortOld) {
			// transfer normally if not finished yet.
			// if finished only transfer if starttimestamp is equal to the lastSend.starttimestamp
			if ((finished && lastSend != null && lastSend.getMetadata()
					.getStart().equals(object.getMetadata().getStart()))
					|| !finished) {
				lastSend = object;
				transfer(object);
			} 
		} else if(finished && lastSend != null && object.getMetadata().getStart().after(lastSend.getMetadata().getStart())) {
			transfer(object);
			LOG.debug("LAST TUPLE WITH PARALLEL EXECUTION {} TIMESTAMP {}", object, System.currentTimeMillis());
			this.onlyNew = true;
			fireMigrationFinishedEvent(this);
		}

	}
	
	@SuppressWarnings("unused")
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
		LOG.debug("Receiced punctuation for {} from {}", p.getSource().getName(), (port == 0 ? "old" : "new"));
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
			punctuationsReceived = true;
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

	@SuppressWarnings("unchecked")
	@Override
	public AbstractPipe<R, R> clone() {
		MigrationRouterPO<R> clone = new MigrationRouterPO<R>(
				this.sourcesToPunctuations.keySet(), this.inPortOld,
				this.inPortNew, this.areas.clone());
		Map<ISource<?>, Pair<IPunctuation, IPunctuation>> stp = new HashMap<ISource<?>, Pair<IPunctuation, IPunctuation>>();
		stp.putAll(this.sourcesToPunctuations);
		clone.setSourcesToPunctuations(stp);
		clone.finished = this.finished;
		clone.lastSend = (R) this.lastSend.clone();
		clone.onlyNew = this.onlyNew;
		clone.punctuationsReceived = this.punctuationsReceived;
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

	/**
	 * @return the printNextTuple
	 */
	public boolean isPrintNextTuple() {
		return printNextTuple;
	}

	/**
	 * @param printNextTuple the printNextTuple to set
	 */
	public void setPrintNextTuple(boolean printNextTuple) {
		this.printNextTuple = printNextTuple;
	}
}
