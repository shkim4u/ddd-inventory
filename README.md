# ***설치 순서***

---

## 1. **소스 코드 다운로드**

```bash
````

---

## 2. **쿠버네테스 클러스터 생성 (Kind)**

```bash
make create-cluster
```

---

## 3. **데이터베이스 배포 (PostgreSQL)**

```bash
make install-database-k8s
```

---

## 4. **RabbitMQ 배포 및 설처**

### 4.1. **RabbitMQ 배포**

```bash
make install-rabbitmq-k8s
```

### 4.2. **RabbitMQ 관리 UI 포트 포워딩**

```bash
make port-forward-rabbitmq-management-ui
```

### 4.3. **RabbitMQ Exchange 및 Queue 생성**

> 📌(참고)📌<br>
> 위에서 Port Forwarding을 수행한 터미널이 아닌 새로운 터미널을 열어서 아래 명령어를 수행합니다.

```bash
make download-rabbitmqadmin
make configure-rabbitmq-exchange-queue
```

### 4.4. **RabbitMQ 관리 UI 포트 포워딩 종료**

---

## 5. **애플리케이션 빌드 및 배포**

