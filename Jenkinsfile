pipeline {

    agent {
        label "linux-instance"        
    }

    tools {
        maven "DEFAULT_MAVEN"
    }

    environment {
        REGISTRY = "amitfegade121/todo-app"
        REGISTRY_CREDENTIALS = "dockerhubcred"
        dockerImage = ""
    }

    stages {

        stage("Build and Package an application") {
            steps {
                echo "Building an application"
                sh 'mvn clean package'
            }
            post {
                success {
                    echo "Archiving an artifact"
                    archiveArtifacts artifacts: '**/*.war', followSymlinks: false
                }
                failure {
                    echo "Failed to build an application"
                }
            }
        }

        stage("Build an docker image") {
            steps {
                script {
                    dockerImage = docker.build  REGISTRY + ":$BUILD_NUMBER"
                }
            }
        }

        stage("Push docker image") {
            steps {
                script {
                    docker.withRegistry("", REGISTRY_CREDENTIALS) {
                        dockerImage.push();    
                        dockerImage.push("latest");   
                    }
                }
            }
        }

        stage("Remove unwanted docker images") {
            steps {
                sh '''docker rmi $REGISTRY:$BUILD_NUMBER 
                      docker rmi $REGISTRY:latest
                    '''
            }
        }
    }
}

