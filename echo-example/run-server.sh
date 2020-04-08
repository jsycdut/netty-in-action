#!/usr/bin/env bash

echo "========================================="
echo "Usage: bash run-server.sh port-for-server"
echo "========================================="

cd server 
mvn compile && mvn exec:java -Dexec.mainClass="com.piperstack.Server" -Dexec.args=$1
