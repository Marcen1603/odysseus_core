package de.uniol.inf.is.odysseus.rest2.server;

import static java.util.stream.Collectors.collectingAndThen;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.core.HttpHeaders;

import org.wso2.msf4j.Request;
import org.wso2.msf4j.Response;
import org.wso2.msf4j.Session;
import org.wso2.msf4j.interceptor.RequestInterceptor;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

public class SecurityAuthInterceptor implements RequestInterceptor {

	public static final String SESSION_ATTRIBUTE_NAME = "session";

	private static final String CHARSET_UTF_8 = "UTF-8";

	private static final String AUTH_TYPE_BASIC = "Basic";
	private static final int AUTH_TYPE_BASIC_LENGTH = AUTH_TYPE_BASIC.length();

	private static final String AUTH_TYPE_BEARER = "Bearer";
	private static final int AUTH_TYPE_BEARER_LENGTH = AUTH_TYPE_BEARER.length();

	// Change to Set.of("...") after switch to Java 9
	private static final Set<String> PUBLIC_URIS = Arrays.stream(new String[] { "/services/login", "/swagger" })
			.collect(collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));

	@Override
	public boolean interceptRequest(Request request, Response response) throws Exception {

		// Check if the resource is public
		if (checkPublicUri(request, response)) {
			return true;
		}

		// Check if the session holds a valid token
		if (checkSessionToken(request, response)) {
			return true;
		}

		// Check if there is a valid token in the HTTP header
		if (checkBearerToken(request, response)) {
			return true;
		}

		// Check if there is a valid username and password as HTTP Basic
		if (checkBasicAuth(request, response)) {
			return true;
		}

		// Unauthorized
		response.setStatus(javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode());
		response.setHeader(HttpHeaders.WWW_AUTHENTICATE, AUTH_TYPE_BASIC);
		return false;
	}

	private boolean checkPublicUri(Request request, Response response) {
		if (PUBLIC_URIS.contains(request.getUri())) {
			return true;
		}
		return false;
	}

	private boolean checkSessionToken(Request request, Response response) {
		Session httpSession = request.getSession();
		Object token = httpSession.getAttribute(SESSION_ATTRIBUTE_NAME);
		if (token != null && token instanceof String) {
			ISession session = SessionManagement.instance.login((String) token);
			if (session != null) {
				return true;
			}
		}
		return false;
	}

	private boolean checkBearerToken(Request request, Response response) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null) {
			String authType = authHeader.substring(0, AUTH_TYPE_BEARER_LENGTH);
			String token = authHeader.substring(AUTH_TYPE_BEARER_LENGTH).trim();
			if (AUTH_TYPE_BEARER.equals(authType) && !token.isEmpty()) {
				ISession session = SessionManagement.instance.login(token);
				if (session != null) {
					Session httpSession = request.getSession();
					httpSession.setAttribute(SESSION_ATTRIBUTE_NAME, session.getToken());
					return true;
				}
			}
		}
		return false;
	}

	private boolean checkBasicAuth(Request request, Response response) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null) {
			String authType = authHeader.substring(0, AUTH_TYPE_BASIC_LENGTH);
			String authEncoded = authHeader.substring(AUTH_TYPE_BASIC_LENGTH).trim();
			if (AUTH_TYPE_BASIC.equals(authType) && !authEncoded.isEmpty()) {
				byte[] decodedByte = authEncoded.getBytes(Charset.forName(CHARSET_UTF_8));
				String authDecoded = new String(Base64.getDecoder().decode(decodedByte),
						Charset.forName(CHARSET_UTF_8));
				String[] authParts = authDecoded.split(":");
				String username = authParts[0];
				String password = authParts[1];
				ISession session = login(username, password);
				if (session != null) {
					Session httpSession = request.getSession();
					httpSession.setAttribute(SESSION_ATTRIBUTE_NAME, session.getToken());
					return true;
				}
			}
		}
		return false;
	}

	private static ISession login(String username, String password) {
		// extract tenant from username
		String tenantname = "";
		int pos = username.indexOf('@');
		String user = username;
		if (pos > 0) {
			user = username.substring(0, pos);
			tenantname = username.substring(pos + 1);
		}

		ITenant tenant = UserManagementProvider.instance.getTenant(tenantname);
		ISession session = SessionManagement.instance.login(user, password.getBytes(), tenant);

		return session;
	}

}
