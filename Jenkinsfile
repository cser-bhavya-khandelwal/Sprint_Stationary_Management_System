pipeline {
    agent any

    tools {
        maven 'Maven 3.9'
        jdk 'Java 17'
    }

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
                            sh 'mvn clean test-compile'
                        }
                    }
                }
                stage('Inventory Service') {
                    steps {
                        dir('inventory-service') {
                            sh 'mvn clean test-compile'
                        }
                    }
                }
                stage('Request Service') {
                    steps {
                        dir('request-service') {
                            sh 'mvn clean test-compile'
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
                            sh 'mvn test'
                        }
                    }
                }
                stage('Inventory Service Tests') {
                    steps {
                        dir('inventory-service') {
                            sh 'mvn test'
                        }
                    }
                }
                stage('Request Service Tests') {
                    steps {
                        dir('request-service') {
                            sh 'mvn test'
                        }
                    }
                }
            }
        }

        stage('Build & Package JARs') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Dockerize Services') {
            steps {
                script {
                    docker.build("${DOCKER_REGISTRY}/auth-service:${BUILD_NUMBER}", "./auth-service")
                    docker.build("${DOCKER_REGISTRY}/inventory-service:${BUILD_NUMBER}", "./inventory-service")
                    docker.build("${DOCKER_REGISTRY}/request-service:${BUILD_NUMBER}", "./request-service")
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'Capstone microservices build pipeline completed successfully!'
        }
        failure {
            echo 'Capstone pipeline failed. Check build logs.'
        }
    }
}
