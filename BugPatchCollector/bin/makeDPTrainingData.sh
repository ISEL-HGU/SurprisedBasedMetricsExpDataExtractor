for project_name in "ant-ivy" "bval" "camel" "cayenne" "deltaspike" "gora" "guacamole-client" "incubator-hivemall";
  do
  ./BugPatchCollector -i https://github.com/apache/${project_name} -o ../../Output/DP -t -c ../../BIC/BIC_${project_name}.csv -s 2017-01-01 00:00:00 -e 2019-09-30 59:59:59 -d 2 -p ../../TrainData/Commit/${project_name}
done
