FROM ubuntu
MAINTAINER Christian Kuka
LABEL Description="Docker Image for running the Odysseus data stream management system" Version="0.1"  URL="http://odysseus.informatik.uni-oldenburg.de"

# Install required packages
RUN apt-get -y update
RUN apt-get -y install apt-utils
RUN apt-get -y install wget unzip
RUN apt-get -y install screen nano
RUN apt-get -y install openjdk-8-jre 

# Download nightly build
RUN mkdir -p /usr/lib/odysseus
RUN wget -c http://odysseus.offis.uni-oldenburg.de/download/products/server/lastBuild/odysseus.server-linux.gtk.x86_64.zip -O /tmp/odysseus.zip && unzip /tmp/odysseus.zip -d /usr/lib/odysseus
RUN rm -f /tmp/odysseus.zip

# Create group and user
RUN groupadd odysseus
RUN useradd -r -g odysseus -s /bin/bash -d /var/lib/odysseus -m odysseus

# Change timezone for UTC
RUN echo "UTC" > /etc/timezone

# Expose web server port for wsdl
EXPOSE 9669

# Change user and setup environment
USER odysseus
ENV home /var/lib/odysseus
WORKDIR /var/lib/odysseus

CMD ["/usr/lib/odysseus/odysseus"]
