package de.uniol.inf.is.odysseus.rest2.server.api;

import de.uniol.inf.is.odysseus.rest2.server.api.*;
import de.uniol.inf.is.odysseus.rest2.server.model.*;

import org.wso2.msf4j.formparam.FormDataParam;
import org.wso2.msf4j.formparam.FileInfo;

import de.uniol.inf.is.odysseus.rest2.server.model.User;

import java.util.List;
import de.uniol.inf.is.odysseus.rest2.server.api.NotFoundException;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public abstract class UsersApiService {
    public abstract Response usersGet() throws NotFoundException;
    public abstract Response usersNameGet(String name
 ) throws NotFoundException;
    public abstract Response usersNamePatch(String name
 ,User user
 ) throws NotFoundException;
    public abstract Response usersNamePut(String name
 ,User user
 ) throws NotFoundException;
    public abstract Response usersPost(User user
 ) throws NotFoundException;
}
