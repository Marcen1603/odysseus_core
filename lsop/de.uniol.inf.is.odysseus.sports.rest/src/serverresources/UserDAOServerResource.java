package serverresources;

import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import dao.UserDAO;
import de.uniol.inf.is.odysseus.sports.rest.resources.IUserResource;

public class UserDAOServerResource extends ServerResource implements
		IUserResource {
	
	UserDAO user;
	
	@Override
	public void doInit() throws ResourceException {
		this.user = new UserDAO("Thomas");
	}

	@Override
	public UserDAO getUser() {		
		return user;
	}

	@Override
	public void setUser(UserDAO user) {
		this.user = user;
		Response r = getResponse();
		r.setStatus(Status.SUCCESS_ACCEPTED);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	

	
}
