(ns opendata.routes.apiv1.transparency-report
  (:require [compojure.core :refer :all]
            [liberator.core
             :refer [defresource resource request-method-in]]
            [cheshire.core :refer [generate-string]]
            [csv-map.core :as csv]))

(def report "Transparency_Report_January_2014.csv")

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


(defn select-report
  "Takes a selection string of format 2014/january and returns the report file string from
  the reports map"
  [year month]
  (get (get reports (keyword year)) (keyword month)))


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
  "Gets the transparency report amounts for column key"
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

(defn get-all-payments
  "Returns a list of all payments to one column value"
  [])


(defn get-report-amounts
  "Gets the transparency report amounts for column key"
  [year month column]

  (if-let [report (select-report year month)]

    (if (key-ret (keyword column))

      (let [lines (csv/parse-csv (slurp report) :key "amount")]
        (apply merge-with +
               (for [x (map
                        (fn [line]
                          (if-not (= line {"" ""})
                            (amount-by-key (keyword column) line)))
                      lines)]
               {(:name x)  (:value x)})))
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

(defresource get-trans-amounts
  :allowed-methods [:get]
  :handle-ok (fn [{{{column :column} :route-params} :request}]
               (generate-string (get-trans-rep-amounts column)))
  :available-media-types ["application/json"])

(defresource get-report
  :allowed-methods [:get]
  :handle-ok (fn [{{{year :year month :month column :column} :route-params} :request}]
               (generate-string (get-report-amounts year month column)))
  :available-media-types ["application/json"])


(defroutes api-routes
  (GET "/api/v1/transparency-report/" request describe-service)
  (GET "/api/v1/transparency-report/2014/:column" request get-trans-amounts)
  (GET "/api/v1/transparency-report/:year/:month/:column" request get-report))
