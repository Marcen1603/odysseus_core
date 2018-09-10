/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.spatial.functions;


import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.spatial.utilities.Ellipsoid;

/**
 * @author Mazen Salous <mazen.salous@offis.de>
 */
public class ToEllipsoid extends AbstractFunction<Ellipsoid> {

	private static final long serialVersionUID = 7202373953195273323L;

	public ToEllipsoid() {
		super("ToEllipsoid", 1, accTypes, SDFSpatialDatatype.SPATIAL_POINT);
	}
	
	 public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{{SDFDatatype.STRING}};

	@Override
	public Ellipsoid getValue() {
		Ellipsoid ellipsoid =Ellipsoid.WGS84;
		try {
			Class<?> c = ellipsoid.getClass();
			java.lang.reflect.Field f = c.getDeclaredField(this.getInputValue(0).toString());
			f.setAccessible(true);
			return (Ellipsoid) f.get(ellipsoid);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return ellipsoid;
	 }
}
