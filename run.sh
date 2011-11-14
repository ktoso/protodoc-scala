#!/bin/sh

alias sbt="java -Xmx512M -jar $HOME/coding/sbt/sbt-launch.jar '$@'"

sbt clean
sbt collect-jars
sbt package

cd target/scala-2.9.1
java -jar protodoc-scala_2.9.1-1.0.jar $@
