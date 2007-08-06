package mg.dynaquest.queryexecution.po.streaming.object;

// TODO: Noch mal ber die Grenzen nachdenken (Wann <=, wann <)
// TODO: Im Moment ist das Intervall geschlossen, offene Intervalle notwendig?
// TODO: Gibt es evtl. effizientere Algorithmen?

/** Klasse, mit deren Hilfe ein diskretes (!) Intervall ber zwei Zeitpunkte (PointInTime) definiert werden kann
 *  die linke Grenze muss immer kleiner oder gleich der rechten Grenze sein
 *  
 */

public class TimeInterval implements Comparable<TimeInterval>{
	
	private PointInTime start;
	private PointInTime end;
	
	public TimeInterval(PointInTime start, PointInTime end) {
		init(start,end);
	}
	
	public TimeInterval(PointInTime start) {
		init(start, PointInTime.getInfinityTime());
	}
	
	private void init(PointInTime start, PointInTime end){
		if (!start.before(end)) {
			throw new IllegalArgumentException("start point is not before end point in time interval");
		}
		this.start = start;
		setEnd(end);
	}
	
	public boolean isEndInfinite(){
		return end.isInfinite();
	}
	
	public void setEnd(PointInTime end){
		if (start.after(end)) throw new IllegalArgumentException("Intervall startelement after endelement!");
		this.end = end;
	}
	
	public boolean isPoint(){
		return start.equals(end);
	}
	
	public boolean includes(PointInTime p) {
		return p.before(this.end) && p.afterOrEquals(this.start);
	}
	
	public static boolean startsBefore(TimeInterval left, TimeInterval right){
		return left.start.before(right.start);
	}
	
	/** Liegt mindestens der Startzeitpunkt von links vor dem Startzeitpunkt von rechts und
	 * der Endzeitpunkt von links von dem Endzeitpunkt von rechts (Achtung, Intervall muss sich nicht 
	 * berschneiden!)
	 * @param left linkes Intervall
	 * @param right rechtes Intervall
	 * @return true, wenn sowohl der Startzeitpunkt von left vor dem Startzeitpunkt von right liegt und
	 * der Endzeitpunkt von left vor dem Endzeitpunkt von right liegt
	 */
	public static boolean totallyBefore(TimeInterval left, TimeInterval right){
		return left.end.beforeOrEquals(right.start);
	}
	
	/** Berhren sich die beiden Intervall an einer der beiden Grenzen
	 * 
	 * @param left Linkes Intervall
	 * @param right Rechtes Intervall
	 * @return true, wenn das Ende von left und das Start von right oder wenn
	 * der Start von left und das Ende von right zusammenfallen
	 */
	public static boolean meets(TimeInterval left, TimeInterval right){
		return left.end == right.start || left.start == right.end;
	}
	
	public static boolean totallyAfter(TimeInterval left, TimeInterval right){
		return totallyBefore(right, left);
	}
	
	public static boolean overlaps(TimeInterval left, TimeInterval right){
		return !(totallyBefore(left,right) || totallyAfter(left,right));
	}
	
	/** Liegt das linke Intervall innerhalb des rechten 
	 * 
	 * @param left Linkes Intervall
	 * @param right Rechtes Intervall
	 * @return true, wenn left inside right, false sonst
	 */
	public static boolean inside(TimeInterval left, TimeInterval right){
		return right.start.beforeOrEquals(left.start) && left.end.beforeOrEquals(right.end); 
	}
	
	public static TimeInterval intersection(TimeInterval left, TimeInterval right){
		if (overlaps(left, right)){
			return new TimeInterval(PointInTime.max(left.start, right.start), PointInTime.min(left.end, right.end));
		}
		return null;		
	}
	
	public static TimeInterval union(TimeInterval left, TimeInterval right){
		if (overlaps(left, right)){
			return new TimeInterval(PointInTime.min(left.start, right.start), PointInTime.max(left.end, right.end));
		}
		return null;		
	}
	
	public static TimeInterval[] difference(TimeInterval left, TimeInterval right){
		TimeInterval[] ret = null; 
		if (overlaps(left, right) && !inside(left,right)){
			ret = new TimeInterval[2];
			if (totallyAfter(left, right)){
				ret[0] = new TimeInterval(right.end, left.end);
				return ret;
			}
			if (left.start.before(right.start)  && left.end.beforeOrEquals(right.end)){
				ret[0] = new TimeInterval(left.start, right.start);
				return ret;
			}
			if (inside(right, left)){ 
				// Geht hier, da left nicht vollstndig in right liegt (s.o).
				// Ansonsten mssten noch die Grenzen berprft werden
				ret[0] = new TimeInterval(left.start, right.start);
				ret[1] = new TimeInterval(right.end, left.end);
				return ret;
			}
		}
		return ret;		
	}

	/** Beim Vergleich werden zunchst die Startzeitpunkte und dann die Endzeitpunkte der Intervalle betrachtet
	 * 
	 */
	public int compareTo(TimeInterval toCompare) {
		int s = this.start.compareTo(toCompare.getStart());
		if (s == 0){ // Wenn Startpunkte gleich sind, die Endpunkte vergleichen
			s = this.end.compareTo(toCompare.getEnd());
		}
		return s;
	}

	public PointInTime getStart() {
		return start;
	}

	public void setStart(PointInTime start) {
		this.start = start;
	}

	public PointInTime getEnd() {
		return end;
	}
	
	public String toString () {
		return "[" + getStart().toString() + "," + getEnd().toString() + ")";
	}

}
