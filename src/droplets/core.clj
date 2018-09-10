(ns droplets.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn -main [& args]
  (println ""))

(def hue 200)
(def avg-size 10)

(defn draw-circle [circle]
  (let [{radius :radius
         x :x
         y :y
         color :color} circle]
    (q/with-fill color
        (q/ellipse x y (* 2 radius) (* 2 radius)))))

(defn create-circle
  ([]
    (let [radius (q/random (- avg-size 5) (+ avg-size 5))
          x (q/random radius (- (q/width) radius))
          y (q/random radius (- (q/height) radius))]
      (create-circle radius x y)))
  ([radius x y]
    (let [color [(q/random (- hue 40) (+ hue 40)) (q/random 160 200) (q/random 160 200)]]
      (create-circle x y color radius)))
  ([x y color radius]
    {:radius radius :x x :y y :color color}))

(defn intersects [circle1 circle2]
  (let [dist (q/dist (:x circle1) (:y circle1) (:x circle2) (:y circle2))]
    (< dist (+ (:radius circle1) (:radius circle2)))))

(defn area [circle]
  (* (:radius circle) (:radius circle) (Math/PI)))

(defn merge-circles [circles]
  (let [total-area (reduce + (map area circles))
        new-radius (Math/sqrt (/ total-area (Math/PI)))
        new-x (/ (reduce + (map :x circles)) (count circles))
        new-y (/ (reduce + (map :y circles)) (count circles))]
    (create-circle new-radius new-x new-y)))

(defn add-circle [all-circles new-circle]
  (let [intersections (filter #(intersects new-circle %) all-circles)]
    (if (> (count intersections) 0)
      (add-circle (remove (set intersections) all-circles) (merge-circles (conj intersections new-circle)))
      (conj all-circles new-circle))))


(defn setup []
  (q/no-stroke)
  (q/frame-rate 60)
  (q/color-mode :hsb)
  (q/background 240)
  [])

(defn update-state [state]
  (add-circle state (create-circle)))

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
