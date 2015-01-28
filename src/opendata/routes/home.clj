(ns opendata.routes.home
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [opendata.views.layout :as layout]
            [csv-map.core :as csv]))

(def exp-over-100-format
  {
   :service-delivery-area "Service Delivery Area(T)"
   :service-delivery-team "Service Delivery Team(T)"
   :cost-c                "CostC"
   :expenditure-group     "ExpenditureGroup(T)"
   :account               "Account(T)"
   :supplier-name         "Supplier Name(T)"
   :transaction-date      "Trans Date"
   :amount                "Amount"
   })

(def testdata {"" "",
               "Amount" "150.00",
               "Trans Date" "12/11/2014",
               "Supplier Name(T)" "Clinical Audit Support CentreLtd",
               "Account(T)" "Staff Training",
               "ExpenditureGroup(T)" "Employees",
               "CostC" "HMAA",
               "Service Delivery Team(T)" "Access & Enablement",
               "Service Delivery Area(T)" "Adult Social Services"})




(defn key-ret [k]
  "Return the correct keyword from the exp-map"
  (k exp-over-100-format))

;; (get testdata (key-ret :amount))

(defn getkey [line k]
  (get line (key-ret k)))

(defn amount-by-key
  "Returns takes a :key and returns a map
  with the string key and the amount eg
  :supplier-name line returns
  {Supplier Name(T) 100.00} "
  [k line]
  {(getkey line k) (if-not (= (get line (key-ret :amount)) "")
                     (read-string (get line (key-ret :amount)))
                     0.00)})

;;(amount-by-key :service-delivery-team testdata)


(defn home []
  (layout/common
   [:h1 "Expenditure over 100 November 2014"]
   (let [lines (csv/parse-csv (slurp "Transparency_Report_November_2014.csv")
                              :key "amount")]
              [:div (merge-with + (map (fn [line]
                           (if-not (= line {"" ""})
                              [:div (str (amount-by-key :supplier-name line))])) lines))

               ])))

(defroutes home-routes
  (GET "/" [] (home)))
