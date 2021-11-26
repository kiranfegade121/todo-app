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

        stage("Deploy to staging environment and perform code analysis") {
            parallel {

                stage("Perform code analysis") {
                     environment {
                         SCANNER_HOME = tool "SONAR_SCANNER"
                         ORGANIZATION = "my-organization-1234"
                         PROJECT_NAME = "todo-app-123"
                     }
                     steps {
                         withSonarQubeEnv(installationName: 'SonarCloudServer', credentialsId: 'SonarQubeToken') {
                             sh '''$SCANNER_HOME/bin/sonar-scanner -Dsonar.projectKey=$PROJECT_NAME \
                                   -Dsonar.organization=$ORGANIZATION \
                                   -Dsonar.sources=src \
                                   -Dsonar.java.binaries=target'''     
                         }
                     }
                }

                stage("Deploy an application to staging senvironment") {
                    steps {
                        script {
                            def remote = [:]
                            remote.name = 'staging-server'
                            remote.host = "10.128.0.13"                         
                            remote.allowAnyHosts = true
                            withCredentials([usernamePassword(credentialsId: 'stagingservercred', passwordVariable: 'passwordValue', usernameVariable: 'username')]) {
                                    remote.user = username
                                    remote.password = passwordValue
                                    sshCommand remote: remote, command: "docker container run -d -p 8080:8080 amitfegade121/todo-app"
                            }
                        }
                    }
                    post {
                        success {
                            echo "An application is deployed successfully to staging environment"
                        }
                        failure {
                            echo "Failed to deploy an application to staging".
                        }
                    }                
            }

            stage("Deploy an application to production server") {
                steps {
                      timeout(time: 1, unit: "DAYS") {
                           input 'Do you want to deploy an application to production?'
                      }
                      script {
                            def remote = [:]
                            remote.name = 'production-server'
                            remote.host = "10.128.0.14"                         
                            remote.allowAnyHosts = true
                            withCredentials([usernamePassword(credentialsId: 'productionservercred', passwordVariable: 'passwordValue', usernameVariable: 'username')]) {
                                    remote.user = username
                                    remote.password = passwordValue
                                    sshCommand remote: remote, command: "docker container run -d -p 8080:8080 amitfegade121/todo-app"
                            }
                      }
                }
                post {
                    success {
                        echo "An application is deployed to production environment successfully."
                    }
                    failure {
                        echo "Failed to deploy an application to production environment."
                    }
                }
            }
        }
    }
}

