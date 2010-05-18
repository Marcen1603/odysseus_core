#!/bin/sh

folders="action base metadata planmanagement pql relational scheduler"

for f in $folders; do
    for s in $(find ../../$f -mindepth 1 -maxdepth 1 -type d); do
	basename=${s##*/}
	echo "Processing $basename"
	artifactId=${basename#*de.uniol.inf.is.odysseus.}
	artifactId=$(echo $artifactId | tr "." "-")
	if [ -d $s/src ]; then
	    mkdir -p $f/odysseus-$artifactId/src/main/java
	    mkdir -p $f/odysseus-$artifactId/src/main/resources
	    mkdir -p $f/odysseus-$artifactId/src/test/java
	    mkdir -p $f/odysseus-$artifactId/src/test/resources
	    cp -uR $s/src/* $f/odysseus-$artifactId/src/main/java/
	fi
    done
done
