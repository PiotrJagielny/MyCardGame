This is a project where i make a card game. Backend is in Spring boot with java and frontend is in React with typescript

This is a card game in which players can build decks with created cards. In the duel, there are two players who fight each other. In each move, a player can play one card or pass. If both players pass, the one with more points on the board wins. The player who wins two rounds wins the game. <br>

Currently, there are about 15 cards. Most of them dont have any effects, but not all. <br>
Fireball - Deals damage to an enemy card. <br>
Archer - A unit that can be placed and deals damage to an enemy card. <br>
Conflagration - Burns all cards with maximum points. <br>
Healer - Boosts every card on the player's board that has no more than 2 points. <br>
Booster - Boosts a single player card. <br>
Leader - Boosts every card in one row. <br>
(Some of these cards are implemented inside the CardGame_JavaFX repository for now.) <br>

There is deck builder where you can add and remove cards from deck, add and remove decks
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/8c23f819-ac07-4168-bbc0-80d65682688b)

There is also duel page, where players can put cards from hand on one of three rows. Game end when someone wins 2 rounds. A round is won by the player that has more points on board after two players clicked 'end round'.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/02249420-6361-4afd-a603-1ad5e4055d88)

