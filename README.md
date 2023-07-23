This is a project where i make a card game. Backend is in Spring boot with java and frontend is in React with typescript. To store important
data such as username, i used react redux. For multiplayer handling with STOMP i used Spring web sockets on backend, and SockJS on frontend.
To store users decks i used PostgreSQL and Java JDBC api. 

This is card game based on rules of Gwint: The witcher card game.

In each move, a player can play one card or pass. If both players pass, the one with more points on the board wins. The player who wins two rounds wins the game. <br>

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
<br>
Before you find an enemy, you have to choose which deck you want to use, and if this deck is not build correctly, you can't start Duel. <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/0b5d2490-8119-4521-8f04-c64df069d3e7)







There is deck builder where you can add and remove cards from deck, add and remove decks
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/22f248e0-6f04-4352-b968-d5e81ca16d69)
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/48e4ff12-c46f-4f3a-a547-95da6b5f17be)



There is also duel page, where players can put cards from hand on one of three rows. 
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/8d6c127c-c1fd-414d-bd47-694c92cac174)





Based on card effect type, player can choose card to target:
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/e4e9d118-ecf0-480d-a8c1-6d8e96ebfe28)



Archer targetted Viking and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/55c9bb6b-3cc7-4123-ba8d-6d2e51643add)




Also player can choose row to target
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/d99b48d2-6241-48ee-8ef5-657742a92dbb)



Rip targetted third row and dealt damage
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/1780fc00-0dc8-4785-9988-9c94cfe49984)




Also there are cards that have effects, but player dont have to target anything
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/6c52144b-710f-42a2-a449-4beda5b5459b)



After playing conflagration max points card is burned.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/b4c7a339-a9a5-4400-8607-9314dad38e9a)

<br>You can play cards in a chain, for example, play a card that allows you to choose another card from the deck, which you can then play.
(this feature is in development for now, not ready for production)<br>
I played Priest, and a card from the deck that can be played appeared. I will choose to play an Archer.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/69601b89-dfec-4af6-abf3-7bb837570567)
Now, after choosing Archer, I can play this card as shown on the screen below. While playing Archer, I can't play any other cards from my hand.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/27e74c0f-2fed-41eb-87ef-5b1e17b18d3c)
Archer played and dealt 3 damage to the Viking. (I am super excited about this feature because there was a little bit of thinking on how to implement that, and voilà!) But apart from this, it's just a cool functionality.
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/403b1702-d25b-49e7-a865-b971312d39a1)




<br>After someone puts effect on row, for example rain that deals damage every turn, this is shown as below with corresponding background image.<br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/a300cdbc-09d0-499c-b595-deb46106112b)


Player lost 2 rounds and lost game <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/e1282036-17ea-474b-bfd7-d2164ec9463b)

After someone wins round, there appears a crown in the middle left on the proper side. <br>
Player won 2 rounds, 2 crowns appeared and won game <br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/24e9d1c6-e8c2-4297-97e5-f1365f8252f6)

<br>If both players had same amount of points, they both scores a crown. Here two players had same points and game ended with draw<br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/386a20c0-d603-46dc-aaa6-de082c4cb500)












