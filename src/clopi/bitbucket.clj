(ns clopi.bitbucket
  (:require [clj-bitbucket [repositories :as bb-repos]
                           [ src :as bb-src]]))

(defn clojure-repos
  ""
  ((bb-repos/search-repos "clojure") :repositories))

(defn repos->urls
  ""
  [results]
  (into #{} (map #(get %1 :resource_uri "") results)))

(defn fetch-bitbucket
  ""
  [url]
  (bb-src (str (.replaceFirst url "/1.0/repositories/" "") "/raw/tip/project.clj")))

