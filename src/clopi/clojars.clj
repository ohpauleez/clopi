(ns clopi.clojars
  (:use [clopi.core :only (gunzip)]))


(def *feed-archive-url* "http://clojars.org/repo/feed.clj.gz")

(defn istream->urls
  "Resolve the feed input stream results lazily, generating a set of all the URLS"
  [results]
  (let [rdr (clojure.java.io/reader results)]
    (reduce (fn [url-set line]
              (let [jar-map (read-string line)]
                (conj url-set (get jar-map :url "")))) #{} (line-seq rdr))))

(defn feed
  "Fetch the Clojars package feed, and process it into an input stream for consumption"
  []
  (gunzip *feed-archive-url*))

