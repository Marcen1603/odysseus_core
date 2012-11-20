package de.offis.gui.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
@RemoteServiceRelativePath("wiring")
public interface WiringEditorService extends RemoteService {

	public ScaiOperatorsData loadOperatorsData();

	public ScaiLoadedOperatorGroup loadOperatorGroup(String name);

	public void deployOperatorGroup(boolean simulate, String operatorGroup,
			List<InputModuleModel> sensors, List<OutputModuleModel> outputs,
			List<OperatorModuleModel> operators, List<ScaiLinkModel> links)
			throws Exception;

	public BackgroundImages getBackgroundImages();
	
	void removeOperatorGroup(String name) throws Exception;
}
