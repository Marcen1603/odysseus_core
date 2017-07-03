package de.uniol.inf.is.odysseus.wrapper.iec62056.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public abstract class AbstractCOSEMParser<T> {

	protected final int ATTRIBUTE_SIZE;

	protected InputStreamReader reader;
	protected boolean isDone;

	public AbstractCOSEMParser(InputStreamReader reader, int attributes) {
		this.reader = reader;
		this.isDone = false;
		this.ATTRIBUTE_SIZE = attributes;
	}
	
	public AbstractCOSEMParser(InputStreamReader reader, SDFSchema schema) {
		this(reader, schema.getAttributes().size());
	}

	public synchronized Iterator<String> parse() {
		try {
			if (this.reader.ready()) {
				Iterator<String> iter = process();
				return iter != null ? iter : new ArrayList<String>().iterator();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isDone = true;
		return new ArrayList<String>().iterator();
	}

	abstract protected Iterator<String> process();

	/**
	 * Returns {@code true} if the input stream reader reached EOF otherwise
	 * {@code false}.
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
