#!/bin/sh

folders="base metadata metadata/pn metadata/interval metadata/priority planmanagement pql relational scheduler"
#createGlobalPom=0
#if [ ! -f pom.xml ]; then
#    createGlobalPom=1
#    cat > pom.xml <<EOF
#<?xml version="1.0" encoding="UTF-8"?>
#<project
#   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
#   xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
#  <modelVersion>4.0.0</modelVersion>
#  <parent>
#    <groupId>de.uniol.inf.is.odysseus</groupId>
#    <artifactId>odysseus-pom</artifactId>
#    <version>1.0-SNAPSHOT</version>
#  </parent>
#  <artifactId>odysseus-parent</artifactId>
#  <packaging>pom</packaging>
#  <name>Odysseus</name>
#
#  <modules>
#EOF
#fi

for f in $folders; do
    mkdir -p $f
    module=$(echo $f | tr "/" "-")
#    createLocalPom=0
#    if [ ! -f $f/pom.xml ]; then
#	createLocalPom=1
#	echo "<module>$module</module>" >> pom.xml
#	cat > $f/pom.xml <<EOF
#<?xml version="1.0" encoding="UTF-8"?>
#<project
#   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
#   xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
#  <modelVersion>4.0.0</modelVersion>
#  <parent>
#    <groupId>de.uniol.inf.is.odysseus</groupId>
#    <artifactId>odysseus-pom</artifactId>
#    <version>1.0-SNAPSHOT</version>
#  </parent>
#  <artifactId>odysseus-$module-parent</artifactId>
#  <packaging>pom</packaging>
#  <name>Odysseus :: $f</name>
#
#  <modules>
#EOF
    fi
    for s in $(find ../../$f -mindepth 1 -maxdepth 1 -type d); do
	basename=${s##*/}
	echo "Processing $basename"
	artifactId=${basename#*de.uniol.inf.is.odysseus.}
	artifactId=$(echo $artifactId | tr "." "-")
	artifactId=$(echo $artifactId | tr "/" "-")
	if [ -d $s/src ]; then
	    mkdir -p $f/odysseus-$artifactId/src/main/java
	    mkdir -p $f/odysseus-$artifactId/src/main/resources
	    mkdir -p $f/odysseus-$artifactId/src/test/java
	    mkdir -p $f/odysseus-$artifactId/src/test/resources
	    cp -uR $s/src/* $f/odysseus-$artifactId/src/main/java/
#	    if [ ! -f $f/odysseus-$artifactId/pom.xml ]; then
#		echo "<module>odysseus-$artifactId</module>" >> $f/pom.xml
#		cat > $f/odysseus-$artifactId/pom.xml <<EOF
#<?xml version="1.0" encoding="UTF-8"?>
#<project
#   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"
#   xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
#  <modelVersion>4.0.0</modelVersion>
#  <parent>
#    <groupId>de.uniol.inf.is.odysseus</groupId>
#    <artifactId>odysseus-pom</artifactId>
#    <version>1.0-SNAPSHOT</version>
#  </parent>
#  <artifactId>odysseus-$artifactId</artifactId>
#  <packaging>bundle</packaging>
#  <name>Odysseus :: $artifactId</name>
#  <properties>
#    <odysseus.osgi.import>
#      sun.misc;resolution:=optional,
#      *
#    </odysseus.osgi.import>
#    <odysseus.osgi.export>
#      $basename*;version=\${project.version}
#    </odysseus.osgi.export>
#    <odysseus.osgi.bundles>
#    </odysseus.osgi.bundles>
#  </properties>
#
#  <build>
#    <plugins>
#      <plugin>
#        <groupId>org.codehaus.mojo</groupId>
#        <artifactId>cobertura-maven-plugin</artifactId>
#      </plugin>
#      <plugin>
#        <groupId>org.apache.felix</groupId>
#        <artifactId>maven-bundle-plugin</artifactId>
#        <extensions>true</extensions>
#      </plugin>
#      <plugin>
#        <groupId>org.ops4j</groupId>
#        <artifactId>maven-pax-plugin</artifactId>
#        <configuration>
#          <provision>
#            <param>--platform=equinox</param>
#          </provision>
#        </configuration>
#      </plugin>
#    </plugins>
#  </build>
#  <dependencies>
#    <dependency>
#      <groupId>\${project.groupId}</groupId>
#      <artifactId>odysseus-base</artifactId>
#      <version>\${project.version}</version>
#    </dependency>
#    <dependency>
#      <groupId>org.osgi</groupId>
#      <artifactId>org.osgi.compendium</artifactId>
#    </dependency>
#    <dependency>
#      <groupId>org.slf4j</groupId>
#      <artifactId>slf4j-api</artifactId>
#      <scope>provided</scope>
#    </dependency>
#    <dependency>
#      <groupId>org.slf4j</groupId>
#      <artifactId>slf4j-log4j12</artifactId>
#      <scope>provided</scope>
#    </dependency>
#    <dependency>
#        <groupId>org.slf4j</groupId>
#        <artifactId>jcl104-over-slf4j</artifactId>
#        <scope>provided</scope>
#    </dependency>
#
#    <dependency>
#      <groupId>org.apache.felix</groupId>
#      <artifactId>org.osgi.core</artifactId>
#      <version>1.0.0</version>
#    </dependency>
#  </dependencies>
#</project>
#EOF
#	    fi
	fi
    done
#    if [ createLocalPom = 1 ]; then
#    cat >> $f/pom.xml <<EOF
#  </modules>
#</project>
#EOF
#    fi
done
#if [ createGlobalPom = 1 ]; then
#    cat >> pom.xml <<EOF
#  </modules>
#</project>
#EOF
#fi

 mkdir -p resources/drools-ruleagent/src/main/java
 mkdir -p resources/drools-ruleagent/src/main/resources
 mkdir -p resources/drools-ruleagent/src/test/java
 mkdir -p resources/drools-ruleagent/src/test/resources
 cp -uR ../../resources/de.uniol.inf.is.drools.osgi_rule_agent/src/* resources/drools-ruleagent/src/main/java/