(ns valk.runtimejs
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.core.async :refer [<!]]
   [cljs-http.client :as http]))

(defn send-request [url data callback]
  (go (let [response (<! (http/post url data))]
        (callback (:response response)))))
