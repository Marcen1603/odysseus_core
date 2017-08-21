package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.mep;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.IeTime16;
import org.openmuc.j60870.IeTime24;
import org.openmuc.j60870.IeTime56;
import org.openmuc.j60870.InformationElement;
import org.openmuc.j60870.InformationObject;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.util.ASDUConverter;

/**
 * This MEP function is designed for data from IEC 60870-5-104 transport handler
 * (either server or client). The transport handlers deliver {@link ASdu}
 * elements (in most cases as a tuple with several attributes for the fields of
 * the ASDU class (see {@link ASDUConverter}). One of the elements is a list of
 * {@link InformationObject}s and this MEP function expects such a list of
 * information objects as its only input parameter.<br />
 *
 * Within the list of information objects it searches for the one
 * {@link InformationElement} (see {@link InformationObject} for the link with
 * {@link InformationElement}) that is a time information. That is either an
 * {@link IeTime16}, {@link IeTime24} or {@link IeTime56} element. The return
 * value of the MEP function is the time information in milliseconds.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class TimestampFromASDUFunction extends AbstractFunction<Long> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -6792142155452423063L;

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "TimestampFromASDU";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.LIST } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.LONG;

	/**
	 * Creates a new MEP function.
	 */
	public TimestampFromASDUFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Long getValue() {
		List<InformationObject> infoObjects = getInputValue(0);

		// find the time information
		Optional<InformationElement> optTimeInfoElem = infoObjects.stream()
				.flatMap(infoObject -> Arrays.asList(infoObject.getInformationElements()).stream())
				.flatMap(infoElemRow -> Arrays.asList(infoElemRow).stream())
				.filter(infoElem -> infoElem instanceof IeTime16 || infoElem instanceof IeTime24
						|| infoElem instanceof IeTime56)
				.findFirst();

		if (!optTimeInfoElem.isPresent()) {
			return null;
		}

		// return time in ms
		InformationElement timeInfoElem = optTimeInfoElem.get();
		if (timeInfoElem instanceof IeTime16) {
			return (long) ((IeTime16) timeInfoElem).getTimeInMs();
		} else if (timeInfoElem instanceof IeTime24) {
			return (long) ((IeTime24) timeInfoElem).getTimeInMs();
		} else if (timeInfoElem instanceof IeTime56) {
			return ((IeTime56) timeInfoElem).getTimestamp();
		}
		// unknown time data type
		return null;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}