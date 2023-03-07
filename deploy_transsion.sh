#!/bin/bash

##### This Script is to dockerize apps #####
APPNAME='partner-apps-spring-boot'
DIR='/home/noetic/partner_apps/partner-apps-spring-boot/'
DIRLOG="/var/log/apps/springTranssion/"
LOG='mo-sptranssion'
IMAGE='mo-sptranssion'
echo "Pull request from GitHub repo to ~${APPNAME} directory ..." && echo "" && \
[ ! -d "${DIR}" ] && sudo mkdir -p "${DIR}"
[ ! -d "${DIRLOG}" ] && sudo mkdir -p "${DIRLOG}"
cd ${DIR}
#git stash && sudo git pull origin production &&

if [ $( sudo docker ps -a | grep ${IMAGE} | wc -l ) -gt 0 ]; then
echo "container ~${IMAGE} already exists"
echo "Stopping ${IMAGE}_c from production environment ..." && echo "" && \
docker stop ${IMAGE}_c  &&
echo "destroying ${IMAGE}_c container  ..." && echo "" && \
docker rm -f ${IMAGE}_c &&
echo "deleting Docker Image  ..." && echo "" &&
docker rmi -f ${IMAGE}
else
echo "container ~${IMAGE} does not exist"
fi
mvn clean package &&
echo "Creating Build for newly pulled code ..." && echo "" && \
docker build -t ${IMAGE} . &&
echo "Deploying the container ${IMAGE}_c ..." && echo "" && \
[ ! -d "${DIRLOG}" ] && mkdir -p "${DIRLOG}"
docker run -d --restart unless-stopped -p 10004:10004 -v ${DIRLOG}:${DIRLOG} --name ${IMAGE}_c ${IMAGE}  &&
echo "Build Successfull ..." && echo "" &&

echo "Success"
exit
