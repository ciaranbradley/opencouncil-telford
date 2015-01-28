(ns opendata.routes.apiv1.transparency-report
  (:require [compojure.core :refer :all]
            [liberator.core
             :refer [defresource resource request-method-in]]
            [cheshire.core :refer [generate-string]]
            [csv-map.core :as csv]))

(def report "Transparency_Report_November_2014.csv")


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


(defn key-ret [k]
  "Return the correct keyword from the exp-map"
  (k exp-over-100-format))


(defn getkey [line k]
  (get line (key-ret k)))

(defn amount-by-key
  "Returns takes a :key and returns a map
  with the string key and the amount eg
  :supplier-name line returns
  {Supplier Name(T) 100.00} "
  [k line]
  {:name (getkey line k) :value (if-not (= (get line (key-ret :amount)) "")
                                  (read-string (get line (key-ret :amount)))
                                  0.00)})

(defn get-trans-rep-amounts
  "Gets the transparency report amounts for key
  "
  [column]
  (let [lines (csv/parse-csv (slurp report)
                             :key "amount")]
    (apply merge-with +
           (for [x (map
                    (fn [line]
                      (if-not (= line {"" ""})
                        (amount-by-key (keyword column)
                                       line)))
                    lines)]
             {(:name x)  (:value x)}))))


(defn describe-service
  "Provides a description of the service endpoint"
  [request]
  {:name "Transparency Report"
   :description "Hacking on the T & W Transparency Report"
   :methods (keys exp-over-100-format)})

(defresource describe-service
  :allowed-methods [:get]
  :handle-ok (fn [request] (generate-string
                            {:name "Transparency Report"
                             :description "The T & W Transparency reports"
                             :amounts-for (keys exp-over-100-format)}))
  :available-media-types ["application/json"])

(defresource get-trans-amounts
  :allowed-methods [:get]
  :handle-ok (fn [{{{column :column} :route-params} :request}]
               (generate-string (get-trans-rep-amounts column)))
  :available-media-types ["application/json"])


(defroutes api-routes
  (GET "/api/v1/transparency-report/" request describe-service)
  (GET "/api/v1/transparency-report/2014/:column" request get-trans-amounts))
