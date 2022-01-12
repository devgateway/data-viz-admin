#!/usr/bin/env groovy
pipeline {
  agent { label 'docker' }

  environment {
    MAVEN_IMAGE = 'maven:3.8-jdk-11'
  }

  stages {

    stage('Package') {
      steps {
        script {
          def args = "-e JAVA_TOOL_OPTIONS=-Duser.home=$WORKSPACE_TMP"
          withDockerContainer(image: env.MAVEN_IMAGE, args: args) {
            // TODO
            sh 'mvn -B clean package -DskipTests -Dcheckstyle.skip && mkdir forms/target/deps'
            dir('forms/target/deps') {
              sh 'jar -xf ../*.jar'
            }
          }
        }
      }
    } // Package

    stage('Build Image') {
      steps {
        script {
          def image = 'registry.developmentgateway.org/tcdi/admin'
          def tag = ['main', 'master'].contains(env.BRANCH_NAME) ?
            'latest' :
            env.BRANCH_NAME.replaceAll('[^\\p{Alnum}-_]', '_').toLowerCase()
          sh "docker build -t $image:$tag ."
        }
      }
    } // Build Image

    stage('Publish Image') {
      steps {
        script {
          def image = 'registry.developmentgateway.org/tcdi/admin'
          def tag = ['main', 'master'].contains(env.BRANCH_NAME) ?
            'latest' :
            env.BRANCH_NAME.replaceAll('[^\\p{Alnum}-_]', '_').toLowerCase()
          sh "docker push $image:$tag"
        }
      }
    } // Publish Image

  } // stages
}
