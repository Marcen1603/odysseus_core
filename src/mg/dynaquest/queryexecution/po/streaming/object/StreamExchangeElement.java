package mg.dynaquest.queryexecution.po.streaming.object;

public class StreamExchangeElement<T> implements Comparable<StreamExchangeElement>{

	T cargo;
	TimeInterval validity; 
	
	public StreamExchangeElement(T cargo, TimeInterval validity) {
		super();
		this.cargo = cargo;
		this.validity = validity;
	}

	public T getCargo() {
		return cargo;
	}

	public void setCargo(T cargo) {
		this.cargo = cargo;
	}

	public TimeInterval getValidity() {
		return validity;
	}

	public void setValidity(TimeInterval validity) {
		this.validity = validity;
	}

	public int compareTo(StreamExchangeElement elem) {
		return this.validity.compareTo(elem.getValidity());
	}
	
	public boolean isValid(PointInTime p, long rowNumber) {
		return validity.includes(p);
	}
}
