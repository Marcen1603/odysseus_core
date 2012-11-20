package de.offis.scai;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import de.offis.client.DataElement;
import de.offis.client.DataStreamType;
import de.offis.client.Operator;
import de.offis.client.OperatorGroup;
import de.offis.client.OperatorType;
import de.offis.client.ScaiLink;
import de.offis.client.Sensor;
import de.offis.client.SensorType;

/**
 * GWT-RPC specific code.
 * 
 * @author Alexander Funk
 *
 */
public interface ScaiWebServiceAsync {
	public void listAllDataStreamTypes(AsyncCallback<List<DataStreamType>> callback);
	public void listAllSensors(AsyncCallback<List<Sensor>> callback);
	public void listAllSensorTypes(AsyncCallback<List<SensorType>> callback);
	public void listAllOperatorGroups(AsyncCallback<List<OperatorGroup>> callback);
	public void listAllOperatorTypes(AsyncCallback<List<OperatorType>> callback);
	public void listAllDataElements(AsyncCallback<List<DataElement>> callback);
	public void listAllOperators(String operatorGroup, AsyncCallback<List<Operator>> callback);
	public void listAllLinks(String operatorGroup, AsyncCallback<List<ScaiLink>> callback);
	
	public void removeOperatorGroup(String name, AsyncCallback<Void> callback);
	
	public void deployOperatorGroup(String operatorGroup, AsyncCallback<Void> callback);
	public void undeployOperatorGroup(String operatorGroup, AsyncCallback<Void> callback);
	
	public void getBatchedScaiData(AsyncCallback<Map<String, String[]>> callback);
	public void getScaiDataBatched(AsyncCallback<ScaiDataBatch> callback);
	
	/**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static ScaiWebServiceAsync instance;
        private static String serviceEntryPoint;
        
        public static ScaiWebServiceAsync getInstance(String serviceEntryPoint)
        {
            if ( instance == null || Util.serviceEntryPoint != serviceEntryPoint)
            {
            	Util.serviceEntryPoint = serviceEntryPoint;
                instance = (ScaiWebServiceAsync) GWT.create( ScaiWebService.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint(Util.serviceEntryPoint);
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
