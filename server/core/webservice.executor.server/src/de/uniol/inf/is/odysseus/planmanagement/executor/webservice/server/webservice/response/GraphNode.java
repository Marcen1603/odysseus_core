/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Dennis Geesen, Thore Stratmann
 * Created at: 12.08.2011
 */
@XmlRootElement
public class GraphNode {

	private Map<GraphNode, Integer> childsMap = new HashMap<GraphNode, Integer>();
	private String name;	
	private String operatorType;
	private boolean sourceOperator;
	private int id;
	private Map<String, String> parameterInfos;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GraphNode(){
		
	}

	public GraphNode(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Map<GraphNode, Integer> getChildsMap() {
		return this.childsMap;
	}
	
	public void setChildsMap(Map<GraphNode, Integer> childsMap) {
		this.childsMap = childsMap;
	}
	
	public void addChild(GraphNode child, Integer port){
		this.childsMap.put(child, port);
	}
	
	public void addChild(GraphNode child){
		this.childsMap.put(child, 0);
	}
	
	public void removeChild(GraphNode child){
		this.childsMap.remove(child);
	}
	
	public GraphNode[] getChilds() {
		return childsMap.keySet().toArray(new GraphNode[0]);
	}

	public void setChilds(GraphNode[] childs) {
		for(GraphNode g : childs){
			this.addChild(g);
		}
	}
	
	public void setChilds(ArrayList<GraphNode> childs){
		this.childsMap.clear();
		for(GraphNode g : childs){
			this.addChild(g);
		}
	}

	public void setParameterInfos(Map<String, String> parameterInfos) {
		this.parameterInfos = parameterInfos;
	}
	
	public Map<String, String> getParameterInfos() {
		return this.parameterInfos;
	}

	public void setSourceOperator(boolean sourceOperator) {
		this.sourceOperator = sourceOperator;
	}
	
	public boolean isSourceOperator() {
		return this.sourceOperator;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	
	public String getOperatorType() {
		return this.operatorType;
	}
	
}