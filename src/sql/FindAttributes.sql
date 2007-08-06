
// Liefere alle Quellen, die mindestens eines der gesuchten Attribute in ihrem
// Ausgabemuster haben

select distinct SourceDescID, aboutSource
from SDFSourceDescription,  SDFIntDescAccessPattern, SDFAccessPattern, SDFOutputPatternElement, SDFAttribute, SDFSchemaMapHasInAttribute, SDFSchemaMapHasOutAttribute
where SDFSourceDescription.IntDescID = SDFIntDescAccessPattern.IntDescID AND
      SDFIntDescAccessPattern.AccessPatternID = SDFAccessPattern.AccessPatternID AND
      SDFAccessPattern.OutputPatternID = SDFOutputPatternElement.OutputPatternID AND
      SDFOutputPatternElement.AttrConstID = SDFAttribute.AttributeID AND
      SDFAttribute.AttributeID = SDFSchemaMapHasInAttribute.AttrConstID AND
      SDFSchemaMapHasInAttribute.MappingID = SDFSchemaMapHasOutAttribute.MappingID AND
      (SDFSchemaMapHasOutAttribute.AttrConstID = 18)


// Auslesen aller Attribute aller OutputPattern einer bestimmten Quelle (lokales Schema)
select SDFOutputPatternElement.ATTRCONSTID, SDFOutputPatternElement.OutputPatternID
from SDFOutputPatternElement, SDFAccessPattern, SDFIntDescAccessPattern, SDFSourceDescription
where SDFOutputPatternElement.OutputPatternID = SDFAccessPattern.OutputPatternID AND
      SDFIntDescAccessPattern.AccessPatternID = SDFAccessPattern.AccessPatternID AND
      SDFSourceDescription.IntDescID = SDFIntDescAccessPattern.IntDescID AND
      SDFSourceDescription.SourceDescID = 23

select SDFOutputPatternElement.OutputPatternID, SDFOutputPatternElement.ATTRCONSTID, SDFAttribute.AttributeURI
from SDFOutputPatternElement, SDFAccessPattern, SDFIntDescAccessPattern, SDFSourceDescription, SDFAttribute
where SDFOutputPatternElement.OutputPatternID = SDFAccessPattern.OutputPatternID AND
      SDFIntDescAccessPattern.AccessPatternID = SDFAccessPattern.AccessPatternID AND
      SDFSourceDescription.IntDescID = SDFIntDescAccessPattern.IntDescID AND
SDFAttribute.AttributeID = SDFOutputPatternElement.AttrConstID AND
      SDFSourceDescription.SourceDescID = 23


// Ermitteln aller Ausgabeattribute eienr Quelle bzgl. des AccessPatterns
create view SDFGlobalAccessPatternView(SourceDescID, AccessPatternID, OutputAttribute) as
select distinct SDFSourceDescription.SourceDescID, SDFAccessPattern.AccessPatternID, SDFAttribute.AttributeURI
from SDFOutputPatternElement, SDFAccessPattern, SDFIntDescAccessPattern, SDFSourceDescription, SDFAttribute, SDFIntDescSchemaMap, SDFSchemaMapHasInAttribute, SDFSchemaMapHasOutAttribute
where SDFOutputPatternElement.OutputPatternID = SDFAccessPattern.OutputPatternID AND
      SDFIntDescAccessPattern.AccessPatternID = SDFAccessPattern.AccessPatternID AND
      SDFSourceDescription.IntDescID = SDFIntDescAccessPattern.IntDescID AND
      SDFIntDescSchemaMap.IntDescID = SDFSourceDescription.IntDescID AND
      SDFSchemaMapHasInAttribute.MappingID = SDFIntDescSchemaMap.MappingID AND
      SDFSchemaMapHasInAttribute.AttrConstID = SDFOutputPatternElement.ATTRCONSTID AND
      SDFSchemaMapHasOutAttribute.MappingID = SDFIntDescSchemaMap.MappingID AND
      SDFSchemaMapHasOutAttribute.AttrConstID = SDFAttribute.AttributeID


select x1.outputpatternid
from GlobalAccessPatternView x1, GlobalAccessPatternView x2, GlobalAccessPatternView x3
where
x1.outputpatternid = x2.outputpatternid and
x2.outputpatternid = x3.outputpatternid and
x1.OutputAttribute='http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Global.sdf#A' and
x2.OutputAttribute='http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Global.sdf#B' and
x3.OutputAttribute='http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Test/Global.sdf#C'


// Auslesen aller Mappings einer Quelle
select SDFSchemaMapHasInAttribute.MappingID, InAttribute.AttributeURI, OutAttribute.AttributeURI
from SDFIntDescSchemaMap, SDFSourceDescription, SDFSchemaMapHasInAttribute, SDFSchemaMapHasOutAttribute, SDFAttribute InAttribute, SDFAttribute OutAttribute
where SDFIntDescSchemaMap.IntDescID = SDFSourceDescription.IntDescID AND
      SDFSchemaMapHasInAttribute.MappingID = SDFIntDescSchemaMap.MappingID AND
      InAttribute.AttrConstID = SDFSchemaMapHasInAttribute.AttrConstID AND
      SDFSchemaMapHasOutAttribute.MappingID = SDFIntDescSchemaMap.MappingID AND
      OutAttribute.AttrConstID = SDFSchemaMapHasOutAttribute.AttrConstID AND
      SDFSourceDescription.SourceDescID = 23;



// Finden einer SourceDescription, die eine bestimmtes Zugriffsmuster enthält
select distinct SourceDescURI, SourceDescID
from SDFSourceDescription,  SDFIntDescAccessPattern, SDFAccessPattern
where SDFSourceDescription.IntDescID = SDFIntDescAccessPattern.IntDescID AND
      SDFIntDescAccessPattern.AccessPatternID = SDFAccessPattern.AccessPatternID AND
      SDFAccessPattern.OutputPatternID = 71


// Ermitteln der notwendigen Attribute eines InputPattern (bzgl. des
// globalen Schemas
select distinct SDFAttribute.*, SDFInputPatternElement.InputPatternID
from SDFAccessPattern, SDFInputPatternElement, SDFNecessity, SDFIntDescSchemaMap, SDFIntDescAccessPattern, SDFSchemaMapHasInAttribute, SDFSchemaMapHasOutAttribute, SDFAttribute
where SDFAccessPattern.AccessPatternID = 62 AND
      SDFAccessPattern.InputPatternID = SDFInputPatternElement.InputPatternID AND
      SDFInputPatternElement.NecessityID = SDFNecessity.NecessityID AND
      SDFNecessity.NecessityURI = 'http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_intensional_descriptions.sdf#required' AND
      SDFIntDescSchemaMap.IntDescID = SDFIntDescAccessPattern.IntDescID AND
      SDFInputPatternElement.AttrConstID = SDFSchemaMapHasInAttribute.AttrConstID AND
      SDFIntDescSchemaMap.MappingID = SDFSchemaMapHasInAttribute.MappingID AND
      SDFSchemaMapHasOutAttribute.MappingID = SDFIntDescSchemaMap.MappingID AND
      SDFAttribute.AttributeID = SDFSchemaMapHasOutAttribute.AttrConstID
