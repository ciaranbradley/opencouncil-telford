(ns opendata.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [link-to]]))

(defn menu []
  [:div#description (link-to "/description" "What Am I?")]
  [:div#navigation
   [:div.year-select
    (link-to {:class "year-name"} (str "#/2014") "2014")]
   [:div.month-select
    (link-to {:class "month-name"} (str "#/january") "January")
    (link-to {:class "month-name"} (str "#/february") "February")
    (link-to {:class "month-name"} (str "#/march") "March")
    (link-to {:class "month-name"} (str "#/april") "April")
    (link-to {:class "month-name"} (str "#/may") "May")
    (link-to {:class "month-name"} (str "#/june") "June")
    (link-to {:class "month-name"} (str "#/july") "July")
    (link-to {:class "month-name"} (str "#/august") "August")
    (link-to {:class "month-name"} (str "#/september") "September")
    (link-to {:class "month-name"} (str "#/october") "October")
    (link-to {:class "month-name"} (str "#/november") "November")
    ;;(link-to (str "#december") "December")
    ]
   [:div.column-name
   (link-to {:class "column-name"} (str "#/service-delivery-area") "Delivery Area ")
   (link-to {:class "column-name"} (str "#/service-delivery-team") "Delivery Team ")
   (link-to {:class "column-name"} (str "#/cost-c") "Cost C ")
   (link-to {:class "column-name"} (str "#/expenditure-group") "Expenditure Group ")
   (link-to {:class "column-name"} (str "#/account") "Account ")
   (link-to {:class "column-name"} (str "#/supplier-name") "Supplier Name ")
   (link-to {:class "column-name"} (str "#/transaction-date") "Transaction Date ")]])

(defn common [& body]
  (html5
    [:head
     [:title "Telford & Wrekin Expenditure"]
     (include-js "//code.jquery.com/jquery-1.10.2.js")
     (include-js "//cdnjs.cloudflare.com/ajax/libs/d3/3.4.11/d3.min.js")
     (include-css "/css/screen.css")
     (include-js "/js/app.js")]
    [:body
      (menu)
     body]))
