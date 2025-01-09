APP_NAME:=ddd-inventory

.PHONY: build

git-clone-with-submodule-step-by-step:
	@echo "이 프로젝트를 클론하고 Avro 메시지 스키마 서브모듈을 초기화합니다."
	@git clone https://github.com/shkim4u/ddd-inventory.git
	@cd ddd-inventory
	@git submodule init
	@git submodule update

git-clone-with-submodule-recurse:
	@echo "Avro 메시지 스키마 서브모듈을 초기화하면서 이 프로젝트를 클로합니다."
	@git clone --recurse-submodules https://github.com/shkim4u/ddd-inventory.git

git-submodule-update:
	@echo "서브모듈을 최신 상태로 업데이트합니다."
	@git submodule update --remote --recursive

git-submodule-troubleshoot:
	@echo "서브모듈을 디렉토리가 비어있을 경우 초기화하고 업데이트합니다."
	@git submodule init
	@git submodule update

git-submodule-add-avro-schemas:
	@echo "Avro 메시지 스키마 리포지터리를 서브모듈로 추가합니다."
	@echo "이는 Avro 메시지 스키마를 서브모듈로 추가하지 않은 프로젝트에서 Avro 메시지를 사용할 때 필요합니다."
	@@git submodule add https://github.com/shkim4u/ddd-avro-schemas.git message-idl/avro
	@git config -f .gitmodules submodule.message-idl/avro.branch main

install-kind-linux:
	@curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.25.0/kind-linux-amd64
	@chmod +x ./kind
	@sudo mv ./kind /usr/local/bin/kind

create-cluster:
	@kind create cluster --name ddd-foundation --config k8s/cluster/kind-cluster.yaml

delete-cluster:
	@kind delete cluster --name ddd-foundation

run-local-database:
	@docker run --rm -d --name postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=inventory -p 5432:5432 public.ecr.aws/docker/library/postgres:17.2

delete-local-database:
	@docker stop postgres

install-tools:
	@echo "Installing helm..."
	@brew install helm
	@echo "Installing yq..."
	@brew install yq

helm-add-repos:
	@helm repo add bitnami https://charts.bitnami.com/bitnami
	@helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
	@helm repo update

install-database-k8s:
	@rm -rf postgresql || true
	@echo "Fetching the PostgreSQL Helm chart..."
	@helm fetch bitnami/postgresql --untar
	@echo "Modifying the postgresql/values.yaml file to customize database and credentials..."
	@yq e '.auth.postgresPassword = "postgres"' -i postgresql/values.yaml
	@yq e '.auth.database = "inventory"' -i postgresql/values.yaml
	@echo "Installing the PostgreSQL Helm chart..."
	@helm install postgresql postgresql --namespace postgresql --create-namespace --wait
	@kubectl get pods -n postgresql

uninstall-database-k8s:
	@helm uninstall postgresql -n postgresql
	@kubectl delete ns postgresql

helm-fetch-postgresql:
	@helm fetch bitnami/postgresql --untar
	@echo "PostgreSQL Helm chart is fetched"
	@echo "You can edit the postgresql/values.yaml file to customize the PostgreSQL deployment."
	@echo "Typically, you may want to change the 'auth.postgresPassword' and 'auth.database' values, for example, 'auth.postgresPassword: postgres' and 'auth.database: inventory' respectively."

helm-install-fetched-postgresql:
	@helm install postgresql postgresql --namespace postgresql --create-namespace --wait
	@kubectl get pods -n postgresql

run-local-rabbitmq:
	@docker run --rm -d --name rabbitmq -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:management

install-rabbitmq-k8s:
	@rm -rf rabbitmq || true
	@echo "Fetching the RabbitMQ Helm chart..."
	@helm fetch bitnami/rabbitmq --untar
	@echo "Modifying the rabbitmq/values.yaml file to customize RabbitMQ credentials..."
	@yq e '.auth.username = "user"' -i rabbitmq/values.yaml
	@yq e '.auth.password = "rabbitmq"' -i rabbitmq/values.yaml
	@echo "Installing the RabbitMQ Helm chart..."
	@helm install rabbitmq rabbitmq --namespace rabbitmq --create-namespace --wait
	@kubectl get pods -n rabbitmq
	@echo "RabbitMQ is installed successfully, now proceed to configure the exchange by running 'make port-forward-rabbitmq-management-ui', make 'download-rabbitmqadmin', and make 'make 'configure-rabbitmq-exchange-queue'."

uninstall-rabbitmq-k8s:
	@helm uninstall rabbitmq -n rabbitmq
	@kubectl delete ns rabbitmq

helm-install-rabbitmq:
	@helm install rabbitmq bitnami/rabbitmq --namespace rabbitmq --create-namespace --wait
	@kubectl get pods -n rabbitmq

helm-fetch-rabbitmq:
	@helm fetch bitnami/rabbitmq --untar
	@echo "RabbitMQ Helm chart is fetched"
	@echo "You can edit the rabbitmq/values.yaml file to customize the RabbitMQ deployment."

helm-install-fetched-rabbitmq:
	@helm install rabbitmq rabbitmq --namespace rabbitmq --create-namespace --wait
	@kubectl get pods -n rabbitmq

show-rabbitmq-credentials:
	@echo "Username: user"
	@echo "Password: $(shell kubectl get secret --namespace rabbitmq rabbitmq -o jsonpath="{.data.rabbitmq-password}" | base64 --decode)"
	@echo "Configure port-forward, and then access to RabbitMQ Management UI URL at: http://localhost:15672"

port-forward-rabbitmq-management-ui:
	@echo "RabbitMQ Management UI is available at: http://localhost:15672"
	@kubectl port-forward --namespace rabbitmq svc/rabbitmq 15672:15672

port-forward-rabbitmq-amqp:
	@echo "RabbitMQ AMQP port is available at: amqp://localhost:5672"
	@kubectl port-forward --namespace rabbitmq svc/rabbitmq 5672:5672

download-rabbitmqadmin:
	@mkdir -p rabbitmq || true
	@curl -Lo rabbitmq/rabbitmqadmin http://localhost:15672/cli/rabbitmqadmin
	@chmod +x rabbitmq/rabbitmqadmin

configure-rabbitmq-exchange-queue:
	@echo "Creating the exchange with name 'order.events'..."
	@rabbitmq/rabbitmqadmin -u user -p rabbitmq declare exchange name=order.events type=topic durable=true
	@echo "Creating the queue with name 'order.queue'..."
	@rabbitmq/rabbitmqadmin -u user -p rabbitmq declare queue name=order.queue durable=true
	@echo "Binding the queue to the exchange with the routing key '#'..."
	@rabbitmq/rabbitmqadmin -u user -p rabbitmq declare binding source=order.events destination=order.queue routing_key=#

build:
	@echo "Building the application..."
	@./gradlew clean build --no-daemon

build-container-image:
	@echo "Building the Docker container image..."
	@docker build -t ddd-inventory:latest .

load-container-image:
	@echo "Loading the Inventory container image onto the Kind Kubernetes cluster..."
	@kind load docker-image ddd-inventory:latest --name ddd-foundation

deploy-application:
	@echo "Deploying the Inventory application..."
	@kubectl apply -f k8s/application/namespace.yaml
	@kubectl apply -f k8s/application/deployment.yaml
	@kubectl apply -f k8s/application/service.yaml

undeploy-application:
	@kubectl delete -f k8s/application/service.yaml
	@kubectl delete -f k8s/application/deployment.yaml
	@kubectl delete -f k8s/application/namespace.yaml
