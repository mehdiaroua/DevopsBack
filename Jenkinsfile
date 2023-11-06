pipeline {
    agent any
    
    environment {
        SONAR_CREDENTIALS = credentials('sonarID')
        GIT_CREDENTIALS = credentials('GITT')
        GIT_REPO_URL = "github.com/mehdiaroua/DevopsBack.git"
        GIT_REPO_URLL = "github.com/mehdiaroua/devopsfront.git"
        GIT_BRANCH = "main"
        REPO_DIR = 'DevopsBack'
        REPO_DIRR = 'DevopsFront'
    }
    
    stages {
       stage('Clone my Branch from Git') {
            steps {
                script {
                    if (fileExists(REPO_DIR)) {
                        dir(REPO_DIR) {
                            sh 'git stash'
                            sh 'git pull origin main --rebase'
                        }
                    } else {
                        sh "git clone --branch ${env.GIT_BRANCH} https://mehdiaroua23:${env.GIT_CREDENTIALS}@${env.GIT_REPO_URL} ${REPO_DIR}"
                    }
                }
            }
        }
        stage('Clone my FrontBranch from Git') {
            steps {
                script {
                    // Check if the repository directory exists
                    if (fileExists(REPO_DIRR)) {
                        // If the directory exists, update the existing repository
                        dir(REPO_DIRR) {
                            sh "git checkout ${env.GIT_BRANCH}"
                            sh 'git stash'
                            sh 'git pull --rebase'
                        }
                    } else {
                        // If the directory does not exist, perform the initial clone
                        sh "git clone --branch ${env.GIT_BRANCH} https://mehdiaroua23:${env.GIT_CREDENTIALS}@${env.GIT_REPO_URLL} ${REPO_DIRR}"
                    }
                }
            }
        }
        stage('Maven Build') {
            steps {
               dir(REPO_DIR) {
            sh 'mvn clean install'
        }
    }
}
       

        stage('Clean and compile with Maven') {
            steps {
               dir(REPO_DIR) {
                    sh 'mvn clean compile'
                }
            }
        }/*
        stage('Test with Maven') {
            steps {
               dir(REPO_DIR) {
                    sh 'mvn test'
                }
            }
        }*/
       
        stage('JaCoCo Code Coverage') {
            steps {
                dir(REPO_DIR) {
                    sh 'mvn jacoco:prepare-agent test jacoco:report'
                }
            }
        }
        stage('Publish JaCoCo Reports') {
            steps {
                dir(REPO_DIR) {
                    sh 'mvn jacoco:report'
                    stash name: 'jacoco-report', includes: 'target/site/jacoco/**'
                }
            }
        }
    
      /*stage('Deploy to Nexus Repository') {
            steps {
               dir(REPO_DIR) {
               withCredentials([usernamePassword(credentialsId: 'nexus', passwordVariable: 'pwd', usernameVariable: 'name')]) {
                sh "mvn deploy -s /usr/share/maven/conf/settings.xml -Dusername=\$name -Dpassword=\$pwd"
            }
           }
          }
        }*/

        stage('Sonar test') {
            steps {
                dir(REPO_DIR) {
                    withCredentials([usernamePassword(credentialsId: 'sonarID', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASSWORD')]) {
                    
                        sh "mvn test sonar:sonar -Dsonar.login=\$SONAR_USER -Dsonar.password=\$SONAR_PASSWORD"}
                    }
                
            }
        }
        
        stage('Install Node.js Packages') {
            steps {
                dir("${REPO_DIRR}/frontend") {
                    sh 'npm install'
                }
            }
        }
        stage('Build Angular Application') {
            steps {
                dir("${REPO_DIRR}/frontend") {
                      sh '/usr/bin/ng build'
             }
            }
       }
        
        
         stage('Build Back DockerImage') {
           steps {
                script {
                   
                       checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'dockertoken', url: 'https://github.com/mehdiaroua/DevopsBack']])                    
                        sh 'docker build -t mahdiaroua72/springapp:back .'
                    
                }
            }
        }
        //stage('Build Front DockerImage') {
          //  steps {
            //    script {
                   
              //    checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'dockerhub', url: 'https://github.com/mehdiaroua/devopsfront']])
                    // sh 'docker build -t mahdiaroua72/angularr .'
               //}
           //}
        //}
        
       stage('Push Docker Images to Docker Hub') {
            steps {
                script {
                   withCredentials([string(credentialsId: 'dockertoken', variable: 'dockerpwd')]){
                     
                      dir("${REPO_DIR}") {
                      sh "docker login -u mahdiaroua72 -p ${dockerpwd}"
                       sh 'docker push mahdiaroua72/springapp:back'
                       

                       

                      }
                      dir("${REPO_DIRR}/frontend") {
                      sh "docker login -u mahdiaroua72 -p ${dockerpwd}"
                       sh 'docker push mahdiaroua72/angular'
                      }
                     }
                    }
                }
        }
          stage('Run Docker Compose') {
    steps {
        script {
            checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'dockertoken', url: 'https://github.com/mehdiaroua/DevopsBack']])
            
            
                sh 'docker-compose up -d'
            
        }
       }
     }



    }
    post {
        success {
            mail to: 'mehdi.aroua@esprit.tn',
            subject: 'Jenkins Build pipeline: Success',
            body: '''Your pipeline build success.
                Build and push to Docker Hub successful.
                Thank you, go and check it
                Mahdi Aroua'''
            
             unstash 'jacoco-report'
        archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
        }

        failure {
            mail to: 'mehdi.aroua@esprit.tn',
            subject: 'Jenkins Build pipeline: Failure',
            body: '''Your pipeline build failed.
                Build or push to Docker Hub failed.
                Thank you, please check
                Mahdi Aroua'''
        }
    }
    
}
