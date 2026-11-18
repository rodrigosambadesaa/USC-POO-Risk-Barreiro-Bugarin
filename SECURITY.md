# Java4Shell / CVE-2026-114938

This application uses Java **21** on both the CLI and GUI Docker runtimes,
and Java 21 in Maven, so it may reach the vulnerable OpenJDK Base64 intrinsic.

The Dockerfile and CI temporarily disable the vulnerable intrinsic with
`JAVA_TOOL_OPTIONS=-XX:-UseBASE64Intrinsics`. This covers both final
runtime images, the Maven build image, and the Ubuntu/Windows CI matrix.
The Dockerfiles no longer pin a stale Java 21.0.8 image, allowing future
`docker build --pull` builds to fetch patched Eclipse Temurin 21 images
when they are published. The mitigation is temporary; replace it with
vendor-confirmed fixed releases as soon as available.

For local Java execution outside Docker, install a **vendor-confirmed
patched JDK 21**. If you cannot update yet, set the same environment
variable before running Maven or Java and restart all Java processes.
Verify with `java -XX:+PrintFlagsFinal -version`, expecting
`UseBASE64Intrinsics = false`.

The setting in GitHub does not automatically change already-running
containers. Rebuild and redeploy both CLI and GUI images after the PR merges.

References:
- https://www.cert.europa.eu/publications/security-advisories/2026-021/
- https://adoptium.net/news/2026/11/temurin-november-2026-cspu-java4shell/
