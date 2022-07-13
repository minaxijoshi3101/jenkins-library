package com.seh.utils;

def call(String repo, String appName,String repoAdmin) {
     sh"aws codecommit create-repository --repository-name ${repo} --tags Name=${appName}  --repository-description 'This '${repo}'- repo is auto created by jenkins'"
     sh """
        git clone https://git-codecommit.ap-southeast-1.amazonaws.com/v1/repos/${repo}
     """
     def fileWrite = libraryResource "readme.md"
     writeFile file: "${repo}/readme.md", text: fileWrite
     def buildSpecFileWrite = libraryResource "build-spec.json"
     writeFile file: "${repo}/build-spec.json", text: buildSpecFileWrite
     def fileWriteDeployScript = libraryResource "deploy.sh"
     writeFile file: "${repo}/deploy.sh", text: fileWriteDeployScript
     def fileWriteBuildScript = libraryResource "build.sh"
     writeFile file: "${repo}/build.sh", text: fileWriteBuildScript
     sh """    
        cd ${repo}
        git checkout -b main
        git add .
        git commit -m "Baseline commit for ${repo} - repo"
        git push --set-upstream origin main
        git checkout -b develop
        git push --set-upstream origin develop
     """
}
