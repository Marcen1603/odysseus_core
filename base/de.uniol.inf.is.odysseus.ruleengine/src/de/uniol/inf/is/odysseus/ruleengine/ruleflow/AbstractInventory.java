package de.uniol.inf.is.odysseus.ruleengine.ruleflow;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;



public abstract class AbstractInventory implements IRuleFlow{
	
	private LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?,?>>> workFlow = new LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?,?>>>();
	
	public AbstractInventory() {
		//intentionally left blank
	}
	
	protected AbstractInventory(AbstractInventory inventory) {
		this.workFlow = new LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?,?>>>(inventory.getCopyOfWorkFlow());	
	}

	private  LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?,?>>> getCopyOfWorkFlow(){
		LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?,?>>> copyflow = new LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?,?>>>();
		for(Entry<IRuleFlowGroup, PriorityQueue<IRule<?,?>>> entry : this.workFlow.entrySet()){
			IRuleFlowGroup group = entry.getKey();
			PriorityQueue<IRule<?,?>> newQueue = new PriorityQueue<IRule<?,?>>();
			for(IRule<?,?> rule : entry.getValue()){
				IRule<?, ?> copyrule;
				try {
					copyrule = rule.getClass().newInstance();
				} catch (Exception e) {				
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				newQueue.add(copyrule);
			}
			copyflow.put(group, newQueue);
			
		}		
		return copyflow;
	}

	public void addRuleFlowGroup(IRuleFlowGroup group) {
		if (!this.workFlow.containsKey(group)) {
			workFlow.put(group, new PriorityQueue<IRule<?,?>>());
		}else{
			LoggerSystem.printlog(Accuracy.WARN, "RuleGroup already exists!");
		}
	}

	public void addRule(IRule<?,?> rule, IRuleFlowGroup group) {
		if (this.workFlow.containsKey(group)) {
			if (this.workFlow.get(group).contains(rule)) {
				LoggerSystem.printlog(Accuracy.WARN, "Rule \"" + rule + "\" already exists in inventory!");
			}
			LoggerSystem.printlog(Accuracy.DEBUG,"Loading rule - " + rule.getClass().getSimpleName() + ": \"" + rule.getName() + "\" for group: \"" + group + "\"");
			this.workFlow.get(group).offer(rule);
		} else {
			throw new RuntimeException("Group " + group + " for rule " + rule + " doesn't exist");
		}
	}

	@Override
	public Iterator<IRuleFlowGroup> iterator() {
		return workFlow.keySet().iterator();
	}

	public Iterator<IRule<?,?>> iteratorRules(IRuleFlowGroup group) {
		// A PriorityQueue does not guaranty a particular order for the iterator
		// (like workFlow.get(group).iterator())!!
		// thus, here comes a order-safe version...
		PriorityQueue<IRule<?,?>> rules = this.workFlow.get(group);
		final IRule<?,?>[] ruleArray = rules.toArray(new IRule<?,?>[0]);
		Arrays.sort(ruleArray);
		Iterator<IRule<?,?>> iterator = new Iterator<IRule<?,?>>() {
			int position = 0;

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public IRule<?,?> next() {
				if (hasNext()) {
					return ruleArray[position++];
				} else {
					throw new NoSuchElementException();
				}
			}

			@Override
			public boolean hasNext() {
				return position < ruleArray.length;
			}
		};

		return iterator;
	}	
}
