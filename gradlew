#!/bin/sh
# Gradle Wrapper startup script for Unix-based systems

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
APP_HOME=$(cd "$(dirname "$0")" && pwd -P)

APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD=maximum

warn () {
    echo "$*"
}

die () {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false
case "$( uname )" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MSYS* | MINGW* )
    msys=true
    ;;
  NONSTOP* )
    nonstop=true
    ;;
esac

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD=java
    if ! command -v java >/dev/null 2>&1
    then
        die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment (or add Java to your PATH)
to match the location of your Java installation."
    fi
fi

# Increases the maximum file descriptors if we can.
if ! "$cygwin" && ! "$darwin" && ! "$nonstop" ; then
    case $MAX_FD in
      max*)
        MAX_FD=$(ulimit -H -n) ||
            warn "Could not query maximum file descriptor limit"
        ;;
    esac
    case $MAX_FD in
      '' | soft) :;;
      *)
        ulimit -n "$MAX_FD" ||
            warn "Could not set maximum file descriptor limit to $MAX_FD"
        ;;
    esac
fi

# Collect all arguments for the java command
APP_ARGS=$(printf ' "%s"' "$@")

# Determine the full path of the Gradle wrapper jar
WRAPPER_LAUNCHER=org.gradle.wrapper.GradleWrapperMain

eval set -- $DEFAULT_JVM_OPTS '"$JAVA_OPTS"' '"$GRADLE_OPTS"' "\"-Dorg.gradle.appname=$APP_BASE_NAME\"" -classpath '"$CLASSPATH"' '"$WRAPPER_LAUNCHER"' $APP_ARGS

exec "$JAVACMD" "$@"
