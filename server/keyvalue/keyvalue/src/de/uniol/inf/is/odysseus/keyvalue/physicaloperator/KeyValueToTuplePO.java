package de.uniol.inf.is.odysseus.keyvalue.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator transforms a KeyValueObject to a Tuple
 * 
 * @author Marco Grawunder
 * 
 * @param <M>
 */

public class KeyValueToTuplePO<M extends IMetaAttribute> extends
		AbstractPipe<KeyValueObject<M>, Tuple<M>> {

	boolean keepInputObject;

	public KeyValueToTuplePO(boolean keepInputObject, SDFSchema outputSchema) {
		this.keepInputObject = keepInputObject;
		setOutputSchema(outputSchema);
	}

	public KeyValueToTuplePO(KeyValueToTuplePO<M> keyValueToTuplePO) {
		super(keyValueToTuplePO);
		this.keepInputObject = keyValueToTuplePO.keepInputObject;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	public void setKeepInputObject(boolean keepInputObject) {
		this.keepInputObject = keepInputObject;
	}

	public boolean isKeepInputObject() {
		return keepInputObject;
	}

	@SuppressWarnings({ "unchecked" })
    @Override
	protected void process_next(KeyValueObject<M> input, int port) {
		SDFSchema outputSchema = getOutputSchema();
		Tuple<M> output = new Tuple<M>(outputSchema.size(), false);
        output.setMetadata((M) input.getMetadata().clone());
		if (keepInputObject) {
			output.setAdditionalContent("base",input.clone());
		}
		int pos = 0;
		for (SDFAttribute attr : outputSchema.getAttributes()) {
			Object inputAttribute = input.getAttribute(attr.getAttributeName());
//			if((inputAttribute instanceof List)) {	
//				// Was soll hier passieren?
//				output.setAttribute(pos++, ((List) inputAttribute).get(0));
//			} else {
				output.setAttribute(pos++, inputAttribute);
//			}
		}
		transfer(output);
	}


	@Override
	public AbstractPipe<KeyValueObject<M>, Tuple<M>> clone() {
		return new KeyValueToTuplePO<M>(this);
	}

}
