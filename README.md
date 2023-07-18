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

This is multiplayer game, so when you click find enemy, there has to be other player waiting for fight to start a game (normally this ring on bottom is rotating).<br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/8be324bf-0f42-4e03-8533-e739ee734c1d)






There is deck builder where you can add and remove cards from deck, add and remove decks
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/22f248e0-6f04-4352-b968-d5e81ca16d69)
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/48e4ff12-c46f-4f3a-a547-95da6b5f17be)



There is also duel page, where players can put cards from hand on one of three rows. 
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/8da00777-52f2-43cb-aca6-f24d30738b17)




Based on card effect type, player can choose card to target:
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/be43a25e-bd82-463d-857a-30f0ccd2b4ab)


Archer targetted Capitan and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/908d7589-54d3-41d2-a950-5bcf6ba62ab0)



Also player can choose row to target
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/7fb245b1-1434-4447-ac59-6350acf50188)


Rip targetted third row and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/7bfdba38-9cc1-4844-afca-d484f1c63c0a)



Also there are cards that have effects, but player dont have to target anything
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/de91a7e0-e257-4a3e-8141-a2c89ac32a6d)


After playing conflagration max points card is burned.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/fb67f3a9-4f92-4202-84d9-7612650233f3)

After someone wins round, there appears a crown in the middle left on the proper side. Enemy won round and scored a crown. <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/fde3871e-4e9b-41eb-aaec-afdc5923a9ee)

Player lost 2 rounds and lost game <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/abf55ec1-ca95-40a9-9258-44abfc027221)
Player won 2 rounds and won game <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/6a587604-3af2-4a75-b2b6-699894061af8)










