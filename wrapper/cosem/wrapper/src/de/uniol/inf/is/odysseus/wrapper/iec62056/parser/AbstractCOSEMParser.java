package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.IOException;
import java.io.InputStreamReader;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * An abstract parser to parse COSEM objects to a string representation such
 * that the {@link TupleDataHandler} can produces tuple objects.
 * 
 * @author Jens Plümer
 *
 */
public abstract class AbstractCOSEMParser {

	protected final String[] ATTRIBUTE_NAMES;

	protected int attrOffset;
	protected boolean isDone;
	protected String[] TOKENS;
	protected InputStreamReader reader;

	public AbstractCOSEMParser(InputStreamReader reader, SDFSchema schema) {
		init(reader);
		this.isDone = false;
		ATTRIBUTE_NAMES = new String[schema.getAttributes().size()];
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			ATTRIBUTE_NAMES[i] = schema.getAttributes().get(i).getAttributeName();
		}
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
	 * Invokes a parsing process on the current {@code InputStream} for a
	 * pull-access.
	 * 
	 * @return
	 */
	public final synchronized String parsePullInputStream() {
		try {
			if (reader.ready()) {
				return next();
			}
		} catch (IOException e) {
			/* reader.ready() throws an exception if the stream is closed */
		}
		close();
		return null;
	}

	/**
	 * Invokes a parsing process on the current {@code InputStream} for a push-access.
	 * 
	 * @return
	 */
	public final synchronized String parsePushInputStream() {
		// TODO Implement
		return null;
	}

	/**
	 * Set tokens for the parser.
	 * 
	 * @param tokens
	 *            that are defined by the parser schema
	 * @param offSet
	 *            that marks the beginning of the token attributes in the array
	 */
	public void setTokens(String[] tokens, int offSet) {
		TOKENS = tokens;
		attrOffset = offSet;
	}

	public String[] getTokens() {
		return TOKENS;
	}

	public String[] getAttributeTokens() {
		String[] attributeTokens = new String[TOKENS.length - attrOffset];
		for (int i = attrOffset; i < TOKENS.length; i++) {
			attributeTokens[i - attrOffset] = TOKENS[i];
		}
		return attributeTokens;
	}

	/**
	 * Returns {@code true} if the input stream reader reached EOF otherwise {@code false}.
	 * 
	 * @return
	 */
	public final synchronized boolean isDone() {
		return isDone;
	}

	/**
	 * Closes the input stream reader.
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
