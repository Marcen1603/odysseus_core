package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.scene.Scene;

@XmlRootElement(name = "playbackscene")
@XmlAccessorType (XmlAccessType.FIELD)
public class PlaybackScene extends Scene
{
	@XmlElement(name = "interval") 	
	private List<TimeInterval> intervalList;
	
	@XmlElement(name = "view") 	
	private List<View> viewList;	
	
	public PlaybackScene()
	{		
		this("","",null,null,null);
	}

	public PlaybackScene(String path, String name, String mapImage, List<TimeInterval> intervalList, List<View> viewList)
	{		
		super(path, name, mapImage, null);
		
		this.intervalList = intervalList != null ? intervalList : new ArrayList<TimeInterval>();
		this.viewList = viewList != null ? viewList : new ArrayList<View>();
		
		checkIntervals();
	}	
	
	private void checkIntervals() 
	{
		// TODO: Check that intervals don't overlap
	}

	public List<TimeInterval> getTimeIntervalList() 
	{
		return intervalList;
	}
	
	@Override
	public void onUnmarshalling(File xmlFile) 
	{
		super.onUnmarshalling(xmlFile);
		checkIntervals();
	}

	public static Scene fromInstanceFile(File instanceFile, String sceneName)
	{
		ArrayList<String> instanceFiles = new ArrayList<>();
		instanceFiles.add(instanceFile.getName());
		
		return new Scene(instanceFile.getParent() + File.separator, sceneName, null, instanceFiles);
	}
	
}

// MapVisualization Scenario BHV
// MapImage = res/map_port.png
/*    			case "192.168.1.30":	// Handy
{
	x = mapX + 188*scale;
	y = mapY + 487*scale;
	rot = Math.toRadians(10.0);
	angle = 60;
	radius = 40.0 * scale;
	break;    				
}

case "192.168.1.31":	// IR
{
	x = mapX + 780*scale;
	y = mapY + 290*scale;
	rot = Math.toRadians(150.0);
	angle = 70;
	radius = 40.0 * scale;
	break;
}

case "192.168.1.79":	// Basler AWI
{
	x = mapX + 780*scale;
	y = mapY + 290*scale;
	rot = Math.toRadians(140.0);
	angle = 90;
	radius = 80.0 * scale;
	break;
}    			    			

case "192.168.1.78":	// Basler Mole
{
	x = mapX + 178*scale;
	y = mapY + 490*scale;
	rot = Math.toRadians(65.0);
	radius = 40.0 * scale;
	angle = 58;
	break;
}*/

/*
 Constraint Strings from PlaybackSensor 
if (getSensorInfo().getEthernetAddr().equals("192.168.1.30")) { constraintString = "cell 3 0"; }
if (getSensorInfo().getEthernetAddr().equals("192.168.1.31")) { constraintString = "cell 3 1"; }
if (getSensorInfo().getEthernetAddr().equals("192.168.1.78")) { constraintString = "cell 2 0 1 2"; }
if (getSensorInfo().getEthernetAddr().equals("192.168.1.79")) { constraintString = "cell 0 2 4 1"; }
*/