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

package de.uniol.inf.is.odysseus.planmigration.simpleplanmigrationstrategy;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.planmigration.IMigrationEventSource;
import de.uniol.inf.is.odysseus.planmigration.IMigrationListener;

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
public class MigrationRouterPO<R extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<R, R>
		implements IMigrationEventSource {

	public static final Logger LOG = LoggerFactory.getLogger(MigrationRouterPO.class);

	// left is old and right is new
	// private Map<ISource<?>, Pair<IPunctuation, IPunctuation>>
	// sourcesToPunctuations;
	// private Map<ISource<?>, Pair<Integer, Integer>> pathesFromSource;
	private int inPortOld;
	private int inPortNew;
	private boolean finished;
	private PointInTime migrationStart;
	private List<R> newPlanBuffer;

	// Evaluation
	private boolean printNextTuple = false;

	private Set<IMigrationListener> listener;

	public MigrationRouterPO(List<ISource<?>> sources, int inPortOld, int inPortNew) {
		if (sources == null || sources.isEmpty() || inPortOld == inPortNew) {
			throw new IllegalArgumentException((inPortOld == inPortNew ? "Input ports of old and new plan are equal."
					: "Sources are null or empty."));
		}
		// this.sourcesToPunctuations = new HashMap<ISource<?>,
		// Pair<IPunctuation, IPunctuation>>();
		// this.pathesFromSource = new HashMap<>();
		// for (ISource<?> source : sources) {
		// this.sourcesToPunctuations.put(source, new Pair<IPunctuation,
		// IPunctuation>(null, null));
		// }
		this.inPortOld = inPortOld;
		this.inPortNew = inPortNew;
		this.listener = new HashSet<IMigrationListener>();
		this.newPlanBuffer = new LinkedList<>();
	}

	public MigrationRouterPO(Set<ISource<?>> sources, int inPortOld, int inPortNew) {
		if (sources == null || sources.isEmpty() || inPortOld == inPortNew) {
			throw new IllegalArgumentException((inPortOld == inPortNew ? "Input ports of old and new plan are equal."
					: "Sources are null or empty."));
		}
		// this.sourcesToPunctuations = new HashMap<ISource<?>,
		// Pair<IPunctuation, IPunctuation>>();
		// this.pathesFromSource = new HashMap<>();
		// for (ISource<?> source : sources) {
		// this.sourcesToPunctuations.put(source, new Pair<IPunctuation,
		// IPunctuation>(null, null));
		// }
		this.inPortOld = inPortOld;
		this.inPortNew = inPortNew;
		this.listener = new HashSet<IMigrationListener>();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(R object, int port) {
		if (this.migrationStart == null) {
			this.migrationStart = object.getMetadata().getStart();
			LOG.error("Migrationstart not set.");
		}
		if (isPrintNextTuple()) {
			// Evaluation
			LOG.debug("FIRST TUPLE WITH PARALLEL EXECUTION {} TIMESTAMP {}", object, System.currentTimeMillis());
			setPrintNextTuple(false);
		}
		// for marking if object should be transfered, not synchronized
		boolean transfer = false;
		boolean migrationFinished = false;
		
		synchronized (this) {

			// happens if process_next is called while the migration is already
			// finished but the router is not yet removed.
			if (port == this.inPortNew && object.getMetadata().getStart().after(migrationStart)) {
				if (!this.finished) {
					synchronized (this.newPlanBuffer) {
						this.newPlanBuffer.add(object);
					}
				} else {
					transfer = true;
				}
			} else

			// transfer old objects if the port is correct.
			if (port == this.inPortOld) {
				// transfer if elements starttimestamp is not after
				// migrationstart timestamp
				if (object.getMetadata().getStart().beforeOrEquals(migrationStart)) {
					transfer = true;
				} else if (!this.finished) {
					LOG.debug("Latestendtimestamp was at migration start in old plan was {}", this.migrationStart);
					LOG.debug("First element of old plan not transfered was {}", object);
					this.finished = true;
					this.drainBuffer();
					migrationFinished = true;
				}
			}
		}
		if (transfer) {
			synchronized (this.newPlanBuffer) {
				transfer(object);
			}
		}
		if (migrationFinished) {
				//this.drainBuffer();
				fireMigrationFinishedEvent(this);
		}
	}

	// @SuppressWarnings("unused")
	// private boolean isFinished(R object, int port) {
	// int otherport = port ^ 1;
	// Order order = Order.fromOrdinal(port);
	// synchronized (this.areas[otherport]) {
	// this.areas[otherport].purgeElements(object, order);
	// }
	// Iterator<R> qualifies;
	// synchronized (this.areas) {
	// synchronized (this.areas[otherport]) {
	// qualifies = this.areas[otherport].queryCopy(object, order);
	// }
	// synchronized (this.areas[port]) {
	// this.areas[port].insert(object);
	// }
	// }
	// R elem = null;
	// while (qualifies.hasNext()) {
	// elem = qualifies.next();
	// if (elem.equals(object)) {
	// // migration is finished
	// return true;
	// }
	// }
	// return false;
	// }

	// private synchronized void
	// process_migrationMarkerPunctuation(MigrationMarkerPunctuation p, int
	// port)
	// throws MigrationException {
	// Pair<IPunctuation, IPunctuation> pair =
	// this.sourcesToPunctuations.get(p.getSource());
	// Pair<Integer, Integer> pathesPair =
	// this.pathesFromSource.get(p.getSource());
	// LOG.debug("Receiced punctuation for {} ", p.toString());
	// if (pair == null) {
	// throw new MigrationException("Source " +
	// p.getSource().getClass().getSimpleName() + " ("
	// + p.getSource().hashCode() + ") is not known to the RouterPO (" +
	// hashCode() + ")");
	// }
	// if (port == inPortOld) {
	// pair = new Pair<IPunctuation, IPunctuation>(p, pair.getE2());
	// this.sourcesToPunctuations.put(p.getSource(), pair);
	// pathesPair = new Pair<Integer, Integer>(pathesPair.getE1() + 1,
	// pathesPair.getE2());
	// this.pathesFromSource.put(p.getSource(), pathesPair);
	// } else if (port == inPortNew) {
	// pair = new Pair<IPunctuation, IPunctuation>(pair.getE1(), p);
	// this.sourcesToPunctuations.put(p.getSource(), pair);
	// pathesPair = new Pair<Integer, Integer>(pathesPair.getE1() + 1,
	// pathesPair.getE2());
	// this.pathesFromSource.put(p.getSource(), pathesPair);
	// }
	// if (pair.getE2() != null && pair.getE1() != null && pathesPair.getE1() ==
	// pathesPair.getE2()) {
	// // this source is satisfied.
	// this.sourcesToPunctuations.remove(p.getSource());
	// LOG.debug("Source: " + p.getSource().getName() + " is satisfied");
	// }
	// // are all sources satisfied?
	// if (this.sourcesToPunctuations.isEmpty()) {
	// LOG.debug("All sources are satisfied");
	// }
	// }

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// if (punctuation instanceof MigrationMarkerPunctuation) {
		// // gotcha
		// try {
		// process_migrationMarkerPunctuation((MigrationMarkerPunctuation)
		// punctuation, port);
		// } catch (MigrationException ex) {
		// LOG.error("Processing migration marker punctuation failed", ex);
		// fireMigrationFailedEvent(this, ex);
		// }
		// } else {
		sendPunctuation(punctuation);
		// }
	}

	// public Set<ISource<?>> getSources() {
	// return this.sourcesToPunctuations.keySet();
	// }

	// public void setSourcesToPunctuations(Map<ISource<?>, Pair<IPunctuation,
	// IPunctuation>> stp) {
	// this.sourcesToPunctuations = stp;
	// }

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
	public void fireMigrationFailedEvent(IMigrationEventSource sender, Throwable ex) {
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
	 * @param printNextTuple
	 *            the printNextTuple to set
	 */
	public void setPrintNextTuple(boolean printNextTuple) {
		this.printNextTuple = printNextTuple;
	}

	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// @Override
	// public void process_open() {
	// super.process_open();
	// for (ISource<?> source : this.sourcesToPunctuations.keySet()) {
	// ExtendedGraphWalker walker = new ExtendedGraphWalker<>(false, true);
	// CountPathesBetweenOperatorsVisitor pathVisitor = new
	// CountPathesBetweenOperatorsVisitor(this);
	// walker.prefixWalkPhysical(source, pathVisitor);
	// int pathes = pathVisitor.getResult();
	// LOG.debug("Counted {} pathes from source {} to root {}", pathes,
	// source.getName(), this.getName());
	// this.pathesFromSource.put(source, new Pair<>(0, pathes));
	// }
	//
	// }

	@Override
	public void process_done(int port) {
		String plan = port == 0 ? "old" : "new";
		LOG.info("{} plan propagated done before migration finished.", plan);
		synchronized (this) {
			if (port == this.inPortOld) {
				this.finished = true;
				drainBuffer();
			}
		}
		super.process_done(port);
	}

	private void drainBuffer() {
		synchronized (this.newPlanBuffer) {
			LOG.debug("Draining {} elements of MigrationRouter...", this.newPlanBuffer.size());
			for (R e : this.newPlanBuffer) {
				transfer(e);
			}
			this.newPlanBuffer.clear();
		}
	}

	public void setMigrationStartPoint(PointInTime point) {
		LOG.debug("Set migration timestamp to {}.",point);
		this.migrationStart = point;
	}
}
