(ns opendata.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]))

(defn common [& body]
  (html5
    [:head
     [:title "Groovy"]
     (include-js "//code.jquery.com/jquery-1.10.2.js")
     (include-js "//cdnjs.cloudflare.com/ajax/libs/d3/3.4.11/d3.min.js")
     (include-css "/css/screen.css")
     (include-js "/js/app.js")]
    [:body body]))
