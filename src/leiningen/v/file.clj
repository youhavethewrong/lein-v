(ns leiningen.v.file
  (:use [clojure.string :only [join]]))

(defn- cache-source
  "Return Clojure source code for the version cache file"
  [version name]
  (join "\n" [";; This code was automatically generated by 'lein v cache'"
              (format ";; for the project %s" name)
              "(ns version)"
              (format "(def version \"%s\")" version)
              ""]))

(defn- cache-path
  "Return path to version cache file"
  [project]
  (str (or (first (:sources-path project)) "src") "/version.clj"))

(defn version
  "Peek into the source of the project to read the cached version"
  []
  (try
    (require 'version)
    (eval 'version/version)
    (catch Exception e nil)))

(defn cache
  "Write the version of the given Leiningen project to a file-backed cache"
  [project]
  (let [path (cache-path project)]
    (spit path (cache-source (:version project) (:name project)))))