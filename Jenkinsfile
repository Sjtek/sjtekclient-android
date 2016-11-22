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
        sh 'rm -vf */build/test-results/release/*.xml'
        sh './gradlew test'
        junit allowEmptyResults: true, testResults: '*/build/test-results/release/*.xml'
    }

    stage('Lint') {
        sh './gradlew lint'
        androidLint canComputeNew: false, defaultEncoding: '', failedTotalHigh: '0', failedTotalLow: '30', failedTotalNormal: '20', healthy: '', pattern: '**/lint-results*.xml', unHealthy: '', unstableTotalHigh: '0', unstableTotalLow: '20', unstableTotalNormal: '10'
    }
}
