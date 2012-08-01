/**
 * 
 */
package de.uniol.inf.is.soop.control.workflow;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.www.ode.pmapi.ProcessManagementPortType;
import org.apache.www.ode.pmapi.ProcessManagementPortTypeProxy;
import org.example.www.WorkflowCallMessages.StartMissionRequest;
import org.example.www.WorkflowCallMessages.StartMissionResponse;

import de.uniol.inf.is.soop.control.dsms.DatastreamManagementSystem;
import de.uniol.inf.is.soop.control.sensorregistry.SensorRegistry;
import de.uniol.inf.soop.control.processClient.ProcessClientPortType;
import de.uniol.inf.soop.control.processClient.ProcessClientPortTypeProxy;

/**
 * @author jbrode
 *
 */
public class ODEWorkflowEngineAdapter extends AbstractWorkflowEngineAdapter implements IWorkflowEngineAdapter {

	
	private ProcessManagementPortTypeProxy proxy;
	
	private ProcessManagementPortType port;
	
	public ODEWorkflowEngineAdapter(String endpoint){
		
		proxy = new ProcessManagementPortTypeProxy(endpoint);
		
		port = proxy.getProcessManagementPortType();
		
	}
	
	public String startProcess(WorkflowEngine wfe, DatastreamManagementSystem dsms, SensorRegistry sr){
		
		ProcessClientPortTypeProxy pproxy = new ProcessClientPortTypeProxy();
		ProcessClientPortType pport = pproxy.getSoopMissionsStepOverStepOverProcessClientPortType();
		
		StartMissionRequest req = new StartMissionRequest(wfe.getUsername(),wfe.getPassword(), dsms.getWebserviceUrl(), sr.getWebserviceUrl());
		StartMissionResponse res = null;
		
		try {
			res = pport.startMission(req);
			
			if (res instanceof StartMissionResponse && res.getSuccsessful().equals("true")){
				return res.getInstanceId();
			} else {
				System.out.println("StartMission failed: "  + res.toString());
				
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public HashMap<String, WorkflowProcess> listProcesses(){
		
		HashMap<String, WorkflowProcess> activeProcesses = new HashMap<String, WorkflowProcess>();

		/* TProcessInfo[] processList = null;
		
		try {
			processList = port.listProcesses(null, null);
		} catch (ManagementFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		WorkflowProcess pi;
		String endpointUrl;

		/* for (TProcessInfo p : processList) {

			//filter retired processes
			if (p.getStatus().getValue() == "ACTIVE") {
				
				// parse url from tree
				try {
				
					endpointUrl = ((Text) ((MessageElement) ((MessageElement) p.getEndpoints()[0].get_any()[0].getChildren().get(0)).getChildren().get(1)).getChildren().get(0)).getValue();
				
				} catch (ArrayIndexOutOfBoundsException e){
					endpointUrl = "not parsable";
				}
				
				// ProcessInfo does not contain the needed endpoint, http://localhost:8080/ode/deployment/services/ has it, but no API found
				endpointUrl = "http://localhost:8080/ode/processes/SoopMissions/StepOver/StepOverProcess/";
				
				pi = new WorkflowProcess(p.getPid(), endpointUrl, p.getDefinitionInfo().getProcessName().getLocalPart());
				pi.setAdapter(new ODEWorkflowProcessAdapter(endpointUrl));
				activeProcesses.put(p.getPid(), pi);
			}
		}*/
		
		// ProcessInfo does not contain the needed endpoint, http://localhost:8080/ode/deployment/services/ has it, but no API found
		endpointUrl = "http://localhost:8080/ode/processes/SoopMissions/StepOver/StepOverProcess/Client";
		
		pi = new WorkflowProcess("stepOverProcess", endpointUrl, "Übersteige-Prozess");
		pi.setAdapter(new ODEWorkflowProcessAdapter(endpointUrl));
		activeProcesses.put("stepOverProcess", pi);
		
		System.out.println("REGISTRIERTE PROZESSE: " + activeProcesses.size());
		
		return activeProcesses;
	}

	/**
	 * @return the proxy
	 */
	public ProcessManagementPortTypeProxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(ProcessManagementPortTypeProxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the port
	 */
	public ProcessManagementPortType getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(ProcessManagementPortType port) {
		this.port = port;
	}
	
	
	
}
