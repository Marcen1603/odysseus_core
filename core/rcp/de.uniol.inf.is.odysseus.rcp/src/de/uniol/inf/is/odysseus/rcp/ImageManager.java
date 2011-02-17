/** Copyright [2011] [The Odysseus Team]
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class ImageManager {

	private static ImageManager instance = null;
	
	private Map<String, String> imageIDs;
	private Map<String, Image> loadedImages;
	
	private ImageManager() {
		
	}
	
	public static ImageManager getInstance() {
		if( instance == null ) 
			instance = new ImageManager();
		return instance;
	}
	
	public void register( String imageID, String fileName ) {
		Assert.isNotNull(imageID, "imageID");
		Assert.isNotNull(fileName, "fileName");
		Assert.isLegal(!getImageIDs().containsKey(imageID), "imageID "+ imageID + " already registered");
		
		getImageIDs().put(imageID, fileName);
	}
	
	public Image get( String imageID ) {
		Assert.isNotNull(imageID, "imageID");
		
		if( !getLoadedImages().containsKey(imageID)) {
			// Load image
			String filename = getImageIDs().get(imageID);
			if( filename == null )
				throw new IllegalArgumentException("imageID " + imageID + " not registered ");
			
			ImageDescriptor img = ImageDescriptor.createFromURL(OdysseusRCPPlugIn.getDefault().getBundle().getEntry(filename));
			Image image = img.createImage();
			if( image == null ) 
				throw new IllegalArgumentException("image is null (imageID=" + imageID + ")");
			
			getLoadedImages().put(imageID, image);
		}
		
		return getLoadedImages().get(imageID);
	}
	
	private Map<String, String> getImageIDs() {
		if( imageIDs == null )
			imageIDs = new HashMap<String, String>();
		return imageIDs;
	}
	
	private Map<String, Image> getLoadedImages() {
		if( loadedImages == null ) 
			loadedImages = new HashMap<String, Image>();
		return loadedImages;
	}
}
