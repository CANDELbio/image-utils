(defproject image-utils "0.1.0"
  :description "PICI Image Utilities"
  :url "http://example.com/FIXME"
  :license {:name "GPL-3.0 "
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "0.4.2"]
                 [me.raynes/fs "1.4.6"]
                 [net.imglib2/imglib2 "5.8.0"]
                 [net.imglib2/imglib2-ij "2.0.0-beta-45"]
                 [net.imagej/ij "1.52s"]]
  :main ^:skip-aot org.parkerici.image-utils.core
  :target-path "target/%s"
  :jvm-opts ["-Xmx16G"]
  :repositories {"imagej snapshots" "https://maven.imagej.net/content/repositories/snapshots/"
                 "imagej releases" "https://maven.imagej.net/content/repositories/releases/"
                 "imagej public" "https://maven.imagej.net/content/repositories/public/"}
  :profiles {:uberjar
             {:aot :all}
             :dev
             {:dependencies [[org.clojure/tools.nrepl "0.2.13"]]}})
