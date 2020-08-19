build_client: client/out/
	cd client && clj -m cljs.main -c valk.runtime

build_server: build_client vserver/src/vserver/* vserver/resources/*
	rm -rf vserver/resources
	mkdir -p vserver/resources/public
	cp -r client/out/ vserver/resources/public/out
	cp -r client/static/ vserver/resources/public
	cd vserver && lein uberjar

run_server: build_server
	java -jar vserver/target/vserver.jar 8888
