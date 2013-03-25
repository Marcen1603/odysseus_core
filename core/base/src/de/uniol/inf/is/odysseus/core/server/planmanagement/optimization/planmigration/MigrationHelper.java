/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.planmigration;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IWindow;

//import de.uniol.inf.is.odysseus.physicaloperator.BlockingBuffer;
//import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
//import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
//import de.uniol.inf.is.odysseus.physicaloperator.ISink;
//import de.uniol.inf.is.odysseus.physicaloperator.ISource;
//import de.uniol.inf.is.odysseus.physicaloperator.MetadataCreationPO;
//import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
//import de.uniol.inf.is.odysseus.planmanagement.IWindow;
//import de.uniol.inf.is.odysseus.planmanagement.optimization.planmigration.IPlanMigrationStrategy;

/**
 * Helper class that provides useful methods on physical plans for an
 * {@link IPlanMigrationStrategy}.
 * 
 * @author Tobias Witt
 * 
 */
public class MigrationHelper {

	/**
	 * Finds so called pseudo sources in a subplan with op as root. Potentially
	 * pseudo sources are operators, where isPseudoSource() is true or that are
	 * sources only. A pseudo source is then the first operator downwards in a
	 * row of potentially pseudo sources before an actual source.
	 * 
	 * @param op
	 *            Root of subplan.
	 * @return {@link List} of pseudo sources.
	 */
	public static List<ISource<?>> getPseudoSources(IPhysicalOperator op) {
		List<ISource<?>> sources = new ArrayList<ISource<?>>();
		getPseudoSources(sources, op, null);
		return sources;
	}

	private static void getPseudoSources(List<ISource<?>> sources,
			IPhysicalOperator op, IPhysicalOperator last) {
		if (isPseudoSource(op)) {
			if (last == null) {
				last = op;
			}
		} else if (!op.isSink()) {
			if (last == null) {
				sources.add((ISource<?>) op);
			} else {
				sources.add((ISource<?>) last);
			}
			return;
		} else {
			last = null;
		}

		for (PhysicalSubscription<?> sub : ((ISink<?>) op)
				.getSubscribedToSource()) {
			getPseudoSources(sources, (IPhysicalOperator) sub.getTarget(), last);
		}
	}

	/**
	 * Finds the operators in a subplan that are directly before a source listed
	 * in sources.
	 * 
	 * @param op
	 *            Root of subplan.
	 * @return {@link List} of {@link IPhysicalOperator}s that are before
	 *         sources.
	 */
	public static List<IPhysicalOperator> getOperatorsBeforeSources(
			IPhysicalOperator op, List<ISource<?>> sources) {
		List<IPhysicalOperator> list = new ArrayList<IPhysicalOperator>();
		getOperatorsBeforeSources(list, op, sources);
		return list;
	}

	private static void getOperatorsBeforeSources(List<IPhysicalOperator> list,
			IPhysicalOperator op, List<ISource<?>> sources) {
		for (ISource<?> source : sources) {
			for (PhysicalSubscription<?> sub : source.getSubscriptions()) {
				IPhysicalOperator target = (IPhysicalOperator) sub.getTarget();
				// prevent from casting DataSourceObserverSink to Source
				if (target.isSource()) {
					if (op.hasOwner() == target.hasOwner()) {
						if (op.hasOwner()
								&& op.getOwner().containsAll(target.getOwner())) {
							list.add(target);
							continue;
						}
						if (!op.hasOwner()) {
							list.add(target);
							continue;
						}
					}
				}
			}
		}
	}

	public static List<ISource<?>> getMetaDataUpdatePOs(List<ISource<?>> sources) {
		List<ISource<?>> mdus = new ArrayList<ISource<?>>();
		for (ISource<?> source : sources) {
			for (PhysicalSubscription<?> sub : source.getSubscriptions()) {
				ISource<?> target = (ISource<?>) sub.getTarget();
				if (target instanceof MetadataUpdatePO) {
					mdus.add(target);
				}
			}
		}
		return mdus;
	}

	// private static void getOperatorsBeforeSources(List<IPhysicalOperator>
	// list,
	// IPhysicalOperator op, List<ISource<?>> sources) {
	// for (PhysicalSubscription<?> sub :
	// ((ISink<?>)op).getSubscribedToSource()) {
	// IPhysicalOperator target = (IPhysicalOperator)sub.getTarget();
	// if (sources.contains(target)) {
	// list.add(op);
	// continue;
	// }
	// getOperatorsBeforeSources(list, target, sources);
	// }
	// }

	/**
	 * True for MetadataCreationPO. IWindows are not seen as pseudo sources
	 * because Odysseus does not use the same window in different queries after
	 * a source.
	 * 
	 * @param op
	 *            Operator to check.
	 * @return True, if a potential pseudo source.
	 */
	private static boolean isPseudoSource(IPhysicalOperator op) {
		return op instanceof MetadataCreationPO<?, ?>;
		// || op instanceof IWindow;
	}

	/**
	 * Finds the longest {@link IWindow} in a subplan. Supports only windows of
	 * the same type (element or time based).
	 * 
	 * @param root
	 *            Root of subplan.
	 * @return {@link IWindow} with greatest size.
	 */
	public static IWindow getLongestWindow(IPhysicalOperator root) {
		if (!root.isSink()) {
			return null;
		}
		IWindow wMax = null;
		if (root instanceof IWindow) {
			wMax = (IWindow) root;
		}
		for (PhysicalSubscription<?> sub : ((ISink<?>) root)
				.getSubscribedToSource()) {
			IWindow w = getLongestWindow((IPhysicalOperator) sub.getTarget());
			if (wMax == null
					|| (w != null && w.getWindowSize() > wMax.getWindowSize())) {
				wMax = w;
			}
		}
		return wMax;
	}

	/**
	 * Drains all tuples out of a subplan by explicitly scheduling iterable
	 * sources.
	 * 
	 * @param op
	 *            Root of subplan.
	 */
	public static void drainTuples(IPhysicalOperator op) {
		if (op instanceof BufferPO<?>) {
			return;
		}
		for (PhysicalSubscription<?> sub : ((ISink<?>) op)
				.getSubscribedToSource()) {
			drainTuples((IPhysicalOperator) sub.getTarget());
			if (op instanceof IIterableSource<?>
					&& !(op instanceof BufferPO<?>) && op.isSink()) {
				IIterableSource<?> iterableSource = (IIterableSource<?>) op;
				while (iterableSource.hasNext()) {
					iterableSource.transferNext();
				}
			}
		}
	}

}
