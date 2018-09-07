#JOIN DATA ON GIVEN COLUMN

bash script - Join_by_field.sh\
Pig Script - Join_by_field.pig


##params
````
inputPath1  - Path to file 1
inputPath2  - Path to file 2 
schema1     - schema for file 1                  
schema2     - schema for file 2
joinField1  - column to be joined in file 1
joinField2  - column to be joined in file 2
delimeter   - common seperator for both files
outputPath  - path for Output file
pigScriptPath - Path for the pig script
````

##sample run

*     bash ./Join_data/Join_by_field.sh \
            inputPath1=./file1.tsv \
            inputPath2=./file2.tsv \
            schema1=id:int,values:CHARARRAY \
            schema2=id:chararray,values:CHARARRAY,title:chararray \
            joinField1=id \
            joinField2=id \
            delimeter="'\\\\t'" \
            outputPath=./result.csv 
            pigScriptPath=./Join_data/Join_by_field.pig
