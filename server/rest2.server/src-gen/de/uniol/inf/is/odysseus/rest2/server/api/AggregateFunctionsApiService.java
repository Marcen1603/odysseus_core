package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.core.Response;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class AggregateFunctionsApiService {
    public abstract Response aggregateFunctionsGet(Optional<ISession> session, String datamodel
 );
}
