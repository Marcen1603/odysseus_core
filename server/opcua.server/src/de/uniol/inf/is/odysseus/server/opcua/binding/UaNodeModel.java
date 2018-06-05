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
package de.uniol.inf.is.odysseus.server.opcua.binding;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.StatusCodes;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.enumerated.ServerState;
import com.inductiveautomation.opcua.stack.core.types.structured.BuildInfo;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.inductiveautomation.opcua.stack.core.types.structured.ServerStatusDataType;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.core.sdk.enums.ValueRanks;
import com.xafero.turjumaan.server.sdk.api.INodeModel;
import com.xafero.turjumaan.server.ua.NodeSetModel;

import de.uniol.inf.is.odysseus.server.opcua.Activator;

/**
 * The UA NodeSet model for Odysseus.
 */
public class UaNodeModel extends NodeSetModel implements INodeModel {

	/** The start time. */
	private final DateTime startTime;

	/** The namespace uri. */
	private final String namespaceUri;

	/** The build info. */
	private final BuildInfo buildInfo;

	/**
	 * Instantiates a new UA node model.
	 *
	 * @param fileName
	 *            the file name
	 */
	public UaNodeModel(String fileName) {
		super(fileName);
		startTime = DateTime.now();
		namespaceUri = "http://odysseus.informatik.uni-oldenburg.de/opcua/";
		// Get OSGI context
		BundleContext ctx = Activator.getContext();
		Bundle bundle = ctx.getBundle();
		// Parse version
		String rawVer = (bundle.getVersion() + "");
		int verPos = rawVer.lastIndexOf('.');
		String ver = rawVer.substring(0, verPos);
		String build = rawVer.substring(verPos + 1);
		// Create build info
		buildInfo = new BuildInfo("plugin://" + bundle.getSymbolicName(), bundle.getHeaders().get("Bundle-Vendor"),
				bundle.getHeaders().get("Bundle-Name"), ver, build, new DateTime(new Date(bundle.getLastModified())));
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId id, BrowseDirection dir) {
		Iterable<ReferenceNode> items = null;
		if (id.getNamespaceIndex().intValue() == 0)
			items = patchRefTypes(super.browse(id, dir));
		return items;
	}

	/**
	 * Patch reference types.
	 *
	 * @param refs
	 *            the references
	 * @return the iterable
	 */
	private Iterable<ReferenceNode> patchRefTypes(Iterable<ReferenceNode> refs) {
		Collection<ReferenceNode> items = new LinkedList<>();
		for (ReferenceNode ref : refs) {
			NodeId rt = patchRefType(ref.getReferenceTypeId());
			items.add(new ReferenceNode(rt, ref.getIsInverse(), ref.getTargetId()));
		}
		return items;
	}

	/**
	 * Patch reference type.
	 *
	 * @param refType
	 *            the reference type
	 * @return the node id
	 */
	private NodeId patchRefType(NodeId refType) {
		// If some error occurred, just assume the most common case!
		return refType == null ? Identifiers.Organizes : refType;
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		DataValue item;
		if (id.getNamespaceIndex().intValue() == 0) {
			// Patch some values
			if (attr == AttributeIds.Value) {
				String idStr = id.getIdentifier() + "";
				// Server\Auditing
				if (idStr.equals("2994"))
					return new DataValue(new Variant(false));
				// Server\ServiceLevel
				if (idStr.equals("2267"))
					return new DataValue(new Variant(UByte.valueOf(255)));
				// Server\ServerArray
				if (idStr.equals("2254"))
					return new DataValue(new Variant(new String[] { "urn:localhost:UA Odysseus Server" }));
				// Server\NamespaceArray
				if (idStr.equals("2255"))
					return new DataValue(new Variant(new String[] { "http://opcfoundation.org/UA/", namespaceUri }));
				// Server\ServerStatus
				if (idStr.equals("2256"))
					return new DataValue(
							new Variant(new ServerStatusDataType(startTime, DateTime.now(), ServerState.Running,
									buildInfo, UInteger.valueOf(86400), LocalizedText.english("No reason at all."))));
			}
		}
		// Delegate to parent
		item = super.read(id, attr);
		if (item.getValue().isNull() && attr == AttributeIds.ValueRank)
			// Patch value dimensions
			item = new DataValue(new Variant(ValueRanks.Scalar.getValue()));
		return item;
	}

	@Override
	public StatusCode execute(NodeId obj, NodeId meth, Variant[] args, List<Object> outs) {
		StatusCode item = new StatusCode(StatusCodes.Bad_NotFound);
		if (obj.getNamespaceIndex().intValue() == 0)
			item = super.execute(obj, meth, args, outs);
		return item;
	}

	@Override
	public void write(NodeId id, AttributeIds attr, DataValue value) {
		if (id.getNamespaceIndex().intValue() == 0)
			super.write(id, attr, value);
	}
}