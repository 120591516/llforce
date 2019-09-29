#!/bin/bash
img_name='llvision/force:1.0.0'

echo "删除镜像"
docker rmi -f "$img_name"

echo "开始创建新镜像"
docker build -t "$img_name" .
