(ns vserver.generated)

(defn app [req]
  (println req)
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Valk server runtime"})
