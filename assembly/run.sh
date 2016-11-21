#!/bin/sh
DIR=`dirname $0`
cd $DIR
java -server \
    -Xmx1G \
    -Djava.net.preferIPv4Stack=true \
    -Dcom.sun.management.jmxremote \
    -cp .:./resources:./lib/* ${start-class} "$@"