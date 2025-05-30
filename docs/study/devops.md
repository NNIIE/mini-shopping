#  ECS / EKS / Lambda

| 항목      | ECS <br>(Elastic Container Service)      | EKS <br>(Elastic Kubernetes Service )         | Lambda          |
|--------------|-----------------------|----------------------------|--------------------------------|
| **방향성**  | - AWS 네이티브 환경에서 컨테이너를 가장 간단하게 운영할 수 있도록 하는 것이 목표 <br>- 컨테이너 오케스트레이션의 복잡성을 AWS 방식으로 단순화 | - 표준 Kubernetes API를 그대로 제공하면서 AWS의 안정성과 확장성을 결합하는 것이 목표 <br>- 오픈소스 표준을 클라우드 관리형 서비스로 제공 | - 서버 관리를 완전히 제거하고 순수한 비즈니스 로직에만 집중할 수 있게 하는 것이 목표 <br>- 이벤트가 발생할 때만 코드를 실행 |
| **원리**  | - ECS Task 정의를 기반으로 컨테이너를 스케줄링 / 관리 <br>- EC2 인스턴스의 ECS Agent나 Fargate가 실제 컨테이너를 실행 | - 표준 Kubernetes 컨트롤 플레인(API Server, etcd, Scheduler 등)을 AWS가 관리 <br>- 워커 노드에서 Pod를 실행 | - 이벤트 소스가 트리거하면 AWS가 컨테이너를 즉시 프로비저닝하고 함수 코드를 실행한 후 리소스를 회수 |
| **장점**   | - AWS 서비스들의 쉬운 활용 <br>- Fargate로 서버리스 컨테이너 실행 | - Kubernetes 생태계의 모든 도구와 확장 기능을 활용 <br>- 멀티 클라우드 전략이 가능하여 벤더 종속성이 낮음 | - 완전한 서버리스로 인프라 관리가 전혀 필요없음 <br>- 자동확장이 밀리초 단위로 빠름 <br>- 사용한 만큼만 비용 지불       |
| **단점**     | - Kubernetes에 비해 기능이 제한적 <br>- AWS 종속 | - 운영복잡도 증가 <br>- 클러스터 관리비용 추가 발생  | - 실행시간 제한 (15분) <br>- 메모리 제한 (10GB) |
| **trade-off** | 단순성 높음 / 유연성 낮음       | 강력한 기능과 유연성 높음 / 복잡도 높음     | 완전한 서버리스 가능 / 실행환경 제약 |
| **적합** | - 중간 규모 마이크로서비스 아키텍처 <br>- Fargate로 서버리스 컨테이너를 원하는 경우   | - 대규모 마이크로서비스 아키텍처 <br>- 멀티 클라우드 전략이 필요한 경우 | - 이벤트 기반 처리 <br>- 간헐적으로 실행되는 작업 <br>- 예약된 크론 작업 |
| **부적합** | - Kubernetes 특화 기능이 필요한 경우 <br>- 멀티 클라우드가 필수인 환경   | - 작은규모의 어플리케이션 <br>- 쿠버네티스 경험 없는 팀 | - 15분 이상 실행되는 작업 <br>- 지속적 통신 연결이 필요한 경우 <br>- 상태를 유지해야 하는 어플리케이션 |
| **확장성** | Auto Scaling Group / Fargate를 통해 자동 확장 가능  | 다양한 확장 옵션 존재 (HPA, VPA, Cluster Autoscaler ..) | 요청에 따라 자동으로 확장되고 동시 실행 수는 기본 1000개까지 가능 |
| **모니터링** | - CloudWatch 기본 통합 <br>- X-Ray 연동  | - Prometheus, Grafana등의 오픈소스 <br>- CloudWatch 가능 | - CloudWatch Logs가 자동 통합 <br>- X-Ray 트레이싱이 내장 |
| **배포전략** | - 롤링 업데이트가 기본 <br>-  Blue/Green은 CodeDeploy 연동 필요  | - 롤링, Blue/Green, 카나리 등 모든 전략이 네이티브로 지원 | 버전 관리와 별칭을 통한 트래픽 분할이 가능 |

하이브리드: 메인 서비스는 ECS/EKS 사용, 이벤트 처리/배치작업은 Lambda 사용

<br>

# User Flow - ECS
1. **DNS (Route 53)**
2. **CDN (S3, CloudFront) - 선택사항**
3. **로드밸런서 (ALB)**
4. **컨테이너 오케스트레이션 (ECS)**
5. **Storage (RDS, ElastiCache ...)**
6. **응답 반환**

## Route53
- AWS DNS 관리 서비스
- 도메인 주소를 ALB/ELB 또는 CDN에 연결
- 브라우저가 DNS 서버에 질의 → Route 53이 ALB/CloudFront의 IP 리턴

## CloudFront
- 정적 콘텐츠: 캐싱되어 바로 반환
- 동적 콘텐츠: ALB로 전달

## ALB
트래픽 분산, 라우팅, SSL 종료
- HTTP/HTTPS 요청을 받아 ECS Task들에게 분산
- 경로 기반 라우팅
- SSL 종단점 (HTTPS 종료)
- ALB → Target Group → ECS Task
- ALB / NLB
	- ALB: HTTP/HTTPS 기반, 경로/호스트 기반 라우팅, 웹에 최적
	- NLB: TCP/UDP 기반, 초고속, WebSocket 등 특수목적

## ECS
AWS의 컨테이너 오케스트레이션 서비스
### ECS Launch Type
- Fargate: 서버리스, 인프라 관리 X, 비용 조금 더 높음, 개발 편의성 좋음, 빠른 배포
- EC2: 직접 인스턴스 관리, 세밀한 튜닝, 비용 효율적일 수 있음, 유지보수 부담
### ECS Service    
하나의 백엔드 마이크로서비스(예: ProductService) 관리
- 원하는 Task 수를 유지
- 오토스케일링 설정 가능
### ECS Task
실제로 실행되는 컨테이너 인스턴스(예: product-api)
- 보통 1 Task - 1 컨테이너 패턴
- RDS, ElastiCache 등 다른 AWS 리소스와 통신
	- 보통 VPC 내에서 Private Subnet을 사용
### ALB Target Group
각 ECS Task들이 타겟 그룹의 멤버로 등록
- ALB에서 헬스체크, 트래픽 분배
### Task Definition

<br>

# 통신
### SSL
### HTTPS

<br>

# AWS
### IAM
- AWS 리소스에 누가, 무엇을, 어떻게 접근할지에 대한 권한을 관리
### VPC
AWS의 독립적인 네트워크 공간
- 다른 서비스와 논리적으로 격리
### Subnet
VPC 내에서 리소스를 가용 영역 단위로 분리하기 위해 도입
- public
	- 인터넷에서 직접 접근 가능(로드밸런서, Bastion Host 등)
    - 라우트 테이블에 엔트리 포함
    - 직접적인 인터넷 접근 가능
    - 퍼블릭 IP 또는 EIP를 할당하면 외부와 양방향 통신 가능
- private
	- 인터넷에서 직접 접근 불가(ECS Task, DB 등 내부 서비스)
    - 라우트 테이블에 게이트웨이 경로 X
    - 외부로부터 직접 접근 차단
    - 외부연결이 필요하면 NAT Gateway 경유
### IGW (Internet Gateway)
- VPC 리소스에 퍼블릭 인터넷을 연결하기 위해 도입된 완전 관리형 컴포넌트
### NAT Gateway
Private Subnet의 ECS Task가 인터넷(예: DockerHub, S3 등)에 접근하려면 필요
- 퍼블릭 서브넷에 생성하며 EIP(Elastic IP)를 할당해야 함
### 보안그룹
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


# CI / CD


<br>

# 다이어 그램

