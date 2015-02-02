(ns opendata.routes.apiv1.transparency-report
  (:require [clojure.edn :as edn]
            [compojure.core :refer :all]
            [liberator.core
             :refer [defresource resource request-method-in]]
            [cheshire.core :refer [generate-string]]
            [csv-map.core :as csv]))

;;
;; A map of the available reports
;;
(def reports {:2014 {:january    "Transparency_Report_January_2014.csv"
                     :february   "Transparency_Report_February_2014.csv"
                     :march      "Transparency_Report_March_2014.csv"
                     :april      "Transparency_Report_April_2014.csv"
                     :may        "Transparency_Report_May_2014.csv"
                     :june       "Transparency_Report_June_2014.csv"
                     :july       "Transparency_Report_July_2014.csv"
                     :august     "Transparency_Report_August_2014.csv"
                     :september  "Transparency_Report_September_2014.csv"
                     :october    "Transparency_Report_October_2014.csv"
                     :november   "Transparency_Report_November_2014.csv"
                     :december nil}})


;;
;; These column headers from the Transparency report format area
;; mapped to some keys.
;;
(def exp-over-100-format {
   :service-delivery-area "Service Delivery Area(T)"
   :service-delivery-team "Service Delivery Team(T)"
   :cost-c                "CostC"
   :expenditure-group     "ExpenditureGroup(T)"
   :account               "Account(T)"
   :supplier-name         "Supplier Name(T)"
   :transaction-date      "Trans Date"
   :amount                "Amount"
   })


;;
;; Helper function to round the values from the data
;; Taken from stack overflow
;; http://stackoverflow.com/questions/10751638/clojure-rounding-to-decimal-places
;;
(defn round
  [precision d]
  (let [factor (Math/pow 10 precision)]
    (/ (Math/round (* d factor)) factor)))
;;
;; Accessor functions to the reports and formats
;;
(defn select-report
  "Takes a year and month string and returns the report file string from
  the reports map"
  [year month]
  (get (get reports (keyword year)) (keyword month)))


(defn key-ret
  "Returns the column name from the format map"
  [k]
  (k exp-over-100-format))

(defn getkey
  "takes a line of data in map {} and returns the value at key
  using the key return from the format map"
  [line k]
  (get line (key-ret k)))


;;
;; Functions to parse the data to be consumed by the front end
;;
(defn amount-by-key
  "Takes a :key and a line of data, returns a map
  with the value in the relevent column and the amount
  :supplier-name line returns
  {Supplier Name(T) 100.00} "
  [k line]
  {:name (getkey line k) :value (if-not (= (get line (key-ret :amount)) "")
                                  (edn/read-string (get line (key-ret :amount)))
                                  0.00)})


(defn get-all-payments
  "Takes a CSV report and a keyword for the column map
  and returns a vector of maps [{}{}{}] of all payments to one column"
  [report key-column]
  (let [lines (csv/parse-csv (slurp report)
                             :key "amount")]
    (map (fn [line]
           (amount-by-key key-column line))
         lines)))


;;
;; Rounding function for the payments
;;
(defn round-payments
  [paymentsmap]
  (into {} (for [[k v] paymentsmap]
             [k (round 2 v)])))


(defn get-payment-totals
  "Takes a vector of maps in the form
   [{:name \"some string\" :value xx.xx }
   {:name \"Another string\" :value xxx.xx}]
   and aggregates all values to each  string type
   returns a condensed map of the aggregates
  {\"some string\" xx.xx, \"Another string\" xxx.xx}"
  [report key-column]
  (round-payments (apply merge-with +
                         (for [x (get-all-payments report key-column)]
                           {(:name x) (:value x)}))))


;;
;; May be possible to iterate over the payments with a query
;; for that column
;;
(defn query-payments
  "Takes a map of condensed payments and a string as a key. Searches the
  map for that key"
  [payments key]
  (get payments key))


;;
;; The Entry Point for the API
;;
(defn get-report-amounts
  "Gets the transparency report amounts for column key"
  [year month column]
  (if-let [report (select-report year month)]
    (if (key-ret (keyword column))
      (get-payment-totals report (keyword column))
      (str "No such column"))
    (str "No report found")))



;;
;; Might be nice
;;
(defn describe-service
  "Provides a description of the service endpoint"
  [request]
  {:name "Transparency Report"
   :description "Hacking on the T & W Transparency Report"
   :methods (keys exp-over-100-format)})

;;
;;
;; Resource points
;;
(defresource describe-service
  :allowed-methods [:get]
  :handle-ok (fn [request] (generate-string
                            {:name "Transparency Report"
                             :description "The T & W Transparency reports"
                             :amounts-for (keys exp-over-100-format)}))
  :available-media-types ["application/json"])

(defresource get-report
  :allowed-methods [:get]
  :handle-ok (fn [{{{year :year month :month column :column} :route-params} :request}]
               (generate-string (get-report-amounts year month column)))
  :available-media-types ["application/json"])


(defroutes api-routes
  (GET "/api/v1/transparency-report/" request describe-service)
  (GET "/api/v1/transparency-report/:year/:month/:column" request get-report))
