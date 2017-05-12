package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104;

import java.util.ArrayList;
import java.util.List;

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
	 * 0: typeId -- {@link SDFDatatype#TUPLE}<br />
	 * 1: isSequenceOfElements -- {@link SDFDatatype#BOOLEAN}<br />
	 * 2: causeOfTransmission -- {@link SDFDatatype#TUPLE}<br />
	 * 3: test -- {@link SDFDatatype#BOOLEAN}<br />
	 * 4: negativeConfirm -- {@link SDFDatatype#BOOLEAN}<br />
	 * 5: originatorAddress -- {@link SDFDatatype#INTEGER}<br />
	 * 6: commonAddress -- {@link SDFDatatype#INTEGER}<br />
	 * 7: informationObjects -- {@link SDFDatatype#LIST_TUPLE}<br />
	 * 8: privateInformation -- {@link SDFDatatype#LIST_BYTE}<br />
	 * 9: sequenceLength -- {@link SDFDatatype#INTEGER}
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
		tuple.setAttribute(0, typeIdToTuple(asdu.getTypeIdentification()));
		tuple.setAttribute(1, asdu.isSequenceOfElements());
		tuple.setAttribute(2, causeOfTransmissionToTuple(asdu.getCauseOfTransmission()));
		tuple.setAttribute(3, asdu.isTestFrame());
		tuple.setAttribute(4, asdu.isNegativeConfirm());
		tuple.setAttribute(5, asdu.getOriginatorAddress());
		tuple.setAttribute(6, asdu.getCommonAddress());
		tuple.setAttribute(7, informationObjectsToListTuple(asdu.getInformationObjects()));
		tuple.setAttribute(8, privateInformationToListByte(asdu.getPrivateInformation()));
		tuple.setAttribute(9, asdu.getSequenceLength());
		return tuple;
	}

	/**
	 * Converts a {@link TypeId} to a tuple by going over the fields of the enum
	 * in the following order:<br />
	 * 0: key -- {@link SDFDatatype#STRING}<br />
	 * 1: id -- {@link SDFDatatype#INTEGER}<br />
	 * 2: description -- {@link SDFDatatype#STRING}
	 *
	 * @param typeId
	 *            A valid typeId.
	 * @return A tuple with to schema given above.
	 */
	private static Tuple<IMetaAttribute> typeIdToTuple(TypeId typeId) {
		final int numAttributes = 3;
		final Tuple<IMetaAttribute> tuple = new Tuple<>(numAttributes, false);

		if (typeId != null) {
			tuple.setAttribute(0, typeId.toString());
			tuple.setAttribute(1, typeId.getId());
			tuple.setAttribute(2, typeId.getDescription());
		}

		return tuple;
	}

	/**
	 * Converts a {@link CauseOfTransmission} to a tuple by going over the
	 * fields of the enum in the following order:<br />
	 * 0: key -- {@link SDFDatatype#STRING}< br/> 1: id --
	 * {@link SDFDatatype#INTEGER}
	 *
	 * @param causeOfTransmission
	 *            A valid typeId.
	 * @return A tuple with to schema given above.
	 */
	private static Tuple<IMetaAttribute> causeOfTransmissionToTuple(CauseOfTransmission causeOfTransmission) {
		final int numAttributes = 2;
		final Tuple<IMetaAttribute> tuple = new Tuple<>(numAttributes, false);

		if (causeOfTransmission != null) {
			tuple.setAttribute(0, causeOfTransmission.toString());
			tuple.setAttribute(1, causeOfTransmission.getId());
		}

		return tuple;
	}

	/**
	 * Converts an array of {@link InformationObject}s to a list of tuples.
	 *
	 * @param informationObjects
	 *            A valid array of information objects.
	 * @return A list of tuples.
	 */
	private static List<Tuple<IMetaAttribute>> informationObjectsToListTuple(InformationObject[] informationObjects) {
		if(informationObjects == null) {
			return new ArrayList<>();
		}

		List<Tuple<IMetaAttribute>> list = new ArrayList<>(informationObjects.length);
		for (InformationObject object : informationObjects) {
			list.add(informationObjectToTuple(object));
		}
		return list;
	}

	/**
	 * Converts an {@link InformationObject} to a tuple by going over the fields
	 * of the class in the following order:<br />
	 * 0: informationObjectAddress -- {@link SDFDatatype#INTEGER}< br/> 1:
	 * informationElements -- {@link SDFDatatype#OBJECT}
	 *
	 * @param object
	 *            A valid information object.
	 * @return A tuple with to schema given above.
	 */
	private static Tuple<IMetaAttribute> informationObjectToTuple(InformationObject object) {
		final int numAttributes = 2;
		final Tuple<IMetaAttribute> tuple = new Tuple<>(numAttributes, false);

		if (object != null) {
			tuple.setAttribute(0, object.getInformationObjectAddress());
			tuple.setAttribute(1, object.getInformationElements());
		}

		return tuple;
	}

	/**
	 * Converts an array of bytes to a list of tuples.
	 *
	 * @param privateInformation
	 *            A valid array of bytes.
	 * @return A list of bytes.
	 */
	private static List<Byte> privateInformationToListByte(byte[] privateInformation) {
		if(privateInformation == null) {
			return new ArrayList<>();
		}

		List<Byte> list = new ArrayList<>(privateInformation.length);
		for (byte b : privateInformation) {
			list.add(b);
		}
		return list;
	}

}