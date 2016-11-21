#!/bin/sh

cd `dirname $0`
echo Starting Application
nohup ./run.sh > /dev/null 2>&1 &
