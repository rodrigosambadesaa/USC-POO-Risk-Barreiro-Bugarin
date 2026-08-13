#!/bin/sh
set -eu

cleanup() {
  trap - EXIT INT TERM
  for process in ${JAVA_PID:-} ${NOVNC_PID:-} ${VNC_PID:-} ${OPENBOX_PID:-} ${XVFB_PID:-}; do
    if [ -n "$process" ]; then
      kill "$process" 2>/dev/null || true
    fi
  done
  wait 2>/dev/null || true
}
trap cleanup EXIT INT TERM

Xvfb "$DISPLAY" -screen 0 "${VNC_GEOMETRY}x24" -nolisten tcp -ac &
XVFB_PID=$!

attempt=0
until xdpyinfo -display "$DISPLAY" >/dev/null 2>&1; do
  attempt=$((attempt + 1))
  if [ "$attempt" -ge 50 ]; then
    echo "Xvfb no inició correctamente" >&2
    exit 1
  fi
  sleep 0.1
done

openbox >/tmp/openbox.log 2>&1 &
OPENBOX_PID=$!
x11vnc -display "$DISPLAY" -rfbport 5900 -forever -shared -nopw -noxdamage -quiet &
VNC_PID=$!
websockify --web=/usr/share/novnc 6080 localhost:5900 &
NOVNC_PID=$!

java \
  -Dfile.encoding=UTF-8 \
  -Duser.language=es \
  -Duser.country=ES \
  -Drisk.output.dir=/app/output \
  -cp '/app/risk.jar:/app/lib/*' \
  gal.sdc.usc.risk.adapters.javafx.JavaFxApplication &
JAVA_PID=$!
wait "$JAVA_PID"
