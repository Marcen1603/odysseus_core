package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.IInformationObject;
import de.uniol.inf.ei.oj104.model.InformationElementSequence;
import de.uniol.inf.ei.oj104.model.SequenceInformationObject;
import de.uniol.inf.ei.oj104.model.SingleInformationObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Get104ElementSequences extends AbstractFunction<List<InformationElementSequence>> {
	
	private static final long serialVersionUID = 8507879947669315622L;

	private static final Logger logger = LoggerFactory.getLogger(Get104ElementSequences.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Get104ElementSequences.class.getSimpleName();

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
	public Get104ElementSequences() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public List<InformationElementSequence> getValue() {
		if(!(getInputValue(0) instanceof IInformationObject)) {
			logger.error("'{}' is not an information object!", (Object) getInputValue(0));
			return null;
		}
		
		IInformationObject io = (IInformationObject) getInputValue(0);
		List<InformationElementSequence> retVal = new ArrayList<>();
		if(io instanceof SingleInformationObject) {
			retVal.add((SingleInformationObject) io);
		} else if(io instanceof SequenceInformationObject) {
			retVal.addAll(((SequenceInformationObject) io).getInformationElementSequences());
		}
		return retVal;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}