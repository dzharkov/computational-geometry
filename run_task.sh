#!/bin/sh

PACKAGE=$1
shift 1
$JAVA_HOME/bin/java -cp target/computational-geometry-1.0-SNAPSHOT-jar-with-dependencies.jar ru.spbau.mit.compgeom.$PACKAGE.MainKt $@

