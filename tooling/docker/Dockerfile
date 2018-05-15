FROM openjdk:8

ARG date

MAINTAINER Christian Kuka <christian@kuka.cc>
LABEL Description="Docker Image for running the Odysseus data stream management system" Version="${date}"  URL="http://odysseus.informatik.uni-oldenburg.de"

# Install required packages
RUN apt-get -y update && apt-get install -yq --no-install-recommends \
    wget \
    unzip

# Download nightly build
RUN mkdir -p /usr/lib/odysseus && \
    wget -c http://odysseus.offis.uni-oldenburg.de/download/products/server/lastBuild/odysseus.server-linux.gtk.x86_64.zip -O /tmp/odysseus.zip && \
    unzip /tmp/odysseus.zip -d /usr/lib/odysseus && \
    rm -f /tmp/odysseus.zip

# Define additional VM parameters
RUN echo "-XX:NativeMemoryTracking=summary" >> /usr/lib/odysseus/odysseus.ini && \
    echo "-XX:+HeapDumpOnOutOfMemoryError" >> /usr/lib/odysseus/odysseus.ini && \
    echo "-XX:HeapDumpPath=/var/log/odysseus/dumps" >> /usr/lib/odysseus/odysseus.ini && \
    echo "-XX:ErrorFile=/var/log/odysseus/dumps/hs_err_pid%p.log" >> /usr/lib/odysseus/odysseus.ini && \
    sed -i "s/org\.apache\.log4j\.ConsoleAppender/org\.apache\.log4j\.FileAppender\nlog4j\.appender\.default\.File=\/var\/log\/odysseus\/odysseus\.log/" /usr/lib/odysseus/plugins/de.uniol.inf.is.odysseus.slf4j_1.7.16/log4j.properties

# Create group, user, and change timezone for UTC
RUN groupadd odysseus && \
    useradd -r -g odysseus -s /bin/bash -d /var/lib/odysseus -m odysseus && \
    mkdir -p  /var/log/odysseus && \
    chown -R odysseus /var/log/odysseus && \
    echo "UTC" > /etc/timezone

# Cleanup APT
RUN apt-get purge -yq wget unzip && apt-get clean -yq && rm -rf /var/lib/apt/lists/*

ADD Dockerfile /Dockerfile
# Expose web server port for wsdl
EXPOSE 9669

# Change user and setup environment
USER odysseus
ENV home /var/lib/odysseus
WORKDIR /var/lib/odysseus

CMD ["/usr/lib/odysseus/odysseus"]
