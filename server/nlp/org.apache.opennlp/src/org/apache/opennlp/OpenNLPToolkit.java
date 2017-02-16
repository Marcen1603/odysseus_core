package org.apache.opennlp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.TrainableFileAnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPException;
import de.uniol.inf.is.odysseus.nlp.datastructure.exception.NLPModelSerializationFailed;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;


public class OpenNLPToolkit extends NLPToolkit{

	/**
	 * Instantiates Toolkit with empty Pipeline for algorithm access only.
	 */
	public OpenNLPToolkit(){
		super();
	}
	
	public OpenNLPToolkit(List<String> models, HashMap<String, Option> configuration) throws NLPException {
		super(models, configuration);
	}

	@Override
	protected void instantiatePipeline(List<String> models, HashMap<String, Option> configuration) throws NLPException {
		this.pipeline = new OpenNLPPipeline(models, configuration);
	}

	@Override
	protected void instantiateEmptyPipeline() {
		this.pipeline = new OpenNLPPipeline();
	}
	
	@Override
	public AnnotationModel<?> deserialize(String serializedModel){
     
		byte[] objectData = Base64.getDecoder().decode(serializedModel);

	    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(objectData);
	    try (ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)){
		     Object obj = objectInputStream.readObject();
		     
		     if(obj instanceof TrainableFileAnnotationModel<?>)
		    	return (AnnotationModel<?>) obj;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
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
}
