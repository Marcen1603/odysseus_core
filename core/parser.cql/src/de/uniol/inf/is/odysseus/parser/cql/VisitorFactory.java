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
package de.uniol.inf.is.odysseus.parser.cql;

import java.util.HashMap;
import java.util.Map;

public class VisitorFactory {

	private Map<String, IVisitor> visitorMap = null;
	
	static VisitorFactory instance = null; 
	
	private VisitorFactory(){
		visitorMap = new HashMap<String, IVisitor>();
	}
	
	public static synchronized VisitorFactory getInstance(){
		if (instance == null){
			instance = new VisitorFactory();
		}
		return instance;
	}
	
	public void removeVisitor(String name){
		this.visitorMap.remove(name);
	}
	
	public boolean setVisitor(IVisitor visitor, String name){
		visitorMap.put(name, visitor);
		return true;
	}
	
	public IVisitor getVisitor(String name){
		return visitorMap.get(name);
	}
	
	
}
