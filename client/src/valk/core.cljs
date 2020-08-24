(ns valk.core
  (:require
   [valk.generated :as generated]
   [reagent.core :as r]
   [reagent.dom :as r.dom]))

(defn mount []
  (r.dom/render [generated/app] (js/document.getElementById "root")))

(defn ^:after-load re-render [] (mount))
(defonce start-up (do (mount) true))
