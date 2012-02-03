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
package de.uniol.inf.is.odysseus.objecttracking.util;

import de.uniol.inf.is.odysseus.IClone;

@SuppressWarnings({"rawtypes","unchecked"})
public class ObjectTrackingPair<T1 extends IClone, T2 extends IClone> implements Comparable<ObjectTrackingPair>{

	private T1 key;
	private T2 value;
	
	private int priority;
	
	public ObjectTrackingPair(T1 key, T2 value){
		this.key = key;
		this.value = value;
		this.priority = 0;
	}
	
	private ObjectTrackingPair(ObjectTrackingPair<T1, T2> original) {
		this.key = (T1)original.key.clone();
		this.value = (T2)original.value.clone();
	}

	@Override
	public int compareTo(ObjectTrackingPair o) {
		if(o.getPriority() < this.getPriority()){
			return -1;
		}else if(o.getPriority() == this.getPriority()){
			return 0;
		}else if(o.getPriority()> this.getPriority()){
			return 1;
		}
		return 0;
	}
	
	@Override
	public ObjectTrackingPair<T1, T2> clone() {
		return new ObjectTrackingPair<T1, T2>(this);
	}
	
	public T1 getKey(){
		return this.key;
	}
	
	public T2 getValue(){
		return this.value;
	}
	
	public void increasePriority(){
		this.priority++;
	}
	
	public void decreasePriority(){
		this.priority--;
	}
	
	public void setPriority(int newPriority){
		this.priority = newPriority;
	}
	
	private int getPriority(){
		return priority;
	}	
	
}
