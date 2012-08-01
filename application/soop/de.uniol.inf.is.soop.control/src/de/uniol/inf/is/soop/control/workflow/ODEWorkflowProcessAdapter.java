/**
 * 
 */
package de.uniol.inf.is.soop.control.workflow;

import java.rmi.RemoteException;

import org.example.www.WorkflowCallMessages.GotoNextRequest;
import org.example.www.WorkflowCallMessages.GotoNextResponse;
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
public class ODEWorkflowProcessAdapter extends AbstractWorkflowProcessAdapter implements IWorkflowProcessAdapter {

	
	private ProcessClientPortTypeProxy proxy;
	
	private ProcessClientPortType port;
	
	public ODEWorkflowProcessAdapter(String endpoint){
		
		proxy = new ProcessClientPortTypeProxy(endpoint);
		
		port = proxy.getSoopMissionsStepOverStepOverProcessClientPortType();
		
	}
	
	public WorkflowProcessInstance startProcess(WorkflowEngine wfe, DatastreamManagementSystem dsms, SensorRegistry sr){
		
		
		StartMissionRequest req = new StartMissionRequest(wfe.getUsername(), wfe.getPassword(), dsms.getWebserviceUrl(), sr.getWebserviceUrl());
		StartMissionResponse res = null;
		
		try {
			res = port.startMission(req);
			
			if (res instanceof StartMissionResponse && res.getSuccsessful().equals("true")){
				return new WorkflowProcessInstance(res.getInstanceId());
			} else {
				System.out.println("StartMission failed: "  + res.toString());
				
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
public String forwardButtonPressed(String instanceId){
		
		
		GotoNextRequest req = new GotoNextRequest(instanceId);
		GotoNextResponse res = null;
		
		try {
			res = port.Signal__leave_step(req);
			
			
			if (res instanceof GotoNextResponse && res.getSuccsessful().equals("true")){
				return res.getSuccsessful();
			} else {
				System.out.println("GotoNext failed: "  + res.toString());
				
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * @return the proxy
	 */
	public ProcessClientPortTypeProxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(ProcessClientPortTypeProxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the port
	 */
	public ProcessClientPortType getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(ProcessClientPortType port) {
		this.port = port;
	}

	@Override
	public String Signal__leave_step(String instanceId) {
		// same action
		return forwardButtonPressed(instanceId);
	}
	
	
	
}
