package com.seh.utils;

def call(Map jiraParams) {
   def projectkey= jiraParams.appName[0]+""+jiraParams.appName[1]+""+jiraParams.appName[2]
   withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId:'DevOpsSVCAccount',
  usernameVariable: 'USER', passwordVariable: 'PASSWORD']
]) {
     sh """
     curl -u ${USER}:${PASSWORD} -X GET 'http://localhost:8082/rest/scriptrunner/latest/custom/newClonedProject?project_key=${projectkey}&project_name=${jiraParams.appName}'
 #       curl -u ${USER}:${PASSWORD} -X POST \
 # http://10.13.11.13:8080/rest/api/2/project \
 # -H 'Content-Type: application/json' \
 # -H 'cache-control: no-cache' \
 # -d '{
 #"key": "${projectkey}",
 #"name":  "${jiraParams.appName}",
 #"projectTypeKey": "software",
 #"description": "This Jira Project Shell is auto created through Jenkins for ${jiraParams.appName}",
 #"workflowSchemeId": 12202,
 #"lead": "213082",
 #"assigneeType": "PROJECT_LEAD",
 #"avatarId": 10200
 #}'
 #
 #
 #curl -u ${USER}:${PASSWORD} -X POST   http://10.13.11.13:8080/rest/api/3/issuetypescheme/14507/associations  -H 'Content-Type: #application/json'   -H 'cache-control: no-cache'   -d '{
 #"idsOrKeys": ["${projectkey}"]
 #}' 

#curl -u ${USER}:${PASSWORD} -X POST http://10.13.11.13:8080/rest/scriptrunner/latest/custom/newClonedProject?project_key=AFIN&#project_name=AFINITY
#
#curl -u ${USER}:${PASSWORD}  --request PUT --url 'http://10.13.11.13:8080/rest/api/3/issuetypescreenscheme/project' --header 'Accept: #application/json' --header 'Content-Type: application/json' --data '{
#  "issueTypeScreenSchemeId": "12410",
#  "projectId": "12204"
#}'
#
#curl -u ${USER}:${PASSWORD} --request PUT --url 'http://10.13.11.13:8080/rest/api/3/screenscheme/12104' --header 'Accept: application/#json' --header 'Content-Type: application/json' --data '{
#  "screens": {
#    "default": "12402",
#    "create": "12410"
#  },
#  "name": "FWD: Standard Release Issue Type Screen Scheme"
#}'
     """
     }
}