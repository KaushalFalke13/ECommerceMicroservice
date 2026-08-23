pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Java') {
            steps {
                sh 'java -version'
            }
        }

        stage('Build Orders Service') {
            steps {
                dir('Backend/OrdersServices') {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package'
                }
            }
        }

        stage('Build Order Service Docker Image') {
            steps {
                dir('Backend/OrdersServices') {
                    sh 'docker build -t orders-service:latest .'
                }
            }

        }
    }
}
