#!groovy

node {

   stage 'Checkout'
   git url: 'https://github.com/Sjtek/sjtekclient-android'

   stage 'Prepare'
   step ([$class: 'CopyArtifact',
          projectName: 'sjtekcontrol-core',
          filter: 'data/build/libs/data.jar',
          target: 'sjtekcontrol-data/']);
   sh '''mkdir -p app/libs
         mv sjtekcontrol-data/data/build/libs/data.jar app/libs/data.jar'''
   sh 'echo "sdk.dir=/tools/android-sdk" > local.properties'
   
   stage 'Build'
   sh './gradlew assembleDebug'
   archive 'app/build/outputs/apk/app-debug.apk'

}
