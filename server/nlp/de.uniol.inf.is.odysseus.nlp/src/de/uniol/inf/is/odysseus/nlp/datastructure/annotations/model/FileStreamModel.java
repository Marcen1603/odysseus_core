package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;

public abstract class FileStreamModel<A extends IAnnotation> extends AnnotationModel<A> {
	private static final long serialVersionUID = -8698291176230199423L;

	/**
	 * Creates OpenNLP Model based on an user-specified configuration HashMap.
	 * @param configuration contains information about model-location
	 * @throws NLPModelNotFoundException if the file was not found
	 * @throws NLPException if something goes wrong besides the model was not found
	 */
	public FileStreamModel(HashMap<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
		Option option = configuration.get(AnnotationModel.NAME+"."+this.identifier());
		if(option == null)
			throw new NLPModelNotFoundException(AnnotationModel.NAME+"."+this.identifier());
		
		String[] paths = new String[1];
		
		if(option.getValue() instanceof String){
			paths[0] = (String)option.getValue();
		}else if(option.getValue() instanceof String[]){
			paths = (String[])option.getValue();
		}
		
		if(paths.length == 0)
			throw new NLPModelNotFoundException(AnnotationModel.NAME+"."+this.identifier());
		
		FileInputStream[] streams = new FileInputStream[paths.length];
		try{
			for(int i = 0; i < paths.length; i++){
				streams[i] = new FileInputStream(new File(paths[i]));
			}
			load(streams);
		} catch (FileNotFoundException e) {
			throw new NLPModelNotFoundException(e.getMessage());
		} catch (IOException e) {
			throw new NLPException(e.getMessage());
		} finally {
			for(FileInputStream stream : streams){
				if(stream != null)
					try {
						stream.close();
					} catch (IOException ignored) {
					}
			}
		}
	}
	


	public FileStreamModel() {
		super();
	}

	/**
	 * Initializes Model with a multiple models loaded by input streams.
	 * @param streams containing model data
	 * @throws IOException if loading fails
	 */
	protected abstract void load(InputStream... stream) throws IOException;
}
