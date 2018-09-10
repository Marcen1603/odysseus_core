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

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Iterables;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

import de.uniol.inf.is.odysseus.server.opcua.wrappers.Data;

/**
 * The root node model for Odysseus.
 */
public class OdysseusNodeModel implements INodeModel, Closeable {

	/** The boot model. */
	private UaNodeModel boot;

	/** The system model. */
	private OdyNodeModel sys;

	/** The user model. */
	private DynNodeModel user;

	/** The data node. */
	final Data data;

	/**
	 * Instantiates a new node model.
	 *
	 * @param path
	 *            the path
	 */
	public OdysseusNodeModel(String path) {
		boot = new UaNodeModel(path);
		data = new Data();
		sys = new OdyNodeModel(data);
		user = new DynNodeModel();
	}

	@Override
	public Iterable<ReferenceNode> browse(NodeId id, BrowseDirection dir) {
		return Iterables.concat(notNull(boot.browse(id, dir)), notNull(sys.browse(id, dir)),
				notNull(user.browse(id, dir)));
	}

	/**
	 * Always retrieve not null iterable.
	 *
	 * @param <T>
	 *            the generic type
	 * @param iterable
	 *            the iterable
	 * @return the iterable
	 */
	private <T> Iterable<T> notNull(Iterable<T> iterable) {
		return iterable == null ? new LinkedList<T>() : iterable;
	}

	@Override
	public DataValue read(NodeId id, AttributeIds attr) {
		DataValue ret;
		ret = readSafe(user, id, attr);
		if (ret == null)
			ret = readSafe(sys, id, attr);
		if (ret == null)
			ret = readSafe(boot, id, attr);
		return ret;
	}

	/**
	 * Read a value the safe way.
	 *
	 * @param model
	 *            the model
	 * @param id
	 *            the id
	 * @param attr
	 *            the attribute
	 * @return the data value
	 */
	private DataValue readSafe(INodeModel model, NodeId id, AttributeIds attr) {
		try {
			return model.read(id, attr);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void write(NodeId id, AttributeIds attr, DataValue val) {
		user.write(id, attr, val);
		sys.write(id, attr, val);
		boot.write(id, attr, val);
	}

	@Override
	public StatusCode execute(NodeId obj, NodeId meth, Variant[] ins, List<Object> outs) {
		StatusCode ret;
		ret = user.execute(obj, meth, ins, outs);
		if (!ret.isGood())
			ret = sys.execute(obj, meth, ins, outs);
		if (!ret.isGood())
			ret = boot.execute(obj, meth, ins, outs);
		return ret;
	}

	@Override
	public void close() throws IOException {
		close(user);
		user = null;
		close(sys);
		sys = null;
		close(boot);
		boot = null;
	}

	/**
	 * Close the model and release all resources.
	 *
	 * @param model
	 *            the model
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void close(INodeModel model) throws IOException {
		if (model == null || !(model instanceof Closeable))
			return;
		((Closeable) model).close();
	}

	/**
	 * Adds a new model to the address space.
	 *
	 * @param xml
	 *            the XML
	 * @return the long
	 */
	public long addModel(String xml) {
		return user.addModel(xml);
	}

	/**
	 * Removes an existing model from the address space.
	 *
	 * @param id
	 *            the id
	 * @return the string
	 */
	public String removeModel(long id) {
		return user.removeModel(id);
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public DynNodeModel getModel() {
		return user;
	}
}