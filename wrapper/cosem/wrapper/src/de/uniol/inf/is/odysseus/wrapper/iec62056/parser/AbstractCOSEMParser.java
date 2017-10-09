package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.IOException;
import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;

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
public abstract class AbstractCOSEMParser {

//	private static final Logger logger = LoggerFactory.getLogger(AbstractCOSEMParser.class.getSimpleName());
	
	protected final StringBuilder builderCopy;
	protected StringBuilder builder;
	protected boolean isDone;
	protected InputStreamReader reader;
	protected String[] tokens;

	public AbstractCOSEMParser(InputStreamReader reader, StringBuilder builder, String[] tokens) {
		this.tokens = tokens;
		this.isDone = false;
		this.builder = builder;
		this.builderCopy = builder;
		init(reader);
	}

	/**
	 * Returns the next tuple representation of a smart meter tuple.
	 * 
	 * @return String representation of a tuple; in general
	 *         {@code [attribute1|attribute2|..]}
	 */
	abstract protected String next();

	/**
	 * Initializes a new {@code InputStreamReader} for the parser.
	 */
	abstract protected void init(InputStreamReader reader);

	/**
	 * Invokes a parsing process on the current {@code InputStream}.
	 * @return a string that contains the information about a smart meter
	 */
	public final synchronized String parse() {
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
	protected final synchronized void setValue(String key, String value) {
		int index = this.builder.indexOf(key);
		if(index != -1) {
			this.builder = new StringBuilder(this.builder.toString().replaceFirst(key, value));
		}
	}
	
	/**
	 * Returns the current string representation and should be called only if one smart meter was processed completely. 
	 * @return string representation of a smart meter
	 */
	protected String getStringRepresentation() {
		String representation = this.builder.toString();
		this.builder = null;
		this.builder = new StringBuilder(builderCopy);
		return representation;
	}
	
	/**
	 * Returns {@code true} if the input stream reader reached EOF otherwise {@code false}.
	 * 
	 * @return true, if the processed stream reached its end otherwise false
	 */
	public final synchronized boolean isDone() {
		return isDone;
	}

	/**
	 * Closes the input stream reader and signals that the parser reached the end of the stream.
	 */
	public synchronized void close() {
		try {
			this.isDone = true;
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
