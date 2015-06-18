package de.uniol.inf.is.odysseus.sensormanagement.application.view.scene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import de.uniol.inf.is.odysseus.sensormanagement.application.view.playback.PlaybackScene;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelper;
import de.uniol.inf.is.odysseus.sensormanagement.common.utilities.XmlMarshalHelperHandler;

@XmlRootElement(name = "scene")
@XmlAccessorType (XmlAccessType.FIELD)
public class Scene implements XmlMarshalHelperHandler
{
	@XmlTransient
	private String path;
	
	private final String name;
	private final String mapImage;
	
	@XmlElement(name = "serverInstance")		
	public final List<String> instanceFiles;	
	
	public Scene()
	{		
		this(null, null, null, null);
	}

	public Scene(String path, String name, String mapImage, List<String> instanceFiles)
	{		
		this.path = path;
		this.name = name;
		this.mapImage = mapImage;
		this.instanceFiles = instanceFiles == null ? new ArrayList<String>() : instanceFiles;
	}	
	
	public String getPath() {
		return path;
	}	

	public String getName() {
		return name;
	}

	public String getMapImage() {
		return mapImage;
	}

	public List<String> getInstanceFileList() {
		return instanceFiles;
	}		
	
	@Override
	public void onUnmarshalling(File xmlFile) 
	{
		path = xmlFile.getParent() + File.separator;
	}

	@Override
	public void onUnmarshalling(String xmlString) 
	{
	}
	
	public static Scene fromFile(File file) throws IOException
	{
		return (Scene) XmlMarshalHelper.fromXmlFile(file, new Class[]{Scene.class, PlaybackScene.class});
	}
}