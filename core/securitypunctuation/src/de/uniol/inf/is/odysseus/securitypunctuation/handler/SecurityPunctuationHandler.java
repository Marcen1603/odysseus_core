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
package de.uniol.inf.is.odysseus.securitypunctuation.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.sp.SecurityPunctuation;

/**
 * @author Jan Sören Schwarz
 * @param <SecurityPunctuation>
 *
 */
public class SecurityPunctuationHandler extends AbstractDataHandler<ISecurityPunctuation> {

	protected IDataHandler<?>[] securityPunctuationHandlers = null;
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("SecurityPunctuation");
	}

	protected SDFSchema schema;

	/**
	 * Create a new Security Punctuation Data Handler
	 * 
	 */
	public SecurityPunctuationHandler() {
		createSecurityPunctuationHandlers();
	}

	/**
	 * Create a new Security Punctuation Data Handler
	 * 
	 * @param schema
	 */
	private SecurityPunctuationHandler(SDFSchema schema) {
		createSecurityPunctuationHandlers();
		this.schema = schema;
	}
	
	
	public ISecurityPunctuation readData(ByteBuffer buffer) {
		Object[] objects = new Object[securityPunctuationHandlers.length];
		for(int i=0; i < securityPunctuationHandlers.length; i++) {
			objects[i] = securityPunctuationHandlers[i].readData(buffer);
		}
		
		ISecurityPunctuation sp = new SecurityPunctuation(objects, schema);
		return sp;
	}

	@Override
	public ISecurityPunctuation readData(ObjectInputStream inputStream) throws IOException {
		return null;
	}

	@Override
	public ISecurityPunctuation readData(String string) {
		return null;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
	}

	@Override
	public int memSize(Object attribute) {
		return 0;
	}

	@Override
	protected IDataHandler<ISecurityPunctuation> getInstance(SDFSchema schema) {
		return new SecurityPunctuationHandler(schema);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}
	
	private void createSecurityPunctuationHandlers() {
		schema = new SDFSchema("securityPunctuation", 
				new SDFAttribute("SP", "streamname", new SDFDatatype("String")),
				new SDFAttribute("SP", "tupleStartTS", new SDFDatatype("Long")),
				new SDFAttribute("SP", "tupleEndTS", new SDFDatatype("Long")),
				new SDFAttribute("SP", "attributeNames", new SDFDatatype("String")),
				new SDFAttribute("SP", "role", new SDFDatatype("String")),
				new SDFAttribute("SP", "sign", new SDFDatatype("Integer")),
				new SDFAttribute("SP", "mutable", new SDFDatatype("Integer")),
				new SDFAttribute("SP", "ts", new SDFDatatype("Long")));
		this.securityPunctuationHandlers = new IDataHandler<?>[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			securityPunctuationHandlers[i++] = DataHandlerRegistry.getDataHandler(uri, new SDFSchema("", attribute));
		}
	}
}
