package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

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
public class Set104ElementSequences extends AbstractFunction<IInformationObject> {

	private static final long serialVersionUID = -8131413321172405715L;

	private static final Logger logger = LoggerFactory.getLogger(Set104ElementSequences.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Set104ElementSequences.class.getSimpleName();

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.LIST } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public Set104ElementSequences() {
		super(name, numInputs, inputTypes, outputType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IInformationObject getValue() {
		List<?> iesList;
		if (!(getInputValue(0) instanceof IInformationObject)) {
			logger.error("'{}' is not an information object!", (Object) getInputValue(0));
			return null;
		} else if ((iesList = (List<?>) getInputValue(1)) != null && !iesList.isEmpty()
				&& !(iesList.get(0) instanceof InformationElementSequence)) {
			logger.error("'{}' is not a list of information element sequences!", iesList);
			return null;
		}

		IInformationObject io = (IInformationObject) getInputValue(0);

		if (io instanceof SingleInformationObject) {
			if (iesList.size() > 1) {
				logger.error("For a single information object, the number of information element sequences must be 1!");
				return null;
			}
			InformationElementSequence ies = (InformationElementSequence) iesList.get(0);
			SingleInformationObject singleIO = (SingleInformationObject) io;
			singleIO.setInformationElements(ies.getInformationElements());
			singleIO.setTimeTag(ies.getTimeTag());
		} else if (io instanceof SequenceInformationObject) {
			((SequenceInformationObject) io).setInformationElementSequences((List<InformationElementSequence>) iesList);
		} else {
			logger.error("'{}' ins not a known type of information object", io.getClass());
			return null;
		}
		return io;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}