(ns opendata.routes.home
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [opendata.views.layout :as layout]
            [csv-map.core :as csv]
            [hiccup.page :refer [include-js]]
            [hiccup.element :refer [link-to]]))

;;
;; Emit the homepage
;;
(defn home []
  (layout/common
   [:h1 "Expenditure over 100 " [:span#title-replace]]
   (include-js "/js/app.js")
   (link-to "/description" "What am I?")))

(defn description []
  (layout/common
   [:h1 "Description"]
   [:p "A simple graph application"]))

(defroutes home-routes
  (GET "/" [] (home))
  (GET "/description" [] (description)))
