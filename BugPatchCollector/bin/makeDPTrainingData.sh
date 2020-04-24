#!/bin/sh
# #incubator-hivemall
# ./BugPatchCollector -i https://github.com/apache/incubator-hivemall -o /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP -t -c /data/BIC/BIC_incubator-hivemall.csv -s 2017-01-01 00:00:00 -e 2019-06-30 59:59:59 -d 2 -p /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/Commit/incubator-hivemall

# # bval
# ./BugPatchCollector -i https://github.com/apache/bval -o /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP -t -c /data/BIC/BIC_bval.csv -s 2017-01-01 00:00:00 -e 2019-06-30 59:59:59 -d 2 -p /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/Commit/bval

# # lottie-android
# ./BugPatchCollector -i https://github.com/airbnb/lottie-android -o /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP -t -c /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/BIC_lottie-android.csv -s 2017-01-01 00:00:00 -e 2019-06-30 59:59:59 -d 2 -p /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/Commit/lottie-android
for project_name in "camel" "groovy" "jena" "bval" "incubator-hivemall" "ace" "incubator-dolphinscheduler" "ant-ivy" "apex-core" "bigtop" "cayenne" "cordova-android" "creadur-rat" "crunch" "cxf-fediz" "deltaspike" "gora" "guacamole-client";
  do
  ./BugPatchCollector -i https://github.com/apache/${project_name} -o /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/DP -t -c /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/BIC/BIC_${project_name}.csv -s 2017-01-01 00:00:00 -e 2019-06-30 59:59:59 -d 2 -p /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData/Commit/${project_name} 
done
# if occure error then make directory witch is naming each project name
