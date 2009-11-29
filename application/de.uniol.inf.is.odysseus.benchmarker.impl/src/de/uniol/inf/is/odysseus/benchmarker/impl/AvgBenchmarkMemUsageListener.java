package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEvent;

import de.uniol.inf.is.odysseus.priority.buffer.DirectInterlinkBufferedPipe;

/**
 * Called after initializing the plan and stopping the execution. AvgBenchmarkMemUsageListener produces
 * memory usage statistics based uppon collected data by AvgTempMemUsageListener during the plan execution.
 * @author Jan Steinke
 *
 */
public class AvgBenchmarkMemUsageListener implements IPlanExecutionListener{
	
	// Ueberwacht Groesse von SweepAreas der JoinTIPOs
	List<AvgTempMemUsageListener> listeners = new ArrayList<AvgTempMemUsageListener>();
	
	// Ueberwacht die Groesse der Zwischenpuffer
	List<AvgTempMemUsageListener> listenersBuffer = new ArrayList<AvgTempMemUsageListener>();
	
	// Ueberwacht die Gesamten Groessen der PunctuationStorages in allen Operatoren/Puffern
	List<AvgTempMemUsageListener> listenersPunc = new ArrayList<AvgTempMemUsageListener>();
	
	// Ueberwacht Extra die PunctuationStorages der vorhandenen Joins
	List<AvgTempMemUsageListener> listenersPuncJoin = new ArrayList<AvgTempMemUsageListener>();
	
	
	List<DescriptiveStatistics> statsJoinSweepArea = null;
	List<DescriptiveStatistics> statsBufferSize = null;
	List<DescriptiveStatistics> statsJoinPunctuationStorage = null;
	
	@Override
	public void planExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs) {
		if(eventArgs instanceof PlanExecutionEvent) {
			
			if(((PlanExecutionEvent) eventArgs).getID().equals(PlanExecutionEvent.EXECUTION_STARTED)) {
				System.out.println("Plan execution prepared...collecting memory usage data!");
				for(IPartialPlan each : eventArgs.getSender().getExecutionPlan().getPartialPlans()) {
					for(ISink<?> op : each.getRoots()) {
						addMemListeners(op);
					}
					for(IIterableSource<?> op : each.getIterableSource()) {
						addMemListeners(op);
					}
				}
			}			
			
			if(((PlanExecutionEvent) eventArgs).getID().equals(PlanExecutionEvent.EXECUTION_STOPPED)) {
				System.out.println("Plan execution finished...create benchmark results!");
				statsJoinSweepArea = calcMemUsageBenchmarkResult(listeners);
				statsBufferSize = calcMemUsageBenchmarkResult(listenersBuffer);
				statsJoinPunctuationStorage = calcMemUsageBenchmarkResult(listenersPuncJoin);
				hash.clear();
			}
		}
		
	}

	private Map<Integer,IPhysicalOperator> hash = new HashMap<Integer,IPhysicalOperator>();
	
	@SuppressWarnings("unchecked")
	private void addMemListeners(IIterableSource<?> op) {
		for(PhysicalSubscription<?> sub : op.getSubscriptions()) {
			
			if(op instanceof IBuffer && hash.get(op.hashCode()) == null) {
				System.out.println("Monitoring temp memory usage for: " + op.getName() + " with hash " + op.hashCode());
				
				AvgTempMemUsageListener listener = new AvgTempMemUsageListener(op);
				listenersBuffer.add(listener);
				op.subscribe(listener, POEventType.PushDone);
				
				hash.put(op.hashCode(), op);
			}
			
			if(op instanceof DirectInterlinkBufferedPipe) {
				AvgTempMemUsageListener listener = new AvgTempMemUsageListener(((DirectInterlinkBufferedPipe)op).getStorage());
				listenersPunc.add(listener);
				op.subscribe(listener, POEventType.PushDone);				
			}
			 
			if(((IPhysicalOperator)sub.getTarget()).isSink()) {
				addMemListeners((ISink<?>) sub.getTarget());
			}
		}
		
	}	
	
	@SuppressWarnings("unchecked")
	private void addMemListeners(ISink<?> op) {
		for(PhysicalSubscription<?> sub : op.getSubscribedTo()) {
			
			if(op instanceof JoinTIPO && hash.get(op.hashCode()) == null) {
				System.out.println("Monitoring temp memory usage for: " + op.getName() + " with hash " + op.hashCode());
				
				AvgTempMemUsageListener listener = new AvgTempMemUsageListener((JoinTIPO) op);
				listeners.add(listener);
				op.subscribe(listener, POEventType.PushDone);
				
				AvgTempMemUsageListener listenerStorage = new AvgTempMemUsageListener(((JoinTIPO) op).getStorage());
				listenersPuncJoin.add(listenerStorage);
				op.subscribe(listenerStorage, POEventType.PushDone);
				
				hash.put(op.hashCode(), op);
			}
			
			if(((IPhysicalOperator)sub.getTarget()).isSink()) {
				addMemListeners((ISink<?>) sub.getTarget());
			}
		}
	}
	
	public List<DescriptiveStatistics> calcMemUsageBenchmarkResult(List<AvgTempMemUsageListener> list) {
		List<DescriptiveStatistics> stats = new ArrayList<DescriptiveStatistics>();
		for(AvgTempMemUsageListener each : list) {
			stats.add(each.getStats());	
		}
		return stats;
	}
	
	public List<DescriptiveStatistics> getMemUsageJoinStatistics() {
		return statsJoinSweepArea;
	}

	public List<DescriptiveStatistics> getMemUsageBufferStatistics() {
		return statsBufferSize;
	}
	
	public List<DescriptiveStatistics> getMemUsageJoinPunctuationStatistics() {
		return statsJoinPunctuationStorage;
	}	
}
