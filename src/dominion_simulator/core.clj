(ns dominion-simulator.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn is-game-over
  "given a game state, determine if the game is over"
  [game-state]
  false)

(defn get-winner
  "given a game state, determine the winner of the game"
  [game-state]
  (first (:players game-state)))

(defn do-turn
  "given a game state, simulate turn and return next game state"
  [game-state]
  game-state)

(defn simulate-game
  "main function for simulating a game of dominion"
  [game-state]
  (if (is-game-over (:cards game-state))
    (get-winner (:players game-state))
    (let [next-game-state (do-turn game-state)]
      (simulate-game next-game-state))))
