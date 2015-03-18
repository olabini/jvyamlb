#!/bin/sh

VERSION=0.2.7

mvn deploy:deploy-file -Dfile=lib/jvyamlb-$VERSION.jar -Durl=dav:https://dav.codehaus.org/repository/jruby -DgroupId=org.jruby.extras -DartifactId=jvyamlb -DrepositoryId=codehaus-jruby-repository -Dversion=$VERSION
