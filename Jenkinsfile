#!groovy

node {

    stage 'Checkout'
    checkout scm

    stage 'Build'
    sh './gradlew assemble'
    archive 'app/build/outputs/apk/*.apk wear/build/outputs/apk/*.apk'
}
