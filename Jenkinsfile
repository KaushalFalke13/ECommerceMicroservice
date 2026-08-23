def SERVICES = [
    [name: 'api-gateway',     path: 'Backend/Api-Gateway'],
    [name: 'auth-service',    path: 'Backend/AuthServices'],
    [name: 'eureka-client',   path: 'Backend/EurekaClient'],
    [name: 'orders-service',  path: 'Backend/OrdersServices'],
    [name: 'payment-service', path: 'Backend/PaymentServices'],
    [name: 'products-service', path: 'Backend/ProductsServices'],
    [name: 'user-service',    path: 'Backend/UserServices'],
]

pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                script {
                    env.GIT_SHA = sh(script: "git rev-parse --short=8 HEAD", returnStdout: true).trim()

                    env.PREVIOUS_COMMIT = env.GIT_PREVIOUS_SUCCESSFUL_COMMIT ?: sh(
                        script: "git rev-parse HEAD~1 2>/dev/null || echo ''",
                        returnStdout: true
                    ).trim()

                    env.CHANGED_FILES = sh(
                        script: env.PREVIOUS_COMMIT ?
                            "git diff --name-only ${env.PREVIOUS_COMMIT} HEAD" :
                            "git ls-files",   // first-ever build: treat everything as changed
                        returnStdout: true
                    ).trim()

                    // All release tags pointing at this exact commit, e.g. orders-service-v1.2.0
                    env.TAGS_ON_HEAD = sh(
                        script: "git tag --points-at HEAD",
                        returnStdout: true
                    ).trim()
                }
            }
        }

        stage('Build & Push Changed Services') {
            steps {
                script {
                    def changedFiles = env.CHANGED_FILES.split('\n')
                    def tagsOnHead   = env.TAGS_ON_HEAD ? env.TAGS_ON_HEAD.split('\n') : []

                    def toBuild = SERVICES.findAll { svc ->
                        def pathChanged = changedFiles.any { it.startsWith("${svc.path}/") }
                        def releaseTag  = tagsOnHead.find { it =~ /^${svc.name}-v[0-9]+\.[0-9]+\.[0-9]+$/ }
                        svc.releaseTag = releaseTag   // stash it for later
                        return pathChanged || releaseTag
                    }

                    if (toBuild.isEmpty()) {
                        echo "No service paths changed and no release tags on this commit. Nothing to build."
                        return
                    }

                    echo "Services to build this run: ${toBuild.collect { it.name }.join(', ')}"

                    def branches = [:]
                    toBuild.each { svc ->
                        branches[svc.name] = {
                            buildAndPushService(svc)
                        }
                    }
                    parallel branches
                }
            }
        }
    }
}

// ---- Reusable logic for a single service ----
def buildAndPushService(svc) {
    def isRelease = svc.releaseTag != null
    def version = isRelease ? svc.releaseTag.replaceFirst("^${svc.name}-v", '') : null

    dir(svc.path) {
        echo "[${svc.name}] Building (${isRelease ? "RELEASE v${version}" : 'dev build'})"

        sh 'chmod +x mvnw'
        sh './mvnw clean package'

        withCredentials([
            usernamePassword(
                credentialsId: 'dockerhub-credentials',
                usernameVariable: 'DOCKER_USERNAME',
                passwordVariable: 'DOCKER_PASSWORD'
            )
        ]) {
            if (isRelease) {
                def exists = sh(
                    script: """
                        curl -s -o /dev/null -w "%{http_code}" \
                        https://hub.docker.com/v2/repositories/\$DOCKER_USERNAME/${svc.name}/tags/v${version}/
                    """,
                    returnStdout: true
                ).trim()

                if (exists == "200") {
                    error("[${svc.name}] v${version} already exists on Docker Hub. Refusing to overwrite.")
                }
            }

            sh "docker build -t ${svc.name}:sha-${env.GIT_SHA} ."
            if (isRelease) {
                sh "docker tag ${svc.name}:sha-${env.GIT_SHA} ${svc.name}:v${version}"
            }

            sh '''
                echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
            '''

            sh """
                docker tag ${svc.name}:sha-${env.GIT_SHA} \$DOCKER_USERNAME/${svc.name}:sha-${env.GIT_SHA}
                docker push \$DOCKER_USERNAME/${svc.name}:sha-${env.GIT_SHA}
            """

            if (isRelease) {
                sh """
                    docker tag ${svc.name}:v${version} \$DOCKER_USERNAME/${svc.name}:v${version}
                    docker push \$DOCKER_USERNAME/${svc.name}:v${version}
                """
            }

            sh 'docker logout'
        }

        echo "[${svc.name}] Done. ${isRelease ? "Released v${version}" : "Dev image sha-${env.GIT_SHA} pushed"}"
    }
}