package de.uniol.inf.is.odysseus.transform.engine;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import org.slf4j.Logger;

import de.uniol.inf.is.odysseus.transform.flow.IRuleFlow;
import de.uniol.inf.is.odysseus.transform.flow.RuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.ITransformRule;

/**
 * Handles the global state: current loaded rules and current workflow
 * 
 * 
 * @author Dennis Geesen
 * 
 */
public class TransformationInventory implements IRuleFlow {

	private LinkedHashMap<RuleFlowGroup, PriorityQueue<ITransformRule<?>>> workFlow = new LinkedHashMap<RuleFlowGroup, PriorityQueue<ITransformRule<?>>>();
	private Logger logger = TransformationExecutor.getLogger();
	private static TransformationInventory instance = null;

	private TransformationInventory() {
		// intentionally left blank
	}

	public static synchronized TransformationInventory getInstance() {
		if (instance == null) {
			instance = new TransformationInventory();
		}
		return instance;
	}

	public void addRuleFlowGroup(RuleFlowGroup group) {
		if (!this.workFlow.containsKey(group)) {
			workFlow.put(group, new PriorityQueue<ITransformRule<?>>());
		}
	}

	public void addRule(ITransformRule<?> rule, RuleFlowGroup group) {
		if (this.workFlow.containsKey(group)) {
			if (this.workFlow.get(group).contains(rule)) {
				logger.warn("Rule \"" + rule + "\" already exists in inventory!");
			}
			logger.debug("Loading rule - " + rule.getClass().getSimpleName() + ": \"" + rule.getName() + "\" for group: \"" + group + "\"");
			this.workFlow.get(group).offer(rule);
		} else {
			throw new RuntimeException("Group " + group + " for rule " + rule + " doesn't exist");
		}
	}

	@Override
	public Iterator<RuleFlowGroup> iterator() {
		return workFlow.keySet().iterator();
	}

	public Iterator<ITransformRule<?>> iteratorRules(RuleFlowGroup group) {
		// A PriorityQueue does not guaranty a particular order for the iterator
		// (like workFlow.get(group).iterator())!!
		// thus, here comes a order-safe version...
		PriorityQueue<ITransformRule<?>> rules = this.workFlow.get(group);
		final ITransformRule<?>[] ruleArray = rules.toArray(new ITransformRule<?>[0]);
		Arrays.sort(ruleArray);
		Iterator<ITransformRule<?>> iterator = new Iterator<ITransformRule<?>>() {
			int position = 0;

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public ITransformRule<?> next() {
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
