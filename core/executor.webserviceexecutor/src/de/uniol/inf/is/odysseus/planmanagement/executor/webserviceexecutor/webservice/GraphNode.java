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

package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Dennis Geesen
 * Created at: 12.08.2011
 */
@XmlRootElement
public class GraphNode {

	private ArrayList<GraphNode> childs = new ArrayList<GraphNode>();
	private String name;	
	private int id;
	
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

	public void addChild(GraphNode child){
		this.childs.add(child);
	}
	
	public void removeChild(GraphNode child){
		this.childs.remove(child);
	}
	
	public GraphNode[] getChilds() {
		return childs.toArray(new GraphNode[0]);
	}

	public void setChilds(GraphNode[] childs) {
		for(GraphNode g : childs){
			this.childs.add(g);
		}
	}
	
	public void setChilds(ArrayList<GraphNode> childs){
		this.childs = childs;
	}
	
}
