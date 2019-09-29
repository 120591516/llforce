#!/bin/bash
img_name='llvision/force:1.0.0'
container_name='force'
config_path=/usr/local/force/config/
logs_path=/usr/local/force/logs/
expose_port=48861

mkdir -p $log_path

docker rm -f $container_name
 
docker run -d --rm --name "$container_name"\
 -e TZ="Asia/Shanghai"\
 -v $config_path:/app/config/\
 -v $logs_path:/app/logs/\
 -p $expose_port:8861\
 $img_name

