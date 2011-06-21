#!/bin/sh
while true ; do
  java -Xmx512M -jar /home/ktoso/coding/sbt/sbt-launch.jar test
  testsPassed=$?
  if [ testsPassed == 1  ] ; then
    notify-send -i /home/ktoso/red.png 'Tests failed'
  else
    notify-send -i /home/ktoso/red.png 'Tests failed'
  fi

  sleep 15
done

