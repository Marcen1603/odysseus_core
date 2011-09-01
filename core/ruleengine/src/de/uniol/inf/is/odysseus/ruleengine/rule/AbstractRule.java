/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.ruleengine.rule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.ruleengine.system.WorkingMemory;
import de.uniol.inf.is.odysseus.usermanagement.User;

public abstract class AbstractRule<T, U> implements IRule<T, U> {

	protected static Logger LOGGER = LoggerFactory.getLogger(AbstractRule.class);
	
	private WorkingMemory currentWorkingMemory;
	private Class<?> condtionClass = null;

	public AbstractRule() {

	}
	
	

	@Override
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

	protected User getCaller(){
		return this.getCurrentWorkingMemory().getCaller();
	}
	
	protected IDataDictionary getDataDictionary(){
		return this.getCurrentWorkingMemory().getDataDictionary();
	}
	
	@Override
	public String toString() {
		return this.getName() + " (" + getClass().getName() + ") - "+super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		IRule<?, ?> rule = (IRule<?,?>)obj;
		
		return getName().equals(rule.getName());
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	public Class<?> getConditionClass(){	
		if(this.condtionClass == null){
			for (Method method : getClass().getMethods()) {
				if(method.getName().equals("execute")){
					Class<?> pt = method.getParameterTypes()[0];
					if (!pt.equals(Object.class)) {
						this.condtionClass = pt;
					}
				}
			}
		}
		return this.condtionClass;
	}
}
