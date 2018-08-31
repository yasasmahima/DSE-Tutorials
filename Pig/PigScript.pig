rel = LOAD 'file:///tmp/sample.csv' USING PigStorage(',') AS (name:CHARARRAY,age:int);
dump rel;

rel1 = FOREACH rel GENERATE name, age + 1 AS new_age;


fs -rm -r hdfs://bigd-demo-08:8020/tmp/output.csv 
--remove existing directory (output.csv) is a directory


STORE rel1 INTO 'hdfs://bigd-demo-08:8020/tmp/output.csv' USING PigStorage(',');
-- store output.csv as directory , result will be in a part file
