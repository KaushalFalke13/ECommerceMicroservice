pipeline {
    agent any

    environment {
        IMAGE_NAME = "orders-service"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_SHA = sh(script: "git rev-parse --short=8 HEAD", returnStdout: true).trim()

                    // Does the current commit have an exact release tag like v1.2.0 on it?
                    def tag = sh(
                        script: "git describe --tags --exact-match --match 'v[0-9]*.[0-9]*.[0-9]*' 2>/dev/null || true",
                        returnStdout: true
                    ).trim()

                    if (tag) {
                        env.IS_RELEASE = "true"
                        env.APP_VERSION = tag.replaceFirst('^v', '')
                        echo "RELEASE build detected: ${tag}"
                    } else {
                        env.IS_RELEASE = "false"
                        env.APP_VERSION = "dev"
                        echo "Dev build (commit ${env.GIT_SHA} has no release tag)"
                    }
                }
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

        stage('Check For Existing Release Tag') {
            when {
                environment name: 'IS_RELEASE', value: 'true'
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    script {
                        def exists = sh(
                            script: """
                                curl -s -o /dev/null -w "%{http_code}" \
                                https://hub.docker.com/v2/repositories/${DOCKER_USERNAME}/${env.IMAGE_NAME}/tags/v${env.APP_VERSION}/
                            """,
                            returnStdout: true
                        ).trim()

                        if (exists == "200") {
                            error("v${env.APP_VERSION} already exists on Docker Hub. Releases are immutable — bump the version instead of reusing it.")
                        }
                    }
                }
            }
        }

        stage('Build Orders Service Docker Image') {
            steps {
                dir('Backend/OrdersServices') {
                    sh "docker build -t ${env.IMAGE_NAME}:sha-${env.GIT_SHA} ."
                    script {
                        if (env.IS_RELEASE == "true") {
                            sh "docker tag ${env.IMAGE_NAME}:sha-${env.GIT_SHA} ${env.IMAGE_NAME}:v${env.APP_VERSION}"
                        }
                    }
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

                        docker tag "$IMAGE_NAME:sha-$GIT_SHA" "$DOCKER_USERNAME/$IMAGE_NAME:sha-$GIT_SHA"
                        docker push "$DOCKER_USERNAME/$IMAGE_NAME:sha-$GIT_SHA"
                    '''
                    script {
                        if (env.IS_RELEASE == "true") {
                            sh '''
                                docker tag "$IMAGE_NAME:v$APP_VERSION" "$DOCKER_USERNAME/$IMAGE_NAME:v$APP_VERSION"
                                docker push "$DOCKER_USERNAME/$IMAGE_NAME:v$APP_VERSION"
                            '''
                        }
                    }
                    sh 'docker logout'
                }
            }
        }
    }

    post {
        success {
            script {
                if (env.IS_RELEASE == "true") {
                    echo "RELEASE v${env.APP_VERSION} (sha-${env.GIT_SHA}) pushed."
                } else {
                    echo "Dev build sha-${env.GIT_SHA} pushed. No version change."
                }
            }
        }
    }
}