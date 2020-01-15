(ns org.parkerici.image-utils.ij.io
  (:import [net.imglib2.img ImagePlusAdapter]
           [ij.io Opener FileSaver]))

(defn write-tiff
  [img fpath]
  (.saveAsTiff (FileSaver. (.getImagePlus img)) fpath))

(defn read-tiff
  [fpath]
  (ImagePlusAdapter/wrap (.openImage (Opener.) fpath)))
