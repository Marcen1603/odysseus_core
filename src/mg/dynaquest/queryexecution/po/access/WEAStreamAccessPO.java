package mg.dynaquest.queryexecution.po.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.streaming.object.PointInTime;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.queryexecution.po.streaming.object.TimeInterval;

import org.w3c.dom.NodeList;

public class WEAStreamAccessPO extends PhysicalAccessPO {

	private BufferedReader iStream;

	public WEAStreamAccessPO() {
		super();
		iStream = null;
	}
	
	public WEAStreamAccessPO(AccessPO algebraPo) {
		super(algebraPo);
		iStream = null;
	}

	public WEAStreamAccessPO(WEAStreamAccessPO accessPO) {
		super(accessPO);
		// Fraglich ob gewuenscht. Wenn Stream in einer Kopie geschlossen wird,
		// wird er auch in allen anderen geschlossen.
		// Ist gewünscht ;-) --> Erzeugen von Anfrageplänen benötigt clonen
		this.iStream = accessPO.iStream;
	}

	@Override
	public String getInternalPOName() {
		return "WEAStreamAccesPO";
	}

	public void setInputStream(InputStreamReader iStream) {
		if (this.iStream == null) {
			synchronized (this) {
				if (this.iStream == null) {
					this.iStream = new BufferedReader(iStream);
				} else {
					throw new RuntimeException("Inputstream is already set");
				}
			}
		} else {
			throw new RuntimeException("Inputstream is already set");
		}
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
		xmlRetValue.append(indent).append("<algebraPO>\n");
    	getAccessPO().getInternalXMLRepresentation(baseIndent,indent+indent,xmlRetValue);
    	xmlRetValue.append(indent).append("</algebraPO>\n");
	}
	
	public SupportsCloneMe cloneMe() {
		return new WEAStreamAccessPO(this);
	}

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {

	}

	@Override
	protected boolean process_close() throws POException {
		try {
			synchronized (iStream) {
				this.iStream.close();
			}
		} catch (IOException e) {
			throw new POException(e);
		}
		return true;
	}

	@Override
	protected Object process_next() throws POException, TimeoutException {
		try {
			// TODO funktioniert nicht, wenn ein String enthalten ist, der das
			// Trennzeichen enthaelt
			synchronized (iStream) {
				return new StreamExchangeElement<RelationalTuple>(
						new RelationalTuple(iStream.readLine(), ','), new TimeInterval(
								new PointInTime(System.currentTimeMillis(),0), 
								new PointInTime())
						);
			}
		} catch (IOException e) {
			throw new POException(e);
		}
	}

	@Override
	protected boolean process_open() throws POException {
		if (this.iStream == null) {
			throw new POException("Inputstream is not set");
		}
		return true;
	}

}
