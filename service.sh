#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Keep the pwd in mind!
# Example: RUN="java -jar $DIR/target/magic.jar"
RUN=
NAME=

DATA_FILE=$2

PIDFILE=/tmp/$NAME.pid
LOGFILE=/tmp/$NAME.log

start() {
    if [ -f $PIDFILE ]; then
        if kill -0 $(cat $PIDFILE); then
            echo 'Service already running' >&2
            return 1
        else
            rm -f $PIDFILE
        fi
    fi
    local CMD="$RUN $DATA_FILE &> \"$LOGFILE\" & echo \$!"
    sh -c "$CMD" > $PIDFILE
}

stop() {
    if [ ! -f $PIDFILE ] || ! kill -0 $(cat $PIDFILE); then
        echo 'Service not running' >&2
        return 1
    fi
    kill -15 $(cat $PIDFILE) && rm -f $PIDFILE
}


case $1 in
    start)
        start
        ;;
    stop)
        stop
        ;;
    block)
        start
        sleep infinity
        ;;
    *)
        echo "Usage: $0 {start|stop|block} DATA_FILE"
esac
