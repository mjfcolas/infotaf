#!/bin/sh
PATH_TO_APP=../..
PATH_TO_PACKAGE=.
VERSION=$1

mkdir $PATH_TO_PACKAGE/tmp
mvn -f $PATH_TO_APP/back/restapi/pom.xml package -P dev
cp $PATH_TO_APP/back/restapi/target/restapi-$VERSION.jar $PATH_TO_PACKAGE/tmp/restapi-$VERSION.jar
cp -R $PATH_TO_APP/front $PATH_TO_PACKAGE/tmp/front
cp $PATH_TO_APP/scripts/infotaf-initd $PATH_TO_PACKAGE/tmp/infotaf
cp $PATH_TO_APP/back/restapi/schema_bdd $PATH_TO_PACKAGE/tmp/schema_bdd
cp $PATH_TO_APP/scripts/install.sh $PATH_TO_PACKAGE/tmp/install.sh

mv $PATH_TO_PACKAGE/tmp $PATH_TO_PACKAGE/infotaf-$VERSION
zip -r $PATH_TO_PACKAGE/infotaf-$VERSION.zip $PATH_TO_PACKAGE/infotaf-$VERSION
rm -rf $PATH_TO_PACKAGE/infotaf-$VERSION
