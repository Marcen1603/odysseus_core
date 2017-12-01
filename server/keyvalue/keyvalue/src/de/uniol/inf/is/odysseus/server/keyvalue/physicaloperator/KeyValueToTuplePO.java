package de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ToTupleAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;

/**
 * This operator transforms a KeyValueObject to a Tuple
 *
 * @author Marco Grawunder, Jan Sören Schwarz
 *
 * @param <M>
 */

public class KeyValueToTuplePO<M extends IMetaAttribute> extends AbstractPipe<KeyValueObject<M>, Tuple<M>> {

	Logger logger = LoggerFactory.getLogger(KeyValueToTuplePO.class);

	private IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>> tHandler = new TupleDataHandler();
	private List<RenameAttribute> renameAttributes;

	boolean keepInputObject;

	public KeyValueToTuplePO(SDFSchema outputSchema) {
		this.tHandler = (IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>>) tHandler
				.createInstance(outputSchema);
		setOutputSchema(outputSchema);
	}

	public KeyValueToTuplePO(ToTupleAO operator) {
		this.tHandler = (IStreamObjectDataHandler<Tuple<? extends IMetaAttribute>>) tHandler
				.createInstance(operator.getOutputSchema());
		this.renameAttributes = operator.getAttributes();
		setOutputSchema(operator.getOutputSchema());
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void process_next(KeyValueObject<M> input, int port) {
		List<String> dataValues = new ArrayList<String>(getOutputSchema().size());
		for (int i = 0; i < getOutputSchema().size(); i++) {
			dataValues.add(null);
		}
		Object[] notToParse = new Object[getOutputSchema().size()];
		for (int i = 0; i < getOutputSchema().size(); i++) {
			try {
				String attributeName = this.renameAttributes.get(i).getAttribute().getAttributeName();
				SDFDatatype outputDatatype = getOutputSchema().getAttributes().get(i).getDatatype();
				if (outputDatatype.equals(SDFKeyValueDatatype.KEYVALUEOBJECT)) {
					if (attributeName.equals("$")) {
						notToParse[i] = input.clone();
						((KeyValueObject<IMetaAttribute>)notToParse[i]).setMetadata(null);
					} else {
						notToParse[i] = input.path(attributeName);
					}
				} else if (outputDatatype.isListValue()) {
					// In this attributeName should be a reference to an
					// array
					// getAttribute delivers only the last element in an
					// path so
					// path must be used
					List listObj = input.path(attributeName);
					notToParse[i] = listObj;
				} else if (input.containsKey(attributeName)) {
					if (outputDatatype.isNumeric()) {
						notToParse[i] = input.getNumberAttribute(attributeName);
					} else {
						dataValues.set(i, input.getAttribute(attributeName)+"");
					}
				}else{
					// try with path expression
					List<Object> v = input.path(attributeName);
					if (!outputDatatype.isListValue()){
						if (v.size() == 1){
							notToParse[i] = v.get(0);
						}
					}else{
						notToParse[i] = v;
					}
				}
			} catch (Exception e) {
				if (logger.isTraceEnabled()) {
					e.printStackTrace();
				}
				logger.warn(e.getMessage(), e);
			}

		}
		// First all input that needs to be parsed (Maybe this is also not
		// necessary ...)
		Tuple<M> output = (Tuple<M>) tHandler.readData(dataValues.iterator());
		// all other input
		for (int i = 0; i < notToParse.length; i++) {
			if (notToParse[i] != null) {
				output.setAttribute(i, notToParse[i]);
			}
		}

		if (input.getMetadata() != null)
			output.setMetadata((M) input.getMetadata().clone());

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
