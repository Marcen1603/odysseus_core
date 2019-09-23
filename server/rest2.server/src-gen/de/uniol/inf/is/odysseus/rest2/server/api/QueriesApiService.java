package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;

public abstract class QueriesApiService {
    public abstract Response queriesGet(Optional<ISession> session);
    public abstract Response queriesIdDelete(Optional<ISession> session, Integer id
 );
    public abstract Response queriesIdGet(Optional<ISession> session, Integer id
 );
    public abstract Response queriesIdPut(Optional<ISession> session, Integer id
 ,Query query
 );
    public abstract Response queriesNameDelete(Optional<ISession> session, String name
 );
    public abstract Response queriesNameGet(Optional<ISession> session, String name
 );
    public abstract Response queriesNamePut(Optional<ISession> session, String name
 ,Query query
 );
    public abstract Response queriesPost(Optional<ISession> session, Query query
 );
}
