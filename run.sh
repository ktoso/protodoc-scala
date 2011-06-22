#!/bin/sh
sbt clean
sbt collect-jars
sbt package

cd target/scala_2.8.1/
java -jar protodoc-1.0.jar $@
