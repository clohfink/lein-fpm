# lein-fpm

A Leiningen plugin for building simple packages using
[fpm](https://github.com/jordansissel/fpm).

Generated packages install will be copy the files from the target to destination defined in `files` of `fpm` in `project.clj`.


    (defproject foo "0.1.0-SNAPSHOT"
      ...

      :plugins [[lein-fpm "0.2.4"]]
      :uberjar-name "foo.jar"

      :fpm {:depends {"deb" ["openjdk-7-jre-headless" "something" "jsvc"]
                      "rpm" ["java-1.7.0-openjdk" "something" "jsvc"]}

            :files ["target/foo.jar=/usr/lib/foo/foo.jar"
                    "bin/start=/etc/init.d/foo"
                    "config.edn=/etc/foo/config.edn"]}
       
       ...

Then after `lein uberjar` run `lein fpm deb`.

[![Clojars Project](http://clojars.org/lein-fpm/latest-version.svg)](http://clojars.org/lein-fpm)

## Usage

### System-wide install

Put `[lein-fpm "0.2.4"]` into the `:plugins` vector of your `:user` profile.

### Per-project install

Put `[lein-fpm "0.2.4"]` into the `:plugins` vector of your project.clj.

### Building a package

lein-fpm will produce a deb by default:

    $ lein fpm

or you can supply a specific target type:

    $ lein fpm deb

This will produce a package in the `target` directory.

#### Included dependencies

By default, deb packages will depend on `openjdk-7-jre-headless`, rpm packages
will depend on `java-1.7.0-openjdk`, and solaris packages will depend on
`jdk-7`. If you want to override this you can provide your own dependencies by defining them in `project.clj`:

    :fpm {
      :depends {
        "deb" ["jsvc" "openjdk-7-jre-headless"]
        "rpm" ["jsvc" "java-1.7.0-openjdk"]}}

## Dependencies

This plugin depends on [fpm](https://github.com/jordansissel/fpm), and
[rpmbuild](http://www.rpm.org/max-rpm-snapshot/rpmbuild.8.html) if you are
creating rpms.

## License

Copyright © 2015 Brian Schroeder

Distributed under the MIT License.
