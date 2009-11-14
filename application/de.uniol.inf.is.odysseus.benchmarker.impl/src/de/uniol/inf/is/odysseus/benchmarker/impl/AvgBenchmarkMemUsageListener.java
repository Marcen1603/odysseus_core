package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
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
	
	List<AvgTempMemUsageListener> listeners = new ArrayList<AvgTempMemUsageListener>();
	List<AvgTempMemUsageListener> listenersBuffer = new ArrayList<AvgTempMemUsageListener>();
	List<AvgTempMemUsageListener> listenersPunc = new ArrayList<AvgTempMemUsageListener>();
	
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
				storeBenchmarkResult(listeners, "memUsage.properties");
				storeBenchmarkResult(listenersBuffer, "memUsageBuffer.properties");
				storeBenchmarkResult(listenersPunc, "memUsagePunc.properties");
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
				AvgTempMemUsageListener listener = new AvgTempMemUsageListener(op);
				System.out.println(op.getName());
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
				
				hash.put(op.hashCode(), op);
			}
			
			if(((IPhysicalOperator)sub.getTarget()).isSink()) {
				addMemListeners((ISink<?>) sub.getTarget());
			}
		}
	}
	
	public void storeBenchmarkResult(List<AvgTempMemUsageListener> list, String file) {
		double mean_sum = 0;
		double operators = 0;
		
		
		double min = -1;
		double max = -1;
		
		for(AvgTempMemUsageListener each : list) {
			double tmp = each.getAverage();
			operators++;
			mean_sum += tmp;
			
			if(min == -1 || tmp < min) {
				min = each.getMin();
			}
			
			if(max == -1 || tmp > max) {
				max = each.getMax();
			}			
			
		}
		
		double mean = -1;
		
		if(operators > 0) {
			mean = mean_sum/operators;
		}
		
		Properties props = new Properties();
		props.put("operators", String.valueOf(operators));
		props.put("mean", String.valueOf(mean));
		props.put("min", String.valueOf(min));
		props.put("max", String.valueOf(max));

		try {
			props.store(new FileWriter(file), "Memory Usage - Benchmark Results:");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
