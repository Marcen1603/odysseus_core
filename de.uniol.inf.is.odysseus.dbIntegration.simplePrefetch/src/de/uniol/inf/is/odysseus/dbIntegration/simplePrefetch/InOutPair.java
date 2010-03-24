package de.uniol.inf.is.odysseus.dbIntegration.simplePrefetch;

/**
 * Diese Klasse wird dazu genutzt, aufeinanderfolgende Datenstromtupel zu speichern
 * und aus ihnen Attribute und Klassen fuer Instance-Objekte zu erzeugen.
 * 
 * @author crolfes
 *
 * @param <T> - das eingehende Tupel
 * @param <S> - das nachfolgende Tupel
 */
public class InOutPair<T, S> {

	public InOutPair(T in, S out) {
		this.in = in;
		this.out = out;
	}

	public T getIn() {
		return in;
	}

	public S getOut() {
		return out;
	}

	public String toString() {
		return "(" + in.toString() + ", " + out.toString() + ")";
	}

	private final T in;
	private final S out;
}
