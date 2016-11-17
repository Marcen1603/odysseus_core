package de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.server.keyvalue.logicaloperator.KeyValueToTupleAO;

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

	public KeyValueToTuplePO(KeyValueToTupleAO operator) {
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
		Object[] notToParse = new Object[getOutputSchema().size()];
		for (int i = 0; i < getOutputSchema().size(); i++) {
			try {
				String attributeName = this.renameAttributes.get(i).getAttribute().getAttributeName();
				SDFDatatype outputDatatype = getOutputSchema().getAttributes().get(i).getDatatype();

				if (attributeName.endsWith("*")) {
					final String attr = attributeName.substring(0, attributeName.length() - 1);

					Iterator<Entry<String, Object>> iter = input.getAsKeyValueMap().entrySet().stream()
							.filter(e -> e.getKey().startsWith(attr)).iterator();
					if (iter.hasNext()) {
						StringBuilder sb = new StringBuilder();
						while (iter.hasNext()) {
							Entry<String, Object> entry = iter.next();
							sb.append(entry.getKey().substring(attr.length()));
							sb.append("|");
							sb.append(entry.getValue());
							if (iter.hasNext()) {
								sb.append("\n");
							}
						}
						dataValues.set(i, sb.toString());
					}
				} else {

					if (outputDatatype.equals(SDFKeyValueDatatype.KEYVALUEOBJECT)) {
						if (attributeName.equals("$")) {
							notToParse[i] = input.clone();
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
						Object attribute = input.getAttribute(attributeName);
						if (attribute != null) {
							if (attribute instanceof List) {
								if (((List<?>) attribute).size() != 0) {
									StringBuilder sb = new StringBuilder();
									for (Iterator<Object> iter = ((List<Object>) attribute).iterator(); iter
											.hasNext();) {
										sb.append(iter.next());
										if (iter.hasNext()) {
											sb.append("\n");
										}
									}
									dataValues.set(i, sb.toString());
								}
							} else {
								dataValues.set(i, attribute.toString());
							}
						}
					}
				}
			} catch (Exception e) {
				logger.warn(e.getMessage());
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
