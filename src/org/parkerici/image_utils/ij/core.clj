(ns org.parkerici.image-utils.ij.core
  (:require [org.parkerici.image-utils.ij.hyperstack :as hyperstack]
            [org.parkerici.image-utils.ij.io :as ij-io]
            [org.parkerici.image-utils.utils.path :as path]
            [me.raynes.fs :as fs]))

(defn print-image-info
  [img]
  (println "Image info:")
  (println (str "Channels: " (.getChannels img)))
  (println (str "Depth: " (.getDepth img)))
  (println (str "Frames: " (.getFrames img)))
  (println (str "Height: " (.getHeight img)))
  (println (str "Width: " (.getWidth img)))
  (println (str "Dimensions: " (.numDimensions img)))
  (println (str "Slices: " (.numSlices img))))

(defn output-hyperstack-slice
  [source-img slice dimension img-base-name img-extension outpath]
  (let [fname (str img-base-name "_" dimension img-extension)
        fpath (path/join outpath fname)
        slice-img (hyperstack/slice->img source-img slice)]
    (ij-io/write-tiff slice-img fpath)))

(defn split-hyperstack
  ([fpath] (split-hyperstack fpath (.getPath (fs/parent fpath))))
  ([fpath outpath]
   (println (str "Reading image at " fpath))
   (println (str "Writing outputs to " outpath))
   (let [img-base-name (fs/base-name fpath true)
         img-extension (fs/extension fpath)
         img (ij-io/read-tiff fpath)
         slices (hyperstack/img->slices img)]
     (print-image-info img)
     (println "Writing slices to files. Warning: this may take a while.")
     (doall (pmap
             #(output-hyperstack-slice img (get slices %) % img-base-name img-extension outpath)
             (keys slices)))
     (println "Done!"))))

