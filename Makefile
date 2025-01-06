APP_NAME:=ddd-inventory

.PHONY: build

run-local-database:
	@@docker run --rm -d --name postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=inventory -p 5432:5432 public.ecr.aws/docker/library/postgres:17.2

git-submodule-add-avro-schemas:
	@@git submodule add https://github.com/shkim4u/ddd-avro-schemas.git message-idl/avro
