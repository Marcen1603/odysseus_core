package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.AggregateFunctionsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.AggregateFunctionsApiServiceImpl;

public class AggregateFunctionsApiServiceFactory {
    private final static AggregateFunctionsApiService service = new AggregateFunctionsApiServiceImpl();

    public static AggregateFunctionsApiService getAggregateFunctionsApi() {
        return service;
    }
}
