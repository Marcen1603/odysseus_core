package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ListPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.Nest;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class RelationalNest extends Nest<RelationalTuple<? extends IMetaAttribute>> {

	@Override
	public RelationalTuple evaluate(IPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		List<RelationalTuple<?>> elems = ((ListPartialAggregate<RelationalTuple<? extends IMetaAttribute>>)p).getElems();
		// TODO: Set Value of generated RelationalTuple ...
		return null;
	}

}
