(ns droplets.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn -main [& args]
  (println ""))

(def hue 51)

(defn draw-circle [circle]
  (let [{radius :radius
         x :x
         y :y
         color :color} circle]
    (q/with-fill color
        (q/ellipse x y (* 2 radius) (* 2 radius)))))

(defn create-circle []
  (let [radius (q/random 5 10)
        x (q/random radius (- (q/width) radius))
        y (q/random radius (- (q/height) radius))
        color [(q/random (- hue 20) (+ hue 20)) (q/random 180 220) (q/random 180 220)]]
    {:radius radius :x x :y y :color color}))

(defn setup []
  (q/no-stroke)
  (q/frame-rate 60)
  (q/color-mode :hsb)
  (q/background 240)
  {:circles [(create-circle)]})

(defn update-state [state]
  (let [circle (get (get state :circles) 0)]
    {:circles [(update circle :radius inc)]}))

(defn draw [state]
  (draw-circle (get (get state :circles) 0)))

(q/defsketch droplets
  :title "Droplets"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw
  :features [:keep-on-top]
  :middleware [m/fun-mode])
