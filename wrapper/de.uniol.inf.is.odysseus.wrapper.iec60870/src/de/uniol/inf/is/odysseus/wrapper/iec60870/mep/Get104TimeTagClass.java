package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Get104TimeTagClass extends AbstractFunction<Class<? extends ITimeTag>> {

	private static final long serialVersionUID = 2544519845279121731L;

	private static final Logger logger = LoggerFactory.getLogger(Get104TimeTagClass.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Get104TimeTagClass.class.getSimpleName();

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
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public Get104TimeTagClass() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Class<? extends ITimeTag> getValue() {
		if (!(getInputValue(0) instanceof ITimeTag)) {
			logger.error("'{}' is not a timetag!", (Object) getInputValue(0));
			return null;
		}

		ITimeTag tt = (ITimeTag) getInputValue(0);
		return tt.getClass();
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}