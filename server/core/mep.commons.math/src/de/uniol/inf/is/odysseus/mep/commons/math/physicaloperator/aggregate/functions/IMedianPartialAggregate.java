/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.commons.math.physicaloperator.aggregate.functions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IMedianPartialAggregate<R> extends IPartialAggregate<R> {
    void add(final Double value);

    Double getAggValue();

    void clear();
}
