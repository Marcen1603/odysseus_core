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
package de.uniol.inf.is.odysseus.ruleengine.system;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class WorkingMemory {

	public static Logger LOGGER = LoggerFactory.getLogger("ruleengine");
	
	private Map<IRule<?, ?>, Object> notexecuted = new HashMap<IRule<?, ?>, Object>();

	private IWorkingEnvironment<?> env;
	private Collection<Object> objects = new HashSet<Object>();
	private volatile boolean hasChanged = false;
	private int executedRules = 0;
	
	private Map<Class<?>, Collection<Object>> objectMap = new HashMap<Class<?>, Collection<Object>>();
//	private Map<IRuleFlowGroup, Map<Class<?>, List<IRule<?, ?>>>> ruleTree = new HashMap<>();

	private ISession caller;
	private IDataDictionary dd;

	public WorkingMemory(IWorkingEnvironment<?> env, ISession caller, IDataDictionary dd) {
		this.env = env;
		this.caller = caller;
		this.dd = dd;				
	}

	

//	private void initRuleTree(IWorkingEnvironment<?> e) {
//		for (IRuleFlowGroup group : this.env.getRuleFlow()) {
//			Map<Class<?>, List<IRule<?,?>>> groupMap = new HashMap<Class<?>, List<IRule<?, ?>>>();
//			Iterator<IRule<?, ?>> iterator = this.env.getRuleFlow().iteratorRules(group);
//			while(iterator.hasNext()){
//				IRule<?,?> nextRule = iterator.next();
//				Class<?> clazz = nextRule.getConditionClass();
//				if(!groupMap.containsKey(clazz)){
//					groupMap.put(clazz, new ArrayList<IRule<?,?>>());
//				}
//				groupMap.get(clazz).add(nextRule);
//			}
//			this.ruleTree.put(group, groupMap);
//		}		
//	}

	public void removeObject(Object o) {
		this.removeObject(o, false);
	}

	public void insertObject(Object o) {
		this.insertObject(o, false);
	}

	private void insertObject(Object o, boolean isupdate) {
		if (!isupdate) {
			LOGGER.trace("Inserted into memory: \t" + o);
		}
		this.objects.add(o);
		insertIntoTree(o);
		this.hasChanged = true;
	}
	
	private void insertIntoTree(Object o){
		for(Class<?> sc :  getAllInterfacesAndSuperclasses(o)){
			if(!objectMap.containsKey(sc)){
				objectMap.put(sc, new HashSet<>());
			}
			objectMap.get(sc).add(o);			
		}
	}
	
	private void removeFromTree(Object o){
		for(Class<?> sc : getAllInterfacesAndSuperclasses(o)){
			objectMap.get(sc).remove(o);
			if(objectMap.get(sc).isEmpty()){
				objectMap.remove(sc);
			}					
		}
	}
	
	
	private List<Class<?>> getAllInterfacesAndSuperclasses(Object o){
		List<Class<?>> allClasses = new ArrayList<Class<?>>();
		allClasses.add(o.getClass());
		List<Class<?>> supercs = ClassUtils.getAllSuperclasses(o.getClass());		
		if(supercs!=null){
			allClasses.addAll(supercs);
		}
		List<Class<?>> intercs = ClassUtils.getAllInterfaces(o.getClass());		
		if(intercs!=null){
			allClasses.addAll(intercs);
		}
		return allClasses;
		
	}

	public void removeObject(Object o, boolean isupdate) {
		if (!isupdate) {
			LOGGER.trace("Removed from memory: \t" + o);
		}
		boolean removed = this.objects.remove(o);
		if (!removed) {
			throw new RuntimeException("Could not remove object " + o + " from working memory");
		}
		removeFromTree(o);
		this.hasChanged = true;
	}

	public void updateObject(Object o) {
		LOGGER.trace("Update memory: \t" + o);
		// removeObject(o, true);
		// insertObject(o, true);
	}

	public int process() {
		LOGGER.trace("Rule engine started and now looking for matches...");
		for (IRuleFlowGroup group : this.env.getRuleFlow()) {			
			LOGGER.trace("Running group: " + group + "...");			
			runGroup(group);
			while(hasChanged){
				runGroup(group);
			}
			LOGGER.trace("Group finished: " + group);			
		}

		// Iterator<IRuleFlowGroup> iterator =
		// this.env.getRuleFlow().iterator();
		// while (iterator.hasNext()) {
		// IRuleFlowGroup group = iterator.next();
		// LoggerSystem.printlog(Accuracy.TRACE, "Running group: " + group +
		// "...");
		// runGroup(group);
		// LoggerSystem.printlog(Accuracy.TRACE, "Group finished: " + group);
		// }
		return executedRules;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void runGroup(IRuleFlowGroup group){
		hasChanged = false;
		Iterator<IRule<?, ?>> iterator = this.env.getRuleFlow().iteratorRules(group);
		while (iterator.hasNext()) {
			IRule rule = iterator.next();
			Collection<Object> matchingObjects = objectMap.get(rule.getConditionClass());
			if(matchingObjects==null){
				// no objects for this condition clazz
				continue;
			}
			for(Object matchingObject : matchingObjects){
				rule.setCurrentWorkingMemory(this);
				if(rule.isExecutable(matchingObject, this.env.getConfiguration())){
					rule.execute(matchingObject, this.env.getConfiguration());					
					if(this.hasChanged){
						// if wm was changed: stop...
						return;
					}
				}
			}
		}
	}
	
//	private void runGroupOLD(IRuleFlowGroup group) {
//
//		Iterator<IRule<?, ?>> iterator = this.env.getRuleFlow().iteratorRules(group);
//		while (iterator.hasNext()) {
//			IRule<?, ?> rule = iterator.next();
//			runRuleOLD(rule);
//			if (hasChanged) {
//				// run group again...
//				LOGGER.trace("Working memory has changed, running this group again!");
//				hasChanged = false;
//				runGroupOLD(group);
//			}
//		}
//
//	}

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	private void runRuleOLD(IRule rule) {
//		LOGGER.trace("Checking rule: " + rule);
//		for (Object o : this.objects) {
//			if (ruleMatches(o, rule)) {
//				LOGGER.trace("\t\tType is ok, rule matches...");
//				synchronized (rule) {
//					rule.setCurrentWorkingMemory(this);
//					if (rule.isExecutable(o, this.env.getConfiguration())) {
//						LOGGER.trace("\t\t... and is executable at the moment. Executing rule...");
//						try {
//							rule.execute(o, this.env.getConfiguration());
//							executedRules++;
//							LOGGER.trace("\t\t... rule was executed!");
//							if (this.hasChanged) {
//								// if wm was changed: stop...
//								return;
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//							LOGGER.error(e.getLocalizedMessage());
//							throw new RuntimeException("Transformation Failed in rule " + rule + " " + e.getLocalizedMessage(), e);
//						}
//					} else {
//						this.wasnotexecuted(rule, o);
//						LOGGER.trace("\t\t... but is NOT executable at the moment.");
//					}
//				}
//			} else {
//				continue;
//			}
//
//		}
//	}

	public Collection<Object> getCurrentContent() {
		return this.objects;
	}

//	private static boolean ruleMatches(Object o, IRule<?, ?> rule) {
//		Class<?> pt = rule.getConditionClass();
//		LOGGER.trace("\tChecking object (\"" + o + "\") if its type is an instance of the rule type: " + pt.getCanonicalName() + "...");
//		return pt.isInstance(o);
//	}

	// private boolean ruleMatches(Object o, IRule<?, ?> rule) {
	// for (Method method : rule.getClass().getMethods()) {
	// if (method.getName().equals("execute")) {
	// Class<?> pt = method.getParameterTypes()[0];
	// // Object is not allowed...
	// if (!pt.equals(Object.class)) {
	// if
	// (pt.getCanonicalName().equals("de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator.StatelessDetectionSplitAO"))
	// {
	// System.out.println("HERE!");
	// }
	// LoggerSystem.printlog(Accuracy.TRACE, "\tChecking object (\"" + o +
	// "\") if its type is an instance of the rule type: " +
	// pt.getCanonicalName() + "...");
	// if (pt.isInstance(o)) {
	// return true;
	// }
	// }
	// }
	// }
	// return false;
	// }

//	private void wasnotexecuted(IRule<?, ?> rule, Object ob) {
//		//: als list, weil so werden alte überschrieben...
//		this.notexecuted.put(rule, ob);
//	}

	public String getDebugTrace() {
		String out = "Following rules were not executed although there was a successful matching to an object:\n";
		for (Entry<IRule<?, ?>, Object> entry : this.notexecuted.entrySet()) {
			out = out + "Rule: " + entry.getKey();
			out = out + "\n";
		}

		return out;
	}

	public ISession getCaller() {
		return caller;
	}

	public IDataDictionary getDataDictionary() {
		return dd;
	}

}
