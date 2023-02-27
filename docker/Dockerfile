FROM amazonlinux:latest

ARG MAVEN_VERSION=3.6.3
ARG DOCKER_GID=130

ENV PATH="/opt:/opt/apache-maven-$MAVEN_VERSION/bin:$PATH" \
    DEBIAN_FRONTEND=noninteractive

CMD ["bash"]
RUN mkdir /opt/java
ENV JAVA_HOME=/opt/java
ENV PATH=/opt/java/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
RUN yum -y update \
&& yum install yum-utils -y \
&& yum install -y wget tar gzip bzip2-devel ed gcc gcc-c++ gcc-gfortran \
        less libcurl-devel openssl openssl-devel readline-devel xz-devel freetype-devel \
        zlib-devel glibc-static libcxx libcxx-devel llvm-toolset-7 zlib-static shadow-utils unzip git curl \
 && curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.3/install.sh | bash \
 && . ~/.nvm/nvm.sh \
 && nvm install 16 \
  && rm -rf /var/cache/yum \
 && rm -rf /var/lib/apt/lists/* \
 && yum -y update \
 && cd /opt \
 && curl -LO https://github.com/making/rsc/releases/download/0.7.1/rsc-x86_64-pc-linux \
 && mv rsc-x86_64-pc-linux rsc \
 && chmod +x rsc \
 && curl -LO https://repo1.maven.org/maven2/org/apache/maven/apache-maven/$MAVEN_VERSION/apache-maven-$MAVEN_VERSION-bin.zip \
 && unzip -o apache-maven-${MAVEN_VERSION}-bin.zip \
 && rm apache-maven-${MAVEN_VERSION}-bin.zip \
 && curl -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-22.3.1/graalvm-ce-java19-linux-amd64-22.3.1.tar.gz | tar -zx  --overwrite --directory /opt/java --strip-components=1 \
 && curl -L https://github.com/fullstorydev/grpcurl/releases/download/v1.5.0/grpcurl_1.5.0_linux_x86_64.tar.gz | tar -xz \
 && rm LICENSE \
 && curl -LO https://github.com/tecfu/tty-table/archive/master.zip \
 && unzip -o master.zip \
 && rm master.zip \
 && cd tty-table-master \
 && npm i -g \
 && npm cache clean --force \
 && rm -rf /var/cache/apt/archives \
 && rm -rf /var/lib/apt/lists/* \
 && cd / \
 && export ENTRYKIT_VERSION=0.4.0 \
 && curl -L https://github.com/progrium/entrykit/releases/download/v${ENTRYKIT_VERSION}/entrykit_${ENTRYKIT_VERSION}_Linux_x86_64.tgz | tar zx \
 && chmod +x entrykit \
 && mv entrykit /bin/entrykit \
 && entrykit --symlink \
 && yum-config-manager --add-repo https://cli.github.com/packages/rpm/gh-cli.repo \
 && yum install gh -y \
 && gu install llvm-toolchain \
 && gu install native-image \
 && echo '%sudo ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

CMD native-image --version