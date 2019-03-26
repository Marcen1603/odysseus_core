package de.uniol.inf.is.odysseus.rest2.client.api;

import de.uniol.inf.is.odysseus.rest2.client.ApiException;
import de.uniol.inf.is.odysseus.rest2.client.RestService;
import de.uniol.inf.is.odysseus.rest2.client.Configuration;
import de.uniol.inf.is.odysseus.rest2.client.Pair;

import javax.ws.rs.core.GenericType;

import de.uniol.inf.is.odysseus.rest2.common.model.BundleInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.Datatype;
import de.uniol.inf.is.odysseus.rest2.common.model.EventWebSocket;
import de.uniol.inf.is.odysseus.rest2.common.model.Function;
import de.uniol.inf.is.odysseus.rest2.common.model.OperatorInfo;
import de.uniol.inf.is.odysseus.rest2.common.model.Query;
import de.uniol.inf.is.odysseus.rest2.common.model.Resource;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import de.uniol.inf.is.odysseus.rest2.common.model.Token;
import de.uniol.inf.is.odysseus.rest2.common.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Code base based on generate code, needed some modifications
 * @author Marco Grawunder
 *
 */

public class DefaultApi {
  private static final String DELETE = "DELETE";
private static final String APPLICATION_JSON = "application/json";

private RestService apiClient;

  public DefaultApi() {
    this(Configuration.getDefaultApiClient());
  }

  public DefaultApi(RestService apiClient) {
    this.apiClient = apiClient;
  }

  public RestService getApiClient() {
    return apiClient;
  }

  public void setApiClient(RestService apiClient) {
    this.apiClient = apiClient;
  }
  
  /**
   * Returns a list of all available aggregate functions.
   * Returns a list of functions that can be used in the [aggregate operator](https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Aggregate+%28and+Group%29+operator).
   * @param datamodel  (optional, default to de.uniol.inf.is.odysseus.core.collection.Tuple)
   * @return List&lt;List&lt;Object&gt;&gt;
   * @throws ApiException if fails to make API call
   */
  public List<List<Object>> aggregateFunctionsGet(String datamodel) throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/aggregate_functions";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "datamodel", datamodel));   
    
    String method = "GET";
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { RestService.O_AUTH };

    GenericType<List<List<Object>>> localVarReturnType = new GenericType<List<List<Object>>>() {};
    return apiClient.invokeAPI(localVarPath, method, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of all available data streams. A data stream is provided by a source or a query (as view).
   * 
   * @return List&lt;Resource&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Resource> datastreamsGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/datastreams";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<Resource>> localVarReturnType = new GenericType<List<Resource>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Removes the data stream with the given name.
   * 
   * @param name The name of the data stream. (required)
   * @throws ApiException if fails to make API call
   */
  public void datastreamsNameDelete(String name) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling datastreamsNameDelete");
    }
    
    // create path and map variables
    String localVarPath = "/datastreams/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
   
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { RestService.O_AUTH  };


    apiClient.invokeAPI(localVarPath, DELETE, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Returns a data stream by name.
   * 
   * @param name The name of the data stream. (required)
   * @return Resource
   * @throws ApiException if fails to make API call
   */
  public Resource datastreamsNameGet(String name) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling datastreamsNameGet");
    }
    
    // create path and map variables
    String localVarPath = "/datastreams/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>(); 
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Resource> localVarReturnType = new GenericType<Resource>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of all available data types.
   * 
   * @return List&lt;Datatype&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Datatype> datatypesGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/datatypes";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<Datatype>> localVarReturnType = new GenericType<List<Datatype>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of all available functions.
   * Returns a list of [functions and operations (MEP)](https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/MEP%3A+Functions+and+Operators) Odysseus provides.
   * @return List&lt;Function&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Function> functionsGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/functions";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<Function>> localVarReturnType = new GenericType<List<Function>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of all available operators.
   * 
   * @return List&lt;OperatorInfo&gt;
   * @throws ApiException if fails to make API call
   */
  public List<OperatorInfo> operatorsGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/operators";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<OperatorInfo>> localVarReturnType = new GenericType<List<OperatorInfo>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of all available parsers.
   * 
   * @return List&lt;String&gt;
   * @throws ApiException if fails to make API call
   */
  public List<String> parsersGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/parsers";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>(); 
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<String>> localVarReturnType = new GenericType<List<String>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Executes a script with the adressed parser.
   * 
   * @param name The name of the parser. (required)
   * @param body  (required)
   * @return Query
   * @throws ApiException if fails to make API call
   */
  public Query parsersNamePost(String name, String body) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling parsersNamePost");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling parsersNamePost");
    }
    
    // create path and map variables
    String localVarPath = "/parsers/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();    
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      "text/plain"
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Query> localVarReturnType = new GenericType<Query>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of all installed queries.
   * 
   * @return List&lt;Query&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Query> queriesGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/queries";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();

    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<Query>> localVarReturnType = new GenericType<List<Query>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Removes the query with the given ID.
   * 
   * @param id The ID of the query. (required)
   * @throws ApiException if fails to make API call
   */
  public void queriesIdDelete(Integer id) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'id' is set
    if (id == null) {
      throw new ApiException(400, "Missing the required parameter 'id' when calling queriesIdDelete");
    }
    
    // create path and map variables
    String localVarPath = "/queries/{id}"
      .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));
    String method = DELETE;

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };


    apiClient.invokeAPI(localVarPath, method, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Returns a query by ID.
   * 
   * @param id The ID of the query. (required)
   * @return Query
   * @throws ApiException if fails to make API call
   */
  public Query queriesIdGet(Integer id) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'id' is set
    if (id == null) {
      throw new ApiException(400, "Missing the required parameter 'id' when calling queriesIdGet");
    }
    
    // create path and map variables
    String localVarPath = "/queries/{id}"
      .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();

    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Query> localVarReturnType = new GenericType<Query>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Updates the query with the given ID.
   * With this operation you can update the state of the query (e.g. to start or stop a query). Updating the query text is not allowed. Remove this query and add a new one instead.
   * @param id The ID of the query. (required)
   * @param body  (required)
   * @return Query
   * @throws ApiException if fails to make API call
   */
  public Query queriesIdPut(Integer id, Query body) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'id' is set
    if (id == null) {
      throw new ApiException(400, "Missing the required parameter 'id' when calling queriesIdPut");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling queriesIdPut");
    }
    
    // create path and map variables
    String localVarPath = "/queries/{id}"
      .replaceAll("\\{" + "id" + "\\}", apiClient.escapeString(id.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      APPLICATION_JSON
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Query> localVarReturnType = new GenericType<Query>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Removes the query with the given name.
   * 
   * @param name The name of the query. (required)
   * @throws ApiException if fails to make API call
   */
  public void queriesNameDelete(String name) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling queriesNameDelete");
    }
    
    // create path and map variables
    String localVarPath = "/queries/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };


    apiClient.invokeAPI(localVarPath, DELETE, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Returns a query by name.
   * 
   * @param name The name of the query. (required)
   * @return Query
   * @throws ApiException if fails to make API call
   */
  public Query queriesNameGet(String name) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling queriesNameGet");
    }
    
    // create path and map variables
    String localVarPath = "/queries/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Query> localVarReturnType = new GenericType<Query>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Updates the query with the given name.
   * With this operation you can update the state of the query (e.g. to start or stop a query). Updating the query text is not allowed. Remove this query and add a new one instead.
   * @param name The name of the query. (required)
   * @param body  (required)
   * @return Query
   * @throws ApiException if fails to make API call
   */
  public Query queriesNamePut(String name, Query body) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling queriesNamePut");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling queriesNamePut");
    }
    
    // create path and map variables
    String localVarPath = "/queries/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      APPLICATION_JSON
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Query> localVarReturnType = new GenericType<Query>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Installs a new query.
   * 
   * @param body  (required)
   * @return Query
   * @throws ApiException if fails to make API call
   */
  public List<Query> queriesPost(Query body) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling queriesPost");
    }
    
    // create path and map variables
    String localVarPath = "/queries";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      APPLICATION_JSON
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<Query>> localVarReturnType = new GenericType<List<Query>>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of installed OSGi bundles.
   * 
   * @param filter If filter is given only bundles whose symbolic name contains the given filter string are returned. (optional)
   * @return List&lt;BundleInfo&gt;
   * @throws ApiException if fails to make API call
   */
  public List<BundleInfo> servicesBundlesGet(String filter) throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/services/bundles";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "filter", filter));
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<BundleInfo>> localVarReturnType = new GenericType<List<BundleInfo>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
 
  
  /**
   * Returns a list of websockets that provides server events.
   * 
   * @return List&lt;EventWebSocket&gt;
   * @throws ApiException if fails to make API call
   */
  public List<EventWebSocket> servicesEventsGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/services/events";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<EventWebSocket>> localVarReturnType = new GenericType<List<EventWebSocket>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Executes a login and returns a token.
   * 
   * @param body  (required)
   * @return Token
   * @throws ApiException if fails to make API call
   */
  public Token servicesLoginPost(User body) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling servicesLoginPost");
    }
    
    // create path and map variables
    String localVarPath = "/services/login";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      APPLICATION_JSON
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] { "HttpBasicAuth" };

    GenericType<Token> localVarReturnType = new GenericType<Token>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  
  /**
   * Returns the current server session.
   * 
   * @return List&lt;EventWebSocket&gt;
   * @throws ApiException if fails to make API call
   */
  public Token servicesSessionGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/services/session";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Token> localVarReturnType = new GenericType<Token>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  
  
  /**
   * Determines the output schema of the given query.
   * 
   * @param body  (required)
   * @param port The port number of the output port of the root operator that should be used to determine the output schema. (optional, default to 0)
   * @return Schema
   * @throws ApiException if fails to make API call
   */
  public Schema servicesOutputschemaPost(Query body, Integer port) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling servicesOutputschemaPost");
    }
    
    // create path and map variables
    String localVarPath = "/services/outputschema";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "port", port));
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      APPLICATION_JSON
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Schema> localVarReturnType = new GenericType<Schema>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of all available sinks.
   * 
   * @return List&lt;Resource&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Resource> sinksGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/sinks";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<Resource>> localVarReturnType = new GenericType<List<Resource>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Removes the sink with the given name.
   * 
   * @param name The name of the sink. (required)
   * @throws ApiException if fails to make API call
   */
  public void sinksNameDelete(String name) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling sinksNameDelete");
    }
    
    // create path and map variables
    String localVarPath = "/sinks/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };


    apiClient.invokeAPI(localVarPath, DELETE, localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
  /**
   * Returns a sink by name.
   * 
   * @param name The name of the sink. (required)
   * @return Resource
   * @throws ApiException if fails to make API call
   */
  public Resource sinksNameGet(String name) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling sinksNameGet");
    }
    
    // create path and map variables
    String localVarPath = "/sinks/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();

    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<Resource> localVarReturnType = new GenericType<Resource>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a list of all users.
   * 
   * @return List&lt;User&gt;
   * @throws ApiException if fails to make API call
   */
  public List<User> usersGet() throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/users";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<List<User>> localVarReturnType = new GenericType<List<User>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Returns a user by username.
   * 
   * @param name The username. (required)
   * @return User
   * @throws ApiException if fails to make API call
   */
  public User usersNameGet(String name) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling usersNameGet");
    }
    
    // create path and map variables
    String localVarPath = "/users/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<User> localVarReturnType = new GenericType<User>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Updates the user with the given username. This action allows to send a partial user object.
   * 
   * @param name The username. (required)
   * @param body  (required)
   * @return User
   * @throws ApiException if fails to make API call
   */
  public User usersNamePatch(String name, User body) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling usersNamePatch");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling usersNamePatch");
    }
    
    // create path and map variables
    String localVarPath = "/users/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      APPLICATION_JSON
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<User> localVarReturnType = new GenericType<User>() {};
    return apiClient.invokeAPI(localVarPath, "PATCH", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Updates the user with the given username. This actions needs a complete user object.
   * 
   * @param name The username. (required)
   * @param body  (required)
   * @return User
   * @throws ApiException if fails to make API call
   */
  public User usersNamePut(String name, User body) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling usersNamePut");
    }
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling usersNamePut");
    }
    
    // create path and map variables
    String localVarPath = "/users/{name}"
      .replaceAll("\\{" + "name" + "\\}", apiClient.escapeString(name));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      APPLICATION_JSON
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<User> localVarReturnType = new GenericType<User>() {};
    return apiClient.invokeAPI(localVarPath, "PUT", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
  /**
   * Creates a new user.
   * 
   * @param body  (required)
   * @return User
   * @throws ApiException if fails to make API call
   */
  public User usersPost(User body) throws ApiException {
    Object localVarPostBody = body;
    
    // verify the required parameter 'body' is set
    if (body == null) {
      throw new ApiException(400, "Missing the required parameter 'body' when calling usersPost");
    }
    
    // create path and map variables
    String localVarPath = "/users";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<>();
    Map<String, String> localVarHeaderParams = new HashMap<>();
    Map<String, Object> localVarFormParams = new HashMap<>();
    
    final String[] localVarAccepts = {
      APPLICATION_JSON
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      APPLICATION_JSON
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

   String[] localVarAuthNames = new String[] { RestService.O_AUTH  };

    GenericType<User> localVarReturnType = new GenericType<User>() {};
    return apiClient.invokeAPI(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
      }
}
