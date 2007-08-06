package mg.dynaquest.queryexecution.po.relational;

import java.util.Collection;

import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;

public interface IsSetOperator {
	public Collection<RelationalTuple> getAllReadElements();
}