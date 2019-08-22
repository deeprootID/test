#!/usr/bin/env groovy

def call(){
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
		sh 'scp /var/lib/jenkins/workspace/test/target/my-app-1.0-SNAPSHOT.jar root@192.168.99.101:/home/revina/'
		sh 'ssh root@192.168.99.101 \'java -jar /home/revina/my-app-1.0-SNAPSHOT.jar\''
            }
        }
    }
}
}
