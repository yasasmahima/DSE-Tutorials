REGISTER '$additionalJarPath';

--macro to load file in given schema and path
DEFINE get_input_data(path) returns inputfile{

    $inputfile = LOAD '$path' USING org.apache.pig.piggybank.storage.IndexedStorage('$delimeter', '0');
};

--get input files
inputfile1 = get_input_data('$inputPath1');
inputfile2 = get_input_data('$inputPath2');


--join files
$joined = JOIN inputfile1 BY $0 , inputfile2 BY $0 USING 'merge-sparse';


--store to outputpath
STORE joined_file INTO '$outputPath';