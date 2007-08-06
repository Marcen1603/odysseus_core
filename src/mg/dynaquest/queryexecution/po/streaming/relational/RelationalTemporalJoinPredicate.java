package mg.dynaquest.queryexecution.po.streaming.relational;

import java.util.Map;

import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.queryexecution.po.streaming.object.TemporalJoinPredicate;
import mg.dynaquest.queryexecution.po.streaming.object.TimeIntervalPredicate;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElement;

public class RelationalTemporalJoinPredicate extends TemporalJoinPredicate {
	
	SDFAttributeList leftSchema = null; 
	SDFAttributeList rightSchema = null;

	public RelationalTemporalJoinPredicate(SDFPredicate joinPredicate, TimeIntervalPredicate timeIntervalPredicate, 
			SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		super(joinPredicate, timeIntervalPredicate);
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
	}

	@Override
	public void doAttributeAssignment(
			Map<SDFAttribute, Object> attributeAssigment,
			StreamExchangeElement left, StreamExchangeElement right) {
		RelationalTuple leftCargo = (RelationalTuple) left.getCargo();
		RelationalTuple rightCargo = (RelationalTuple) right.getCargo();
		int i=0;
		for (SDFSchemaElement e:leftSchema){
			attributeAssigment.put((SDFAttribute)e,leftCargo.getAttribute(i++));
		}
		i=0;
		for (SDFSchemaElement e:rightSchema){
			attributeAssigment.put((SDFAttribute)e,rightCargo.getAttribute(i++));
		}		
	}

}
