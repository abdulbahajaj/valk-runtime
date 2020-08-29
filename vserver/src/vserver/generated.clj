(ns vserver.generated
  (:require [vserver.runtime :refer [defs]]
            [clojurewerkz.quartzite.scheduler :as scheduler]
            [clojurewerkz.quartzite.triggers :as triggers]
            [clojurewerkz.quartzite.jobs :as jobs]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.cron :refer [schedule cron-schedule]]))

(defmacro startd [name interval & rest]
  (let [n (rand-int 1000)]
    `(do 
      (defjob ~name [ctx] (~@rest))
      (let [s# (-> (scheduler/initialize) scheduler/start)
            job# (jobs/build
                (jobs/of-type ~name)
                (jobs/with-identity (jobs/key (str "jobs." ~n))))
            trigger# (triggers/build
            (triggers/with-identity (triggers/key (str "triggers." ~n)))
            (triggers/start-now)
            (triggers/with-schedule (schedule
                (cron-schedule ~interval))))]
        (scheduler/schedule s# job# trigger#))
    )
  )
)
;; Generated code below
