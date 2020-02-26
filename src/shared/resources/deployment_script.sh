#!/bin/bash

# Modify vars to fit test

#first host is master
declare -a hosts=()


  #first host is master
  declare -a localIPhosts=(
    "172.31.18.90"
    "172.31.24.226"
    "172.31.28.239"
    "172.31.17.239"
  )

entryIP="ec2-18-218-5-63.us-east-2.compute.amazonaws.com"
numberOfMachines="4"
datasetName="dataset1M.txt"
localFolder=/home/pietro/flowgraph/
localLogFolder=/home/pietro/Desktop/Logs/
remoteFolder=/home/ec2-user/flowgraph/
sshOptions='-o StrictHostKeyChecking=no' #You need ssh-add /home/pietro/amazonKey.pem
executableName="flowgraph-0.1-launcher.jar"


#region: Functions

function launchSimulationAndCollect {

  #kill all java processes on slaves
  for i in "${hosts[@]}"
  do
    command="ssh ${sshOptions} ec2-user@${i} killall -9 java &"
    #echo "${command}"
    ${command}
  done

  wait

  # Execute

  for i in "${hosts[@]}"
  do
    command="ssh ${sshOptions} ec2-user@${i} 'nohup java -Xms24g -Xmx24g -XX:+AggressiveHeap -XX:+UseParallelGC -Xlog:gc*:file=/home/ec2-user/flowgraph/log/gc${i}.txt -jar ${remoteFolder}${executableName}' &"
    echo "${command}"
    eval "${command}"
  done


  echo "Wait execution"

  # Wait execution completion
  sleep 10s
  finished=false
  while [[ $finished == *"false"* ]]; do
      finished=true
      for i in "${hosts[@]}"
      do
        command="ssh ${sshOptions} ec2-user@${i} 'pgrep java'"
        eval "${command}"  >/dev/null
        if [[ $? -ne 0 ]] ; then
            echo "Finished"
        else
            finished=false
        fi
      done
  done

  # Kill remaining background processes on client
  eval "kill \$(jobs -p)"

  echo "Executed"

  # Collect results
  mkdir ${localLogFolder}${datasetName}

  for i in "${hosts[@]}"
  do
    scp -r "${sshOptions}"  ec2-user@"${i}":"${remoteFolder}"log/* "${localLogFolder}""${datasetName}"/ &
  done

  wait

}

function oneDataset {

  # Set dataset on config.properties
  sed -i -e "s:datasetPath =.*$:datasetPath = ${remoteFolder}datasets/${datasetName}:" ${localFolder}config.properties

  # Set number of instances on config.properties
  sed -i -e "s:numberOfSlaves =.*$:numberOfSlaves = "${numberOfMachines}":" ${localFolder}config.properties

  # If M order, try only 10 updates
  if [[ $datasetName == *"M"* ]]; then
    sed -i -e "s:numRecordsAsInput =.*$:numRecordsAsInput = 20:" ${localFolder}config.properties
  else
    sed -i -e "s:numRecordsAsInput =.*$:numRecordsAsInput = 100:" ${localFolder}config.properties
  fi

  # Send local folder to coordinator and set 600 permissions for privateKey
  rsync -ruPav -e "ssh ${sshOptions}" ${localFolder} ec2-user@${entryIP}:${remoteFolder} --delete
  ssh ${sshOptions} ec2-user@${entryIP} 'sudo chmod 600 '${remoteFolder}'amazonPoli.pem'

  allCommands=""
  for i in "${hosts[@]}"
  do
    #rsync on entire flowgraph folder and delete old logs
    insideCommand="rsync -ruPav -e \"ssh ${sshOptions} -i ${remoteFolder}amazonPoli.pem\" ${remoteFolder} ec2-user@'${i}':'${remoteFolder}' --delete"
    completeCommand="ssh ${sshOptions} ec2-user@${entryIP} '${insideCommand}' & "
    allCommands="${allCommands}${completeCommand}"

  done

  allCommands="${allCommands} ssh ${sshOptions} ec2-user@${entryIP} 'wait'"
  echo "${allCommands}"
  result=$(eval "${allCommands}")
  echo "${result}"

  wait

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
  rsync -ruPav -e "ssh ${sshOptions}" ${localFolder}amazonPoli.pem ec2-user@${entryIP}:${remoteFolder}
  ssh ${sshOptions} ec2-user@${entryIP} 'sudo chmod 600 '${remoteFolder}'amazonPoli.pem'


  for ((i=0; i<$numberOfMachines; i++)) ; do
      currentHost="${localIPhosts["${i}"]}"
      insideCommand="ssh -i \"${remoteFolder}amazonPoli.pem\" ${sshOptions} ec2-user@${currentHost} 'dig +short myip.opendns.com @resolver1.opendns.com'"
      completeCommand="ssh ${sshOptions} ec2-user@${entryIP} '${insideCommand}'"
      result=$(eval "$completeCommand")
      hosts+=( "${result}" )
  done

  echo "${hosts[@]}"

  echo "GOT IPS"
  }

function setup {

  # install jdk
  installCommand='sudo amazon-linux-extras enable java-openjdk11 && sudo yum install java-11-openjdk -y && sudo yum install java-1.8.0-openjdk-devel.x86_64 -y && sudo yum install java-1.8.0-openjdk-headless-debug.x86_64 -y && sudo yum -y install nmap-ncat && sudo update-alternatives --set java /usr/lib/jvm/java-11-openjdk-11.0.5.10-0.amzn2.x86_64/bin/java'

  for i in "${hosts[@]}" ; do
     ssh ${sshOptions} ec2-user@"${i}" "${installCommand}" &
  done

  wait

  echo "END SETUP"
}



#endregion


getPublicIPs
#setup
#setup

if [ "$datasetName" = "NULL" ]; then
  echo "allDatasets"
        allDatasets
else
  echo "oneDataset"
        oneDataset
fi

aplay ${localFolder}finish.WAV
echo "finish"

