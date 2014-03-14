#!/bin/sh

#
# You can set JAVA_HOME to point ot JDK 1.4 or newer version
# or this shell script will try to deterine java location using which
# --------------------------------------------------------------------


if [ -z "$JAVA_HOME" ] ; then
  JAVA=`/usr/bin/which java`
  if [ -z "$JAVA" ] ; then
    echo "Cannot find JAVA. Please set your PATH."
    exit 1
  fi
  JAVA_BIN=`dirname $JAVA`
  JAVA_HOME=$JAVA_BIN/..
else
  JAVA=$JAVA_HOME/bin/java
fi


#---------------------------------------------------------------------#
TOP=.

CP=`echo lib/*/*.jar | tr ' ' ':'`:$CP
CP=${TOP}/build/classes:$CP

if [ -z "$1" ] ; then
   echo Please specify test name.
   exit 1
fi

NAME=$1
shift


if [ "$NAME" = "server" ] ; then
    CMD="$JAVA $JAVA_OPTS -cp $CP chat.server.ServerDriver"
elif [ "$NAME" = "client" ] ; then
    CMD="$JAVA $JAVA_OPTS -cp $CP chat.client.ClientDriver"
else 
    echo "Cannot recognize the name of the test that you want to run."
    exit 1
fi

## Note that $@ is for the arguments
echo $CMD "$@"
$CMD "$@"

