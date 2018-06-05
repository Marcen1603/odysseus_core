package de.uniol.inf.is.odysseus.nlp.physicaloperator;

import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model.AnnotationModel;
import de.uniol.inf.is.odysseus.nlp.datastructure.toolkit.NLPToolkit;

public class AnnotateKeyValuePO extends AbstractPipe<Tuple<IMetaAttribute>, KeyValueObject<IMetaAttribute>> {
	private SDFAttribute attribute;
	private int attributePosition;
	private NLPToolkit toolkit;
	private HashMap<String, Option> configuration;
	private List<String> models;
	public int getAttributePosition(){
		return attributePosition;
	}

    public AnnotateKeyValuePO(AnnotateKeyValuePO splitPO) {
        super();
		init(splitPO.toolkit, splitPO.models, splitPO.attribute, splitPO.configuration);
    }
     
    public AnnotateKeyValuePO(NLPToolkit nlpToolkit, List<String> models, SDFAttribute attribute, HashMap<String, Option> configuration) {
		init(nlpToolkit, models, attribute, configuration);
	}

	private void init(NLPToolkit nlpToolkit, List<String> models, SDFAttribute attribute, HashMap<String, Option> configuration) {
        this.toolkit = nlpToolkit;
        this.models = models;
        this.attribute = attribute;
        this.configuration = configuration;
    }
    
    
    
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	public void process_open() throws OpenFailedException{
		SDFSchema schema = getSubscribedToSource(0).getSchema();
		this.attributePosition = schema.indexOf(this.attribute);		

	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
	    if(!(ipo instanceof AnnotateKeyValuePO)) {
	        return false;
	    }
	    AnnotateKeyValuePO spo = (AnnotateKeyValuePO) ipo;
	    if(this.hasSameSources(spo) &&
	            this.attribute.equals(spo.attribute) &&
	            this.toolkit.equals(spo.toolkit) &&
	            this.models.equals(spo.models) &&
	            this.configuration.equals(spo.configuration)
	            ) {
	        return true;
	    }
	    return false;
	}

	@Override
	protected void process_next(Tuple<IMetaAttribute> object, int port) {
		if(port == 0){
			processAnnotation(object);
			return;
		}
		//Else: trained model arrived
		//String serializedModel = (String)object.getAttribute(0);
		
		AnnotationModel<?> model = (AnnotationModel<?>) object.getAttribute(0); //toolkit.deserialize(serializedModel);
		if(model != null){
			this.toolkit.getPipeline().exchange(model);
		}
	}

	private void processAnnotation(Tuple<IMetaAttribute> object) {
		Annotated annotated = this.toolkit.annotate(object.getAttribute(attributePosition).toString());
	   	
    	KeyValueObject<IMetaAttribute> output = KeyValueObject.fromTuple((Tuple<IMetaAttribute>) object, getOutputSchema());
    	KeyValueObject<IMetaAttribute> annotations = annotated.toObject();
    	
    	output.setAttribute("annotations", annotations);
    	output.setMetadata(object.getMetadata());
		transfer(output);
	}
}

