package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.core.Response;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class DatastreamsApiService {
    public abstract Response datastreamsGet(Optional<ISession> session);
    public abstract Response datastreamsNameDelete(Optional<ISession> session, String name
 );
    public abstract Response datastreamsNameGet(Optional<ISession> session, String name
 );
}
