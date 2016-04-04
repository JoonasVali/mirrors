#!/bin/bash
export MIRRORS_HOME=../..
java -jar -Djava.library.path=./$MIRRORS_HOME/sl-libs/ -Xmx512M ./$MIRRORS_HOME/lib/mirrors.jar