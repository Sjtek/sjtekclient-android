#!groovy

node {

    stage 'Checkout'
    git url: 'https://github.com/Sjtek/sjtekclient-android'

    stage 'Build'
    step([$class     : 'CopyArtifact',
          projectName: 'sjtekcontrol-core',
          filter     : 'data/build/libs/data.jar',
          flatten    : true,
          target     : 'app/libs/']);
    sh 'echo "sdk.dir=/tools/android-sdk" > local.properties'
    sh './gradlew assembleDebug'
    archive 'app/build/outputs/apk/app-debug.apk'

}