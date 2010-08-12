package de.uniol.inf.is.odysseus.ruleengine.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.system.WorkingMemory;

public abstract class AbstractRule<T, U> implements IRule<T, U> {

	private WorkingMemory currentWorkingMemory;

	public AbstractRule() {

	}
	
	

	public void setCurrentWorkingMemory(WorkingMemory wm) {
		this.currentWorkingMemory = wm;
	}
	
	protected <K> List<K> getAllOfSameTyp(K k){
		List<K> liste = new ArrayList<K>();
		for (Object object : getCollection()) {
			if (k.getClass().isInstance(object)) {
				@SuppressWarnings("unchecked")
				K o = (K) object;
				liste.add(o);
			}
		}
		return liste;
	}

	protected List<?> getCollection() {
		List<Object> liste = new ArrayList<Object>(this.getCurrentWorkingMemory().getCurrentContent());
		return liste;
	}

	private WorkingMemory getCurrentWorkingMemory() {
		return this.currentWorkingMemory;
	}

	protected void update(Object operator) {
		this.getCurrentWorkingMemory().updateObject(operator);

	}

	protected void retract(Object operator) {
		this.getCurrentWorkingMemory().removeObject(operator);
	}

	protected void insert(Object operator) {
		this.getCurrentWorkingMemory().insertObject(operator);
	}
	
	protected void updateAll(Collection<Object> objects){
		for(Object o : objects){
			update(o);
		}
	}

	@Override
	public int compareTo(IRule<T, U> o) {
		if (this.getPriority() < o.getPriority()) {
			return 1;
		} else {
			if (this.getPriority() > o.getPriority()) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public String toString() {
		return this.getName() + " (" + getClass().getName() + ") - "+super.hashCode();
	}
}
