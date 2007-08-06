package mg.dynaquest.queryexecution.po.streaming.base;

import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.queryexecution.po.streaming.algebra.WindowPO;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;

import org.w3c.dom.NodeList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class AbstractWindowPO extends UnaryPlanOperator {

	public AbstractWindowPO(AbstractWindowPO operator) {
		 super(operator);
	 }

	public AbstractWindowPO() {
		super();
	}

	public AbstractWindowPO(WindowPO algebraPO) {
		super(algebraPO);
	}
	
	public long getWindowSize(){
		return ((WindowPO)getAlgebraPO()).getWindowSize();
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
	   	xmlRetValue.append(indent).append("<algebraPO>\n");
    	getAlgebraPO().getInternalXMLRepresentation(baseIndent,indent+indent,xmlRetValue);
    	xmlRetValue.append(indent).append("</algebraPO>\n");
	}
	
	@Override
	protected boolean process_close() throws POException {
		return true;
	}

	@Override
	protected Object process_next() throws POException, TimeoutException {
		StreamExchangeElement<?> next = (StreamExchangeElement<?>) getInputNext(this, -1);
		setWindow(next);
		return next;
	}

	protected abstract void setWindow(StreamExchangeElement<?> next);

	@Override
	protected boolean process_open() throws POException {
		return true;
	}

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
		throw new NotImplementedException();
		// TODO: Initialisieren eines AlgebraPOs aus XML
	}


}
