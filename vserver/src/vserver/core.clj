(ns vserver.core
  (:use [ring.middleware.resource]
        [ring.middleware.content-type]
        [ring.middleware.not-modified])
  (:require [org.httpkit.server :refer [run-server]]
            [vserver.generated :as gen])
  (:gen-class))

(def app
  (-> {}
      (wrap-resource "public")
      (wrap-content-type)
      (wrap-not-modified)))

(defn -main [& args]
  (let [port (Integer/parseInt (first args))]
    (run-server app {:port port})
    (println (format "Server started on port %s" port))))
