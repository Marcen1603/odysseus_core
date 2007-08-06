package mg.dynaquest.queryexecution.po.streaming.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.base.BinaryPlanOperator;
import mg.dynaquest.queryexecution.po.streaming.object.DynamicStreamBuffer;
import mg.dynaquest.queryexecution.po.streaming.object.PointInTime;
import mg.dynaquest.queryexecution.po.streaming.object.SortedMapDynamicStreamBuffer;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;

import org.w3c.dom.NodeList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class StreamingRippleJoinPO extends BinaryPlanOperator {

	private static final int STREAM_READ_TIMEOUT = 10;
	Map<StreamingRippleJoinPO, DynamicStreamBuffer[]> buffer = new HashMap<StreamingRippleJoinPO, DynamicStreamBuffer[]>();
	Map<StreamingRippleJoinPO, DynamicStreamBuffer> heap = new HashMap<StreamingRippleJoinPO, DynamicStreamBuffer>();
	Map<StreamingRippleJoinPO, DynamicStreamBuffer> returnBuffer = new HashMap<StreamingRippleJoinPO, DynamicStreamBuffer>();
	Map<StreamingRippleJoinPO, PointInTime[]> ts = new HashMap<StreamingRippleJoinPO, PointInTime[]>();
	Map<StreamingRippleJoinPO, PointInTime> mints = new HashMap<StreamingRippleJoinPO, PointInTime>();
	
	private Map<StreamingRippleJoinPO, Integer> lastInputFrom;
	
	public StreamingRippleJoinPO(){
		super();
	}
	
	public StreamingRippleJoinPO(StreamingRippleJoinPO po){
		super(po);	
	}
	
	public StreamingRippleJoinPO(AlgebraPO po){
		super(po);
	}
	
	@Override
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
		throw new NotImplementedException();
	}

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
		throw new NotImplementedException();
	}

	@Override
	protected boolean process_close() throws POException {
		buffer.remove(this);
		heap.remove(this);
		returnBuffer.remove(this);
		ts.remove(this);
		mints.remove(this);
		return true;
	}

	@Override
	protected Object process_next() throws POException, TimeoutException {

		StreamExchangeElement elem = null;
		int k = lastInputFrom.get(this);
		int j = k % 2 + 1;
		boolean cont = true;
		DynamicStreamBuffer[] buf = buffer.get(this); 
		DynamicStreamBuffer h = heap.get(this);
		PointInTime[] thisTs = ts.get(this);
				
		while (cont){
			
			// Entweder Daten zurückliefern 
			if (returnBuffer.get(this).size() > 0) 
				return returnBuffer.get(this).top();
			
			// Oder Daten produzieren
			
			// Algorithmus orientiert sich an dem Join von PIPES, der wiederum den 
			// RippleJoin umsetzt (für den es auch eine andere Version gibt)
			
			// Solange an einem der beiden Eingabeströme Daten vorliegen, auslesen
			// und zwar immer im Wechsel
			try {
				elem = getInputNext(j, this, STREAM_READ_TIMEOUT);	
			} catch (TimeoutException e) {
				j = j % 2 +1; // Dann den anderen Strom testen
				continue;
			}
			// Stromoperatoren können eigentlich nie terminieren ... sollte mann
			// trotzdem eine Behandlung für null in einem der beiden Ströme durchführen ?
			
			k = j % 2 + 1;
			buf[k].reorganize(elem, null);
			buf[j].insert(elem);
			Iterator<StreamExchangeElement> qualifies = buf[k].query(elem);
			while (qualifies.hasNext()){
				h.insert(process_join(elem,qualifies.next()));
			}
			thisTs[j] = elem.getValidity().getStart();
			
			if (thisTs[0] != null && thisTs[1] != null){
				mints.put(this,PointInTime.min(thisTs[0], thisTs[1]));
			}else{
				mints.put(this,null);
			}
			
			PointInTime lmints = mints.get(this);
			if (lmints != null){
				while (h.size() > 0){
					StreamExchangeElement top = h.top();
					if (top.getValidity().getStart().before(lmints)){
						h.remove(top);
						// TODO: Hier noch eine bessere Lösung finden
						putToReturnBuffer(top);											
					}else{
						// So richtig? Im Pipes-Alg ist hier ein return ...
						break;
					}
				}
			}
			
		}

		return null;
	}
	
	protected void putToReturnBuffer(StreamExchangeElement elem){
		synchronized (returnBuffer.get(this)) {
			returnBuffer.get(this).insert(elem);
		}
	}

	protected abstract StreamExchangeElement process_join(StreamExchangeElement elem, StreamExchangeElement element);
	protected abstract void setStreamBufferQueryPredicate(SortedMapDynamicStreamBuffer b1, SortedMapDynamicStreamBuffer b2);
	
	
	
	@Override
	protected boolean process_open() throws POException {
		SortedMapDynamicStreamBuffer[] b = new SortedMapDynamicStreamBuffer[2];
		b[0] = new SortedMapDynamicStreamBuffer();
		b[0].setDefaultRemovePredicate();
		b[1] = new SortedMapDynamicStreamBuffer();
		b[1].setDefaultRemovePredicate();
		buffer.put(this, b);
		heap.put(this, new SortedMapDynamicStreamBuffer());
		returnBuffer.put(this, new SortedMapDynamicStreamBuffer());
		setStreamBufferQueryPredicate(b[0], b[1]);
		PointInTime[] tsl = new PointInTime[2];
		tsl[0] = null; tsl[1] = null;
		ts.put(this, tsl);
		mints.put(this, null);
		lastInputFrom.put(this,1);
		return true;
	}



}
