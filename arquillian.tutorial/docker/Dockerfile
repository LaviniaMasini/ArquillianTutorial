# Base image: Wildfly latest version with 8080 port exposed
FROM jboss/wildfly:latest
ARG warFile
COPY /maven/$warFile /tmp/$warFile
WORKDIR /tmp

EXPOSE 9990
EXPOSE 8787

ADD /maven/$warFile /opt/jboss/wildfly/standalone/deployments/
RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

# This will boot WildFly in the standalone mode and bind to all interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
