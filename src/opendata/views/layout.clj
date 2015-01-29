(ns opendata.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [link-to]]))

(defn menu []
  [:div.menu
   (link-to (str "#/service-delivery-area") "Delivery Area ")
   (link-to (str "#/service-delivery-team") "Delivery Team ")
   (link-to (str "#/cost-c") "Cost C ")
   (link-to (str "#/expenditure-group") "Expenditure Group ")
   (link-to (str "#/account") "Account ")
   (link-to (str "#/supplier-name") "Supplier Name ")
   (link-to (str "#/transaction-date") "Transaction Date ")])

(defn common [& body]
  (html5
    [:head
     [:title "Groovy"]
     (include-js "//code.jquery.com/jquery-1.10.2.js")
     (include-js "//cdnjs.cloudflare.com/ajax/libs/d3/3.4.11/d3.min.js")
     (include-css "/css/screen.css")
     (include-js "/js/app.js")]
    [:body
      (menu)
     body]))
