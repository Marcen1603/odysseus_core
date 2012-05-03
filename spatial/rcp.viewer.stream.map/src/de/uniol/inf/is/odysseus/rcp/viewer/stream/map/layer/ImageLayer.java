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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.MapTransformation;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.Style;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ImageLayer implements Layer {

	private MapTransformation transformation = null;
	private Image image = null;
	private String name = null;

	private boolean redraw = true;
	
	public ImageLayer(String name) {
		this.name = name;
	}

	public void drawImage(Image image, Point point, GC gc) {
		int[] uv = transformation.transformCoord(point.getCoordinate());
		gc.drawImage(image, uv[0], uv[1]);
	}

	@Override
	public void draw(GC gc) {
		if(redraw){
			image = createImage(gc);
			redraw = false;
		}
		gc.drawImage(image, 0, 0);
	}

	@Override
	public String getName() {
		return name;
	}

	private Image createImage(GC gc){
		Image image = null;
		try {
			URL imageUrl = new URL("http://wms.latlon.org/?layers=bing&width=1024&height=1024&x=2&y=1&z=2");
			InputStream in = imageUrl.openStream();
			image = new Image(gc.getDevice(), in);
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
}
