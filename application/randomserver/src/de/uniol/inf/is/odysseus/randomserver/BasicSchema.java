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
package de.uniol.inf.is.odysseus.randomserver;

import java.util.ArrayList;
import java.util.List;

public class BasicSchema {

	public static enum DataType {
		Integer, Long, String, Double, Time
	};

	private List<BasicAttribute> attributes = new ArrayList<BasicAttribute>();

	public BasicSchema() {

	}

	public void addAttribute(DataType type, String min, String max){
		this.attributes.add(new BasicAttribute(type, min, max));
	}
		
	public void addAttribute(DataType type){
		switch (type) {
		case Double:
			this.addAttribute(type, "0.0", "1.0");
			break;
		case Integer:
			this.addAttribute(type, "0", "100");
			break;
		case String:
			this.addAttribute(type, "0", "10");
			break;
		case Long:
			this.addAttribute(type, "0", String.valueOf(Long.MAX_VALUE));
			break;
		case Time:
			this.addAttribute(type, "0", String.valueOf(Long.MAX_VALUE));
			break;
		default:
			break;
		}
		
	}
	
	
	public List<BasicAttribute> getAttributes(){
		return this.attributes;
	}
	
	@Override
	public String toString(){
		String val = "";
		String del = "";
		for(BasicAttribute at : this.attributes){
			val = val + del +at.getDataType().toString().toUpperCase()+"["+at.getMin()+":"+at.getMax()+"]";
			del = ", ";
		}
		return val;
	}

}
