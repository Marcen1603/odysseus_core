/** Copyright 2011 The Odysseus Team
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
/**
 * 
 * @author Dennis Geesen
 * Created at: 06.02.2012
 */
public class VisitorService {
	
	public void addVisitor(IVisitor visitor){
		String name = generateName(visitor);
		System.out.println("add visitor: "+name);
		VisitorFactory.getInstance().setVisitor(visitor, name);
	}
	
	public void removeVisitor(IVisitor visitor){
		String name = generateName(visitor);
		System.out.println("remove visitor: "+name);
		VisitorFactory.getInstance().removeVisitor(name);
	}
	
	
	private String generateName(IVisitor visitor){
		return visitor.getClass().getCanonicalName();
	}
}
