#!groovy

node {

    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh './gradlew assemble'
    archive '*/build/outputs/apk/*.apk'
}
