package de.uniol.inf.is.odysseus.rest.impl;

import org.restlet.routing.Router;

import de.uniol.inf.is.odysseus.rest.provider.IRestProvider;
import de.uniol.inf.is.odysseus.rest.serverresources.AddQueryServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.CreateMultipleSocketServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.CreateSocketServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.DetermineOutputSchemaServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.GetAggregateFunctionsServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.GetAllOperatorInformationServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.GetDatatypesServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.GetOperatorInformationServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.GetQueryIdsServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.GetQueryInformationServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.GetResultStreamInformationServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.GetViewsServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.LoginServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.RemoveQueryServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.RunCommandServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.StartQueryServerResource;
import de.uniol.inf.is.odysseus.rest.serverresources.StopQueryServerResource;

public class CoreRestProvider implements IRestProvider{

	@Override
	public String getPath() {
		return "/core";
	}

	@Override
	public void attachServerResources(Router router) {
		router.attach("/"+LoginServerResource.PATH, LoginServerResource.class);
		router.attach("/"+AddQueryServerResource.PATH, AddQueryServerResource.class);
		router.attach("/"+CreateSocketServerResource.PATH, CreateSocketServerResource.class);
		router.attach("/"+CreateMultipleSocketServerResource.PATH, CreateMultipleSocketServerResource.class);
		router.attach("/"+GetResultStreamInformationServerResource.PATH, GetResultStreamInformationServerResource.class);
		router.attach("/"+StartQueryServerResource.PATH, StartQueryServerResource.class);
		router.attach("/"+StopQueryServerResource.PATH, StopQueryServerResource.class);
		router.attach("/"+RemoveQueryServerResource.PATH, RemoveQueryServerResource.class);
		router.attach("/"+GetQueryIdsServerResource.PATH, GetQueryIdsServerResource.class);
		router.attach("/"+RunCommandServerResource.PATH, RunCommandServerResource.class);
		router.attach("/"+GetAllOperatorInformationServerResource.PATH, GetAllOperatorInformationServerResource.class);
		router.attach("/"+GetOperatorInformationServerResource.PATH, GetOperatorInformationServerResource.class);
		router.attach("/"+GetViewsServerResource.PATH, GetViewsServerResource.class);
		router.attach("/"+GetDatatypesServerResource.PATH, GetDatatypesServerResource.class);
		router.attach("/"+GetAggregateFunctionsServerResource.PATH, GetAggregateFunctionsServerResource.class);
		router.attach("/"+DetermineOutputSchemaServerResource.PATH, DetermineOutputSchemaServerResource.class);
		router.attach("/"+GetQueryInformationServerResource.PATH, GetQueryInformationServerResource.class);
			
	}

}
