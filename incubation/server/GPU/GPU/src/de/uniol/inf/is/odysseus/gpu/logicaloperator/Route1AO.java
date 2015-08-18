package de.uniol.inf.is.odysseus.gpu.logicaloperator;

import java.util.List;
 
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.PredicateParameter;

@LogicalOperator(name="ROUTE", minInputPorts=1, maxInputPorts=1, doc = "My operator is doing ...", url = "http://example.com/MyOperator.html", category = { "Category" })
public class Route1AO extends UnaryLogicalOp {
 
    private static final long serialVersionUID = -8015847502104587689L;
     
    public Route1AO(){
        super();
    }
     
    public RouteAO1(Route1AO routeAO){
        super(routeAO);
    }
 
    @Override
    @Parameter(type=PredicateParameter.class, isList=true, optional = false, doc = "This parameter sets the selection predicate")
    public void setPredicates(List<IPredicate<?>> predicates) {
        super.setPredicates(predicates);
    }
     
    @Override
    public AbstractLogicalOperator clone() {
        return new RouteAO(this);
    }
     
}