(ns opendata.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [opendata.routes.home :refer [home-routes]]
            [opendata.routes.apiv1.transparency-report :refer [api-routes]]))

(defn init []
  (println "opendata is starting"))

(defn destroy []
  (println "opendata is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes home-routes api-routes app-routes)
      (handler/site)
      (wrap-base-url)))
