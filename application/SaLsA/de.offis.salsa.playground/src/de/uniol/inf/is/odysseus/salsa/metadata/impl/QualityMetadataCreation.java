package de.uniol.inf.is.odysseus.salsa.metadata.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataCreationPO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.metadata.Quality;

public class QualityMetadataCreation<M extends Quality> extends
        MetadataCreationPO<M, RelationalTuple<M>> {

    /**
     * 
     */
    private static final long serialVersionUID = -6291152184553419310L;
    protected volatile static Logger LOG = LoggerFactory.getLogger(QualityMetadataCreation.class);

    public QualityMetadataCreation(Class<M> type) {
        super(type);
    }

    public QualityMetadataCreation(QualityMetadataCreation<M> po) {
        super(po);
    }

    @Override
    public void process_next(RelationalTuple<M> tuple, int port) {
        try {
            assignMetadata(tuple);
            this.transfer(tuple);
        }
        catch (InstantiationException e) {
            LOG.error(e.getMessage(), e);
        }
        catch (IllegalAccessException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @SuppressWarnings("unchecked")
    private void assignMetadata(Object object) throws InstantiationException,
            IllegalAccessException {

        if (object instanceof IMetaAttributeContainer) {
            ((IMetaAttributeContainer<M>) object).setMetadata(getType().newInstance());
        }
        else {
            RelationalTuple<M> tuple = (RelationalTuple<M>) object;
            for (int i = 0; i < tuple.size(); i++) {
                assignMetadata(tuple.getAttribute(i));
            }
        }

    }

}
