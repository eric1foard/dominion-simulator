(ns dominion-simulator.core)

(def draw-count 5)

(defn init-player
  "initialize player"
  [player-name strategy-fn]
  {:name           player-name
   :strategy       strategy-fn
   :hand           []
   :discard        []
   :draw           []
   :in-play        []
   :action-count   0
   :buy-count      0
   :treasure-count 0})

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

(defn update-player
  "update game state to reflect new player state"
  [game-state player updated-player-keys]
  (assoc-in game-state [:players (:name player)]
            (update-in player merge updated-player-keys)))

(defn maybe-shuffle-draw-deck
  [game-state player]
  (if (>= (count (:draw player)) draw-count)
    game-state
    (let [draw (conj (:draw player) (shuffle (:discard player)))]
      (update-player game-state player {:discard [] :draw draw}))))

(defn draw-hand
  [game-state player]
  (let [hand (take draw-count (:draw player))
        draw (drop draw-count (:draw player))]
    (update-player game-state player {:hand hand :draw draw})))

(defn discard-hand
  [game-state player]
  (let [discard (conj (:discard player) (:hand player))]
    (update-player game-state player {:discard discard :hand []})))


(defn turn-cleanup-phase
  "given a game state, simulate turn of a single player and return next game state"
  [game-state player]
  (-> game-state
      (discard-hand player)
      (maybe-shuffle-draw-deck player)
      (draw-hand player)))

(defn do-player-turn
"given a game state, simulate turn of a single player and return next game state"
[game-state player]
(-> game-state
    (:action-fn player)
    (:buy-fn player)
    (turn-cleanup-phase player)))

(defn do-turn
"given a game state, simulate turn of all players and return next game state"
[game-state]
(loop [players (vals (:players game-state))]
  (if (empty? players)
    game-state
    (recur (do-player-turn game-state (first players)) (rest players)))))

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
