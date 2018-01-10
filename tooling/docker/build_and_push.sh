#!/bin/sh

NAMESPACE="ckuka"
REPO="odysseus"
DATE=$(date +"%Y%m%d")
#CACHE="--no-cache"
CACHE=""


docker build --no-cache --build-arg date="${DATE}" $CACHE -t ${NAMESPACE}/${REPO}:${DATE} .
docker tag "${NAMESPACE}/${REPO}:${DATE}" "${NAMESPACE}/${REPO}:latest" 
docker push ckuka/odysseus:latest
