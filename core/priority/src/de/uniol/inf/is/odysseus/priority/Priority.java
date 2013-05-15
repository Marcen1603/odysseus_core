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
package de.uniol.inf.is.odysseus.priority;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class Priority implements IPriority{
	
	private static final long serialVersionUID = 1837720176871400611L;

	static private NumberFormat defaultNumberFormat = NumberFormat.getInstance();
	
	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[]{ 
		IPriority.class
	};
	
	
	byte prio;
	
	public Priority(){
		this.prio = 0;
	}
	
	private Priority(Priority original){
		this.prio = original.prio;
	}
	
	@Override
	public byte getPriority(){
		return this.prio;
	}
	
	@Override
	public void setPriority(byte prio){
		this.prio = prio;
	}
	
	@Override
	public IPriority clone(){
		return new Priority(this);
	}
	
	@Override
	public String toString() {
		return ""+prio;
	}
	
	@Override
	public String csvToString(NumberFormat ff, NumberFormat nf, boolean withMetadata) {
		return nf.format(prio);
	}
	
	@Override
	public String getCSVHeader() {
		return "Priority";
	}
	
	@Override
	public final String csvToString() {
		return this.csvToString(defaultNumberFormat,defaultNumberFormat,true);
	}

	@Override
	public final String csvToString(boolean withMetada) {
		return this.csvToString(defaultNumberFormat,defaultNumberFormat, withMetada);
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

}
