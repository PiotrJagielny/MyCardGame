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
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/b6a77471-4af0-4eed-ba58-181bf8404602)

![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/42d573bb-66e4-41be-bbd1-434cefb6530f)





There is deck builder where you can add and remove cards from deck, add and remove decks
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/22f248e0-6f04-4352-b968-d5e81ca16d69)
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/48e4ff12-c46f-4f3a-a547-95da6b5f17be)



There is also duel page, where players can put cards from hand on one of three rows. (multiplayer ready but enemy has to manually update data, messaging system incoming)
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/46151c06-b384-433f-9d2c-c23281c66d82)



Based on card effect type, player can choose card to target:
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/dfe7afeb-3567-4985-b7a8-97bc97ac02ec)

Archer targetted Capitan and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/5658d961-991a-4a4a-b2a5-8d12b27bfca9)


Also player can choose row to target
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/d3ac3163-43d7-4b65-a608-8ff4dec8893d)

Rip targetted third row and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/7cf61f50-1f4c-4151-b403-f099e5c8a170)


Also there are cards that have effects, but player dont have to target anything
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/66f681a7-2f32-48c9-a2f9-37bcac274ede)

After playing conflagration max points card is burned.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/09112b40-8b17-443c-b16c-6cf1dc2982fb)






