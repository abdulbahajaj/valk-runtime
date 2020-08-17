(ns vserver.core
  (:require [org.httpkit.server :refer [run-server]]
            [vserver.generated :as gen])
  (:gen-class))



(defn -main [& args]
  (let [port (Integer/parseInt (first args))]
    (run-server gen/app {:port port})
    (println (format "Server started on port %s" port))))
