package de.uniol.inf.is.odysseus.transform.rule;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.transform.engine.TransformationExecutor;
import de.uniol.inf.is.odysseus.transform.engine.WorkingMemory;

public abstract class AbstractTransformationRule<T> implements ITransformRule<T> {

	private WorkingMemory currentWorkingMemory;
	
	public AbstractTransformationRule(){
		
	}
	
	public void setCurrentWorkingMemory(WorkingMemory wm){
		this.currentWorkingMemory = wm;
	}
	
	protected List<?> getCollection(){
		List<Object> liste = new ArrayList<Object>(this.getCurrentWorkingMemory().getCurrentContent());
		return liste;				
	}
	
	private WorkingMemory getCurrentWorkingMemory() {
		return this.currentWorkingMemory;
	}

	protected void update(Object operator){
		this.getCurrentWorkingMemory().updateObject(operator);
		
	}
	
	protected void retract(Object operator){
		this.getCurrentWorkingMemory().removeObject(operator);
	}
	
	protected void insert(Object operator){
		this.getCurrentWorkingMemory().insertObject(operator);
	}
	
	@Override
	public int compareTo(ITransformRule<T> o) {
		if(this.getPriority()<o.getPriority()){
			return 1;
		}else{
			if(this.getPriority()>o.getPriority()){
				return -1;
			}else{
				return 0;
			}			
		}
	}
	
	@Override
	public String toString(){
		return this.getName()+" ("+getClass().getName()+")";
	}
	
	public Logger getLogger(){
		String mainName = TransformationExecutor.getLogger().getName();
		return LoggerFactory.getLogger(mainName+" - Rule["+this.getName()+"]");
	}
	
}
