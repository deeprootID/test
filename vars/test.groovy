#!/usr/bin/env groovy

def call(Map param){
pipeline {
    agent {
        node {
	    label 'jenkins'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deliver') {
            steps {
                sh './jenkins/scripts/deliver.sh'
		sh "scp /var/lib/jenkins/workspace/test/target/my-app-1.0-SNAPSHOT.jar root@${param.hostname}:/home/revina/"
		sh "ssh root@${param.hostname} \'java -jar /home/revina/my-app-1.0-SNAPSHOT.jar\'"
            }
        }
    }
}
}
