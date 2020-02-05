#!/bin/bash

# Modify vars to fit test

#first host is master
declare -a hosts=(
  "ec2-18-191-233-219.us-east-2.compute.amazonaws.com"
  "ec2-3-15-240-215.us-east-2.compute.amazonaws.com"
  "ec2-3-137-41-31.us-east-2.compute.amazonaws.com"
  "ec2-3-15-149-185.us-east-2.compute.amazonaws.com"
)

datasetName="NULL"
localFolder=/home/pietro/Desktop/flowgraph/
remoteFolder=/home/ec2-user/flowgraph/
sshOptions='-o StrictHostKeyChecking=no' #You need ssh-add /home/pietro/amazonKey.pem
executableName="flowgraph-0.1-launcher.jar"


#region: Functions

function launchSimulationAndCollect {

  for i in "${hosts[@]}"
  do
    command="ssh ${sshOptions} ec2-user@${i} java -jar ${remoteFolder}${executableName} &"
    echo "${command}"
    ${command}
  done
  echo "Wait execution"
  wait
  echo "Executed"


for i in "${hosts[@]}"
  do
    mkdir ${localFolder}log/${datasetName}
    scp -r "$sshOptions"  "${i}:${remoteFolder}log/*" "${localFolder}log/${datasetName}"
  done


}

function oneDataset {

  sed -i -e "s:datasetPath =.*$:datasetPath = ${remoteFolder}datasets/${datasetName}:" ${localFolder}config.properties

  for i in "${hosts[@]}"
  do
    #rsync on entire flowgraph folder
    rsync -ruPav -e "ssh ${sshOptions}" ${localFolder} ec2-user@${i}:${remoteFolder}
    #scp "$sshOptions" "${localFolder}config.properties" "ec2-user@${i}:${remoteFolder}"
    ssh  "$sshOptions" ec2-user@"${i}" 'rm -r ${remoteFolder}log/*'

  done

  launchSimulationAndCollect

}

function allDatasets {

  for i in "${localFolder}datasets"/*
  do
    datasetName=$i
    datasetName=${datasetName##*/}
    echo ${datasetName}
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

