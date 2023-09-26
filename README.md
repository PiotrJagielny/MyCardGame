

## General overview
This is a Gwent: The Witcher card game clone. <br>
Backend is built with Spring boot with Java and frontend is made using React with Typescript. <br>
To store important data on frontend such as username, i used react redux. <br>
For multiplayer messaging i used Spring web sockets and SockJS. <br>
To store users decks i used PostgreSQL and JPA/Hibernate. <br>
Frontend is hosted via Vercel <br>
Backend is hosted via AWS. Server is running on EC2 instance and database is running on AWS RDS <br>
Also frontend is secured by vercel automatically, but for backend i had to get SSL cerfificate to get HTTPS <br>

## Rules
Game consists of 3 rounds, player who wins 2 rounds wins game. When both players end round, one with more points on board wins round. <br>
For every won round a crown appears in the middle of board. <br>
In each move, a player can either play card or end round.<br>
(card that base power is 0 or below is destroyed from game) <br>
## Cards
Currently, there are about 30 cards. Most of them dont have any effects.<br>

Here are cards with effect. 

Spells: <br>
Fireball - Deals damage to an enemy card.<br>
Conflagration - Burns all cards with maximum points. <br>
Epidemic - Destroy all min points cards on board <br>
Rip - deals damage to whole row <br>
Rain - deals damage to max points card every turn <br>
Mushrooms - Decrease base power of enemy card<br>
Tasty mushrooms - Increase base power of enemy card<br>
Handcuffs - Lock enemy card and deal damage <br>
Key - Unlock and boost your card <br>

Units: <br>
Archer - A unit that can be placed and deals damage to an enemy card. <br>
Healer - Boosts every card on the player's board that has no more than 2 points. <br>
Booster - Boosts a single player card. <br>
Leader - Boosts every card in one row. <br>
Doubler - Doubles chosen card points. <br>
Sharpshooter - deals damage to one enemy card and if this card dies, sharpshooter boost itself <br>
Cow - If cow dies, spawn chort in the same row <br>
Trebuchet - Deal i damage to random enemy every 2 turns<br>
Good person - Boost card on your board every 2 turns<br>
Gravedigger - Boost by number of cards on your graveyard on deploy<br>
Witch - Ressurect card from your graveyard<br>
Wild roam - Play all copies of this card from you deck on the same row<br>
Supplier - Play from deck copy of chosen card from board <br>
Breaker - After 2 turns, play copy of this cards from deck on the same row <br>
Ginger - Deal 1 damage to every card on specified enemy row <br>
Spy - After playing this card it moves to enemy row, and player draws 1 card <br>
Blue fire - If opposite row has at least 10 points, burn all max points cards on this row <br>
Axer - Deal damage to enemy card by number of weakened cards on enemy board <br>
Copier - Choose card on your board and insert 2 base copier of this card into your deck <br>
Trex - Eat card on your board and boost this card by eaten cards power <br>

## New features
Points of weakened cards have red color and and boosted cards have green points. <br>
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/3f0d6ccf-ec18-4e0c-8370-495f4dfe2441)

There are bronze, silver and gold cards. Gold cards can only be targetter by other gold cards, for example <br>
archer cant shoot Priest <br>
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/3e948464-8128-4e59-a98c-e32fe139894a)

I added two fractions: Monsters, Humans. You can build deck with only one fraction and only neutral cards
are available with every fraction <br>
Every fraction has special fraction ability. <br>
Humans - Has +2 points for gold cards <br>
Monsters - Random card stays on board after round ends <br>
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/82891e53-d20c-48e6-95a0-aa027e553bde)
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/094b976c-6ba7-4129-a99f-e3c04618fe8c)
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/0cc04074-9f49-4e9d-ac9c-66c4517ce172)




## Deck builder demo
![ezgif com-video-to-gif (3)](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/b0575655-54c8-4e7c-adbb-eb686bbc594a)


## Duel demo
Quick duel demo
![ezgif com-video-to-gif (1)](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/27a04121-5294-48a9-adea-92601d2e1f2d)
<br>
<br>
I can play cards on board
![play card](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/816886d2-7e18-46a5-83b6-19f107f18671) <br>
I can target my card
![target my card](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/def2bafb-c98b-4ec1-8bbd-d481ab9d15a5) <br>
I can target and destroy enemy card
![target enemy card](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/c7a174bd-9b3a-463a-bf6b-901bea94cf48) <br>
I can play cards in chain, target enemy row and make weather effects
![chain and weather](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/a8ef3a38-1111-4da0-a1e4-9295592ee5ba) <br>
<br>


Player lost 2 rounds and lost game <br>
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/e8740511-dc1e-40e6-8174-b07ec354009d)

Player won 2 rounds and won game <br>
![image](https://github.com/PiotrJagla/GwentClone-MainProj/assets/76881722/16fdb528-e908-457e-8943-2fbe81a2e3e9)

<br>If both players had same amount of points, they both score a crown. Here two players had same amount of points and game ended with draw<br>
![image](https://github.com/PiotrJagla/MyCardGame-MainProj/assets/76881722/3ceeddc5-9664-4f9c-a8ca-89237f3a8e98)













