REGISTER '$additionalJarPath';
DEFINE HASH com.kohls.eval.utils.udfs.HashStringWithRecIdConsolidated;
DEFINE RANKVAL com.kohls.eval.utils.udfs.ranking.ranking.RankRowVal(',');
%DEFAULT algoToRun 'filter_P1P2P3_Off(full_view_bought_ranked_select)';


-- Read gold sku table and filter P1P2P3
DEFINE filter_P1P2P3_On(full_view_bought_ranked_select) RETURNS algo_result {

full_view_bought_grp = GROUP $full_view_bought_ranked_select BY (pId, view_bought_product);

full_view_bought_final_ranked = FOREACH full_view_bought_grp GENERATE
                                        FLATTEN(group) AS (pId, view_bought_product),
                                        MIN($full_view_bought_ranked_select.row_id) AS finalRank;

sku_data_all_raw = LOAD '$db_gold.gold_atg_sku' USING org.apache.hive.hcatalog.pig.HCatLoader();

sku_data_filtered = FILTER sku_data_all_raw BY
                           sku_id MATCHES '^[0-9]*$' AND
                           parent_product_id MATCHES '^[0-9]*$' AND
                           product_primary_type_value IS NOT NULL AND
                           product_primary_type_value != '' AND product_type_value IS NOT NULL AND
                           product_type_value != '' AND product_sub_type_value IS NOT NULL AND
                           product_sub_type_value != '';

sku_data_select = FOREACH sku_data_filtered GENERATE
                          parent_product_id AS pId,
                          product_primary_type_value AS p1,
                          product_type_value AS p2,
                          product_sub_type_value AS p3;

sku_data = DISTINCT sku_data_select;

--- Join sku

sku_and_vb_key = JOIN sku_data BY pId, full_view_bought_final_ranked BY pId;

sku_vb_key_and_sku_vb_val = JOIN sku_data BY pId, sku_and_vb_key BY full_view_bought_final_ranked::view_bought_product;

sku_vb_key_and_sku_vb_val_select = FOREACH sku_vb_key_and_sku_vb_val GENERATE
                                           sku_and_vb_key::sku_data::pId AS pId,
                                           sku_and_vb_key::sku_data::p1 AS key_p1,
                                           sku_and_vb_key::sku_data::p2 AS key_p2,
                                           sku_and_vb_key::sku_data::p3 AS key_p3,
                                           sku_and_vb_key::full_view_bought_final_ranked::view_bought_product AS view_bought_product,
                                           sku_and_vb_key::full_view_bought_final_ranked::finalRank AS finalRank,
                                           sku_data::p1 AS val_p1,
                                           sku_data::p2 AS val_p2,
                                           sku_data::p3 AS val_p3;

sku_vb_key_and_sku_vb_val_filtered = FILTER sku_vb_key_and_sku_vb_val_select BY key_p1 == val_p1 AND key_p2 == val_p2 AND key_p3 == val_p3;

$algo_result = FOREACH sku_vb_key_and_sku_vb_val_filtered GENERATE
                       pId AS pId,
                       view_bought_product AS view_bought_product,
                       finalRank AS finalRank;
};

DEFINE filter_P1P2P3_Off(full_view_bought_ranked_select) RETURNS algo_result {

full_view_bought_grp = GROUP $full_view_bought_ranked_select BY (pId, view_bought_product);

$algo_result = FOREACH full_view_bought_grp GENERATE
                                        FLATTEN(group) AS (pId, view_bought_product),
                                        MIN($full_view_bought_ranked_select.row_id) AS finalRank;

};

-- Step 1
-- Read view bought and rank all exploded content

view_bought_data_raw = LOAD '$inBoughtPath' USING PigStorage('\t') AS (pId:CHARARRAY, bought_products:CHARARRAY);

view_bought_ranked_bag_data = FOREACH view_bought_data_raw GENERATE
                                pId,
                                RANKVAL(bought_products, 1) AS ranked_bag;

view_bought_rank_fllaten = FOREACH view_bought_ranked_bag_data GENERATE
                                        pId,
                                        FLATTEN(ranked_bag) AS (finalBoughtRank, view_bought_product);

-- Step 2
-- Get view view data set

view_view_data_raw = LOAD '$inViewedPath' USING PigStorage('\t') AS (pId:CHARARRAY, viewed_products:CHARARRAY);


view_view_ranked_bag_data = FOREACH view_view_data_raw GENERATE
                                pId,
                                RANKVAL(viewed_products, 1) AS ranked_bag;

view_view_rank_fllaten = FOREACH view_view_ranked_bag_data GENERATE
                                        pId,
                                        FLATTEN(ranked_bag) AS (finalViewedRank, viewed_product);

---- Step 3
--- Join view view with view bought and filter out same product purchases

view_view_and_view_bought = JOIN view_view_rank_fllaten BY viewed_product, view_bought_rank_fllaten BY pId;

view_view_and_view_bought_select  = FOREACH view_view_and_view_bought GENERATE
                                            view_view_rank_fllaten::pId AS pId,
                                            view_view_rank_fllaten::viewed_product AS viewed_product,
                                            view_view_rank_fllaten::finalViewedRank AS viewRank,
                                            view_bought_rank_fllaten::view_bought_product AS view_bought_product,
                                            view_bought_rank_fllaten::finalBoughtRank AS finalBoughtRank;

view_view_and_view_bought_filter = FILTER view_view_and_view_bought_select BY view_bought_product != pId;

---- Step 4
--- Union to get whole view bought set

full_view_bought_set = UNION ONSCHEMA view_view_and_view_bought_filter, view_bought_rank_fllaten;

full_view_bought_select = FOREACH full_view_bought_set GENERATE
                             pId AS pId,
                             viewed_product AS viewed_product,
                             ((viewRank IS NULL)? -101 : viewRank) AS viewRank,
                             view_bought_product AS view_bought_product,
                             finalBoughtRank AS finalBoughtRank;

-- Step 5
-- Add rank per productid and get min rank per view bought product for deduplication

full_view_bought_ranked = RANK full_view_bought_select BY pId ASC, viewRank ASC, finalBoughtRank ASC;

full_view_bought_ranked_select   = FOREACH full_view_bought_ranked GENERATE
                                           FLATTEN($0) AS row_id,
                                           pId,
                                           viewed_product,
                                           viewRank,
                                           view_bought_product,
                                           finalBoughtRank;

-- Step 6
-- Filter on or off p1p2p3

sku_vb_key_and_val_select = $algoToRun;

-- Step 7
-- Sort by finalRank and pick TOP K

raw_advanced_view_bought_grp = GROUP sku_vb_key_and_val_select BY pId;

raw_advanced_view_bought_select = FOREACH raw_advanced_view_bought_grp {
                                          sort = ORDER sku_vb_key_and_val_select BY finalRank ASC;
                                          viewbought = LIMIT sort $limitReco;
                                          GENERATE FLATTEN(group) AS pId,
                                          BagToString(viewbought.view_bought_product, ',') AS pList;
                                         };

raw_avb_grp = GROUP raw_advanced_view_bought_select BY pId;

advanced_view_bought = FOREACH raw_avb_grp  GENERATE
                               group AS pId,
                               BagToString(raw_advanced_view_bought_select.pList, ',') AS pidlist;

STORE advanced_view_bought INTO '$outHdfsPath/csv/' USING PigStorage('\t', '-schema');

hashed_result = FOREACH advanced_view_bought GENERATE HASH(pId, '$recId') AS hashed_pId, pidlist;

STORE hashed_result INTO '$outHdfsPath/consolidated_ede/' USING PigStorage('\t');

