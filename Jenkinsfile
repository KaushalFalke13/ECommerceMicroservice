pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Verify Code') {
            steps {
                sh 'pwd'
                sh 'ls -la'
            }
        }
    }
}