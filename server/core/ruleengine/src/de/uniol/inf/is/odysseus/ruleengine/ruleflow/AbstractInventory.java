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
package de.uniol.inf.is.odysseus.ruleengine.ruleflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.system.WorkingMemory;

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

	protected AbstractInventory(AbstractInventory inventory, Set<String> rulesToApply) {
		if (rulesToApply == null) {
			this.ruleBase = new LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>>(
					inventory.getCopyOfRuleBase());
		} else {
			LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>> copy = inventory.getCopyOfRuleBase();
			for (Entry<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>> e : copy.entrySet()) {
				Iterator<IRule<?, ?>> iter = e.getValue().iterator();
				while (iter.hasNext()) {
					if (!rulesToApply.contains(iter.next().getName())) {
						iter.remove();
					}
				}
			}
			this.ruleBase = new LinkedHashMap<>(copy);
		}
		this.workFlow = new LinkedList<IRuleFlowGroup>(inventory.getCopyOfWorkFlow());
	}

	private LinkedList<IRuleFlowGroup> getCopyOfWorkFlow() {
		return this.workFlow;
	}

	private LinkedHashMap<IRuleFlowGroup, PriorityQueue<IRule<?, ?>>> getCopyOfRuleBase() {
		synchronized (ruleBase) {
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
	}

	@Override
	public void addRuleFlowGroup(IRuleFlowGroup group) {
		synchronized (ruleBase) {
			if (!this.ruleBase.containsKey(group)) {
				this.ruleBase.put(group, new PriorityQueue<IRule<?, ?>>());
			}
			workFlow.add(group);
			WorkingMemory.LOGGER.debug(this.getInventoryName() + " - Group added to workflow: " + group
					+ ". New workflow is: " + workFlow.toString());
		}
	}

	public synchronized void addRule(IRule<?, ?> rule) {
		synchronized (ruleBase) {
			IRuleFlowGroup group = rule.getRuleFlowGroup();
			if (!containsRule(rule)) {
				WorkingMemory.LOGGER
						.debug(this.getInventoryName() + " - Loading rule - " + rule.getClass().getSimpleName() + ": \""
								+ rule.getName() + "\" for group: \"" + group + "\"");
				this.ruleBase.get(group).offer(rule);
			} else {
				// For DEBUGGING ONLY
				@SuppressWarnings("unused")
				int a = 0;
			}
		}
	}

	public synchronized boolean containsRule(IRule<?, ?> rule) {
		IRuleFlowGroup group = rule.getRuleFlowGroup();
		return ruleBase.containsKey(group) && ruleBase.get(group).contains(rule);
	}

	public void removeRule(IRule<?, ?> rule) {
		synchronized (ruleBase) {
			IRuleFlowGroup group = rule.getRuleFlowGroup();
			if (this.ruleBase.containsKey(group)) {
				if (this.ruleBase.get(group).contains(rule)) {
					if (this.ruleBase.get(group).remove(rule)) {
						WorkingMemory.LOGGER
								.debug(this.getInventoryName() + " - Rule removed - " + rule.getClass().getSimpleName()
										+ ": \"" + rule.getName() + "\" for group: \"" + group + "\"");
					} else {
						WorkingMemory.LOGGER
								.warn(this.getInventoryName() + " - Removing rule \"" + rule + "\" failed!");
					}
				} else {
					WorkingMemory.LOGGER.warn(this.getInventoryName() + " - Unable to remove rule \"" + rule
							+ "\", because it does not exists in the inventory!");
				}
			} else {
				WorkingMemory.LOGGER.warn(this.getInventoryName() + " - Unable to remove rule \"" + rule
						+ "\", because the given group does not exists in the inventory!");
			}
		}
	}

	@Override
	public Iterator<IRuleFlowGroup> iterator() {
		return workFlow.iterator();
	}

	@Override
	public Iterator<IRule<?, ?>> iteratorRules(IRuleFlowGroup group, Collection<Class<?>> filter) {
		synchronized (ruleBase) {
			// A PriorityQueue does not guaranty a particular order for the
			// iterator
			// (like workFlow.get(group).iterator())!!
			// thus, here comes a order-safe version...

			PriorityQueue<IRule<?, ?>> rules = new PriorityQueue<>(this.ruleBase.get(group));
			if (!filter.isEmpty()) {
				Iterator<IRule<?, ?>> iter = rules.iterator();
				while (iter.hasNext()) {
					if (!filter.contains(iter.next().getConditionClass())) {
						iter.remove();
					}
				}
			}
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
					}

					throw new NoSuchElementException();
				}

				@Override
				public boolean hasNext() {
					return position < ruleArray.length;
				}
			};

			return iterator;
		}
	}

	@Override
	public Iterator<IRule<?, ?>> iteratorRules(IRuleFlowGroup group) {
		return this.iteratorRules(group, new ArrayList<Class<?>>());
	}

	public void bindRuleProvider(IRuleProvider provider) {
		WorkingMemory.LOGGER.debug(getInventoryName() + " - Loading rules for... " + provider);
		List<IRule<?, ?>> rules = provider.getRules();
		for (IRule<?, ?> rule : rules) {
			this.getCurrentInstance().addRule(rule);
		}
	}

	public abstract AbstractInventory getCurrentInstance();

	public void unbindRuleProvider(IRuleProvider provider) {
		WorkingMemory.LOGGER.debug(getInventoryName() + " - Removing rules for... " + provider);
		for (IRule<?, ?> rule : provider.getRules()) {
			if (this.getCurrentInstance().containsRule(rule)) {
				this.getCurrentInstance().removeRule(rule);
			}
		}
	}

	public String getInventoryName() {
		return "Abstract";
	}

}
