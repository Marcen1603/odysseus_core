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
package com.xafero.turjumaan.server.sdk.util;

import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.Unsigned.uint_a;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.serialization.UaStructure;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExpandedNodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INamespaceResolver;
import com.xafero.turjumaan.server.sdk.api.INodeInfo;
import com.xafero.turjumaan.server.sdk.base.DefaultNamespaceResolver;

/**
 * The full node information.
 */
public class FullNodeInfo implements INodeInfo {

	/** The Constant defResolver. */
	private static final INamespaceResolver defResolver = new DefaultNamespaceResolver();

	/** The node id. */
	private NodeId nodeId;

	/** The node class. */
	private NodeClass nodeClass;

	/** The browse name. */
	private QualifiedName browseName;

	/** The display name. */
	private LocalizedText displayName;

	/** The description. */
	private LocalizedText description;

	/** The write mask. */
	private UInteger writeMask;

	/** The user write mask. */
	private UInteger userWriteMask;

	/** The _abstract. */
	private Boolean _abstract;

	/** The symmetric. */
	private Boolean symmetric;

	/** The inverse name. */
	private LocalizedText inverseName;

	/** The contains no loops. */
	private Boolean containsNoLoops;

	/** The event notifier. */
	private UByte eventNotifier;

	/** The value. */
	private Object value;

	/** The data type. */
	private NodeId dataType;

	/** The value rank. */
	private Integer valueRank;

	/** The array dimensions. */
	private UInteger[] arrayDimensions;

	/** The access level. */
	private UByte accessLevel;

	/** The executable. */
	private Boolean executable;

	/** The historizing. */
	private Boolean historizing;

	/** The min sampling interval. */
	private Double minSamplingInterval;

	/** The user executable. */
	private Boolean userExecutable;

	/** The user access level. */
	private UByte userAccessLevel;

	/** The references. */
	private List<ReferenceNode> references;

	/**
	 * Instantiates a new full node information.
	 *
	 * @param nodeId
	 *            the node id
	 * @param nodeClass
	 *            the node class
	 * @param displayName
	 *            the display name
	 */
	public FullNodeInfo(NodeId nodeId, NodeClass nodeClass, String displayName) {
		this.nodeId = nodeId;
		this.nodeClass = nodeClass;
		this.browseName = new QualifiedName(nodeId.getNamespaceIndex(), displayName);
		this.displayName = new LocalizedText(null, displayName);
		this.description = new LocalizedText(null, displayName);
		this.writeMask = uint(0);
		this.userWriteMask = uint(0);
		this._abstract = false;
		this.symmetric = false;
		this.inverseName = new LocalizedText(null, displayName);
		this.containsNoLoops = false;
		this.eventNotifier = ubyte(0);
		this.value = Short.MAX_VALUE;
		this.dataType = Identifiers.Int16;
		this.valueRank = -1;
		@SuppressWarnings("unused")

		UInteger[] arrayDimensions = uint_a(0, 0, 0);
		this.accessLevel = ubyte(1);
		this.executable = false;
		this.historizing = false;
		this.minSamplingInterval = 0.0;
		this.userExecutable = false;
		this.userAccessLevel = ubyte(1);
	}

	@Override
	public Object get(AttributeIds attribute) {
		switch (attribute) {
		case NodeId:
			return nodeId;
		case NodeClass:
			return nodeClass;
		case BrowseName:
			return browseName;
		case DisplayName:
			return displayName;
		case Description:
			return description;
		case WriteMask:
			return writeMask;
		case UserWriteMask:
			return userWriteMask;
		case IsAbstract:
			return _abstract;
		case Symmetric:
			return symmetric;
		case InverseName:
			return inverseName;
		case ContainsNoLoops:
			return containsNoLoops;
		case EventNotifier:
			return eventNotifier;
		case Value:
			return value;
		case DataType:
			return dataType;
		case ValueRank:
			return valueRank;
		case ArrayDimensions:
			return arrayDimensions;
		case AccessLevel:
			return accessLevel;
		case Executable:
			return executable;
		case Historizing:
			return historizing;
		case MinimumSamplingInterval:
			return minSamplingInterval;
		case UserAccessLevel:
			return userAccessLevel;
		case UserExecutable:
			return userExecutable;
		default:
			throw new UnsupportedOperationException(attribute + "!");
		}
	}

	@Override
	public void set(AttributeIds attribute, Object value) {
		switch (attribute) {
		case NodeId:
			nodeId = (NodeId) value;
			break;
		case NodeClass:
			nodeClass = (NodeClass) value;
			break;
		case BrowseName:
			browseName = (QualifiedName) value;
			break;
		case DisplayName:
			displayName = (LocalizedText) value;
			break;
		case Description:
			description = (LocalizedText) value;
			break;
		case WriteMask:
			writeMask = (UInteger) value;
			break;
		case UserWriteMask:
			userWriteMask = (UInteger) value;
			break;
		case IsAbstract:
			_abstract = (boolean) value;
			break;
		case AccessLevel:
			accessLevel = (UByte) value;
			break;
		case ArrayDimensions:
			arrayDimensions = (UInteger[]) value;
			break;
		case ContainsNoLoops:
			containsNoLoops = (Boolean) value;
			break;
		case DataType:
			dataType = (NodeId) value;
			break;
		case EventNotifier:
			eventNotifier = (UByte) value;
			break;
		case Executable:
			executable = (Boolean) value;
			break;
		case Historizing:
			historizing = (Boolean) value;
			break;
		case InverseName:
			inverseName = (LocalizedText) value;
			break;
		case MinimumSamplingInterval:
			minSamplingInterval = (Double) value;
			break;
		case Symmetric:
			symmetric = (Boolean) value;
			break;
		case UserAccessLevel:
			userAccessLevel = (UByte) value;
			break;
		case UserExecutable:
			userExecutable = (Boolean) value;
			break;
		case ValueRank:
			valueRank = (Integer) value;
			break;
		case Value:
			this.value = value;
			break;
		default:
			throw new UnsupportedOperationException(attribute + "!");
		}
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName
	 *            the display name
	 * @return the full node info
	 */
	public FullNodeInfo displayName(LocalizedText displayName) {
		this.displayName = displayName;
		return this;
	}

	/**
	 * Sets the browse name.
	 *
	 * @param browseName
	 *            the browse name
	 * @return the full node info
	 */
	public FullNodeInfo browseName(QualifiedName browseName) {
		this.browseName = browseName;
		return this;
	}

	/**
	 * Sets the description.
	 *
	 * @param description
	 *            the description
	 * @return the full node info
	 */
	public FullNodeInfo desc(LocalizedText description) {
		this.description = description;
		return this;
	}

	/**
	 * Sets the write mask.
	 *
	 * @param writeMask
	 *            the write mask
	 * @return the full node info
	 */
	public FullNodeInfo writeMask(UInteger writeMask) {
		this.writeMask = writeMask;
		return this;
	}

	/**
	 * Sets the user write mask.
	 *
	 * @param userWriteMask
	 *            the user write mask
	 * @return the full node info
	 */
	public FullNodeInfo userWriteMask(UInteger userWriteMask) {
		this.userWriteMask = userWriteMask;
		return this;
	}

	/**
	 * Sets the containsNoLoops flag.
	 *
	 * @param containsNoLoops
	 *            the contains no loops
	 * @return the full node info
	 */
	public FullNodeInfo containsNoLoops(Boolean containsNoLoops) {
		this.containsNoLoops = containsNoLoops;
		return this;
	}

	/**
	 * Sets the historizing flag.
	 *
	 * @param historizing
	 *            the historizing
	 * @return the full node info
	 */
	public FullNodeInfo historizing(Boolean historizing) {
		this.historizing = historizing;
		return this;
	}

	/**
	 * Sets the executable flag.
	 *
	 * @param executable
	 *            the executable
	 * @return the full node info
	 */
	public FullNodeInfo executable(Boolean executable) {
		this.executable = executable;
		return this;
	}

	/**
	 * Sets the user executable flag.
	 *
	 * @param userExecutable
	 *            the user executable
	 * @return the full node info
	 */
	public FullNodeInfo userExecutable(Boolean userExecutable) {
		this.userExecutable = userExecutable;
		return this;
	}

	/**
	 * Sets the inverse name.
	 *
	 * @param inverseName
	 *            the inverse name
	 * @return the full node info
	 */
	public FullNodeInfo inverseName(LocalizedText inverseName) {
		this.inverseName = inverseName;
		return this;
	}

	/**
	 * Sets the event notifier.
	 *
	 * @param eventNotifier
	 *            the event notifier
	 * @return the full node info
	 */
	public FullNodeInfo eventNotifier(UByte eventNotifier) {
		this.eventNotifier = eventNotifier;
		return this;
	}

	/**
	 * Sets the value rank.
	 *
	 * @param valueRank
	 *            the value rank
	 * @return the full node info
	 */
	public FullNodeInfo valueRank(Integer valueRank) {
		this.valueRank = valueRank;
		return this;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the value
	 * @return the full node info
	 */
	public FullNodeInfo value(Object value) {
		this.value = value;
		// Auto-detect other settings based on the value
		if (value != null) {
			Class<?> type = value.getClass();
			if (type.isArray()) {
				// This is an array
				arrayDimensions = null; // TODO: Why NULL?!
				valueRank = type.getName().split("\\[").length - 1;
				// Work on non-empty arrays
				if (Array.getLength(value) >= 1) {
					Object firstVal = Array.get(value, 0);
					// Maybe we can get the correct data type?
					if (firstVal instanceof UaStructure) {
						UaStructure struct = (UaStructure) firstVal;
						dataType = struct.getTypeId();
					}
				}
			} else {
				// This is no array
				arrayDimensions = null;
				valueRank = 0;
				// Maybe we can get the correct data type?
				if (value instanceof UaStructure) {
					UaStructure struct = (UaStructure) value;
					dataType = struct.getTypeId();
				}
			}
		}
		return this;
	}

	/**
	 * Sets the access level.
	 *
	 * @param accessLevel
	 *            the access level
	 * @return the full node info
	 */
	public FullNodeInfo accessLevel(UByte accessLevel) {
		this.accessLevel = accessLevel;
		return this;
	}

	/**
	 * Sets the user access level.
	 *
	 * @param userAccessLevel
	 *            the user access level
	 * @return the full node info
	 */
	public FullNodeInfo userAccessLevel(UByte userAccessLevel) {
		this.userAccessLevel = userAccessLevel;
		return this;
	}

	/**
	 * Sets the symmetric.
	 *
	 * @param symmetric
	 *            the symmetric
	 * @return the full node info
	 */
	public FullNodeInfo symmetric(Boolean symmetric) {
		this.symmetric = symmetric;
		return this;
	}

	/**
	 * Sets the abstract flag.
	 *
	 * @param _abstract
	 *            the _abstract
	 * @return the full node info
	 */
	public FullNodeInfo _abstract(Boolean _abstract) {
		this._abstract = _abstract;
		return this;
	}

	/**
	 * Sets the data type.
	 *
	 * @param dataType
	 *            the data type
	 * @return the full node info
	 */
	public FullNodeInfo dataType(NodeId dataType) {
		this.dataType = dataType;
		return this;
	}

	/**
	 * Sets the minimum sampling interval.
	 *
	 * @param minSamplingInterval
	 *            the min sampling interval
	 * @return the full node info
	 */
	public FullNodeInfo minSamplingInterval(Double minSamplingInterval) {
		this.minSamplingInterval = minSamplingInterval;
		return this;
	}

	/**
	 * Sets the array dimensions.
	 *
	 * @param arrayDimensions
	 *            the array dimensions
	 * @return the full node info
	 */
	public FullNodeInfo arrayDimensions(UInteger[] arrayDimensions) {
		this.arrayDimensions = arrayDimensions;
		return this;
	}

	/**
	 * Gets the references.
	 *
	 * @return the references
	 */
	public List<ReferenceNode> getReferences() {
		return references;
	}

	/**
	 * Adds the reference.
	 *
	 * @param refTypeId
	 *            the reference type id
	 * @param isInverse
	 *            if it is inverse
	 * @param info
	 *            the info
	 */
	public synchronized void addReference(NodeId refTypeId, boolean isInverse, INodeInfo info) {
		addReference(refTypeId, isInverse, info, defResolver);
	}

	/**
	 * Adds the reference.
	 *
	 * @param refTypeId
	 *            the reference type id
	 * @param isInverse
	 *            if it is inverse
	 * @param info
	 *            the info
	 * @param resolver
	 *            the resolver
	 */
	public synchronized void addReference(NodeId refTypeId, boolean isInverse, INodeInfo info,
			INamespaceResolver resolver) {
		addReference(refTypeId, isInverse, (NodeId) info.get(AttributeIds.NodeId), resolver);
	}

	/**
	 * Adds the reference.
	 *
	 * @param refTypeId
	 *            the reference type id
	 * @param isInverse
	 *            if it is inverse
	 * @param nodeId
	 *            the node id
	 * @param resolver
	 *            the resolver
	 */
	public synchronized void addReference(NodeId refTypeId, boolean isInverse, NodeId nodeId,
			INamespaceResolver resolver) {
		addReference(refTypeId, isInverse,
				new ExpandedNodeId(nodeId, resolver.getNamespaceUri(nodeId), resolver.getServerIndex(nodeId)));
	}

	/**
	 * Adds the reference.
	 *
	 * @param refTypeId
	 *            the reference type id
	 * @param isInverse
	 *            if it is inverse
	 * @param targetId
	 *            the target id
	 */
	public synchronized void addReference(NodeId refTypeId, boolean isInverse, ExpandedNodeId targetId) {
		if (references == null)
			references = new LinkedList<ReferenceNode>();
		references.add(new ReferenceNode(refTypeId, isInverse, targetId));
	}
}