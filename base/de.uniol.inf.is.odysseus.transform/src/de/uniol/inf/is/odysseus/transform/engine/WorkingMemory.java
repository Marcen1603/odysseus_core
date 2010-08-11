package de.uniol.inf.is.odysseus.transform.engine;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.transform.flow.RuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.ITransformRule;

public class WorkingMemory {

	private TransformationEnvironment env;
	private List<Object> objects = new ArrayList<Object>();
	private volatile boolean hasChanged = false;

	public WorkingMemory(TransformationEnvironment env) {
		this.env = env;
	}

	
	public void removeObject(Object o) {
		this.removeObject(o, false);
	}
	
	public void insertObject(Object o) {
		this.insertObject(o, false);
	}
	
	private void insertObject(Object o, boolean isupdate) {
		if(!isupdate){
			TransformationExecutor.getLogger().trace("Inserted into memory: \t" + o);
		}
		this.objects.add(o);
		this.hasChanged = true;
	}

	public void removeObject(Object o, boolean isupdate) {
		if(!isupdate){
			TransformationExecutor.getLogger().trace("Removed from memory: \t" + o);
		}
		this.objects.remove(o);
		this.hasChanged = true;
	}

	public void updateObject(Object o) {
		TransformationExecutor.getLogger().trace("Update memory: \t" + o);
		removeObject(o, true);
		insertObject(o, true);
	}

	public void process() {
		TransformationExecutor.getLogger().trace("Starting transformation process...");
		Iterator<RuleFlowGroup> iterator = TransformationInventory.getInstance().iterator();
		while (iterator.hasNext()) {
			RuleFlowGroup group = iterator.next();
			TransformationExecutor.getLogger().trace("Running group: " + group+"...");			
			runGroup(group);
			TransformationExecutor.getLogger().trace("Group finished: " + group);
		}
	}

	private void runGroup(RuleFlowGroup group) {
		
		Iterator<ITransformRule<?>> iterator = TransformationInventory.getInstance().iteratorRules(group);
		while (iterator.hasNext()) {
			ITransformRule<?> rule = iterator.next();
			runRule(rule);
			if (hasChanged) {
				// run group again...
				TransformationExecutor.getLogger().trace("Working memory has changed, running this group again!");
				hasChanged = false;
				runGroup(group);		
			}
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void runRule(ITransformRule rule) {
		TransformationExecutor.getLogger().trace("Checking rule: " + rule);
		for (Object o : this.objects) {
			if (ruleMatches(o, rule)) {
				TransformationExecutor.getLogger().trace("\t\tType is ok, rule matches...");
				if (rule.isExecutable(o, this.env.getTransformationConfiguration())) {
					TransformationExecutor.getLogger().trace("\t\t... and is executable at the moment. Executing rule...");
					try {
						synchronized (rule) {
							rule.setCurrentWorkingMemory(this);
							rule.transform(o, this.env.getTransformationConfiguration());
						}
						TransformationExecutor.getLogger().trace("\t\t... rule was executed!");
						if (this.hasChanged) {
							// if wm was changed: stop...
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
						TransformationExecutor.getLogger().error(e.getLocalizedMessage());
						throw new RuntimeException("Transformation Failed: "+e.getLocalizedMessage(), e);
					}
				}else{
					TransformationExecutor.getLogger().trace("\t\t... but is NOT executable at the moment.");
				}
			} else {
				continue;
			}

		}
	}

	public List<Object> getCurrentContent() {
		return this.objects;
	}

	private boolean ruleMatches(Object o, ITransformRule<?> rule) {
		for (Method method : rule.getClass().getMethods()) {
			if (method.getName().equals("transform")) {
				Class<?> pt = method.getParameterTypes()[0];
				// Object is not allowed...
				if (!pt.equals(Object.class)) {
					TransformationExecutor.getLogger().trace("\tChecking object (\""+ o +"\") if its type is an instance of the rule type: " + pt.getCanonicalName() + "...");
					if (pt.isInstance(o)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
