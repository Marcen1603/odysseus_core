package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.*;
import de.uniol.inf.is.odysseus.rest2.common.model.*;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import de.uniol.inf.is.odysseus.rest2.common.model.User;

import java.util.List;
import java.util.Optional;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
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
