#!/bin/sh


echo "Are we in odysseus root directory? (y/n)"
read yesno
if [ $yesno != "y" ]; then
    exit 1
fi

git checkout master
git branch next
git checkout next
git branch migration
git checkout migration

partyDir="3rdParty/maven"
cp -uR $partyDir/odysseus-parent .
git add odysseus-parent/pom.xml

folders=". action application base broker cep metadata metadata/pn metadata/interval metadata/priority new_transformation p2p p2p/adminpeer p2p/base p2p/distribution p2p/execution p2p/jxta p2p/operatorpeer p2p/splitting p2p/superpeer p2p/thinpeer planmanagement pql rcp relational scheduler sensor viewer webservice"

cat > pom.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
   xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>de.uniol.inf.is.odysseus</groupId>
    <artifactId>odysseus-pom</artifactId>
    <version>1.0-SNAPSHOT</version>
  </parent>
  <artifactId>odysseus-parent</artifactId>
  <packaging>pom</packaging>
  <name>Odysseus</name>

  <modules>
EOF

for f in $folders; do
    mkdir -p $f
    module=$(echo $f | tr "/" "-")
    cat > $f/pom.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
   xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>de.uniol.inf.is.odysseus</groupId>
    <artifactId>odysseus-pom</artifactId>
    <version>1.0-SNAPSHOT</version>
  </parent>
  <artifactId>odysseus-$module-parent</artifactId>
  <packaging>pom</packaging>
  <name>Odysseus :: $module</name>

  <modules>
EOF
    for s in $(find $f -mindepth 1 -maxdepth 1 -type d -name 'de.uniol*'); do
	basename=${s##*/}
	isin=0
	for i in $folders; do
	    if [ $i = $basename ]; then
		isin=1
	    fi
	done
	if [ $isin = 0 ]; then
	    echo "Processing $basename"
	    artifactId=${basename#*de.uniol.inf.is.odysseus.}
	    artifactId=$(echo $artifactId | tr "." "-")
	    artifactId=$(echo $artifactId | tr "/" "-")
	    if [ -d $s/src ]; then
		echo "<module>odysseus-$artifactId</module>" >> $f/pom.xml
		mkdir -p $f/odysseus-$artifactId/src/main/java
		mkdir -p $f/odysseus-$artifactId/src/main/resources
		mkdir -p $f/odysseus-$artifactId/src/test/java
		mkdir -p $f/odysseus-$artifactId/src/test/resources
		if [ -d $partyDir/$f/odysseus-$artifactId ]; then
		    cp -u $partyDir/$f/odysseus-$artifactId/pom.xml $f/odysseus-$artifactId/
		else
		    echo "Generate pom"
		    cat > $f/odysseus-$artifactId/pom.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
   xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>de.uniol.inf.is.odysseus</groupId>
    <artifactId>odysseus-pom</artifactId>
    <version>1.0-SNAPSHOT</version>
  </parent>
  <artifactId>odysseus-$artifactId</artifactId>
  <packaging>bundle</packaging>
  <name>Odysseus :: $artifactId</name>
  <properties>
    <odysseus.osgi.import>
      sun.misc;resolution:=optional,
      *
    </odysseus.osgi.import>
    <odysseus.osgi.export>
      $basename*;version=\${project.version}
    </odysseus.osgi.export>
    <odysseus.osgi.bundles>
    </odysseus.osgi.bundles>
  </properties>

  <build>
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>cobertura-maven-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.apache.felix</groupId>
        <artifactId>maven-bundle-plugin</artifactId>
        <extensions>true</extensions>
      </plugin>
      <plugin>
        <groupId>org.ops4j</groupId>
        <artifactId>maven-pax-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.apache.felix</groupId>
        <artifactId>maven-scr-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
  
  <dependencies>
    <dependency>
      <groupId>\${project.groupId}</groupId>
      <artifactId>odysseus-base</artifactId>
      <version>\${project.version}</version>
    </dependency>
  </dependencies>
</project>
EOF
		fi
		git add $f/odysseus-$artifactId/pom.xml
		git mv -kf $s/META-INF $f/odysseus-$artifactId/src/main/resources/
		git mv -kf $s/OSGI-INF $f/odysseus-$artifactId/src/main/resources/
		git mv -kf $s/src/* $f/odysseus-$artifactId/src/main/java/
	    fi
	fi
    done
    cat >> $f/pom.xml <<EOF
  </modules>
</project>
EOF
    git add $f/pom.xml
done
mkdir -p resources/drools-pathscanner/src/main/java
mkdir -p resources/drools-pathscanner/src/main/resources
mkdir -p resources/drools-pathscanner/src/test/java
mkdir -p resources/drools-pathscanner/src/test/resources
git mv -kf resources/de.uniol.inf.is.drools.osgi_path_scanner/src/org resources/drools-pathscanner/src/main/java/
cp -u $partyDir/resources/drools-pathscanner/pom.xml resources/drools-pathscanner/
git add resources/drools-pathscanner/pom.xml

mkdir -p resources/drools-ruleagent/src/main/java
mkdir -p resources/drools-ruleagent/src/main/resources
mkdir -p resources/drools-ruleagent/src/test/java
mkdir -p resources/drools-ruleagent/src/test/resources
git mv -kf resources/de.uniol.inf.is.drools.osgi_rule_agent/src/de resources/drools-ruleagent/src/main/java/
cp -u $partyDir/resources/drools-ruleagent/pom.xml resources/drools-ruleagent/
git add resources/drools-ruleagent/pom.xml

mkdir -p resources/drools/src/main/java
mkdir -p resources/drools/src/main/resources
mkdir -p resources/drools/src/test/java
mkdir -p resources/drools/src/test/resources
git mv -kf resources/de.uniol.org.drools/src/* resources/drools/src/main/java/
cp -u $partyDir/resources/drools/pom.xml resources/drools/
git add resources/drools/pom.xml

git mv -kf scheduler/odysseus-scheduler2-0 scheduler/odysseus-scheduler
git mv -kf scheduler/odysseus-scheduler-singlethreadscheduler2-0 scheduler/odysseus-scheduler-singlethreadscheduler
cp $partyDir/scheduler/odysseus-scheduler/pom.xml scheduler/odysseus-scheduler/
cp $partyDir/scheduler/odysseus-scheduler-singlethreadscheduler/pom.xml scheduler/odysseus-scheduler-singlethreadscheduler
git add scheduler/odysseus-scheduler/pom.xml
git add scheduler/odysseus-scheduler-singlethreadscheduler/pom.xml

    cat > resources/pom.xml <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
   xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>de.uniol.inf.is.odysseus</groupId>
    <artifactId>odysseus-pom</artifactId>
    <version>1.0-SNAPSHOT</version>
  </parent>
  <artifactId>odysseus-$module-parent</artifactId>
  <packaging>pom</packaging>
  <name>Odysseus :: $module</name>

  <modules>
   <module>drools</module>
   <module>drools-ruleagent</module>
   <module>drools-pathscanner</module>
  </modules>
</project>
EOF
git add resources/pom.xml

for s in $(find . -mindepth 1 -maxdepth 1 -type f -name 'pom.xml'); do
    basename=${s%/*}
    echo "<module>$basename</module>" >> pom.xml
done

cat >> pom.xml <<EOF
  </modules>
</project>
EOF
git add pom.xml

echo "Migration processed. Ready to merge? (y/n)"
read yesno
if [ $yesno = "y" ]; then
    git commit
    git checkout next
    git merge migration
else
    git checkout master
    git branch -D migration
fi
