package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.FunctionsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.FunctionsApiServiceImpl;

public class FunctionsApiServiceFactory {
    private final static FunctionsApiService service = new FunctionsApiServiceImpl();

    public static FunctionsApiService getFunctionsApi() {
        return service;
    }
}
