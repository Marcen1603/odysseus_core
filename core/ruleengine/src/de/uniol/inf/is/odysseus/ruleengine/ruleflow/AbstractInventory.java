package de.uniol.inf.is.odysseus.ruleengine.ruleflow;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;

public abstract class AbstractInventory implements IRuleFlow {

	private LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>> ruleBase = new LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>>();
	private LinkedList<IRuleFlowGroup> workFlow = new LinkedList<IRuleFlowGroup>();	

	public AbstractInventory() {
		// intentionally left blank
	}

	protected AbstractInventory(AbstractInventory inventory) {
		this.ruleBase = new LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>>(inventory.getCopyOfRuleBase());
		this.workFlow = new LinkedList<IRuleFlowGroup>(inventory.getCopyOfWorkFlow());
	}

	private LinkedList<IRuleFlowGroup> getCopyOfWorkFlow() {
		return this.workFlow;
	}

	private LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>> getCopyOfRuleBase() {
		LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>> copyflow = new LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>>();
		for (Entry<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>> entry : this.ruleBase.entrySet()) {
			IRuleFlowGroup group = entry.getKey();
			PriorityQueue<IRule<?, ?>> newQueue = new PriorityQueue<IRule<?, ?>>();
			for (IRule<?, ?> rule : entry.getValue()) {
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

	@Override
	public void addRuleFlowGroup(IRuleFlowGroup group) {
		if (!this.ruleBase.containsKey(group)) {
			this.ruleBase.put(group, new PriorityQueue<IRule<?, ?>>());
		}
		workFlow.add(group);
		LoggerSystem.printlog(this.getInventoryName()+" - Group added to workflow: " + group + ". New workflow is: " + workFlow.toString());
	}

	private void addRule(IRule<?, ?> rule, IRuleFlowGroup group) {
		if (this.ruleBase.containsKey(group)) {
			if (this.ruleBase.get(group).contains(rule)) {
				LoggerSystem.printlog(Accuracy.WARN, this.getInventoryName()+" - Rule \"" + rule + "\" already exists in inventory!");
			}
			LoggerSystem.printlog(Accuracy.DEBUG, this.getInventoryName()+" - Loading rule - " + rule.getClass().getSimpleName() + ": \"" + rule.getName() + "\" for group: \"" + group + "\"");
			this.ruleBase.get(group).offer(rule);
		} else {
			throw new RuntimeException(this.getInventoryName()+" - Group " + group + " for rule " + rule + " doesn't exist");
		}
	}

	private void removeRule(IRule<?, ?> rule, IRuleFlowGroup group) {
		if (this.ruleBase.containsKey(group)) {
			if (this.ruleBase.get(group).contains(rule)) {
				if (this.ruleBase.get(group).remove(rule)) {
					LoggerSystem.printlog(Accuracy.DEBUG, this.getInventoryName()+" - Rule removed - " + rule.getClass().getSimpleName() + ": \"" + rule.getName() + "\" for group: \"" + group + "\"");
				} else {
					LoggerSystem.printlog(Accuracy.WARN, this.getInventoryName()+" - Removing rule \"" + rule + "\" failed!");
				}
			} else {
				LoggerSystem.printlog(Accuracy.WARN, this.getInventoryName()+" - Unable to remove rule \"" + rule + "\", because it does not exists in the inventory!");
			}
		} else {
			LoggerSystem.printlog(Accuracy.WARN, this.getInventoryName()+" - Unable to remove rule \"" + rule + "\", because the given group does not exists in the inventory!");
		}
	}
	@Override
	public Iterator<IRuleFlowGroup> iterator() {
		return workFlow.iterator();
	}

	@Override
	public Iterator<IRule<?, ?>> iteratorRules(IRuleFlowGroup group) {
		// A PriorityQueue does not guaranty a particular order for the iterator
		// (like workFlow.get(group).iterator())!!
		// thus, here comes a order-safe version...
		PriorityQueue<IRule<?, ?>> rules = this.ruleBase.get(group);
		final IRule<?, ?>[] ruleArray = rules.toArray(new IRule<?, ?>[0]);
		Arrays.sort(ruleArray);
		Iterator<IRule<?, ?>> iterator = new Iterator<IRule<?, ?>>() {
			int position = 0;

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public IRule<?, ?> next() {
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
	
	
	public void bindRuleProvider(IRuleProvider provider) {
		LoggerSystem.printlog(Accuracy.DEBUG, getInventoryName()+" - Loading rules for... "+provider);
		for (IRule<?, ?> rule : provider.getRules()) {			
			this.getCurrentInstance().addRule(rule, rule.getRuleFlowGroup());
		}
	}

	public abstract AbstractInventory getCurrentInstance();

	public void unbindRuleProvider(IRuleProvider provider) {
		LoggerSystem.printlog(Accuracy.DEBUG, getInventoryName()+" - Removing rules for... "+provider);
		for (IRule<?, ?> rule : provider.getRules()) {
			this.getCurrentInstance().removeRule(rule, rule.getRuleFlowGroup());
		}
	}
	
	public String getInventoryName(){
		return "Abstract";
	}

}
