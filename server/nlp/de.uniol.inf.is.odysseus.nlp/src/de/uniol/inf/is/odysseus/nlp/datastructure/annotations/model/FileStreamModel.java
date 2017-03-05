package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelNotFoundException;

public abstract class FileStreamModel<A extends IAnnotation> extends AnnotationModel<A> {
	public final String SEPARATOR = "::";
	protected String[] filenames;

	/**
	 * Creates OpenNLP Model based on an user-specified configuration HashMap.
	 * @param configuration contains information about model-location
	 * @throws NLPModelNotFoundException if the file was not found
	 * @throws NLPException if something goes wrong besides the model was not found
	 */
	public FileStreamModel(Map<String, Option> configuration) throws NLPModelNotFoundException, NLPException{
		super(configuration);
		Option option = configuration.get(AnnotationModel.NAME+"."+this.identifier());
		if(option == null)
			throw new NLPModelNotFoundException(AnnotationModel.NAME+"."+this.identifier());
		
		String[] paths = ((String)option.getValue()).split(SEPARATOR);
		filenames = new String[paths.length];
		
		URI[] uris = new URI[paths.length];
		
		for(int i=0; i < paths.length; i++){
			try {
				uris[i] = new URI(paths[i]);
			} catch (URISyntaxException e) {
				throw new NLPModelNotFoundException(e.getMessage());
			}
		}
		
		if(paths.length == 0)
			throw new NLPModelNotFoundException(AnnotationModel.NAME+"."+this.identifier());
		
		FileInputStream[] streams = new FileInputStream[paths.length];
		try{
			for(int i = 0; i < uris.length; i++){
				File file = new File(uris[i]);
				filenames[i] = file.getName();
				streams[i] = new FileInputStream(file);
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
