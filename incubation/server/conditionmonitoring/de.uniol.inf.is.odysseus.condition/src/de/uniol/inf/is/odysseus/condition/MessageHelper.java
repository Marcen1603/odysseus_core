package de.uniol.inf.is.odysseus.condition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.condition.messages.MessageEnvelope;
import de.uniol.inf.is.odysseus.condition.messages.MessageType;
import de.uniol.inf.is.odysseus.condition.messages.WarningLevel;
import de.uniol.inf.is.odysseus.condition.messages.WarningMessage;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class MessageHelper {

	public static String createWarningJSONMessage(int machineId, int sensorId, WarningLevel level, String description) {
		WarningMessage warningMessage = new WarningMessage(machineId, sensorId, level, description);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(warningMessage);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

	@SuppressWarnings("rawtypes")
	public static Tuple createWarningTuple(int machineId, int sensorId, WarningLevel level, String description) {
		Tuple out = new Tuple(1, false);
				
		
		//out.setAttribute(0, info);
		return out;
	}

}
