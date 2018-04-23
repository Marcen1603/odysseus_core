package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.IInformationObject;
import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.ei.oj104.model.InformationElementSequence;
import de.uniol.inf.ei.oj104.model.SequenceInformationObject;
import de.uniol.inf.ei.oj104.model.SingleInformationObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
// adds null for not present time tags to keep the order
public class Get104TimeTagsFromObject extends AbstractFunction<List<ITimeTag>> {

	private static final long serialVersionUID = -7988258561209130644L;

	private static final Logger logger = LoggerFactory.getLogger(Get104TimeTagsFromObject.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Get104TimeTagsFromObject.class.getSimpleName();

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.LIST;

	/**
	 * Creates a new MEP function.
	 */
	public Get104TimeTagsFromObject() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public List<ITimeTag> getValue() {
		if (!(getInputValue(0) instanceof IInformationObject)) {
			logger.error("'{}' is not an information objects!", (Object) getInputValue(0));
			return null;
		}

		IInformationObject io = (IInformationObject) getInputValue(0);
		if (io instanceof SingleInformationObject) {
			return readTimeTags((InformationElementSequence) io);
		} else if (io instanceof SequenceInformationObject) {
			return readTimeTags((SequenceInformationObject) io);
		} else {
			logger.error("'{}' is not an information object class!", io.getClass());
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

	private static List<ITimeTag> readTimeTags(InformationElementSequence seq) {
		return Arrays.asList(new ITimeTag[] { seq.getTimeTag().orElse(null) });
	}

	private static List<ITimeTag> readTimeTags(SequenceInformationObject io) {
		List<ITimeTag> retVal = new ArrayList<>();
		io.getInformationElementSequences().forEach(seq -> retVal.addAll(readTimeTags(seq)));
		return retVal;
	}

}