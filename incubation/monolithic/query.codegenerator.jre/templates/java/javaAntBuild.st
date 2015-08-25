javaAntBuild(copyJars)::=<<

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
		
		<project default="jar" name="Create Runnable Jar for Odysseus">
		
		<target name="clean">
		
		<delete dir="build"/>
		
		</target>
		
		
		<target name="compile">
		
		<mkdir dir="build/classes"/>
		
		 <javac srcdir="./src" destdir="./build/classes">   
		
		<classpath>
		
	 	$copyJars: {copyJar| 
			
			 <pathelement path="lib/$copyJar$"/>
		}$
		
			 <pathelement path="lib/gson-2.2.4.jar"/>
	
 
		</classpath>
		
		</javac>
		
		</target>
		
		
		<target name="jar" depends="compile">
		
		<mkdir dir="target"/>
		
		<jar destfile="target/OdysseusQuery.jar" filesetmanifest="mergewithoutmain" basedir="build/classes">
		
		<manifest>
		
		<attribute name="Main-Class" value="main.Main"/>
		
			<attribute name="Class-Path" value="."/>
			
				</manifest>
				
				<fileset dir="./build"/>
				<fileset dir="lib"/>
				
			 	$copyJars: {copyJar| 
			 		<zipfileset excludes="META-INF/*.SF" src="lib/$copyJar$"/>
				}$
            	<zipfileset excludes="META-INF/*.SF" src="lib/slf4j-log4j12-1.6.4.jar"/>
            	         
            	<zipfileset excludes="META-INF/*.SF" src="lib/slf4j-api-1.6.4.jar"/>
            	
            	<zipfileset excludes="META-INF/*.SF" src="lib/log4j-1.2.16.jar"/>
            	
            	<zipfileset excludes="META-INF/*.SF" src="lib/gson-2.2.4.jar"/>
            	
        	</jar>
        	
    	</target>
    	
	</project>
	

>>
