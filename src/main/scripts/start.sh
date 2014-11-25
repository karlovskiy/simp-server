#!/bin/sh

PRGDIR=`dirname "$0"`

[ -z "$SIMP_HOME" ] && SIMP_HOME=`cd "$PRGDIR" >/dev/null; pwd`

SIMP_LOG="$SIMP_HOME"/server.log
SIMP_JAR="$SIMP_HOME"/lib/simp-server.jar
SIMP_PID="$SIMP_HOME"/simp.pid


if [ -z "$JAVA_HOME" ]; then
  JAVA_PATH=`which java 2>/dev/null`
  if [ -n "$JAVA_PATH" ]; then
    JAVA_PATH=`dirname $JAVA_PATH 2>/dev/null`
    JAVA_HOME=`dirname $JAVA_PATH 2>/dev/null`
  fi
  if [ -z "$JAVA_HOME" ]; then
    if [ -x /usr/bin/java ]; then
      JAVA_HOME=/usr
      echo "$JAVA_HOME"
    fi
  fi
  if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME environment variable isn't defined. It's needed to run this program"
    exit 1
  fi
fi

if [ -z "$_JAVACMD" ]; then
  _JAVACMD="$JAVA_HOME"/bin/java
fi

echo "Using SIMP_HOME:  $SIMP_HOME"
echo "Using JAVA_HOME:  $JAVA_HOME"

if [ ! -z "$SIMP_PID" ]; then
  if [ -f "$SIMP_PID" ]; then
    if [ -s "$SIMP_PID" ]; then
      echo "Existing PID file found during start."
      if [ -r "$SIMP_PID" ]; then
        PID=`cat "$SIMP_PID"`
        ps -p $PID >/dev/null 2>&1
        if [ $? -eq 0 ] ; then
          echo "Simp server appears to still be running with PID $PID. Start aborted."
          exit 1
        else
          echo "Removing/clearing stale PID file."
          rm -f "$SIMP_PID" >/dev/null 2>&1
          if [ $? != 0 ]; then
            if [ -w "$SIMP_PID" ]; then
              cat /dev/null > "$SIMP_PID"
            else
              echo "Unable to remove or clear stale PID file. Start aborted."
              exit 1
            fi
          fi
        fi
      else
        echo "Unable to read PID file. Start aborted."
        exit 1
      fi
    else
      rm -f "$SIMP_PID" >/dev/null 2>&1
      if [ $? != 0 ]; then
        if [ ! -w "$SIMP_PID" ]; then
          echo "Unable to remove or write to empty PID file. Start aborted."
          exit 1
        fi
      fi
    fi
  fi
fi


touch "$SIMP_LOG"
eval "\"$_JAVACMD\"" $JAVA_OPTS \
  -Dsimp.home="\"$SIMP_HOME\"" \
  -jar "\"$SIMP_JAR\"" \
  >> "$SIMP_LOG" 2>&1 "&"

echo $! > "$SIMP_PID"
echo "Simp server started with PID $!."