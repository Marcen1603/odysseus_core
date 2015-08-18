/********************************************************************************** 
  * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.datahandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Marco Grawunder
 *
 */
public class NullAwareTupleDataHandler extends TupleDataHandler {

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.NTUPLE.getURI());
	}
	
	public NullAwareTupleDataHandler(){
		super(true);
	}
	
	public NullAwareTupleDataHandler(SDFSchema schema) {
		super(schema,true);
	}
	
	@Override
	public IDataHandler<Tuple<? extends IMetaAttribute>> getInstance(SDFSchema schema) {
		return new NullAwareTupleDataHandler(schema);
	}

	@Override
	public IDataHandler<Tuple<? extends IMetaAttribute>> getInstance(List<SDFDatatype> schema) {
		NullAwareTupleDataHandler handler = new NullAwareTupleDataHandler();
		handler.init(schema);
		return handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.
	 * AbstractDataHandler #getSupportedDataTypes()
	 */
	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}

}
