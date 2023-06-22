#!/bin/bash

npm --prefix frontend run build
rm -rf backend/src/main/resources/META-INF/resources/
cp -r frontend/build/ backend/src/main/resources/META-INF/resources/
mvn clean install -f backend -DskipTests
