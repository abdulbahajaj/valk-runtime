(defproject vserver "0.1.0-SNAPSHOT"
  :description "Valkyrie server runtime"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring "1.6.3"]
                 [http-kit "2.4.0"]]
  :repl-options {:init-ns vserver.core}
  :uberjar-name "vserver.jar"
  :profiles {:uberjar {:aot [vserver.core]}}
  :main vserver.core)