#!/bin/sh
#更新，打包，推送
cd $1;

npm i --only=prod

shepherd build production;

docker build -t git.hshbao.com:5000/$2 -f $3 .;

docker push git.hshbao.com:5000/$2;

