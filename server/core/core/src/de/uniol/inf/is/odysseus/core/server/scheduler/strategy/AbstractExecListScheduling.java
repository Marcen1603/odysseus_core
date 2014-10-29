/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.scheduler.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

abstract public class AbstractExecListScheduling extends
		AbstractScheduling {

	/**
	 * contains all operators to schedule, Strategy stops if no more operators
	 * in List
	 */
	protected List<IIterableSource<?>> executionList;

	protected Iterator<IIterableSource<?>> iterator;
	private IIterableSource<?> curSource = null;

	public AbstractExecListScheduling(IPhysicalQuery plan) {
		super(plan);
		executionList = calculateExecutionList(plan);
		// Nur die Operatoren im Plan d�rfen gescheduled werden (in den abgeleiten Methoden Aufruf entfernen)
		executionList.retainAll(plan.getIterableSources());
		iterator = executionList.iterator();
	}

	@Override
	public void sourceDone(IIterableSource<?> source) {
		if (curSource == source) {
			synchronized (iterator) {
				iterator.remove();
			}
		} else {
			// ist hier was echt komisch gelaufen ... TODO
			//throw new IllegalArgumentException();
		}
	}

	
	
	@Override
	public boolean isDone() {
		synchronized (executionList) {
			return executionList.isEmpty();
		}
	}

	abstract protected List<IIterableSource<?>> calculateExecutionList(
			IPhysicalQuery operators);

	protected synchronized Iterator<IIterableSource<?>> getExecutionListIterator() {
		return executionList.iterator();
	}
	
	protected synchronized boolean executionListHasElements(){
		return !executionList.isEmpty();
	}
	
	@Override
	public synchronized IIterableSource<?> nextSource() {
		return nextSourceIter();
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
		}
		
		return 0;
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
		for (AbstractPhysicalSubscription<? extends ISink<?>> sub: s.getSubscriptions() ){
			if (sub.getTarget().isSource()){
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
		if (sink.getSubscribedToSource() == null
				|| sink.getSubscribedToSource().size() == 0) {
			leafs.add((ISource<?>) sink);
		} else {
			for (AbstractPhysicalSubscription<? extends ISource<?>> sub : sink.getSubscribedToSource()) {
				if (sub.getTarget().isSink()) {
					findLeafs((ISink<?>) sub.getTarget(), leafs);
				} else {
					// Only ISource
					leafs.add(sub.getTarget());
				}
			}
		}
	}

}
