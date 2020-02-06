#!/bin/bash

# Modify vars to fit test

#first host is master
declare -a hosts=()


  #first host is master
  declare -a localIPhosts=(
    "172.31.6.122"
    "172.31.6.197"
    "172.31.8.217"
    "172.31.0.197"
  )

entryIP="ec2-18-188-214-187.us-east-2.compute.amazonaws.com"
numberOfMachines="4"
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

  function getPublicIPs {

  # Send public key
  rsync -ruPav -e "ssh ${sshOptions}" ${localFolder}amazonKey.pem ec2-user@${entryIP}:${remoteFolder}


  for ((i=1; i<=$numberOfMachines; i++)) ; do
      currentHost="${localIPhosts[${i}]}"
      insideCommand="ssh -i ${remoteFolder}amazonKey.pem ${sshOptions} ec2-user@${currentHost} 'dig +short myip.opendns.com @resolver1.opendns.com'"
      result=$("ssh ${sshOptions} ec2-user@${entryIP} '"${insideCommand}"'")
      hosts+=( "${result}" )
  done

  echo "${hosts[@]}"

  }


#endregion

getPublicIPs

if [ "$datasetName" = "NULL" ]; then
  echo "allDatasets"
        allDatasets
else
  echo "oneDataset"
        oneDataset
fi

echo "finish"

