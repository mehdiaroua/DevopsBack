pipeline {
    agent any
    stages {
        stage('Set Java Version') {
            steps {
                script {
                    // Set the default JAVA_HOME to Java 8
                    tool name: 'JAVA_HOME', type: 'jdk'
                }
            }
        }
        stage('Checkout') {
            steps {
                script {
                    // Checkout your source code from the repository.
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/master']], 
                        userRemoteConfigs: [[url: 'https://github.com/Mohamed-Rouahi/DevopsProject.git']]
                    ])
                }
            }
        }
        stage('BUILD') {
            steps {
                // Use Java 8 for this stage
                withEnv(["JAVA_HOME=${tool name: 'JAVA_HOME', type: 'jdk'}"]) {
                    sh 'mvn clean package'
                }
            }
        }
        stage('COMPILE') {
            steps {
                // Use the default Java 8 for this stage
                sh 'mvn compile'
            }
        }
        stage("SonarQube Analysis") {
            steps {
                // Set Java 11 for this stage
                tool name: 'JAVAA_HOME', type: 'jdk'
                withEnv(["JAVA_HOME=${tool name: 'JAVAA_HOME', type: 'jdk'}"]) {
                    withSonarQubeEnv('sonarQube') {
                        script {
                            def scannerHome = tool 'SonarQubeScanner'
                            withEnv(["PATH+SCANNER=${scannerHome}/bin"]) {
                                sh '''
                                    mvn sonar:sonar \
                                        -Dsonar.java.binaries=target/classes
                                '''
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            emailext(
                subject: "Success: SonarQube Analysis Completed",
                body: "SonarQube analysis was successful.",
                to: "mohamed.rouahi@esprit.tn"
            )
        }
        failure {
            emailext(
                subject: "Failure: SonarQube Analysis Failed",
                body: "SonarQube analysis has failed.",
                to: "mohamed.rouahi@esprit.tn"
            )
        }
    }
}
