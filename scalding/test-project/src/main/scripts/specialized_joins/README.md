#JOIN DATA ON GIVEN COLUMN

bash script - special_join.sh\
Pig Script - special_join.pig


##params
````
inputPath1  - Path to file 1
inputPath2  - Path to file 2 
schema1     - schema for file 1                  
schema2     - schema for file 2
joinField1  - column to be joined in file 1
joinField2  - column to be joined in file 2
join_method - specialized join method in pig (skewed/merge/replicated)
delimeter   - common seperator for both files
outputPath  - path for Output file
outerJoinMethod - sql join technique(''/full/right/left) "'"'""'"'" - for inner
pigScriptPath - Path for the pig script
````

##sample run - replicated join

*     bash ./special_join.sh 
            inputPath1=../../resources/specialized_joins_pig/apache_nobots_tsv.txt 
            inputPath2=../../resources/specialized_joins_pig/nobots_ip_country_tsv.txt 
            schema1=ip:chararray,timestamp:long,page:chararray,http_status:int,payload_size:int,useragent:chararray 
            schema2=ip:chararray,country:chararray 
            joinField1=ip 
            joinField2=ip 
            delimeter="'\\\\t'" 
            outputPath=./replicated_result.tsv
            join_method=replicated
            outerJoinMethod="'"'""'"'" | outerJoinMethod="left"
            pigScriptPath=./special_join.pig
            
 ~~~           
Note - replicated join works only for inner and left joins
       inputPath1 should be the path to the large dataset
       inputPath2 should be a dataset which is small enought to fit into main memory          
 ~~~   
    
    
    
    
##sample run - skewed join

*     bash ./special_join.sh 
            inputPath1=../../resources/specialized_joins_pig/skewed_apache_nobots_tsv.txt 
            inputPath2=../../resources/specialized_joins_pig/nobots_ip_country_tsv.txt 
            schema1=ip:chararray,timestamp:long,page:chararray,http_status:int,payload_size:int,useragent:chararray 
            schema2=ip:chararray,country:chararray 
            joinField1=ip 
            joinField2=ip 
            delimeter="'\\\\t'" 
            outputPath=./skewed_result.tsv
            join_method=skewed 
            outerJoinMethod="'"'""'"'" | outerJoinMethod="left" | outerJoinMethod="right" | outerJoinMethod="full"
            pigScriptPath=./special_join.pig
                      
 ~~~           
Note - skewed join works for all full,right,left and inner joins
       inputPath1 should be the path to the skewed dataset
 ~~~
 
 
            
##sample run - merge join

*     bash ./special_join.sh 
            inputPath1=../../resources/specialized_joins_pig/sorted_apache_nobots_tsv.txt 
            inputPath2=../../resources/specialized_joins_pig/sorted_nobots_ip_country_tsv.txt 
            schema1=ip:chararray,timestamp:long,page:chararray,http_status:int,payload_size:int,useragent:chararray 
            schema2=ip:chararray,country:chararray 
            joinField1=ip 
            joinField2=ip 
            delimeter="'\\\\t'" 
            outputPath=./merge_result.tsv
            join_method=merge 
            outerJoinMethod="'"'""'"'"
            pigScriptPath=./special_join.pig
                  
 ~~~           
Note - merge join works only for inner joins
       Relations should be pre sorted in the ascending order by the joinField given
 ~~~
 
 
 
#######################################################################################
 
Merge Sparse Join 
 
bash script - merge_sparse.sh\
Pig Script - merge_sparse.pig

*     bash ./special_join.sh 
            inputPath1=../../resources/specialized_joins_pig/sorted_apache_nobots_tsv.txt 
            inputPath2=../../resources/specialized_joins_pig/sorted_nobots_ip_country_tsv.txt 
            delimeter="'\\\\t'" 
            outputPath=./merge_result.tsv
            additionalJarPath='../../hadoop_jar.jar'
            pigScriptPath=./merge_sparse.pig

~~~
Note - Preconditions same as merge join
       Will not work with PigStorage, only loader pig has provided is org.apache.pig.piggybank.storage.IndexedStorage
~~~
 