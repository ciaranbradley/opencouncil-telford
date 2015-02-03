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
  (layout/graph-page
   [:div#graph]
   (include-js "/js/app.js")))

(defn description []
  (layout/content-page
   [:div.content-page 
    [:h1 "Infrequently Asked Qestions"]
    [:h3 "What am I looking at?"]
    [:p (str "Bubble graphs, fed by the Expenditure open data supplied by Telford & Wrekin. You can view the data sources on the ") (link-to "https://www.telford.gov.uk/info/20110/budgets_and_spending/55/expenditure_over_100" "Telford &amp; Wrekin website") "." ]
    [:h3 "Why would you do such a thing?"]
    [:p "Although the data was open, reading it made me cry. I thought that this might be better."]
    [:h3 "What alchemy have you used?"]
    [:p "The system is built in " (link-to "http://clojure.org" "Clojure") " mainly with the " (link-to "https://github.com/weavejester/compojure" "Compojure") " and " (link-to "http://clojure-liberator.github.io/liberator/" "Liberator") " libraries. The front end uses " (link-to "http://getbootstrap.com/" "Bootstrap") " for layout and " (link-to "http://d3js.org/" "d3")" for the funky bubble generation"]
    [:h3 "Can I have the code for this?"]
    [:p "Yes, the code is available on " (link-to "https://github.com/ciaranbradley/opencouncil-telford" "my github account")". It's very rough. But feel free to do as you wish with it."]
    [:h3 "Can you make one for {insert my council}"]
    [:p "Maybe. I dunno. Talk to me on Twitter " (link-to "https://twitter.com/ciaranbradley" "@ciaranbradley")"."]]))

(defroutes home-routes
  (GET "/" [] (home))
  (GET "/description" [] (description)))
