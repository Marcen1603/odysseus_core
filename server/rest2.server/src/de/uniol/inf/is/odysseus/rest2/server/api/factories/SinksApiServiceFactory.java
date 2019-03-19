package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.SinksApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.SinksApiServiceImpl;

public class SinksApiServiceFactory {
    private final static SinksApiService service = new SinksApiServiceImpl();

    public static SinksApiService getSinksApi() {
        return service;
    }
}
