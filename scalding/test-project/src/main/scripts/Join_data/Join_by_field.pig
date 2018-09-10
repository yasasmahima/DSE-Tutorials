--macro to load file in given schema and path
DEFINE get_input_data(path,schema) returns inputfile{

    $inputfile = LOAD '$path' USING PigStorage('$delimeter') AS ($schema) ;
};

--macro to join files in given field
DEFINE join_files(relation1,relation2,joinField1,joinField2) returns joined{

    $joined = JOIN $relation1 BY (chararray)$joinField1 , $relation2 BY (chararray)$joinField2 ;
};

--get input files
inputfile1 = get_input_data('$inputPath1','$schema1');
inputfile2 = get_input_data('$inputPath2','$schema2');


--join files
joined_file = join_files(inputfile1, inputfile2,'$joinField1','$joinField2');


--store to outputpath
STORE joined_file INTO '$outputPath' USING PigStorage('$delimeter');