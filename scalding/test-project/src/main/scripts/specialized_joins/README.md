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
join_method - specialized join method in pig
delimeter   - common seperator for both files
outputPath  - path for Output file
pigScriptPath - Path for the pig script
````

##sample run - replicated join

*     bash ./special_join.sh \
            inputPath1=../../resources/specialized_joins_pig/apache_nobots_tsv.txt \
            inputPath2=../../resources/specialized_joins_pig/nobots_ip_country_tsv.txt \
            schema1=ip:chararray,timestamp:long,page:chararray,http_status:int,payload_size:int,useragent:chararray \
            schema2=ip:chararray,country:chararray \
            joinField1=ip \
            joinField2=ip \
            delimeter="'\\\\t'" \
            outputPath=./replicated_result.tsv
            join_method=replicated 
            pigScriptPath=./special_join.pig
            
            
##sample run - skewed join

*     bash ./special_join.sh \
            inputPath1=../../resources/specialized_joins_pig/skewed_apache_nobots_tsv.txt \
            inputPath2=../../resources/specialized_joins_pig/nobots_ip_country_tsv.txt \
            schema1=ip:chararray,timestamp:long,page:chararray,http_status:int,payload_size:int,useragent:chararray \
            schema2=ip:chararray,country:chararray \
            joinField1=ip \
            joinField2=ip \
            delimeter="'\\\\t'" \
            outputPath=./skewed_result.tsv
            join_method=skewed 
            pigScriptPath=./special_join.pig
            
            
##sample run - merge join

*     bash ./special_join.sh \
            inputPath1=../../resources/specialized_joins_pig/sorted_apache_nobots_tsv.txt \
            inputPath2=../../resources/specialized_joins_pig/sorted_nobots_ip_country_tsv.txt \
            schema1=ip:chararray,timestamp:long,page:chararray,http_status:int,payload_size:int,useragent:chararray \
            schema2=ip:chararray,country:chararray \
            joinField1=ip \
            joinField2=ip \
            delimeter="'\\\\t'" \
            outputPath=./merge_result.tsv
            join_method=merge 
            pigScriptPath=./special_join.pig