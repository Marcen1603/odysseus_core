package de.uniol.inf.is.odysseus.nlp.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.filter.IFilter;

public class FilterPO extends AbstractPipe<Tuple<IMetaAttribute>, KeyValueObject<IMetaAttribute>> {
	private OptionMap options;
	private IFilter filter;
	
    public FilterPO(FilterPO splitPO) {
        super();
		init(splitPO.filter, splitPO.options);
    }
     
    public FilterPO(IFilter filter, OptionMap options) {
		init(filter, options);
	}

	private void init(IFilter filter, OptionMap options) {
        this.filter = filter;
        this.options = options;
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
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
	    if(!(ipo instanceof FilterPO)) {
	        return false;
	    }
	    FilterPO spo = (FilterPO) ipo;
	    if(this.hasSameSources(spo) &&
	            this.filter.equals(spo.filter) &&
	            this.options.equals(spo.options)
	            ) {
	        return true;
	    }
	    return false;
	}

	@Override
	protected void process_next(Tuple<IMetaAttribute> input, int port) {
		Object object = input.getAttribute(0);
		if(!(object instanceof Annotated))
			throw new RuntimeException("Error: the input-object must be an AnnotateNLP output-object.");
		Annotated annotated = (Annotated) object;
		if(filter.filter(annotated)){
			KeyValueObject<IMetaAttribute> output = new KeyValueObject<IMetaAttribute>();
	    	KeyValueObject<IMetaAttribute> annotations = annotated.toObject();
	    	output.setAttribute("annotations", annotations);
	    	output.setMetadata(input.getMetadata());
			transfer(output);
		}
		
	}
}
