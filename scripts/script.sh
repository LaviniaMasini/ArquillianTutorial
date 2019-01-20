#!/bin/bash

download_jboss () {
    root_dir=`pwd`
    cd $HOME
    rm -rf "$JBOSS_HOME"
    wget "$WF_LINK"
    tar xfz "wildfly-$WF_VERSION.tar.gz"
    cd "$root_dir"
}

if [ ! -d "$JBOSS_HOME" ]; then
    download_jboss
else
    if [ `ls "$JBOSS_HOME" | wc -l` -eq 0 ]; then
        download_jboss
    fi
fi
