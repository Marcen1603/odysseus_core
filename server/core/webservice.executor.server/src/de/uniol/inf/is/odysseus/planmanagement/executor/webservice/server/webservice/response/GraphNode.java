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

import de.uniol.inf.is.odysseus.core.sdf.SDFSchemaInformation;

/**
 *
 * @author Dennis Geesen, Thore Stratmann
 * Created at: 12.08.2011
 */
@XmlRootElement
public class GraphNode {

	private Map<GraphNode, Integer> childsMap = new HashMap<GraphNode, Integer>();
	private String name;
	private int id;
	private Map<String, String> parameterInfos;
	private SDFSchemaInformation outputSchema;
	private boolean open;
	private boolean source;
	private boolean sink;
	private boolean pipe;
	private String ownerIDs;
	private int hash;
	private String className;

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
		this.childsMap.clear();
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

	public void setOutputSchema(SDFSchemaInformation outputSchema) {
		this.outputSchema = outputSchema;
	}

	public SDFSchemaInformation getOutputSchema() {
		return this.outputSchema;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	public void setSink(boolean sink) {
		this.sink = sink;
	}

	public void setPipe(boolean pipe) {
		this.pipe = pipe;
	}

	public void setOwnerIDs(String ownerIDs) {
		this.ownerIDs = ownerIDs;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isOpen() {
		return open;
	}

	public boolean isSource() {
		return source;
	}

	public boolean isSink() {
		return sink;
	}

	public boolean isPipe() {
		return pipe;
	}

	public String getOwnerIDs() {
		return ownerIDs;
	}

	public int getHash() {
		return hash;
	}

	public String getClassName() {
		return className;
	}


}