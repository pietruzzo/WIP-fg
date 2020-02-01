#!/bin/bash

# Modify vars to fit test

#first host is master
declare -a hosts=(
  "192.168.1.1"
  "element2"
)

datasetName=dataset1M.txt
localFolder=/home/pietro/Desktop/flowgraph/
remoteFolder=/home/ec2-user/flowgraph/
sshOptions=" -oStrictHostKeyChecking=no -i \"/home/pietro/amazonKey.pem\" "
executableName="flowgraph-0.1-launcher.jar"


#region: Functions

function launchSimulationAndCollect {

  command=""
  for i in "${hosts[@]}"
  do
    command="${command}ssh ${sshOptions} ${i} 'java -jar ${remoteFolder}${executableName}' && "
  done

  command="{command} echo \"launched all\""

  echo "$command"

  command
  wait

for i in "${hosts[@]}"
  do
    mkdir ${localFolder}log/${datasetName}
    scp -r "$sshOptions"  "${i}:${remoteFolder}log/*" "${localFolder}log/${datasetName}"
  done


}

function oneDataset {

  sed -i -e "/datasetPath =/ s/= .*/= ${remoteFolder}datasets/${datasetName}/" ${localFolder}config.properties

  for i in "${hosts[@]}"
  do
    scp "$sshOptions" "${localFolder}config.properties" "${i}:${remoteFolder}"
    ssh  "$sshOptions" ${i} 'rm -r ${remoteFolder}log/*'

  done

  launchSimulationAndCollect

}

function allDatasets {

  for i in "${localFolder}/datasets"/*
  do
    datasetName=$i
    oneDataset
  done
}


#endregion


if [ "$datasetName" = "NULL" ]; then
  echo "allDatasets"
        allDatasets
else
  echo "oneDataset"
        oneDataset
fi

echo "finish"

