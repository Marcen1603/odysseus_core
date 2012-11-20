package de.offis.gui.client.rpc;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import de.offis.client.OperatorGroup;
import de.offis.gui.client.Operators;
import de.offis.gui.client.util.BackgroundImageUtil.BackgroundImages;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gui.shared.ScaiLinkModel;
import de.offis.gui.shared.ScaiLoadedOperatorGroup;
import de.offis.gui.shared.ScaiOperatorsData;

/**
 * Wrapper for RPC-Calls to show a loading screen.
 *
 * @author Alexander Funk
 * 
 */
public class OperatorsServiceProxy {
	private static OperatorsServiceProxy instance = new OperatorsServiceProxy();
	private static WiringEditorServiceAsync async = null;
	
	public static OperatorsServiceProxy get(){
		if(instance == null){
			instance = new OperatorsServiceProxy();
		}
		
		if(async == null){
			async = (WiringEditorServiceAsync) GWT.create(WiringEditorService.class);
			ServiceDefTarget target = (ServiceDefTarget) async;
			target.setServiceEntryPoint(GWT.getModuleBaseURL() + "wiring");
		}
				
		return instance;
	}
		
	public void loadScaiOperatorsData(AsyncCallback<ScaiOperatorsData> callback) {
		async.loadOperatorsData(new ProxyCallbackHelper<ScaiOperatorsData>(callback, "Updating Sensors, Operators and Outputs ... "));
	}

	public void loadOperatorGroup(String name,	AsyncCallback<ScaiLoadedOperatorGroup> callback) {
		async.loadOperatorGroup(name, new ProxyCallbackHelper<ScaiLoadedOperatorGroup>(callback, "Loading OperatorGroup ..."));
	}

	public void getBackgroundImages(AsyncCallback<BackgroundImages> callback) {
		async.getBackgroundImages(new ProxyCallbackHelper<BackgroundImages>(callback, "Loading Background Images ..."));
	}
	
	public void removeOperatorGroup(String name, AsyncCallback<Void> callback){
		async.removeOperatorGroup(name, new ProxyCallbackHelper<Void>(callback, "Removing OperatorGroup ... "));
	}

	public void deployOperatorGroup(boolean simulate, String operatorGroup,
			List<InputModuleModel> sensors, List<OutputModuleModel> outputs,
			List<OperatorModuleModel> operators, List<ScaiLinkModel> links,
			AsyncCallback<Void> callback) {
		async.deployOperatorGroup(simulate, operatorGroup, sensors, outputs, operators, links, new ProxyCallbackHelper<Void>(callback, "Saving OperatorGroup ..."));
	}

	public void listAllOperatorGroups(AsyncCallback<List<OperatorGroup>> callback) {
		Operators.cache.getWebService().listAllOperatorGroups(new ProxyCallbackHelper<List<OperatorGroup>>(callback, "Updating OperatorGroups ..."));
	}

	public void undeployOperatorGroup(String name, AsyncCallback<Void> callback) {
		Operators.cache.getWebService().undeployOperatorGroup(name, new ProxyCallbackHelper<Void>(callback, "Stopping OperatorGroup ... "));
	}

	public void deployOperatorGroup(String name, AsyncCallback<Void> callback) {
		Operators.cache.getWebService().deployOperatorGroup(name, new ProxyCallbackHelper<Void>(callback, "Starting OperatorGroup ... "));
	}
	
	private class ProxyCallbackHelper<T> implements AsyncCallback<T> {

		private AsyncCallback<T> cb;
		private String text;
		
		public ProxyCallbackHelper(AsyncCallback<T> cb, String text) {
			this.cb = cb;
			this.text = text;
			Operators.showLoadingPanel(text);
		}
		
		@Override
		public void onFailure(Throwable caught) {
			Operators.hideLoadingPanel(text);
			
			cb.onFailure(caught);
		}

		@Override
		public void onSuccess(T result) {
			Operators.hideLoadingPanel(text);

			cb.onSuccess(result);
		}
	}
}
