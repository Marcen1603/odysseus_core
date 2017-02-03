package de.uniol.inf.is.odysseus.nlp.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
import de.uniol.inf.is.odysseus.nlp.toolkits.*;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.NamedEntityAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.SentenceAnnotation;
import de.uniol.inf.is.odysseus.nlp.toolkits.annotations.TokenAnnotation;

public class AnnotatePO<M extends IMetaAttribute> extends AbstractPipe<Tuple<M>, KeyValueObject<M>> {
	private SDFAttribute attribute;
	private int attributePosition;
	private NLPToolkit toolkit;
	private String toolkitName;
	private Set<String> information;	
	private HashMap<String, Option> configuration;
	private boolean appendTokens = false;
	private boolean appendSentences = false;
	private boolean appendNamedEntities = false;

	public int getAttributePosition(){
		return attributePosition;
	}

    public AnnotatePO(AnnotatePO<M> splitPO) {
        super();
		init(splitPO.toolkitName, splitPO.toolkit, splitPO.information, splitPO.attribute, splitPO.configuration);
    }
     
    public AnnotatePO(String toolkit, NLPToolkit nlpToolkit, Set<String> information, SDFAttribute attribute, HashMap<String, Option> configuration) {
		init(toolkit, nlpToolkit, information, attribute, configuration);
	}

	private void init(String toolkit, NLPToolkit nlpToolkit, Set<String> information, SDFAttribute attribute, HashMap<String, Option> configuration) {
        this.toolkitName = toolkit;
        this.toolkit = nlpToolkit;
        this.information = information;
        this.attribute = attribute;
        this.configuration = configuration;
        this.appendTokens = information.contains(TokenAnnotation.NAME);
        this.appendSentences = information.contains(SentenceAnnotation.NAME);
        this.appendNamedEntities = information.contains(NamedEntityAnnotation.NAME);
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
	            this.information.equals(information)	            
	            ) {
	        return true;
	    }
	    return false;
	}

	@Override
	protected void process_next(Tuple<M> object, int port) {
		Annotation annotation;
		annotation = toolkit.annotate(object.getAttribute(attributePosition).toString());
		    	
    	KeyValueObject<M> testOutput = (KeyValueObject<M>) KeyValueObject.fromTuple((Tuple<IMetaAttribute>) object, getOutputSchema());
    	KeyValueObject<M> annotations = (KeyValueObject<M>) KeyValueObject.createInstance();

    	if(this.appendTokens)
    		annotations.setAttribute("tokens", getTokens(annotation).toArray());
    	if(this.appendSentences)
    		annotations.setAttribute("sentence", getSentences(annotation).toArray());
    	if(this.appendNamedEntities)
    		annotations.setAttribute("ner", getNamedEntities(annotation).toArray());
    	
    	testOutput.setAttribute("annotations", annotations);
		transfer(testOutput);
	}
		
	
	private List<String> getSentences(Annotation annotation){
		SentenceAnnotation sentences = (SentenceAnnotation) annotation.getAnnotation(SentenceAnnotation.class);
		return Arrays.asList(sentences.getSentences());
	}
	
	private List<String> getTokens(Annotation annotation){
		TokenAnnotation annot = (TokenAnnotation) annotation.getAnnotation(TokenAnnotation.class);
		List<String> tokens = new ArrayList<>();
		
		for(String[] ts : annot.getTokens()){
			tokens.addAll(Arrays.asList(ts));
		}
		
		return tokens;		
	}
	
	private List<String> getNamedEntities(Annotation annotation){
		NamedEntityAnnotation namedEntityAnnotation = (NamedEntityAnnotation) annotation.getAnnotation(NamedEntityAnnotation.class);
		return namedEntityAnnotation.getNamedEntities();
	}
}

