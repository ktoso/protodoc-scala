#!/bin/sh

alias sbt="java -Xmx512M -XX:MaxPermSize=256m -jar $HOME/opt/tools/sbt/sbt-launch.jar '$@'"

sbt clean export-jars package

cd target/scala-2.9.1
java -jar protodoc-scala_2.9.1-1.0.jar $@
