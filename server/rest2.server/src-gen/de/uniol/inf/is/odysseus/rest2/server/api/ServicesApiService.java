package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.*;
import de.uniol.inf.is.odysseus.rest2.common.model.*;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import de.uniol.inf.is.odysseus.rest2.common.model.BundleInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.EventWebSocket;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import de.uniol.inf.is.odysseus.rest2.common.model.Token;
import de.uniol.inf.is.odysseus.rest2.common.model.User;

import java.util.List;
import java.util.Optional;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-19T15:31:13.631Z[GMT]")
public abstract class ServicesApiService {
    public abstract Response servicesBundlesGet(Optional<ISession> session, String filter);
    public abstract Response servicesEventsGet(Optional<ISession> session);
    public abstract Response servicesLoginPost(Optional<ISession> session, User user);
    public abstract Response servicesValidateSession(Optional<ISession> session);
    public abstract Response servicesOutputschemaPost(Optional<ISession> session, Query query
 ,Integer port
 );
}
