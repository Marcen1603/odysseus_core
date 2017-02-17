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

public class AnnotatePO<M extends IMetaAttribute> extends AbstractPipe<Tuple<M>, KeyValueObject<M>> {
	private SDFAttribute attribute;
	private int attributePosition;
	private NLPToolkit toolkit;
	private String toolkitName;
	private HashMap<String, Option> configuration;
	private List<String> models;
	public int getAttributePosition(){
		return attributePosition;
	}

    public AnnotatePO(AnnotatePO<M> splitPO) {
        super();
		init(splitPO.toolkitName, splitPO.toolkit, splitPO.models, splitPO.attribute, splitPO.configuration);
    }
     
    public AnnotatePO(String toolkit, NLPToolkit nlpToolkit, List<String> models, SDFAttribute attribute, HashMap<String, Option> configuration) {
		init(toolkit, nlpToolkit, models, attribute, configuration);
	}

	private void init(String toolkit, NLPToolkit nlpToolkit, List<String> models, SDFAttribute attribute, HashMap<String, Option> configuration) {
        this.toolkitName = toolkit;
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
	    if(!(ipo instanceof AnnotatePO)) {
	        return false;
	    }
	    AnnotatePO<?> spo = (AnnotatePO<?>) ipo;
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
	protected void process_next(Tuple<M> object, int port) {
		if(port == 0){
			processAnnotation(object);
			return;
		}
		//Else: trained model arrived
		String serializedModel = (String)object.getAttribute(0);
		
		AnnotationModel<?> model = toolkit.deserialize(serializedModel);
		if(model != null){
			this.toolkit.getPipeline().exchange(model);
		}
	}

	private void processAnnotation(Tuple<M> object) {
		Annotated annotated = this.toolkit.annotate(object.getAttribute(attributePosition).toString());
	   	
    	KeyValueObject<M> output = (KeyValueObject<M>) KeyValueObject.fromTuple((Tuple<IMetaAttribute>) object, getOutputSchema());
    	KeyValueObject<IMetaAttribute> annotations = annotated.toObject();
    	
    	output.setAttribute("annotations", annotations);
		transfer(output);
	}
}

