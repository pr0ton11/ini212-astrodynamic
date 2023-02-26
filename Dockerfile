FROM azul/zulu-openjdk-alpine:17-latest as build

ARG MAVEN_VERSION=3.9.0
ARG SHA=1ea149f4e48bc7b34d554aef86f948eca7df4e7874e30caf449f3708e4f8487c71a5e5c072a05f17c60406176ebeeaf56b5f895090c7346f8238e2da06cf6ecd
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

# Add maven to the container
RUN apk --no-cache add curl \
  && mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && echo "${SHA}  /tmp/apache-maven.tar.gz" | sha512sum -c - \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && rm -f /usr/bin/mvn \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

COPY astrodynamic/ /astrodynamic
WORKDIR /astrodynamic
# Run init tests
RUN mvn clean test
# Run jar package builds
RUN mvn clean package

FROM azul/zulu-openjdk-alpine:17-latest

WORKDIR /astrodynamic
COPY --from=build /astrodynamic/target/*.jar /astrodynamic/
