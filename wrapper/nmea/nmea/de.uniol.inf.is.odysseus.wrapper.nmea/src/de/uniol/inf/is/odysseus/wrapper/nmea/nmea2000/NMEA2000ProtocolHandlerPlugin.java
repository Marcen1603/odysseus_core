package de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.nmea.NMEAProtocolHandler;
import de.uniol.inf.is.odysseus.wrapper.nmea.nmea2000.model.Message;

public class NMEA2000ProtocolHandlerPlugin {

	public static final NMEA2000ProtocolHandlerPlugin INSTANCE = new NMEA2000ProtocolHandlerPlugin();

	private final Logger log = LoggerFactory
			.getLogger(NMEAProtocolHandler.class);

	private ArrayList<Message> messages = new ArrayList<Message>();

	private NMEA2000ProtocolHandlerPlugin() {
	}

	void add(Message message) {
		messages.add(message);
	}

	public void extractMessage(Map<String, Object> map, int pgn,
			byte[] payload) throws IOException {
		Message message = getMessageByPGN(pgn);
		if (message != null) {
			message.parse(map, payload);
		} else {
			log.warn("No NMEA2000 Message corresponding to PGN " + pgn);
		}
	}

	private Message getMessageByPGN(int pgn) {
		for (Message m : messages)
			if (m.pgn == pgn)
				return m;
		return null;
	}
}
