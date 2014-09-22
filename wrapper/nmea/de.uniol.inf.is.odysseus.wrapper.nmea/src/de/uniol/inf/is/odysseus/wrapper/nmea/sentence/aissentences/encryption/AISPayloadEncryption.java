package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.encryption;


import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.decoded.DecodedAISPayload;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.EncodedAISPayload;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.exceptions.InvalidEncodedPayload;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.exceptions.UnsupportedPayloadType;
import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.payload.types.AISMessageType;

public class AISPayloadEncryption {

	public AISPayloadEncryption() {
	}
	

	public DecodedAISPayload decode(EncodedAISPayload encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedPayload(encodedMessage);
		
		DecodedAISPayload decodedMessage;
		AISMessageType messageType = encodedMessage.getMessageType();
		
		switch(messageType) {
		case PositionReportClassA:
			decodedMessage = AISPayloadEncyptionUtil.decodePositionReportClassA(encodedMessage);
			break;
		case PositionReportClassAAssignedSchedule:
			decodedMessage = AISPayloadEncyptionUtil.decodePositionReportClassAAssignedSchedule(encodedMessage);
			break;
		case PositionReportClassAResponseToInterrogation:
			decodedMessage = AISPayloadEncyptionUtil.decodePositionReportClassAResponseToInterrogation(encodedMessage);
			break;
		case BaseStationReport:
			decodedMessage = AISPayloadEncyptionUtil.decodeBaseStationReport(encodedMessage);
			break;
		case StaticAndVoyageRelatedData:
			decodedMessage = AISPayloadEncyptionUtil.decodeStaticAndVoyageData(encodedMessage);
			break;
		case BinaryAddressedMessage: 
			decodedMessage = AISPayloadEncyptionUtil.decodeBinaryAddressedMessage(encodedMessage);
			break;
		case BinaryAcknowledge:
		case SafetyRelatedAcknowledge:
			decodedMessage = AISPayloadEncyptionUtil.decodeBinaryAcknowledge(encodedMessage);
			break;
		case BinaryBroadcastMessage:
			decodedMessage = AISPayloadEncyptionUtil.decodeBinaryBroadcastMessage(encodedMessage);
			break;
		case StandardSARAircraftPositionReport:
			decodedMessage = AISPayloadEncyptionUtil.decodeStandardSARAircraftPositionReport(encodedMessage);
			break;
		case UTCAndDateInquiry:
			decodedMessage = AISPayloadEncyptionUtil.decodeUTCAndDateInquiry(encodedMessage);
			break;
		case UTCAndDateResponse:
			decodedMessage = AISPayloadEncyptionUtil.decodeUTCAndDateResponse(encodedMessage);
			break;
		case AddressedSafetyRelatedMessage:
			decodedMessage = AISPayloadEncyptionUtil.decodeAddressedSafetyRelatedMessage(encodedMessage);
			break;
		case SafetyRelatedBroadcastMessage:
			decodedMessage = AISPayloadEncyptionUtil.decodeSafetyRelatedBroadcastMessage(encodedMessage);
			break;
		case Interrogation:
			decodedMessage = AISPayloadEncyptionUtil.decodeInterrogation(encodedMessage);
			break;
		case AssignedModeCommand:
			decodedMessage = AISPayloadEncyptionUtil.decodeAssignedModeCommand(encodedMessage);
			break;
		case DGNSSBinaryBroadcastMessage:
			decodedMessage = AISPayloadEncyptionUtil.decodeDGNSSBinaryBroadcastMessage(encodedMessage);
			break;
		case StandardClassBCSPositionReport:
			decodedMessage = AISPayloadEncyptionUtil.decodeStandardClassBCSPositionReport(encodedMessage);
			break;
		case ExtendedClassBEquipmentPositionReport:
			decodedMessage = AISPayloadEncyptionUtil.decodeExtendedClassBEquipmentPositionReport(encodedMessage);
			break;
		case DataLinkManagement:
			decodedMessage = AISPayloadEncyptionUtil.decodeDataLinkManagement(encodedMessage);
			break;
		case AidToNavigationReport:
			decodedMessage = AISPayloadEncyptionUtil.decodeAidToNavigationReport(encodedMessage);
			break;
		case ChannelManagement:
			decodedMessage = AISPayloadEncyptionUtil.decodeChannelManagement(encodedMessage);
			break;
		case GroupAssignmentCommand:
			decodedMessage = AISPayloadEncyptionUtil.decodeGroupAssignmentCommand(encodedMessage);
			break;
		case StaticDataReport:
			decodedMessage = AISPayloadEncyptionUtil.decodeStaticDataReport(encodedMessage);
			break;
		case SingleSlotBinaryMessage:
			decodedMessage = AISPayloadEncyptionUtil.decodeSingleSlotBinaryMessage(encodedMessage);
			break;
		case MultipleSlotBinaryMessageWithCommunicationState:
			decodedMessage = AISPayloadEncyptionUtil.decodeMultipleSlotBinaryMessageWithCommunicationState(encodedMessage);
			break;
		default:
			throw new UnsupportedPayloadType(messageType.getMessageType());
		}
		
		return decodedMessage;
	}
	
	/***
	 * Encode a decodedPayload. 
	 * @return encodedPayload regarding the AIVDM/AIVDO NMEA Sentence. 
	 */
	public EncodedAISPayload encode(DecodedAISPayload decodedPayload){
		EncodedAISPayload encodedPayload = null;
		return encodedPayload;
	}
}



