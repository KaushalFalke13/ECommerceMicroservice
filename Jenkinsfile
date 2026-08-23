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

        stage('Build Orders Service Docker Image') {
            steps {
                dir('Backend/OrdersServices') {
                    sh 'docker build -t orders-service:latest .'
                }
            }
        }

        stage('Push Orders Service Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker tag orders-service:latest "$DOCKER_USERNAME/orders-service:latest"
                        docker push "$DOCKER_USERNAME/orders-service:latest"
                        docker logout
                    '''
                }
            }
        }
    }
}