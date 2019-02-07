package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.DatatypesApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.DatatypesApiServiceImpl;

public class DatatypesApiServiceFactory {
    private final static DatatypesApiService service = new DatatypesApiServiceImpl();

    public static DatatypesApiService getDatatypesApi() {
        return service;
    }
}
