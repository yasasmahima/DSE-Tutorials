#!/usr/bin/env bash

#parameter passing
for item in $@; do
case $item in
(*=*) eval $item;
esac
done

#hadoop fs -rm -r -f $outputPath
rm -r -f $outputPath

pig -x local -stop_on_failure -param additionalJarPath=$additionalJarPath -param inputPath1=$inputPath1 -param inputPath2=$inputPath2 -param delimeter=$delimeter -param outputPath=$outputPath $pigScriptPath;
