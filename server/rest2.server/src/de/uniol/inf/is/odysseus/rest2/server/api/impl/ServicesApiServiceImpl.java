package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;
import de.uniol.inf.is.odysseus.rest2.common.model.BundleInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.Token;
import de.uniol.inf.is.odysseus.rest2.common.model.User;
import de.uniol.inf.is.odysseus.rest2.server.Application;
import de.uniol.inf.is.odysseus.rest2.server.api.ServicesApiService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class ServicesApiServiceImpl extends ServicesApiService {

	@Override
	public Response servicesLoginPost(Optional<ISession> ignore, User user) {
		ITenant tenant = UserManagementProvider.instance.getTenant(user.getTenant() == null ? "" : user.getTenant());
		ISession session = SessionManagement.instance.login(user.getUsername(), user.getPassword().getBytes(), tenant);
		if (session != null) {
			final Token token = new Token();
			token.setToken(session.getToken());
			return Response.ok().entity(token).build();
		} else {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@Override
	public Response servicesOutputschemaPost(Optional<ISession> session, Integer port) {
		
		if (!session.isPresent()) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		// do some magic!
		return Response.ok().entity("not implemented").build();
	}

	@Override
	public Response servicesBundlesGet(Optional<ISession> session, String filter) {
		
		if (!session.isPresent()) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		final BundleContext context = Application.getContext();
		final Bundle[] bundles = context.getBundles();
		Stream<Bundle> stream = Arrays.stream(bundles);
		if (filter != null && !filter.trim().equals("")) {
			stream = stream.filter(bundle -> bundle.getSymbolicName().contains(filter));
		}
		final List<BundleInfo> bundleInfos = stream.map(ServicesApiServiceImpl::transform).collect(Collectors.toList());
		return Response.ok().entity(bundleInfos).build();
	}

	private static BundleInfo transform(Bundle bundle) {
		final BundleInfo result = new BundleInfo();
		result.setBundleId(bundle.getBundleId());
		result.setLastModified(bundle.getLastModified());
		switch (bundle.getState()) {
		case Bundle.ACTIVE:
			result.setState(BundleInfo.StateEnum.ACTIVE);
			break;
		case Bundle.INSTALLED:
			result.setState(BundleInfo.StateEnum.INSTALLED);
			break;
		case Bundle.RESOLVED:
			result.setState(BundleInfo.StateEnum.RESOLVED);
			break;
		case Bundle.UNINSTALLED:
			result.setState(BundleInfo.StateEnum.UNINSTALLED);
			break;
		case Bundle.STARTING:
			result.setState(BundleInfo.StateEnum.STARTING);
			break;
		case Bundle.STOPPING:
			result.setState(BundleInfo.StateEnum.STOPPING);
			break;
		default:
			break;
		}
		result.setSymbolicName(bundle.getSymbolicName());
		if (bundle.getVersion() != null) {
			result.setVersion(bundle.getVersion().toString());
		}
		return result;
	}
}
