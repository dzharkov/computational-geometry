#!/bin/sh

java -cp target/computational-geometry-1.0-SNAPSHOT-jar-with-dependencies.jar ru.spbau.mit.compgeom.`echo $1|tr '[:upper:]' '[:lower:]'`.$1Package

