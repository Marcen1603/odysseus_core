/**********************************************************************************
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.rcp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class ImageManager {

	private static final Logger LOG = LoggerFactory.getLogger(ImageManager.class);

	private final Bundle bundle;

	private Map<String, String> imageIDs = Maps.newHashMap();
	private Map<String, Image> loadedImages = Maps.newHashMap();

	public ImageManager( Bundle bundle ) {
		this.bundle = Preconditions.checkNotNull(bundle, "Bundle for ImageRegistry must not be null!");
	}

	public void register( String imageID, String fileName ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(imageID), "ImageID must be not null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(fileName), "Filename to register must not be null or empty!");

		if( imageIDs.containsKey(imageID)) {
			LOG.warn("Registering already registered imageID {}.", imageID);
		}

		imageIDs.put(imageID, fileName);
	}
	
	public void registerImageSet(String imageSetName, String imageSetFileName) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(imageSetName), "imageSetName must not be null or empty!");
		Preconditions.checkArgument(!Strings.isNullOrEmpty(imageSetFileName), "imageSetFileName must not be null or empty!");
		
		URL url = bundle.getEntry(imageSetFileName);
		registerImageSet(imageSetName, url);
	}
	
	public void registerImageSet(File file) {
		Preconditions.checkArgument(file.isFile(), "image set file " + file.getAbsolutePath() + " does not exist");
		
		URL url = null;
		try {
			url = file.toURI().toURL();
		} catch (MalformedURLException e) {
			LOG.error(e.getMessage());
		}

		if (url != null) {
			String iconSetName = file.getName();
			if (iconSetName.endsWith(".xml")) {
				iconSetName = iconSetName.substring(0, iconSetName.length() - 4);
			}
			registerImageSet(iconSetName, url);
		}
	}
	
	private void registerImageSet(String imageSetName, URL url){
		try(InputStream in = url.openConnection().getInputStream()) {
			Properties props = new Properties();
			props.loadFromXML(in);
			
			for (Object key : props.keySet()) {
				String imageID = (String)key;
				register(imageSetName + "." + imageID, props.getProperty(imageID));
			}
		} catch (FileNotFoundException e) {
			LOG.error("image set file not found: " + url.toString());
		} catch (IOException e) {
			LOG.error("Error while reading image set " + imageSetName + " (" + url.toString() + ")");
			LOG.error(e.toString());
		}
	}
	
	public void unregister( String imageID ) {
		if( imageIDs.containsKey(imageID)) {
			imageIDs.remove(imageID);
			if(isLoaded(imageID)) {
				dispose(imageID);
			}
		} else {
			LOG.warn("Unregister an imageID {} which was not registered before.", imageID);
		}
	}

	public ImmutableList<String> getRegisteredImageIDs() {
		return ImmutableList.copyOf(imageIDs.keySet());
	}

	public ImmutableList<String> getLoadedImageIDs() {
		return ImmutableList.copyOf(loadedImages.keySet());
	}

	public Image get( String imageID ) {

		if (Strings.isNullOrEmpty(imageID)){
			imageID = "broken";
		}

		if( !loadedImages.containsKey(imageID)) {
			// Load image
			ImageDescriptor img = getImageDescriptor(imageID);
			Image image = img.createImage();
			if( image == null )
				throw new IllegalArgumentException("Returned image with imageID=" + imageID + " is null!");

			loadedImages.put(imageID, image);
		}

		return loadedImages.get(imageID);
	}

	private ImageDescriptor getImageDescriptor(String imageID) {
		String filename = imageIDs.get(imageID);
		if( filename == null )
			throw new IllegalArgumentException("ImageID " + imageID + " not registered ");

		URL url = bundle.getEntry(filename);
		if (url == null) {
			try {
				url = new File(filename).toURI().toURL();
			} catch (MalformedURLException e) {
				LOG.error("image cannot be found: " + filename);
			}
		}
		return ImageDescriptor.createFromURL(url);
	}
	

	public ImageDescriptor getDescriptor(String imageID) {

		if (Strings.isNullOrEmpty(imageID)){
			imageID = "broken";
		}
		return getImageDescriptor(imageID);
	}
	
	public boolean isLoaded( String imageID ) {
		return loadedImages.containsKey(imageID);
	}

	public void disposeAll() {
		debugNotLoadedImages();

		for( Image image : loadedImages.values()) {
			image.dispose();
		}
		loadedImages.clear();
	}

	public void dispose( String imageID ) {
		Image img = loadedImages.get(imageID);
		if( img != null ) {
			img.dispose();
			loadedImages.remove(imageID);
		}
	}

	private void debugNotLoadedImages() {
		for( String imageID : imageIDs.keySet() ) {
			if( loadedImages.containsKey(imageID)) {
				LOG.debug("ImageID {} was never loaded.", imageID);
			}
		}
	}

	public boolean isRegistered(String imageID) {
		return imageIDs.containsKey(imageID);
	}
}
