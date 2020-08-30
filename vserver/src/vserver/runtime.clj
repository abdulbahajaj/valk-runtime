(ns vserver.runtime
  (:require [ring.middleware.json :refer [wrap-json-response]]
            [clojurewerkz.quartzite.scheduler :as scheduler]
            [clojurewerkz.quartzite.triggers :as triggers]
            [clojurewerkz.quartzite.jobs :as jobs]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.cron :refer [schedule cron-schedule]]))

(def server-funcs (atom {}))
(def on-start-hooks (atom ()))
(defn add-hook [hook-list new-fn]
  (swap! hook-list #(cons new-fn %)))
(defn run-hooks [hook-list]
  (doall (map #(%) @hook-list) ))
(defn run-on-start-hooks []
  (run-hooks on-start-hooks))


(defmacro defs [name args & rest]
  `(swap! server-funcs
          assoc
          (format "/server-functions/%s" ~(str name))
          (fn [request#]
            {:status 200
             :body
             {:response
              (let [body# (:body request#)]
                (apply (fn [~@args] (do ~@rest))
                       (map #(% body#) ~(map #(keyword %) args))))}})))

(defn get-server-function [path]
  (get @server-funcs path))

(defn call-server-function [req]
  (let [sf (-> req :uri get-server-function)]
    (if sf ((-> sf wrap-json-response) req) nil)))


(defmacro startd [name interval & rest]
  (let [random-num (rand-int 1000)
        name (symbol (str "daemon-job-" name))]
    `(do (defjob ~name [ctx] (~@rest))
         (add-hook on-start-hooks
                   (fn []
                     (let [schedule# (-> (scheduler/initialize) scheduler/start)
                           job# (jobs/build
                                 (jobs/of-type ~name)
                                 (jobs/with-identity (jobs/key ~(str "jobs." random-num))))
                           trigger# (triggers/build
                                     (triggers/with-identity (triggers/key ~(str "triggers." random-num)))
                                     (triggers/start-now)
                                     (triggers/with-schedule (schedule
                                                              (cron-schedule ~interval))))]
                       (scheduler/schedule schedule# job# trigger#)))))))
