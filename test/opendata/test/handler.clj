(ns opendata.test.handler
  (:require [opendata.routes.home :as home]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [opendata.handler :refer :all]
            [opendata.routes.apiv1.transparency-report
             :refer [reports
                     exp-over-100-format
                     get-report-amounts]]
  (:use midje.sweet))

(def testdata {"" "",
               "Amount" "150.00",
               "Trans Date" "12/11/2014",
               "Supplier Name(T)" "Clinical Audit Support CentreLtd",
               "Account(T)" "Staff Training",
               "ExpenditureGroup(T)" "Employees",
               "CostC" "HMAA",
               "Service Delivery Team(T)" "Access & Enablement",
               "Service Delivery Area(T)" "Adult Social Services"})




(facts "GET /api/v1/trasparency-report/2014/january/supplier-name"
  (fact "Test GET"
        (let [response (app (mock/request :get "/api/v1/transparency-report/2014/january/supplier-name"))]
          (:status response) => 200)))


(deftest test-amount-by-key
  (testing "amount-by-key returns correct data"
    (is (= (home/amount-by-key :supplier-name testdata)
           {"Clinical Audit Support CentreLtd" "150.00"}))))

(deftest test-app

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404))))

  (testing "api route exists"
    (let [response (app (request :get "/api/v1/trasparency-report/2014/january/supplier-name"))]
      (is (= (:status response) 200)))))
