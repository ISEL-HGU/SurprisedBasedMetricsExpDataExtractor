#!/bin/sh
# # incubator-hivemall
# ./BugPatchCollector -i https://github.com/apache/incubator-hivemall -o /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/LM -t -c /data/BIC/BIC_incubator-hivemall.csv -s 0000-00-00 00:00:00 -e 2016-12-31 59:59:59 -d 1 -p /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData
# # bval
# ./BugPatchCollector -i https://github.com/apache/bval -o /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/LM -t -c /data/BIC/BIC_bval.csv -s 0000-00-00 00:00:00 -e 2016-12-31 59:59:59 -d 1 -p /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData
# # lottie-android
# ./BugPatchCollector -i https://github.com/airbnb/lottie-android -o /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/LM -t -c /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/BIC_lottie-android.csv -s 0000-00-00 00:00:00 -e 2016-12-31 59:59:59 -d 1 -p /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData
for project_name in "ant-ivy" "bval" "camel" "cayenne" "deltaspike" "gora" "guacamole-client" "incubator-hivemall";
#for project_name in "jena";
  do
  ./BugPatchCollector -i https://github.com/apache/${project_name} -o /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/Output/LM -t -c /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/BIC/BIC_${project_name}.csv -s 0000-00-00 00:00:00 -e 2016-12-31 59:59:59 -d 1 -p /home/eunjiwon/Git/Collect-Data-with-BugPatchCollector/TrainData
done




