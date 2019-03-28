package de.uniol.inf.is.odysseus.rest2.server.api;

import java.util.Optional;

import javax.ws.rs.core.Response;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.User;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
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
