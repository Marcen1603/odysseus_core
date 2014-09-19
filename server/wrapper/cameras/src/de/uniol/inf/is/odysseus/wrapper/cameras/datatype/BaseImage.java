/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.wrapper.cameras.datatype;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Henrik Surm
 * 
 */
public class BaseImage implements IClone, Cloneable 
{
	private BufferedImage image;
	
	public static BaseImage createNewFrom(BufferedImage bufferedImage)
	{
		return new BaseImage(bufferedImage).clone();
	}
	
	public BaseImage(int width, int height, int colorModel) 
	{
		image = new BufferedImage(width, height, colorModel);
	}

	public BaseImage(BufferedImage bufferedImage) 
	{
		image = bufferedImage;
	}	
	
	public int getWidth() 	{ return image.getWidth();  }
	public int getHeight() 	{ return image.getHeight(); }

	public BufferedImage getImage() { return image; }
	
	public int get(int x, int y) { return image.getRGB(x, y); }

	public void set(int x, int y, int value) 
	{
		image.setRGB(x, y, value);
	}

/*	public int[][] get(int x1, int y1, int x2, int y2) 
	{
		int[][] value = new int[y2 - y1 + 1][x2 - x1 + 1];

		try 
		{
			for (int i = 0; i < value.length; i++) 
			{	
				System.arraycopy(this.buffer, (i + y1) * this.width + x1, value[i], 0, value[i].length);
			}
		}
		catch (Exception e) 
		{
				e.printStackTrace();
		}

		return value;
	}

	public void set(int x1, int y1, int x2, int y2, int value) 
	{ 
		int[] valueArray = new int[x2 - x1 + 1];
		Arrays.fill(valueArray, value);
		for (int i = y1; i <= y2; i++) 
		{
			System.arraycopy(valueArray, 0, this.buffer, i * this.width + x1, valueArray.length);
		}
	}

	public void setBuffer(int[] value) 
	{
		System.arraycopy(this.buffer, 0, value, 0, value.length);
	}

	public void fill(int value) 
	{
		Arrays.fill(buffer, value);
	}*/

	@Override public BaseImage clone() 
	{
		ColorModel cm = image.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = image.copyData(null);
		
		return new BaseImage(new BufferedImage(cm, raster, isAlphaPremultiplied, null));
	}

	@Override public String toString() 
	{
		return "{Width: " + getWidth() + " Height: " + getHeight() + "}";
	}
}
