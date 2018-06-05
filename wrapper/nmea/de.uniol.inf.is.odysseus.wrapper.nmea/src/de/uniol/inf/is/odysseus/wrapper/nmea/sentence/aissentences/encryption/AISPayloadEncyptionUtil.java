package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.encryption;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.AddressedSafetyRelatedMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.AidToNavigationReport;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.AssignedModeCommand;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.BaseStationReport;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.BinaryAcknowledge;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.BinaryAddressedMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.BinaryBroadcastMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.ChannelManagement;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.DGNSSBinaryBroadcastMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.DataLinkManagement;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.EncodedAISPayload;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.ExtendedClassBEquipmentPositionReport;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.GroupAssignmentCommand;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.Interrogation;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.MultipleSlotBinaryMessageWithCommunicationState;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.PositionReportClassA;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.PositionReportClassAAssignedSchedule;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.PositionReportClassAResponseToInterrogation;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.SafetyRelatedBroadcastMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.SingleSlotBinaryMessage;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StandardClassBCSPositionReport;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StandardSARAircraftPositionReport;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StaticAndVoyageData;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.StaticDataReport;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.UTCAndDateInquiry;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.UTCAndDateResponse;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.exceptions.InvalidEncodedPayload;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.exceptions.UnsupportedPayloadType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AidType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.IMO;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.MMSI;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ManeuverIndicator;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.NavigationStatus;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.PositionFixingDevice;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ReportingInterval;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.ShipType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.StationType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.TxRxMode;

/**
 * @author msalous
 *
 */
public class AISPayloadEncyptionUtil {

	/**
	 * BitString to different types conversion
	 * 
	 */

	public static Integer convertToUnsignedInteger(String bitString) {
		return Integer.parseInt(bitString, 2);
	}

	public static Integer convertToSignedInteger(String bitString) {
		Integer value;
		String signBit = bitString.substring(0, 1);
		String valueBits = bitString.substring(1);
		if ("0".equals(signBit))
			value = convertToUnsignedInteger(valueBits);
		else {
			valueBits = valueBits.replaceAll("0", "x");
			valueBits = valueBits.replaceAll("1", "0");
			valueBits = valueBits.replaceAll("x", "1");
			value = convertToUnsignedInteger("-" + valueBits);
		}

		return value;
	}

	public static Float convertToUnsignedFloat(String bitString) {
		return Float.valueOf(convertToUnsignedInteger(bitString));
	}

	public static Double convertToUnsignedDouble(String bitString) {
		return Double.valueOf(convertToUnsignedInteger(bitString));
	}

	public static Float convertToFloat(String bitString) {
		return Float.valueOf(convertToSignedInteger(bitString));
	}

	public static Double convertToDouble(String bitString) {
		return Double.valueOf(convertToSignedInteger(bitString));
	}

	public static Long convertToUnsignedLong(String bitString) {
		return Long.parseLong(bitString, 2);
	}

	public static Boolean convertToBoolean(String bits) {
		return "1".equals(bits.substring(0, 1));
	}

	public static String convertToTime(String bits) {
		Integer month = convertToUnsignedInteger(bits.substring(0, 4));
		Integer day = convertToUnsignedInteger(bits.substring(4, 9));
		Integer hour = convertToUnsignedInteger(bits.substring(9, 14));
		Integer minute = convertToUnsignedInteger(bits.substring(14, 20));

		String monthAsString = month < 10 ? "0" + month : "" + month;
		String dayAsString = day < 10 ? "0" + day : "" + day;
		String hourAsString = hour < 10 ? "0" + hour : "" + hour;
		String minuteAsString = minute < 10 ? "0" + minute : "" + minute;

		return dayAsString + "-" + monthAsString + " " + hourAsString + ":" + minuteAsString;
	}

	public static String convertToString(String bits) {
		StringBuffer stringBuffer = new StringBuffer();
		String remainingBits = bits;
		while (remainingBits.length() >= 6) {
			String binaryValue = remainingBits.substring(0, 6);
			remainingBits = remainingBits.substring(6);
			Integer intValue = convertToUnsignedInteger(binaryValue);
			String character = AISPayLoadEncryptionMaps.sixBitAscii.get(intValue);
			stringBuffer.append(character);
		}
		String ret = stringBuffer.toString();
		ret = ret.replaceAll("@", " ").trim();
		return ret;
	}

	/***************************************************************************
	 *************** D E C O D I N G F U N C T I O N S*************************
	 ***************************************************************************/

	/**
	 * StaticAndVoyageData
	 */
	public static StaticAndVoyageData decodeStaticAndVoyageData(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.StaticAndVoyageRelatedData))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		IMO imo = IMO.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		String callsign = AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(70, 112));
		String shipName = AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(112, 232));
		ShipType shipType = ShipType
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(232, 240)));
		Integer toBow = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(240, 249));
		Integer toStern = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(249, 258));
		Integer toPort = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(258, 264));
		Integer toStarboard = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(264, 270));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(270, 274)));
		String eta = AISPayloadEncyptionUtil.convertToTime(encodedMessage.getBits(274, 294));
		Float draught = AISPayloadEncyptionUtil.convertToUnsignedFloat(encodedMessage.getBits(294, 302)) / 10f;
		String destination = AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(302, 422));
		Boolean dataTerminalReady = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(422, 423));

		return new StaticAndVoyageData(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, imo, callsign,
				shipName, shipType, toBow, toStern, toStarboard, toPort, positionFixingDevice, eta, draught,
				destination, dataTerminalReady);
	}

	/**
	 * PositionReportClassA, for protocol definition see
	 * http://catb.org/gpsd/AIVDM.html#_types_1_2_and_3_position_report_class_a
	 */
	public static PositionReportClassA decodePositionReportClassA(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.PositionReportClassA))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		NavigationStatus navigationStatus = NavigationStatus
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 42)));
		Integer rateOfTurn = AISPayloadEncyptionUtil.convertToSignedInteger(encodedMessage.getBits(42, 50));
		Double speedOverGround = AISPayloadEncyptionUtil.convertToUnsignedDouble(encodedMessage.getBits(50, 60)) / 10f;
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(60, 61));
		Double longitude = AISPayloadEncyptionUtil.convertToDouble(encodedMessage.getBits(61, 89)) / 600000f;
		Double latitude = AISPayloadEncyptionUtil.convertToDouble(encodedMessage.getBits(89, 116)) / 600000f;
		Double courseOverGround = AISPayloadEncyptionUtil.convertToUnsignedDouble(encodedMessage.getBits(116, 128))
				/ 10f;
		Integer trueHeading = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(128, 137));
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(137, 143));
		ManeuverIndicator maneuverIndicator = ManeuverIndicator
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(148, 149));

		return new PositionReportClassA(encodedMessage.getOriginalNmea(), AISMessageType.PositionReportClassA,
				repeatIndicator, sourceMmsi, navigationStatus, rateOfTurn, speedOverGround, positionAccurate, latitude,
				longitude, courseOverGround, trueHeading, second, maneuverIndicator, raimFlag);
	}

	/**
	 * PositionReportClassAAssignedSchedule
	 */
	public static PositionReportClassAAssignedSchedule decodePositionReportClassAAssignedSchedule(
			EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.PositionReportClassAAssignedSchedule))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		NavigationStatus navigationStatus = NavigationStatus
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 42)));
		Integer rateOfTurn = AISPayloadEncyptionUtil.convertToSignedInteger(encodedMessage.getBits(42, 50));
		Double speedOverGround = AISPayloadEncyptionUtil.convertToUnsignedDouble(encodedMessage.getBits(50, 60)) / 10f;
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(60, 61));
		Double longitude = AISPayloadEncyptionUtil.convertToDouble(encodedMessage.getBits(61, 89)) / 600000f;
		Double latitude = AISPayloadEncyptionUtil.convertToDouble(encodedMessage.getBits(89, 116)) / 600000f;
		Double courseOverGround = AISPayloadEncyptionUtil.convertToUnsignedDouble(encodedMessage.getBits(116, 128))
				/ 10f;
		Integer trueHeading = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(128, 137));
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(137, 143));
		ManeuverIndicator maneuverIndicator = ManeuverIndicator
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(148, 149));

		return new PositionReportClassAAssignedSchedule(encodedMessage.getOriginalNmea(),
				AISMessageType.PositionReportClassAAssignedSchedule, repeatIndicator, sourceMmsi, navigationStatus,
				rateOfTurn, speedOverGround, positionAccurate, latitude, longitude, courseOverGround, trueHeading,
				second, maneuverIndicator, raimFlag);
	}

	/**
	 * PositionReportClassAResponseToInterrogation
	 */
	public static PositionReportClassAResponseToInterrogation decodePositionReportClassAResponseToInterrogation(
			EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.PositionReportClassAResponseToInterrogation))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		NavigationStatus navigationStatus = NavigationStatus
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 42)));
		Integer rateOfTurn = AISPayloadEncyptionUtil.convertToSignedInteger(encodedMessage.getBits(42, 50));
		Double speedOverGround = AISPayloadEncyptionUtil.convertToUnsignedDouble(encodedMessage.getBits(50, 60)) / 10f;
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(60, 61));
		Double longitude = AISPayloadEncyptionUtil.convertToDouble(encodedMessage.getBits(61, 89)) / 600000f;
		Double latitude = AISPayloadEncyptionUtil.convertToDouble(encodedMessage.getBits(89, 116)) / 600000f;
		Double courseOverGround = AISPayloadEncyptionUtil.convertToUnsignedDouble(encodedMessage.getBits(116, 128))
				/ 10f;
		Integer trueHeading = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(128, 137));
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(137, 143));
		ManeuverIndicator maneuverIndicator = ManeuverIndicator
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(148, 149));

		return new PositionReportClassAResponseToInterrogation(encodedMessage.getOriginalNmea(),
				AISMessageType.PositionReportClassAResponseToInterrogation, repeatIndicator, sourceMmsi,
				navigationStatus, rateOfTurn, speedOverGround, positionAccurate, latitude, longitude, courseOverGround,
				trueHeading, second, maneuverIndicator, raimFlag);
	}

	/**
	 * BaseStationReport
	 */
	public static BaseStationReport decodeBaseStationReport(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.BaseStationReport))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		Integer year = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 52));
		Integer month = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(52, 56));
		Integer day = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(56, 61));
		Integer hour = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(61, 66));
		Integer minute = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(66, 72));
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(72, 78));
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(78, 79));
		Float longitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(79, 107)) / 600000f;
		Float latitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(107, 134)) / 600000f;
		PositionFixingDevice positionFixingDevice = PositionFixingDevice
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(134, 138)));
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(148, 149));

		return new BaseStationReport(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, year, month, day,
				hour, minute, second, positionAccurate, latitude, longitude, positionFixingDevice, raimFlag);
	}

	/**
	 * BinaryAddressedMessage
	 */
	public static BinaryAddressedMessage decodeBinaryAddressedMessage(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.BinaryAddressedMessage))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer sequenceNumber = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		MMSI destinationMmsi = MMSI
				.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Boolean retransmit = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(70, 71));
		Integer designatedAreaCode = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(72, 82));
		Integer functionalId = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(82, 88));
		String binaryData = encodedMessage.getBits(88, 1009);

		return new BinaryAddressedMessage(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, sequenceNumber,
				destinationMmsi, retransmit, designatedAreaCode, functionalId, binaryData);
	}

	/**
	 * BinaryAcknowledge (SafetyRelatedAcknowledge)
	 */
	public static BinaryAcknowledge decodeBinaryAcknowledge(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.BinaryAcknowledge))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		MMSI mmsi1 = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		MMSI mmsi2 = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(72, 102)));
		MMSI mmsi3 = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(104, 134)));
		MMSI mmsi4 = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(136, 166)));

		return new BinaryAcknowledge(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, mmsi1, mmsi2, mmsi3,
				mmsi4);
	}

	/**
	 * BinaryBroadcastMessage
	 */
	public static BinaryBroadcastMessage decodeBinaryBroadcastMessage(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.BinaryBroadcastMessage))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer designatedAreaCode = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 52));
		Integer functionalId = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(52, 56));
		String binaryData = encodedMessage.getBits(52, 56);

		return new BinaryBroadcastMessage(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi,
				designatedAreaCode, functionalId, binaryData);
	}

	/**
	 * StandardSARAircraftPositionReport
	 */
	public static StandardSARAircraftPositionReport decodeStandardSARAircraftPositionReport(
			EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.StandardSARAircraftPositionReport))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer altitude = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 50));
		Integer speed = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(50, 60));
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(60, 61));
		Float longitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(61, 89)) / 600000f;
		Float latitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(89, 116)) / 600000f;
		Float courseOverGround = AISPayloadEncyptionUtil.convertToUnsignedFloat(encodedMessage.getBits(116, 128)) / 10f;
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(128, 134));
		String regionalReserved = encodedMessage.getBits(134, 142);
		Boolean dataTerminalReady = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(142, 143));
		Boolean assigned = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(146, 147));
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(147, 148));
		String radioStatus = encodedMessage.getBits(148, 168);

		return new StandardSARAircraftPositionReport(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi,
				altitude, speed, positionAccurate, latitude, longitude, courseOverGround, second, regionalReserved,
				dataTerminalReady, assigned, raimFlag, radioStatus);
	}

	/**
	 * UTCAndDateInquiry
	 */
	public static UTCAndDateInquiry decodeUTCAndDateInquiry(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.UTCAndDateInquiry))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		MMSI destinationMmsi = MMSI
				.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));

		return new UTCAndDateInquiry(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, destinationMmsi);
	}

	/**
	 * UTCAndDateResponse
	 */
	public static UTCAndDateResponse decodeUTCAndDateResponse(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.UTCAndDateResponse))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		Integer year = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 52));
		Integer month = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(52, 56));
		Integer day = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(56, 61));
		Integer hour = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(61, 66));
		Integer minute = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(66, 72));
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(72, 78));
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(78, 79));
		Float longitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(79, 107)) / 600000f;
		Float latitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(107, 134)) / 600000f;
		PositionFixingDevice positionFixingDevice = PositionFixingDevice
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(134, 138)));
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(148, 149));

		return new UTCAndDateResponse(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, year, month, day,
				hour, minute, second, positionAccurate, latitude, longitude, positionFixingDevice, raimFlag);
	}

	/**
	 * AddressedSafetyRelatedMessage
	 */
	public static AddressedSafetyRelatedMessage decodeAddressedSafetyRelatedMessage(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.AddressedSafetyRelatedMessage))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		Integer sequenceNumber = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 40));
		MMSI destinationMmsi = MMSI
				.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Boolean retransmit = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(70, 71));
		String text = AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(70, 1009));

		return new AddressedSafetyRelatedMessage(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi,
				sequenceNumber, destinationMmsi, retransmit, text);
	}

	/**
	 * SafetyRelatedBroadcastMessage
	 */
	public static SafetyRelatedBroadcastMessage decodeSafetyRelatedBroadcastMessage(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.SafetyRelatedBroadcastMessage))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		String text = AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(40, 1049));

		return new SafetyRelatedBroadcastMessage(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, text);
	}

	/**
	 * Interrogation
	 */
	public static Interrogation decodeInterrogation(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.Interrogation))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		int messageLength = encodedMessage.getNumberOfBits();
		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		MMSI mmsi1 = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer type1_1 = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(70, 76));
		Integer offset1_1 = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(76, 88));
		Integer type1_2 = messageLength > 88
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(90, 96)) : null;
		Integer offset1_2 = messageLength > 88
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(96, 108)) : null;

		MMSI mmsi2 = messageLength > 160
				? MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(110, 140))) : null;
		Integer type2_1 = messageLength > 160
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(140, 146)) : null;
		Integer offset2_1 = messageLength > 160
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(146, 158)) : null;

		return new Interrogation(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, mmsi1, type1_1,
				offset1_1, type1_2, offset1_2, mmsi2, type2_1, offset2_1);
	}

	/**
	 * AssignedModeCommand
	 */
	public static AssignedModeCommand decodeAssignedModeCommand(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.AssignedModeCommand))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		int messageLength = encodedMessage.getNumberOfBits();
		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		MMSI destinationMmsiA = MMSI
				.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer offsetA = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(70, 82));
		Integer incrementA = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(82, 92));

		MMSI destinationMmsiB = messageLength >= 144
				? MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(92, 122))) : null;
		Integer offsetB = messageLength >= 144
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(122, 134)) : null;
		Integer incrementB = messageLength >= 144
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(134, 144)) : null;

		return new AssignedModeCommand(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, destinationMmsiA,
				offsetA, incrementA, destinationMmsiB, offsetB, incrementB);
	}

	/**
	 * DGNSSBinaryBroadcastMessage
	 */
	public static DGNSSBinaryBroadcastMessage decodeDGNSSBinaryBroadcastMessage(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.DGNSSBinaryBroadcastMessage))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Float longitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(40, 58)) / 10f;
		Float latitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(58, 75)) / 10f;
		String binaryData = encodedMessage.getBits(80, 816);

		return new DGNSSBinaryBroadcastMessage(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, latitude,
				longitude, binaryData);
	}

	/**
	 * StandardClassBCSPositionReport
	 */
	public static StandardClassBCSPositionReport decodeStandardClassBCSPositionReport(
			EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.StandardClassBCSPositionReport))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		String regionalReserved1 = encodedMessage.getBits(38, 46);
		Float speedOverGround = AISPayloadEncyptionUtil.convertToUnsignedFloat(encodedMessage.getBits(46, 55)) / 10f;
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(56, 57));
		Float longitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(57, 85)) / 600000f;
		Float latitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(85, 112)) / 600000f;
		Float courseOverGround = AISPayloadEncyptionUtil.convertToUnsignedFloat(encodedMessage.getBits(112, 124)) / 10f;
		Integer trueHeading = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(124, 133));
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(133, 139));
		String regionalReserved2 = encodedMessage.getBits(139, 141);
		Boolean csUnit = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(141, 142));
		Boolean display = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(142, 143));
		Boolean dsc = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(143, 144));
		Boolean band = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(144, 145));
		Boolean message22 = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(145, 146));
		Boolean assigned = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(146, 147));
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(147, 148));
		String radioStatus = encodedMessage.getBits(148, 168);

		return new StandardClassBCSPositionReport(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi,
				regionalReserved1, speedOverGround, positionAccurate, latitude, longitude, courseOverGround,
				trueHeading, second, regionalReserved2, csUnit, display, dsc, band, message22, assigned, raimFlag,
				radioStatus);
	}

	/**
	 * ExtendedClassBEquipmentPositionReport
	 */
	public static ExtendedClassBEquipmentPositionReport decodeExtendedClassBEquipmentPositionReport(
			EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.ExtendedClassBEquipmentPositionReport))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		String regionalReserved1 = encodedMessage.getBits(38, 46);
		Float speedOverGround = AISPayloadEncyptionUtil.convertToUnsignedFloat(encodedMessage.getBits(46, 55)) / 10f;
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(56, 57));
		Float longitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(57, 85)) / 600000f;
		Float latitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(85, 112)) / 600000f;
		Float courseOverGround = AISPayloadEncyptionUtil.convertToUnsignedFloat(encodedMessage.getBits(112, 124)) / 10f;
		Integer trueHeading = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(124, 133));
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(133, 139));
		String regionalReserved2 = encodedMessage.getBits(139, 143);
		String shipName = AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(143, 263));
		ShipType shipType = ShipType
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(263, 271)));
		Integer toBow = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(271, 280));
		Integer toStern = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(280, 289));
		Integer toPort = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(289, 295));
		Integer toStarboard = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(295, 301));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(301, 305)));
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(305, 306));
		Boolean dataTerminalReady = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(306, 307));
		Boolean assigned = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(307, 308));

		return new ExtendedClassBEquipmentPositionReport(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi,
				regionalReserved1, speedOverGround, positionAccurate, latitude, longitude, courseOverGround,
				trueHeading, second, regionalReserved2, shipName, shipType, toBow, toStern, toStarboard, toPort,
				positionFixingDevice, raimFlag, dataTerminalReady, assigned);
	}

	/**
	 * DataLinkManagement
	 */
	public static DataLinkManagement decodeDataLinkManagement(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.DataLinkManagement))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		final int n = encodedMessage.getNumberOfBits();

		Integer offsetNumber1 = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(40, 52));
		Integer reservedSlots1 = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(52, 56));
		Integer timeout1 = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(56, 59));
		Integer increment1 = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(59, 70));
		Integer offsetNumber2 = n >= 100
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(70, 82)) : null;
		Integer reservedSlots2 = n >= 100
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(82, 86)) : null;
		Integer timeout2 = n >= 100 ? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(86, 89))
				: null;
		Integer increment2 = n >= 100
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(89, 100)) : null;
		Integer offsetNumber3 = n >= 130
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(100, 112)) : null;
		Integer reservedSlots3 = n >= 130
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(112, 116)) : null;
		Integer timeout3 = n >= 130 ? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(116, 119))
				: null;
		Integer increment3 = n >= 130
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(119, 130)) : null;
		Integer offsetNumber4 = n >= 160
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(130, 142)) : null;
		Integer reservedSlots4 = n >= 160
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(142, 146)) : null;
		Integer timeout4 = n >= 160 ? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(146, 149))
				: null;
		Integer increment4 = n >= 160
				? AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(149, 160)) : null;

		return new DataLinkManagement(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, offsetNumber1,
				reservedSlots1, timeout1, increment1, offsetNumber2, reservedSlots2, timeout2, increment2,
				offsetNumber3, reservedSlots3, timeout3, increment3, offsetNumber4, reservedSlots4, timeout4,
				increment4);
	}

	/**
	 * AidToNavigationReport, see
	 * http://catb.org/gpsd/AIVDM.html#_type_21_aid_to_navigation_report for
	 * specification
	 */
	public static AidToNavigationReport decodeAidToNavigationReport(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.AidToNavigationReport))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		AidType aidType = AidType
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 43)));
		String name = AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(43, 163));
		Boolean positionAccurate = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(163, 164));
		Float longitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(164, 192)) / 600000f;
		Float latitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(192, 219)) / 600000f;
		Integer toBow = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(219, 228));
		Integer toStern = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(228, 237));
		Integer toPort = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(237, 243));
		Integer toStarboard = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(243, 249));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(249, 253)));
		Integer second = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(253, 259));
		Boolean offPosition = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(259, 260));
		String regionalUse = encodedMessage.getBits(260, 268);
		Boolean raimFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(268, 269));
		Boolean virtualAid = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(269, 270));
		Boolean assignedMode = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(270, 271));

		/*
		 * Definition
		 * 
		 * The name field is up to 20 characters of 6-bit ASCII. If this field
		 * is full (has no trailing @ characters) the decoder should interpret
		 * the Name Extension field later in the message (no more than 14 6-bit
		 * characters) and concatenate it to this one to obtain the full name.
		 * 
		 * If present, the Name Extension consists of packed six-bit ASCII
		 * characters followed by 0-6 bits of padding to an 8-bit boundary. The
		 * [IALA] description says "This parameter should be omitted when no
		 * more than 20 characters for the name of the A-to-N are needed in
		 * total. Only the required number of characters should be transmitted,
		 * i.e. no @-character should be used." A decoder can deduce the bit
		 * length of the name extension field by subtracting 272 from the total
		 * message bit length.
		 */
		int messageLength = encodedMessage.getBits().length();
		String nameExtension = AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(272, messageLength));

		return new AidToNavigationReport(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, aidType, name,
				positionAccurate, latitude, longitude, toBow, toStern, toStarboard, toPort, positionFixingDevice,
				second, offPosition, regionalUse, raimFlag, virtualAid, assignedMode, nameExtension);
	}

	/**
	 * ChannelManagement
	 */
	public static ChannelManagement decodeChannelManagement(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.ChannelManagement))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer channelA = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(40, 52));
		Integer channelB = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(52, 64));
		TxRxMode transmitReceiveMode = TxRxMode
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(64, 68)));
		Boolean power = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(68, 69));
		Boolean addressed = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(139, 140));
		Boolean bandA = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(140, 141));
		Boolean bandB = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(141, 142));
		Integer zoneSize = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(142, 145));
		MMSI destinationMmsi1 = !addressed ? null
				: MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(69, 99)));
		MMSI destinationMmsi2 = !addressed ? null
				: MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(104, 134)));
		Float northEastLatitude = addressed ? null
				: AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(87, 104)) / 10f;
		Float northEastLongitude = addressed ? null
				: AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(69, 87)) / 10f;
		Float southWestLatitude = addressed ? null
				: AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(122, 138)) / 10f;
		Float southWestLongitude = addressed ? null
				: AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(104, 122)) / 10f;

		return new ChannelManagement(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, channelA, channelB,
				transmitReceiveMode, power, northEastLongitude, northEastLatitude, southWestLongitude,
				southWestLatitude, destinationMmsi1, destinationMmsi2, addressed, bandA, bandB, zoneSize);
	}

	/**
	 * GroupAssignmentCommand
	 */
	public static GroupAssignmentCommand decodeGroupAssignmentCommand(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.GroupAssignmentCommand))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Float northEastLatitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(61, 89)) / 10f;
		Float northEastLongitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(61, 89)) / 10f;
		Float southWestLatitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(61, 89)) / 10f;
		Float southWestLongitude = AISPayloadEncyptionUtil.convertToFloat(encodedMessage.getBits(61, 89)) / 10f;
		StationType stationType = StationType
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		ShipType shipType = ShipType
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		TxRxMode transmitReceiveMode = TxRxMode
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(128, 137)));
		ReportingInterval reportingInterval = ReportingInterval
				.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(143, 145)));
		Integer quietTime = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(128, 137));

		return new GroupAssignmentCommand(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi,
				northEastLatitude, northEastLongitude, southWestLatitude, southWestLongitude, stationType, shipType,
				transmitReceiveMode, reportingInterval, quietTime);
	}

	/**
	 * StaticDataReport
	 */
	public static StaticDataReport decodeStaticDataReport(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.StaticDataReport))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Integer partNumber = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(38, 40));

		String shipName = partNumber == 1 ? null
				: AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(40, 160));
		ShipType shipType = partNumber == 0 ? null
				: ShipType
						.fromInteger(AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(40, 48)));
		String vendorId = partNumber == 0 ? null
				: AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(48, 90));
		String callsign = partNumber == 0 ? null
				: AISPayloadEncyptionUtil.convertToString(encodedMessage.getBits(90, 132));
		Integer toBow = partNumber == 0 ? null
				: AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(132, 141));
		Integer toStern = partNumber == 0 ? null
				: AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(141, 150));
		Integer toPort = partNumber == 0 ? null
				: AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(150, 156));
		Integer toStarboard = partNumber == 0 ? null
				: AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(156, 162));
		MMSI mothershipMmsi = partNumber == 0 ? null
				: MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(132, 162)));

		return new StaticDataReport(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi, partNumber, shipName,
				shipType, vendorId, callsign, toBow, toStern, toStarboard, toPort, mothershipMmsi);
	}

	/**
	 * SingleSlotBinaryMessage
	 */
	public static SingleSlotBinaryMessage decodeSingleSlotBinaryMessage(EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.SingleSlotBinaryMessage))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Boolean destinationIndicator = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(38, 39));
		Boolean binaryDataFlag = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(39, 40));
		MMSI destinationMMSI = MMSI
				.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		String binaryData = encodedMessage.getBits(40, 168);

		return new SingleSlotBinaryMessage(encodedMessage.getOriginalNmea(), repeatIndicator, sourceMmsi,
				destinationIndicator, binaryDataFlag, destinationMMSI, binaryData);
	}

	/**
	 * MultipleSlotBinaryMessageWithCommunicationState
	 */
	public static MultipleSlotBinaryMessageWithCommunicationState decodeMultipleSlotBinaryMessageWithCommunicationState(
			EncodedAISPayload encodedMessage) {
		if (!encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		if (!encodedMessage.getMessageType().equals(AISMessageType.MultipleSlotBinaryMessageWithCommunicationState))
			throw new UnsupportedPayloadType(encodedMessage.getMessageType().getMessageType());

		Integer repeatIndicator = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(8, 38)));

		Boolean addressed = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(38, 39));
		Boolean structured = AISPayloadEncyptionUtil.convertToBoolean(encodedMessage.getBits(39, 40));
		MMSI destinationMmsi = MMSI
				.valueOf(AISPayloadEncyptionUtil.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		Integer applicationId = AISPayloadEncyptionUtil.convertToUnsignedInteger(encodedMessage.getBits(70, 86));
		String data = encodedMessage.getBits(86, 86 + 1004 + 1);
		String radioStatus = null; // Decoder.convertToBinaryString(encodedMessage.getBits(6,
									// 8));

		return new MultipleSlotBinaryMessageWithCommunicationState(encodedMessage.getOriginalNmea(), repeatIndicator,
				sourceMmsi, addressed, structured, destinationMmsi, applicationId, data, radioStatus);
	}
}
