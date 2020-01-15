(ns org.parkerici.image-utils.ij.hyperstack
  (:import [net.imglib2 Cursor RandomAccess]
           [net.imglib2.view Views])
  (:require [clojure.string :as str]))

; Takes a hyperstack image and splits it into its individual slices.
; Each slice is an ItervalView object that must be converted back into an image before being saved.
; Returns a map of the format {"dimension_string" IntervalView}, where dimension string is of the format
; 0_1_2 where the first each position represents a dimension and the value there represents the slice number. 
(defn img->slices
  ([img] (img->slices img ""))
  ([img dimensions]
  ; If we are down to 2 dimensions (width and height) then stop recurring and return the image
  ; Otherwise split on the 3rd dimension and recur on the split images.
   (if  (= (.numDimensions img) 2)
     {dimensions img}
     (apply merge (for [x (range (.dimension img 2))]
                    (let [split-dimensions (str/split dimensions #"_")
                          filtered-dimensions (filter not-empty split-dimensions)
                          new-dimensions (str/join "_" (concat filtered-dimensions [x]))]
                      (img->slices (Views/hyperSlice img 2 x) new-dimensions)))))))

; The slices returned by img->slices cannot be directly written to a file as they are IntervalView objects.
; If we want to save a slice to a file, first we must convert 
(defn slice->img
  [source-img slice]
  (let [dest-img (.create (.factory source-img) slice) ; User source-image to create a container of type source-img with slice dimensions
        ^Cursor target-cursor (.localizingCursor dest-img)
        ^RandomAccess source-random-access (.randomAccess slice)]
    (while (.hasNext target-cursor)
      (do
        (.fwd target-cursor)
        (.setPosition source-random-access target-cursor)
        (.set (.get target-cursor) (.get source-random-access))))
    dest-img))