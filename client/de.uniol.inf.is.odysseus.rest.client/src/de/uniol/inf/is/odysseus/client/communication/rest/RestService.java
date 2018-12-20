package de.uniol.inf.is.odysseus.client.communication.rest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.uniol.inf.is.odysseus.client.communication.dto.AddQueryRequestDTO;
import de.uniol.inf.is.odysseus.client.communication.dto.CreateSocketRequestDTO;
import de.uniol.inf.is.odysseus.client.communication.dto.GenericResponseDTO;
import de.uniol.inf.is.odysseus.client.communication.dto.LoginRequestDTO;
import de.uniol.inf.is.odysseus.client.communication.dto.SocketInfo;
import de.uniol.inf.is.odysseus.client.communication.json.SocketInfoDeserializer;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 * @author Tobias Brandt
 * @since 25.04.2015.
 */
public class RestService {

	public static final String RESOURCE_PATH_LOGIN = "login";
	public static final String RESOURCE_PATH_ADD_QUERY = "addQuery";
	public static final String RESOURCE_PATH_CREATE_SOCKET = "createMultiSocket";
	public static final String RESOURCE_PATH_START_QUERY = "startQuery";
	/**
	 * Base protocol is http
	 */
	protected static final String BASE_PROTOCOL = "http://";
	protected static final String SERVICE_PATH_CORE = "core";
	protected static final int SERVICE_PORT = 9679;

	/**
	 * Logs in to Odysseus with a specific username and password
	 *
	 * @param ip
	 *            IP of the Odysseus instance
	 * @param username
	 *            Username of the Odysseus instance (e.g. "System")
	 * @param password
	 *            Password of the given user (e.g. "manager")
	 * @return A token which you can use to send requests
	 * @throws RestException
	 */
	public static String login(String ip, String username, String password, String tenant) throws RestException {
		String hostURL = getHostURL(ip, SERVICE_PATH_CORE, RESOURCE_PATH_LOGIN);

		ClientResource cr = new ClientResource(hostURL);
		LoginRequestDTO req = new LoginRequestDTO(username, password, tenant);

		try {
			Representation t = cr.post(req);
			Type resultDataType = new TypeToken<GenericResponseDTO<String>>() {
			}.getType();
			Gson gson = new Gson();
			GenericResponseDTO<String> resp = gson.fromJson(t.getText(), resultDataType);
			return resp.getValue();
		} catch (Exception ex) {
			throw new RestException(ex.toString());
		} finally {
			cr.release();
		}
	}

	/**
	 * @param ip
	 *            The IP of the Odysseus instance
	 * @param token
	 *            The security token from the login
	 * @param query
	 *            The query to run
	 * @return A map with the name of the operator -> the output port of this
	 *         operator -> the socketInformation
	 * @throws RestException
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public static Map<String, Map<Integer, SocketInfo>> runQuery(String ip, String token, String query)
			throws RestException {
		ClientResource crAddQuery = new ClientResource(getHostURL(ip, SERVICE_PATH_CORE, RESOURCE_PATH_ADD_QUERY));

		// Add query
		String transformationConfig = "";
		AddQueryRequestDTO addQueryRequestDTO = new AddQueryRequestDTO(token, query, "OdysseusScript",
				transformationConfig);
		Representation addQueryRepresentation = crAddQuery.post(addQueryRequestDTO);
		GenericResponseDTO<Collection<Double>> queryIds;
		try {
			Gson gson = new Gson();
			queryIds = gson.fromJson(addQueryRepresentation.getText(), GenericResponseDTO.class);

			/*
			 * WARNING Here we have a simplification that is not always true. What if more
			 * than one query was installed? Use installAndConnectQuery instead or to the
			 * desired connection manually and just install the query with installQuery
			 */

			int queryId = queryIds.getValue().iterator().next().intValue();
			return getResultsFromQuery(ip, token, queryId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param ip
	 *            The IP of the Odysseus instance
	 * @param token
	 *            The security token from the login
	 * @param query
	 *            The query to run
	 * @return A map with the id of the query -> name of the operator -> the output
	 *         port of this operator -> the socketInformation
	 * @throws RestException
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, Map<String, Map<Integer, SocketInfo>>> installAndConnectQuery(String ip, String token,
			String query) throws RestException {
		ClientResource crAddQuery = new ClientResource(getHostURL(ip, SERVICE_PATH_CORE, RESOURCE_PATH_ADD_QUERY));

		// Add query
		String transformationConfig = "";
		AddQueryRequestDTO addQueryRequestDTO = new AddQueryRequestDTO(token, query, "OdysseusScript",
				transformationConfig);
		Representation addQueryRepresentation = crAddQuery.post(addQueryRequestDTO);
		GenericResponseDTO<Collection<Double>> queryIds;
		try {
			Gson gson = new Gson();
			queryIds = gson.fromJson(addQueryRepresentation.getText(), GenericResponseDTO.class);

			// Create sockets

			// QueryID, OperatorName, OutputPort, SocketInfo
			Map<Integer, Map<String, Map<Integer, SocketInfo>>> resultMap = new HashMap<>();

			// We need to do this for every query that may be installed due to
			// the query
			for (double queryId : queryIds.getValue()) {
				int queryIdInt = (int) queryId;
				Map<String, Map<Integer, SocketInfo>> queryResultMap = getResultsFromQuery(ip, token, queryIdInt);
				resultMap.put(queryIdInt, queryResultMap);
			}

			return resultMap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Installs a query string in Odysseus.
	 *
	 * @param ip
	 *            The IP of the Odysseus instance
	 * @param token
	 *            The security token from the login
	 * @param query
	 *            The query to run
	 * @return The list of query ids that were installed
	 */
	public static Collection<Integer> installQuery(String ip, String token, String query) {
		return addQuery(ip, token, query, "OdysseusScript");
	}

	@SuppressWarnings("unchecked")
	public static Collection<Integer> addQuery(String ip, String token, String query, String parser) {
		ClientResource crAddQuery = new ClientResource(getHostURL(ip, SERVICE_PATH_CORE, RESOURCE_PATH_ADD_QUERY));

		// Add query
		String transformationConfig = "";
		AddQueryRequestDTO addQueryRequestDTO = new AddQueryRequestDTO(token, query, parser, transformationConfig);
		try {
			Representation addQueryRepresentation = crAddQuery.post(addQueryRequestDTO);
			GenericResponseDTO<Collection<Integer>> queryIds;
			Gson gson = new Gson();
			queryIds = gson.fromJson(addQueryRepresentation.getText(), GenericResponseDTO.class);
			return queryIds.getValue();

		}

		catch (Exception ex) {

			convertAndThrowException(crAddQuery);
		}

		return Collections.emptyList();

	}

	private static void convertAndThrowException(ClientResource resource) {
		Representation responseRepresentation = resource.getResponseEntity();
		KeyValueObject<IMetaAttribute> kv;
		String message = "";
		try {
			kv = KeyValueObject.createInstance(responseRepresentation.getText());
			message = String.valueOf(kv.path("cause.message"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error on Odysseus server occurred: " + message);
	}

	public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, String queryName)
			throws RestException {
		CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryName);
		return getResultsFromQuery(ip, createSocketRequestDTO);
	}

	public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, int queryId)
			throws RestException {
		CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryId);
		return getResultsFromQuery(ip, createSocketRequestDTO);
	}

	public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, String queryName,
			String operatorOutputName, int operatorOutputPort) throws RestException {
		CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryName, operatorOutputName,
				operatorOutputPort);
		return getResultsFromQuery(ip, createSocketRequestDTO);
	}

	public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, int queryId,
			String operatorOutputName, int operatorOutputPort) throws RestException {
		CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryId, operatorOutputName,
				operatorOutputPort);
		return getResultsFromQuery(ip, createSocketRequestDTO);
	}

	public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, String queryName,
			String operatorOutputName) throws RestException {
		CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryName,
				operatorOutputName);
		return getResultsFromQuery(ip, createSocketRequestDTO);
	}

	public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip, String token, int queryId,
			String operatorOutputName) throws RestException {
		CreateSocketRequestDTO createSocketRequestDTO = new CreateSocketRequestDTO(token, queryId, operatorOutputName);
		return getResultsFromQuery(ip, createSocketRequestDTO);
	}

	/**
	 * @param ip
	 *            The IP of the Odysseus instance
	 * @param createSocketRequestDTO
	 *            The request object
	 * @return The outer map has the name of the operator as key, the inner map the
	 *         output port of the operator
	 * @throws RestException
	 */
	public static Map<String, Map<Integer, SocketInfo>> getResultsFromQuery(String ip,
			CreateSocketRequestDTO createSocketRequestDTO) throws RestException {
		String hostURL = getHostURL(ip, SERVICE_PATH_CORE, RESOURCE_PATH_CREATE_SOCKET);
		ClientResource crCreateSocket = new ClientResource(hostURL);

		Gson gson = new GsonBuilder().registerTypeAdapter(SocketInfo.class, new SocketInfoDeserializer()).create();
		try {
			Representation createSocketRepresentation = crCreateSocket.post(createSocketRequestDTO);
			Type socketType = new TypeToken<HashMap<String, HashMap<Integer, SocketInfo>>>() {
			}.getType();
			return gson.fromJson(createSocketRepresentation.getText(), socketType);
		} catch (Exception ex) {
			throw new RestException(ex.toString());
		} finally {
			crCreateSocket.release();
		}
	}

	private static String getHostURL(String ip, String servicePath, String resourcePath) {
		String hostURL = BASE_PROTOCOL + ip + ":" + SERVICE_PORT + "/" + servicePath + "/" + resourcePath;
		if (ip.contains(":")) {
			// The user entered a port
			hostURL = BASE_PROTOCOL + ip + "/" + servicePath + "/" + resourcePath;
		}
		return hostURL;
	}

	/**
	 * Type of possible request
	 */
	public enum RequestType {
		POST, GET, DELETE
	}

}
