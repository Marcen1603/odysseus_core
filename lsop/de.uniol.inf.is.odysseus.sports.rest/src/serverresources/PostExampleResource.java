package serverresources;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class PostExampleResource extends ServerResource {

	@Post("json")
	public String postexample(JsonRepresentation entity) {
		try {
			return entity.getJsonObject().getString("entity");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
}
