pipeline {
    tools {
      maven "M3"
    }
    agent any
    stages {
      stage("Preparation") {
        steps {
          checkout scm
        }
      }
      stage("Compile") {
        steps {
          sh 'mvn clean compile'
        }
      }
      stage("Test") {
        steps {
          sh 'mvn verify'
        }
      }
      stage("Install") {
        steps {
          sh 'mvn install -Dmaven.test.skip=true'
        }
      }
    }
    post {
       always {
 	    junit "**/target/surefire-reports/TEST-*.xml"
       }
       success {
         archiveArtifacts artifacts: '**/target/*.jar, **/target/*.war', fingerprint: true
       }
    }
}