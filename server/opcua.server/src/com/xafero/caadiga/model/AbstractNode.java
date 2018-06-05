/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package com.xafero.caadiga.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The abstract node.
 */
public abstract class AbstractNode {

	/** The display name. */
	@XStreamAlias("DisplayName")
	protected String displayName;

	/** The description. */
	@XStreamAlias("Description")
	protected String description;

	/** The references. */
	@XStreamAlias("References")
	protected List<Reference> references;

	/** The node id. */
	@XStreamAsAttribute
	@XStreamAlias("NodeId")
	protected String nodeId;

	/** The browse name. */
	@XStreamAsAttribute
	@XStreamAlias("BrowseName")
	protected String browseName;

	/** The parent node id. */
	@XStreamAsAttribute
	@XStreamAlias("ParentNodeId")
	private String parentNodeId;

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the references.
	 *
	 * @return the references
	 */
	public List<Reference> getReferences() {
		return references;
	}

	/**
	 * Gets the node id.
	 *
	 * @return the node id
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * Gets the browse name.
	 *
	 * @return the browse name
	 */
	public String getBrowseName() {
		return browseName;
	}

	/**
	 * Gets the parent node id.
	 *
	 * @return the parent node id
	 */
	public String getParentNodeId() {
		return parentNodeId;
	}
}