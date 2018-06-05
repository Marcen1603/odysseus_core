package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openmuc.j60870.ASdu;
import org.openmuc.j60870.CauseOfTransmission;
import org.openmuc.j60870.InformationObject;
import org.openmuc.j60870.TypeId;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * Class which provides methods to convert {@link ASdu} instances to Odysseus
 * data structures like {@link Tuple}. <br />
 * Implemented for j60870 version 1.2.0
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class ASDUConverter {

	/**
	 * Converts an {@link ASdu} to a tuple by going over the fields of the ASdu
	 * class in the following order:<br />
	 * 0: typeId -- {@link SDFDatatype#OBJECT}<br />
	 * 1: isSequenceOfElements -- {@link SDFDatatype#BOOLEAN}<br />
	 * 2: causeOfTransmission -- {@link SDFDatatype#OBJECT}<br />
	 * 3: test -- {@link SDFDatatype#BOOLEAN}<br />
	 * 4: negativeConfirm -- {@link SDFDatatype#BOOLEAN}<br />
	 * 5: originatorAddress -- {@link SDFDatatype#INTEGER}<br />
	 * 6: commonAddress -- {@link SDFDatatype#INTEGER}<br />
	 * 7: sequenceLength -- {@link SDFDatatype#INTEGER}<br />
	 * 8: informationObjects -- {@link SDFDatatype#LIST}<br />
	 * 8: areInfosPrivate -- {@link SDFDatatype#BOOLEAN}
	 *
	 * @param asdu
	 *            A valid ASDU.
	 * @return A tuple with to schema given above.
	 * @throws NullPointerException
	 *             if asdu is null.
	 */
	public static Tuple<IMetaAttribute> asduToTuple(ASdu asdu) throws NullPointerException {
		final int numAttributes = 10;
		final Tuple<IMetaAttribute> tuple = new Tuple<>(numAttributes, false);
		tuple.setAttribute(0, asdu.getTypeIdentification());
		tuple.setAttribute(1, asdu.isSequenceOfElements());
		tuple.setAttribute(2, asdu.getCauseOfTransmission());
		tuple.setAttribute(3, asdu.isTestFrame());
		tuple.setAttribute(4, asdu.isNegativeConfirm());
		tuple.setAttribute(5, asdu.getOriginatorAddress());
		tuple.setAttribute(6, asdu.getCommonAddress());
		tuple.setAttribute(7, asdu.getSequenceLength());
		if (asdu.getPrivateInformation() != null && asdu.getPrivateInformation().length > 0) {
			tuple.setAttribute(8, Arrays.asList(asdu.getPrivateInformation()));
			tuple.setAttribute(9, true);
		} else {
			if (asdu.getInformationObjects() != null && asdu.getInformationObjects().length > 0) {
				tuple.setAttribute(8, Arrays.asList(asdu.getInformationObjects()));
			} else {
				// Should not happen
				tuple.setAttribute(8, new ArrayList<Object>());
			}
			tuple.setAttribute(9, false);
		}

		return tuple;
	}

	/**
	 * Converts a tuple to an {@link ASdu}. The tuple must have the following
	 * attributes:<br />
	 * 0: typeId -- {@link SDFDatatype#OBJECT}<br />
	 * 1: isSequenceOfElements -- {@link SDFDatatype#BOOLEAN}<br />
	 * 2: causeOfTransmission -- {@link SDFDatatype#OBJECT}<br />
	 * 3: test -- {@link SDFDatatype#BOOLEAN}<br />
	 * 4: negativeConfirm -- {@link SDFDatatype#BOOLEAN}<br />
	 * 5: originatorAddress -- {@link SDFDatatype#INTEGER}<br />
	 * 6: commonAddress -- {@link SDFDatatype#INTEGER}<br />
	 * 7: sequenceLength -- {@link SDFDatatype#INTEGER}<br />
	 * 8: informationObjects -- {@link SDFDatatype#LIST}<br />
	 * 8: areInfosPrivate -- {@link SDFDatatype#BOOLEAN}
	 *
	 * @param tuple
	 *            A valid tuple with the schema given above.
	 * @return A valid ASDU.
	 * @throws NullPointerException
	 *             if tuple is null.
	 */
	public static ASdu TupleToASDU(Tuple<IMetaAttribute> tuple) throws NullPointerException {
		final TypeId typeId = tuple.getAttribute(0);
		final boolean isSeq = tuple.getAttribute(1);
		final CauseOfTransmission cot = tuple.getAttribute(2);
		final boolean isTest = tuple.getAttribute(3);
		final boolean isNegConf = tuple.getAttribute(4);
		final int getOrigAddress = tuple.getAttribute(5);
		final int getCommonAddress = tuple.getAttribute(6);
		final int seqLength = tuple.getAttribute(7);
		final boolean areInfosPrivate = tuple.getAttribute(9);
		if (areInfosPrivate) {
			final List<Byte> infoList = tuple.getAttribute(8);
			final Byte[] infoArray = infoList.toArray(new Byte[infoList.size()]);
			return new ASdu(typeId, isSeq, seqLength, cot, isTest, isNegConf, getOrigAddress, getCommonAddress,
					ArrayUtils.toPrimitive(infoArray));
		} else {
			final List<InformationObject> infoList = tuple.getAttribute(8);
			final InformationObject[] infoArray = infoList.toArray(new InformationObject[infoList.size()]);
			return new ASdu(typeId, isSeq, cot, isTest, isNegConf, getOrigAddress, getCommonAddress, infoArray);
		}
	}

}