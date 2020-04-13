(ns org.parkerici.image-utils.ij.tiled
  (:require [clojure.xml]
            [org.parkerici.image-utils.utils.error :as error])
  (:import [loci.plugins BF]
           [java.io ByteArrayInputStream]
           [ij IJ ImagePlus ImageStack]
           [loci.formats.tiff TiffParser]))

(defn parse-xml [s]
  (clojure.xml/parse (ByteArrayInputStream. (.getBytes s "utf-16"))))

(defn get-tiff-comment [filename]
  (.getComment (TiffParser. filename)))

(defn read-tiff-metadata [filename]
  (-> filename get-tiff-comment parse-xml))

(defn get-num-channels
  "Return the number of channels in an ImagePlus."
  [^ImagePlus imp]
  (.getNChannels imp))

(defn get-num-frames
  "Return the number of frames in an ImagePlus."
  [^ImagePlus imp]
  (.getNFrames imp))

(defn get-num-slices
  "Return the number of slices in an ImagePlus."
  [^ImagePlus imp]
  (.getNSlices imp))

(defn get-width
  "Return the width of an image."
  [imp]
  (.getWidth ^ImagePlus imp))

(defn get-height
  "Return the height of an image."
  [imp]
  (.getHeight ^ImagePlus imp))

(defn get-channels
  [imp]
  (let [info (.getInfoProperty imp)
        split-info (clojure.string/split-lines info)
        name-strings (filterv #(clojure.string/starts-with? % "Name #") split-info)]
    (if (> (count name-strings) 0)
      (let [channels (map (fn [x] (last (clojure.string/split x #" = "))) name-strings)
            channels-underscore (map (fn [x] (clojure.string/replace x #" " "_")) channels)]
        channels-underscore)
      (error/exit 1 (error/error-msg ["No field Name found in image Info"]))
      )))

(defn split-channels
  "Split channels."
  [^ImagePlus imp]
  (doall
    (for [chan (range (get-num-channels imp))]
      (let [new-stack (ij.ImageStack. (get-width imp) (get-height imp))]
        (dotimes [t (get-num-frames imp)]
          (dotimes [z (get-num-slices imp)]
            (let [n (.getStackIndex imp (inc chan) (inc z) (inc t))]
              (.addSlice new-stack (.getProcessor (.getImageStack imp) n)))))
        (ImagePlus. (str "C" chan "-" (.getTitle imp))
                    new-stack)))))

(defn save-imp-as-tiff
  "Save an image as a tiff."
  [imp filename]
  (IJ/saveAsTiff ^ImagePlus imp ^string filename)
  imp)

(defn open-tiled-tiff
  [filename]
  (first (BF/openImagePlus filename))
  )

(comment
  (def filename "/Users/rkageyama/temp/seg/expandedtest/raw/840-100100-002_panel21_Baseline_10.tiff")
  (def metadata (read-tiff-metadata filename))
  (def myimg (BF/openImagePlus filename))
  (def imp (first myimg))
  (def channels (get-channels imp))
  (def split (split-channels imp))
  (range 3)
  (for [n (range (count split))]
    (let [channel (nth channels n)
          filepath (format "%s.tiff" channel)
          slice (nth split n)
          ]
      (save-imp-as-tiff slice filepath))
    )
  (def impo (ImporterOptions.setquiet))
  (map println (hash-map "a" "b"))
  (ImporterOptions/KEY_QUIET) )