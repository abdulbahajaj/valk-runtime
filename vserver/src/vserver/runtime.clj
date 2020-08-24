(ns vserver.runtime)

(def server-funcs (atom {}))

(defmacro defs [name args & rest]
    `(swap! server-funcs
            assoc
            (format "/server-functions/%s" ~(str name))
            (fn [request#]
              {:status 200
               :body
               (let [body# (:body request#)]
                 (apply (fn [~@args] (do ~@rest))
                        (map #(% body#) ~(map #(keyword %) args))))})))

(defn get-server-function [path]
  (get @server-funcs path))

(defn call-server-function [req]
  (let [sf (-> req :uri get-server-function)]
    (if sf (sf req) nil)))
