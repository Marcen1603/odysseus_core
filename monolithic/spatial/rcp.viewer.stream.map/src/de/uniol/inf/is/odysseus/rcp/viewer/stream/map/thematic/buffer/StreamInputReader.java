//package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.buffer;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//
//import org.osgeo.proj4j.CoordinateTransform;
//import org.osgeo.proj4j.ProjCoordinate;
//
//import com.vividsolutions.jts.geom.Coordinate;
//import com.vividsolutions.jts.geom.Geometry;
//import com.vividsolutions.jts.geom.LineString;
//import de.uniol.inf.is.odysseus.core.collection.Tuple;
//import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
//import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
//import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
//import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
//import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTISweepArea;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.IStreamMapEditor;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.LayerUpdater;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenTransformation;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;
//import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.VectorLayer;
//
///**
// * 
// * Reads IStreamObjects with a time attribute and can draw associated tuples on
// * a layer
// * 
// * @author Stefan Bothe
// * 
// */
//public class StreamInputReader {
//
//	// private MapEditorModel mapEditorModel;
//	private IStreamMapEditor streamMapEditor;
//	private TimeSliderControl timeSliderControl;
//	private TimeSliderComposite timeComposite;
//	private LayerUpdater layerUpdater;
//
//	private HashMap<LayerUpdater, DefaultTISweepArea<IStreamObject<? extends ITimeInterval>>> puffer;
//
//	private PointInTime startPointInTime;
//	private PointInTime endPointInTime;
//
//	private Tuple<? extends ITimeInterval> tuple;
//
//	public StreamInputReader(LayerUpdater layerUpdater,
//			IStreamMapEditor streamMapEditor) {
//		 this.streamMapEditor = streamMapEditor;
//		this.layerUpdater = layerUpdater;
//		puffer = new HashMap<>();
//	}
//
//	public boolean hasTimeComposite() {
//		return (this.timeComposite != null);
//	}
//
//	public void readTuple(IStreamObject<? extends ITimeInterval> element) {
//
//		if (!hasTimeComposite()) {
//			TimeSliderComposite timeComposite = streamMapEditor.getTimeSliderComposite();
//			this.timeSliderControl = new TimeSliderControl(this, timeComposite);
//		}
//
//		PointInTime start = element.getMetadata().getStart();
//
//		if (startPointInTime == null || start.before(startPointInTime)) {
//			startPointInTime = start;
//		}
//		if (endPointInTime == null || start.after(endPointInTime)) {
//			endPointInTime = start;
//		}
//
//		streamMapEditor.getScreenManager().getDisplay().syncExec(new Runnable() {
//			@Override
//			public void run() {
//				timeSliderControl.updateSliderRange(new PointInTime(
//						startPointInTime.getMainPoint() - 1), endPointInTime);
//			}
//		});
//
//		if (puffer.get(layerUpdater) == null) {
//			puffer.put(layerUpdater, new DefaultTISweepArea<>());
//		}
//		DefaultTISweepArea<IStreamObject<? extends ITimeInterval>> tempSweep = puffer
//				.get(layerUpdater);
//
//		tempSweep.insert(element);
//
//		streamMapEditor.getScreenManager().getDisplay().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				timeSliderControl.updateCanvas();
//			}
//		});
//	}
//
//	public Geometry transformGeometry(Geometry srcGeom, int destSrid,
//			ScreenTransformation transformation) {
//		Geometry geom = srcGeom;
//		CoordinateTransform ct = transformation.getCoordinateTransform(
//				geom.getSRID(), destSrid);
//		geom = (Geometry) geom.clone();
//		for (Coordinate coord : geom.getCoordinates()) {
//			ProjCoordinate src = new ProjCoordinate(coord.x, coord.y);
//			src.z = coord.z;
//			ProjCoordinate dest = new ProjCoordinate();
//			ct.transform(src, dest);
//			coord.x = dest.x;
//			coord.y = dest.y;
//			coord.z = dest.z;
//		}
//		geom.setSRID(destSrid);
//		return geom;
//	}
//
//	public void draw(long start, long end) {
//		PointInTime startPoint = new PointInTime(start);
//		PointInTime endPoint = new PointInTime(end);
//
//		fillLayersWithTuples(startPoint, endPoint);
//
//		streamMapEditor.getScreenManager().getDisplay().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//				streamMapEditor.getScreenManager().getCanvas().redraw();
//			}
//		});
//	}
//
//	public void fillLayersWithLineString(LineString line) {
//
//		for (LayerUpdater element : puffer.keySet()) {
//			for (ILayer iLayer : element) {
//				if (iLayer instanceof VectorLayer) {
//					VectorLayer layer = (VectorLayer) iLayer;
////					layer.clean();
//					tuple.setAttribute(0, line);
//					layer.addTuple(tuple);
//				}
//			}
//		}
//	}
//
//	public void fillLayersWithTuples(PointInTime start, PointInTime end) {
//		TimeInterval interval = new TimeInterval(start, end.plus(1));
//
//		for (LayerUpdater element : puffer.keySet()) {
//			DefaultTISweepArea<IStreamObject<? extends ITimeInterval>> sweep = puffer
//					.get(element);
//			if (sweep != null) {
//				Iterator<IStreamObject<? extends ITimeInterval>> iter = sweep
//						.queryOverlaps(interval);
//
//				ArrayList<IStreamObject<? extends ITimeInterval>> elementList = new ArrayList<>();
//				while (iter.hasNext()) {
//					elementList.add(iter.next());
//				}
//
//				for (ILayer iLayer : element) {
//					if (iLayer instanceof VectorLayer) {
//						VectorLayer layer = (VectorLayer) iLayer;
////						layer.clean();
//
//						for (IStreamObject<? extends ITimeInterval> iStreamObject : elementList) {
//							layer.addTuple((Tuple<? extends ITimeInterval>) iStreamObject);
//						}
//					}
//				}
//			}
//		}
//	}
//}
