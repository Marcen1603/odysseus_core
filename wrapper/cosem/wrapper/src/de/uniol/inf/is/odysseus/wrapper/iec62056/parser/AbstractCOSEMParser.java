package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * An abstract parser to parse COSEM objects to a string representation such
 * that the {@link TupleDataHandler} can produces tuple objects. For the
 * definition of the string the following format is used:
 * 
 * <pre>
 * [attribute1|attribute2|attribute3|..]
 * </pre>
 * 
 * @author Jens Plümer
 *
 */
public abstract class AbstractCOSEMParser<T extends IStreamObject<? extends IMetaAttribute>> {

//	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCOSEMParser.class);
	
	protected SDFSchema schema;
	protected Map<String, SDFAttribute> schemaMap;
	protected Map<String, Integer> schemaIndexMap;
	protected int attributeSize;
	protected int currentTupleSize;
	protected boolean isDone;
	protected InputStream inputStream;
	protected String[] tokens;
	protected String rootNode;
	
	private T t; 

	public AbstractCOSEMParser(InputStream inputStream, SDFSchema schema, String rootNode) {
		this.isDone = false;
		this.inputStream = inputStream;
		this.schema = schema;
		this.schemaMap = new HashMap<>();
		this.schemaIndexMap = new HashMap<>();
		this.attributeSize = schema.size();
		this.currentTupleSize = 0;
		this.rootNode = rootNode;
		
		for(int i = 0; i < schema.getAttributes().size(); i++) {
			SDFAttribute attribute = schema.getAttribute(i);
			schemaMap.put(attribute.getAttributeName(), attribute);
			schemaIndexMap.put(attribute.getAttributeName(), i);
		}
		
		init(inputStream);
	}

	/**
	 * Returns the next tuple representation of a smart meter tuple.
	 * @return String representation of a tuple; in general {@code [attribute1|attribute2|..]}
	 */
	abstract protected T next();

	/** Initializes a new {@code InputStreamReader} for the parser. */
	abstract protected void init(InputStream inputStream);

	/**
	 * Invokes a parsing process on the current {@code InputStream}.
	 * @return a string that contains the information about a smart meter
	 */
	public final synchronized T parse() {
		if (!isDone) {
			return next();
		}
		return null;
	}

	/**
	 * Set a new value for the string that represents a smart meter.
	 * @param key is the name of a schema attribute that corresponds to a field name (e.g. a xml or json token)
	 * @param value is the corresponding value for the given key
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected final synchronized void setValue(String key, String value) {
		if(currentTupleSize < attributeSize) {
			if(currentTupleSize == 0) {
				t = (T) new Tuple<>(this.attributeSize, false);
			}

			((Tuple) t).setAttribute(this.schemaIndexMap.get(key), value);
			
			currentTupleSize++;
			if(currentTupleSize == attributeSize) {
				currentTupleSize = 0;
			}
		}
	}
	
	/**
	 * Returns the current string representation and should be called only if one smart meter was processed completely. 
	 * @return string representation of a smart meter
	 */
	protected T getTuple() {
		return t;
	}
	
	/**
	 * Returns {@code true} if the input stream reader reached EOF otherwise {@code false}.
	 * @return true, if the processed stream reached its end otherwise false
	 */
	public final synchronized boolean isDone() {
		return isDone;
	}

	/** Closes the input stream reader and signals that the parser reached the end of the stream. */
	public synchronized void close() {
		try {
			this.isDone = true;
			this.inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
