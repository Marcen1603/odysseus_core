package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.DatastreamsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.DatastreamsApiServiceImpl;

public class DatastreamsApiServiceFactory {
    private final static DatastreamsApiService service = new DatastreamsApiServiceImpl();

    public static DatastreamsApiService getDatastreamsApi() {
        return service;
    }
}
