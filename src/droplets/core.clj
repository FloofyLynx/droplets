(ns droplets.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn draw-random-circle [color]
  (q/with-fill color
    (let [radius (q/random 10 20)
          diameter (* 2 radius)
          x (q/random radius (- (q/width) radius))
          y (q/random radius (- (q/height) radius))]
      (q/ellipse x y diameter diameter))
  ))

(defn setup []
  (q/no-stroke)
  (q/frame-rate 60)
  (q/color-mode :hsb)
  (q/background 240))

;(defn update)

(defn draw []
  (draw-random-circle [(q/random 200 225) (q/random 100 200) (q/random 100 200)]))

(q/defsketch droplets
  :title "Droplets"
  :size [500 500]
  :setup setup
  ;:update update
  :draw draw
  :features [:keep-on-top])
