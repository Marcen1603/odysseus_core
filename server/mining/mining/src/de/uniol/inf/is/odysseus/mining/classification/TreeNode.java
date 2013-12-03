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
package de.uniol.inf.is.odysseus.mining.classification;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Dennis Geesen
 *
 */
public class TreeNode {
	private SDFAttribute attribute;
	
	private Object clazz;
	
	private Map<IPredicate<Tuple<?>>, TreeNode> childs = new HashMap<IPredicate<Tuple<?>>, TreeNode>(); 
	
	public TreeNode(){
		
	}
	
	public SDFAttribute getAttribute() {
		return attribute;
	}
	
	public void addChild(IPredicate<Tuple<?>> predicate, TreeNode childNode){
		this.childs.put(predicate, childNode);
	}
	
	public void setAttribute(SDFAttribute attribute) {
		this.attribute = new SDFAttribute(null, attribute.getAttributeName(), attribute.getDatatype());	
	}
	
	public TreeNode getMatchingChild(Tuple<?> tuple){
		for(Entry<IPredicate<Tuple<?>>, TreeNode> e : this.childs.entrySet()){
//			System.out.println("check: ");
//			System.out.println("predicate: "+e.getKey());
//			System.out.println("tuple: "+tuple);
			if(e.getKey().evaluate(tuple)){
				
				return e.getValue(); 
			}
		}
		return null;
	}
	
	public TreeNode getChild(IPredicate<Tuple<?>> predicate){
		return this.childs.get(predicate);
	}

	public Collection<TreeNode> getChildNodes() {
		return childs.values();
	}

	/**
	 * 
	 */
	public void printSubTree() {
		printSubTree("");		
	}
	
	public void printSubTree(String indent){
		System.out.println(indent+"Node: "+attribute);
		System.out.println(indent+"value: "+clazz);
		for(Entry<IPredicate<Tuple<?>>, TreeNode> e : this.childs.entrySet()){
			System.out.println(indent+" - "+e.getKey()+" ");
			e.getValue().printSubTree(indent+"   ");
		}
	}

	public Object getClazz() {
		return clazz;
	}

	public void setClazz(Object clazz) {
		if(clazz==null){
			System.out.println("WARN: CLAZZ IS NULL!");
		}
		this.clazz = clazz;
	}
	
	@Override
	public String toString() {
		return "attribute="+attribute+", class="+clazz+" children="+this.childs.size();		
	}
		 

}
