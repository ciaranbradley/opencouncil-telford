(ns opendata.test.handler
  (:require [opendata.routes.home :as home])
  (:use clojure.test
        ring.mock.request
        opendata.handler))

(def testdata {"" "",
               "Amount" "150.00",
               "Trans Date" "12/11/2014",
               "Supplier Name(T)" "Clinical Audit Support CentreLtd",
               "Account(T)" "Staff Training",
               "ExpenditureGroup(T)" "Employees",
               "CostC" "HMAA",
               "Service Delivery Team(T)" "Access & Enablement",
               "Service Delivery Area(T)" "Adult Social Services"})


(deftest test-amount-by-key
  (testing "amount-by-key returns correct data"
    (is (= (home/amount-by-key :supplier-name testdata)
           {"Clinical Audit Support CentreLtd" "150.00"}))))

(deftest test-app

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
