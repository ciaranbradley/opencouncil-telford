(ns opendata.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]
            [hiccup.element :refer [link-to]]))

(defn menu []
  [:div#navigation
   [:div.year-select
    [:h4 "Select Year"]
    [:ul.nav.nav-sidebar
     [:li (link-to {:class "year-name"} (str "#/2013") "2013")]
     [:li {:class "active"} (link-to {:class "year-name"} (str "#/2014") "2014")]]]
   [:div.month-select
    [:h4 "Select Month"]
    [:ul.nav.nav-sidebar
     [:li (link-to {:class "month-name"} (str "#/january") "January")]
     [:li (link-to {:class "month-name"} (str "#/february") "February")]
     [:li (link-to {:class "month-name"} (str "#/march") "March")]
     [:li (link-to {:class "month-name"} (str "#/april") "April")]
     [:li (link-to {:class "month-name"} (str "#/may") "May")]
     [:li (link-to {:class "month-name"} (str "#/june") "June")]
     [:li (link-to {:class "month-name"} (str "#/july") "July")]
     [:li (link-to {:class "month-name"} (str "#/august") "August")]
     [:li (link-to {:class "month-name"} (str "#/september") "September")]
     [:li (link-to {:class "month-name"} (str "#/october") "October")]
     [:li {:class "active"} (link-to {:class "month-name"} (str "#/november") "November")]
     [:li (link-to {:class "month-name"} (str "#december") "December")]]]
   [:div.column-name
    [:h4 "Select Grouping"]
    [:ul.nav.nav-sidebar
     [:li (link-to {:class "column-name"} (str "#/service-delivery-area") "Delivery Area ")]
     [:li (link-to {:class "column-name"} (str "#/service-delivery-team") "Delivery Team ")]
     [:li (link-to {:class "column-name"} (str "#/cost-c") "Cost C ")]
     [:li (link-to {:class "column-name"} (str "#/expenditure-group") "Expenditure Group ")]
     [:li (link-to {:class "column-name"} (str "#/account") "Account ")]
     [:li {:class "active"} (link-to {:class "column-name"} (str "#/supplier-name") "Supplier Name ")]
     [:li (link-to {:class "column-name"} (str "#/transaction-date") "Transaction Date ")]]]])
   
   ;;[:div.payment-type
    ;;[:h4 "Payment Type"]
    ;;[:ul.nav.nav-sidebar
     ;;[:li {:class "active"} (link-to {:id "payment-debit" :class "payment-type"} (str "#/debit") "Debits")]
     ;;  [:li (link-to {:id "payment-credit" :class "payment-type"} (str "#/credit") "Credits")]]]


(defn main-menu []
  [:div#navigation
   [:ul.nav.nav-sidebar
    [:li (link-to "/" "Home (Graphs)")]]])

(defn common [menu & body]
  (html5 {:lang "en-gb"}
   [:head
    [:meta {:charset "UTF-8"}]
    [:title "Telford & Wrekin Expenditure Reports"]
    (include-js "//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js")
    (include-css "//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css")
    (include-css "//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css")
    (include-js "//code.jquery.com/ui/1.11.2/jquery-ui.js")
    (include-js "//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js")
    (include-js "//cdnjs.cloudflare.com/ajax/libs/d3/3.4.11/d3.min.js")
    (include-css "/css/screen.css")]
    [:body
     [:nav.navbar.navbar-inverse.navbar-fixed-top
      [:div.container-fluid
       [:div.navbar-header
        [:button.navbar-toggle.collapsed {:type "button"
                                          :data-toggle "collapse"
                                          :data-target "#navbar"
                                          :aria-expanded "false"
                                          :aria-controls "navbar"}
         [:span.sr-only "Toggle Navigation"]
         [:span.icon-bar]
         [:span.icon-bar]
         [:span.icon-bar]]

        [:a.navbar-brand {:href "/"} "T&W Expenditure"]]
       [:div#navbar.navbar-collapse.collapse
        [:ul.nav.navbar-nav.navbar-right
         [:li (link-to "/description" "What is this?")]]]]]
     [:div.container-fluid
      [:div.row
       [:div.col-sm-3.col-md-2.sidebar
        menu]
       [:div.col-sm-9.col-md-10.main
        body]]]]))

(defn graph-page [& body]
  (common (menu) body))

(defn content-page [& body]
  (common (main-menu) body))
