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
package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class IntervalPriority extends TimeInterval implements IPriority {

	private static final long serialVersionUID = -313473603482217362L;
	private byte priority = 0;

	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[]{ 
		ITimeInterval.class, IPriority.class
	};
	
	public IntervalPriority() {
	}
	
	public IntervalPriority(IntervalPriority other){
		super(other);
		this.priority = other.priority;
	}
	
	@Override
	public byte getPriority() {
		return this.priority;
	}

	@Override
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	
	@Override
	public IntervalPriority clone() {
		return new IntervalPriority(this);
	}
	
	@Override
	public String toString() {
		return super.toString()+" p= "+this.priority;
	}
	
	@Override
	public String csvToString() {
		return super.csvToString()+";"+this.priority;
	}
	
	@Override
	public String getCSVHeader() {
		return super.getCSVHeader()+";priority";
	}

	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}
}
