pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Java & Maven') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Build Orders Service') {
            steps {
                dir('Backend/OrdersServices') {
                    sh 'mvn clean package'
                }
            }
        }
    }
}
