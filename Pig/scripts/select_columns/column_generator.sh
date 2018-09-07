#!/usr/bin/env bash

#parameter passing
for item in $@; do
case $item in
(*=*) eval $item;
esac
done

hadoop fs -rm -r -f $outputPath

pig -stop_on_failure -param inputPath=$inputPath -param schema=$schema -param columns=$columns -param delimeter=$delimeter -param outputPath=$outputPath $pigScriptPath;
