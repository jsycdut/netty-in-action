#!/usr/bin/env bash

echo "================================================="
echo "Usage: bash run-client.sh server-host server-port"
echo "================================================="

cd client 
mvn compile && mvn exec:java -Dexec.mainClass="com.piperstack.Client" -Dexec.args="$1 $2"
