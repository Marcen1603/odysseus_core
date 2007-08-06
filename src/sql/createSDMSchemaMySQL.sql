
DROP TABLE SDFAttrConstSetElements;

CREATE TABLE SDFAttrConstSetElements (
       AttrConstID          INTEGER NOT NULL,
       SetID                INTEGER NOT NULL
);


ALTER TABLE SDFAttrConstSetElements
       ADD  ( PRIMARY KEY (AttrConstID, SetID) ) ;


DROP TABLE SDFHasSimpleDescriptionPredica;

CREATE TABLE SDFHasSimpleDescriptionPredica (
       ExtDescID            INTEGER NOT NULL,
       SimplePredicateID    INTEGER NOT NULL
);


ALTER TABLE SDFHasSimpleDescriptionPredica
       ADD  ( PRIMARY KEY (ExtDescID, SimplePredicateID) ) ;


DROP TABLE SDFHasComplexDescriptionPredic;

CREATE TABLE SDFHasComplexDescriptionPredic (
       ExtDescID            INTEGER NOT NULL,
       ComplexPredicateID   INTEGER NOT NULL
);


ALTER TABLE SDFHasComplexDescriptionPredic
       ADD  ( PRIMARY KEY (ExtDescID, ComplexPredicateID) ) ;


DROP TABLE SDFComplexPredicate;

CREATE TABLE SDFComplexPredicate (
       ComplexPredicateID   INTEGER NOT NULL,
       rightComplexID       INTEGER ,
       leftComplexID        INTEGER ,
       ComplexPredicateURI  VARCHAR(500) ,
       leftSimplePartID     INTEGER ,
       RightSimplePartID    INTEGER ,
       PredicateTypeID      INTEGER 
);


ALTER TABLE SDFComplexPredicate
       ADD  ( PRIMARY KEY (ComplexPredicateID) ) ;


DROP TABLE SDFSimplePredicate;

CREATE TABLE SDFSimplePredicate (
       SimplePredicateID    INTEGER NOT NULL,
       SimplePredicateURI   VARCHAR(500) ,
       predAttrID           INTEGER ,
       Value                VARCHAR(500) ,
       ConstantValueID      INTEGER ,
       CompareOpId          INTEGER ,
       PredicateTypeID      INTEGER ,
       inSetId              INTEGER 
);


ALTER TABLE SDFSimplePredicate
       ADD  ( PRIMARY KEY (SimplePredicateID) ) ;


DROP TABLE SDFInputPatternElement;

CREATE TABLE SDFInputPatternElement (
       InputPatternElementID INTEGER NOT NULL,
       InputPatternElementURI VARCHAR(500) ,
       InputPatternID       INTEGER ,
       AttrConstID          INTEGER ,
       NecessityID          INTEGER ,
       ConstantSetSelectionID INTEGER ,
       dependsOnID          INTEGER 
);


ALTER TABLE SDFInputPatternElement
       ADD  ( PRIMARY KEY (InputPatternElementID) ) ;


DROP TABLE SDFInputPatternCompareOperator;

CREATE TABLE SDFInputPatternCompareOperator(
	InputPatternElementID INTEGER NOT NULL,
    CompareOpId          INTEGER NOT NULL
);

ALTER TABLE SDFInputPatternCompareOperator
       ADD  ( PRIMARY KEY (InputPatternElementID, CompareOpId) ) ;


DROP TABLE SDFConstantSetSelection;

CREATE TABLE SDFConstantSetSelection (
       ConstantSetSelectionID INTEGER NOT NULL,
       ConstantSetSelectionURI VARCHAR(500) ,
       ConstantSetSelectionTypeID INTEGER ,
       SetID                INTEGER 
);


ALTER TABLE SDFConstantSetSelection
       ADD  ( PRIMARY KEY (ConstantSetSelectionID) ) ;


DROP TABLE SDFAttrConstSet;

CREATE TABLE SDFAttrConstSet (
       SetID                INTEGER NOT NULL,
       SetURI               VARCHAR(500) 
);


ALTER TABLE SDFAttrConstSet
       ADD  ( PRIMARY KEY (SetID) ) ;


DROP TABLE SDFConstantSetSelectionType;

CREATE TABLE SDFConstantSetSelectionType (
       ConstantSetSelectionTypeID INTEGER NOT NULL,
       ConstantSetSelectionTypeURI VARCHAR(500) 
);


ALTER TABLE SDFConstantSetSelectionType
       ADD  ( PRIMARY KEY (ConstantSetSelectionTypeID) ) ;


DROP TABLE SDFCompareOp;

CREATE TABLE SDFCompareOp (
       CompareOpId          INTEGER NOT NULL,
       CompareOpURI         VARCHAR(500) 
);


ALTER TABLE SDFCompareOp
       ADD  ( PRIMARY KEY (CompareOpId) ) ;


DROP TABLE SDFGlobalSchema;

CREATE TABLE SDFGlobalSchema (
       SchemaID             INTEGER NOT NULL
);


ALTER TABLE SDFGlobalSchema
       ADD  ( PRIMARY KEY (SchemaID) ) ;


DROP TABLE SDFIntDescSchemaMap;

CREATE TABLE SDFIntDescSchemaMap (
       IntDescID            INTEGER NOT NULL,
       MappingID            INTEGER NOT NULL
);


ALTER TABLE SDFIntDescSchemaMap
       ADD  ( PRIMARY KEY (IntDescID, MappingID) ) ;


DROP TABLE SDFNecessity;

CREATE TABLE SDFNecessity (
       NecessityID          INTEGER NOT NULL,
       NecessityURI         VARCHAR(500) 
);


ALTER TABLE SDFNecessity
       ADD  ( PRIMARY KEY (NecessityID) ) ;


DROP TABLE SDFIntDescAccessPattern;

CREATE TABLE SDFIntDescAccessPattern (
       IntDescID            INTEGER NOT NULL,
       AccessPatternID      INTEGER NOT NULL
);


ALTER TABLE SDFIntDescAccessPattern
       ADD  ( PRIMARY KEY (IntDescID, AccessPatternID) ) ;


DROP TABLE SDFURIIDMaxId;

CREATE TABLE SDFURIIDMaxId (
       maxId                INTEGER NOT NULL
);


ALTER TABLE SDFURIIDMaxId
       ADD  ( PRIMARY KEY (maxId) ) ;


DROP TABLE SDFURIIDMapping;

CREATE TABLE SDFURIIDMapping (
       ID                   INTEGER NOT NULL,
       URI                  VARCHAR(500) ,
       isAnon               CHAR(18) 
);


ALTER TABLE SDFURIIDMapping
       ADD  ( PRIMARY KEY (ID) ) ;


DROP TABLE SDFSourceDescription;

CREATE TABLE SDFSourceDescription (
       SourceDescID         INTEGER NOT NULL,
       SourceDescURI        VARCHAR(500) ,
       aboutSource          VARCHAR(500) ,
       IntDescID            INTEGER ,
       ExtDescID            INTEGER ,
       QualDescID           INTEGER 
);


ALTER TABLE SDFSourceDescription
       ADD  ( PRIMARY KEY (SourceDescID) ) ;


DROP TABLE SDFIntensionalDescription;

CREATE TABLE SDFIntensionalDescription (
       IntDescID            INTEGER NOT NULL,
       IntDescURI           VARCHAR(500) ,
       SchemaID             INTEGER 
);


ALTER TABLE SDFIntensionalDescription
       ADD  ( PRIMARY KEY (IntDescID) ) ;


DROP TABLE SDFExtensionalDescription;

CREATE TABLE SDFExtensionalDescription (
       ExtDescID            INTEGER NOT NULL,
       ExtDescURI           VARCHAR(500) 
);


ALTER TABLE SDFExtensionalDescription
       ADD  ( PRIMARY KEY (ExtDescID) ) ;


DROP TABLE SDFQualitativeDescription;

CREATE TABLE SDFQualitativeDescription (
       QualDescID           INTEGER NOT NULL,
       QualDescURI          VARCHAR(500) 
);


ALTER TABLE SDFQualitativeDescription
       ADD  ( PRIMARY KEY (QualDescID) ) ;


DROP TABLE SDFSchemaEntity;

CREATE TABLE SDFSchemaEntity (
       SchemaID             INTEGER NOT NULL,
       EntityID             INTEGER NOT NULL
);


ALTER TABLE SDFSchemaEntity
       ADD  ( PRIMARY KEY (SchemaID, EntityID) ) ;


DROP TABLE SDFSchema;

CREATE TABLE SDFSchema (
       SchemaID             INTEGER NOT NULL,
       SchemaURI            VARCHAR(500) 
);


ALTER TABLE SDFSchema
       ADD  ( PRIMARY KEY (SchemaID) ) ;


DROP TABLE SDFOutputPatternElement;

CREATE TABLE SDFOutputPatternElement (
       OutputPatternElementID INTEGER NOT NULL,
       OutputPatternElementURI VARCHAR(500) ,
       OutputPatternID      INTEGER ,
       AttrConstID          INTEGER ,
       OutputAttributeBindingID    INTEGER 
);


ALTER TABLE SDFOutputPatternElement
       ADD  ( PRIMARY KEY (OutputPatternElementID) ) ;


DROP TABLE SDFSchemaMapHasOutAttribute;

CREATE TABLE SDFSchemaMapHasOutAttribute (
       pos                  INTEGER ,
       MappingID            INTEGER NOT NULL,
       AttrConstID          INTEGER NOT NULL
);


ALTER TABLE SDFSchemaMapHasOutAttribute
       ADD  ( PRIMARY KEY (MappingID, AttrConstID) ) ;


DROP TABLE SDFSchemaMapHasInAttribute;

CREATE TABLE SDFSchemaMapHasInAttribute (
       pos                  INTEGER ,
       MappingID            INTEGER NOT NULL,
       AttrConstID          INTEGER NOT NULL
);


ALTER TABLE SDFSchemaMapHasInAttribute
       ADD  ( PRIMARY KEY (MappingID, AttrConstID) ) ;


DROP TABLE SDFEntityAttribute;

CREATE TABLE SDFEntityAttribute (
       idAttr               INTEGER ,
       AttrConstID          INTEGER NOT NULL,
       EntityID             INTEGER NOT NULL
);


ALTER TABLE SDFEntityAttribute
       ADD  ( PRIMARY KEY (AttrConstID, EntityID) ) ;


DROP TABLE SDFAttrConst;

CREATE TABLE SDFAttrConst (
       AttrConstID          INTEGER NOT NULL,
       AttrConstURI         VARCHAR(500) ,
       UnitID               INTEGER ,
       DatatypeID           INTEGER ,
       isConstant           INTEGER ,
       value                VARCHAR(4000) 
);


ALTER TABLE SDFAttrConst
       ADD  ( PRIMARY KEY (AttrConstID) ) ;


DROP TABLE SDFAttrConstConstraint;

CREATE TABLE SDFAttrConstConstraint(
       AttrConstConstraintTypeURI	  VARCHAR(500) NOT NULL,
       AttrConstID					  INTEGER NOT NULL,
       value1						  VARCHAR(4000),
       value2						  VARCHAR(4000),
       value3						  VARCHAR(4000),
	   value4	       				  VARCHAR(4000)
);

ALTER TABLE SDFAttrConstConstraint
	ADD ( PRIMARY KEY (AttrConstConstraintTypeURI, AttrConstID) );


DROP TABLE SDFUnit;

CREATE TABLE SDFUnit (
       UnitID               INTEGER NOT NULL,
       superUnitID          INTEGER ,
       UnitURI              VARCHAR(500) 
);


ALTER TABLE SDFUnit
       ADD  ( PRIMARY KEY (UnitID) ) ;


DROP TABLE SDFDatatype;

CREATE TABLE SDFDatatype (
       DatatypeID           INTEGER NOT NULL,
       DatatypeURI          VARCHAR(500) 
);


ALTER TABLE SDFDatatype
       ADD  ( PRIMARY KEY (DatatypeID) ) ;


DROP TABLE SDFSchemaMapping;

CREATE TABLE SDFSchemaMapping (
       MappingID            INTEGER NOT NULL,
       MappingURI           VARCHAR(500) ,
       MappingTypeID        INTEGER ,
       MappingFunctionID    INTEGER 
);


ALTER TABLE SDFSchemaMapping
       ADD  ( PRIMARY KEY (MappingID) ) ;


DROP TABLE SDFMappingType;

CREATE TABLE SDFMappingType (
       MappingTypeID        INTEGER NOT NULL,
       MappingTypeURI       VARCHAR(500) ,
       superMappingID       INTEGER 
);


ALTER TABLE SDFMappingType
       ADD  ( PRIMARY KEY (MappingTypeID) ) ;


DROP TABLE SDFMappingFunction;

CREATE TABLE SDFMappingFunction (
       MappingFunctionID    INTEGER NOT NULL,
       MappingFunctionURI   VARCHAR(500) ,
       hasReverseFunction   INTEGER ,
       FunctionTypeID       INTEGER 
);


ALTER TABLE SDFMappingFunction
       ADD  ( PRIMARY KEY (MappingFunctionID) ) ;


DROP TABLE SDFFunctionType;

CREATE TABLE SDFFunctionType (
       FunctionTypeID       INTEGER NOT NULL,
       FunctionTypeURI      VARCHAR(500) ,
       superFunctionID      INTEGER 
);


ALTER TABLE SDFFunctionType
       ADD  ( PRIMARY KEY (FunctionTypeID) ) ;


DROP TABLE SDFAccessPattern;

CREATE TABLE SDFAccessPattern (
       AccessPatternID      INTEGER NOT NULL,
       AccessPatternURI     VARCHAR(500) ,
       InputPatternID       INTEGER ,
       OutputPatternID      INTEGER 
);


ALTER TABLE SDFAccessPattern
       ADD  ( PRIMARY KEY (AccessPatternID) ) ;


DROP TABLE SDFInputPattern;

CREATE TABLE SDFInputPattern (
       InputPatternID       INTEGER NOT NULL,
       InputPatternURI      VARCHAR(500) 
);


ALTER TABLE SDFInputPattern
       ADD  ( PRIMARY KEY (InputPatternID) ) ;


DROP TABLE SDFOutputPattern;

CREATE TABLE SDFOutputPattern (
       OutputPatternID      INTEGER NOT NULL,
       OutputPatternURI     VARCHAR(500) 
);


ALTER TABLE SDFOutputPattern
       ADD  ( PRIMARY KEY (OutputPatternID) ) ;


DROP TABLE SDFEntity;

CREATE TABLE SDFEntity (
       EntityID             INTEGER NOT NULL,
       EntityURI            VARCHAR(500) 
);


ALTER TABLE SDFEntity
       ADD  ( PRIMARY KEY (EntityID) ) ;


DROP TABLE SDFOutputAttributeBinding;

CREATE TABLE SDFOutputAttributeBinding (
       OutputAttributeBindingID    INTEGER NOT NULL,
       OutputAttributeBindingURI   VARCHAR(500) 
);


ALTER TABLE SDFOutputAttributeBinding
       ADD  ( PRIMARY KEY (OutputAttributeBindingID) ) ;


DROP TABLE SDFPredicateType;

CREATE TABLE SDFPredicateType (
       PredicateTypeID      INTEGER NOT NULL,
       PredicateTypeURI     VARCHAR(500) 
);


ALTER TABLE SDFPredicateType
       ADD  ( PRIMARY KEY (PredicateTypeID) ) ;

DROP VIEW SDFConstant;

create view SDFConstant(ConstantID, ConstantURI, UnitID, DatatypeID) as
select AttrConstID, AttrConstURI, UnitID, DatatypeID
from SDFAttrConst
where SDFAttrConst.isConstant = 1;

DROP VIEW SDFAttribute;

create view SDFAttribute(AttributeID, AttributeURI, UnitID, DatatypeID) as
select AttrConstID, AttrConstURI, UnitID, DatatypeID
from SDFAttrConst
where isConstant = 0;

DROP VIEW SDFGlobalAccessPatternView;

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

;


ALTER TABLE SDFAttrConstSetElements
       ADD  ( FOREIGN KEY (SetID)
                             REFERENCES SDFAttrConstSet
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFAttrConstSetElements
       ADD  ( FOREIGN KEY (AttrConstID)
                             REFERENCES SDFAttrConst
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFHasSimpleDescriptionPredica
       ADD  ( FOREIGN KEY (SimplePredicateID)
                             REFERENCES SDFSimplePredicate ) ;


ALTER TABLE SDFHasSimpleDescriptionPredica
       ADD  ( FOREIGN KEY (ExtDescID)
                             REFERENCES SDFExtensionalDescription ) ;


ALTER TABLE SDFHasComplexDescriptionPredic
       ADD  ( FOREIGN KEY (ComplexPredicateID)
                             REFERENCES SDFComplexPredicate ) ;


ALTER TABLE SDFHasComplexDescriptionPredic
       ADD  ( FOREIGN KEY (ExtDescID)
                             REFERENCES SDFExtensionalDescription ) ;


ALTER TABLE SDFComplexPredicate
       ADD  ( FOREIGN KEY (rightComplexID)
                             REFERENCES SDFComplexPredicate
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFComplexPredicate
       ADD  ( FOREIGN KEY (leftComplexID)
                             REFERENCES SDFComplexPredicate
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFComplexPredicate
       ADD  ( FOREIGN KEY (PredicateTypeID)
                             REFERENCES SDFPredicateType
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFComplexPredicate
       ADD  ( FOREIGN KEY (RightSimplePartID)
                             REFERENCES SDFSimplePredicate
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFComplexPredicate
       ADD  ( FOREIGN KEY (leftSimplePartID)
                             REFERENCES SDFSimplePredicate
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSimplePredicate
       ADD  ( FOREIGN KEY (inSetId)
                             REFERENCES SDFAttrConstSet
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSimplePredicate
       ADD  ( FOREIGN KEY (PredicateTypeID)
                             REFERENCES SDFPredicateType
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSimplePredicate
       ADD  ( FOREIGN KEY (CompareOpId)
                             REFERENCES SDFCompareOp
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSimplePredicate
       ADD  ( FOREIGN KEY (ConstantValueID)
                             REFERENCES SDFAttrConst
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSimplePredicate
       ADD  ( FOREIGN KEY (predAttrID)
                             REFERENCES SDFAttrConst
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFInputPatternElement
       ADD  ( FOREIGN KEY (dependsOnID)
                             REFERENCES SDFAttrConstSet
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFInputPatternElement
       ADD  ( FOREIGN KEY (ConstantSetSelectionID)
                             REFERENCES SDFConstantSetSelection
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFInputPatternElement
       ADD  ( FOREIGN KEY (NecessityID)
                             REFERENCES SDFNecessity
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFInputPatternElement
       ADD  ( FOREIGN KEY (AttrConstID)
                             REFERENCES SDFAttrConst
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFInputPatternElement
       ADD  ( FOREIGN KEY (InputPatternID)
                             REFERENCES SDFInputPattern
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFConstantSetSelection
       ADD  ( FOREIGN KEY (SetID)
                             REFERENCES SDFAttrConstSet
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFConstantSetSelection
       ADD  ( FOREIGN KEY (ConstantSetSelectionTypeID)
                             REFERENCES SDFConstantSetSelectionType
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFGlobalSchema
       ADD  ( FOREIGN KEY (SchemaID)
                             REFERENCES SDFSchema
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFIntDescSchemaMap
       ADD  ( FOREIGN KEY (MappingID)
                             REFERENCES SDFSchemaMapping
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFIntDescSchemaMap
       ADD  ( FOREIGN KEY (IntDescID)
                             REFERENCES SDFIntensionalDescription
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFIntDescAccessPattern
       ADD  ( FOREIGN KEY (AccessPatternID)
                             REFERENCES SDFAccessPattern
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFIntDescAccessPattern
       ADD  ( FOREIGN KEY (IntDescID)
                             REFERENCES SDFIntensionalDescription
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFSourceDescription
       ADD  ( FOREIGN KEY (QualDescID)
                             REFERENCES SDFQualitativeDescription
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSourceDescription
       ADD  ( FOREIGN KEY (ExtDescID)
                             REFERENCES SDFExtensionalDescription
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSourceDescription
       ADD  ( FOREIGN KEY (IntDescID)
                             REFERENCES SDFIntensionalDescription
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFIntensionalDescription
       ADD  ( FOREIGN KEY (SchemaID)
                             REFERENCES SDFSchema
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSchemaEntity
       ADD  ( FOREIGN KEY (EntityID)
                             REFERENCES SDFEntity
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFSchemaEntity
       ADD  ( FOREIGN KEY (SchemaID)
                             REFERENCES SDFSchema
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFOutputPatternElement
       ADD  ( FOREIGN KEY (OutputAttributeBindingID)
                             REFERENCES SDFOutputAttributeBinding
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFOutputPatternElement
       ADD  ( FOREIGN KEY (AttrConstID)
                             REFERENCES SDFAttrConst
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFOutputPatternElement
       ADD  ( FOREIGN KEY (OutputPatternID)
                             REFERENCES SDFOutputPattern
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFSchemaMapHasOutAttribute
       ADD  ( FOREIGN KEY (AttrConstID)
                             REFERENCES SDFAttrConst
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFSchemaMapHasOutAttribute
       ADD  ( FOREIGN KEY (MappingID)
                             REFERENCES SDFSchemaMapping
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFSchemaMapHasInAttribute
       ADD  ( FOREIGN KEY (AttrConstID)
                             REFERENCES SDFAttrConst
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFSchemaMapHasInAttribute
       ADD  ( FOREIGN KEY (MappingID)
                             REFERENCES SDFSchemaMapping
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFEntityAttribute
       ADD  ( FOREIGN KEY (EntityID)
                             REFERENCES SDFEntity
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFEntityAttribute
       ADD  ( FOREIGN KEY (AttrConstID)
                             REFERENCES SDFAttrConst
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFAttrConst
       ADD  ( FOREIGN KEY (DatatypeID)
                             REFERENCES SDFDatatype
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFAttrConst
       ADD  ( FOREIGN KEY (UnitID)
                             REFERENCES SDFUnit
                             ON DELETE SET NULL ) ;

ALTER TABLE SDFAttrConstConstraint
       ADD  ( FOREIGN KEY (AttrConstID)
                             REFERENCES SDFAttrConst
                             ON DELETE SET NULL ) ;

ALTER TABLE SDFUnit
       ADD  ( FOREIGN KEY (superUnitID)
                             REFERENCES SDFUnit
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSchemaMapping
       ADD  ( FOREIGN KEY (MappingFunctionID)
                             REFERENCES SDFMappingFunction
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFSchemaMapping
       ADD  ( FOREIGN KEY (MappingTypeID)
                             REFERENCES SDFMappingType
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFMappingType
       ADD  ( FOREIGN KEY (superMappingID)
                             REFERENCES SDFMappingType
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFMappingFunction
       ADD  ( FOREIGN KEY (FunctionTypeID)
                             REFERENCES SDFFunctionType
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFMappingFunction
       ADD  ( FOREIGN KEY (hasReverseFunction)
                             REFERENCES SDFMappingFunction
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFFunctionType
       ADD  ( FOREIGN KEY (superFunctionID)
                             REFERENCES SDFFunctionType
                             ON DELETE SET NULL ) ;


ALTER TABLE SDFAccessPattern
       ADD  ( FOREIGN KEY (OutputPatternID)
                             REFERENCES SDFOutputPattern
                             ON DELETE CASCADE ) ;


ALTER TABLE SDFAccessPattern
       ADD  ( FOREIGN KEY (InputPatternID)
                             REFERENCES SDFInputPattern
                             ON DELETE CASCADE ) ;
