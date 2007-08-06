
DROP TABLE SDFAttrConstSetElements CASCADE CONSTRAINTS;

CREATE TABLE SDFAttrConstSetElements (
       AttrConstID          INTEGER NOT NULL,
       SetID                INTEGER NOT NULL
);


ALTER TABLE SDFAttrConstSetElements
       ADD  ( PRIMARY KEY (AttrConstID, SetID) ) ;


DROP TABLE SDFHasSimpleDescriptionPredica CASCADE CONSTRAINTS;

CREATE TABLE SDFHasSimpleDescriptionPredica (
       ExtDescID            INTEGER NOT NULL,
       SimplePredicateID    INTEGER NOT NULL
);


ALTER TABLE SDFHasSimpleDescriptionPredica
       ADD  ( PRIMARY KEY (ExtDescID, SimplePredicateID) ) ;


DROP TABLE SDFHasComplexDescriptionPredic CASCADE CONSTRAINTS;

CREATE TABLE SDFHasComplexDescriptionPredic (
       ExtDescID            INTEGER NOT NULL,
       ComplexPredicateID   INTEGER NOT NULL
);


ALTER TABLE SDFHasComplexDescriptionPredic
       ADD  ( PRIMARY KEY (ExtDescID, ComplexPredicateID) ) ;


DROP TABLE SDFComplexPredicate CASCADE CONSTRAINTS;

CREATE TABLE SDFComplexPredicate (
       ComplexPredicateID   INTEGER NOT NULL,
       rightComplexID       INTEGER NULL,
       leftComplexID        INTEGER NULL,
       ComplexPredicateURI  VARCHAR2(500) NULL,
       leftSimplePartID     INTEGER NULL,
       RightSimplePartID    INTEGER NULL,
       PredicateTypeID      INTEGER NULL
);


ALTER TABLE SDFComplexPredicate
       ADD  ( PRIMARY KEY (ComplexPredicateID) ) ;


DROP TABLE SDFSimplePredicate CASCADE CONSTRAINTS;

CREATE TABLE SDFSimplePredicate (
       SimplePredicateID    INTEGER NOT NULL,
       SimplePredicateURI   VARCHAR2(500) NULL,
       predAttrID           INTEGER NULL,
       Value                VARCHAR2(500) NULL,
       ConstantValueID      INTEGER NULL,
       CompareOpId          INTEGER NULL,
       PredicateTypeID      INTEGER NULL,
       inSetId              INTEGER NULL
);


ALTER TABLE SDFSimplePredicate
       ADD  ( PRIMARY KEY (SimplePredicateID) ) ;


DROP TABLE SDFInputPatternElement CASCADE CONSTRAINTS;

CREATE TABLE SDFInputPatternElement (
       InputPatternElementID INTEGER NOT NULL,
       InputPatternElementURI VARCHAR2(500) NULL,
       InputPatternID       INTEGER NULL,
       AttrConstID          INTEGER NULL,
       NecessityID          INTEGER NULL,
       ConstantSetSelectionID INTEGER NULL,
       dependsOnID          INTEGER NULL
);

DROP TABLE SDFInputPatternCompareOperator;

CREATE TABLE SDFInputPatternCompareOperator(
	InputPatternElementID INTEGER NOT NULL,
    CompareOpId          INTEGER NOT NULL
);

ALTER TABLE SDFInputPatternCompareOperator
       ADD  ( PRIMARY KEY (InputPatternElementID, CompareOpId) ) ;


ALTER TABLE SDFInputPatternElement
       ADD  ( PRIMARY KEY (InputPatternElementID) ) ;


DROP TABLE SDFConstantSetSelection CASCADE CONSTRAINTS;

CREATE TABLE SDFConstantSetSelection (
       ConstantSetSelectionID INTEGER NOT NULL,
       ConstantSetSelectionURI VARCHAR2(500) NULL,
       ConstantSetSelectionTypeID INTEGER NULL,
       SetID                INTEGER NULL
);


ALTER TABLE SDFConstantSetSelection
       ADD  ( PRIMARY KEY (ConstantSetSelectionID) ) ;


DROP TABLE SDFAttrConstSet CASCADE CONSTRAINTS;

CREATE TABLE SDFAttrConstSet (
       SetID                INTEGER NOT NULL,
       SetURI               VARCHAR2(500) NULL
);


ALTER TABLE SDFAttrConstSet
       ADD  ( PRIMARY KEY (SetID) ) ;


DROP TABLE SDFConstantSetSelectionType CASCADE CONSTRAINTS;

CREATE TABLE SDFConstantSetSelectionType (
       ConstantSetSelectionTypeID INTEGER NOT NULL,
       ConstantSetSelectionTypeURI VARCHAR2(500) NULL
);


ALTER TABLE SDFConstantSetSelectionType
       ADD  ( PRIMARY KEY (ConstantSetSelectionTypeID) ) ;


DROP TABLE SDFCompareOp CASCADE CONSTRAINTS;

CREATE TABLE SDFCompareOp (
       CompareOpId          INTEGER NOT NULL,
       CompareOpURI         VARCHAR2(500) NULL
);


ALTER TABLE SDFCompareOp
       ADD  ( PRIMARY KEY (CompareOpId) ) ;


DROP TABLE SDFGlobalSchema CASCADE CONSTRAINTS;

CREATE TABLE SDFGlobalSchema (
       SchemaID             INTEGER NOT NULL
);


ALTER TABLE SDFGlobalSchema
       ADD  ( PRIMARY KEY (SchemaID) ) ;


DROP TABLE SDFIntDescSchemaMap CASCADE CONSTRAINTS;

CREATE TABLE SDFIntDescSchemaMap (
       IntDescID            INTEGER NOT NULL,
       MappingID            INTEGER NOT NULL
);


ALTER TABLE SDFIntDescSchemaMap
       ADD  ( PRIMARY KEY (IntDescID, MappingID) ) ;


DROP TABLE SDFNecessity CASCADE CONSTRAINTS;

CREATE TABLE SDFNecessity (
       NecessityID          INTEGER NOT NULL,
       NecessityURI         VARCHAR2(500) NULL
);


ALTER TABLE SDFNecessity
       ADD  ( PRIMARY KEY (NecessityID) ) ;


DROP TABLE SDFIntDescAccessPattern CASCADE CONSTRAINTS;

CREATE TABLE SDFIntDescAccessPattern (
       IntDescID            INTEGER NOT NULL,
       AccessPatternID      INTEGER NOT NULL
);


ALTER TABLE SDFIntDescAccessPattern
       ADD  ( PRIMARY KEY (IntDescID, AccessPatternID) ) ;


DROP TABLE SDFUriIDMaxId CASCADE CONSTRAINTS;

CREATE TABLE SDFUriIDMaxId (
       maxId                INTEGER NOT NULL
);


ALTER TABLE SDFUriIDMaxId
       ADD  ( PRIMARY KEY (maxId) ) ;


DROP TABLE SDFURIIdMapping CASCADE CONSTRAINTS;

CREATE TABLE SDFURIIdMapping (
       ID                   INTEGER NOT NULL,
       URI                  VARCHAR2(500) NULL,
       isAnon               CHAR(18) NULL
);


ALTER TABLE SDFURIIdMapping
       ADD  ( PRIMARY KEY (ID) ) ;


DROP TABLE SDFSourceDescription CASCADE CONSTRAINTS;

CREATE TABLE SDFSourceDescription (
       SourceDescID         INTEGER NOT NULL,
       SourceDescURI        VARCHAR2(500) NULL,
       aboutSource          VARCHAR2(500) NULL,
       IntDescID            INTEGER NULL,
       ExtDescID            INTEGER NULL,
       QualDescID           INTEGER NULL
);


ALTER TABLE SDFSourceDescription
       ADD  ( PRIMARY KEY (SourceDescID) ) ;


DROP TABLE SDFIntensionalDescription CASCADE CONSTRAINTS;

CREATE TABLE SDFIntensionalDescription (
       IntDescID            INTEGER NOT NULL,
       IntDescURI           VARCHAR2(500) NULL,
       SchemaID             INTEGER NULL
);


ALTER TABLE SDFIntensionalDescription
       ADD  ( PRIMARY KEY (IntDescID) ) ;


DROP TABLE SDFExtensionalDescription CASCADE CONSTRAINTS;

CREATE TABLE SDFExtensionalDescription (
       ExtDescID            INTEGER NOT NULL,
       ExtDescURI           VARCHAR2(500) NULL
);


ALTER TABLE SDFExtensionalDescription
       ADD  ( PRIMARY KEY (ExtDescID) ) ;


DROP TABLE SDFQualitativeDescription CASCADE CONSTRAINTS;

CREATE TABLE SDFQualitativeDescription (
       QualDescID           INTEGER NOT NULL,
       QualDescURI          VARCHAR2(500) NULL
);


ALTER TABLE SDFQualitativeDescription
       ADD  ( PRIMARY KEY (QualDescID) ) ;


DROP TABLE SDFSchemaEntity CASCADE CONSTRAINTS;

CREATE TABLE SDFSchemaEntity (
       SchemaID             INTEGER NOT NULL,
       EntityID             INTEGER NOT NULL
);


ALTER TABLE SDFSchemaEntity
       ADD  ( PRIMARY KEY (SchemaID, EntityID) ) ;


DROP TABLE SDFSchema CASCADE CONSTRAINTS;

CREATE TABLE SDFSchema (
       SchemaID             INTEGER NOT NULL,
       SchemaURI            VARCHAR2(500) NULL
);


ALTER TABLE SDFSchema
       ADD  ( PRIMARY KEY (SchemaID) ) ;


DROP TABLE SDFOutputPatternElement CASCADE CONSTRAINTS;

CREATE TABLE SDFOutputPatternElement (
       OutputPatternElementID INTEGER NOT NULL,
       OutputPatternElementURI VARCHAR2(500) NULL,
       OutputPatternID      INTEGER NULL,
       AttrConstID          INTEGER NULL,
       OutputAttributeBindingID    INTEGER NULL
);


ALTER TABLE SDFOutputPatternElement
       ADD  ( PRIMARY KEY (OutputPatternElementID) ) ;


DROP TABLE SDFSchemaMapHasOutAttribute CASCADE CONSTRAINTS;

CREATE TABLE SDFSchemaMapHasOutAttribute (
       pos                  INTEGER NULL,
       MappingID            INTEGER NOT NULL,
       AttrConstID          INTEGER NOT NULL
);


ALTER TABLE SDFSchemaMapHasOutAttribute
       ADD  ( PRIMARY KEY (MappingID, AttrConstID) ) ;


DROP TABLE SDFSchemaMapHasInAttribute CASCADE CONSTRAINTS;

CREATE TABLE SDFSchemaMapHasInAttribute (
       pos                  INTEGER NULL,
       MappingID            INTEGER NOT NULL,
       AttrConstID          INTEGER NOT NULL
);


ALTER TABLE SDFSchemaMapHasInAttribute
       ADD  ( PRIMARY KEY (MappingID, AttrConstID) ) ;


DROP TABLE SDFEntityAttribute CASCADE CONSTRAINTS;

CREATE TABLE SDFEntityAttribute (
       idAttr               INTEGER NULL,
       AttrConstID          INTEGER NOT NULL,
       EntityID             INTEGER NOT NULL
);


ALTER TABLE SDFEntityAttribute
       ADD  ( PRIMARY KEY (AttrConstID, EntityID) ) ;


DROP TABLE SDFAttrConst CASCADE CONSTRAINTS;

CREATE TABLE SDFAttrConst (
       AttrConstID          INTEGER NOT NULL,
       AttrConstURI         VARCHAR2(500) NULL,
       UnitID               INTEGER NULL,
       DatatypeID           INTEGER NULL,
       isConstant           INTEGER NULL,
       value                VARCHAR2(4000) NULL
);


ALTER TABLE SDFAttrConst
       ADD  ( PRIMARY KEY (AttrConstID) ) ;

DROP TABLE SDFAttrConstConstraint CASCADE CONSTRAINTS;

CREATE TABLE SDFAttrConstConstraint(
       AttrConstConstraintTypeURI	  VARCHAR2(500) NOT NULL,	
       AttrConstID					  INTEGER NOT NULL,
       value1						  VARCHAR2(4000) NULL,
       value2						  VARCHAR2(4000) NULL,
       value3						  VARCHAR2(4000) NULL,
       value4						  VARCHAR2(4000) NULL
);

ALTER TABLE SDFAttrConstConstraint
	ADD ( PRIMARY KEY(AttrConstConstraintTypeURI, AttrConstID));


DROP TABLE SDFUnit CASCADE CONSTRAINTS;

CREATE TABLE SDFUnit (
       UnitID               INTEGER NOT NULL,
       superUnitID          INTEGER NULL,
       UnitURI              VARCHAR2(500) NULL
);


ALTER TABLE SDFUnit
       ADD  ( PRIMARY KEY (UnitID) ) ;


DROP TABLE SDFDatatype CASCADE CONSTRAINTS;

CREATE TABLE SDFDatatype (
       DatatypeID           INTEGER NOT NULL,
       DatatypeURI          VARCHAR2(500) NULL
);


ALTER TABLE SDFDatatype
       ADD  ( PRIMARY KEY (DatatypeID) ) ;


DROP TABLE SDFSchemaMapping CASCADE CONSTRAINTS;

CREATE TABLE SDFSchemaMapping (
       MappingID            INTEGER NOT NULL,
       MappingURI           VARCHAR2(500) NULL,
       MappingTypeID        INTEGER NULL,
       MappingFunctionID    INTEGER NULL
);


ALTER TABLE SDFSchemaMapping
       ADD  ( PRIMARY KEY (MappingID) ) ;


DROP TABLE SDFMappingType CASCADE CONSTRAINTS;

CREATE TABLE SDFMappingType (
       MappingTypeID        INTEGER NOT NULL,
       MappingTypeURI       VARCHAR2(500) NULL,
       superMappingID       INTEGER NULL
);


ALTER TABLE SDFMappingType
       ADD  ( PRIMARY KEY (MappingTypeID) ) ;


DROP TABLE SDFMappingFunction CASCADE CONSTRAINTS;

CREATE TABLE SDFMappingFunction (
       MappingFunctionID    INTEGER NOT NULL,
       MappingFunctionURI   VARCHAR2(500) NULL,
       hasReverseFunction   INTEGER NULL,
       FunctionTypeID       INTEGER NULL
);


ALTER TABLE SDFMappingFunction
       ADD  ( PRIMARY KEY (MappingFunctionID) ) ;


DROP TABLE SDFFunctionType CASCADE CONSTRAINTS;

CREATE TABLE SDFFunctionType (
       FunctionTypeID       INTEGER NOT NULL,
       FunctionTypeURI      VARCHAR2(500) NULL,
       superFunctionID      INTEGER NULL
);


ALTER TABLE SDFFunctionType
       ADD  ( PRIMARY KEY (FunctionTypeID) ) ;


DROP TABLE SDFAccessPattern CASCADE CONSTRAINTS;

CREATE TABLE SDFAccessPattern (
       AccessPatternID      INTEGER NOT NULL,
       AccessPatternURI     VARCHAR2(500) NULL,
       InputPatternID       INTEGER NULL,
       OutputPatternID      INTEGER NULL
);


ALTER TABLE SDFAccessPattern
       ADD  ( PRIMARY KEY (AccessPatternID) ) ;


DROP TABLE SDFInputPattern CASCADE CONSTRAINTS;

CREATE TABLE SDFInputPattern (
       InputPatternID       INTEGER NOT NULL,
       InputPatternURI      VARCHAR2(500) NULL
);


ALTER TABLE SDFInputPattern
       ADD  ( PRIMARY KEY (InputPatternID) ) ;


DROP TABLE SDFOutputPattern CASCADE CONSTRAINTS;

CREATE TABLE SDFOutputPattern (
       OutputPatternID      INTEGER NOT NULL,
       OutputPatternURI     VARCHAR2(500) NULL
);


ALTER TABLE SDFOutputPattern
       ADD  ( PRIMARY KEY (OutputPatternID) ) ;


DROP TABLE SDFEntity CASCADE CONSTRAINTS;

CREATE TABLE SDFEntity (
       EntityID             INTEGER NOT NULL,
       EntityURI            VARCHAR2(500) NULL
);


ALTER TABLE SDFEntity
       ADD  ( PRIMARY KEY (EntityID) ) ;


DROP TABLE SDFOutputAttributeBinding CASCADE CONSTRAINTS;

CREATE TABLE SDFOutputAttributeBinding (
       OutputAttributeBindingID    INTEGER NOT NULL,
       OutputAttributeBindingURI   VARCHAR2(500) NULL
);


ALTER TABLE SDFOutputAttributeBinding
       ADD  ( PRIMARY KEY (OutputAttributeBindingID) ) ;


DROP TABLE SDFPredicateType CASCADE CONSTRAINTS;

CREATE TABLE SDFPredicateType (
       PredicateTypeID      INTEGER NOT NULL,
       PredicateTypeURI     VARCHAR2(500) NULL
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
       ADD  ( FOREIGN KEY (CompareOpId)
                             REFERENCES SDFCompareOp
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
