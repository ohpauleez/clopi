(ns clopi.core
  (:import (java.util.zip GZIPInputStream)
           (java.io StringReader)))

(def *feed-archive-url* "http://clojars.org/repo/feed.clj.gz")

(defn gunzip
  "Unzip a gzip archive into an input stream"
  [archive]
  (GZIPInputStream. (clojure.java.io/input-stream archive)))

(defn istream->urls
  "Resolve the feed input stream lazily, generating a set of all the URLS"
  [istream]
  (let [rdr (clojure.java.io/reader istream)]
    (reduce (fn [url-set line]
              (let [jar-map (read-string line)]
                (conj url-set (get jar-map :url "")))) #{} (line-seq rdr))))

(defn project->map
  "Process a project.clj, generating a map of the data"
  [project-str]
  (if (< 1 (count project-str))
    (let [project-map-str (-> project-str
                            (.replace "(" "") 
                            (.replace ")" "") 
                            (.replaceFirst "defproject" "{") 
                            (str "}"))]
      (read-string project-map-str))
    {}))

(defn fetch-url
  "Fetch the dependencies and dev-dependencies of a project.clj url"
  [url]
  (let [rdr (try
              (clojure.java.io/reader url)
              (catch Exception e (clojure.java.io/reader (StringReader. ""))))
        proj-map (try
                   (project->map (apply str (line-seq rdr)))
                   (catch Exception e {}))]
    (select-keys proj-map [:dependencies :dev-dependencies])))

(defn fetch-github
  "Fetch the dependencies and dev-dependencies of a github host project"
  [url]
  (try
    (fetch-url (.replaceFirst (str url "/raw/master/project.clj") "http:" "https:"))
    (catch Exception e {})))
 
(defn count-dep-vec
  [dep-vector stats-map-start]
  (reduce (fn [stats-map [proj-index version-str]]
            (let [proj-index (str proj-index)]
              (update-in stats-map [proj-index version-str] (fnil inc 0))))
          stats-map-start dep-vector))

(defn count-deps
  "Create a map of all artifacts, and how many times someone has a dependency on a specific version"
  [deps]
  (reduce (fn [stats-map dep-map]
            (try
              (count-dep-vec (into (get dep-map :dependencies [])
                                   (get dep-map :dev-dependencies [])) stats-map)
              (catch Exception e (do (println dep-map) stats-map))))
          (sorted-map) deps))
 
#_(defn process-gzip
  "This needs to be done, but will be done via the project files."
  [istream]
  (let [rdr (clojure.java.io/reader istream)]
    (loop [stat-map (transient {})
           line (.readLine rdr)]
      (if line
        (let [jar-map (read-string line)
              url (jar-map :url)]
          (condp .contains url
            "github" (fetch-github url)
            "bitbucket" (fetch-bitbucket url)
            ""))))))

