#!/bin/sh

for project_name in "camel" "groovy" "jena" "bval" "incubator-hivemall" "ace" "incubator-dolphinscheduler" "ant-ivy" "apex-core" "bigtop" "cayenne" "cordova-android" "creadur-rat" "crunch" "cxf-fediz" "deltaspike" "gora" "guacamole-client";
  do
        if [ ! -d ${project_name} ]; then
                mkdir ${project_name}
        fi
done
