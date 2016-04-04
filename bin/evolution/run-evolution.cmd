@echo off
SET MIRRORS_HOME=../..
java -jar -Djava.library.path=./%MIRRORS_HOME%/sl-libs/ -Xmx2048M ./%MIRRORS_HOME%/lib/mirrors.jar evolution ./%MIRRORS_HOME%/bin/evolution/evolution.properties