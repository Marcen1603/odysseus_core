package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.User;

public abstract class ServicesApiService {
    public abstract Response servicesBundlesGet(Optional<ISession> session, String filter
 );
    public abstract Response servicesEventsGet(Optional<ISession> session);
    public abstract Response servicesLoginPost(Optional<ISession> session, User user
 );
    public abstract Response servicesOutputschemaPost(Optional<ISession> session, Query query
 ,Integer port
 );
    public abstract Response servicesSessionGet(Optional<ISession> session);
}
