IMAGE_NAME = docker.pkg.github.com/lukeshay/jeffery-krueger/jeffery-krueger
TAG = $(shell git rev-parse HEAD)
CMD ?= check

build:
	docker build --build_arg SHA=$(TAG) -f Dockerfile -t $(IMAGE_NAME):$(TAG) .
.PHONY: build

push:
	docker push $(IMAGE_NAME):$(TAG)
.PHONY: push

login:
	echo -n "$(GITHUB_TOKEN)" | docker login https://docker.pkg.github.com -u Actions --password-stdin
.PHONY: login

run:
	docker run --rm \
		-v "$(PWD)":/usr/src/myapp \
		-w /usr/src/myapp \
		openjdk:11-jdk \
		./gradlew --no-daemon $(CMD)
.PHONY: run

version:
	find . -maxdepth 1 -type f -exec sed -i '' 's/\$(OLD)/$(NEW)/g' {} \;
.PHONY: version