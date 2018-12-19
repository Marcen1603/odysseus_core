package de.uniol.inf.is.odysseus.client.communication.json;


import com.google.gson.*;

import de.uniol.inf.is.odysseus.client.communication.dto.AttributeInformation;
import de.uniol.inf.is.odysseus.client.communication.dto.SocketInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Deserializes the socket information from Odysseus
 * @author Tobias Brandt
 * @since 26.06.2015.
 */
public class SocketInfoDeserializer implements JsonDeserializer<SocketInfo> {
    @Override
    public SocketInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        Map.Entry<String, JsonElement> entry = obj.entrySet().iterator().next();
        if (entry == null) return null;
        String ip = obj.get("ip").getAsString();
        int socketPort = obj.get("port").getAsInt();

        List<AttributeInformation> socketSchema = new ArrayList<>();
        JsonArray schemaArray = obj.get("schema").getAsJsonArray();
        for (JsonElement element : schemaArray) {
            JsonObject attributeObject = element.getAsJsonObject();
            String attributeName = attributeObject.get("name").getAsString();
            String dataType = attributeObject.get("dataType").getAsString();
            AttributeInformation attributeInformation = new AttributeInformation(attributeName, dataType);
            socketSchema.add(attributeInformation);
        }

        SocketInfo socketInfo = new SocketInfo(ip, socketPort, socketSchema);
        return socketInfo;
    }
}
