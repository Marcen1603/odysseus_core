package de.uniol.inf.is.odysseus.fastflowerdelivery.json;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.uniol.inf.is.odysseus.fastflowerdelivery.event.AbstractEvent;

/**
 * A class used by GSON to serialize instances of the abstract type AbstractEvent to the JSON format.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class JsonAbstractEventAdapter implements JsonSerializer<AbstractEvent> {

	@Override
	public JsonElement serialize(AbstractEvent event, Type type, JsonSerializationContext context) {
		JsonObject obj = context.serialize(event).getAsJsonObject();
		obj.addProperty("type", event.getClass().getSimpleName());
		return obj;
	}

}
