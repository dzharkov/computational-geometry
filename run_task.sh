#!/bin/sh

PACKAGE=$1
shift 1
java -cp target/computational-geometry-1.0-SNAPSHOT-jar-with-dependencies.jar ru.spbau.mit.compgeom.`echo $PACKAGE|tr '[:upper:]' '[:lower:]'`.${PACKAGE}Package $@

