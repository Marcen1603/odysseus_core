package de.uniol.inf.is.odysseus.rest2.server.api.factories;

import de.uniol.inf.is.odysseus.rest2.server.api.OperatorsApiService;
import de.uniol.inf.is.odysseus.rest2.server.api.impl.OperatorsApiServiceImpl;

public class OperatorsApiServiceFactory {
    private final static OperatorsApiService service = new OperatorsApiServiceImpl();

    public static OperatorsApiService getOperatorsApi() {
        return service;
    }
}
