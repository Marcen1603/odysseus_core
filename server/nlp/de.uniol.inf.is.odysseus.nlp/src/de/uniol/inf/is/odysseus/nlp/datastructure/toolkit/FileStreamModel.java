package de.uniol.inf.is.odysseus.nlp.datastructure.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;

public abstract class FileStreamModel<A extends IAnnotation> extends AnnotationModel<A> {
	public FileStreamModel(HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
		Option option = configuration.get(AnnotationModel.NAME+"."+this.identifier());
		if(option == null)
			throw new NLPModelNotFoundException(AnnotationModel.NAME+"."+this.identifier());
		File file = new File((String)option.getValue());
		try {
			load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new NLPModelNotFoundException(e.getMessage());
		} catch (IOException e) {
			throw new NLPException(e.getMessage());
		}
	}
	
	/**
	 * Creates new OpenNLPModel with loading an existing model with a specified InputStream.
	 * @param stream
	 * @throws NLPException if loading fails
	 */
	protected FileStreamModel(InputStream stream) throws NLPException{
		try {
			load(stream);
		} catch (IOException e) {
			throw new NLPException(e.getMessage());
		}
	}


	public FileStreamModel() {
		super();
	}

	/**
	 * Initializes Model with a model loaded by an input stream.
	 * @param stream containing model data
	 * @throws IOException if loading fails
	 */
	protected abstract void load(InputStream stream) throws IOException;
}
