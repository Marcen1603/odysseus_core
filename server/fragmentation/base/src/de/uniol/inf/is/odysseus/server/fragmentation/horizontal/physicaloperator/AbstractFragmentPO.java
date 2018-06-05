package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;

/**
 * A {@link AbstractFragmentPO} can be used to realize a
 * {@link AbstractFragmentAO}.
 * 
 * @author Michael Brand
 * @author Marco Grawunder
 */
public abstract class AbstractFragmentPO<T extends IStreamObject<IMetaAttribute>> extends AbstractPipe<T, T> implements IPhysicalOperatorKeyValueProvider {

    @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AbstractFragmentPO.class);

    /**
     * Constructs a new {@link AbstractFragmentPO}.
     * 
     * @param fragmentAO
     *            the {@link AbstractFragmentAO} transformed to this
     *            {@link AbstractFragmentPO}.
     */
    public AbstractFragmentPO(AbstractFragmentAO fragmentAO) {

        super();

    }

    public AbstractFragmentPO(){
    	super();
    }
    
    @Override
    public OutputMode getOutputMode() {

        return OutputMode.INPUT;

    }

    /**
     * Routes an incoming object to the next output port.
     * 
     * @param object
     *            The incoming {@link IStreamable} object.
     * @return The output port to which <code>object</code> shall be transfered.
     */
    protected abstract int route(IStreamObject<IMetaAttribute> object);

    @Override
    public Map<String, String> getKeyValues() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    @Override
    public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
        if (!(ipo instanceof AbstractFragmentPO)) {
            return false;
        }

        return super.isSemanticallyEqual(ipo);
    }

}