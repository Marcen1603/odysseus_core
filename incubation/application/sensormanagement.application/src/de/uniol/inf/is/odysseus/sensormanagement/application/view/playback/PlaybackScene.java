package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.scene.Scene;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.scene.TimeInterval;
import de.uniol.inf.is.odysseus.sensormanagement.application.view.scene.View;

@XmlRootElement(name = "playbackscene")
@XmlAccessorType (XmlAccessType.FIELD)
public class PlaybackScene extends Scene
{
	@XmlElement(name = "interval") 	
	private final List<TimeInterval> intervalList;
	
	@XmlElement(name = "file")		
	private final List<String> fileList;

	@XmlElement(name = "view") 	
	private final List<View> viewList;	
	
	public PlaybackScene()
	{		
		this("","",null,null,null,null);
	}

	public PlaybackScene(String path, String name, String mapImage, List<TimeInterval> intervalList, List<String> fileList, List<View> viewList)
	{		
		super(path, name, mapImage);
		
		this.intervalList = intervalList != null ? intervalList : new ArrayList<TimeInterval>();
		this.fileList = fileList != null ? fileList : new ArrayList<String>();
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
	
	public List<String> getFileList() 
	{
		return fileList;
	}

	@Override
	public void onUnmarshalling(File xmlFile) 
	{
		super.onUnmarshalling(xmlFile);
		checkIntervals();
	}

	public static PlaybackScene fromDirectory(String path, String sceneName)
	{
		if (!path.endsWith(File.separator))
			path += File.separator;
		
		File dir = new File(path);
		File[] directoryListing = dir.listFiles();
		if (directoryListing == null) return null; 
		
		List<String> fileList = new ArrayList<>();
		for (File child : directoryListing) 
		{
			String videoFileName = child.getName();
			if (!child.isFile()) 
				continue;
			
			if (videoFileName.endsWith("cfg"))
				fileList.add(child.getName());
		}
		
		return new PlaybackScene(path, sceneName, null, null, fileList, null);
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