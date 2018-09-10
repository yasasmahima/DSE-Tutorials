#!/bin/sh
export HCAT_HOME=/usr/lib/hive-hcatalog
export PIG_OPTS="-Dmapred.job.queue.name=long_running"

echo "HCAT_HOME: " $HCAT_HOME
echo "PIG_OPTS: " $PIG_OPTS

algo_prefix="filter_P1P2P3_"
algo_postfix="(full_view_bought_ranked_select)"

echo "Parsing args.."
for item in $@; do
case $item in
(*=*) eval $item;
esac
done

algoToRun="'${algo_prefix}${filterP1P2P3}${algo_postfix}'"

echo "pig script path       : " $pigScriptPath
echo "additional jar path   : " $additionalJarPath
echo "out hdfs path         : " $outHdfsPath
echo "view input path       : " $inViewedPath
echo "bought input path     : " $inBoughtPath
echo "db_gold               : " $db_gold
echo "recId                 : " $recId
echo "limitReco             : " $limitReco
echo "filterP1P2P3          : " $filterP1P2P3
echo "algoToRun             : " $algoToRun

hadoop fs -rm -r $outHdfsPath

pig -useHCatalog -stop_on_failure -param additionalJarPath=$additionalJarPath -param db_gold=$db_gold -param inViewedPath=$inViewedPath -param inBoughtPath=$inBoughtPath -param outHdfsPath=$outHdfsPath -param recId=$recId -param limitReco=$limitReco -param algoToRun=$algoToRun $pigScriptPath;

echo "The advanced view bought recommendation process is complete now."
