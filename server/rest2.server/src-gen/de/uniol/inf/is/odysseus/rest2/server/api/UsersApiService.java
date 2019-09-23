package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.User;

public abstract class UsersApiService {
    public abstract Response usersGet(Optional<ISession> session);
    public abstract Response usersNameGet(Optional<ISession> session, String name
 );
    public abstract Response usersNamePatch(Optional<ISession> session, String name
 ,User user
 );
    public abstract Response usersNamePut(Optional<ISession> session, String name
 ,User user
 );
    public abstract Response usersPost(Optional<ISession> session, User user
 );
}
