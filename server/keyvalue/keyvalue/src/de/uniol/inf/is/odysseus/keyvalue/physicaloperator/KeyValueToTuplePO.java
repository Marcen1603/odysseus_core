package de.uniol.inf.is.odysseus.keyvalue.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.logicaloperator.KeyValueToTupleAO;

/**
 * This operator transforms a KeyValueObject to a Tuple
 * 
 * @author Marco Grawunder, Jan Sören Schwarz
 * 
 * @param <M>
 */

public class KeyValueToTuplePO<M extends IMetaAttribute> extends AbstractPipe<KeyValueObject<M>, Tuple<M>> {

	private IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>> tHandler = new TupleDataHandler();
	private List<RenameAttribute> renameAttributes;

	boolean keepInputObject;

	public KeyValueToTuplePO(boolean keepInputObject, SDFSchema outputSchema) {
		this.keepInputObject = keepInputObject;
		this.tHandler = (IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>>) tHandler
				.createInstance(outputSchema);
		setOutputSchema(outputSchema);
	}

	public KeyValueToTuplePO(KeyValueToTupleAO operator) {
		this.keepInputObject = operator.isKeepInputObject();
		this.tHandler = (IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>>) tHandler
				.createInstance(operator.getOutputSchema());
		this.renameAttributes = operator.getAttributes();
		setOutputSchema(operator.getOutputSchema());
	}

	public KeyValueToTuplePO(KeyValueToTuplePO<M> keyValueToTuplePO) {
		super(keyValueToTuplePO);
		this.keepInputObject = keyValueToTuplePO.keepInputObject;
		this.tHandler = keyValueToTuplePO.tHandler;
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
		List<String> dataValues = new ArrayList<String>();
		for (int i = 0; i < getOutputSchema().size(); i++) {
			String attributeName = this.renameAttributes.get(i).getAttribute().getAttributeName();
			if (input.getAttributes().containsKey(attributeName)) {
				Object attribute = input.getAttribute(attributeName);
				if (attribute instanceof List) {
					if(((List<?>) attribute).size() == 0) {
						dataValues.add(null);
					} else {
						StringBuilder sb = new StringBuilder();
						for(Iterator<Object> iter = ((List<Object>) attribute).iterator(); iter.hasNext(); ) {
							sb.append(iter.next());
							if(iter.hasNext()) {
								sb.append("\n");
							}
						}
						dataValues.add(sb.toString());
//						for (Object object : (List<Object>) attribute) {
							// Add data values as single values. The data handler
							// handles this as a list. It will create a list over a
							// couple of entries in the list. Only works if the list
							// is the last element!
//							dataValues.add(object.toString());
//						}
					}
				} else {
					dataValues.add(attribute.toString());
				}
			} else {
				dataValues.add(null);
			}
		}
		Tuple<M> output = (Tuple<M>) tHandler.readData(dataValues);

		if (input.getMetadata() != null)
			output.setMetadata((M) input.getMetadata().clone());

		if (keepInputObject) {
			output.setKeyValue("base", input.clone());
		}
		transfer(output);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof KeyValueToTuplePO<?>)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		KeyValueToTuplePO<M> spo = (KeyValueToTuplePO<M>) ipo;
		// Schema match
		if (this.getOutputSchema().equals(spo.getOutputSchema())) {
			return true;
		}

		return false;
	}

}
