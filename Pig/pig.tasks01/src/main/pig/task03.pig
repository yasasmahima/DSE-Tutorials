world_cup_files1 = LOAD '../resources/WorldCupMatches.csv' USING PigStorage(',')
    AS(Year:chararray,date_time:chararray,Stage:chararray,Stadium:chararray ,City:chararray,Home_Team_Name:chararray,
    Home_Team_Goals:int,Away_Team_Goals:int ,Away_Team_Name:chararray,Win_conditions:chararray,
    Attendance:int, Half_time_Home_Goals:int,Half_time_Away_Goals:int,Referee:chararray,
    Assistant_1:chararray, Assistant_2:chararray,RoundID:int,MatchID:int,
    Home_Team_Initials:chararray, Away_Team_Initials:chararray);


ranked = rank world_cup_files1;

world_cup_files2 = Filter ranked by (rank_world_cup_files1 > 1);
world_cup_files3 = FILTER world_cup_files2 BY Year != '';

world_cup_files4 = foreach world_cup_files3 generate
        Year,REPLACE(date_time,'July','Jul')as date_time,Stage,Stadium,City,Home_Team_Name,Home_Team_Goals,Away_Team_Goals,
        Away_Team_Name,Win_conditions,Attendance,Half_time_Home_Goals,Half_time_Away_Goals,Referee,Assistant_1,
        Assistant_2,RoundID,MatchID,Home_Team_Initials,Away_Team_Initials;

world_cup_files5 = foreach world_cup_files4 generate
            Year,REPLACE(date_time,'June','Jun')as date_time,Stage,Stadium,City,Home_Team_Name,Home_Team_Goals,Away_Team_Goals,
            Away_Team_Name,Win_conditions,Attendance,Half_time_Home_Goals,Half_time_Away_Goals,Referee,Assistant_1,
            Assistant_2,RoundID,MatchID,Home_Team_Initials,Away_Team_Initials;

world_cup_files = foreach world_cup_files5 generate
            Year,ToDate(TRIM(REPLACE(REPLACE(date_time,'[-]',''),'["]','')),'dd MMM yyyy  HH:mm') as date_time:DateTime,
            Stage,Stadium,City,Home_Team_Name,Home_Team_Goals,Away_Team_Goals,
            Away_Team_Name,Win_conditions,Attendance,Half_time_Home_Goals,Half_time_Away_Goals,Referee,Assistant_1,
            Assistant_2,RoundID,MatchID,Home_Team_Initials,Away_Team_Initials;



--find most popular country
temp_1= GROUP world_cup_files BY Home_Team_Name;
temp_2 = GROUP world_cup_files BY Away_Team_Name;
temp_1_count = FOREACH temp_1 GENERATE FLATTEN(group) as (Home_Team_Name), COUNT($1) as home_match_count;
temp_2_count = FOREACH temp_2 GENERATE FLATTEN(group) as (Away_Team_Name), COUNT($1) as away_match_count;

matches_teams= JOIN temp_1_count BY Home_Team_Name, temp_2_count by Away_Team_Name;

max_count_matches = foreach matches_teams generate Home_Team_Name,(home_match_count + away_match_count) as total_count;
max_count_matches = ORDER max_count_matches BY total_count DESC;
max_count_matches = LIMIT max_count_matches 1;
dump max_count_matches;



--find matches in the current month
matches_current_month =  FILTER world_cup_files BY (GetMonth(date_time)==07);
dump matches_current_month;



--remove duplicates
unique_wc_matches = DISTINCT world_cup_files;
dump unique_wc_matches;



--total goals for a country in each year
temp1 = group world_cup_files by(Year,Home_Team_Name);
temp2 = foreach temp1 generate flatten(group) AS (Year,Home_Team_Name), SUM(world_cup_files.Home_Team_Goals) as home_goals;
temp3 = group world_cup_files by(Year,Away_Team_Name);
temp4 = foreach temp3 generate flatten(group) AS (Year,Away_Team_Name), SUM(world_cup_files.Away_Team_Goals) as away_goals;

temp5 = join temp2 by (Year,Home_Team_Name) full outer, temp4 by (Year,Away_Team_Name);
goals_each_year_country = foreach temp5 generate temp2::Year as Year,
                       temp2::Home_Team_Name as Home_Team_Name,
                       (temp2::home_goals+ temp4::away_goals) as total_goals;
dump goals_each_year_country;



--find winning team of each match
winner_of_each_match = foreach world_cup_files generate MatchID,Home_Team_Name,Away_Team_Name,Home_Team_Goals,
                Away_Team_Goals,(Home_Team_Goals > Away_Team_Goals ? Home_Team_Name :
                (Home_Team_Goals == Away_Team_Goals ? 'Draw' : Away_Team_Name)) as Winner;
dump winner_of_each_match;
