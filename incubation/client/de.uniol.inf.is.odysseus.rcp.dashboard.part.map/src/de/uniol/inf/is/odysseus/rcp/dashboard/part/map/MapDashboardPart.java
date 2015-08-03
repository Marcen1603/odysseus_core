package de.uniol.inf.is.odysseus.rcp.dashboard.part.map;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer.LayerConfiguration;

public class MapDashboardPart extends AbstractDashboardPart {
	
	private int maxData = 10;
	
	private int updateInterval = 1000;
	
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
	
	public HashMap<Integer, LayerConfiguration> getLayerList(){
		return layerMap;
	}
	
	public void addLayerToMap(Integer id, LayerConfiguration layerToAdd){
		this.layerMap.put(id, layerToAdd);
	}
	
	public void deleteLayerFromMap(Integer id){
		//this.layerMap.containsKey(id);
		this.layerMap.remove(id);
	}
	
	public void changeLayerOrder(Integer firstId, Integer secondId){

	}
	
	
	
}
