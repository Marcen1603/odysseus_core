pwd = $(shell pwd)

generate:
	docker run --rm -v $(CURDIR):/local openapitools/openapi-generator-cli:latest generate -i /local/odysseus.yaml -g java-msf4j -o /local/java-msf4j-gen/ -c /local/generator-config.json
