# syntax=docker/dockerfile:1.7
FROM maven:3.9.11-eclipse-temurin-21-alpine AS build
WORKDIR /workspace
COPY pom.xml ./
COPY config ./config
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency

FROM build AS test
CMD ["mvn", "-B", "clean", "verify"]

FROM eclipse-temurin:21.0.8_9-jre-alpine AS runtime
RUN addgroup -S risk && adduser -S -G risk -h /app risk
WORKDIR /app
RUN mkdir -p /app/output && chown risk:risk /app/output
COPY --from=build --chown=risk:risk /workspace/target/risk-2.0.0-SNAPSHOT-all.jar /app/risk.jar
USER risk:risk
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Duser.language=es", "-Duser.country=ES", "-Drisk.output.dir=/app/output", "-jar", "/app/risk.jar"]

FROM eclipse-temurin:21.0.8_9-jre-jammy AS gui
RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install --yes --no-install-recommends \
        curl \
        fonts-dejavu-core \
        libasound2 \
        libgl1 \
        libgtk-3-0 \
        libxi6 \
        libxrender1 \
        libxtst6 \
        novnc \
        openbox \
        websockify \
        x11-utils \
        x11vnc \
        xvfb \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /tmp/.X11-unix \
    && chmod 1777 /tmp/.X11-unix \
    && groupadd --gid 10001 risk \
    && useradd --uid 10001 --gid risk --home-dir /app --create-home --shell /usr/sbin/nologin risk
WORKDIR /app
RUN mkdir -p /app/output && chown risk:risk /app/output
COPY --from=build --chown=risk:risk /workspace/target/risk-2.0.0-SNAPSHOT.jar /app/risk.jar
COPY --from=build --chown=risk:risk /workspace/target/dependency /app/lib
COPY --chown=risk:risk docker/gui/entrypoint.sh /app/entrypoint.sh
COPY docker/gui/index.html /usr/share/novnc/index.html
RUN chmod 0555 /app/entrypoint.sh
USER risk:risk
ENV DISPLAY=:99 LANG=C.UTF-8 LC_ALL=C.UTF-8 VNC_GEOMETRY=1440x900
EXPOSE 6080
HEALTHCHECK --interval=10s --timeout=3s --start-period=20s --retries=6 \
  CMD curl --fail --silent http://127.0.0.1:6080/vnc.html >/dev/null || exit 1
ENTRYPOINT ["/app/entrypoint.sh"]
