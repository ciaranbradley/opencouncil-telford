(ns opendata.routes.home
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [opendata.views.layout :as layout]
            [csv-map.core :as csv]))

;;
;; Emit the homepage
;;
(defn home []
  (layout/common
   [:h1 "Expenditure over 100" [:span.title-replace]]))

(defroutes home-routes
     (GET "/" [] (home)))
