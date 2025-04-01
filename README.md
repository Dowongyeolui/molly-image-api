### 150만 장의 이미지 서버
---

## 🔖 프로젝트 개요
- 부하 테스트 및 크롤링 이미지 저장 등 **대량의 저장/조회 요청**이 발생할 것을 고려해,  
  클라우드 파일 스토리지 대신 **직접 이미지 서버를 구축**하여 비용을 절감하고자 함.

<br>

## 💻 서버 스펙
- Raspberry Pi 5  
- Debian 12  
- 2.4GHz / RAM 8GB / SSD 512GB

<br>

## 📚 기술 스택
- Spring Boot, Java, Nginx

<br>

## 🌏 서버 아키텍처
<img width="665" alt="image" src="https://github.com/user-attachments/assets/c3cffbba-c7c1-4718-8ca8-c6e1337e6afa" />

<br>

## 🌈 개선 사항

### 1️⃣ 이미지 조회 성능 최적화 – **응답 속도 81.4% 개선**

#### 🔍 문제 상황
- 기존 이미지 서버(Nginx)는 **원본 이미지를 그대로 제공**하고 있었음  
- 이미지 크기가 크고 포맷 최적화가 이루어지지 않아, **응답 속도 저하 및 과도한 트래픽 발생**  
- 다양한 디바이스 환경에 **최적화된 이미지 제공이 어려움**

---

### ✅ 개선 내용

#### 1. `.webp` 포맷으로 이미지 변환  
- 기존 원본 이미지 대신 `.webp` 포맷으로 변환하여 전송  
- 프론트엔드 팀과 협의해 화질은 약 80% 수준으로 유지하면서, **압축률을 극대화**  
- 다운로드 속도 향상 및 네트워크 트래픽 절감 효과 달성

#### 2. 이미지 리사이징 처리  
- 원본 이미지를 디바이스 환경에 맞게 **서버(Spring)** 에서 리사이징하여 전송  
- 필요 이상으로 큰 이미지 제공을 방지하고, 사용자 환경에 맞는 최적화된 이미지 제공

#### 3. Nginx 캐싱 적용  
- 자주 요청되는 이미지에 대해 **Nginx 캐싱을 설정**  
- 서버 부하를 줄이고 응답 속도를 향상시킴


### ✅ 결과
<img width="665" alt="image" src="https://github.com/user-attachments/assets/bfb5e179-950c-4fc3-9cc2-5a9be97839e1" />

