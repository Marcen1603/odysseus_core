package de.uniol.inf.is.odysseus.planmanagement.optimization.migration;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.intervalapproach.window.AbstractWindowTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.BlockingBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.MetadataCreationPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

/**
 * 
 * @author Tobias Witt
 *
 */
public class MigrationHelper {
	
	public static List<ISource<?>> getPseudoSources(IPhysicalOperator op) {
		List<ISource<?>> sources = new ArrayList<ISource<?>>();
		getPseudoSources(sources,op);
		return sources;
	}
	
	private static void getPseudoSources(List<ISource<?>> sources, IPhysicalOperator op) {
		if (isPseudoSource(op)) {
			sources.add((ISource<?>)op);
			return;
		}
		for (PhysicalSubscription<?> sub : ((ISink<?>)op).getSubscribedToSource()) {
			getPseudoSources(sources, (IPhysicalOperator)sub.getTarget());
		}
	}
	
	public static List<IPhysicalOperator> getOperatorsBeforeSources(IPhysicalOperator op) {
		List<IPhysicalOperator> list = new ArrayList<IPhysicalOperator>();
		getOperatorsBeforeSources(list, op);
		return list;
	}
	
	private static void getOperatorsBeforeSources(List<IPhysicalOperator> list, IPhysicalOperator op) {
		for (PhysicalSubscription<?> sub : ((ISink<?>)op).getSubscribedToSource()) {
			IPhysicalOperator target = (IPhysicalOperator)sub.getTarget();
			if (isPseudoSource(target)) {
				list.add(op);
				continue;
			}
			getOperatorsBeforeSources(list, target);
		}
	}
	
	private static boolean isPseudoSource(IPhysicalOperator op) {
		return !op.isSink() || op instanceof MetadataCreationPO<?, ?>;
	}
	
	public static long getLongestWindowSize(IPhysicalOperator root) {
		if (!root.isSink()) {
			return 0L;
		}
		long wMax = 0L;
		if (root instanceof AbstractWindowTIPO) {
			wMax = ((AbstractWindowTIPO)root).getWindowSize();
		}
		for (PhysicalSubscription<?> sub : ((ISink<?>)root).getSubscribedToSource()) {
			long w = getLongestWindowSize((IPhysicalOperator)sub.getTarget());
			if (w > wMax) {
				wMax = w;
			}
		}
		return wMax;
	}
	
	public static void drainTuples(IPhysicalOperator op) {
		if (op instanceof BlockingBuffer<?>) {
			return;
		}
		for (PhysicalSubscription<?> sub : ((ISink<?>)op).getSubscribedToSource()) {
			drainTuples((IPhysicalOperator) sub.getTarget());
			if (op instanceof IIterableSource<?>) {
				IIterableSource<?> iterableSource = (IIterableSource<?>) op;
				while (iterableSource.hasNext()) {
					iterableSource.transferNext();
				}
				// TODO: Was passiert, wenn Puffer im Plan volllaufen?
			}
		}
	}

}
