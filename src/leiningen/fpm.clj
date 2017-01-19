(ns leiningen.fpm
  (:require
    [clojure.string :as string]
    [clojure.java.io :as io]
    [clojure.java.shell :as shell]
    [me.raynes.fs :as fs]
    [leiningen.uberjar :as uberjar]
    [clojure.pprint :as pprint]))

(def default-depends {
  "deb" ["openjdk-7-jre-headless"]
  "rpm" ["java-1.7.0-openjdk"]
  "solaris" ["jdk-7"]
})

(defn- package-path
  "The full path to the output package."
  [project package-type]
  (io/file (:target-path project)
           (str (:name project) "_" (:version project) "." package-type)))

(defn- dependencies
  "The JDK package name and version for the provided package type."
  [project package-type]
  (get (-> project (:fpm {}) (:depends default-depends)) package-type))

(defn options
  "The options to be passed to fpm. Returns a vector of vectors instead of a
  map to support multiple instances of the same option."
  [project package-type]
  (concat
    [["-s" "dir"]
     ["-t" package-type]
     ["--force"]
     ["-a" "all"]
     ["-p" (str (package-path project package-type))]
     ["-n" (:name project)]
     ["-v" (:version project)]
     ["--url" (:url project)]
     ["--description" (:description project)]
     ["--rpm-os" "linux"]]
    (mapv (fn [d] ["-d" d])
         (dependencies project package-type))))

(defn- warnln
  "Prints a message to stderr."
  [message]
  (binding [*out* *err*]
    (println message)))

(defn command-string
  [project package-type]
  (concat ["fpm"]
          (flatten (options project package-type))
          (-> project (:fpm {}) (:files []))))

(defn package
  "Invokes fpm to build the package and returns the resulting path."
  [project package-type]
  (let [command-string (command-string project package-type)
        _ (println (str "Running: "(string/join " " command-string)))
        {:keys [exit out err]} (apply shell/sh command-string)]
    (when-not (empty? out)
      (println (string/trim-newline out)))
    (when-not (empty? err)
      (warnln (string/trim-newline err)))
    (when (pos? exit)
      (warnln "Failed to build package!")
      (System/exit exit))
    (package-path project package-type)))

(defn fpm
  "Generates a minimalist package for the current project."
  ([project] (fpm project "deb"))
  ([project package-type & args]
   (println "Building package")
   (package project package-type)))
