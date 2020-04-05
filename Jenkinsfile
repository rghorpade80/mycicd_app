pipeline {
    agent any 
    environment {
        MAVEN_HOME = tool('Maven')
        JAVA_HOME = tool('JDK')
	registry = "rghorpade80/mycicd_app_repo"
        registryCredential = 'docker_hub_login'
    }
    
    stages {

         stage('MAVEN BUILD') {
            steps {
                sh '${MAVEN_HOME}/bin/mvn -B package'
              
            }
            
        }    
        
        
        stage('COPY ARTIFACT') {
            
            steps {
                
                sh label: '', script: '''
                
                #! bin/bash
              
                    
                workspace_HOME=/var/lib/jenkins/workspace/$JOB_NAME
		
		
		mkdir -p /var/lib/jenkins/docker_images_for_jenkins/$JOB_NAME/config/
		
		mkdir -p /var/lib/jenkins/docker_images_for_jenkins/$JOB_NAME/src/main/webapp/WEB-INF/views/
		
		DOCKER_IMAGE_CREATE_HOME=/var/lib/jenkins/docker_images_for_jenkins/$JOB_NAME
		
		cp -R $workspace_HOME/Dockerfile $DOCKER_IMAGE_CREATE_HOME/
                    
                cp -R $workspace_HOME/config/* $DOCKER_IMAGE_CREATE_HOME/config/
                    
                cp -R $workspace_HOME/src/main/webapp/WEB-INF/views/* $DOCKER_IMAGE_CREATE_HOME/src/main/webapp/WEB-INF/views/
                    
                cp -R $workspace_HOME/target/GraphSearchPortal-0.0.1-SNAPSHOT.jar $DOCKER_IMAGE_CREATE_HOME
                    
                mv $DOCKER_IMAGE_CREATE_HOME/GraphSearchPortal-0.0.1-SNAPSHOT.jar $DOCKER_IMAGE_CREATE_HOME/GraphSearchPortal.jar
                    
                cd $DOCKER_IMAGE_CREATE_HOME/config/
                sed -i "s/172.16.200.72:8090/172.16.48.108:8080/g"  webServicesURL.xml   ;
                sed -i "s/honda/hods/g" webServicesURL.xml ;
                sed -i "s/ITVSTestDB/ToyotaDB2/g" application.properties ;
                sed -i "s/localhost/172.16.48.150/g" application.properties ;
                sed -i "s/8091/8093/g" application.properties ;'''
                
            }
            
        }    
        
        
         
        stage ('BUILD DOCKER IMAGE'){
            steps{
			   
              
                sh 'cd /var/lib/jenkins/docker_images_for_jenkins/mycicd_test_app_pipeline && docker build --tag $JOB_NAME:v1.$BUILD_ID . '
                sh 'docker tag $JOB_NAME:v1.$BUILD_ID rghorpade80/mycicd_app_repo:v1.$BUILD_ID'
                sh 'docker tag $JOB_NAME:v1.$BUILD_ID rghorpade80/mycicd_app_repo:latest'
                
                
            }
            
        }
       
	   stage('PUSH IMAGE') {
           steps{
               script {
		       docker.withRegistry( '', registryCredential ) {
		       
		       	sh 'docker push rghorpade80/mycicd_app_repo:v1.$BUILD_ID'
                        sh 'docker push rghorpade80/mycicd_app_repo:latest'
                        sh 'docker rmi -f rghorpade80/mycicd_app_repo/$JOB_NAME:v1.$BUILD_ID'
                        sh 'docker rmi -f $JOB_NAME:v1.$BUILD_ID'
		     }
        }
      }
    }
	   
	   
        stage ('DEPLOY GRAPH SEARCH ON K8S'){
		
            steps{
					kubernetesDeploy configs: 'deployment.yml', kubeConfig: [path: ''], kubeconfigId: 'kubeconfig', secretName: '', ssh: [sshCredentialsId: '*', sshServer: ''], textCredentials: [certificateAuthorityData: '', clientCertificateData: '', clientKeyData: '', serverUrl: 'https://']
					
            }
            
        }


    }

}
