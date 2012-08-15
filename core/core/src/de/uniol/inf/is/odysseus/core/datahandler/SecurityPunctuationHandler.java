package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.SecurityPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jan S�ren Schwarz
 * @param <SecurityPunctuation>
 *
 */
public class SecurityPunctuationHandler extends AbstractDataHandler<SecurityPunctuation> {

	protected IDataHandler<?>[] securityPunctuationHandlers = null;
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("SecurityPunctuation");
	}

//	private SDFSchema schema;

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
//		this.schema = schema;
	}
	
	
	public SecurityPunctuation readData(ByteBuffer buffer) {
		Object[] objects = new Object[securityPunctuationHandlers.length];
		for(int i=0; i < securityPunctuationHandlers.length; i++) {
			objects[i] = securityPunctuationHandlers[i].readData(buffer);
		}
		
		SecurityPunctuation sp = new SecurityPunctuation(objects);
		return sp;
	}

	@Override
	public SecurityPunctuation readData(ObjectInputStream inputStream) throws IOException {
		return null;
	}

	@Override
	public SecurityPunctuation readData(String string) {
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
	protected IDataHandler<SecurityPunctuation> getInstance(SDFSchema schema) {
		return new SecurityPunctuationHandler(schema);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}
	
	private void createSecurityPunctuationHandlers() {
		SDFSchema securityPunctuationSchema = new SDFSchema("securityPunctuation", 
				new SDFAttribute("SP", "stream", new SDFDatatype("String")),
				new SDFAttribute("SP", "int1", new SDFDatatype("Integer")),
				new SDFAttribute("SP", "int2", new SDFDatatype("Integer")),
				new SDFAttribute("SP", "name", new SDFDatatype("String")),
				new SDFAttribute("SP", "role", new SDFDatatype("String")),
				new SDFAttribute("SP", "int3", new SDFDatatype("Integer")),
				new SDFAttribute("SP", "int4", new SDFDatatype("Long")));
		this.securityPunctuationHandlers = new IDataHandler<?>[securityPunctuationSchema.size()];
		int i = 0;
		for (SDFAttribute attribute : securityPunctuationSchema) {
			String uri = attribute.getDatatype().getURI(false);
			securityPunctuationHandlers[i++] = DataHandlerRegistry.getDataHandler(uri, new SDFSchema("", attribute));
		}
	}

}
