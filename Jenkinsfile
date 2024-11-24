pipeline {
    agent any

    stages {
        stage('test') {
            steps {
                sh './gradlew test'
            }
        }
        stage('build jar') {
            steps {
                sh './gradlew build -x test'
            }
        }
        stage('load to remote server') {
            steps {
                sshagent(credentials : ['prusakova-ssh']) {
                    sh "scp -o StrictHostKeyChecking=no ./build/libs/link-shortener-${tag}.jar prusakova@195.93.252.91:./nexus/link-shortener-${tag}.jar"
                }
            }
        }
    }
}