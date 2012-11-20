package de.offis.scai;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.offis.client.DataStreamType;
import de.offis.client.OperatorType;
import de.offis.client.Sensor;
import de.offis.client.SensorType;

/**
 * Client-side access of RPC-Methods.
 * 
 * @author Alexander Funk
 *
 */
public class GwtScai {	
	public static final String SENSORS = "sensors";
	
	private String servletEndpoint = null;
	private ScaiDataBatch scaiBatch;
    public ScaiWebServiceAsync rpc = null;
	
    public GwtScai(String servletEndpoint) {
    	this.servletEndpoint = servletEndpoint;
		this.rpc = ScaiWebServiceAsync.Util.getInstance(servletEndpoint);
		this.update();
	}
    
	public static boolean checkDataStreamEquals(String[] source, String[] destination){				
		if(source.length != destination.length){
			return false;
		}
		
		for(int i = 0 ; i < source.length ; i++){
			if(!source[i].equals(destination[i])){
				return false;
			}
		}
		
		return true;
	}
	
	public List<Sensor> listAllSensors(){
    	return scaiBatch.getSensors();
    }

    public List<OperatorType> listAllOperatorTypes(){
    	return scaiBatch.getOperatorTypes();
    }
    
    public List<SensorType> listAllSensorTypes(){
    	return scaiBatch.getSensorTypes();
    }
    
    public List<DataStreamType> listAllDataStreamTypes(){
    	return scaiBatch.getDataStreamTypes();
    }
    
    public String[] getDataElementsFromSensorType(String sensorType){
    	if(scaiBatch == null)
            return new String[0];
    	
    	return scaiBatch.getDataElementsForSensorType(sensorType);
    }
    
    public List<String> getDataElementsFromSensorTypeKeys(){
    	return scaiBatch.getDataElementsFromSensorTypeKeys();
    }
    
    public String[] getSensorDomains(){
        if(scaiBatch == null)
            return new String[0];

        return scaiBatch.getSensorDomainsString();
    }

    public String[] getSensorTypes(){
        if(scaiBatch == null)
            return new String[0];

        return scaiBatch.getSensorTypesString();
    }
    
    public String[] getSensorTypesForDatastream(String[] datastream){
        if(scaiBatch == null)
            return null;

        List<String> result = new ArrayList<String>();
        
        for(String sensorType : scaiBatch.getSensorTypesString()){
        	String[] dataElementsSensorType = scaiBatch.getDataElementsForSensorType(sensorType);
        	
        	boolean equals = true;
        	int minLength = datastream.length < dataElementsSensorType.length ? datastream.length : dataElementsSensorType.length;
        	for(int i = 0 ; i < minLength ; i++){
        		if(!dataElementsSensorType[i].equals(datastream[i])){
        			equals = false;
        			break;
        		}
        	}
        	
        	if(equals){
        		result.add(sensorType);
        	}        	
        }
        
        return result.toArray(new String[0]);
    }

    public void update(){
    	update(null);
    }
    
    public void update(final Command onSuccess){
    	if(servletEndpoint == null)
    		return;        
        
    	rpc.getScaiDataBatched(new AsyncCallback<ScaiDataBatch>() {
			
			@Override
			public void onSuccess(ScaiDataBatch result) {
				GwtScai.this.scaiBatch = result;
				if(onSuccess != null){                    
                    onSuccess.execute();	
                }
			}
			
			@Override
			public void onFailure(Throwable caught) {
				GWT.log("ScaiClientDatabase: ", caught);		
			}
		});        
    }
    
    public ScaiWebServiceAsync getWebService(){
    	return this.rpc;
    }
    
    public void listAllSensors(final AsyncCallback<List<Sensor>> callback){
    	rpc.listAllSensors(new AsyncCallback<List<Sensor>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<Sensor> result) {
				// TODO ergebnis in dieser klasse speichern als cache
				callback.onSuccess(result);
			}
		});
    }

    public void listAllOperatorTypes(final AsyncCallback<List<OperatorType>> callback){
    	rpc.listAllOperatorTypes(new AsyncCallback<List<OperatorType>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<OperatorType> result) {
				// TODO ergebnis in dieser klasse speichern als cache
				callback.onSuccess(result);
			}
		});
    }
    
    public void listAllSensorTypes(final AsyncCallback<List<SensorType>> callback){
    	rpc.listAllSensorTypes(new AsyncCallback<List<SensorType>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<SensorType> result) {
				// TODO ergebnis in dieser klasse speichern als cache
				callback.onSuccess(result);
			}
		});
    }
    
    public void listAllDataStreamTypes(final AsyncCallback<List<DataStreamType>> callback){
    	rpc.listAllDataStreamTypes(new AsyncCallback<List<DataStreamType>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<DataStreamType> result) {
				// TODO ergebnis in dieser klasse speichern als cache
				callback.onSuccess(result);
			}
		});
    }
}
