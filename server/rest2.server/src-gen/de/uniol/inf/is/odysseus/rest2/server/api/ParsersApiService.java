package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.*;
import de.uniol.inf.is.odysseus.rest2.common.model.*;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import de.uniol.inf.is.odysseus.rest2.common.model.Query;

import java.util.List;
import java.util.Optional;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public abstract class ParsersApiService {
    public abstract Response parsersGet(Optional<ISession> session);
    public abstract Response parsersNamePost(Optional<ISession> session, String name
 ,String body
 );
}
