package serverresources;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class GetExampleResource extends ServerResource {
	
	@Get
	public String getexample() {
        String name = (String) getRequestAttributes().get("name");
        return "Your name is "+name;
	}
	
}
