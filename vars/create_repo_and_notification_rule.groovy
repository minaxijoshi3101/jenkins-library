import com.seh.utils.autoCreateJiraShell
@Library("jenkins-shared-library")_
import com.seh.utils.*
    try {
        timeout(time: 60, unit: 'MINUTES') {
            pipeline {
                Map jiraParams= [:]
                def JIRA_KEY = params.JIRA_ISSUE_KEY
                node("master") {
                    def issue = jiraGetIssue idOrKey: JIRA_KEY, site: 'JIRA_SG'
                    List<String> repoNames = Arrays.asList(issue.data.fields.customfield_13705.toString().split("\\s*,\\s*"));
                    String projectName = issue.data.fields.customfield_13706.toString().replaceAll(' ','_')
                    String appName = issue.data.fields.customfield_13707.toString().replaceAll(' ','_')
                    String repoAdmin =  issue.data.fields.customfield_13710.toString().replaceAll(' ','_')
                    String exclusions = issue.data.fields.customfield_14302.toString()
                    jiraParams.put("appName", appName)
                    jiraParams.put("repoAdmin", repoAdmin)

                    stage("Create Repo & Jenkins Job") {
                        for(repo in repoNames) {
                            if(!exclusions.contains("Exclude CodeCommit Repo"))
                            {
                                print repo
                                new autoCreateRepo().call(repo,appName,repoAdmin)
                                //new applyNotificationRule().call(repo)
                            }
                            if(!exclusions.contains("Exclude Jenkins Jobs"))
                            {
                               //new autoCreateJob().call(repo,appName)
                            }
                        }
                        if(!exclusions.contains("Exclude Jira Project Creation"))
                        {
                            //new autoCreateJiraShell().call(jiraParams)
                        }
                    }
                    stage("Update Jira") {
                        echo "Update Jira"
                        def transitionInput = [transition: [id: '21']]
                        def response = jiraTransitionIssue idOrKey: params.JIRA_ISSUE_KEY, input: transitionInput, site: 'JIRA_SG'
                        print response
                    }  
                }
            }
        }
    }catch (err) {
        echo "Caught: ${err}"
        def transitionInput = [transition: [id: '31']]
        jiraTransitionIssue idOrKey: params.JIRA_ISSUE_KEY, input: transitionInput, site: 'JIRA_SG'
        messaging = 'DO ticket Failed'
        jiraAddComment comment: messaging, idOrKey: params.JIRA_ISSUE_KEY, site: 'JIRA_SG'
        throw err
    }finally {
         node("master") {
           sh "rm -rf ${WORKSPACE}/*"
        }
    }
