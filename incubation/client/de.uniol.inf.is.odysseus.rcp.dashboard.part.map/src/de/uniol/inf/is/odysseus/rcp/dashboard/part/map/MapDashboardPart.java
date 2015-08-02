package de.uniol.inf.is.odysseus.rcp.dashboard.part.map;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerConfiguration;

public class MapDashboardPart extends AbstractDashboardPart {
	
	private int maxData = 10;
	
	private int updateInterval = 1000;
	
	private int layerCounter = 0;
	
	private HashMap<Integer,LayerConfiguration> layerMap = new HashMap<Integer,LayerConfiguration>();

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		// TODO Auto-generated method stub
		
	}
	
	public int getMaxData() {
		return maxData;
	}
	
	public void setMaxData(int maxData){
		this.maxData = maxData;
	}
	
	public int getUpdateInterval() {
		return updateInterval;
	}
	
	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public int getLayerCounter(){
		return this.layerCounter;
	}
	public HashMap<Integer, LayerConfiguration> getLayerList(){
		return layerMap;
	}
	
	public void addLayerToMap(Integer id, LayerConfiguration layerToAdd){
		this.layerMap.put(id, layerToAdd);
	}
	
	public void deleteLayerFromMap(Integer id){
		//this.layerMap.containsKey(id);
		this.layerMap.remove(id);
		layerCounter--;
	}
	
	public void changeLayerOrder(Integer firstId, Integer secondId){
		
	}
	
	public void increaseLayerCounter(){
		this.layerCounter++;
	}
	
	
	
	
}
