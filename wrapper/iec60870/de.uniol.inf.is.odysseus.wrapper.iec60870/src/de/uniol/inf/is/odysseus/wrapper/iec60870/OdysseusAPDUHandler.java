/*******************************************************************************
 * Copyright 2018 University of Oldenburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.iec60870;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.ei.oj104.communication.IAPDUHandler;
import de.uniol.inf.ei.oj104.communication.IASDUHandler;
import de.uniol.inf.ei.oj104.communication.ICommunicationHandler;
import de.uniol.inf.ei.oj104.exception.IEC608705104ProtocolException;
import de.uniol.inf.ei.oj104.model.APCI;
import de.uniol.inf.ei.oj104.model.APDU;
import de.uniol.inf.ei.oj104.model.ASDU;
import de.uniol.inf.ei.oj104.model.IControlField;
import de.uniol.inf.ei.oj104.model.controlfield.ControlFunctionType;
import de.uniol.inf.ei.oj104.model.controlfield.InformationTransfer;
import de.uniol.inf.ei.oj104.model.controlfield.NumberedSupervisoryFunction;
import de.uniol.inf.ei.oj104.model.controlfield.UnnumberedControlFunction;
import de.uniol.inf.ei.oj104.util.SystemProperties;

//TODO change javadoc
/**
 * Standard implementation of an {@link IAPDUHandler}. An {@link IAPDUHandler}
 * is the central part of the communication architecture of OJ104. The other
 * parts are an {@link ICommunicationHandler} and an {@link IASDUHandler}. An
 * {@link IAPDUHandler} has two main objectives:
 * <ol>
 * <li>Handling of incoming {@link APDU}s from an {@link ICommunicationHandler}.
 * This handling includes the handling of send and reseice sequence numbers and
 * timers as well as the sending of responses via an
 * {@link ICommunicationHandler}.</li>
 * <li>Sending of {@link UnnumberedControlFunction}s to start or stop the data
 * transfer.</li>
 * </ol>
 * The {@link OdysseusAPDUHandler} is implemented to follow the protocol as
 * defined in the standard with all timeouts etc. The length of timeouts and
 * other application variables can be set via the {@link SystemProperties}.
 * 
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class OdysseusAPDUHandler implements IAPDUHandler {

	private static final Logger logger = LoggerFactory.getLogger(OdysseusAPDUHandler.class);

	private IASDUHandler asduHandler;

	private ICommunicationHandler communicationHandler;

	private boolean dtStarted = false;

	private boolean ignoreHandshakes = false;

	private boolean ignoreTimeouts = false;

	private boolean sendResponses = true;

	private Integer receiveState = 0;

	private int sendState = 0;

	private int ackState = 0;

	// sent APDU mapped to their send sequence number
	private Map<Integer, APDU> apduBuffer = new HashMap<>();

	@Override
	public void setASDUHandler(IASDUHandler handler) {
		asduHandler = handler;
	}

	@Override
	public void setCommunicationHandler(ICommunicationHandler handler) {
		communicationHandler = handler;
	}

	@Override
	public boolean isDataTransferStarted() {
		return dtStarted;
	}

	@Override
	public void ignoreHandshakes(boolean ignore) {
		ignoreHandshakes = ignore;
	}

	@Override
	public boolean areHandshakesIgnored() {
		return ignoreHandshakes;
	}

	@Override
	public void ignoreTimeouts(boolean ignore) {
		ignoreTimeouts = ignore;
	}

	@Override
	public boolean areTimeoutsIgnored() {
		return ignoreTimeouts;
	}

	@Override
	public void sendResponses(boolean send) {
		sendResponses = send;
	}

	@Override
	public boolean areResponsesSent() {
		return sendResponses;
	}

	@Override
	public void stopAllThreads() {
		// nothing to do
	}

	private void sendAPDUandLogErrors(APDU apdu) {
		try {
			communicationHandler.send(apdu);
		} catch (IOException | IEC608705104ProtocolException e) {
			logger.error("Error while sending {}!", apdu, e);
		}
	}

	/*
	 * Retrieval of APDUs
	 */

	@Override
	public void handleAPDU(APDU apdu) throws IEC608705104ProtocolException, IOException {
		IControlField controlField = apdu.getApci().getControlField();
		if (controlField instanceof UnnumberedControlFunction) {
			handleUnnumberedControlFunction((UnnumberedControlFunction) controlField, apdu);
		} else if (controlField instanceof InformationTransfer) {
			handleInformationTransfer((InformationTransfer) controlField, apdu);
		} else if (controlField instanceof NumberedSupervisoryFunction) {
			handleNumberedSupervisoryFunction((NumberedSupervisoryFunction) controlField, apdu);
		} else {
			logger.warn("Unknown control field type: {}", controlField.getClass());
		}
	}

	private void handleNumberedSupervisoryFunction(NumberedSupervisoryFunction controlField, APDU apdu)
			throws IEC608705104ProtocolException, IOException {
		// 1. check whether data transfer is started
		if (!ignoreHandshakes && !dtStarted) {
			throw new IEC608705104ProtocolException("Received numbered supervisory function before startDT!");
		}

		// 2. check whether message has been acknowledged
		if (!ignoreHandshakes) {
			handleReceiveSequenceNumberForAck(controlField.getReceiveSequenceNumber(), apdu);
		}
	}

	private void handleInformationTransfer(InformationTransfer controlField, APDU apdu)
			throws IEC608705104ProtocolException, IOException {
		// 1. check whether data transfer is started
		if (!ignoreHandshakes && !dtStarted) {
			throw new IEC608705104ProtocolException("Received information transfer before startDT!");
		}

		// 2. check whether send and receive variables match
		if (!ignoreHandshakes && receiveState != controlField.getSendSequenceNumber()) {
			throw new IEC608705104ProtocolException("Send (" + controlField.getSendSequenceNumber()
					+ ") and receive sequence numbers (" + receiveState + ") do not match for '" + apdu + "'!");
		}
		receiveState++;

		// 3. check whether message has been acknowledged
		if (!ignoreHandshakes) {
			handleReceiveSequenceNumberForAck(controlField.getReceiveSequenceNumber(), apdu);
		}
		
		// 4. call ASDU handler
		Optional<ASDU> responseASDU = asduHandler.handleASDU(apdu.getAsdu());
		
		if(sendResponses) {
			if (responseASDU.isPresent()) {
				// build response with ASDU
				buildAndSendAPDU(responseASDU.get());
			} else {
				buildAndSendSFormat();
			}
		}
	}

	private void handleReceiveSequenceNumberForAck(int receiveSequenceNumber, APDU apdu) {
		if (receiveSequenceNumber > ackState) {
			ackState = receiveSequenceNumber;

			// delete buffered APDUs
			apduBuffer.entrySet().removeIf(entry -> entry.getKey() <= ackState);
			logger.debug("Anknowledged messages upto {}. Removed APDUs from buffer.", ackState);
		} else if (receiveSequenceNumber < ackState) {
			// send buffered APDUs again
			apduBuffer.entrySet().stream()
					.filter(entry -> entry.getKey() > receiveSequenceNumber && entry.getKey() <= ackState)
					.forEach(entry -> sendAPDUandLogErrors(entry.getValue()));
			logger.warn("No anknowledgement for messages from {} upto {}. Sent APDUs again.", receiveSequenceNumber,
					ackState);
		}
	}

	private void handleUnnumberedControlFunction(UnnumberedControlFunction controlField, APDU apdu)
			throws IEC608705104ProtocolException, IOException {
		// 1. check whether data transfer is started
		if (!ignoreHandshakes && !dtStarted && controlField.getType() != ControlFunctionType.STARTDT) {
			throw new IEC608705104ProtocolException("Received unnumbered control function before startDT!");
		}

		Optional<APDU> responseAPDU = Optional.empty();
		switch (controlField.getType()) {
		case STARTDT:
			if (controlField.isActivate() && sendResponses) {
				// send confirm
				responseAPDU = Optional.of(new APDU(new APCI(IControlField.getEncodedSize(),
						new UnnumberedControlFunction(ControlFunctionType.STARTDT, false))));
			}

			// set startDT flag
			dtStarted = true;
			break;
		case STOPDT:
			if (controlField.isActivate() && sendResponses) {
				// send confirm
				responseAPDU = Optional.of(new APDU(new APCI(IControlField.getEncodedSize(),
						new UnnumberedControlFunction(ControlFunctionType.STOPDT, false))));
			}

			// set startDT flag
			dtStarted = false;
			break;
		case TESTFR:
			if (controlField.isActivate() && sendResponses) {
				// send confirm
				responseAPDU = Optional.of(new APDU(new APCI(IControlField.getEncodedSize(),
						new UnnumberedControlFunction(ControlFunctionType.TESTFR, false))));
			}
			break;
		default:
			throw new IEC608705104ProtocolException(
					"Received unknowen control function type: " + controlField.getType());
		}

		responseAPDU.ifPresent(this::sendAPDUandLogErrors);
	}

	/*
	 * Sending of APDUs
	 */

	@Override
	public void startDataTransfer() throws IEC608705104ProtocolException, IOException {
		// 1. check whether data transfer is already started
		if (!ignoreHandshakes && dtStarted) {
			logger.warn("Data transfer is already started! Command is ignored.");
			return;
		}

		// 2. build APDU
		APDU apdu = new APDU(new APCI(IControlField.getEncodedSize(),
				new UnnumberedControlFunction(ControlFunctionType.STARTDT, true)));

		// 3. send APDU
		sendAPDUandLogErrors(apdu);
	}

	@Override
	public void stopDataTransfer() throws IEC608705104ProtocolException, IOException {
		// 1. check whether data transfer is started
		if (!ignoreHandshakes && !dtStarted) {
			logger.warn("Data transfer is not started! Command is ignored.");
			return;
		}

		// 2. build APDU
		APDU apdu = new APDU(new APCI(IControlField.getEncodedSize(),
				new UnnumberedControlFunction(ControlFunctionType.STOPDT, true)));

		// 3. send APDU
		sendAPDUandLogErrors(apdu);
	}

	@Override
	public void buildAndSendAPDU(ASDU asdu) throws IEC608705104ProtocolException, IOException {
		buildAndSendIFormat(asdu);
	}

	private void buildAndSendIFormat(ASDU asdu) throws IEC608705104ProtocolException, IOException {
		// 1. check whether data transfer is started
		if (!ignoreHandshakes && !dtStarted) {
			throw new IEC608705104ProtocolException("Can not sent ASDU before startDT!");
		}

		// 2. build APDU
		 APDU apdu = new APDU(new APCI(IControlField.getEncodedSize() + asdu.getEncodedSize(),
					new InformationTransfer(sendState, receiveState)), asdu);

		// 3. send APDU
		sendAPDUandLogErrors(apdu);
		apduBuffer.put(sendState, apdu);
		sendState++;
	}

	private void buildAndSendSFormat() throws IEC608705104ProtocolException, IOException {
		APDU apdu = new APDU(new APCI(IControlField.getEncodedSize(), new NumberedSupervisoryFunction(receiveState)));
		sendAPDUandLogErrors(apdu);
	}

}