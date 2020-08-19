SERVER_COMPILATION_PATH = "vserver/target"
JAR_PATH = "vserver/target/vserver.jar"
SERVER_RESOURCES_PATH = "vserver/resources"
CLIENT_COMPILATION_PATH = "client/out"
CLIENT_STATIC_PATH = "client/static"

build_client: client/src/valk/*
	cd client && clj -m cljs.main -c valk.runtime

build_server: build_client vserver/src/vserver/* vserver/resources/*
	rm -rf ${SERVER_RESOURCES_PATH}
	mkdir -p ${SERVER_RESOURCES_PATH}/public
	cp -r ${CLIENT_COMPILATION_PATH} ${SERVER_RESOURCES_PATH}/public/out
	cp -r ${CLIENT_STATIC_PATH} ${SERVER_RESOURCES_PATH}/public
	cd vserver && lein uberjar

.PHONY: clean
clean:
	rm -rf ${CLIENT_COMPILATION_PATH} ${SERVER_COMPILATION_PATH} ${SERVER_RESOURCES_PATH}/*

.PHONY: keep_jar_only
keep_jar_only:
	mv ${JAR_PATH} .
	rm -rf vserver client Makefile

run_server: build_server
	java -jar ${JAR_PATH} 8888
