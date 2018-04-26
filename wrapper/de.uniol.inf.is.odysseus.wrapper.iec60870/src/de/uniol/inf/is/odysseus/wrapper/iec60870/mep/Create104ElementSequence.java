package de.uniol.inf.is.odysseus.wrapper.iec60870.mep;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.model.IInformationElement;
import de.uniol.inf.ei.oj104.model.ITimeTag;
import de.uniol.inf.ei.oj104.model.InformationElementSequence;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class Create104ElementSequence extends AbstractFunction<InformationElementSequence> {

	private static final long serialVersionUID = -7069073881012544190L;

	private static final Logger logger = LoggerFactory.getLogger(Create104ElementSequence.class);

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = Create104ElementSequence.class.getSimpleName();

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 4;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.LIST }, { SDFDatatype.LIST },
			{ SDFDatatype.OBJECT }, { SDFDatatype.OBJECT } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public Create104ElementSequence() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public InformationElementSequence getValue() {
		List<?> ieClassList, ielist;
		if ((ieClassList = (List<?>) getInputValue(0)) != null && !ieClassList.isEmpty()
				&& !(ieClassList.get(0) instanceof Class)) {
			logger.error("'{}' is not a list of information element classes!", ieClassList);
			return null;
		} else if ((ielist = (List<?>) getInputValue(1)) != null && !ielist.isEmpty()
				&& !(ielist.get(0) instanceof IInformationElement)) {
			logger.error("'{}' is not a list of information element sequences!", ielist);
			return null;
		} else if (!(getInputValue(2) instanceof Class) || !ITimeTag.class.isAssignableFrom(getInputValue(2))) {
			logger.error("'{}' is not a time tag class!", (Object) getInputValue(2));
			return null;
		} else if (!(getInputValue(3) instanceof ITimeTag)) {
			logger.error("'{}' is not a time tag!", (Object) getInputValue(3));
			return null;
		}

		@SuppressWarnings("unchecked")
		List<Class<? extends IInformationElement>> informationElementClasses = (List<Class<? extends IInformationElement>>) getInputValue(
				0);
		@SuppressWarnings("unchecked")
		List<IInformationElement> informationElements = (List<IInformationElement>) ielist;
		@SuppressWarnings("unchecked")
		Optional<Class<? extends ITimeTag>> timeTagClass = Optional
				.ofNullable((Class<? extends ITimeTag>) getInputValue(2));
		Optional<ITimeTag> timeTag = Optional.ofNullable((ITimeTag) getInputValue(3));
		return new InformationElementSequence(informationElementClasses, informationElements, timeTagClass, timeTag);
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}