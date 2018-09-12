(ns droplets.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn -main [& args]
  (println ""))

(def hue 200)
(def avg-size 10)
(def variance 1)
(def drop-area 1000)

(defn draw-circle [circle]
  (let [{radius :radius
         x :x
         y :y
         color :color} circle]
    (q/with-fill color
        (q/ellipse x y (* 2 radius) (* 2 radius)))))

(defn create-circle
  ([]
    (let [radius (q/random (- avg-size variance) (+ avg-size variance))
          x (q/random radius (- (q/width) radius))
          y (q/random radius (- (q/height) radius))
          color [(q/random (- hue 40) (+ hue 40)) (q/random 160 200) (q/random 160 200)]]
      (create-circle radius x y color)))
  ([radius x y color]
    {:radius radius :x x :y y :color color :drop-vel 0}))

(defn intersects [circle1 circle2]
  (let [dist (q/dist (:x circle1) (:y circle1) (:x circle2) (:y circle2))]
    (< dist (+ (:radius circle1) (:radius circle2)))))

(defn all-intersections [circle circles]
  (filter #(intersects circle %) circles))

(defn area [circle]
  (* (:radius circle) (:radius circle) (Math/PI)))

(defn total-area [circles]
  (reduce + (map area circles)))

(defn calc-midpoint [circles]
  {:x (/ (reduce + (map #(* (area %) (:x %)) circles)) (total-area circles))
   :y (/ (reduce + (map #(* (area %) (:y %)) circles)) (total-area circles))})

(defn merge-circles [circles]
   (let [new-radius (Math/sqrt (/ (total-area circles) (Math/PI)))
         new-loc (calc-midpoint circles)
         new-color (:color (apply max-key #(area %) circles))]
     (create-circle new-radius (:x new-loc) (:y new-loc) new-color)))

(defn check-merges [circles]
  (let [to-merge (filter #(> (count %) 1) (map #(all-intersections % circles) circles))
        to-add (map #(merge-circles %) to-merge)]
    (if (= (count to-merge) 0)
      circles
      (remove (set (flatten to-merge)) (concat circles to-add)))))

(defn update-positions [circles]
  (map #(assoc % :y (+ (:y %) (:drop-vel %))) circles))

(defn check-sizes [circles]
  (map #(assoc % :drop-vel (if (> (area %) drop-area) 10 0)) circles))

(defn setup []
  (q/no-stroke)
  (q/frame-rate 60)
  (q/color-mode :hsb)
  (q/background 240)
  [])

(defn update-state [state]
  (->> state
       (cons (create-circle))
       check-sizes
       update-positions
       check-merges))

(defn draw [state]
  (q/background 240)
  (doseq [circle state]
    (draw-circle circle)))

(q/defsketch droplets
  :title "Droplets"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw
  :features [:keep-on-top]
  :middleware [m/fun-mode])
