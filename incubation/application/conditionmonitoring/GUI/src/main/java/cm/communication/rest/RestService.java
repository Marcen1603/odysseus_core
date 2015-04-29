package cm.communication.rest;

import cm.communication.dto.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Tobias
 * @since 25.04.2015.
 */
public class RestService {

    /**
     * Type of possible request
     */
    public enum RequestType {
        POST, GET, DELETE
    }

    /**
     * Base protocol is http
     */
    private static final String BASE_PROTOCOL = "http://";

    /**
     * The service path is set to /condition
     */
    private static final String SERVICE_PATH_CONDITION = "condition";
    private static final String SERVICE_PATH_CORE = "core";
    private static final int SERVICE_PORT = 9679;
    public static final String RESOURCE_PATH_LOGIN = "login";
    public static final String RESOURCE_PATH_ADD_QUERY = "addQuery";
    public static final String RESOURCE_PATH_CREATE_SOCKET = "createSocket";
    public static final String RESOURCE_CONDITIONQL = "conditionQL";

    public static String login(String ip, String username, String password) throws RestException {

        //Engine.getInstance().getRegisteredConverters().add(new JacksonConverter());

        String hostURL = BASE_PROTOCOL + ip + ":" + SERVICE_PORT + "/" + SERVICE_PATH_CORE + "/" + RESOURCE_PATH_LOGIN;
        ClientResource cr = new ClientResource(hostURL);
        LoginRequestDTO req = new LoginRequestDTO(username, password, "");

        Representation t = cr.post(req);
        try {
            Type resultDataType = new TypeToken<GenericResponseDTO<String>>() {
            }.getType();
            Gson gson = new Gson();

            GenericResponseDTO<String> resp = gson.fromJson(t.getText(), resultDataType);

            return resp.getValue();
        } catch (IOException ex) {
            throw new RestException(ex.toString());
        } finally {
            cr.release();
        }
    }

    public static SocketInfo addConditionQLQuery(String ip, String token, String query) throws RestException {
        String hostURL = BASE_PROTOCOL + ip + ":" + SERVICE_PORT + "/" + SERVICE_PATH_CONDITION + "/" + RESOURCE_CONDITIONQL;

        ClientResource cr = new ClientResource(hostURL);
        ExecuteConditionQLRequestDTO req = new ExecuteConditionQLRequestDTO("System", "manager", query, token);

        Representation t = cr.post(req);
        try {
            Gson gson = new Gson();
            ExecuteConditionQLResponseDTO resp = gson.fromJson(t.getText(), ExecuteConditionQLResponseDTO.class);
            return resp.getSocketInfo();
        } catch (IOException ex) {
            throw new RestException(ex.toString());
        } finally {
            cr.release();
        }
    }
}
