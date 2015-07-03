@Echo off
::Odysseus Core
cd %1\common\core
%3
xcopy %1\common\core\src\* %1\common\core\bin /s /e /c /y
jar cvf OdysseusCore.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusCore.jar %2

::Odysseus Core Server 
cd %1\server\core\core
xcopy %1\server\core\core\src\* %1\server\core\core\bin /s /e /c /y
jar cvf OdysseusServer.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusServer.jar %2

::server\relational\relational.base
cd %1\server\relational\relational.base
xcopy %1\server\relational\relational.base\src\* %1\server\relational\relational.base\bin /s /e /c /y
jar cvf OdysseusRelational.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusRelational.jar %2

::common\resources\de.uniol.inf.is.odysseus.slf4j
cd %1\common\resources\de.uniol.inf.is.odysseus.slf4j
xcopy %1\common\resources\de.uniol.inf.is.odysseus.slf4j\src\* %1\common\resources\de.uniol.inf.is.odysseus.slf4j\bin /s /e /c /y
jar cvf OdysseusLogger.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusLogger.jar %2


::server\relational\parser.pql.relational
cd %1\server\relational\parser.pql.relational
xcopy %1\server\relational\parser.pql.relational\src\* %1\server\relational\parser.pql.relational\bin /s /e /c /y
jar cvf OdysseusParserPQLRelational.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusParserPQLRelational.jar %2

::common\mep
cd %1\common\mep
xcopy %1\common\mep\src\* %1\common\mep\bin /s /e /c /y
jar cvf OdysseusMEP.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusMEP.jar %2

::common\mep.matrix
cd %1\common\mep.matrix
xcopy %1\common\mep.matrix\src\* %1\common\mep.matrix\bin /s /e /c /y
jar cvf OdysseusMEPMatrix.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusMEPMatrix.jar %2


::common\interval\datatype.interval
cd %1\common\interval\datatype.interval
xcopy %1\common\interval\datatype.interval\src\* %1\common\interval\datatype.interval\bin /s /e /c /y
jar cvf OdysseusDatatypeInterval.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusDatatypeInterval.jar %2

::server\memstore\de.uniol.inf.is.odysseus.memstore.mdastore
cd %1\server\memstore\de.uniol.inf.is.odysseus.memstore.mdastore
xcopy %1\server\memstore\de.uniol.inf.is.odysseus.memstore.mdastore\src\* %1\server\memstore\de.uniol.inf.is.odysseus.memstore.mdastore\bin /s /e /c /y
jar cvf OdysseusMemstoreMdastore.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusMemstoreMdastore.jar %2




