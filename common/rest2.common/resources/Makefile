pwd = $(shell pwd)

generate:
	docker run --rm \
		-v $(CURDIR):/local \
		-v $(CURDIR)/openapi-generator-cli.jar:/opt/openapi-generator/modules/openapi-generator-cli/target/openapi-generator-cli.jar \
		openapitools/openapi-generator-cli:latest generate \
			-i /local/odysseus.yaml \
			-g java-msf4j \
			-o /local/java-msf4j-gen/ \
			-c /local/generator-config.json

copy:
	cp java-msf4j-gen/src-gen/de/uniol/inf/is/odysseus/rest2/server/api/*Api.java ../../../server/rest2.server/src-gen/de/uniol/inf/is/odysseus/rest2/server/api/
	cp java-msf4j-gen/src-gen/de/uniol/inf/is/odysseus/rest2/server/api/*ApiService.java ../../../server/rest2.server/src-gen/de/uniol/inf/is/odysseus/rest2/server/api/
	cp java-msf4j-gen/src-gen/de/uniol/inf/is/odysseus/rest2/common/model/*.java ../../../common/rest2.common/src/de/uniol/inf/is/odysseus/rest2/common/model/
