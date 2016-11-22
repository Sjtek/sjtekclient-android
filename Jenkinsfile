#!groovy

node {

    stage('Checkout') {
        checkout scm
    }

    stage('Build') {
        sh './gradlew assemble'
        archive 'app/build/outputs/apk/*.apk'
    }

    stage('Test') {
        sh './gradlew test'
        junit 'app/build/test-results/release/*.xml'
    }
}
