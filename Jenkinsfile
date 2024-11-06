pipeline {
    agent any
    options {
        disableConcurrentBuilds() // 동시에 여러 빌드가 실행되지 않도록 설정
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "저장소 체크아웃 중"
                    echo "GITHUB_CREDENTIAL_ID: ${env.KINDERPIA_GITHUB_CREDENTIAL_ID}"
                    echo "DOCKER_CREDENTIAL_ID: ${env.DOCKER_CREDENTIAL_ID}"
                    echo "EC2_CREDENTIAL_ID: ${env.EC2_CREDENTIAL_ID}"
                    echo "EC2_IP: ${env.EC2_IP}"
                }
                git branch: 'dev',
                    credentialsId: "${env.GITHUB_CREDENTIAL_ID}",
                    url: 'https://github.com/SeSAC-3rd-Kinderpia/kinderpia_back.git'

                // Git 체크아웃 후 현재 브랜치 이름 가져오기
                script {
                    def branchName = sh(script: 'git rev-parse --abbrev-ref HEAD', returnStdout: true).trim()
                    if (branchName != 'dev') {
                        echo "이 빌드는 ${branchName} 브랜치에 대한 푸시로 트리거되었습니다. 'dev' 브랜치가 아니므로 빌드를 건너뜁니다."
                        currentBuild.result = 'SUCCESS'
                        return
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Docker 이미지 빌드 중: ymind14563/kinderpia_back-image:latest"
                    docker.build("ymind14563/kinderpia_back-image:latest")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo "Docker Hub에 이미지 푸시 중"
                    docker.withRegistry('https://index.docker.io/v1/', "${env.DOCKER_CREDENTIAL_ID}") {
                        docker.image("ymind14563/kinderpia_back-image:latest").push()
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo "EC2 인스턴스로 배포 단계 시작: IP 주소 ${env.EC2_IP}..."
                }
                sshagent (credentials: ["${env.EC2_CREDENTIAL_ID}"]) {
                    withCredentials([usernamePassword(credentialsId: "${env.DOCKER_CREDENTIAL_ID}", passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                        echo "EC2 인스턴스로 SSH 연결 시도 중..."

                        sh """
                            ssh -o StrictHostKeyChecking=no ubuntu@${env.EC2_IP} << 'EOF'
                            #!/bin/bash
                            set -e
                            echo "=== EC2에서 SSH 세션 시작 ==="

                            # 환경 변수 확인
                            export DOCKER_USERNAME="${DOCKER_USERNAME}"
                            export DOCKER_PASSWORD="${DOCKER_PASSWORD}"

                            echo "DOCKER_USERNAME 값: \${DOCKER_USERNAME}"
                            echo "DOCKER_PASSWORD 길이: \${#DOCKER_PASSWORD}"

                            echo "1단계: Docker Hub 로그인 중"
                            if echo "\${DOCKER_PASSWORD}" | docker login -u "\${DOCKER_USERNAME}" --password-stdin; then
                                echo "Docker 로그인 성공."
                            else
                                echo "Docker 로그인 실패!" && exit 1
                            fi

                            echo "2단계: 최신 Docker 이미지 가져오는 중 (Docker Pull)"
                            if docker pull ymind14563/kinderpia_back-image:latest; then
                                echo "Docker 이미지 가져오기 성공."
                            else
                                echo "Docker 이미지 가져오기 실패!" && exit 1
                            fi

                            echo "3단계: 8080 포트를 사용하는 Docker 컨테이너가 있는지 확인 중..."
                            container_id=\$(docker ps --filter "publish=8080" -q)

                            if [ -n "\$container_id" ]; then
                                echo "8080 포트를 사용하는 컨테이너 중지 중 (컨테이너 ID: \$container_id)"
                                sleep 2
                                docker ps --filter "publish=8080" -q | xargs -r docker stop
                                echo "컨테이너 중지 완료를 위한 15초 대기 시작"
                                sleep 15
                                echo "컨테이너 중지 완료"
                                sleep 2
                                echo "\$container_id" | xargs -r docker rm -f
                                echo "컨테이너 삭제 완료를 위한 15초 대기 시작"
                                sleep 15
                                echo "컨테이너 삭제 완료"
                                echo "8080 포트를 사용하는 컨테이너 중지 및 제거 완료."
                                sleep 3
                            else
                                echo "8080 포트를 사용하는 컨테이너가 없습니다."
                            fi

                            # 상태가 exited 또는 created인 모든 컨테이너 삭제
                            echo "불필요한 상태의 모든 컨테이너 삭제 중..."
                            exited_containers=\$(docker ps -a -q --filter "status=exited" --filter "status=created")

                            if [ -n "\$exited_containers" ]; then
                                docker rm \$exited_containers
                                echo "불필요한 컨테이너 삭제 완료를 위한 15초 대기 시작"
                                sleep 15
                                echo "상태가 exited 또는 created인 컨테이너 삭제 완료."
                            else
                                echo "삭제할 불필요한 컨테이너가 없습니다."
                            fi

                            echo "4단계: 태그가 없는 이미지 제거 중 (dangling 이미지)"
                            if docker images | grep "<none>" | awk '{print \$3}' | xargs -r docker rmi -f; then
                                sleep 5
                                echo "태그가 없는 이미지가 성공적으로 제거되었습니다."
                            else
                                echo "제거할 태그가 없는 이미지가 없습니다."
                            fi

                            echo "5단계: 새로운 Docker 컨테이너 시작 중"
                            if docker run -d -p 8080:8080 -v /home/ubuntu/application.properties:/config/application.properties ymind14563/kinderpia_back-image:latest; then
                                sleep 3
                                echo "새로운 Docker 컨테이너가 성공적으로 시작되었습니다."
                            else
                                echo "새로운 Docker 컨테이너 시작 실패!" && exit 1
                            fi

                            echo "6단계: Docker Hub 로그아웃 중"
                            if docker logout; then
                                echo "Docker 로그아웃 성공."
                            else
                                echo "Docker 로그아웃 실패!" && exit 1
                            fi

                            echo "=== 배포가 성공적으로 완료되었습니다 ==="
EOF
                        """
                    }
                }
                echo "배포 단계가 완료되었습니다."
            }
        }
    }
}
