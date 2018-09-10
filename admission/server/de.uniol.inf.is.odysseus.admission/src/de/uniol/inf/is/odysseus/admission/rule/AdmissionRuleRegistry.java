package de.uniol.inf.is.odysseus.admission.rule;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;

import de.uniol.inf.is.odysseus.admission.AdmissionRuleGroup;
import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.IAdmissionRule;

public class AdmissionRuleRegistry {

	private final Table<AdmissionRuleGroup, Class<? extends IAdmissionEvent>, Collection<Class<? extends IAdmissionRule<?>>>> ruleTable = HashBasedTable.create();
	
	private static final Logger LOG = LoggerFactory.getLogger(AdmissionRuleRegistry.class);
	
	@SuppressWarnings("unchecked")
	public synchronized void addAdmissionRuleType( IAdmissionRule<?> rule ) {
		Preconditions.checkNotNull(rule, "rule must not be null!");
		
		AdmissionRuleGroup group = rule.getRouleGroup();
		if( group == null ) {
			throw new RuntimeException("Rule " + rule.getClass().getName() + " has no RuleGroup set!");
		}
		
		Class<? extends IAdmissionEvent> eventType = rule.getEventType();
		if( eventType == null ) {
			throw new RuntimeException("Rule " + rule.getClass().getName() + " has no admission event type set!");
		}
		
		Class<? extends IAdmissionRule<?>> ruleType = (Class<? extends IAdmissionRule<?>>) rule.getClass();
		try {
			createRuleInstance(ruleType);
		} catch( Throwable t ) {
			throw new RuntimeException("Rule " + rule.getClass().getName() + " can not be created with default constructor", t);
		}

		Collection<Class<? extends IAdmissionRule<?>>> ruleTypes = ruleTable.get(group, eventType);
		if( ruleTypes == null ) {
			ruleTypes = Lists.newArrayList();
			ruleTable.put(group, eventType, ruleTypes);
		}
		
		if( !ruleTypes.contains(ruleType)) {
			ruleTypes.add(ruleType);
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void removeAdmissionRuleType( IAdmissionRule<?> rule ) {
		Preconditions.checkNotNull(rule, "rule must not be null!");
		
		AdmissionRuleGroup group = rule.getRouleGroup();
		Class<? extends IAdmissionEvent> eventType = rule.getEventType();
		Class<? extends IAdmissionRule<?>> ruleType = (Class<? extends IAdmissionRule<?>>) rule.getClass();
		
		Collection<Class<? extends IAdmissionRule<?>>> ruleTypes = ruleTable.get(group, eventType);
		if( ruleTypes != null ) {
			ruleTypes.remove(ruleType);
			if( ruleTypes.isEmpty() ) {
				ruleTable.remove(group, eventType);
			}
		}
	}
	
	public synchronized <E extends IAdmissionEvent> List<? extends IAdmissionRule<?>> getAdmissionRules( AdmissionRuleGroup group, Class<E> eventType ) {
		Collection<Class<? extends IAdmissionRule<?>>> ruleTypes = ruleTable.get(group, eventType);
		if( ruleTypes == null || ruleTypes.isEmpty() ) {
			return Lists.newArrayList();
		}
		
		List<IAdmissionRule<E>> rules = createInstances(ruleTypes, eventType);
		
		Collections.sort(rules, new Comparator<IAdmissionRule<?>>() {
			@Override
			public int compare(IAdmissionRule<?> o1, IAdmissionRule<?> o2) {
				return Integer.compare(o1.getPriority(), o2.getPriority());
			}
		});
		
		return rules;
	}

	@SuppressWarnings("unchecked")
	private static <E extends IAdmissionEvent> List<IAdmissionRule<E>> createInstances(Collection<Class<? extends IAdmissionRule<?>>> ruleTypes, Class<E> eventType) {
		List<IAdmissionRule<E>> rules = Lists.newArrayList();
		
		for( Class<? extends IAdmissionRule<?>> ruleType : ruleTypes ) {
			try {
				rules.add((IAdmissionRule<E>) createRuleInstance(ruleType));
			} catch (InstantiationException | IllegalAccessException e) {
				LOG.error("Could not create instance of rule {}", ruleType.getName());
			}
		}
		
		return rules;
	}

	@SuppressWarnings("unchecked")
	private static <E extends IAdmissionEvent> IAdmissionRule<E> createRuleInstance(Class<? extends IAdmissionRule<?>> ruleType) throws InstantiationException, IllegalAccessException {
		return (IAdmissionRule<E>) ruleType.newInstance();
	}
	
	public Collection<Class<? extends IAdmissionEvent>> getAdmissionEventTypes( AdmissionRuleGroup ruleGroup ) {
		Preconditions.checkNotNull(ruleGroup, "ruleGroup must not be null!");
		
		Map<Class<? extends IAdmissionEvent>, Collection<Class<? extends IAdmissionRule<?>>>> rules = ruleTable.row(ruleGroup);
		return rules.keySet();
	}
}
