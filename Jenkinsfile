#!/usr/bin/env groovy
pipeline {
  agent { label 'docker' }

  environment {
    MAVEN_IMAGE = 'maven:3.8-jdk-11'
    DOCKER_BUILDKIT = '1'
    REPO = 'registry.developmentgateway.org/'
  }

  stages {

    stage('Compile') {
      steps {
        script {
          def args = "-e JAVA_TOOL_OPTIONS=-Duser.home=$WORKSPACE_TMP"
          withDockerContainer(image: env.MAVEN_IMAGE, args: args) {
            // TODO: checkstyle
            sh 'mvn -B clean package -DskipTests -Dcheckstyle.skip && mkdir forms/target/deps'
            dir('forms/target/deps') {
              sh 'jar -xf ../*.jar'
            }
          }
        }
      }
    } // Compile

    stage('Package & Publish') {
      steps {
        script {
          def tag = ['main', 'master'].contains(env.BRANCH_NAME) ?
            'latest' :
            env.BRANCH_NAME.replaceAll('[^\\p{Alnum}-_]', '_').toLowerCase()
          withEnv(["TAG=$tag"]) {
            def dc = 'docker-compose'
            sh "$dc build admin && $dc push admin"
          }
        }
      }
    } // Package & Publish

  } // stages
}
