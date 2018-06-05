/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Topics are needed for topic based filtering in publish/Subscribe systems
 * 
 * @author ChrisToenjesDeye
 *
 */
public class Topic implements Comparable<Topic>{
	private List<Topic> childs;
	private String name;

	public Topic(String name) {
		this.name = name;
		this.childs = new ArrayList<Topic>();
	}

	public List<Topic> getChilds() {
		return childs;
	}

	public void setChilds(List<Topic> childs) {
		this.childs = childs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isHierarchical() {
		if (childs.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public int getNumberOfChilds(){
		return childs.size();
	}

	/**
	 * Checks if topics are equal.
	 * Topics are equal if name is equal
	 */
	@Override
	public boolean equals(Object obj) {
		Topic other;
		if (obj instanceof Topic) {
			other = (Topic) obj;
		} else {
			return false;
		}
		if (this.name.toLowerCase().equals(other.name.toLowerCase())){
			return true;
		}
		return false;

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	/**
	 * Returns the child with a given name
	 * @param name
	 * @return child with name, null if not exists
	 */
	public Topic getChildWithName(String name) {
		for (Topic child : childs) {
			if (child.getName().toLowerCase().equals(name.toLowerCase())){
				return child;
			}
		}
		return null;
	}

	/**
	 * needed for sorting (better performance)
	 */
	@Override
	public int compareTo(Topic other) {
		return name.compareTo(other.getName());
	}

}
