package de.uniol.inf.is.odysseus.ruleengine.system;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class WorkingMemory {
	
	
	private Map<IRule<?,?>, Object> notexecuted = new HashMap<IRule<?,?>, Object>();
	
	private IWorkingEnvironment<?> env;
	private List<Object> objects = new ArrayList<Object>();
	private volatile boolean hasChanged = false;

	private User caller;

	public WorkingMemory(IWorkingEnvironment<?> env, User caller) {
		this.env = env;
		this.caller = caller;
	}

	public void removeObject(Object o) {
		this.removeObject(o, false);
	}

	public void insertObject(Object o) {
		this.insertObject(o, false);
	}

	private void insertObject(Object o, boolean isupdate) {
		if (!isupdate) {
			LoggerSystem.printlog(Accuracy.TRACE, "Inserted into memory: \t" + o);
		}
		this.objects.add(o);
		this.hasChanged = true;
	}

	public void removeObject(Object o, boolean isupdate) {
		if (!isupdate) {
			LoggerSystem.printlog(Accuracy.TRACE, "Removed from memory: \t" + o);
		}
		boolean removed = this.objects.remove(o);
		if(!removed){
			throw new RuntimeException("Could not remove object " + o + " from working memory");
		}
		this.hasChanged = true;
	}

	public void updateObject(Object o) {
		LoggerSystem.printlog(Accuracy.TRACE, "Update memory: \t" + o);
//		removeObject(o, true);
//		insertObject(o, true);
	}

	public void process() {
		LoggerSystem.printlog(Accuracy.TRACE, "Rule engine started and now looking for matches...");
		for(IRuleFlowGroup group : this.env.getRuleFlow()){
			LoggerSystem.printlog(Accuracy.TRACE, "Running group: " + group + "...");
			runGroup(group);
			LoggerSystem.printlog(Accuracy.TRACE, "Group finished: " + group);
		}
		
		
//		Iterator<IRuleFlowGroup> iterator = this.env.getRuleFlow().iterator();
//		while (iterator.hasNext()) {
//			IRuleFlowGroup group = iterator.next();
//			LoggerSystem.printlog(Accuracy.TRACE, "Running group: " + group + "...");
//			runGroup(group);
//			LoggerSystem.printlog(Accuracy.TRACE, "Group finished: " + group);
//		}
	}

	private void runGroup(IRuleFlowGroup group) {

		Iterator<IRule<?, ?>> iterator = this.env.getRuleFlow().iteratorRules(group);
		while (iterator.hasNext()) {
			IRule<?, ?> rule = iterator.next();
			runRule(rule);
			if (hasChanged) {
				// run group again...
				LoggerSystem.printlog(Accuracy.TRACE, "Working memory has changed, running this group again!");
				hasChanged = false;
				runGroup(group);
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void runRule(IRule rule) {
		LoggerSystem.printlog(Accuracy.TRACE, "Checking rule: " + rule);
		for (Object o : this.objects) {			
			if (ruleMatches(o, rule)) {
				LoggerSystem.printlog(Accuracy.TRACE, "\t\tType is ok, rule matches...");
				synchronized (rule) {
					rule.setCurrentWorkingMemory(this);					
					if (rule.isExecutable(o, this.env.getConfiguration())) {
						LoggerSystem.printlog(Accuracy.TRACE, "\t\t... and is executable at the moment. Executing rule...");
						try {						
							rule.execute(o, this.env.getConfiguration());
							LoggerSystem.printlog(Accuracy.TRACE, "\t\t... rule was executed!");
							if (this.hasChanged) {
								// if wm was changed: stop...
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
							LoggerSystem.printlog(Accuracy.ERROR, e.getLocalizedMessage());
							throw new RuntimeException("Transformation Failed: " + e.getLocalizedMessage(), e);
						}
					} else {
						this.wasnotexecuted(rule, o);
						LoggerSystem.printlog(Accuracy.TRACE, "\t\t... but is NOT executable at the moment.");
					}
				}
			} else {
				continue;
			}

		}
	}

	public List<Object> getCurrentContent() {
		return this.objects;
	}

	private boolean ruleMatches(Object o, IRule<?, ?> rule) {
		for (Method method : rule.getClass().getMethods()) {
			if (method.getName().equals("execute")) {
				Class<?> pt = method.getParameterTypes()[0];
				// Object is not allowed...
				if (!pt.equals(Object.class)) {
					LoggerSystem.printlog(Accuracy.TRACE, "\tChecking object (\"" + o + "\") if its type is an instance of the rule type: " + pt.getCanonicalName() + "...");
					if (pt.isInstance(o)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	private void wasnotexecuted(IRule<?,?> rule, Object ob){
		//TODO: als list, weil so werden alte überschrieben...
		this.notexecuted.put(rule, ob);
	}
	
	public String getDebugTrace(){
		String out = "Following rules were not executed although there was a successful matching to an object:\n";
		for(Entry<IRule<?,?>, Object> entry : this.notexecuted.entrySet()){
			out = out+"Rule: "+entry.getKey();
			out = out+"\n";
		}
		
		return out;
	}

	public User getCaller() {
		return caller;
	}

}
