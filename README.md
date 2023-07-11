This is a project where i make a card game. Backend is in Spring boot with java and frontend is in React with typescript

This is a card game in which players can build decks with created cards. In the duel, there are two players who fight each other. In each move, a player can play one card or pass. If both players pass, the one with more points on the board wins. The player who wins two rounds wins the game. <br>

Right now it doesn't make much sense, because what is the point of playing such game when you can see enemy cards. I am implementing messaging 
system with Spring Boot Web sockets so that this game can be played with multiplayer mode.

Currently, there are about 15 cards. Most of them dont have any effects, but not all. <br>
Fireball - Deals damage to an enemy card. <br>
Archer - A unit that can be placed and deals damage to an enemy card. <br>
Conflagration - Burns all cards with maximum points. <br>
Healer - Boosts every card on the player's board that has no more than 2 points. <br>
Booster - Boosts a single player card. <br>
Leader - Boosts every card in one row. <br>
Doubler - Doubles chosen card points. <br>
Rip - deals damage to whole row <br>
Rain - deals damage to max points card every turn <br>

To log in you have to enter name. Every user have its own instance of deck builder so there cannot be two users with same name.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/8c184afe-dbe5-4aa3-82b8-932e23d1fb89)
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/b57775f4-44c4-49cc-a294-0b325b1a5020)




There is deck builder where you can add and remove cards from deck, add and remove decks
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/8c23f819-ac07-4168-bbc0-80d65682688b)

There is also duel page, where players can put cards from hand on one of three rows. (multiplayer ready but enemy has to manually update data, messaging system incoming)
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/4b777948-a687-4378-8aaf-cbe1af10dedd)


Based on card effect type, player can choose card to target:
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/fb4125a3-fb0d-46b7-9dae-07ef059d58f1)
Archer targetted Armageddon and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/28d8d3bd-70bb-4a90-a1b9-4dab6beb8310)

Also player can choose row to target
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/224a6d56-50c7-4e3f-82e2-72ffddff225b)
Rip targetted second row and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/9d5e7c71-fe99-4fb6-9c5a-f96758decd79)

Also there are cards that have effects, but player dont have to target anything
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/d8eb15a9-99b4-43ee-a324-792fa63cea64)
After playing conflagration max points card is burned.





