package mg.dynaquest.queryexecution.po.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.OOAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.streaming.object.PointInTime;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.queryexecution.po.streaming.object.TimeInterval;

import org.w3c.dom.NodeList;

/**
 * Zugriff auf einen ObjectInputStream.
 * 
 * Jedes eingehende Element wird in ein StreamExchangeElement<Map<String, Object>> verpackt.
 * Die Map enthaelt genau das eingehende Element als Objekt unter dem Key {@link #objectName}, welcher
 * im C'tor gesetzt wird und im Anfrageplan eindeutig sein muss.
 *  
 * @author Jonas Jacobi
 */
public class OOStreamAccessPO extends PhysicalAccessPO<OOAccessPO> {

	ObjectInputStream iStream;

	public OOStreamAccessPO() {
		this.iStream = null;
	}

	public OOStreamAccessPO(OOStreamAccessPO accessPO2) {
		super(accessPO2);
		this.iStream = accessPO2.iStream;
	}

	public OOStreamAccessPO(OOAccessPO algebraPO) {
		super(algebraPO);
		this.iStream = null;
	}
	
	public void setInputStream(ObjectInputStream iStream) {
		if (this.iStream == null) {
			synchronized (this) {
				if (this.iStream == null) {
					this.iStream = iStream;
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

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
		// TODO Auto-generated method stub
	}

	@Override
	protected boolean process_close() throws POException {
		try {
			this.iStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	protected Object process_next() throws POException, TimeoutException {
		Object o;
		try {
			o = iStream.readObject();
		} catch (Exception e) {
			throw new POException(e);
		}
		Map<String, Object> cargo = new HashMap<String, Object>();
		cargo.put(((OOAccessPO)getAccessPO()).getAsName(), o);
		return new StreamExchangeElement<Map<String, Object>>(cargo, new TimeInterval(
				new PointInTime(System.currentTimeMillis(), 0),
				new PointInTime()));
	}

	@Override
	protected boolean process_open() throws POException {
		return true;
	}

	public SupportsCloneMe cloneMe() {
		return new OOStreamAccessPO(this);
	}

}
