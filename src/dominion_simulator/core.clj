(ns dominion-simulator.core)


(defn init-player
  "initialize player"
  [player-name strategy-fn]
  {:name     player-name
   :strategy strategy-fn
   :hand     []
   :discard  []
   :draw     []
   :in-play  []})

(defn init-card
  "initialize card"
  [card-type card-name cost effect-fn])

(defn init-game-state
  "initialize game state"
  [players kingdom-cards victory-cards treasure-cards]
  {:players players
   :cards   {
             :kingdom  kingdom-cards
             :victory  victory-cards
             :treasure treasure-cards}})

(defn count-empty-decks
  "given a set of decks of an arbitrary card type, count how many are empty"
  [cards]
  ;; TODO
  0)

(defn is-game-over
  "given a game state, determine if the game is over"
  [game-state]
  (cond
    (let [provice-cards (get-in game-state [:cards :victory "provice"])]
      (= ((count provice-cards) 0))) true
    (let [empty-decks (+ (map count-empty-decks (vals (:cards game-state))))]
      (>= 3 empty-decks))            true
    :else                            false
    ))

(defn get-winner
  "given a game state, determine the winner of the game"
  [game-state]
  (first (:players game-state)))

(defn maybe-shuffle-draw-deck
  [game-state player-index]
  (let [player (get (:players game-state) player-index)]))

(defn draw-hand
  [game-state player-index]
  ;; TODO
  game-state)

(defn do-player-action-phase
  [game-state player-index]
  ;; TODO
  game-state)


(defn do-player-buy-phase
  [game-state player-index]
  ;; TODO
  game-state)


(defn do-player-cleanup-phase
  [game-state player-index]
  ;; TODO
  game-state)

(defn do-player-turn
  "given a game state, simulate turn of a single player and return next game state"
  [game-state player-index]
  (let [player (get (:players game-state) player-index)]
    (-> game-state
        (do-player-action-phase player-index)
        (do-player-buy-phase player-index)
        (do-player-cleanup-phase player-index))))

(defn do-turn
  "given a game state, simulate turn of all players and return next game state"
  [game-state]
  (loop [players           (:players game-state)
         curr-player-index 0]
    (if (= curr-player-index (- (count players) 1))
      game-state
      (recur (do-player-turn game-state curr-player-index) (+ curr-player-index 1)))))

(defn setup-game
  "perform required setup prior to player turns"
  [game-state]
  ;; TODO
  game-state)

(defn simulate-game
  "main function for simulating a game of dominion"
  [game-state]
  (if (is-game-over game-state)
    (get-winner game-state)
    (let [next-game-state (do-turn game-state)]
      (simulate-game next-game-state))))


;; (simulate-game (setup-game (init-game-state default-players default-kingdom-cards default-victory-cards default-treasure-cards )))
