javaClasspath(jarList)::=<<
<?xml version="1.0" encoding="UTF-8"?>
	<classpath>
			<classpathentry kind="src" path="src"/>
			<classpathentry exported="true" kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.7"/>
			<classpathentry kind="lib" path="lib"/>
	
			<classpathentry kind="lib" path="lib/slf4j-log4j12-1.6.4.jar"/>
			<classpathentry kind="lib" path="lib/slf4j-api-1.6.4.jar"/>
			<classpathentry kind="lib" path="lib/log4j-1.2.16.jar"/>
			
			$jarList: {jarFile| 
			<classpathentry kind="lib" path="lib/$jarFile$"/>
			}$
	
			classpathentry kind="output" path="build"/>
		</classpath>		
>>