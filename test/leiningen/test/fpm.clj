(ns leiningen.test.fpm
  (:require [leiningen.fpm :as fpm]
            [me.raynes.fs :as fs]
            [clojure.java.io :as io]
            [clojure.test :refer :all])
  (:use [leiningen.fpm]))

(deftest test-command-string
  (testing "options and files merged"
    (is (= (command-string {:fpm {:files ["a=b" "c=d"]}} "deb")
           '("fpm"
              "-s" "dir" "-t" "deb" "--force" "-a" "all" "-p" "_.deb" "-n" nil "-v" nil "--url" nil "--description" nil "--rpm-os" "linux"
              "-d" "openjdk-7-jre-headless"
              "a=b" "c=d")))))

(def foo-project
  {:name "test"
   :target-path "/"
   :description "abc"
   :version "1"
   :fpm {:depends {"deb" ["a" "b" "c"]}}})

(deftest test-options
  (testing "project name"
    (is (= (options foo-project "deb")
           '(["-s" "dir"] ["-t" "deb"] ["--force"] ["-a" "all"] ["-p" "/test_1.deb"] ["-n" "test"] ["-v" "1"]
              ["--url" nil] ["--description" "abc"] ["--rpm-os" "linux"]
              ["-d" "a"] ["-d" "b"] ["-d" "c"])))))