package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.*;
import de.uniol.inf.is.odysseus.rest2.common.model.*;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import de.uniol.inf.is.odysseus.rest2.common.model.Resource;

import java.util.List;
import java.util.Optional;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-18T18:25:21.780Z[GMT]")
public abstract class DatastreamsApiService {
    public abstract Response datastreamsGet(Optional<ISession> session);
    public abstract Response datastreamsNameDelete(Optional<ISession> session, String name
 );
    public abstract Response datastreamsNameGet(Optional<ISession> session, String name
 );
}
