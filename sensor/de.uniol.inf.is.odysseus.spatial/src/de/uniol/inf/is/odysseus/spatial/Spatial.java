package de.uniol.inf.is.odysseus.spatial;

import de.uniol.inf.is.odysseus.spatial.datatypes.Facet3D;
import de.uniol.inf.is.odysseus.spatial.datatypes.Line3D;
import de.uniol.inf.is.odysseus.spatial.datatypes.Point3D;
import de.uniol.inf.is.odysseus.spatial.datatypes.Segment3D;
import de.uniol.inf.is.odysseus.spatial.datatypes.Solid3D;
import de.uniol.inf.is.odysseus.spatial.exception.CombinationImpossibleException;
import de.uniol.inf.is.odysseus.spatial.exception.NoIntersectionException;


/**
 * This is a Utility-Class for spatial objects. It provides methods for
 * spatial objects of different types
 * @author Andre Bolles
 *
 */
public class Spatial{

	/**
	 * See Weinrich et. al 2005
	 */
	public static boolean on(Point3D p, Segment3D s, double eps){
//		if(((DoubleValUtils.higherEquals(p.getX(), s.getStart().getX(), eps) && DoubleValUtils.lowerEquals(p.getX(), s.getEnd().getX(), eps)) ||
//				(DoubleValUtils.higherEquals(p.getX(), s.getEnd().getX(), eps) && DoubleValUtils.lowerEquals(p.getX(), s.getStart().getX(), eps) ))
//				
//			&&
//			
//			((DoubleValUtils.higherEquals(p.getY(), s.getStart().getY(), eps) && DoubleValUtils.lowerEquals(p.getY(), s.getEnd().getY(), eps)) ||
//					(DoubleValUtils.higherEquals(p.getY(), s.getEnd().getY(), eps) && DoubleValUtils.lowerEquals(p.getY(), s.getStart().getY(), eps) ))
//				
//			&& 
//				
//			((DoubleValUtils.higherEquals(p.getZ(), s.getStart().getZ(), eps) && DoubleValUtils.lowerEquals(p.getZ(), s.getEnd().getZ(), eps)) ||
//					(DoubleValUtils.higherEquals(p.getZ(), s.getEnd().getZ(), eps) && DoubleValUtils.lowerEquals(p.getZ(), s.getStart().getZ(), eps) ))){
//			
//			// jetzt m�ssen auch noch die Steigungen gleich sein.
//			double x_m_x1 = p.getX() - s.getStart().getX();
//			double y_m_y1 = p.getY() - s.getStart().getY();
//			double z_m_z1 = p.getZ() - s.getStart().getZ();
//			
//			
//			
//			double x2_m_x1 = s.getEnd().getX() - s.getStart().getX();
//			double y2_m_y1 = s.getEnd().getY() - s.getStart().getY();
//			double z2_m_z1 = s.getEnd().getZ() - s.getStart().getZ();
//			
//			// Es kann nat�rlich sein, dass die Steigung 0 ist.
//			// In dem Fall kann die jeweilige �berpr�fung 
//			// ignoriert werden, weil dann klar ist,
//			// dass diese Komponente des Punktes auf dem
//			// Segment liegt. Sonst w�re man nicht bis
//			// hierher gekommen.
//			
//			double m1 = Double.NaN;
//			try{
//				m1 = x_m_x1 / (x2_m_x1);
//			}catch(ArithmeticException e){
//				// ignore
//			}
//			
//			double m2 = Double.NaN;
//			try{
//				m2 = y_m_y1 / y2_m_y1;
//			}catch(ArithmeticException e){
//				// ignore
//			}
//			
//			double m3 = Double.NaN;
//			try{
//				m3 = z_m_z1/(z2_m_z1);
//			}catch(ArithmeticException e){
//				// ignore
//			}
//			if(m1 != Double.NaN && m2 != Double.NaN && m3 != Double.NaN){
//				return DoubleValUtils.equal(m1, m2, eps) && DoubleValUtils.equal(m2, m3, eps);
//			}
//			else if(m1 != Double.NaN && m2 != Double.NaN && m3 == Double.NaN){
//				return DoubleValUtils.equal(m1, m2, eps);
//			}
//			else if(m1 != Double.NaN && m2 == Double.NaN && m3 != Double.NaN){
//				return DoubleValUtils.equal(m1, m3, eps);
//			}
//			else if(m1 != Double.NaN && m2 == Double.NaN && m3 == Double.NaN){
//				return true;
//			}
//			else if(m1 == Double.NaN && m2 != Double.NaN && m3 != Double.NaN){
//				return DoubleValUtils.equal(m2, m3, eps);
//			}
//			else if(m1 == Double.NaN && m2 != Double.NaN && m3 == Double.NaN){
//				return true;
//			}
//			else if(m1 == null && m2 == null && m3 != null){
//				return true;
//			}
//			else if(m1 == null && m2 == null && m3 == null){
//				throw new IllegalArgumentException("Ein Segment muss verschiedene Endpunkte besitzen.");
//			}
//		}
		return false;
	}
	
	/**
	 * see Weinrich et. al 2005
	 */
	public static boolean on(Point3D p, Facet3D f){
		for(Segment3D s : f.getSegments()){
			if(p.on(s)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * See Weinrich et. al 2005
	 */
	public static boolean in(Point3D p, Segment3D s){
//		if(Spatialdouble.on(p, s)){
//			if(!p.equals(s.getStart()) && !p.equals(s.getEnd())){
//				return true;
//			}
//		}
//		else{
//			return false;
//		}
		return false;
	}
	
	/**
	 * see Weinricht et. al 2005
	 */
	public static boolean in(Point3D p, Facet3D f){
//		// Bestimme die gr��ten Koordinaten
//		double x_max = double.ZERO;
//		double y_max = double.ZERO;
//		double z_max = double.ZERO;
//		
//		for(Segment3D s: f.getSegments()){
//
//			if(s.getStart().getX().compareTo(x_max) > 0){
//				y_max = s.getStart().getX();
//			}
//			if(s.getEnd().getX().compareTo(x_max) > 0){
//				x_max = s.getEnd().getX();
//			}
//			
//			if(s.getStart().getY().compareTo(y_max) > 0){
//				y_max = s.getStart().getY();
//			}
//			if(s.getEnd().getY().compareTo(y_max) > 0){
//				y_max = s.getEnd().getY();
//			}
//			
//			// wie bei y-Koordinaten sind auch die z-Koordinaten
//			// nicht unbeding sortiert.
//			if(s.getStart().getZ().compareTo(z_max) > 0){
//				z_max = s.getStart().getZ();
//			}
//			if(s.getEnd().getZ().compareTo(z_max) > 0){
//				z_max = s.getEnd().getZ();
//			}
//		}
//		
//		Point3D p_max = new Point3D(x_max, y_max, z_max);
//		
//		// jetzt muss ein Punkt gefunden werden, dessen jeweilige Koordinaten groesser sind,
//		// als die soeben gefundenen max-Koordinaten und der coplanar zur Facet ist.
//		// Dazu w�hle ein Segment aus der Facet aus und erzeuge eine Vektorgeradengleichung.
//		// (a1, a2, a2) + l(b1-a1, b2-a2, b3-a2)
//		// Bestimme jeweils l in den folgenden Gleichungen
//		// ai + l(bi-ai) = i_max, mit i_max aus {x_max, y_max, z_max)
//		// Waehle das groesste l aus.
//		// Es k�nnte nat�rlich sein, dass p_max gleich einem der Punkte des gew�hlten Segments
//		// ist. In dem Fall waehle den jewils anderen Punkt des Segmentes.
//		
//		Segment3D s = f.getSegment(0, false);
//		
//		// damit l != 0 wird, muss als Aufpunkt in der Vektorgeradengleichung ein
//		// Punkt != p_max gew�hlt werden.
//		double r1 = null;
//		double r2 = null;
//		double r3 = null;
//		if(s.getStart().equals(p_max)){
//			r1 = s.getStart().getX().subtract(s.getEnd().getX());
//			r2 = s.getStart().getY().subtract(s.getEnd().getY());
//			r3 = s.getStart().getZ().subtract(s.getEnd().getZ());
//		}
//		else{
//			r1 = s.getEnd().getX().subtract(s.getStart().getX());
//			r2 = s.getEnd().getY().subtract(s.getStart().getY());
//			r3 = s.getEnd().getZ().subtract(s.getStart().getZ());
//		}
//		
//		double l1 = double.ZERO;
//		double l2 = double.ZERO;
//		double l3 = double.ZERO;
//		
//		// man muss jetzt noch jeweils aufpassen das rx != 0, weil
//		// es ja sein k�nnte, dass die Facet genau in einer der Ebenen
//		// xy, yz, xz liegt. In diesem Fall kann das enstprechende lx
//		// ignoriert werden.
//		// Auch hier muss wieder darauf geachtet werden, dass der Aufpunkt
//		// von p_max verschieden ist.
//		if(!r1.equals(double.ZERO)){
//			if(s.getStart().equals(p_max)){
//				l1 = x_max.subtract(s.getEnd().getX().divide(r1));
//			}
//			else{
//				l1 = x_max.subtract(s.getStart().getX()).divide(r1);
//			}
//		}
//		
//		if(!r2.equals(double.ZERO)){
//			if(s.getStart().equals(p_max)){
//				l2 = y_max.subtract(s.getEnd().getX().divide(r2));
//			}
//			else{
//				l2 = y_max.subtract(s.getStart().getY()).divide(r2);
//			}
//		}
//		
//		if(!r3.equals(double.ZERO)){
//			if(s.getStart().equals(p_max)){
//				l3 = z_max.subtract(s.getEnd().getX().divide(r3));
//			}
//			else{
//				l3 = z_max.subtract(s.getStart().getZ()).divide(r3);
//			}
//		}
//		
//		double l = null;
//
//		if(l1.compareTo(l2) > 0){
//			if(l1.compareTo(l3) > 0){
//				l = l1;
//			}
//			else{
//				l = l3;
//			}
//		}
//		else{
//			if(l2.compareTo(l3) > 0){
//				l = l2;
//			}
//			else{
//				l = l3;
//			}
//		}
//		
//		// l muss noch um Betrag 1 erh�ht werden
//		if(l.compareTo(double.ZERO) > 0){
//			l = l.add(double.ONE);
//		}
//		else if(l.compareTo(double.ZERO) < 0){
//			l = l.add(new double(-1, 1));
//		}
//		
//		// hier muss man wieder darauf achten, dass der Aufpunkt in
//		// der Vektorgeradengleichung != p_max ist.
//		double x_out = null;
//		double y_out = null;
//		double z_out = null;
//		if(s.getStart().equals(p_max)){
//			x_out = s.getEnd().getY().add(l.multiply(r1));
//			y_out = s.getEnd().getY().add(l.multiply(r2));
//			z_out = s.getEnd().getY().add(l.multiply(r3));
//		}
//		else{
//			x_out = s.getStart().getX().add(l.multiply(r1));
//			y_out = s.getStart().getY().add(l.multiply(r2));
//			z_out = s.getStart().getZ().add(l.multiply(r3));
//		}
//		
//		Point3D out = new Point3D(x_out, y_out, z_out);
//		
//		// erzeuge ein Segment von p nach out
//		Segment3D sp = new Segment3D(p, out);
//		
//		// Bestimme alle Segemente s in f mit s intersect sp || s meets sp || s touches sp
//		ArrayList<Segment3D> seg_intersect = new ArrayList<Segment3D>();
//		ArrayList<Segment3D> seg_on = new ArrayList<Segment3D>();
//		for(Segment3D seg : f.getSegments()){
//			try{
//				// falls der Endpunkt von s auf sp liegt, dann f�ge zu s_on hinzu
//				if(seg.getEnd().on(sp)){
//					seg_on.add(seg);
//				}
//				// falls sp und s sich schneiden, f�ge zu s_intersect hinzu
//				else if(seg.intersects(sp)){
//					seg_intersect.add(seg);
//				}
//			}catch(ArithmeticException e){
//				seg.intersects(sp);
//			}
//		}
//		
//		// falls p nicht auf einem der Randsegmente liegt, und die Anzahl geschnittener + getouchter
//		// Segmente ungerade ist, liegt p in der Facet sonst nicht.
//		return !p.on(f) && (seg_intersect.size() + seg_on.size()) % 2 == 1;
		return false;
	}
	
	public static boolean out(Point3D p, Facet3D f){
		return !(on(p, f) || in(p, f));
	}
	
	/**
	 * A facet f1 is coplanar to a facet f2 if three
	 * different points of f1 are coplanar to f2.
	 * @param f1 The first facet
	 * @param f2 The second facet
	 * @return True, if f1 and f2 are in the same plane. False otherwise.
	 */
	public static boolean coplanar(Facet3D f1, Facet3D f2){
		Point3D p1 = f1.getSegment(0, true).getStart();
		Point3D p2 = f1.getSegment(0, true).getEnd();
		
		// Der dritte Punkt muss ein Punkt des naechsten Segments sein.
		// Da dieser Punkt jedoch mit einem der beiden Punkte des vorherigen
		// Segments identisch sein kann, muss erst herausgefunden werden,
		// welcher Punkt des Segments mit einem der Punkte des vorherigen
		// Segments identisch ist. Waehle dann den anderen.
		Point3D p3_t1 = f1.getSegment(1, true).getStart();
		Point3D p3_t2 = f1.getSegment(1, true).getEnd();
		
		Point3D p3 = p3_t1.equals(p1) || p3_t1.equals(p2) ? p3_t2 : p3_t1;

		return coplanar(p1, f2) && coplanar(p2, f2) && coplanar(p3, f2);
	}	
	
	/**
	 * See Weinricht et. al 2005
	 */
	public static boolean coplanar(Point3D p, Facet3D f){
//		Matrix3D matrix = new Matrix3D();
//		
//		// Choose 3 points of the facet
//		Point3D p1 = f.getSegment(0, true).getStart();
//		Point3D p2 = f.getSegment(0, true).getEnd();
//		
//		// Der dritte Punkt muss ein Punkt des naechsten Segments sein.
//		// Da dieser Punkt jedoch mit einem der beiden Punkte des vorherigen
//		// Segments identisch sein kann, muss erst herausgefunden werden,
//		// welcher Punkt des Segments mit einem der Punkte des vorherigen
//		// Segments identisch ist. Waehle dann den anderen.
//		Point3D p3_t1 = f.getSegment(1, true).getStart();
//		Point3D p3_t2 = f.getSegment(1, true).getEnd();
//		
//		Point3D p3 = p3_t1.equals(p1) || p3_t1.equals(p2) ? p3_t2 : p3_t1;
//		
//		matrix.setValue(0, 0, p.getX().subtract(p1.getX()));
//		matrix.setValue(1, 0, p.getY().subtract(p1.getY()));
//		matrix.setValue(2, 0, p.getZ().subtract(p1.getZ()));
//		
//		matrix.setValue(0, 1, p2.getX().subtract(p1.getX()));
//		matrix.setValue(1, 1, p2.getY().subtract(p1.getY()));
//		matrix.setValue(2, 1, p2.getZ().subtract(p1.getZ()));
//		
//		matrix.setValue(0, 2, p3.getX().subtract(p1.getX()));
//		matrix.setValue(1, 2, p3.getY().subtract(p1.getY()));
//		matrix.setValue(2, 2, p3.getZ().subtract(p1.getZ()));
//		
//		return matrix.det().equals(double.ZERO);
		return false;
	}
	
	/**
	 * A segment is coplanar to a facet, if both
	 * endpoints of the segment are coplanar to the
	 * facet.
	 * @param s The segment to be tested
	 * @param f The facet to be tested
	 * @return True, if s and f are in the same plane. False otherwise.
	 */
	public static boolean coplanar(Segment3D s, Facet3D f){
		return coplanar(s.getStart(), f) && coplanar(s.getEnd(), f);
	}
	
	/**
	 * A segment intersect with a facet, if there exists
	 * an intersection point
	 */
	public static boolean intersect(Segment3D s, Facet3D f){
//		try{
//			Spatialdouble.intersection(s, f);
//		}catch(NoIntersectionException e){
//			return false;
//		}
		return true;
	}
	
	public static boolean intersect(Segment3D s, Line3D l){
		return l.intersects(new Line3D(false, s));
	}
	
	/**
	 * A line intersects with a facet, if there exists at
	 * least one intersection point
	 */
	public static boolean intersect(Line3D l, Facet3D f){
//		for(Segment3D s: l.getSegments()){
//			try{
//				Spatialdouble.intersection(s, f);
//				return true;
//			}catch(NoIntersectionException e){
//				// ignore
//			}
//		}
		return false;
	}
	
	
	/**
	 * To facets meet if they do not overlap
	 * and they have at least one segment in common.
	 * 
	 * Remember that we do not allow partial meeting, which
	 * means that to segments overlap but are not equal.
	 * 
	 * @param f1
	 * @param f2
	 * @return True, if f1 and f2 meet by the above condition,
	 * false otherwise.
	 */
	public static boolean meet(Facet3D f1, Facet3D f2){
		if(overlaps(f1, f2)){
			return false;
		}
		
		for(Segment3D s1: f1.getSegments()){
			for(Segment3D s2: f2.getSegments()){
				if(s1.weakEquals(s2)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * For this algorithm, we assume that the two facet objects
	 * represent strictly convex facets. Calling the method
	 * with at least one non-convex facet may lead to wrong
	 * results.
	 * 
	 * A convex facet overlaps another convex facet if
	 * 1. at least one segment of the one facet intersects at least
	 * one segment of the other facet.
	 * 2. there exist two different touching or meeting points or
	 * 3. a point of the one facet is in the other facet and
	 * 
	 * 4. the facets are coplanar
	 * 
	 * overlapping of two segments must not be tested,
	 * because if there are overlapping segments, there
	 * must also exist a touching or meeting point. Otherwise
	 * this overlapping does not say anything about overlapping
	 * of the two facets.
	 * 		
	 */
	public static boolean overlaps(Facet3D f1, Facet3D f2){
//		if(!f1.coplanar(f2)){
//			return false;
//		}
//		
//		// first check of the corresponding bounding boxes
//		// overlap. If not the facet also do not overlap
//		if( f1.getPMax().getX().compareTo(f2.getPMin().getX()) < 0 || f2.getPMax().getX().compareTo(f2.getPMin().getX()) < 0 ||
//				f1.getPMax().getY().compareTo(f2.getPMin().getY()) < 0 || f2.getPMax().getY().compareTo(f1.getPMin().getY()) < 0 ||
//				f1.getPMax().getZ().compareTo(f2.getPMin().getZ()) < 0 || f2.getPMax().getZ().compareTo(f1.getPMin().getZ()) < 0 ){
//			return false;
//		}
//		
//		// this store maximal 3 meeting points
//		// if there exist more than two meeting
//		// points an overlapping was found
//		// if there exist only two meeting
//		// points check, whether the segment
//		// between the two meeting points
//		// overlaps with a segment of the one
//		// facet AND with one of the other
//		// facet. If this condition is true,
//		// this overlapping only says something
//		// about meeting facets. So another of the above
//		// conditions must be true for overlapping
//		ArrayList<Point3D> meeting_points = new ArrayList<Point3D>();
//		
//		// if there exists more than one touching point
//		// an overlapping is true
//		Point3D touching_point = null;
//		for(int i = 0; i<f1.getSegments().size(); i++){
//			Segment3D s1 = f1.getSegment(i, false);
//			for(int u = 0; u<f2.getSegments().size(); u++){
//				Segment3D s2 = f2.getSegment(u, false);
//				
//				if(s1.intersects(s2)){
//					return true;
//				}
//				if(s1.getEnd().in(f2) || s1.getStart().in(f2) ||
//						s2.getStart().in(f1) || s2.getEnd().in(f1)){
//					return true;
//				}
//				if(s1.meets(s2) || s1.touches(s2)){
//					// find out the meeting or touching point
//					Point3D mpoint = null;
//					Point3D tpoint = null;
//					if(s1.getStart().equals(s2.getStart()) || s1.getStart().equals(s2.getEnd())){
//						mpoint = s1.getStart();
//					}
//					else if(s1.getEnd().equals(s2.getStart()) || s1.getEnd().equals(s2.getEnd())){
//						mpoint = s1.getEnd();
//					}
//					
//					if(mpoint != null){
//						// check, if this meeting point already exists
//						boolean found_point = false;
//						for(int v = 0; v<meeting_points.size(); v++){
//							if(mpoint.equals(meeting_points.get(v))){
//								found_point = true;
//								break;
//							}
//						}
//						if(!found_point){
//							meeting_points.add(mpoint);
//							if(meeting_points.size() == 3){
//								return true;
//							}
//							
//							// I think it would be more
//							// performant to not check
//							// the overlapping now
//							// but first check the rest
//							// of the facets. Maybe the
//							// overlapping will be
//							// solved before testing
//							// all the overlapping segments.
//						}
//					}
//					else{
//						if(s1.getStart().in(s2)){
//							tpoint = s1.getStart();
//						}
//						else if(s1.getEnd().in(s2)){
//							tpoint = s1.getEnd();
//						}
//						else if(s2.getStart().in(s1)){
//							tpoint = s2.getStart();
//						}
//						else if(s2.getEnd().in(s1)){
//							tpoint = s2.getEnd();
//						}
//						else{
//							throw new RuntimeException("This case must not occur.");
//						}
//						
//						if(tpoint != null){
//							// if there exists a touching point and a meeting point
//							// there is an overlapping. The touching points cannot
//							// be meeeting points and vice versa. So they must be
//							// different and no check to be done for this.
//							if(meeting_points.size() > 0){
//								return true;
//							}
//							
//							// if there exist two different touching points
//							// than there is an overlapping
//							if(touching_point != null && !touching_point.equals(tpoint)){
//								return true;
//							}
//							else if(touching_point == null){
//								touching_point = tpoint;
//							}
//						}
//					}
//				}
//			}
//		}
//		// if we are here now, maybe there are two meeting points.
//		// if these meeting points form a segment that overlaps
//		// with a segment from one AND the other facet, there
//		// is not overlapping.
//		// if overlaps with no or only a segment of one facet
//		// there must be an overlapping
//		if(meeting_points.size() == 2){
//			// form the segment
//			Segment3D mSeg = new Segment3D(meeting_points.get(0), meeting_points.get(1));
//			
//			boolean overlapf1 = false;
//			boolean overlapf2 = false;
//			// test a against the first facet
//			for(int v = 0; v<f1.getSegments().size(); v++){
//				if(mSeg.overlaps(f1.getSegment(v, false))){
//					overlapf1 = true;
//					break;
//				}
//			}
//			
//			// if there is no overlapping segment in f1
//			// mSeg can at most overlap with a segment
//			// from one facet, so there must be an overlapping
//			// of the facets. You return true, here
//			if(!overlapf1){
//				return true;
//			}
//			
//			// if there is an overlapping
//			// test the second facet
//			for(int v = 0; v<f2.getSegments().size(); v++){
//				if(mSeg.overlaps(f2.getSegment(v, false))){
//					overlapf2 = true;
//					break;
//				}
//			}
//			
//			// there is only an overlapping segment in
//			// f1
//			if(!overlapf2){
//				return true;
//			}
//			// there is an overlapping segment in f1 and
//			// f2
//			return false;
//		}
		return false;
	}
	
	// Methods relating segments and facets ========================================================================


	/**
	 * Determines the intersection point of a facet and a segment.
	 * Verfahren nach buckel:vektorgeo:2001
	 */
	public static Point3D intersection(Segment3D s, Facet3D f){
//		// W�hle drei unterschiedliche Punkte auf der Facet aus.
//		Point3D p3 = f.getSegment(0, false).getStart();
//		Point3D p4 = f.getSegment(0, false).getEnd();
//		Point3D p5 = null;
//		
//		// Pr�fe, welcher der beiden Punkte identisch mit den beiden
//		// bereits gew�hlten Punkten ist und w�hle den anderen.
//		if(p3.equals(f.getSegment(1, false).getStart()) || p4.equals(f.getSegment(1, false).getStart())){
//			p5 = f.getSegment(1, false).getEnd();
//		}
//		else if(p3.equals(f.getSegment(1, false).getEnd()) || p4.equals(f.getSegment(1, false).getEnd())){
//			p5 = f.getSegment(1, false).getStart();
//		}
//		
//		// Bestimme die Gleichung der Ebene, in der f liegt
//		// in der Parameterform
//		// p3 wird Aufpunkt
//		// p4-p3 ist ein Richtungsvektor r
//		// p5-p3 ist ein weiterer Richtungsvektor t
//		
//		// Bestimmung von r
//		double[] r = new double[3];
//		r[0] = p4.getX().subtract(p3.getX());
//		r[1] = p4.getY().subtract(p3.getY());
//		r[2] = p4.getZ().subtract(p3.getZ());
//		
//		
//		// Bestimmung von s
//		double t[] = new double[3];
//		t[0] = p5.getX().subtract(p3.getX());
//		t[1] = p5.getY().subtract(p3.getY());
//		t[2] = p5.getZ().subtract(p3.getZ());
//		
//		// Bestimmung der Geradengleichung f�r die Gerade,
//		// die die Verl�ngung des Segmentes s ist.
//		
//		// Bestimmung des Richtungsvektors v
//		double[] v = new double[3];
//		v[0] = s.getEnd().getX().subtract(s.getStart().getX());
//		v[1] = s.getEnd().getY().subtract(s.getStart().getY());
//		v[2] = s.getEnd().getZ().subtract(s.getStart().getZ());
//		
//		// Bestimmung der Nennerdeterminanten
//		// | r1 t1 -v1|
//		// | r2 t2 -v2|
//		// | r3 t3 -v3|
//		Matrix3D matrix = new Matrix3D(3,3);
//		matrix.setValue(0, 0, r[0]);
//		matrix.setValue(1, 0, t[0]);
//		matrix.setValue(2, 0, v[0].multiply(new double(-1, 1)));
//		matrix.setValue(0, 1, r[1]);
//		matrix.setValue(1, 1, t[1]);
//		matrix.setValue(2, 1, v[1].multiply(new double(-1, 1)));
//		matrix.setValue(0, 2, r[2]);
//		matrix.setValue(1, 2, t[2]);
//		matrix.setValue(2, 2, v[2].multiply(new double(-1, 1)));
//		
//		double detn = matrix.det();
//		
//		// Falls ungleich 0, Bestimmung der Determinaten f�r die Variable v (Wert f�r v)
//		// | r1 t1 b1-a1|
//		// | r2 t2 b2-a2|
//		// | r3 t3 b3-a3|
//		if(!detn.equals(double.ZERO)){
//			Matrix3D mv = new Matrix3D(3, 3);
//			mv.setValue(0, 0, r[0]);
//			mv.setValue(1, 0, t[0]);
//			mv.setValue(2, 0, s.getStart().getX().subtract(p3.getX()));
//			mv.setValue(0, 1, r[1]);
//			mv.setValue(1, 1, t[1]);
//			mv.setValue(2, 1, s.getStart().getY().subtract(p3.getY()));
//			mv.setValue(0, 2, r[2]);
//			mv.setValue(1, 2, t[2]);
//			mv.setValue(2, 2, s.getStart().getZ().subtract(p3.getZ()));
//			
//			double val = mv.det().divide(detn);
//			
//			// Einsetzen von val in die Geradengleichung f�r die Segmentgerade
//			double x = s.getStart().getX().add(val.multiply(v[0]));
//			double y = s.getStart().getY().add(val.multiply(v[1]));
//			double z = s.getStart().getZ().add(val.multiply(v[2]));
//			
//			Point3D p_cut = new Point3D(x, y, z);
//			
//			// pr�fe, ob der Punkt auf dem Segment und in der Facet liegt.
//			// Falls nicht, so liegt der Schnittpunkt au�erhalb und damit
//			// gibt es keinen Schnittpunkt.
//			if(p_cut.on(s) && !p_cut.out(f)){
//				return p_cut;
//			}
//			else{
//				throw new NoIntersectionException("Der Schnittpunkt " + p_cut + " liegt ausserhalb des Segments oder der Facet.");
//			}
//		}
//		else{
//			throw new NoIntersectionException("Es gibt keinen Schnittpunkt zwischen Segment " + s.toString() +
//					" und Facet " + f.toString() + ". Sie sind parallel oder liegen ineinander.");
//		}
		return null;
	}
	
	/**
	 * A segment overlaps a facet, if the segment and the facet are
	 * coplanar and at least one of the segments endpoints
	 * is in the facet or both endpoints are outside the facet,
	 * but there exists a segment in the facet that intersects with
	 * the segment to test.
	 * 
	 * This is not the same as in Weinrich at el. where segment
	 * also overlaps a facet if segment and facet are coplanar,
	 * the segment is outside
	 * and the segment only touches a segment of the facet.
	 * 
	 * A segment does not overlap a facet if it is inside and
	 * touches a boundary segment. In this case the segment
	 * is inside the facet.
	 */
	public static boolean overlaps(Segment3D s, Facet3D f){
		if(coplanar(s, f)){
			if((s.getEnd().in(f) && s.getStart().out(f))
					||
				(s.getStart().in(f) && s.getEnd().out(f))){
				return true;
			}
			else if(s.getStart().out(f) && s.getEnd().out(f)){
				for(Segment3D sf: f.getSegments()){
					if(s.intersects(sf)){
						return true;
					}
				}
				return false;
			}
			return false;
		}
		return false;
	}

	
	// Methods relating segments and solids ===========================================================================
	
	/**
	 * A segment grooves a solid, if it intersects
	 * with an odd number of facets and segments of the solid
	 */
	public static boolean groove(Segment3D s, Solid3D sd){
//		int counter = 0;
//		for(Facet3D f: sd.getFacets()){
//			if(Spatialdouble.intersect(s, f)){
//				counter++;
//			}
//			// If the segment is coplanar
//			// to any of the solid's facets
//			// it cannot groove the solid, because
//			// of the assumption of only regarding
//			// convex solids.
//			else if(Spatialdouble.coplanar(s, f)){
//				return false;
//			}
//		}
//		
//		// Also the intersection of s and
//		// a segment of the solid must
//		// be checked.
//		for(Segment3D sf : sd.getSegments()){
//			if(Spatialdouble.intersect(s, sf)){
//				counter++;
//			}
//		}
//		
//		return counter%2 == 1;
		return false;
	}
	
	/**
	 * A line grooves a facet, if
	 * there exists an odd number of intersection
	 * points between segments of the line, which are
	 * not coplanar to any facet of solid, and a facet or
	 * a segment of the solid.
	 * 
	 * @param l
	 * @param sd
	 * @return
	 */
	public static boolean groove(Line3D l, Solid3D sd){
//		int counter = 0;
//		for(Segment3D s: l.getSegments()){
//			boolean coplanarToFacet = false;
//			for(Facet3D f: sd.getFacets()){
//				if(Spatialdouble.intersect(s, f)){
//					counter++;
//				}
//				// If the segment is coplanar
//				// to any of the solid's facets
//				// it cannot groove the solid, because
//				// of the assumption of only regarding
//				// convex solids.
//				else if(Spatialdouble.coplanar(s, f)){
//					coplanarToFacet = true;
//				}
//			}
//			
//			// Also the intersection of s and
//			// a segment of the solid must
//			// be checked. This count will only be regarded
//			// if the segment is not coplanar
//			// to any of the facets
//			if(!coplanarToFacet){
//				for(Segment3D sf : sd.getSegments()){
//					if(Spatialdouble.intersect(s, sf)){
//						counter++;
//					}
//				}
//			}
//		}
//		return counter%2 == 1;
		return false;
	}
	
	public static boolean intersect(Line3D l, Solid3D sd){
//		int counter = 0;
//		for(Segment3D s: l.getSegments()){
//			boolean coplanarToFacet = false;
//			for(Facet3D f: sd.getFacets()){
//				if(Spatialdouble.intersect(s, f)){
//					// the segment already intersects
//					// with one facet, so now it
//					// intersects with two facets
//					// and therefor true can be returned.
//					if(counter == 1){
//						return true;
//					}
//					else{
//						counter++;
//					}
//				}
//				// if the segment is coplanar
//				// to facet of the solid, it cannot
//				// intersect with the solid,
//				// because of the assumption of only
//				// regarding convex solids
//				else if(Spatialdouble.coplanar(s, f)){
//					coplanarToFacet = false;
//				}
//			}
//			
//			// If the segment is coplanar to a facet
//			// of the solid, the intersection of this
//			// segment and some segments of the solid
//			// does not say anything about
//			// the intersection of the line with the solid.
//			// So only count intersections between
//			// segments that are not coplanar to any facets and
//			// segments of the solid
//			if(!coplanarToFacet){
//				for(Segment3D sf: sd.getSegments()){
//					if(s.intersects(sf)){
//						// if there is already one intersection
//						// point this is the second one and therefor
//						// the segment intersects the solid. True can
//						// be returned.
//						if(counter == 1){
//							return true;
//						}else{
//							counter++;
//						}
//					}
//				}
//			}
//		}
//		return counter >= 2;
		return false;
	}
	
	/**
	 * A segment intersects with a solid if it intersects
	 * with at least 2 facets of the solid.
	 * @param s
	 * @param s1
	 * @return True, if s intersects with sd
	 */
	public static boolean intersect(Segment3D s, Solid3D sd){
//		int counter = 0;
//		for(Facet3D f: sd.getFacets()){
//			if(Spatialdouble.intersect(s, f)){
//				// the segment already intersects
//				// with one facet, so now it
//				// intersects with two facets
//				// and therefor true can be returned.
//				if(counter == 1){
//					return true;
//				}
//				else{
//					counter++;
//				}
//			}
//			// if the segment is coplanar
//			// to facet of the solid, it cannot
//			// intersect with the solid,
//			// because of the assumption of only
//			// regarding convex solids
//			else if(Spatialdouble.coplanar(s, f)){
//				return false;
//			}
//		}
//		
//		// the segment is not coplanar to any facet, so
//		// it the segment can also intersect with any
//		// segment of the solid
//		// check it
//		for(Segment3D sf: sd.getSegments()){
//			if(s.intersects(sf)){
//				// if there is already one intersection
//				// point this is the second one and therefor
//				// the segment intersects the solid. True can
//				// be returned.
//				if(counter == 1){
//					return true;
//				}else{
//					counter++;
//				}
//			}
//		}
//		return counter >= 2;
		return false;
	}
	
//	public boolean overlaps_old(Facet3D f1, Facet3D f2){
//		boolean meet_touch = false;
//		for(int i = 0; i<f1.getSegments().size(); i++){
//			Segment3D s1 = f1.getSegment(i, false);
//			for(int u = 0; u<f2.getSegments().size(); u++){
//				Segment3D s2 = f2.getSegment(u, false);
//				if(s1.intersect(s2)){
//					return true;
//				}
//
//				// there must be three segments involved
//				// in touching. If si touches s, then si+1
//				// must also touch s if there is an overlapping
//				// area. Also si+1 must be on the other side
//				// of s than si
//				
//				/* you can do check of left and right side
//				 * in the following way.
//				 * Take s1 and si. Generate the vector product,
//				 * so you get a new vector that is orthogonal
//				 * to s1 and si. Than take the new vector
//				 * and generate the vector product of
//				 * the new vector and s1. Than you get a vector
//				 * that is orthogonal to s1 and in the plan
//				 * of the facet. Then you can determine
//				 * which paramater t is necessary, to 
//				 * lie a straight line through the point of si
//				 * that does not meet or touch s1. Do the same
//				 * with the segment si+1. If the parameters
//				 * t are of different signs, than an overlapping
//				 * occured.
//				 */
//				if(s1.meets(s2) || s1.touches(s2)){
//					meet_touch = true;
//				}
//				if(s1.overlaps(s2)){
//					// two overlapping segments show into
//					// the same direction, if
//					// there exists l > 0 such that
//					// (x1, y1, z1) = l (x2, y2, z2)
//					// They show into different directions
//					// if l < 0
//					// solving the equation system
//					// leads to
//					// l1 = x1 / x2, l2 = y1/y2 and l3 = z1/z2
//					// and l1 = l2 = l3
//					// we already checked, if the segements
//					// overlap, so we just determine x1 / x2
//					
//					// generate the first x component
//					double x1 = s1.getEndPoint().getX().subtract(s1.getStartPoint().getX());
//					
//					// generate the second x component
//					double x2 = s2.getEndPoint().getX().subtract(s2.getStartPoint().getX());
//					
//					// perhaps x1 or x2 are equal to zero. In this case use the y components;
//					if(!(x1.compareTo(double.ZERO) == 0) && !(x2.compareTo(double.ZERO) == 0)){
//						return x1.divide(x2).compareTo(double.ZERO) > 0;
//					}
//					else{
//						// use the y component
//						// generate the first y component
//						double y1 = s1.getEndPoint().getY().subtract(s1.getStartPoint().getY());
//						
//						// generate the second y component
//						double y2 = s2.getEndPoint().getY().subtract(s2.getStartPoint().getY());
//						
//						// perhaps y1 or y2 are equal to zero. In this case use the z components;
//						if(!(y1.compareTo(double.ZERO) == 0) && !(y2.compareTo(double.ZERO) == 0)){
//							return y1.divide(y2).compareTo(double.ZERO) > 0;
//						}
//						else{
//							// generate the first y component
//							double z1 = s1.getEndPoint().getZ().subtract(s1.getStartPoint().getZ());
//							
//							// generate the second y component
//							double z2 = s2.getEndPoint().getZ().subtract(s2.getStartPoint().getZ());
//							
//							// perhaps y1 or y2 are equal to zero. In this case use the z components;
//							if(!(z1.compareTo(double.ZERO) == 0) && !(z2.compareTo(double.ZERO) == 0)){
//								return z1.divide(z2).compareTo(double.ZERO) > 0;
//							}
//							else{
//								throw new InvalidFacetException("A segment of a facet must not be a point.");
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		// maybe there no intersections or meeting points
//		// in this case one facet could completly in the other
//		// facet
//		// do not use the method in of Facet3D, because
//		// for overlapping check it is enough to
//		// find one point that is in the other facet.
//		for(int i = 0; i<f1.getSegments().size(); i++){
//			Segment3D s1 = f1.getSegment(i, false);
//			if(s1.getStartPoint().in(f2)){
//				return true;
//			}
//		}
//		for(int i = 0; i<f2.getSegments().size(); i++){
//			Segment3D s2 = f2.getSegment(i, false);
//			if(s2.getStartPoint().in(f1)){
//				return true;
//			}
//		}
//		
//		if(meet_touch){
//			throw new NotImplementedException("An overlapping of meet and touch cannot be detected at the moment.");
//		}
//		return false;
//	}
	
	// Methods for segments ====================================================================
	/**
	 * see Weinrich et. al 2005
	 * other in weinrich, in our approach
	 * to segments can also be collinear,
	 * if they meet.
	 * collinear: segment x segment -> bool 
	 */
	public static boolean collinear(Segment3D s1, Segment3D s2){
//		if(!s1.equals(s2) && /*!s1.meets(s2) &&*/ !s2.overlaps(s2)){
//			// (x3 - x1) / (x2 - x1)
//			double mX = s2.getStart().getX().subtract(s1.getStart().getX()).divide(s1.getEnd().getX().subtract(s1.getStart().getX()));
//			double mY = s2.getStart().getY().subtract(s1.getStart().getY()).divide(s1.getEnd().getY().subtract(s1.getStart().getY()));
//			double mZ = s2.getStart().getZ().subtract(s1.getStart().getZ()).divide(s1.getEnd().getZ().subtract(s1.getStart().getZ()));
//			
//			if(mX.compareTo(mY) == 0 && mX.compareTo(mZ) == 0){
//				double mX2 = s2.getEnd().getX().subtract(s1.getStart().getX()).divide(s1.getEnd().getX().subtract(s1.getStart().getX()));
//				double mY2 = s2.getEnd().getY().subtract(s1.getStart().getY()).divide(s1.getEnd().getY().subtract(s1.getStart().getY()));
//				double mZ2 = s2.getEnd().getZ().subtract(s1.getStart().getZ()).divide(s1.getEnd().getZ().subtract(s1.getStart().getZ()));
//
//				if(mX2.compareTo(mY2) == 0 && mX2.compareTo(mZ2) == 0){
//					return true;
//				}
//				return false;
//			}
//			return false;
//		}
		return false;
	}
	
	/**
	 * Combines to collinear meeting segments to one segment
	 * 
	 * That means e.g.
	 * 
	 * (0,0,0);(1,1,0)
	 * and
	 * (1,1,0);(2,2,0)
	 * will be combined to
	 * (0,0,0);(2,2,0)
	 * 
	 * In the case that the two start respectively the two
	 * end points meets, the direction of the two segments
	 * differ. In this case the direction of s1
	 * will be taken.
	 * 
	 * @param s1 The segment whose direction will be taken in case of different direction
	 * @param s2 the other segment
	 * @return A combination of both segments a explained above.
	 */
	public static Segment3D combine(Segment3D s1, Segment3D s2){
		if(!s1.collinear(s2) || !s1.meets(s2)){
			throw new CombinationImpossibleException("Only collinear meeting segments can be combined");
		}
		
		if(s1.getStart().equals(s2.getEnd())){
			return new Segment3D(s2.getStart(), s1.getEnd());
		}
		else if(s1.getStart().equals(s2.getStart())){
			return new Segment3D(s1.getEnd(), s2.getEnd());
		}
		else if(s1.getEnd().equals(s2.getEnd())){
			return new Segment3D(s1.getStart(), s2.getStart());
		}
		else if(s1.getEnd().equals(s2.getStart())){
			return new Segment3D(s1.getStart(), s2.getEnd());
		}
		
		throw new CombinationImpossibleException("Could not create combination of the two segments s1 = " + s1.toString() + " s2 = " + s2.toString());
	}
	
	public static boolean coplanar(Segment3D s1, Segment3D s2){
//		Matrix3D matrix = new Matrix3D();
//		matrix.setValue(0, 0, s1.getEnd().getX().subtract(s1.getStart().getX()));
//		matrix.setValue(1, 0, s1.getEnd().getY().subtract(s1.getStart().getY()));
//		matrix.setValue(2, 0, s1.getEnd().getZ().subtract(s1.getStart().getZ()));
//		matrix.setValue(0, 1, s2.getStart().getX().subtract(s1.getStart().getX()));
//		matrix.setValue(1, 1, s2.getStart().getY().subtract(s1.getStart().getY()));
//		matrix.setValue(2, 1, s2.getStart().getZ().subtract(s1.getStart().getZ()));
//		matrix.setValue(0, 2, s2.getEnd().getX().subtract(s1.getStart().getX()));
//		matrix.setValue(1, 2, s2.getEnd().getY().subtract(s1.getStart().getY()));
//		matrix.setValue(2, 2, s2.getEnd().getZ().subtract(s1.getStart().getZ()));
//		
//		return matrix.det().equals(new double(0, 1));
		return false;
	}
	
	/**
	 * Two segments intersect, if there is an intersection point that is different
	 * from the endpoints, so that the segments do not meet or touch
	 * @param s1
	 * @param s2
	 * @return True, if this object and s intersect, false otherwise
	 */
	public static boolean intersect(Segment3D s1, Segment3D s2){
		try{
			s1.intersection(s2);
		}catch(NoIntersectionException e){
			return false;
		}
		return !s1.touches(s2) && !s1.meets(s2);
	}
	
	/**
	 * Two segments meet if they have exactly one start/end point in common
	 * @param s1
	 * @param s2
	 * @return True, if s1 and s2 have exactly one start/end point in common
	 */
	public static boolean meet(Segment3D s1, Segment3D s2){
		if(!s1.overlaps(s2)){
			if((s1.getStart().equals(s2.getStart()) && !s1.getStart().equals(s2.getEnd()) && !s1.getEnd().equals(s2.getStart()) && !s1.getEnd().equals(s2.getEnd())) ||
					(!s1.getStart().equals(s2.getStart()) && s1.getStart().equals(s2.getEnd()) && !s1.getEnd().equals(s2.getStart()) && !s1.getEnd().equals(s2.getEnd())) ||
					(!s1.getStart().equals(s2.getStart()) && !s1.getStart().equals(s2.getEnd()) && s1.getEnd().equals(s2.getStart()) && !s1.getEnd().equals(s2.getEnd())) ||
					(!s1.getStart().equals(s2.getStart()) && !s1.getStart().equals(s2.getEnd()) && !s1.getEnd().equals(s2.getStart()) && s1.getEnd().equals(s2.getEnd()))){
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Two segments touch if they have exactly one point in common.
	 * @param s1
	 * @param s2
	 * @return True, if s1 and s2 have exactly one point in common.
	 */
	public static boolean touch(Segment3D s1, Segment3D s2){
		if(!s1.overlaps(s2)){
			if(s1.getStart().in(s2) || s1.getEnd().in(s2) || s2.getStart().in(s1) || s2.getEnd().in(s1)){
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * Only for testing
	 * @param args
	 */
	public static void main(String[] args){
//		NormalDistribution nd = new NormalDistributionImpl();
//		nd.setMean(0d);
//		nd.setStandardDeviation(1d);
//		try{
//			System.out.println("prob: " + nd.cumulativeProbability(0, 0.1));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		
//		
//		Point3D p1 = new Point3D(double.ZERO, double.ZERO, double.ZERO);
//		Point3D p2 = new Point3D(double.ONE, double.ONE, double.ZERO);
//		Point3D p3 = new Point3D(double.ONE, double.ONE, new double(1, 1));
//		Point3D p4 = new Point3D(new double(2, 1), double.ONE, new double(1, 1));
//		
//		
//		Point3D p5 = new Point3D(new double(2, 1), new double(1, 1), new double(0, 1));
//		Point3D p6 = new Point3D(new double(1, 1), new double(1, 1), new double(1, 1));
//		Point3D p7 = new Point3D(new double(1, 1), new double(2, 1), new double(1, 1));
//		
//		Line3D l1 = new Line3D(p1, p2, p3, p4);
//		Line3D l2 = new Line3D(p5, p6, p7);
//		
//		
//		System.out.println("Intersection: " + l1.intersects(l2));
	}
}
