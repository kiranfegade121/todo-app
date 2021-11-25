pipeline {

    agent {
        lable "linux-instance"
    }

    tools {
        maven "DEFAULT_MAVEN"
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

        stage("Perform code analysis and deploy an application to staging") {

            parallel {

                stage("Perform code analysis") {
                     environment {
                         SCANNER_HOME = tool "SONAR_SCANNER"
                         ORGANIZATION = "my-organization-1234"
                         PROJECT_NAME = "todo-app-123"
                     }
                     steps {
                         withSonarQubeEnv(installationName: 'SonarCloudServer', credentialsId: 'SonarQubeToken') {
                             sh '''$SCANNER_HOME/bin/sonar-scanner -Dsonar.projectKey=$PROJECET_NAME \
                                   -Dsonar.organization=$ORGANIZATION \
                                   -Dsonar.sources=src \
                                   -Dsonar.java.binaries=target'''     
                         }
                     }
                }

                stage("Quality Gate") {
                    steps {
                        timeout(time: 3, unit: "MINUTES") {
                            waitForQualityGate abortPipeline: true, credentialsId: 'SonarQubeToken'
                        }
                    }
                } 

                stage("Deploy an application to staging") {
                    steps {
                        echo "Deploying an application to staging environment"
                        deploy adapters: [tomcat8(credentialsId: 'b9899f1e-0a8f-4538-b56f-2f5133959af9', path: '', url: 'http://35.226.113.24:8080/')], contextPath: null, war: '**/*.war'
                    }
                    post {
                        success {
                            echo "An application is successfully deployed in staging environment."
                        }
                        failure {
                            echo "Failed to deploy an application in staging environment."
                        }
                    }
                }
            }
        }

        stage("Deploy to production") {
           
            steps {
                 timeout(time: 1, unit: "DAYS") {
                      input 'Do you want to deploy an application to production?'
                 }
                echo "Deploying an application to production environment"
                deploy adapters: [tomcat8(credentialsId: '78825198-6145-4027-9329-eef1b87f855f', path: '', url: 'http://34.71.28.84:8080/')], contextPath: null, war: '**/*.war'
            }
            post {
                success {
                    echo "An application is successfully deployed to prod."
                }
                failure {
                    echo "Failed to deploy an application to prod."
                }
            }
        }
    }
}