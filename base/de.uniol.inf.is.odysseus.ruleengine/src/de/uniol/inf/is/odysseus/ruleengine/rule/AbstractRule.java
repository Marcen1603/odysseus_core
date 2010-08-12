package de.uniol.inf.is.odysseus.ruleengine.rule;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.system.WorkingMemory;

public abstract class AbstractRule<T, U> implements IRule<T, U> {

	private WorkingMemory currentWorkingMemory;

	public AbstractRule() {

	}

	public void setCurrentWorkingMemory(WorkingMemory wm) {
		this.currentWorkingMemory = wm;
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
		return this.getName() + " (" + getClass().getName() + ")";
	}
}
