(ns clopi.collect
  (:require [clopi.core :as ccore]
            [clopi.clojars :as clojars]
            [clopi.github :as github]
            ;[clopi.bitbucket :as bitbucket]
            ))

(defn all-urls []
  (let [clourls (clojars/istream->urls (clojars/feed))
        giturls (github/repos->urls (github/clojure-repos))]
    (into clourls giturls)))

(defn depmap
  ([] (depmap (all-urls)))
  ([urls]
   (let [deps (map #(do
                      (Thread/sleep 1000)
                      ((fnil ccore/dispatch-url "") %)) urls)]
     (ccore/count-deps deps))))

