package de.offis.gui.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.offis.gui.client.util.BackgroundImageUtil.BackgroundImages;
import de.offis.gui.shared.InputModuleModel;
import de.offis.gui.shared.OperatorModuleModel;
import de.offis.gui.shared.OutputModuleModel;
import de.offis.gui.shared.ScaiLinkModel;
import de.offis.gui.shared.ScaiLoadedOperatorGroup;
import de.offis.gui.shared.ScaiOperatorsData;

/**
 * GWT-RPC specific.
 *
 * @author Alexander Funk
 * 
 */
public interface WiringEditorServiceAsync {

	void loadOperatorsData(AsyncCallback<ScaiOperatorsData> callback);

	void loadOperatorGroup(String name,
			AsyncCallback<ScaiLoadedOperatorGroup> callback);

	void getBackgroundImages(AsyncCallback<BackgroundImages> callback);

	void deployOperatorGroup(boolean simulate, String operatorGroup,
			List<InputModuleModel> sensors, List<OutputModuleModel> outputs,
			List<OperatorModuleModel> operators, List<ScaiLinkModel> links,
			AsyncCallback<Void> asyncCallback);

	void removeOperatorGroup(String name, AsyncCallback<Void> callback);
	
	
}
