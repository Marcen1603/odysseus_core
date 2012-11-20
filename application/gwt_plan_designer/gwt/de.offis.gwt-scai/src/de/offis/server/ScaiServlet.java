package de.offis.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.offis.client.DataElement;
import de.offis.client.DataStreamType;
import de.offis.client.Operator;
import de.offis.client.OperatorGroup;
import de.offis.client.OperatorType;
import de.offis.client.ScaiLink;
import de.offis.client.Sensor;
import de.offis.client.SensorDomain;
import de.offis.client.SensorType;
import de.offis.scai.ScaiDataBatch;
import de.offis.scai.ScaiWebService;
import de.offis.scaiconnector.factory.ScaiCommand;
import de.offis.scaiconnector.factory.ScaiFactory;

/**
 * Server-side implementation of RPC-Methods.
 * 
 * @author Alexander Funk
 *
 */
@SuppressWarnings("serial")
public class ScaiServlet extends RemoteServiceServlet implements ScaiWebService {

	protected String scai_server;
	protected String scai_user;
	protected String scai_pass;
	protected String scai_blacklist;
	protected String scai_whitelist;
	protected ScaiFactory scai;
	
	protected String odysseus_wsdllocation;
	protected String odysseus_servicenamespace;
	protected String odysseus_service;

    @Override
    public void init() throws ServletException {
        scai_server = getServletConfig().getInitParameter("SCAI_SERVER");
        scai_user = getServletConfig().getInitParameter("SCAI_USER");
        scai_pass = getServletConfig().getInitParameter("SCAI_PASS");
        scai_blacklist = getServletConfig().getInitParameter("SCAI_OPERATORS_BLACKLIST");
        scai_whitelist = getServletConfig().getInitParameter("SCAI_OPERATORS_WHITELIST");
        
        odysseus_wsdllocation = getServletConfig().getInitParameter("ODYSSEUS_WSDLLOCATION");
        odysseus_servicenamespace = getServletConfig().getInitParameter("ODYSSEUS_SERVICENAMESPACE");
        odysseus_service = getServletConfig().getInitParameter("ODYSSEUS_SERVICE");
        
        scai = new ScaiFactory(scai_server, scai_user, scai_pass);
    }
	
	@Override
	public List<Sensor> listAllSensors() {		
		return scai.ListAll.Sensor();
	}

	@Override
	public List<OperatorGroup> listAllOperatorGroups() {
		return scai.ListAll.OperatorGroup();
	}

	@Override
	public List<OperatorType> listAllOperatorTypes() {
		return scai.ListAll.OperatorType();
	}

	@Override
	public List<DataStreamType> listAllDataStreamTypes() {
		return scai.ListAll.DataStreamType();
	}

	@Override
	public List<SensorType> listAllSensorTypes() {
		return scai.ListAll.SensorType();
	}
	
	@Override
	public List<DataElement> listAllDataElements() {
		return scai.ListAll.DataElement();
	}

	@Override
	public List<Operator> listAllOperators(String operatorGroup) {
		return scai.ListAll.Operators(operatorGroup);
	}

	@Override
	public List<ScaiLink> listAllLinks(String operatorGroup) {
		return scai.ListAll.Links(operatorGroup);
	}
	
	@Override
	public Map<String, String[]> getBatchedScaiData() {
		Map<String, String[]> data = new HashMap<String, String[]>();
		
    	List<SensorDomain> scaiSensorDomains = scai.ListAll.SensorDomain();
    	String[] sensorDomains = new String[scaiSensorDomains.size()];
    	for (int i = 0 ; i < sensorDomains.length ; i++) {    		
            sensorDomains[i] = scaiSensorDomains.get(i).getName();
        }
    	
    	List<DataStreamType> scaiDataStreamTypes = scai.ListAll.DataStreamType();
    	Map<String, DataStreamType> scaiDataStreamTypesMap = new HashMap<String, DataStreamType>();
    	for(DataStreamType dst : scaiDataStreamTypes){
    		scaiDataStreamTypesMap.put(dst.getName(), dst);
    	}
    	
    	List<SensorType> scaiSensorTypes = scai.ListAll.SensorType();
    	String[] sensorTypes = new String[scaiSensorTypes.size()];
    	for (int i = 0 ; i < sensorTypes.length ; i++) {    		
    		sensorTypes[i] = scaiSensorTypes.get(i).getName();
    		
    		String[] dataElemsFor = scaiDataStreamTypesMap.get(scaiSensorTypes.get(i).getDataStreamTypeName()).getDataElementNames().toArray(new String[0]);
    		data.put("dataElements for " + sensorTypes[i], dataElemsFor);
        }
        
        data.put("sensorDomains", sensorDomains);
        data.put("sensorTypes", sensorTypes);
        
        return data;
    }

	@Override
	public void deployOperatorGroup(String operatorGroup) throws Exception {
		scai.Misc.deployOperatorGroup(operatorGroup);
	}

	@Override
	public void undeployOperatorGroup(String operatorGroup) throws Exception {
		scai.Misc.undeployOperatorGroup(operatorGroup);
	}

	@Override
	public void removeOperatorGroup(String name) throws Exception {
		ScaiCommand cmd = scai.createScaiCommand();
		cmd.Builder.addRemoveOperatorGroup(name, "OpId");
		
		cmd.commit();
	}

	@Override
	public ScaiDataBatch getScaiDataBatched() {
			
		Map<String, String[]> dataElementsForSensorType = new HashMap<String, String[]>();
		
		List<SensorDomain> scaiSensorDomains = scai.ListAll.SensorDomain();
    	String[] sensorDomains = new String[scaiSensorDomains.size()];
    	for (int i = 0 ; i < sensorDomains.length ; i++) {    		
            sensorDomains[i] = scaiSensorDomains.get(i).getName();
        }
		
    	List<DataStreamType> scaiDataStreamTypes = scai.ListAll.DataStreamType();
    	Map<String, DataStreamType> scaiDataStreamTypesMap = new HashMap<String, DataStreamType>();
    	for(DataStreamType dst : scaiDataStreamTypes){
    		scaiDataStreamTypesMap.put(dst.getName(), dst);
    	}
    	
    	List<SensorType> scaiSensorTypes = scai.ListAll.SensorType();
    	String[] sensorTypes = new String[scaiSensorTypes.size()];
    	for (int i = 0 ; i < sensorTypes.length ; i++) {    		
    		sensorTypes[i] = scaiSensorTypes.get(i).getName();
    		
    		String[] dataElemsFor = scaiDataStreamTypesMap.get(scaiSensorTypes.get(i).getDataStreamTypeName()).getDataElementNames().toArray(new String[0]);
    		dataElementsForSensorType.put(sensorTypes[i], dataElemsFor);
        }
    	
		return new ScaiDataBatch(sensorDomains, sensorTypes, dataElementsForSensorType, scai.ListAll.Sensor(), scaiDataStreamTypes, scaiSensorTypes, scai.ListAll.OperatorType());
	}
}
