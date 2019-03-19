package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.UsersApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.UsersApiServiceImpl;

public class UsersApiServiceFactory {
    private final static UsersApiService service = new UsersApiServiceImpl();

    public static UsersApiService getUsersApi() {
        return service;
    }
}
