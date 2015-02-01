(ns opendata.test.api
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [opendata.handler :refer :all]
            [opendata.routes.apiv1.transparency-report
             :refer [get-report-amounts
                     reports
                     exp-over-100-format]]))




(facts "test the api responds correctly"
       (fact "Test GET actual resource responds"
        (let [response (app (mock/request :get "/api/v1/transparency-report/2014/january/supplier-name"))]
          (:status response) => 200))

        (fact "Test incorrect resource responds with a 404"
          (let [response (app (mock/request :get "/api/v1/transparency-report/2013/january/supplier-name"))]
            (:status response) => 404)))

