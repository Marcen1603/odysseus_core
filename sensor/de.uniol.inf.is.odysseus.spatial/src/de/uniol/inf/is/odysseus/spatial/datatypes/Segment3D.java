package de.uniol.inf.is.odysseus.spatial.datatypes;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.spatial.exception.NoIntersectionException;

public class Segment3D extends AbstractSpatialObject implements Comparable<Segment3D>, IClone, Serializable{

	private Point3D start;
	private Point3D end;
	
	public Segment3D(Point3D start, Point3D end){
		if(start.equals(end)){
			throw new IllegalArgumentException("A segment can only be defined by two different points.");
		}
		this.start = start;
		this.end = end;
	}
	
	private Segment3D(Segment3D original){
		this.start = original.start.clone();
		this.end = original.start.clone();
	}
	
	/**
	 * Two segments are equal, if their start points are equal
	 * and their end points are equal.
	 * =: segment x segment -> bool
	 */
	public boolean equals(Segment3D s){
		if(this.start.equals(s.start) && this.end.equals(s.end)){
			return true;
		}
		return false;
	}
	
	/**
	 * The same as method equals. But in this case, the direction
	 * of the segments will be disregarded. That means, two segments
	 * are weak equal, if their start points are equal either to
	 * the start or to the end point of the other segment and
	 * vice versa for their end points.
	 * @param s
	 * @return
	 */
	public boolean weakEquals(Segment3D s){
		if( (this.start.equals(s.start) || this.start.equals(s.end)) &&
				(this.end.equals(s.start) || this.end.equals(s.end))){
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a copy of this object,
	 * where all components are also copies.
	 */
	public Segment3D clone(){
		return new Segment3D(this);
	}
	
	/**
	 * see Weinrich et. al 2005
	 * besides the test if one point is in the other segment
	 * also test, if the two segments are identical. In this
	 * case they naturally overlap, too.
	 * overlaps: segment x segment -> bool
	 */
	public boolean overlaps(Segment3D s){
//		if(this.weakEquals(s)){
//			return true;
//		}
//		if(this.start.in(s) || this.end.in(s) || s.start.in(this) || s.end.in(this)){
//			// (x3 - x1) / (x2 - x1)
//			Fraction x2_m_x1 = this.end.getX().subtract(this.start.getX());
//			Fraction y2_m_y1 = this.end.getY().subtract(this.start.getY());
//			Fraction z2_m_z1 = this.end.getZ().subtract(this.start.getZ());
//			
//			Fraction x3_m_x1 = s.start.getX().subtract(this.start.getX());
//			Fraction y3_m_y1 = s.start.getY().subtract(this.start.getY());
//			Fraction z3_m_z1 = s.start.getZ().subtract(this.start.getZ());
//			
//			Fraction mX = null;
//			Fraction mY = null;
//			Fraction mZ = null;
//			
//			boolean xOk = false;
//			boolean yOk = false;
//			boolean zOk = false;
//			
//			// It is possible to be x2-x1, y2-y1, z2-z1 = 0.
//			// In this case also x3-x1, y3-y1, z3-z1 = 0 must hold.
//			if(x2_m_x1.equals(Fraction.ZERO)){
//				if(x3_m_x1.equals(Fraction.ZERO)){
//					xOk = true;
//				}
//				else{
//					return false;
//				}
//			}
//			else{	
//				mX = x3_m_x1.divide(x2_m_x1);
//			}
//			
//			if(y2_m_y1.equals(Fraction.ZERO)){
//				if(y3_m_y1.equals(Fraction.ZERO)){
//					yOk = true;
//				}
//				else{
//					return false;
//				}
//			}
//			else{
//				mY = y3_m_y1.divide(y2_m_y1);
//			}
//			
//			if(z2_m_z1.equals(Fraction.ZERO)){
//				if(z3_m_z1.equals(Fraction.ZERO)){
//					zOk = true;
//				}
//				else{
//					return false;
//				}
//			}
//			else{
//				mZ = z3_m_z1.divide(z2_m_z1);
//			}
//
//			
//			if((mX != null && mY != null && mZ != null && mX.compareTo(mY) == 0 && mX.compareTo(mZ) == 0) ||
//					(mX != null && mY != null && mZ == null && zOk && mX.compareTo(mY) == 0) ||
//					(mX != null && mY == null && yOk && mZ != null && mX.compareTo(mZ) == 0) ||
//					(mX != null && mY == null && yOk && mZ == null && zOk) ||
//					(mX == null && xOk && mY != null && mZ != null && mY.compareTo(mZ) == 0) ||
//					(mX == null && xOk && mY != null && mZ == null && zOk) ||
//					(mX == null && xOk && mY == null && yOk && mZ != null) ||
//					(mX == null && xOk && mY == null && yOk && mZ == null && zOk)){
//				
//				Fraction x4_m_x1 = s.end.getX().subtract(this.start.getX());
//				Fraction y4_m_y1 = s.end.getY().subtract(this.start.getY());
//				Fraction z4_m_z1 = s.end.getZ().subtract(this.start.getZ());
//				
//				Fraction mX2 = null;
//				Fraction mY2 = null;
//				Fraction mZ2 = null;
//				
//				boolean xOk2 = false;
//				boolean yOk2 = false;
//				boolean zOk2 = false;
//				
//				if(x2_m_x1.equals(Fraction.ZERO)){
//					if(x4_m_x1.equals(Fraction.ZERO)){
//						xOk2 = true;
//					}
//					else{
//						return false;
//					}
//				}
//				else{
//					mX2 = x4_m_x1.divide(x2_m_x1);
//				}
//				
//				if(y2_m_y1.equals(Fraction.ZERO)){
//					if(y4_m_y1.equals(Fraction.ZERO)){
//						yOk2 = true;
//					}
//					else{
//						return false;
//					}
//				}
//				else{
//					mY2 = y4_m_y1.divide(y2_m_y1);
//				}
//				
//				if(z2_m_z1.equals(Fraction.ZERO)){
//					if(z4_m_z1.equals(Fraction.ZERO)){
//						zOk2 = true;
//					}
//					else{
//						return false;
//					}
//				}
//				else{
//					mZ2 = z4_m_z1.divide(z2_m_z1);
//				}
//				
//				if((mX2 != null && mY2 != null && mZ2 != null && mX2.compareTo(mY2) == 0 && mX2.compareTo(mZ2) == 0) ||
//						(mX2 != null && mY2 != null && mZ2 == null && zOk2 && mX2.compareTo(mY2) == 0 ) ||
//						(mX2 != null && mY2 == null && yOk2 && mZ2 != null && mX2.compareTo(mZ2) == 0 ) ||
//						(mX2 != null && mY2 == null && yOk2 && mZ2 == null && zOk2) ||
//						(mX2 == null && xOk2 && mY2 != null && mZ2 != null && mY2.compareTo(mZ2) == 0 ) ||
//						(mX2 == null && xOk2 && mY2 != null && mZ2 == null && zOk2) ||
//						(mX2 == null && xOk2 && mY2 == null && yOk2 && mZ2 != null) ||
//						(mX2 == null && xOk2 && mY2 == null && yOk2 && mZ2 == null && zOk2)){
//					return true;
//				}
//				return false;
//			}
//			return false;
//		}
		return false;
	}
	
	/**
	 * see Weinrich et. al 2005
	 * meets: segment x segment -> bool
	 */
	public boolean meets(Segment3D s){
		return false; //SpatialFraction.meet(this, s);
	}
	
	/**
	 * see Weinrich et. al 2005
	 * touches: segment x segment -> bool
	 * One segment touches another one if they have one common point which is
	 * the endpoint of exactly one of the segments
	 */
	public boolean touches(Segment3D s){
		return false; //SpatialFraction.touch(this, s);
	}
	
	
	/**
	 * see Weinrich et. al 2005
	 * other in weinricht, two segments can also be
	 * collinear if they meet.
	 * collinear: segment x segment -> bool 
	 */
	public boolean collinear(Segment3D s){
		return false; //SpatialFraction.collinear(this, s);
	}
	
	/**
	 * see Weinrich et. al 2005
	 * coplanar: segment x segment -> bool
	 */
	public boolean coplanar(Segment3D s){
		return false; //SpatialFraction.coplanar(this, s);
	}
	
	public boolean coplanar(Facet3D f){
		return false; //SpatialFraction.coplanar(this, f);
	}
	
	/**
	 * This method determines the intersection point
	 * of two segment if there exists one. If not a NoIntersectionException
	 * will be thrown.
	 * 
	 * This method uses the Gauss Algorithm for solving
	 * a linear equation system. The following equation system has to be solved
	 * 
	 * this.start + l * (this.end - this.start) = seg.start + u * (seg.end - seg.start)
	 * 
	 * @param seg The other segment
	 * @return The intersection point if one exists.
	 * @throws NoIntersectionException will be thrown if no intersection point exists.
	 */
	public Point3D intersection(Segment3D seg)throws NoIntersectionException{
//		// generating the matrix for the equation system		
//		Fraction[][] vals = new Fraction[3][2];
//		vals[0][0] = this.getEnd().getX().subtract(this.getStart().getX());
//		vals[0][1] = this.getEnd().getY().subtract(this.getStart().getY());
//		vals[0][2] = this.getEnd().getZ().subtract(this.getStart().getZ());
//		
//		vals[1][0] = seg.getEnd().getX().subtract(seg.getStart().getX());
//		vals[1][1] = seg.getEnd().getY().subtract(seg.getStart().getY());
//		vals[1][2] = seg.getEnd().getZ().subtract(seg.getStart().getZ());
//		
//		vals[2][0] = seg.start.getX().subtract(this.start.getX());
//		vals[2][1] = seg.start.getY().subtract(this.start.getY());
//		vals[2][2] = seg.start.getZ().subtract(this.start.getZ());
//		
//		Matrix3D m = new Matrix3D(vals);
//		Matrix3D copy = m.clone();
//		
//		// first prepare the matrix so, that the following
//		// situation does not occur in the matrix
//		// 0 1 1	2 0 1
//		// 2 0 1 -> 3 2 2
//		// 3 2 2	0 1 1
//		// there must not be a 0 over a non-0 in the first
//		// non-0 column of a row
//		
//		for(int i = 0; i<m.getRowDimension(); i++){
//			
//		}
//		
//		// use the gauss algorithm, to solve this equation system
//		for(int i = 1; i<m.getRowDimension(); i++){
//			Fraction sign = m.getValue(i-1, i-1).compareTo(Fraction.ZERO) < 0 ? Fraction.ONE : new Fraction(-1, 1);
//			copy.addRow(1, sign.multiply(m.getValue(i-1, i-1)), copy.getRow(i-1, copy.getValue(i, i)));
//		}
//		
//		// the last row must be filled up with 0, otherwise there is no
//		// intersection point
//		
//		Fraction[] lastRow = copy.getRow(2, Fraction.ONE);
//		for(int i = 0; i<lastRow.length; i++){
//			if(lastRow[i].compareTo(Fraction.ZERO) != 0){
//				throw new NoIntersectionException();
//			}
//		}
		
		
		return null;
	}
	
//	/**
//	 * Der Schnittpunkt wird durch Vektorrechnung gel�st. Dabei entsteht
//	 * ein Gleichungssystem mit 3 Gleichungen. Die Gleichungen konnten
//	 * im Vorfeld allgemein aufgel�st werden, so dass in dieser Methode
//	 * lediglich das Berechnen der Gleichungen stattfindet. Da es sich
//	 * jedoch um endliche Geraden muss abschlie�end noch gepr�ft werden,
//	 * ob die Punkte tats�chlich in dem Segement liegen. Dazu m�ssen
//	 * die Parameter l und u zwischen 0 und 1 liegen.
//	 * 
//	 * Seien 
//	 * g: (a1, a2, a3) + l (r1, r2, r3)
//	 * h: (b1, b2, b3) + u (s1, s2, s3)
//	 * 
//	 * I: a1 + l*r1 = b1 + u*s1
//	 * II: a2 + l*r2 = b2 + u*s2
//	 * III: a3 + l* r3 = b3 + u*s3
//	 * 
//	 * l = (b1 + u*s1 - a1) / r1
//	 * 
//	 * ui = ((bi - ai) * r1) / (ri*s1 -si*r1) - (b1 /(s1 - (si*r1)/ri)) + (a1 / (s1 - (si*r1)/ri))
//	 * 
//	 * @return Diese Methode liefert den Schnittpunkt zweier Segmente. Der Schnittpunkt wird auch dann
//	 * geliefert, wenn es sich um einen der Endpunkte eines Segmentes handelt. M�chte man also
//	 * explizit auf meets / touches pr�fen, so muss das mit den entsprechenden Methoden getan werden.
//	 */
//	public Point3D intersection(Segment3D seg) throws NoIntersectionException{
//		// Berechne die beiden Richtungsvektoren
//		Fraction r1 = this.end.getX().subtract(this.start.getX());
//		Fraction r2 = this.end.getY().subtract(this.start.getY());
//		Fraction r3 = this.end.getZ().subtract(this.start.getZ());
//		
//		Fraction s1 = seg.end.getX().subtract(seg.start.getX());
//		Fraction s2 = seg.end.getY().subtract(seg.start.getY());
//		Fraction s3 = seg.end.getZ().subtract(seg.start.getZ());
//		
//		// Komponenten in den Richtungsvektoren auch 0 sein
//		// k�nnen, m�ssen entsprechende Fallunterscheidungen
//		// durchgef�hrt werden
//		Fraction r[] = new Fraction [3];
//		r[0] = r1;
//		r[1] = r2;
//		r[2] = r3;
//		
//		Fraction s[] = new Fraction [3];
//		s[0] = s1;
//		s[1] = s2;
//		s[2] = s3;
//		
//		Fraction a[] = new Fraction [3];
//		a[0] = this.start.getX();
//		a[1] = this.start.getY();
//		a[2] = this.start.getZ();
//		
//		Fraction b[] = new Fraction [3];
//		b[0] = seg.start.getX();
//		b[1] = seg.start.getY();
//		b[2] = seg.start.getZ();
//		
//		// Falls es eine Gleichung i gibt, mit ri = 0 und si != 0 oder
//		// ri != 0 und si = 0
//		// dann l�sst sich u bzw. l  mit dieser Gleichung leicht bestimmen.
//		
//
//		// hier werden l und u gespeichert,
//		// jeweils unter dem Index der Gleichung,
//		// aus der sie ermittelt wurden.
//		Fraction[] l = new Fraction[3];
//		Fraction[] u = new Fraction[3];
//		boolean found_l = false;
//		boolean found_u = false;
//		int gleichung_u = -1;
//		int gleichung_l = -1;
//		for(int index = 0; index<3 && (!found_u || !found_l); index++){
//			if(!found_u && r[index].equals(Fraction.ZERO) && !s[index].equals(Fraction.ZERO)){
//				u[index] = a[index].subtract(b[index]).divide(s[index]);
//				found_u = true;
//				gleichung_u = index; // merke die Gleichung aus der u bestimmt wurde
//			}
//			if(!found_l && !r[index].equals(Fraction.ZERO) && s[index].equals(Fraction.ZERO)){
//				l[index] = b[index].subtract(a[index]).divide(r[index]);
//				found_l = true;
//				gleichung_l = index; // merke die Gleichung aus der l bestimmt wurde
//			}
//		}
//		
//		// l konnte bereits bestimmt werden, u noch nicht
//		if(found_l && !found_u){
//			// finde eine andere gleichung und bestimme u
//			for(int index = 0; index<3; index++){
//				// die nachfolgende Bedingung muss bei einer Gleichung erf�llt
//				// sein, denn sonst w�ren alle Komponenten von s = 0 und damit
//				// w�re seg kein Segment sondern ein Punkt, was aber per Definition
//				// ausgeschlossen ist.
//				if(index != gleichung_l && !s[index].equals(Fraction.ZERO)){
//					// r[index] kann nicht 0 sein, denn sonst w�re u schon gefunden worden.
//					u[index] = a[index].add(l[gleichung_l].multiply(r[index]).subtract(b[index]).divide(s[index]));
//				}
//			}
//			
//			// Falls alle gefundenen u != null identisch sind, dann
//			// gibt es einen schnittpunkt, der sich durch einsetzen
//			// in der werte von l bzw. u in die entsprechende
//			// Vektorgeradengleichung ergibt.
//			for(int v = 0; v<2; v++){
//				for(int w = v+1; w<3; w++){
//					if(u[v] != null && u[w] != null && !u[v].equals(u[w])){
//						throw new NoIntersectionException("Kein Schnittpunkt der Geraden gefunden.");
//					}
//				}
//			}
//			
//			// W�hle eines der u != null aus
//			int index_u = -1;
//			for(int v = 0; v<3; v++){
//				if(u[v] != null){
//					index_u = v;
//					break;
//				}
//			}
//			
//			
//			// Wenn die Werte f�r l oder u nicht zwischen 0 und 1 liegen,
//			// dann liegt der Schnittpunkt au�erhalb der Segemente. Das liegt
//			// daran, dass die Richtungsvektoren r und s so gew�hlt wurden,
//			// dass sie den Segementen inkl. der L�nge entsprechen und dass
//			// als Aufpunkt jeweils der kleinere Punkt der Segmente gew�hlt wurde.
//			if(u[index_u].compareTo(Fraction.ZERO) < 0 || u[index_u].compareTo(Fraction.ONE) > 0 || l[gleichung_l].compareTo(Fraction.ZERO) < 0 || l[gleichung_l].compareTo(Fraction.ONE) > 0){
//				throw new NoIntersectionException("Geraden besitzen Schnittpunkt, der ausserhalb der Segemente liegt.");
//			}
//			else{
//				Fraction x = this.start.getX().add(l[gleichung_l].multiply(r1));
//				Fraction y = this.start.getY().add(l[gleichung_l].multiply(r2));
//				Fraction z = this.start.getZ().add(l[gleichung_l].multiply(r3));
//				return new Point3D(x, y, z);
//			}
//			
//		}
//		// u konnte bereits bestimmt werden, l jedoch nicht.
//		else if(!found_l && found_u){
//			// finde eine andere gleichung und bestimme l
//			for(int index = 0; index<3; index++){
//				// die nachfolgende Bedingung muss irgendwann erf�llt sein
//				// denn sonst w�ren alle Komponenten r = 0 und damit
//				// ware this kein Segment sondern ein Punkt, was aber per Definition
//				// ausgeschlossen ist.
//				if(index != gleichung_u && !r[index].equals(Fraction.ZERO)){
//					// r[index] kann nicht 0 sein, denn sonst w�re u schon gefunden worden.
//					l[index] = b[index].add(u[gleichung_u].multiply(s[index]).subtract(a[index])).divide(r[index]);
//				}
//			}
//			
//			// Falls alle gefundenen l != null identisch sind, dann
//			// gibt es einen schnittpunkt, der sich durch einsetzen
//			// in der werte von l bzw. u in die entsprechende
//			// Vektorgeradengleichung ergibt.
//			for(int v = 0; v<2; v++){
//				for(int w = v+1; w<3; w++){
//					if(l[v] != null && l[w] != null && !l[v].equals(l[w])){
//						throw new NoIntersectionException("Kein Schnittpunkt der Geraden gefunden.");
//					}
//				}
//			}
//			
//			// W�hle eines der l != null aus
//			int index_l = -1;
//			for(int v = 0; v<3; v++){
//				if(l[v] != null){
//					index_l = v;
//					break;
//				}
//			}
//			
//			
//			// Wenn die Werte f�r l oder u nicht zwischen 0 und 1 liegen,
//			// dann liegt der Schnittpunkt au�erhalb der Segemente. Das liegt
//			// daran, dass die Richtungsvektoren r und s so gew�hlt wurden,
//			// dass sie den Segementen inkl. der L�nge entsprechen und dass
//			// als Aufpunkt jeweils der kleinere Punkt der Segmente gew�hlt wurde.
//			if(l[index_l].compareTo(Fraction.ZERO) < 0 || l[index_l].compareTo(Fraction.ONE) > 0 || u[gleichung_u].compareTo(Fraction.ZERO) < 0 || u[gleichung_u].compareTo(Fraction.ONE) > 0){
//				throw new NoIntersectionException("Geraden besitzen Schnittpunkt, der ausserhalb der Segemente liegt.");
//			}
//			else{
//				Fraction x = this.start.getX().add(l[index_l].multiply(r1));
//				Fraction y = this.start.getY().add(l[index_l].multiply(r2));
//				Fraction z = this.start.getZ().add(l[index_l].multiply(r3));
//				return new Point3D(x, y, z);
//			}
//		}
//		// beide parameter konnten bereits bestimmt werden
//		else if(found_l && found_u){
//			// first check, if l and u are between 0 and 1
//			// if not the following checks have not to
//			// be done anymore, because the intersection
//			// point would be out of the segments interval
//			if(l[gleichung_l].compareTo(Fraction.ZERO) < 0 || l[gleichung_l].compareTo(Fraction.ONE) > 0 || u[gleichung_u].compareTo(Fraction.ZERO) < 0 || u[gleichung_u].compareTo(Fraction.ONE) > 0){
//				throw new NoIntersectionException("Straight lines have point of intersection that is outer the segments.");
//			}
//			
//			// pr�fe in der �brig gebliebenen gleichung,
//			// ob sie mit den Parameter-Werten zu gleichen
//			// Ergebnissen f�hren.
//			Fraction e1 = null;
//			Fraction e2 = null;
//			
//			for(int index = 0; index<3; index++){
//				if(index != gleichung_u && index != gleichung_l && 
//						!r[index].equals(Fraction.ZERO) && 
//						!s[index].equals(Fraction.ZERO)){
//					e1 = a[index].add(l[gleichung_l].multiply(r[index]));
//					e2 = b[index].add(u[gleichung_u].multiply(s[index]));
//					// Falls die Ergebnisse gleich sind, so bestimme
//					// den Schnittpunkt und liefere ihn zur�ck.
//					if(e1.equals(e2)){
//						Fraction x = this.start.getX().add(l[gleichung_l].multiply(r1));
//						Fraction y = this.start.getY().add(l[gleichung_l].multiply(r2));
//						Fraction z = this.start.getZ().add(l[gleichung_l].multiply(r3));
//						return new Point3D(x, y, z);
//					}
//					else{
//						throw new NoIntersectionException("Kein Schnittpunkt gefunden.");
//					}
//				}
//			}
//			// falls keine passende gleichung gefunden wurde, weil die �brig gebliebene
//			// Gleichung nur den Wert 0 f�r r und s enth�lt, so gibt es einen Schnittpunkt
//			if(e1 == null && e2 == null){
//				Fraction x = this.start.getX().add(l[gleichung_l].multiply(r1));
//				Fraction y = this.start.getY().add(l[gleichung_l].multiply(r2));
//				Fraction z = this.start.getZ().add(l[gleichung_l].multiply(r3));
//				return new Point3D(x, y, z);
//			}
//			else{
//				// Dieser Fall kann nicht eintreten, muss aber f�r den Compiler
//				// hinzugef�gt werden
//				throw new NoIntersectionException("Dieser Fall darf nicht auftreten.");
//			}
//		}
//		// Falls noch kein Parameter bestimmt werden konnte:
//		// In diesem Fall gibt es keine Gleichung, in der sowohl s als auch r
//		// gleich 0 sind.
//		else{
//			// Bestimme die Gleichung i des Gleichungssystems, in der ri != 0
//			int i = -1;
//			for(int index = 0; index<3; index++){
//				if(!r[index].equals(Fraction.ZERO)){
//					i = index;
//					break;
//				}
//			}
//			
//			// Bestimme die Gleichung j != i des Gleichungssystems, in der sj != 0 und sj != rj und sj != si
//			// Falls es keine solche Gleichung gibt, so sind die geraden parrallel
//			// Beachte, falls es eine Gleichung g�be, in der sj = 0 und rj != 0, dann w�re
//			// bereits zuvor das Gleichungssystem gel�st worden. Dieser Teil der if-Bedingung
//			// w�re also nie erreicht worden. Das bedeutet, sobald sj = 0 ist auch rj = 0 und
//			// man kann mit der Gleichung nichts anfangen.
//			int j = -1;
//			for(int index = 0; index<3; index++){
//				if(index != i && !s[index].equals(Fraction.ZERO) && !s[index].equals(r[i]) && !s[index].equals(s[i])){
//					j = index;
//					break;
//				}
//			}
//			
//			// Falls i oder j < 0 sind, so konnten keine passenden Gleichungen
//			// gefunden werden. In diesem Fall gibt es keinen Schnittpunkt.
//			if(i < 0 || j < 0){
//				throw new NoIntersectionException("Die Geraden sind parallel. Es gibt keinen Schnittpunkt.");
//			}
//			
//			// L�se Gleichung i nach l auf und setze in Gleichung j ein.
//			// L�se Gleichung j nach u auf.
//			
//			// Berechnung von u
//			Fraction bj_m_aj = b[j].subtract(a[j]);
//			Fraction rj_ml_si = r[j].multiply(s[i]);
//			Fraction sj_ml_ri = s[j].multiply(r[i]);
//			
//			Fraction sum1 = bj_m_aj.multiply(r[i]).divide(rj_ml_si.subtract(sj_ml_ri));
//			Fraction sum2 = b[i].divide(s[i].subtract(sj_ml_ri.divide(r[j])));
//			Fraction sum3 = a[i].divide(s[i].subtract(sj_ml_ri.divide(r[j])));
//			
//			Fraction u_j = sum1.subtract(sum2).add(sum3);
//		
//			// Berechnung von l, f�r die Gleichungen i, in denen r[i] != 0
//			for(int index = 0; index<3; index++){
//				if(!r[index].equals(Fraction.ZERO)){
//					l[index] = b[index].add(u_j.multiply(s[index])).subtract(a[index]).divide(r[index]);
//				}
//			}
//			
//			// Wenn zwei Werte f�r l != null verschieden sind, dann gibt es keinen Schnittpunkt
//			for(int v = 0; v<2; v++){
//				for(int w = v+1; w<3; w++){
//					if(l[v] != null && l[w] != null && !l[v].equals(l[w])){
//						throw new NoIntersectionException("Kein Schnittpunkt der Geraden gefunden.");
//					}
//				}
//			}
//			
//			// W�hle eines der l != null aus
//			int index_l = -1;
//			for(int v = 0; v<3; v++){
//				if(l[v] != null){
//					index_l = v;
//					break;
//				}
//			}
//			
//			
//			// Wenn die Werte f�r l oder u nicht zwischen 0 und 1 liegen,
//			// dann liegt der Schnittpunkt au�erhalb der Segemente. Das liegt
//			// daran, dass die Richtungsvektoren r und s so gew�hlt wurden,
//			// dass sie den Segement inkl. der L�nge entsprechen und dass
//			// als Aufpunkt jeweils der kleinere Punkt der Segmente gew�hlt wurde.
//			if(u_j.compareTo(Fraction.ZERO) < 0 || u_j.compareTo(Fraction.ONE) > 0 || l[index_l].compareTo(Fraction.ZERO) < 0 || l[index_l].compareTo(Fraction.ONE) > 0){
//				throw new NoIntersectionException("Geraden besitzen Schnittpunkt, der ausserhalb der Segemente liegt.");
//			}
//			else{
//				Fraction x = this.start.getX().add(l[index_l].multiply(r1));
//				Fraction y = this.start.getY().add(l[index_l].multiply(r2));
//				Fraction z = this.start.getZ().add(l[index_l].multiply(r3));
//				return new Point3D(x, y, z);
//			}
//		}
//	}
	
	/**
	 * A segment intersects a facet if there exists an intersection
	 * point.
	 */
	public boolean intersects(Facet3D f){
		return false; //SpatialFraction.intersect(this, f);
	}
	
	/**
	 * Two segments intersect, if there is an intersection point that is different
	 * from the endpoints, so that the segments do not meet or touch
	 * @param s
	 * @return True, if this object and s intersect, false otherwise
	 */
	public boolean intersects(Segment3D s){
		return false; //SpatialFraction.intersect(this, s);
	}
	
	/**
	 * A segment intersects with a line, if it intersects
	 * with at least one of the line's segments.
	 */
	public boolean intersects(Line3D l){
		return false; //SpatialFraction.intersect(this, l);
	}
	
	/**
	 * A segment intersects a solid, if it intersects with a least
	 * two facets of the solid.
	 */
	public boolean intersects(Solid3D s){
		return false; //SpatialFraction.intersect(this, s);
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
	 * differ. In this case the direction of this object
	 * will be taken.
	 */
	public Segment3D combine(Segment3D s){
		return null; //SpatialFraction.combine(this, s);
	}
	
	/**
	 * determines the distance between two
	 * segments. The distance is the distance
	 * between two straight lines if possible or
	 * if the corresponding touching points are outside
	 * the segments, it is the minimal distance
	 * between two endpoints of the segments. 
	 * @return A double value representing the distance between
	 * the to segments. The distance cannot be
	 * represented as a Fraction. Think of the vector (1, 1, 1)
	 * whose length ist sqrt(3).
	 */
	public double distance(Segment3D s){
//		// the start points are the on points of the corresponding
//		// straight lines
//	    // the direction vectors are from the on points to the other
//		// points
//		
//		// first straight line
//		Fraction[] a = new Fraction[3];
//		a[0] = this.start.getX();
//		a[1] = this.start.getY();
//		a[2] = this.start.getZ();
//		
//		
//		Fraction[] r = new Fraction[3];
//		r[0] = this.end.getX().subtract(this.start.getX());
//		r[1] = this.end.getY().subtract(this.start.getY());
//		r[2] = this.end.getZ().subtract(this.start.getZ());
//		
//		
//		// second straight line
//		Fraction[] b = new Fraction[3];
//		b[0] = s.start.getX();
//		b[1] = s.start.getY();
//		b[2] = s.start.getZ();
//		
//		Fraction[] u = new Fraction[3];
//		u[0] = s.end.getX().subtract(s.start.getX());
//		u[1] = s.end.getY().subtract(s.start.getY());
//		u[2] = s.end.getZ().subtract(s.start.getZ());
//		
//		Fraction[] minus_u = new Fraction[3];
//		minus_u[0] = u[0].multiply(new Fraction(-1, 1));
//		minus_u[1] = u[1].multiply(new Fraction(-1, 1));
//		minus_u[2] = u[2].multiply(new Fraction(-1, 1));
//		
//		
//		// determine the vector, that is orthogonal to both straight lines
//		Matrix3D r_matrix = new Matrix3D(r);
//		Matrix3D u_matrix = new Matrix3D(minus_u);
//		
//		Matrix3D d_matrix = Matrix3D.vectorProduct(r_matrix, u_matrix);
//		Fraction[] d = new Fraction[3];
//		d[0] = d_matrix.getValue(0, 0);
//		d[1] = d_matrix.getValue(0, 1);
//		d[2] = d_matrix.getValue(0, 2);
//		
//		// now you can use the following equation, to determine
//		// points, from which to change from one straight line
//		// to other one.
//		// The straight lines are as following
//		// 1: a + lr
//		// 2: b + iu
//		// 3: d is the direction that is orthogonal to both
//		// straight lines
//		// Now the following condition must hold to switch lines
//		// when walking on the lines and the direction vector
//		// a + lr + vd = b + iu
//		// and therefor
//		// lr + vd + iu = b - a must hold
//		// This is an equation systems of three equations
//		// with three variables, so solve it.
//		
//		// determine the denominator determinate
//		Matrix3D denM = new Matrix3D(3, 3);
//		denM.setValue(0, 0, r[0]);
//		denM.setValue(1, 0, d[0]);
//		denM.setValue(2, 0, minus_u[0]);
//		
//		denM.setValue(0, 1, r[1]);
//		denM.setValue(1, 1, d[1]);
//		denM.setValue(2, 1, minus_u[1]);
//		
//		denM.setValue(0, 2, r[2]);
//		denM.setValue(1, 2, d[2]);
//		denM.setValue(2, 2, minus_u[2]);
//		
//		Fraction denDet = denM.det();
//		
//		if(denDet.compareTo(Fraction.ZERO) == 0){
//			throw new SpatialException("Something is wrong when determining the distance between two segments.");
//		}
//		
//		// determine l
//		Matrix3D lm = new Matrix3D(3, 3);
//		lm.setValue(0, 0, b[0].subtract(a[0]));
//		lm.setValue(1, 0, d[0]);
//		lm.setValue(2, 0, minus_u[0]);
//		
//		lm.setValue(0, 1, b[1].subtract(a[1]));
//		lm.setValue(1, 1, d[1]);
//		lm.setValue(2, 1, minus_u[1]);
//		
//		lm.setValue(0, 2, b[2].subtract(a[2]));
//		lm.setValue(1, 2, d[2]);
//		lm.setValue(2, 2, minus_u[2]);
//		
//		Fraction l = lm.det().divide(denDet);
//		// l must be 0 <= l <= 1
//		if(l.compareTo(Fraction.ZERO) >= 0 && l.compareTo(Fraction.ONE) <= 0){
//			// determine i
//			Matrix3D im = new Matrix3D(3, 3);
//			im.setValue(0, 0, r[0]);
//			im.setValue(1, 0, d[0]);
//			im.setValue(2, 0, b[0].subtract(a[0]));
//			
//			im.setValue(0, 1, r[1]);
//			im.setValue(1, 1, d[1]);
//			im.setValue(2, 1, b[1].subtract(a[1]));
//			
//			im.setValue(0, 2, r[2]);
//			im.setValue(1, 2, d[2]);
//			im.setValue(2, 2, b[2].subtract(a[2]));
//			
//			Fraction i = im.det().divide(denDet);
//			
//			// i must be 0 <= i <= 1
//			if(i.compareTo(Fraction.ZERO) >= 0 && i.compareTo(Fraction.ONE) <= 0){
//				// determine v
//				Matrix3D vm = new Matrix3D(3, 3);
//				vm.setValue(0, 0, r[0]);
//				vm.setValue(1, 0, b[0].subtract(a[0]));
//				vm.setValue(2, 0, minus_u[0]);
//				
//				vm.setValue(0, 1, r[1]);
//				vm.setValue(1, 1, b[1].subtract(a[1]));
//				vm.setValue(2, 1, minus_u[1]);
//				
//				vm.setValue(0, 2, r[2]);
//				vm.setValue(1, 2, b[2].subtract(a[2]));
//				vm.setValue(2, 2, minus_u[2]);
//				
//				Fraction v = vm.det().divide(denDet);
//				
//				// now the distance can be calculated
//				// It is the length of the vector d multiplied
//				// with v.
//				
//				double dlength = Math.sqrt(d[0].multiply(d[0]).add(d[1].multiply(d[1])).add(d[2].multiply(d[2])).doubleValue());
//				
//				double dist = Math.abs(v.doubleValue() * dlength);
//				return dist;
//				
//			}
//		}
//		
//		// the distance could not be determined,
//		// because it would lie out of the
//		// segments. So determine the distance of
//		// an endpoint of the one segment
//		// to the other segment. Do it for all
//		// four endpoints. If the l in the following
//		// equations is 0 <= l <= 1, then such
//		// a distance exists. If not you can determine
//		// the shortest distance between two endpoints
//		// of the segments.
//	
//		double[] dist = {-1.0, -1.0, -1.0, -1.0};
//		
//		// dist(this.start, s)
//		// use a and b and u
//		Fraction[] a_m_b = new Fraction[3];
//		a_m_b[0] = a[0].subtract(b[0]);
//		a_m_b[1] = a[1].subtract(b[1]);
//		a_m_b[2] = a[2].subtract(b[2]);
//		
//		Fraction[] b_m_a = new Fraction[3];
//		b_m_a[0] = b[0].subtract(a[0]);
//		b_m_a[1] = b[1].subtract(a[1]);
//		b_m_a[2] = b[2].subtract(a[2]);
//		
//		Fraction[] x = new Fraction[3];
//		Fraction u_length = u[0].multiply(u[0]).add(u[1].multiply(u[1])).add(u[2].multiply(u[2]));
//		Fraction r_length = r[0].multiply(r[0]).add(r[1].multiply(r[1])).add(r[2].multiply(r[2]));
//		
//		// l = a_m_b * u / |a|^2
//		l = FractionUtils.scalarMultiply(a_m_b, u);
//		l = l.divide(u_length);
//		
//		if(l.compareTo(Fraction.ZERO) >= 0 && l.compareTo(Fraction.ONE) <= 0){
//			x = FractionUtils.add(b_m_a, FractionUtils.multiply(l, u));
//			dist[0] = FractionUtils.length(x);
//		}
//		
//		// dist(this.end, s)
//		Fraction[] end_m_b = FractionUtils.subtract(this.end, s.start);
//		Fraction[] b_m_end = FractionUtils.subtract(s.start, this.end);
//		
//		l = FractionUtils.scalarMultiply(end_m_b, u);
//		l = l.divide(u_length);
//		
//		if(l.compareTo(Fraction.ZERO) >= 0 && l.compareTo(Fraction.ONE) <= 0){
//			x = FractionUtils.add(b_m_end, FractionUtils.multiply(l, u));
//			dist[1] = FractionUtils.length(x);
//		}
//		
//		// dist(s.start, this)
//		Fraction[] start_m_a = FractionUtils.subtract(s.start, this.start);
//		Fraction[] a_m_start = FractionUtils.subtract(this.start, s.start);
//		
//		l = FractionUtils.scalarMultiply(start_m_a, r);
//		l = l.divide(r_length);
//		
//		if(l.compareTo(Fraction.ZERO) >= 0 && l.compareTo(Fraction.ONE) <= 0){
//			x = FractionUtils.add(a_m_start, FractionUtils.multiply(l, r));
//			dist[2] = FractionUtils.length(x);
//		}
//		
//		// dist(s.end, this)
//		Fraction[] end_m_a = FractionUtils.subtract(s.end, this.start);
//		Fraction[] a_m_end = FractionUtils.subtract(this.start, s.end);
//		
//		l = FractionUtils.scalarMultiply(end_m_a, r);
//		l = l.divide(r_length);
//		
//		if(l.compareTo(Fraction.ZERO) >= 0 && l.compareTo(Fraction.ONE) <= 0){
//			x = FractionUtils.add(a_m_end, FractionUtils.multiply(l, r));
//			dist[3] = FractionUtils.length(x);
//		}
//		
//		// return the lowest distance that is greater than zero
//		double ret_val = Double.MAX_VALUE;
//		for(int i = 0; i<dist.length; i++){
//			if(dist[i] >= 0 && dist[i] < ret_val){
//				ret_val = dist[i];
//			}
//		}
//		
//		if(ret_val < Double.MAX_VALUE){
//			return ret_val;
//		}
//		
//		// no distance could be found so determine
//		// the smallest distance between two endpoints.
//		
//		// first this.start, s.end
//		d[0] = s.end.getX().subtract(this.start.getX());
//		d[1] = s.end.getY().subtract(this.start.getY());
//		d[2] = s.end.getZ().subtract(this.start.getZ());
//		
//		double dist1 = Math.sqrt(d[0].multiply(d[0]).add(d[1].multiply(d[1])).add(d[2].multiply(d[2])).doubleValue());
//		
//		// second: this.start, s.start
//		d[0] = s.start.getX().subtract(this.start.getX());
//		d[1] = s.start.getY().subtract(this.start.getY());
//		d[2] = s.start.getZ().subtract(this.start.getZ());
//		
//		double dist2 = Math.sqrt(d[0].multiply(d[0]).add(d[1].multiply(d[1])).add(d[2].multiply(d[2])).doubleValue());
//		
//		// third: this.end, s.start
//		d[0] = s.start.getX().subtract(this.end.getX());
//		d[1] = s.start.getY().subtract(this.end.getY());
//		d[2] = s.start.getZ().subtract(this.end.getZ());
//		
//		double dist3 = Math.sqrt(d[0].multiply(d[0]).add(d[1].multiply(d[1])).add(d[2].multiply(d[2])).doubleValue());
//
//		// fourth: this.end, s.end
//		d[0] = s.end.getX().subtract(this.end.getX());
//		d[1] = s.end.getY().subtract(this.end.getY());
//		d[2] = s.end.getZ().subtract(this.end.getZ());
//		
//		double dist4 = Math.sqrt(d[0].multiply(d[0]).add(d[1].multiply(d[1])).add(d[2].multiply(d[2])).doubleValue());
//		
//		return Math.min(Math.min(Math.min(dist1, dist2), dist3), dist4);		
		
		return 0.0;
	}
	
	
//	/**
//	 * Der Schnittpunkt wird durch drei Projektionen berechnet:
//	 * 
//	 * 1. Projektion auf die xy-Ebene
//	 * 2. Projektion auf die xz-Ebene
//	 * 3. Projektion auf die yz-Ebene
//	 * 
//	 * Die beiden Segemente sind gegeben durch folgende Punkte
//	 * this = (p1 = (x1, y1, z2), p2 = (x2, y2, z2))
//	 * s = (p3 = (x3, y3, z3), p4 = (x4, y4, z4))
//	 * 
//	 * @return Schnittpunkt der beiden Geraden im Raum
//	 * @throws NoIntersectionException falls kein Schnittpunkt gefunden werden kann.
//	 */
//	public Point3D intersection_old2(Segment3D s) throws NoIntersectionException{
//		Fraction x = null;
//		Fraction y = null;
//		Fraction z = null;
//		
//		// Projektion auf die xy-Ebene
//		
//		// 1. Pr�fe ob die beiden Geraden evtl. senkrecht stehen
//		// falls ja, dann m�ssen die x-Werte gleich sein und der
//		// x-Wert des Schnittpunktes steht fest
//		// falls nein, dann berechne den Schnittpunkt der beiden geraden
//		
//		// Alle x-Koordinaten sind gleich
//		if(this.lower.getX().equals(this.higher.getX()) && s.lower.getX().equals(s.higher.getX()) && this.lower.getX().equals(s.lower.getX())){
//			
//			// Mindestens ein Segment muss unterschiedliche y-Koordinaten haben, sonst liegen die Geraden
//			// aufeinander.
//			if(!this.lower.getY().equals(this.higher.getY()) || !s.lower.getY().equals(s.higher.getY())){
//				x = new Fraction(this.lower.getX().getNumerator(), this.lower.getX().getDenominator());
//			}
//			// Sind nur die Y-Koordinaten eines Segments unterschiedlich so sind Geraden windschief
//			// sind die Y-Koordinaten keines Segements unterschiedlich, so liegen die Geraden aufeinander
//			// und ragen in die xz-Ebene hinein.
//			else{
//				throw new NoIntersectionException("Die Segmente liegen entweder aufeinander oder folgen in derselben Richtung aufeinander.");
//			}
//		}
//		// Die Segmente sind senkrecht, liegen jedoch nicht aufeinander
//		else if(this.lower.getX().equals(this.higher.getX()) && s.lower.getX().equals(s.higher.getX()) && !this.lower.getX().equals(s.lower.getX())){
//			throw new NoIntersectionException("Kein Schnitt auf der XY-Projektion.");
//		}
//		
//		// Eines der Segmente ist nicht senkrecht
//		else if(this.lower.getX().equals(this.higher.getX()) && !s.lower.getX().equals(s.higher.getX())){
//			
//			// Falls beide Segmente jeweils unterschiedliche y-Koordinaten haben, l�sst der 
//			// x,y-Schnittpunkt bestimmen
//			if(this.lower.getY().equals(this.higher.getY()) && !s.lower.getX().equals(s.higher.getY())){
//			
//				x = new Fraction(this.lower.getX().getNumerator(), this.lower.getX().getDenominator());
//				
//				// Der y-Wert l�sst sich jetzt bestimmen, indem man den x-Wert in die Gleichung der nicht senkrechten
//				// Gerade einsetzt.
//				Fraction x4_m_x3 = s.higher.getX().subtract(s.lower.getX());
//				Fraction y4_m_y3 = s.higher.getY().subtract(s.lower.getY());
//				Fraction m_xy_1 = y4_m_y3.divide(x4_m_x3);
//				
//				Fraction b1 = s.lower.getY().subtract(m_xy_1.multiply(s.lower.getX()));
//				
//				y = m_xy_1.multiply(x).add(b1);
//			}
//			
//			
//		}
//		else if(!this.lower.getX().equals(this.higher.getX()) && s.lower.getX().equals(s.higher.getX())){
//			x = new Fraction(s.lower.getX().getNumerator(), s.lower.getX().getDenominator());
//		}
//		// die Geraden stehen beide nicht senkrecht.
//		else{
//			Fraction x2_m_x1 = this.higher.getX().subtract(this.lower.getX());
//			Fraction y2_m_y1 = this.higher.getY().subtract(this.lower.getY());
//			Fraction m_xy_1 = y2_m_y1.divide(x2_m_x1);
//			
//			Fraction x4_m_x3 = s.higher.getX().subtract(s.lower.getX());
//			Fraction y4_m_y3 = s.higher.getY().subtract(s.lower.getY());
//			Fraction m_xy_2 = y4_m_y3.divide(x4_m_x3);
//			
//			Fraction b1 = this.lower.getY().subtract(m_xy_1.multiply(this.lower.getX()));
//			Fraction b2 = s.lower.getY().subtract(m_xy_2.multiply(s.lower.getX()));
//			
//			Fraction b2_m_b1 = b2.subtract(b1);
//			Fraction m1_m_m2 = m_xy_1.subtract(m_xy_2);
//			
//			x = b2_m_b1.divide(m1_m_m2);
//			y = m_xy_1.multiply(x).add(b1);
//		}
//		
//		// Projektion auf die xz-Ebene
//		
//		// falls die Geraden wieder senkrecht liegen, l�sst sich der z-Wert nicht
//		// bestimmen und man muss auf die Projektion der 
//		
//		
//	}
//	
//	
//	/**
//	 * see Weinrich et. al 2005
//	 * 
//	 * intersection: segment x segment -> point
//	 * 
//	 * The formular has been made more simple, to avoid calculating
//	 * square roots
//	 */
//	public Point3D intersection_old(Segment3D s){
//		// first calculating the x coordinate of the intersection point
//		
//		// zaehler
//		Fraction x2_m_x1 = this.higher.getX().subtract(this.lower.getX());
//		Fraction x4_m_x3_m_y4_p_y3 = s.higher.getX().subtract(s.lower.getX()).subtract(s.higher.getY()).add(s.lower.getY());
//		
//		// nenner
//		Fraction m_x2_p_x1 = this.higher.getX().multiply(new Fraction(-1, 1)).add(this.lower.getX());
//		Fraction y4_m_y3 = s.higher.getY().subtract(s.lower.getY());
//		
//		Fraction x4_m_x3 = s.higher.getX().subtract(s.lower.getX());
//		Fraction y2_m_y1 = this.higher.getY().subtract(this.lower.getY());
//		
//		Fraction zaehler = x2_m_x1.multiply(x4_m_x3_m_y4_p_y3);
//		Fraction nenner = m_x2_p_x1.multiply(y4_m_y3).add(x4_m_x3.multiply(y2_m_y1));
//		
//		Fraction bruch = zaehler.divide(nenner);
//		
//		Fraction x = this.lower.getX().add(bruch);
//		
//		// second calculating the y coordinate of the intersection point
//		
//		// zaehler
//		Fraction zaehler_y = y2_m_y1.multiply(x4_m_x3_m_y4_p_y3);
//		Fraction nenner_y = m_x2_p_x1.multiply(y4_m_y3).add(x4_m_x3.multiply(y2_m_y1));
//		
//		Fraction bruch_y = zaehler_y.divide(nenner_y);
//		Fraction y = this.lower.getY().add(bruch_y);
//		
//		// third calculating the z coordinate of the intersection point
//		Fraction z2_m_z1 = this.higher.getZ().subtract(this.lower.getZ());
//		
//		// zaehler
//		Fraction zaehler_z = z2_m_z1.multiply(x4_m_x3_m_y4_p_y3);
//		Fraction nenner_z = m_x2_p_x1.multiply(y4_m_y3).add(x4_m_x3.multiply(y2_m_y1));
//		
//		Fraction bruch_z = zaehler_z.divide(nenner_z);
//		
//		Fraction z = this.lower.getZ().add(bruch_z);
//		
//		return new Point3D(x, y, z);
//	}
	
	public Point3D getStart(){
		return this.start;
	}
	
	public void setStart(Point3D lower){
		if(this.end != null && lower.equals(this.end)){
			throw new IllegalArgumentException("A segment can only be defined by two different points. If you want to" +
					"redefine start and end, set end to null, before setting a new start point.");
		}
		this.start = lower;
	}
	
	public Point3D getEnd(){
		return this.end;
	}
	
	public void setEnd(Point3D higher){
		if(this.start != null && higher.equals(this.start)){
			throw new IllegalArgumentException("A segment can only be defined by two different points. If you want to" +
					"redefine start and end, set start to null, before setting a new end point.");
		}
		this.end = higher;
	}
	
	public String toString(){
		return "Segment: p1 = {" + this.start.toString() + "}, p2 = {" + this.end.toString() + "}";
	}
	
	public int compareTo(Segment3D s){
		return 0;
//		int compLower = this.start.compareTo(s.start);
//		return compLower == 0 ? this.end.compareTo(s.end) : compLower;
	}
	
	public static void main(String[] args){
//		// Testing intersection method
//		Point3D p1 = new Point3D(new Fraction(0, 1), new Fraction(0, 1), new Fraction(0, 1));
//		Point3D p2 = new Point3D(new Fraction(2, 1), new Fraction(0, 1), new Fraction(0, 1));
//		Point3D p3 = new Point3D(new Fraction(3, 1), new Fraction(1, 1), new Fraction(1, 1));
//		Point3D p4 = new Point3D(new Fraction(3, 1), new Fraction(1, 1), new Fraction(2, 1));
//		
//		Segment3D s1 = new Segment3D(p1, p2);
//		
//		Segment3D s2 = new Segment3D(p3, p4);
//		
//		System.out.println("s1: " + s1);
//		System.out.println("s2: " + s2);
//		
//		try{
//			long t1 = System.currentTimeMillis();
////			Point3D inter = s2.intersection(s1);
//			long t2 = System.currentTimeMillis();
////			System.out.println("Intersection: " + inter.toString());
//			System.out.println("Dauer der Berechnung: " + (t2 - t1));
//			System.out.println("Coplanar: " + s1.coplanar(s2));
//			System.out.println("Touches: " + s1.touches(s2));
//			System.out.println("Meets: " + s1.meets(s2));
//			System.out.println("Distance: " + s1.distance(s2));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}

}
