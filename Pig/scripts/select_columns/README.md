#GENERATE SELECTED COLUMNS

bash script - column_generator.sh\
Pig Script - column_generator.pig


##params
````
inputPath   - Path to Input file
schema      - schema of Input file 
columns     - selected columns to generate (comma seperated)                  
schema2     - schema for file 2
delimeter   - seperator for Input file
outputPath  - path for Output file
pigScriptPath - Path for the pig script
````


            
##sample run

*       bash ./select_columns/column_generator.sh \
            inputPath=./movies2.csv \
            schema='movieId:int,title:CHARARRAY,genres:CHARARRAY' \
            delimeter="," \
            columns='movieId,title' \
            outputPath=./result.csv \
            pigScriptPath=./select_columns/column_generator.pig
