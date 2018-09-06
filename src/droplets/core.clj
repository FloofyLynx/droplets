(ns droplets.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn -main [& args]
  (println ""))

(def hue 200)

(defn draw-circle [circle]
  (let [{radius :radius
         x :x
         y :y
         color :color} circle]
    (q/with-fill color
        (q/ellipse x y (* 2 radius) (* 2 radius)))))

(defn create-circle []
  (let [radius (q/random 10 20)
        x (q/random radius (- (q/width) radius))
        y (q/random radius (- (q/height) radius))
        color [(q/random (- hue 40) (+ hue 40)) (q/random 160 200) (q/random 160 200)]]
    {:radius radius :x x :y y :color color}))

(defn update-position [circle]
  (let [{x :x
         y :y} circle]
    (assoc (assoc circle :y (+ y (q/cos x) (q/sin x))) :x (+ x (q/sin y) (q/cos y)))))

(defn setup []
  (q/no-stroke)
  (q/frame-rate 60)
  (q/color-mode :hsb)
  (q/background 240)
  [])

(defn update-state [state]
  (map update-position (conj state (create-circle))))

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
