/*
 * Created on 08.06.2006
 *
 */
package mg.dynaquest.queryexecution.po.base;

import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;

/**
 * @author  Marco Grawunder
 */
public interface SimplePlanOperator {

    void open(SimplePlanOperator caller) throws POException;

    <T> T next(SimplePlanOperator caller, long timeout) throws POException,
            TimeoutException;

    boolean close(SimplePlanOperator caller) throws POException;

    /**
	 * @uml.property  name="pOName"
	 */
    public String getPOName();

}