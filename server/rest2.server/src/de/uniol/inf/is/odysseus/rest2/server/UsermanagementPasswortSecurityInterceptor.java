package de.uniol.inf.is.odysseus.rest2.server;

import java.nio.charset.Charset;
import java.util.Base64;

import org.wso2.msf4j.Request;
import org.wso2.msf4j.Response;
import org.wso2.msf4j.Session;
import org.wso2.msf4j.interceptor.RequestInterceptor;

import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

public class UsermanagementPasswortSecurityInterceptor implements RequestInterceptor {

	public static final String SESSION = "session";
	
	private static final String AUTH_TYPE_BASIC = "Basic";
	public static final String CHARSET_UTF_8 = "UTF-8";
	private static final int AUTH_TYPE_BASIC_LENGTH = AUTH_TYPE_BASIC.length();

	@Override
    public boolean interceptRequest(Request request, Response response) throws Exception {
        String authHeader = request.getHeader(javax.ws.rs.core.HttpHeaders.AUTHORIZATION);
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
                	httpSession.setAttribute(SESSION, session.getToken());
                    return true;
                }
            }

        }
        response.setStatus(javax.ws.rs.core.Response.Status.UNAUTHORIZED.getStatusCode());
        response.setHeader(javax.ws.rs.core.HttpHeaders.WWW_AUTHENTICATE, AUTH_TYPE_BASIC);
        return false;
    }

	private ISession login(String username, String password) {
		// extract tenant from username
		String tenantname = "";
		int pos = username.indexOf('@'); 
		String user = username;
		if (pos > 0) {
			user = username.substring(0,pos);
			tenantname = username.substring(pos+1);
		}
		
		ITenant tenant = UserManagementProvider.instance.getTenant(tenantname);
		ISession session = SessionManagement.instance.login(user, password.getBytes(), tenant);
		
		return session;		
	}

}
