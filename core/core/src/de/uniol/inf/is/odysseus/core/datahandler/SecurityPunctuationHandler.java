package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jan Sören Schwarz
 *
 */
public class SecurityPunctuationHandler extends AbstractDataHandler<Tuple<?>> {

	protected Boolean securityPunctuationEnabled = true;
	protected IDataHandler<?>[] securityPunctuationHandlers = null;

	Logger LOG = LoggerFactory.getLogger(SecurityPunctuationHandler.class);
	static protected List<String> types = new ArrayList<String>();
	static {
		types.add("SecurityPunctuation");
	}

	private SDFSchema schema;
	
	public SecurityPunctuationHandler() {
//		if(securityPunctuationEnabled) {
			createSecurityPunctuationHandlers();
//		}
	}

	/**
	 * Create a new Security Punctuation Data Handler
	 * 
	 * @param schema
	 */
	private SecurityPunctuationHandler(SDFSchema schema) {
		LOG.debug("SecurityPunctuationHandler");
		this.schema = schema;
	}
	
	
	@Override
	public Tuple<?> readData(ByteBuffer buffer) {
		LOG.debug("readData(ByteBuffer buffer) in SPHandler");

		Object[] objects = new Object[securityPunctuationHandlers.length];
		for(int i=0; i < securityPunctuationHandlers.length; i++) {
			objects[i] = securityPunctuationHandlers[i].readData(buffer);
		}
		
		boolean requiresDeepClone = false;
		@SuppressWarnings("rawtypes")
		Tuple tuple = new Tuple(objects, requiresDeepClone);
		return tuple;
	}

	@Override
	public Tuple<?> readData(ObjectInputStream inputStream) throws IOException {
		LOG.debug("readData");
		return null;
	}

	@Override
	public Tuple<?> readData(String string) {
		LOG.debug("readData");
		return null;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		LOG.debug("writeData");		
	}

	@Override
	public int memSize(Object attribute) {
		LOG.debug("memSize");
		return 0;
	}

	@Override
	protected IDataHandler<Tuple<?>> getInstance(SDFSchema schema) {
		return new SecurityPunctuationHandler(schema);
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return Collections.unmodifiableList(types);
	}
	
	private void createSecurityPunctuationHandlers() {
		SDFSchema securityPunctuationSchema = new SDFSchema("securityPunctuation", 
				new SDFAttribute("SP", "isSP", new SDFDatatype("String")),
				new SDFAttribute("SP", "stream", new SDFDatatype("String")),
				new SDFAttribute("SP", "int1", new SDFDatatype("Integer")),
				new SDFAttribute("SP", "int2", new SDFDatatype("Integer")),
				new SDFAttribute("SP", "name", new SDFDatatype("String")),
				new SDFAttribute("SP", "role", new SDFDatatype("String")),
				new SDFAttribute("SP", "int3", new SDFDatatype("Integer")),
				new SDFAttribute("SP", "in4", new SDFDatatype("Integer")));
		this.securityPunctuationHandlers = new IDataHandler<?>[securityPunctuationSchema.size()];
		int i = 0;
		for (SDFAttribute attribute : securityPunctuationSchema) {
			SDFDatatype type = attribute.getDatatype();
			String uri = attribute.getDatatype().getURI(false);
			securityPunctuationHandlers[i++] = DataHandlerRegistry.getDataHandler(uri, new SDFSchema("", attribute));
		}
	}

}
