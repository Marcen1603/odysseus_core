package de.uniol.inf.is.odysseus.fusion.physicaloperator.context;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.fusion.store.context.FusionContextStore;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/*
 * 
 * 
 * 
 * 
 */
public class ContextStorePO extends AbstractPipe<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

    private final SDFSchema schema;
    private final FusionContextStore<RelationalTuple<IMetaAttribute>> contextStore;
    
    public ContextStorePO(final SDFSchema schema) {
        this.schema = schema;
        contextStore = new FusionContextStore<RelationalTuple<IMetaAttribute>>(schema);
    }

    public ContextStorePO(final ContextStorePO po) {
        this.schema = po.schema;
        contextStore = new FusionContextStore<RelationalTuple<IMetaAttribute>>(po.schema);
    }
    
    
    @Override
    public ContextStorePO clone() {
        return new ContextStorePO(this);
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(RelationalTuple<? extends IMetaAttribute> object, int port) {
		//contextStore.insertValue(object); 
		transfer(object);
		process_done();
	}





}
