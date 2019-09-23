package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class SinksApiService {
    public abstract Response sinksGet(Optional<ISession> session);
    public abstract Response sinksNameDelete(Optional<ISession> session, String name
 );
    public abstract Response sinksNameGet(Optional<ISession> session, String name
 );
}
