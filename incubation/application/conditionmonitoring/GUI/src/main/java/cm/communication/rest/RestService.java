package cm.communication.rest;

import cm.communication.dto.*;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

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
    public static final String RESOURCE_PATH_START_QUERY = "startQuery";
    public static final String RESOURCE_CONDITIONQL = "conditionQL";


    /**
     * Logs in to Odysseus with a specific username and password
     *
     * @param ip       IP of the Odysseus instance
     * @param username Username of the Odysseus instance (e.g. "System")
     * @param password Password of the given user (e.g. "manager")
     * @return A token which you can use to send requests
     * @throws RestException
     */
    public static String login(String ip, String username, String password) throws RestException {
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

    public static SocketInfo runQuery(String ip, String token, String query) throws RestException {
        String hostURL = BASE_PROTOCOL + ip + ":" + SERVICE_PORT + "/" + SERVICE_PATH_CORE;
        ClientResource crAddQuery = new ClientResource(hostURL + "/" + RESOURCE_PATH_ADD_QUERY);
        ClientResource crCreateSocket = new ClientResource(hostURL + "/" + RESOURCE_PATH_CREATE_SOCKET);

        Gson gson = new Gson();

        try {
            // Add query
            AddQueryRequestDTO addQueryRequestDTO = new AddQueryRequestDTO(token, query, "OdysseusScript", "");
            Representation addQueryRepresentation = crAddQuery.post(addQueryRequestDTO);
            GenericResponseDTO<Collection<Double>> queryIds = gson.fromJson(addQueryRepresentation.getText(), GenericResponseDTO.class);

            // Create socket
            int queryId = queryIds.getValue().iterator().next().intValue();
            CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryId, 0);
            Representation createSocketRepresentation = crCreateSocket.post(createSocketRequestDTO);
            GenericResponseDTO<LinkedTreeMap> socketInfoWrapper = gson.fromJson(createSocketRepresentation.getText(), GenericResponseDTO.class);

            String socketIp = (String) socketInfoWrapper.getValue().get("ip");
            int socketPort = ((Double) socketInfoWrapper.getValue().get("port")).intValue();
            List<AttributeInformation> socketSchema = new ArrayList<AttributeInformation>();
            ArrayList schemaListMap = (ArrayList) socketInfoWrapper.getValue().get("schema");
            LinkedTreeMap schemaMap = (LinkedTreeMap) schemaListMap.get(0);
            String name = "";
            String dataType = "";
            for (Object key : schemaMap.keySet()) {
                String value = (String) schemaMap.get(key);
                if (key.equals("name"))
                    name = value;
                else
                    dataType = value;

                if (!name.isEmpty() && !dataType.isEmpty()) {
                    AttributeInformation info = new AttributeInformation(name, dataType);
                    socketSchema.add(info);
                    name = "";
                    dataType = "";
                }
            }
            return new SocketInfo(socketIp, socketPort, socketSchema);
        } catch (IOException ex) {
            throw new RestException(ex.toString());
        } finally {
            crAddQuery.release();
            crCreateSocket.release();
        }
    }
}
