world_cup_files = LOAD '../resources/WorldCups.csv' USING PigStorage(',')
    AS(Year:chararray,Country:chararray,Winner:chararray,Runners_Up:chararray ,Third:chararray,Fourth:chararray,
    GoalsScored:int, QualifiedTeams:int,MatchesPlayed:int,Attendance:chararray);

world_cup_files = foreach world_cup_files generate
        Year,Country,Winner,Runners_Up,Third,Fourth,GoalsScored,QualifiedTeams,MatchesPlayed,
        REPLACE(Attendance,'[.]', '') as Attendance;

world_cup_files = FOREACH world_cup_files GENERATE
            Year,Country,Winner,Runners_Up,Third,Fourth,GoalsScored,QualifiedTeams,MatchesPlayed,
            (int)Attendance as Attendance;


hostWinnerRunnerUp = FILTER world_cup_files BY Country == Winner OR Country == Runners_Up;
dump hostWinnerRunnerUp;


highestGoalScoredCountries = ORDER world_cup_files BY GoalsScored DESC;
highestGoalScoredCountry = LIMIT highestGoalScoredCountries 1;
dump highestGoalScoredCountry;


top10GoalScoredCountries = LIMIT highestGoalScoredCountries 10;
dump top10GoalScoredCountries;



grouped = group world_cup_files all;
avgAttendance = foreach grouped generate AVG(world_cup_files.Attendance);
dump avgAttendance;


groupedByCounrty = GROUP world_cup_files BY Winner;
temp = FOREACH groupedByCounrty GENERATE FLATTEN(group) as (Winner), COUNT($1);
groupedByCountry = LIMIT temp 1;
dump groupedByCountry