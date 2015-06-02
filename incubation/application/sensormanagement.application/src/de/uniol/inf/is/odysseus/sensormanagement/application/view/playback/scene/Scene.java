package de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.scene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelperHandler;

@XmlRootElement(name = "view")
@XmlAccessorType (XmlAccessType.FIELD)
class View
{
	double startTime;
	double endTime;		
	
	public View()
	{
		startTime = -1.0;
		endTime = -1.0;
	}
	
	public View(double startTime, double endTime) 
	{
		this.startTime = startTime;
		this.endTime = endTime;
	}
}

@XmlRootElement(name = "scene")
@XmlAccessorType (XmlAccessType.FIELD)
public class Scene implements XmlMarshalHelperHandler
{
	@XmlTransient
	private String path;
	
	private final String name;
	
	@XmlElement(name = "interval") 	
	private final List<TimeInterval> intervalList;
	
	@XmlElement(name = "file")		
	private final List<String> fileList;

	@XmlElement(name = "view") 	
	private final List<View> viewList;	
	
	public Scene()
	{		
		this("","",null,null,null);
	}

	public Scene(String path, String name, List<TimeInterval> intervalList, List<String> fileList, List<View> viewList)
	{		
		this.path = path;
		this.name = name;
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

	public String getPath() {
		return path;
	}	

	public String getName() {
		return name;
	}

	@Override
	public void onUnmarshalling(File xmlFile) 
	{
		path = xmlFile.getParent() + File.separator;
		checkIntervals();
	}

	@Override
	public void onUnmarshalling(String xmlString) 
	{
	}
	
	public static Scene fromFile(File file) throws IOException
	{
		return XmlMarshalHelper.fromXmlFile(file, Scene.class);
	}
	
	public static Scene fromDirectory(String path, String sceneName)
	{
		File dir = new File(path);
		File[] directoryListing = dir.listFiles();
		if (directoryListing == null) return null; 
		
		List<String> fileList = new ArrayList<>();
		for (File child : directoryListing) 
		{
			String videoFileName = child.getName();
			if (!child.isFile()) 
				continue;
			
			if (!videoFileName.endsWith("cfg"))
				continue;
			
			fileList.add(child.getName());
		}
		
		return new Scene(path, sceneName, null, fileList, null);
	}
	
}

// MapVisualization Scenario BHV
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