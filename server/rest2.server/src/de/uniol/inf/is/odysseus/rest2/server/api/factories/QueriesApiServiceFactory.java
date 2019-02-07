package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.QueriesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.QueriesApiServiceImpl;

public class QueriesApiServiceFactory {
    private final static QueriesApiService service = new QueriesApiServiceImpl();

    public static QueriesApiService getQueriesApi() {
        return service;
    }
}
