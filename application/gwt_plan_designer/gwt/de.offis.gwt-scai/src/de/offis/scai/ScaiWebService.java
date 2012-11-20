package de.offis.scai;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

import de.offis.client.DataElement;
import de.offis.client.DataStreamType;
import de.offis.client.Operator;
import de.offis.client.OperatorGroup;
import de.offis.client.OperatorType;
import de.offis.client.ScaiException;
import de.offis.client.ScaiLink;
import de.offis.client.Sensor;
import de.offis.client.SensorType;

/**
 * GWT-RPC specific code.
 * 
 * @author Alexander Funk
 *
 */
public interface ScaiWebService extends RemoteService{
	public List<DataStreamType> listAllDataStreamTypes();
	public List<Sensor> listAllSensors();
	public List<SensorType> listAllSensorTypes();
	public List<OperatorGroup> listAllOperatorGroups();
	public List<OperatorType> listAllOperatorTypes();
	public List<DataElement> listAllDataElements();
	public List<Operator> listAllOperators(String operatorGroup);
	public List<ScaiLink> listAllLinks(String operatorGroup);
	
	public void removeOperatorGroup(String name) throws Exception;
	
	public void deployOperatorGroup(String operatorGroup) throws ScaiException, Exception;
	public void undeployOperatorGroup(String operatorGroup) throws ScaiException, Exception;
	
	public Map<String, String[]> getBatchedScaiData();
	public ScaiDataBatch getScaiDataBatched();
}
