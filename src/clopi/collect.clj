(ns clopi.collect
  (:require [clopi.core :as ccore]
            [clopi.clojars :as clojars]
            [clopi.github :as github]
            ;[clopi.bitbucket :as bitbucket]
            ))

(defn all-urls []
  (let [clourls (clojars/istream->urls (clojars/feed))
        giturls (github/repos->urls (github/clojure-repos))
        ret-urls (into clourls giturls)]
    ret-urls))

(defn depmap
  ([] (depmap (all-urls)))
  ([urls]
   (let [deps (map ccore/dispatch-url urls)
         ret-depmap (ccore/count-deps deps)])))

