package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.ParsersApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.ParsersApiServiceImpl;

public class ParsersApiServiceFactory {
    private final static ParsersApiService service = new ParsersApiServiceImpl();

    public static ParsersApiService getParsersApi() {
        return service;
    }
}
