(ns droplets.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn draw-circle [circle]
  (let [{radius :radius
         x :x
         y :y
         color :color} circle]
    (q/with-fill color
        (q/ellipse x y (* 2 radius) (* 2 radius)))))

(defn setup []
  (q/no-stroke)
  (q/frame-rate 60)
  (q/color-mode :hsb)
  (q/background 240)
  {:circles [{:radius 50 :x 100 :y 100 :color [220 180 180]}]})

(defn update [state]
  (update-in state [:radius] inc))

(defn draw [state]
  (draw-circle (get (get state :circles) 0)))

(q/defsketch droplets
  :title "Droplets"
  :size [500 500]
  :setup setup
  :update update
  :draw draw
  :features [:keep-on-top]
  :middleware [m/fun-mode])
