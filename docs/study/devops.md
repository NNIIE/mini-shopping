# ECS / EKS / Beanstalk

| 항목      | ECS <br>(Elastic Container Service)      | EKS <br>(Elastic Kubernetes Service )         | AWS Elastic Beanstalk          |
|--------------|-----------------------|----------------------------|--------------------------------|
| **방향성**  | - 오케스트레이션 복잡성 최소화 | - Kubernetes 100% 활용 <br>- 컨트롤 플레인은 AWS가 관리 <br>- Pod/CRD 등 세밀한 제어와 오픈소스 생태계 연동 | - 코드만으로 인프라, 배포, 운영 자동화 |
| **원리**  | - Task Definition: 컨테이너 실행에 대한 정의 <br>- Cluster: EC2/Fargate 리소스의 논리적 그룹 <br>- Service: 원하는 수의 Task 유지, Task 복제, 롤링 배포 | - 표준 K8s Control Plane은 AWS가 완전 관리 <br>- EC2/Fargate에 Pod 배치 | - 코드 업로드 시 EC2/ALB/AutoScaling 등 인프라 자동 구성 <br>- 배포, 롤백, 헬스체크 등 추상화 <br>- 환경변수, 설정 자동 관리 |
| **장점**   | - AWS 리소스와 통합이 가장 뛰어남(AWS Native) <br>- EKS 대비 러닝커브 낮음 | - 쿠버네티스 오픈 표준 완전 준수 <br>- 온프레미스/타 클라우드와 높은 호환성 | - 코드 업로드만으로 배포/롤백/오토스케일링/모니터링 등 모든 인프라 자동화 |
| **단점**   | - Kubernetes 대비 오픈소스 생태계, 확장성, 클라우드/온프레미스 이식성 부족 | - Control Plane 사용료 별도 <br>- AWS 사용시 완전한 native 아님 | - AWS 인프라 내부 설정에 직접 접근/세밀한 튜닝 한계 |
| **모니터링** | - CloudWatch/X-Ray 기본  | - Prometheus, Grafana등의 오픈소스 <br>- CloudWatch 가능 | - CloudWatch, Enhanced Health Reporting, X-Ray, 로그 아카이브 지원 |
| **배포전략** | - 롤링 업데이트가 기본 <br>-  Blue/Green은 CodeDeploy 연동 필요  | - 롤링, Blue/Green, 카나리 등 모든 전략이 네이티브로 지원 | - 롤링, 블루/그린 등 다양하게 지원 |
| **비용** | - 컨트롤플레인 무료 <br>- ec2/Fargate 과금 | - 컨트롤 플레인 과금 <br>- 워커노드(컨테이너가 실행되는 인스턴스) 과금 | - 생성된 AWS 리소스만 과금 |
| **trade-off** | 단순함 vs 유연성 | 유연성 vs 복잡도 | 편의성 vs 제어권 |

- 컨트롤플레인: 클러스터 및 서비스 상태, 배치, 오케스트레이션을 중앙에서 관리하는 핵심 관리 컴포넌트
- 프로비저닝: 사용자가 요청한 리소스(서버, 컨테이너, 함수 등)를 자동 또는 수동으로 생성/할당/준비하는 과정
- 콜드스타트: 서버리스 함수/컨테이너 서비스가 웜업 없이 배포되어 첫 응답이 지연되는 현상
- 하이브리드 배포: 메인 서비스는 ECS/EKS 사용, 이벤트 처리/배치작업은 Lambda 사용
- 배포전략
  - 롤링: 기존 Task를 점진적으로 새 버전으로 교체
  - blue/green: 기존환경과 신규환경을 교체
  - 카나리: 트래픽 일부만 신규환경에 먼저 보내고 이상 없으면 전체 전환

<br>


# CI / CD

| 항목      | AWS CodePipeline + CodeBuild      | GitHub Actions         | Jenkins          |
|--------------|-----------------------|----------------------------|--------------------------------|
| **방향성**  | - 모든 구성요소가 AWS 서비스로 동작  | - GitHub PR/Push기반 자동화 | - 높은 확장성 및 커스터마이징 <br>- 마스터/에이전트 아키텍처 <br>- 플러그인 중심 생태계 <br>- 온프레미스/클라우드 가리지 않음 |
| **원리**  | - CodeBuild가 빌드 및 도커이미지 생성 <br>- ECR에 Push <br>- ECS 서비스 업데이트 | - 트리거 매커니즘 <br>- Actions Runner가 빌드/테스트/배포까지 수행 <br>- yml 파일 기반 | - 마스터: 작업 스케줄링, UI제공 <br>- 에이전트: 실제 빌드 실행 <br>- 도커/ECR/ECS 연동은 플러그인 또는 쉘스크립트 활용 |
| **장점**   | - AWS 생태계 최적화 <br>- IAM 연동  | - 깃허브 완벽 연동 <br>- 자체 Runner 커스텀 빌드 환경 제공 <br>- Public 저장소 무료 | - 성숙한 커뮤니티 <br>- 무한한 확장성 <br>- 네트워크 격리 가능 <br>- 온프레미스 완벽 지원 |
| **단점**     | - 로컬테스트 불가능 <br>- AWS 종속 <br>- 커스터마이징, 빌드환경 제한적 | - Private 저장소 비용 <br>- AWS통합 수동 <br>- IAM설정, 네트워킹 직접 구성 | - 서버관리 필수 <br>- 단일 마스터 병목 <br>- 플러그인 메모리 누수 <br>- 운영부담 (업데이트, 보안, 백업 등) |
| **trade-off** | AWS 최적화 vs 확장성/커스텀 | GitHub 최적화 vs AWS 최적화 | 높은 커스터마이징 vs 관리 편의성 |
| **비용** <br>(프리티어) | - CodePipeline: 월 1개 파이프라인 무료 <br>- CodeBuild: 월 빌드시간 100분 무료 | - Public: 완전 무료 <br>- Private: 월 2000분 무료 | 오픈소스 (무료) |
| **비용** <br>(프리티어 초과) | - V1: 파이프라인당 월 1$ <br>- V2: 분당 0.002$ <br>- CodeBuild: 타입별 분당 과금 small(0.005$), large(0.02$), 2xlarge(0.08$) | - Free: 월 2000분 무료 후 분당 0.008$ <br>- Pro: 월 4$ 3000분 이후 분당 0.008$ <br>- Team: 사용자당 월 4$ 3000분 이후 분당 0.008$ <br>- Enterpirse: 월 50000분 이후 분당 0.008$ | - 오픈소스 <br>- 인프라 비용 발생 |
| **롤백** | - 이전 Task Definition 버전 기반 수동 롤백 <br>- 블루/그린 배포시 실패 자동롤백 지원 <br>- 파이프라인 스테이지단위 롤백은 직접 추가 | - Git 버전관리 기반 롤백 <br>- yaml로 롤백/백업/재배포 등을 코드로 관리 | - 빌드 번호 기반 관리 <br>- 운영상 모든 시나리오 직접 스크립트작성으로 제어 가능 <br>- 모든 로직을 직접 설계해야 함 |

##### 월간 청구 비용
- 소규모 (2000분 이하)
  - AWS > Github
- 중규모 
  - AWS >= Github
- 대규모 
  - GitHub > AWS


<br>


# 다이어 그램
![ecs drawio (1)](https://github.com/user-attachments/assets/2ac35b7b-f167-446a-aae4-1f8bde4c4694)


<br>


# User Flow - ECS
1. **DNS (Route 53)**
2. **CDN (S3, CloudFront) - 선택사항**
3. **로드밸런서 (ALB)**
4. **컨테이너 오케스트레이션 (ECS)**
5. **Storage (RDS, ElastiCache ...)**
6. **응답 반환**

## Route53
AWS DNS 완전관리형 서비스
- 도메인 주소를 ALB/ELB 또는 CDN에 연결
- 브라우저가 DNS 서버에 질의 → Route 53이 ALB/CloudFront의 IP 리턴
- 헬스체크, 트래픽 라우팅, 가용성 기반 분산 등

## CloudFront
AWS 글로벌 분산형 CDN
- 사용자 콘텐츠 요청시 가까운 위치에서 빠르게 응답
- 정적 콘텐츠: 캐싱되어 바로 반환
- 동적 콘텐츠: ALB로 전달

## ALB
AWS L7 기반 로드밸런서
- 브라우저 -> ALB -> Target Group (ECS Task, EC2, Lambda) -> ECS Task 내부 컨테이너 (Spring)
- 서브넷 분리
  - Public: ALB
  - Private: ECS Task
- ECS에서 ALB 사용 시 동적 포트 매핑 필수
- SSL/TSL 종료
  - ALB에서 SSL 종료 후 ECS Task에는 HTTP로 전달
- 헬스 체크
- ALB / NLB
  - ALB (L7): HTTP/HTTPS 기반, 경로/호스트 기반 라우팅, 웹에 최적
  - NLB (L4): TCP/UDP 기반, 초고속, WebSocket 등 특수목적

## ECS
AWS 완전관리형 컨테이너 오케스트레이션 서비스
<br>
개발/운영 환경 차이 극복: 내 컴퓨터에선 잘돌아가는데.. 방지
- ALB -> Target Group -> ECS Cluster -> ECS Service -> ECS Task

#### Target Group
각 ECS Task들이 타겟 그룹의 멤버로 등록
- ALB에서 헬스체크, 트래픽 분배

#### Cluster
컨테이너들이 모여있는 논리적 집합

#### Container
어플리케이션, 라이브리러, 런타임 환경을 하나로 패키징 해서 어디서든 실행 가능한 단위

#### Launch Type (컨테이너 실행 방식)
- EC2 Launch Type
  - 직접 EC2 인스턴스를 클러스터로 묶고 거기에 컨테이너(Task) 올림
  - 인스턴스 수동 관리 
- Fargate Launch Type
  - 서버리스 컨테이너
  - 인스턴스 신경 X, Task 정의만으로 바로 실행

#### ECS Service    
하나의 백엔드 마이크로서비스(예: ProductService) 관리
- 원하는 Task 수를 유지
- 오토스케일링 설정 가능

#### ECS Task
Task Definition 대로 실제 실행되는 컨테이너 인스턴스
- 보통 1 Task - 1 컨테이너 패턴
- RDS, ElastiCache 등 다른 AWS 리소스와 통신
- 보통 VPC 내에서 Private Subnet을 사용

#### Task Definition
컨테이너를 어떻게 실행할지 정의하는 JSON 템플릿
- 실행에 필요한 모든 설정 (도커 이미지, 환경변수, 포트 등)을 명시


<br>


# AWS
### IAM
AWS 리소스에 누가, 무엇을, 어떻게 접근할지에 대한 권한을 관리

### VPC
AWS의 독립적인 네트워크 공간
- 네트워크 기본단위
- 서브넷/라우팅/보안 정책의 최상위 개념
- 다른 서비스와 논리적으로 격리

### Subnet
VPC 내 IP 주소 대역을 더 작은 단위로 쪼갠 논리적 네트워크 구간
<br>
VPC 내에서 서브넷을 여러개로 쪼개면 트래픽 분산, 보안강화, 네트워크 관리가 쉬워짐
- public
  - 인터넷에서 직접 접근 가능(로드밸런서, Bastion Host 등)
  - IGW와 연결되고 외부 인터넷과 통신 가능
- private
  - 외부에서 직접 접근 불가(ECS Task, DB 등 내부 서비스)
  - 외부연결이 필요하면 NAT Gateway 경유
    - NAT Gateway 등을 통해 제한적으로 외부와 통신
   
### ECR (Elastic Container Registry)
컨테이너 이미지를 저장, 관리, 배포 하는 저장소
- S3를 기반으로 이미지 저장
- IAM 기반 인증/권한 부여

### IGW (Internet Gateway)
VPC와 인터넷 사이의 통신을 가능하게 하는 컴포넌트
- VPC와 인터넷 간의 라우팅 담당
- VPC 내부 인스턴스가 인터넷과 직접 통신할 수 있도록 하는 논리적 네트워크 장치
- 양방향 통신 가능
- EC2 생성 시 자동 퍼블릭 IP 할당 옵션 또는 수동 EIP 할당

### NAT Gateway
Private Subnet의 인스턴스가 인터넷에는 나갈 수 있지만 외부에서 직접 접근당하지 않게 하는 서비스
- 내부 EC2등에서 소프트웨어 업데이트, S3 접근 등 외부 통신필요
- 퍼블릭 서브넷에 생성하며 EIP(Elastic IP)를 할당해야 함
- Private Subnet 내부 리소스는 NAT Gateway를 통해서만 외부로 나감

### 보안그룹
VPC 내 리소스에 대한 가상 방화벽 역할을 하는 논리적 정책 집합
- ALB용 보안그룹
    - Inbound: 80/443 (HTTP/HTTPS) 포트 오픈 (0.0.0.0/0 또는 제한된 IP 대역)
    - Outbound: 0.0.0.0/0 (제한 X)
- ECS Task용 보안그룹
    - Inbound: ALB의 보안그룹만 허용 (예: `source = ALB-SG, port = 8080`)
    - Outbound: RDS, ElastiCache 등 필요한 리소스 대역만 허용
- DB용 보안그룹
    - Inbound: ECS Task의 보안그룹만 허용 (최소 권한 원칙)
    - Outbound: 필요시 제한


<br>


# 보안
### Handshake 과정
1. 클라이언트가 서버(https://) 접속
2. 서버가 공개키(인증서) 전달
3. 클라이언트가 인증서 신뢰성(발급자, 도메인, 만료일 등) 검증
4. 클라이언트가 Premaster Secret을 암호화해서 서버로 전달(공개키 이용)
5. 서버가 비밀키로 복호화 -> 양측이 같은 세션키 생성
6. 이후 통신은 대칭키 암호화
### SSL (Secure Sockets Layer)
네트워크 통신 암호화 프로토콜
- 과거 표준 (TLS 전신)
### TSL (Transport Layer Security)
SSL을 계승한 최신 암호화 프로토콜
- HTTPS의 실제 구현
### HTTPS (HyperText Transfer Protocol Secure)
HTTP + TLS/SSL
- TLS/SSL 위에서 동작하는 프로토콜
- ACM에서 무료 인증서 발급하고 ALB에 붙이면 HTTPS 자동 지원



