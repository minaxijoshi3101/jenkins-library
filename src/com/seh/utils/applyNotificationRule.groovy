package com.fwd.utils;

def call(String repo) {
    def fileWrite = libraryResource "notification_rule.json"
    writeFile file: "${WORKSPACE}/notification_rule.json", text: fileWrite
    sh """
        cd ${WORKSPACE}
        sed -i "s;%REPO_NAME%;${repo};g" notification_rule.json
        aws codestar-notifications create-notification-rule --cli-input-json  file://notification_rule.json
    """
}