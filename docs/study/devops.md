#  ECS / EKS / Lambda

| 항목      | ECS <br>(Elastic Container Service)      | EKS <br>(Elastic Kubernetes Service )         | Lambda          |
|--------------|-----------------------|----------------------------|--------------------------------|
| **방향성**  | - AWS 네이티브 환경에서 컨테이너를 간단하게 운영 <br>- 오케스트레이션 복잡성 최소화 | - 표준 Kubernetes 환경을 완전 관리형으로 제공 <br>- 오픈소스 표준 + AWS 안정성/확장성 | - 완전 서버리스, 인프라 관리 제거 <br>-  이벤트 기반 단위 작업 특화 |
| **원리**  | - Task 정의 기반 컨테이너 스케줄링 <br>- EC2 인스턴스(직접/ASG) 또는 Fargate(서버리스)에서 실행 | - AWS가 컨트롤플레인 완전 관리 <br>- 워커노드는 EC2 또는 Fargate로 실행, K8s 표준 API | - 이벤트 트리거 시 컨테이너로 프로비저닝 후 함수 실행 <br>- 실행 후 리소스 자동 반환 |
| **장점**   | - AWS 서비스와의 쉬운 연동 <br>- Fargate로 완전 서버리스 구현 | - 쿠버네티스 생태계 및 오픈소스 도구 활용 <br>- 멀티클라우드/온프레미스 호환 <br>- 다양한 확장 옵션 | - 완전 서버리스, 인프라 전혀 신경 X <br>- 빠른 확장 <br>- 사용한 만큼 과금 <br>- 이벤트/비동기/예약 작업에 최적       |
| **단점**     | - 쿠버네티스에 비해 기능이 제한적 <br>- AWS 종속 <br>- 복잡한 워크로드에 한계 | - 운영복잡도 증가 <br>- 클러스터 관리비용 추가 발생  | - 실행시간 제한 (15분) <br>- 메모리 제한 (10GB) <br>- 지속 커넥션/상태 유지 불가 <br>- 콜드 스타트 이슈 |
| **trade-off** | 단순성, 운영편의성 높음 / 유연성 낮음       | 유연성, 확장성 높음 / 복잡도 높음     | 완전한 서버리스 가능 / 실행환경, 지속성 제약 |
| **적합** | - 중간 규모 마이크로서비스 아키텍처 <br>- 빠른 배포/확장 <br>- 서버리스 컨테이너  | - 대규모 마이크로서비스 아키텍처 <br>- 멀티 클라우드 전략이 필요한 경우 | - 이벤트 기반 처리 <br>- 간헐적으로 실행되는 작업 <br>- 예약된 크론 작업 |
| **부적합** | - 쿠버네티스 생태계 활용 필수 환경 <br>- 멀티 클라우드가 필수 환경   | - 소규모 어플리케이션 <br>- 쿠버네티스 경험 없는 조직 | - 15분 이상 실행되는 작업 <br>- 지속적 통신 연결이 필요한 경우 <br>- 상태를 유지해야 하는 어플리케이션 |
| **확장성** | ASG/Fargate 오토스케일링  | 다양한 확장 옵션 존재 (HPA, VPA, Cluster Autoscaler ..) | 요청에 따라 자동으로 확장 <br>- 동시실행제한 (디폴트 1000개) |
| **모니터링** | - CloudWatch/X-Ray 기본  | - Prometheus, Grafana등의 오픈소스 <br>- CloudWatch 가능 | - CloudWatch Logs가 자동 통합 <br>- X-Ray 트레이싱이 내장 |
| **배포전략** | - 롤링 업데이트가 기본 <br>-  Blue/Green은 CodeDeploy 연동 필요  | - 롤링, Blue/Green, 카나리 등 모든 전략이 네이티브로 지원 |버전/별칭/Weighted Alias, CodeDeploy |

- 컨트롤플레인: 클러스터 및 서비스 상태, 배치, 오케스트레이션을 중앙에서 관리하는 핵심 관리 컴포넌트
- 프로비저닝: 사용자가 요청한 리소스(서버, 컨테이너, 함수 등)를 자동 또는 수동으로 생성/할당/준비하는 과정
- 콜드스타트: 서버리스 함수/컨테이너 서비스가 웜업 없이 배포되어 첫 응답이 지연되는 현상
- 하이브리드 배포: 메인 서비스는 ECS/EKS 사용, 이벤트 처리/배치작업은 Lambda 사용
- 배포전략
  - 롤링: 기존 Task를 점진적으로 새 버전으로 교체
  - blue/green: 기존환경과 신규환경을 교체
  - 카나리: 트래픽 일부만 신규환경에 먼저 보내고 이상 없으면 전체 전환

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
- SSL 종단점 (HTTPS 종료)
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

<br>

# CI / CD


<br>

# 다이어 그램

