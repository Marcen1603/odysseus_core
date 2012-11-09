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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Dennis Geesen
 *
 */
public class TreeNode {
	private SDFAttribute attribute;
	
	private Object clazz;
	
	private Map<Object, TreeNode> childs = new HashMap<Object, TreeNode>(); 
	
	public TreeNode(){
		
	}
	
	public SDFAttribute getAttribute() {
		return attribute;
	}
	
	public void addChild(Object value, TreeNode childNode){
		this.childs.put(value, childNode);
	}
	
	public void setAttribute(SDFAttribute attribute) {
		this.attribute = new SDFAttribute(null, attribute.getAttributeName(), attribute.getDatatype());	
	}
	
	public TreeNode getChild(Object value){
		return this.childs.get(value);
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
		for(Entry<Object, TreeNode> e : this.childs.entrySet()){
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
