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
                echo "Building a project"
                sh 'mvn clean package'                
            }
            post {
                success {
                     echo "Archieving an artifact"
                     archiveArtifacts artifacts: '**/*.war', followSymlinks: false   
                }
                failure {
                    echo "Failed to build a project"
                }
            }
        }

        stage("Build an image") {
            steps {
                script {
                    dockerImage = docker.build $REGISTRY + ":$BUILD_NUMBER"
                }
            }
        }

        stage("Push Docker Image to registry") {
            steps {
                script {
                    docker.withRegistry("https://registry.hub.docker.com/", REGISTRY_CREDENTIALS) {
                        dockerImage.push();
                        dockerImage.push("latest")
                    }
                }
            }
        }

        stage("Remove unused images") {
            steps {
                sh "docker rmi $REGISTRY:$BUILD_NUMBER"
                sh "docker rmi $REGISTRY:latest"
            }
        }

    }
        
}