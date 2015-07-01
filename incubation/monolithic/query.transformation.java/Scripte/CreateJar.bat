@Echo off
::Odysseus Core
cd %1\common\core
%3
jar cvf OdysseusCore.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusCore.jar %2

::Odysseus Core Server 
cd %1\server\core\core
jar cvf OdysseusServer.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusServer.jar %2

::server\relational\relational.base
cd %1\server\relational\relational.base
jar cvf OdysseusRelational.jar *.properties lib/*.jar -C bin .
xcopy /Y OdysseusRelational.jar %2
