package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.mep;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
 * information objects as its first input parameter. The other input parameter
 * is a time stamp in ms.<br />
 *
 * Within the list of information objects it searches for the one
 * {@link InformationElement} (see {@link InformationObject} for the link with
 * {@link InformationElement}) that is a time information. That is either an
 * {@link IeTime16}, {@link IeTime24} or {@link IeTime56} element. The MEP
 * function replaces the found time information by the given time stamp (second
 * input). The return value of the MEP function is the modified list of
 * information objects.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class TimestampToASDUFunction extends AbstractFunction<List<InformationObject>> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -571136363649697630L;

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "TimestampToASDU";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 2;

	/**
	 * The expected data types of the inputs. One row for each input. Different
	 * data types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.LIST },
			{ SDFDatatype.LONG } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.LIST;

	/**
	 * Creates a new MEP function.
	 */
	public TimestampToASDUFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public List<InformationObject> getValue() {
		List<InformationObject> infoObjects = getInputValue(0);
		long ts = getInputValue(1);

		return infoObjects.stream().map(infoObj -> changeTimeInformation(infoObj, ts)).collect(Collectors.toList());
	}

	/**
	 * Replace the time information ({@link IeTime16}, {@link IeTime24} or
	 * {@link IeTime56}) within an information object by a given time stamp.
	 *
	 * @param infoObj
	 *            The information object to search and replace the time
	 *            information.
	 * @param ts
	 *            The time stamp [ms] to replace the original one.
	 * @return The modified information object (if it contains a time
	 *         information; otherwise it equals the input).
	 */
	private InformationObject changeTimeInformation(InformationObject infoObj, long ts) {
		InformationElement[][] infoElems = Arrays.asList(infoObj.getInformationElements()).stream()
				.map(infoElemRow -> changeTimeInformation(infoElemRow, ts)).toArray(InformationElement[][]::new);
		return new InformationObject(infoObj.getInformationObjectAddress(), infoElems);
	}

	/**
	 * Replace the time information ({@link IeTime16}, {@link IeTime24} or
	 * {@link IeTime56}) within an array of information elements by a given time
	 * stamp.
	 *
	 * @param infoElems
	 *            The information elements to search and replace the time
	 *            information.
	 * @param ts
	 *            The time stamp [ms] to replace the original one.
	 * @return The modified array of information elements (if it contains a time
	 *         information; otherwise it equals the input).
	 */
	private InformationElement[] changeTimeInformation(InformationElement[] infoElems, long ts) {
		return Arrays.asList(infoElems).stream().map(infoElem -> changeTimeInformation(infoElem, ts))
				.toArray(InformationElement[]::new);
	}

	/**
	 * Replace the time information ({@link IeTime16}, {@link IeTime24} or
	 * {@link IeTime56}) by a given time stamp, if the input is a time
	 * information.
	 *
	 * @param infoElem
	 *            The information element to be replaced.
	 * @param ts
	 *            The time stamp [ms] to replace the original one.
	 * @return The modified information element (if it is a time information;
	 *         otherwise it equals the input).
	 */
	private InformationElement changeTimeInformation(InformationElement infoElem, long ts) {
		if (infoElem instanceof IeTime16) {
			return new IeTime16(ts);
		} else if (infoElem instanceof IeTime24) {
			return new IeTime24(ts);
		} else if (infoElem instanceof IeTime56) {
			return new IeTime56(ts);
		}
		return infoElem;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}