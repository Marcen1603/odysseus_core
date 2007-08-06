DROP TABLE IF EXISTS `dynacache`.`DataTuples`;
CREATE TABLE  `dynacache`.`DataTuples` (
  `ParentId` int(10) unsigned NOT NULL COMMENT 'ID der zugeh�rigen semantischen Region',
  `TupleId` int(10) unsigned NOT NULL COMMENT 'ID des Tupels, zu dem das Attribut geh�rt',
  `Attribute` varchar(255) NOT NULL COMMENT 'Attribut',
  `Value` text NOT NULL COMMENT 'Wert',
  `Datatype` varchar(255) NOT NULL COMMENT 'Datentyp'
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Inhalte einer semantischen Region';
DROP TABLE IF EXISTS `dynacache`.`IntensionalPredicates`;
CREATE TABLE  `dynacache`.`IntensionalPredicates` (
  `ParentId` int(10) unsigned NOT NULL COMMENT 'ID der zugeh�rigen semantischen Region',
  `PredicateName` varchar(255) NOT NULL COMMENT 'Pr�dikat',
  `Operator` varchar(2) NOT NULL COMMENT 'Operator (<, >, =, <=, >=)',
  `Value` varchar(255) NOT NULL COMMENT 'Wert',
  `Type` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COMMENT='Constraint Formel zu einer semantischen Region';
DROP TABLE IF EXISTS `dynacache`.`SemanticRegions`;
CREATE TABLE  `dynacache`.`SemanticRegions` (
  `Id` int(10) unsigned NOT NULL auto_increment COMMENT 'Eindeutige ID einer semantischen Region',
  `Source` varchar(255) NOT NULL COMMENT 'URI der zugeh�rigen Datenquelle',
  `ReplacementValue` int(11) NOT NULL default '0' COMMENT 'Verdr�ngungswert',
  `Timestamp` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT 'Zeitstempel des letzten Zugriffs auf diese Region',
  PRIMARY KEY  (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=63 DEFAULT CHARSET=latin1 COMMENT='Semantische Regionen f�r DynaQuest Caching Engine';