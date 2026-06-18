pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = 'stationery-registry:5000'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile & Test Microservices') {
            parallel {
                stage('Auth Service') {
                    steps {
                        dir('auth-service') {
                            bat 'mvn clean test-compile'
                        }
                    }
                }
                stage('Inventory Service') {
                    steps {
                        dir('inventory-service') {
                            bat 'mvn clean test-compile'
                        }
                    }
                }
                stage('Request Service') {
                    steps {
                        dir('request-service') {
                            bat 'mvn clean test-compile'
                        }
                    }
                }
            }
        }

        stage('Unit Testing') {
            parallel {
                stage('Auth Service Tests') {
                    steps {
                        dir('auth-service') {
                            bat 'mvn test -DargLine="-Dnet.bytebuddy.experimental=true"'
                        }
                    }
                }
                stage('Inventory Service Tests') {
                    steps {
                        dir('inventory-service') {
                            bat 'mvn test -DargLine="-Dnet.bytebuddy.experimental=true"'
                        }
                    }
                }
                stage('Request Service Tests') {
                    steps {
                        dir('request-service') {
                            bat 'mvn test -DargLine="-Dnet.bytebuddy.experimental=true"'
                        }
                    }
                }
            }
        }

        stage('Build & Package JARs') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Dockerize Services') {
            steps {
                bat "docker build -t ${DOCKER_REGISTRY}/auth-service:${BUILD_NUMBER} ./auth-service"
                bat "docker build -t ${DOCKER_REGISTRY}/inventory-service:${BUILD_NUMBER} ./inventory-service"
                bat "docker build -t ${DOCKER_REGISTRY}/request-service:${BUILD_NUMBER} ./request-service"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'Capstone microservices build pipeline completed successfully!'
        }
        failure {
            echo 'Capstone pipeline failed. Check build logs.'
        }
    }
}
