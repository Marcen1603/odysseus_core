package de.uniol.inf.is.odysseus.wrapper.telegram;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class encapsulates all the REST calls to the telegram bot api see
 * https://core.telegram.org/bots/api and https://core.telegram.org/bots
 * 
 * @author Marco Grawunder
 *
 */

public class TelegramBotAPI {

	static private Map<String, TelegramBotAPI> instances = new HashMap<>();
	static protected ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());

	public synchronized static TelegramBotAPI getInstance(String token) {
		TelegramBotAPI api = instances.get(token);
		if (api == null) {
			api = new TelegramBotAPI(token);
			instances.put(token, api);
		}
		return api;
	}

	static final String BASE_URL = "https://api.telegram.org/bot";
	final String token;
	final String urlWithToken;
	private int timeout = 100;
	Long lastId = null;
	Long formerLastId = null;
	
	private TelegramBotAPI(String token) {
		this.token = token;
		this.urlWithToken = BASE_URL + token + "/";
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	public List<JsonNode> getUpdates() throws MalformedURLException, IOException {
		List<JsonNode> resultSet = new ArrayList<>();
		String calluri = urlWithToken + "getUpdates?timeout=" + getTimeout();
		if (lastId != null) {
			calluri = calluri + "&offset=" + (lastId+1);
		}
		JsonNode ret = request(calluri);
		// determine update_id (last
		JsonNode result = ret.get("result");
		Iterator<JsonNode> resultIter = result.iterator();
		while (resultIter.hasNext()) {
			JsonNode next = resultIter.next();
			JsonNode id = next.get("update_id");
			if (id != null) {
				// Could be that the call is ignored because of long polling
				// to allow to reset to old id
				formerLastId = lastId;
				lastId = id.asLong();
			}
			resultSet.add(next);
		}
		return resultSet;
	}
	
	public JsonNode sendMessage(long chatID, String message) throws MalformedURLException, IOException{
		String calluri = urlWithToken + "sendMessage?chat_id="+chatID+"&text="+message;
		JsonNode res = request(calluri);
		return res;
		
	}

	public void resetLastId() {
		lastId = formerLastId;
		formerLastId = null;
	}

	
	private JsonNode request(String calluri) throws MalformedURLException, IOException {
		String val = IOUtils.toString(new URL(calluri));
		if (val != null) {
			return jsonMapper.readTree(val);
		}
		return null;
	}


}
