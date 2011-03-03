(ns clopi.github
  (:require [clj-github.repos :as github]))


;; this needs to be way smarter about finding the end page, and about when to rate-limit/sleep
(defn clojure-repos
  ""
  []
  (let [auth {:user "OhPauleez" :token "aef3279d32742b55277fe55c4ceb2220"}]
    (reduce (fn [res-vec page]
              (into res-vec (do (Thread/sleep 1000) (github/search-repos auth "clojure" :language "Clojure" :start-page page)))) [] (range 1 50))))

(defn results->urls
  "Take a search results vector, and return all the urls"
  [results]
  (into #{} (map #(get %1 :url "") results)))


