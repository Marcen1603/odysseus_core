package de.uniol.inf.is.odysseus.nlp.datastructure.toolkit;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.IAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.pipeline.Pipeline;

/**
 * Parent class for integrated nlp frameworks.
 */
public abstract class NLPToolkit {
	/**
	 * Pipeline of the NLPToolkit-Object
	 * 
	 * @see Pipeline
	 */
	protected Pipeline pipeline;
	

	/**
	 * Instantiates a NLPToolkit.
	 * 
	 * @see NLPToolkit
	 * 
	 * @param information List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 * @throws NLPException 
	 */
	public NLPToolkit(List<String> models, Map<String, Option> configuration) throws NLPException{
		this.instantiatePipeline(models, configuration);
	}

	public NLPToolkit() {
		this.instantiateEmptyPipeline();
	}

	/**
	 * Instantiates empty subclass-specified pipeline for algorithm access only.
	 */
	protected abstract void instantiateEmptyPipeline();

	/**
	 * Instantiates the pipeline field with a subclass-specified pipeline.
	 * 
	 * @param models List of Annotation-Names that have to be included in the annotated object after annotation.
	 * @param configuration HashMap of properties for {@link IAnnotationModel} configuration
	 * @throws NLPException 
	 */
	protected abstract void instantiatePipeline(List<String> models, Map<String, Option> configuration) throws NLPException;
	
	/**
	 * Returns new {@link Annotated} object and annotates it with the configured pipeline.
	 * @param text to annotate
	 * @return fully annotated text
	 */
	public Annotated annotate(String text){
		Annotated annotated = new Annotated(text, pipeline.getModels());
		pipeline.annotate(annotated);
		return annotated;
	}
	
	/*
	public AnnotationModel<?> deserialize(String serializedModel){
     
		byte[] objectData = Base64.getDecoder().decode(serializedModel);

	    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(objectData);
	    try (OsgiObjectInputStream objectInputStream = new OsgiObjectInputStream(byteInputStream)){
		     Object obj = objectInputStream.readObject();
		     
		     if(obj instanceof TrainableFileAnnotationModel<?>)
		    	return (AnnotationModel<?>) obj;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public String serialize(AnnotationModel<?> model){
		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try(ObjectOutputStream objectStream = new ObjectOutputStream(stream)){
        	model.makeSerializable();
        	objectStream.writeObject(model);
        	objectStream.close();
            return Base64.getEncoder().encodeToString(stream.toByteArray());
        }catch(IOException exception){
        	throw new NLPModelSerializationFailed("Serialization of trained model failed."+exception.getMessage()); 
        }
	}
	*/
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NLPToolkit){
			NLPToolkit o = (NLPToolkit) obj;
			return pipeline.equals(o.pipeline);
		}
		return false;
	}

	public Pipeline getPipeline() {
		return pipeline;
	}
}
