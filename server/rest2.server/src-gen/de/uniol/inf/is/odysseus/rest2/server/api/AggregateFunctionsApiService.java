package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.*;
import de.uniol.inf.is.odysseus.rest2.common.model.*;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;


import java.util.List;
import java.util.Optional;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-18T19:23:10.153Z[GMT]")
public abstract class AggregateFunctionsApiService {
    public abstract Response aggregateFunctionsGet(Optional<ISession> session, String datamodel
 );
}
