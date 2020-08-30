(ns vserver.core
  (:use [ring.middleware.resource]
        [ring.middleware.content-type]
        [ring.middleware.not-modified])
  (:require [org.httpkit.server :refer [run-server]]
            [vserver.runtime :as runtime]
            [vserver.generated :as gen])
  (:gen-class))

(def static-resources
  (-> {}
      (wrap-resource "public")
      (wrap-content-type)
      (wrap-not-modified)))

(defn app-handler [req]
  (or (runtime/call-server-function req) (static-resources req)))

(defn -main [& args]
  (let [port (Integer/parseInt (first args))]
    (run-server app-handler {:port port})
    (runtime/run-on-start-hooks)
    (println (format "Server started on port %s" port))))
