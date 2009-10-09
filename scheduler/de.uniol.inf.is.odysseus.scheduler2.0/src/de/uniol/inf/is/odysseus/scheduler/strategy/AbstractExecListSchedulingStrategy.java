package de.uniol.inf.is.odysseus.scheduler.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;

abstract public class AbstractExecListSchedulingStrategy extends
		AbstractSchedulingStrategy {

	/**
	 * contains all operators to schedule, Strategy stops if no more operators
	 * in List
	 */
	protected List<IIterableSource<?>> executionList;

	protected Iterator<IIterableSource<?>> iterator;
	private IIterableSource<?> curSource = null;

	private boolean useIter;

	public AbstractExecListSchedulingStrategy(IPartialPlan plan, boolean iterate) {
		super(plan);
		executionList = calculateExecutionList(plan);
		// Nur die Operatoren im Plan d�rfen gescheduled werden (in den abgeleiten Methoden Aufruf entfernen)
		executionList.retainAll(plan.getIterableSource());
		iterator = executionList.iterator();
		this.useIter = iterate;
	}

	@Override
	public void sourceDone(IIterableSource<?> source) {
		if (!useIter){
			executionList.remove(source);			
		}else if (curSource == source) {
			synchronized (iterator) {
				iterator.remove();
			}
		} else {
			// ist hier was echt komisch gelaufen ... TODO
			throw new IllegalArgumentException();
		}
	}

	
	
	@Override
	public boolean isDone() {
		synchronized (executionList) {
			return executionList.isEmpty();
		}
	}

	abstract protected List<IIterableSource<?>> calculateExecutionList(
			IPartialPlan operators);

	protected Iterator<IIterableSource<?>> getExecutionListIterator() {
		return executionList.iterator();
	}
	
	protected boolean executionListHasElements(){
		return !executionList.isEmpty();
	}
	
	@Override
	public IIterableSource<?> nextSource() {
		if (useIter){
			return nextSourceIter();
		}else{
			return nextSourceStart();
		}
	}
	
	private IIterableSource<?> nextSourceIter() {
		curSource = null;
		synchronized (this.iterator) {
			if (iterator.hasNext()) {
				curSource = iterator.next();
			} else {
				iterator = executionList.iterator();
				if (iterator.hasNext()) {
					curSource = iterator.next();
				}
			}
			return curSource;
		}
	}
	
	private IIterableSource<?> nextSourceStart() {
		Iterator<IIterableSource<?>> iter = getExecutionListIterator();
		while(executionListHasElements()){
			if (iter.hasNext()){
				IIterableSource<?> s = iter.next();
				if (s.hasNext()) return s;
			}else{
				iter = getExecutionListIterator();
			}
		}
		return null;
	}


	protected double calcPathCost(List<ISource<?>> p) {
		double selProd = 1;
		double cAvg = 0;
		for (ISource<?>s:p){
			IMonitoringData<Double> sel = s.getMonitoringData(MonitoringDataTypes.ESTIMATED_PRODUCTIVITY.name);
			IMonitoringData<Double> c = s.getMonitoringData(MonitoringDataTypes.ESTIMATED_PROCESSING_COST.name);
			
			// Abort if not MetadataElement available
			if (sel == null || c == null){
				System.err.println(s+" no MetaData");
				return -1;
			}
			cAvg += (((Number)c.getValue()).doubleValue() * selProd);
			selProd *= sel.getValue();			
		}
		if (p.size() > 0){
			return selProd/cAvg;
		}else{
			return 0;
		}
	}

	/** Calculates for s which (out: schedulable, nonschedulable, virtual) operators are passed on the way to the root node
	 * 	and returns this this 
	 * @param s
	 * 
	 * @return
	 */
	static public void getPathToRoot(ISource<?> s, List<IIterableSource<?>> schedulableOps, List<ISource<?>> allOps, Map<IIterableSource<?>,List<ISource<?>>> virtualOps) {
		if (s instanceof IIterableSource<?>){
			IIterableSource<?> is = (IIterableSource<?>) s;
			if (!schedulableOps.contains(is)){
				schedulableOps.add(is);
			}
		}else if (!allOps.contains(s)){
			allOps.add(s);
			if (virtualOps != null){
				// last from scheduleableOPs is Buffer for following virtual op
				if (schedulableOps.size() > 0){ // TODO: Eigentlich muss dies immer der Fall sein? Hinter jeder Quelle erscheint ein Puffer ...
					IIterableSource<?> key = schedulableOps.get(schedulableOps.size()-1);				
					if (key != null){
						List<ISource<?>> list = virtualOps.get(key);
						if (list == null){
							list = new LinkedList<ISource<?>>();
							virtualOps.put(key, list);
						}
						list.add(s);
					}
				}
			}
		}
		for (PhysicalSubscription<? extends ISink<?>> sub: s.getSubscribtions() ){
			if (sub.getTarget() instanceof ISource<?>){
				getPathToRoot((ISource<?>)sub.getTarget(), schedulableOps, allOps, virtualOps);
			}
		}
	}

	static public void calcForLeafsPathsToRoots(List<ISink<?>> roots, Map<IIterableSource<?>, List<ISource<?>>> virtualOps,
			List<List<IIterableSource<?>>> pathes) {
				List<ISource<?>> leafs = new ArrayList<ISource<?>>();
				List<ISource<?>> lleafs = new ArrayList<ISource<?>>();
				for (ISink<?> sink : roots) {
					findLeafs(sink, lleafs);
				}
				for (ISource<?> leaf : lleafs) {
					if (!leafs.contains(leaf)) {
						leafs.add(leaf);
						List<IIterableSource<?>> schPath = new LinkedList<IIterableSource<?>>();
						List<ISource<?>> opPath = new LinkedList<ISource<?>>();
						Map<IIterableSource<?>, List<ISource<?>>> pVirtualOps = new HashMap<IIterableSource<?>, List<ISource<?>>>();
						getPathToRoot(leaf, schPath, opPath, pVirtualOps);
						virtualOps.putAll(pVirtualOps);
						pathes.add(schPath);
					}
				}
			}

	static public void findLeafs(ISink<?> sink, List<ISource<?>> leafs) {
		if (sink.getSubscribedTo() == null
				|| sink.getSubscribedTo().size() == 0) {
			leafs.add((ISource<?>) sink);
		} else {
			for (PhysicalSubscription<? extends ISource<?>> sub : sink.getSubscribedTo()) {
				if (sub.getTarget() instanceof ISink<?>) {
					findLeafs((ISink<?>) sub.getTarget(), leafs);
				} else {
					// Only ISource
					leafs.add(sub.getTarget());
				}
			}
		}
	}

}
