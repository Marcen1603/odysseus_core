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

import java.util.Map;

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
		Preconditions.checkArgument(!Strings.isNullOrEmpty(imageID), "ImageID must be not null or empty!");
		
		if( !loadedImages.containsKey(imageID)) {
			// Load image
			String filename = imageIDs.get(imageID);
			if( filename == null )
				throw new IllegalArgumentException("ImageID " + imageID + " not registered ");
			
			ImageDescriptor img = ImageDescriptor.createFromURL(bundle.getEntry(filename));
			Image image = img.createImage();
			if( image == null ) 
				throw new IllegalArgumentException("Returned image with imageID=" + imageID + " is null!");
			
			loadedImages.put(imageID, image);
		}
		
		return loadedImages.get(imageID);
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
}
