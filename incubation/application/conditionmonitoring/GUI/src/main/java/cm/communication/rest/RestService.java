package cm.communication.rest;

import cm.communication.dto.*;
import cm.communication.json.SocketInfoDeserializer;
import cm.configuration.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.*;

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
    public static final String RESOURCE_PATH_CREATE_SOCKET = "createMultiSocket";
    public static final String RESOURCE_PATH_START_QUERY = "startQuery";
    public static final String RESOURCE_PATH_GET_CONFIGURATIONS = "CMGetConfigurationList";
    public static final String RESOURCE_PATH_RUN_CONFIGURATION = "RunConfiguration";


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

        try {
            Representation t = cr.post(req);
            Type resultDataType = new TypeToken<GenericResponseDTO<String>>() {
            }.getType();
            Gson gson = new Gson();
            GenericResponseDTO<String> resp = gson.fromJson(t.getText(), resultDataType);
            return resp.getValue();
        } catch (IOException ex) {
            throw new RestException(ex.toString());
        } catch (Exception ex) {
            throw new RestException(ex.toString());
        } finally {
            cr.release();
        }
    }

    public static Map<String, Map<Integer, SocketInfo>> runQuery(String ip, String token, String query) throws RestException {
        String hostURL = BASE_PROTOCOL + ip + ":" + SERVICE_PORT + "/" + SERVICE_PATH_CORE;
        ClientResource crAddQuery = new ClientResource(hostURL + "/" + RESOURCE_PATH_ADD_QUERY);
        ClientResource crCreateSocket = new ClientResource(hostURL + "/" + RESOURCE_PATH_CREATE_SOCKET);

        Gson gson = new Gson();

        // Add query
        AddQueryRequestDTO addQueryRequestDTO = new AddQueryRequestDTO(token, query, "OdysseusScript", "");
        Representation addQueryRepresentation = crAddQuery.post(addQueryRequestDTO);
        GenericResponseDTO<Collection<Double>> queryIds = null;
        try {
            queryIds = gson.fromJson(addQueryRepresentation.getText(), GenericResponseDTO.class);
            // Create socket
            int queryId = queryIds.getValue().iterator().next().intValue();
            return getResultsFromQuery(ip, token, queryId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, String queryName) throws RestException {
        CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryName);
        return getResultsFromQuery(ip, createSocketRequestDTO);
    }

    public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, int queryId) throws RestException {
        CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryId);
        return getResultsFromQuery(ip, createSocketRequestDTO);
    }

    public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, String queryName, String operatorOutputName, int operatorOutputPort) throws RestException {
        CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryName, operatorOutputName, operatorOutputPort);
        return getResultsFromQuery(ip, createSocketRequestDTO);
    }

    public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, int queryId, String operatorOutputName, int operatorOutputPort)
            throws RestException {
        CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryId, operatorOutputName, operatorOutputPort);
        return getResultsFromQuery(ip, createSocketRequestDTO);
    }

    public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, String queryName, String operatorOutputName) throws RestException {
        CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryName, operatorOutputName);
        return getResultsFromQuery(ip, createSocketRequestDTO);
    }

    public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, int queryId, String operatorOutputName) throws RestException {
        CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryId, operatorOutputName);
        return getResultsFromQuery(ip, createSocketRequestDTO);
    }


    public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, CreateSocketRequestDTO createSocketRequestDTO) throws RestException {
        String hostURL = BASE_PROTOCOL + ip + ":" + SERVICE_PORT + "/" + SERVICE_PATH_CORE;
        ClientResource crCreateSocket = new ClientResource(hostURL + "/" + RESOURCE_PATH_CREATE_SOCKET);

        Gson gson = new GsonBuilder().registerTypeAdapter(SocketInfo.class, new SocketInfoDeserializer()).create();
        try {
            Representation createSocketRepresentation = crCreateSocket.post(createSocketRequestDTO);
            Type socketType = new TypeToken<HashMap<String, HashMap<Integer, SocketInfo>>>(){}.getType();
            return gson.fromJson(createSocketRepresentation.getText(), socketType);
        } catch (IOException ex) {
            throw new RestException(ex.toString());
        } finally {
            crCreateSocket.release();
        }
    }

    public static List<ConfigurationDescription> getConfigurations(String ip, String username, String password) throws RestException {
        String token = login(ip, username, password);
        return getConfigurations(ip, token);
    }

    public static List<ConfigurationDescription> getConfigurations(String ip, String token) throws RestException {
        try {
            String hostURL = BASE_PROTOCOL + ip + ":" + SERVICE_PORT + "/" + SERVICE_PATH_CONDITION;
            ClientResource crGetConfigs = new ClientResource(hostURL + "/" + RESOURCE_PATH_GET_CONFIGURATIONS);
            GetCMConfigurationListRequestDTO requestDTO = new GetCMConfigurationListRequestDTO(token);
            Representation getConfigurationsRepresentation = crGetConfigs.post(requestDTO);
            Gson gson = new Gson();
            CMConfigurationListResponseDTO responseDTO = gson.fromJson(getConfigurationsRepresentation.getText(), CMConfigurationListResponseDTO.class);
            return responseDTO.getConfigurations();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Configuration runConfiguration(String ip, String username, String password, UUID configurationId) throws RestException {
        String token = login(ip, username, password);
        return runConfiguration(ip, token, configurationId);
    }

    public static Configuration runConfiguration(String ip, String token, UUID configurationId) throws RestException {
        String hostURL = BASE_PROTOCOL + ip + ":" + SERVICE_PORT + "/" + SERVICE_PATH_CONDITION;
        ClientResource crRunConfigs = new ClientResource(hostURL + "/" + RESOURCE_PATH_RUN_CONFIGURATION);
        RunConfigurationRequestDTO runConfigurationRequestDTO = new RunConfigurationRequestDTO();
        runConfigurationRequestDTO.setConfigurationId(configurationId);
        runConfigurationRequestDTO.setToken(token);
        Representation runConfigRepresentation = crRunConfigs.post(runConfigurationRequestDTO);
        Gson gson = new Gson();
        try {
            String config = runConfigRepresentation.getText();
            Configuration configuration = gson.fromJson(config, Configuration.class);
            return configuration;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
