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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.map;

import java.util.TreeMap;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.Proj4jException;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Stephan Jansen
 * @author Kai Pancratz
 * 
 */
public class ScreenTransformation {

	private ScreenManager screenManager;

	CRSFactory csFactory = new CRSFactory();
	CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
	TreeMap<String, CoordinateTransform> transformerRegistry = new TreeMap<String, CoordinateTransform>();

	private CoordinateTransform getCoordinateTransform(int sourceSrid) {
		int srid = screenManager.getSRID();
		return this.getCoordinateTransform(sourceSrid, srid);
	}

	public CoordinateTransform getCoordinateTransform(int sourceSrid, int destSrid) {
		CoordinateTransform trans = transformerRegistry.get(sourceSrid + " " + destSrid);
		if (sourceSrid == 0 || destSrid == 0)
			return new CoordinateTransform() {

				@Override
				public ProjCoordinate transform(ProjCoordinate src, ProjCoordinate tgt) throws Proj4jException {
					tgt.x = src.x;
					tgt.y = src.y;
					tgt.z = src.z;
					return tgt;
				}

				@Override
				public CoordinateReferenceSystem getTargetCRS() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public CoordinateReferenceSystem getSourceCRS() {
					// TODO Auto-generated method stub
					return null;
				}
			};
		if (trans == null) {
			/*
			 * Create {@link CoordinateReferenceSystem} &
			 * CoordinateTransformation. Normally this would be carried out once
			 * and reused for all transformations
			 */
			CoordinateReferenceSystem crs1 = csFactory.createFromName("EPSG:" + sourceSrid);
			CoordinateReferenceSystem crs2 = csFactory.createFromName("EPSG:" + destSrid);

			trans = ctFactory.createTransform(crs1, crs2);
			transformerRegistry.put(sourceSrid + " " + destSrid, trans);
		}
		return trans;
	}

	public int[] transformCoord(Coordinate coordinate, int srid, boolean latitudeFirst) {
		int[] transformedCoordinate = new int[2];

		@SuppressWarnings("unused")
		int x = 0;
		@SuppressWarnings("unused")
		int y = 0;
		long start = 0;
		long end = 0;
		// latitude = y axis on the earth
		ProjCoordinate src;
		ProjCoordinate trgt;
		if (latitudeFirst) {
			src = new ProjCoordinate(coordinate.y, coordinate.x);
			trgt = new ProjCoordinate(coordinate.y, coordinate.x);
		} else {
			src = new ProjCoordinate(coordinate.x, coordinate.y);
			trgt = new ProjCoordinate(coordinate.x, coordinate.y);
		}

		if (srid != screenManager.getSRID()) {
			start = System.currentTimeMillis();
			getCoordinateTransform(srid).transform(src, trgt);
			end = System.currentTimeMillis();
		}
		@SuppressWarnings("unused")
		long dur = end - start;
		// if(srid != 0){
		// x = ProjectionUtil.lon2position(coordinate.x,
		// screenManager.getEnvelope().x);
		// y = ProjectionUtil.lat2position(coordinate.y,
		// screenManager.getEnvelope().y);
		//
		// }
		double scale = screenManager.getScale();
		int[] offset = screenManager.getOffset();
		Coordinate centerUV = screenManager.getCenterUV();
		@SuppressWarnings("unused")
		double d = (trgt.y / scale);
		@SuppressWarnings("unused")
		int xx = (int) Math.floor(centerUV.x + (trgt.x / scale));
		@SuppressWarnings("unused")
		int yy = (int) Math.floor(centerUV.y - (trgt.y / scale));
		transformedCoordinate[0] = offset[0] + (int) Math.floor(centerUV.x + (trgt.x / scale));
		transformedCoordinate[1] = offset[1] + (int) Math.floor(centerUV.y - (trgt.y / scale));
		// int ymax = (int) (360 * scale);
		// int miny = screenManager.getCanvas().getSize().y - (centerUV.y -
		// offset[1]);
		// transformedCoordinate[1] = miny - ((int) Math.floor((1 -
		// Math.log(Math.tan(Math.toRadians(coordinate.y)) + 1 /
		// Math.cos(Math.toRadians(coordinate.y))) / Math.PI) / 2 / ymax));

		return transformedCoordinate;
	}

	public int[] transformCoord(Coordinate coordinate, int srid) {
		return transformCoord(coordinate, srid, false);
	}

	public void setScreenManager(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}

}