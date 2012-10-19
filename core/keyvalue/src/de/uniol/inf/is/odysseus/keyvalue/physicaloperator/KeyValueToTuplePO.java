package de.uniol.inf.is.odysseus.keyvalue.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
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

	public KeyValueToTuplePO(boolean keepInputObject) {
		this.keepInputObject = keepInputObject;
	}

	public KeyValueToTuplePO(KeyValueToTuplePO<M> keyValueToTuplePO) {
		super(keyValueToTuplePO);
		this.keepInputObject = keyValueToTuplePO.keepInputObject;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected boolean canHandleOutOfOrder() {
		return true;
	}

	public void setKeepInputObject(boolean keepInputObject) {
		this.keepInputObject = keepInputObject;
	}

	public boolean isKeepInputObject() {
		return keepInputObject;
	}

	@Override
	protected void process_next(KeyValueObject<M> input, int port) {
		SDFSchema outputSchema = getOutputSchema();
		Tuple<M> output = new Tuple<M>(outputSchema.size(), false);
		if (keepInputObject) {
			output.setAdditionalContent(input.clone());
		}
		int pos = 0;
		for (SDFAttribute attr : outputSchema.getAttributes()) {
			output.setAttribute(pos++,
					input.getAttribute(attr.getAttributeName()));
		}
		transfer(output);
	}


	@Override
	public AbstractPipe<KeyValueObject<M>, Tuple<M>> clone() {
		return new KeyValueToTuplePO<M>(this);
	}

}
