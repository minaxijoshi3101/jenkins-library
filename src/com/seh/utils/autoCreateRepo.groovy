package com.seh.utils;

def call(String repo, String appName,String repoAdmin) {
   //curl and github api to create repo in github
   sh """
   curl -u 'minaxijoshi3101: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' https://api.github.com/user/repos -d '{"name":"${repo}"}'
   """
   echo repo
     //sh"aws codecommit create-repository --repository-name ${repo} --tags Name=${appName}  --repository-description 'This '${repo}'- repo is auto created by jenkins'"
     sh """
        git clone https://github.com/minaxijoshi3101?tab=repositories/${repo}
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
