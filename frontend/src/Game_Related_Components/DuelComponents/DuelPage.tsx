import React, {useState, useEffect} from 'react';
import {Card} from './../Interfaces/Card';
import { DragDropContext, DropResult } from 'react-beautiful-dnd';
import { Droppable, Draggable } from 'react-beautiful-dnd';
import './DuelPage.css';

const DuelPage = () => {
  const [refresh, setRefresh] = useState(false);
  const [cardsInHand, setCardsInHand] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);

  const [cardsOnBoard, setCardsOnBoard] = useState<Card[]>([]);
  const [cardsOnSecondRow, setCardsOnSecondRow] = useState<Card[]>([]);
  const [cardsOnThirdRow, setCardsOnThirdRow] = useState<Card[]>([]);

  const [pointsOnBoard, setPointsOnBoard] = useState<number>();
  const [wonRounds, setWonRounds] = useState<number>();
  const [isTurnOfPlayer1, setIsTurnOfPlayer1] = useState<boolean>(false);
  const [didWon, setDidWon] = useState<boolean>(false);

  const [cardsInHand2, setCardsInHand2] = useState<Card[]>([]);
  const [cardsInDeck2, setCardsInDeck2] = useState<Card[]>([]);

  const [cardsOnBoard2, setCardsOnBoard2] = useState<Card[]>([]);
  const [cardsOnSecondRow2, setCardsOnSecondRow2] = useState<Card[]>([]);
  const [cardsOnThirdRow2, setCardsOnThirdRow2] = useState<Card[]>([]);

  const [pointsOnBoard2, setPointsOnBoard2] = useState<number>();
  const [wonRounds2, setWonRounds2] = useState<number>();
  const [isTurnOfPlayer2, setIsTurnOfPlayer2] = useState<boolean>(false);
  const [didWon2, setDidWon2] = useState<boolean>(false);



  const [deckData, setDeckData] = useState<Card[]>([]);


  useEffect(() => {
    const controller = new AbortController();
    fetch('http://localhost:8000/DeckBuilder/GetCardsInDeck')
      .then((res) => res.json())
      .then((deckData: Card[]) => {
        setDeckData(deckData);
      })
      .catch(console.error);

      return () => {
        controller.abort();
      };
  }, []);
  let firstPlayer:string = "first";
  let secondPlayer:string = "second";
  useEffect(() => {
    const controller = new AbortController();
    if (deckData.length > 0) {
      fetch(`http://localhost:8000/Duel/SetupDecks?firstUser=${firstPlayer}&secondUser=${secondPlayer}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(deckData),
        signal: controller.signal
      });
    }

    return () => {
      controller.abort();
    };

  }, [deckData]);

  const fetchCardsData = () => {

    fetch(`http://localhost:8000/Duel/getHandCards/${firstPlayer}`)
      .then((res) => res.json())
      .then((cardsInHand: Card[]) => {
        setCardsInHand(cardsInHand);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getDeckCards_player1')
      .then((res) => res.json())
      .then((cardsInDeck: Card[]) => {
        setCardsInDeck(cardsInDeck);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardCards_player1')
      .then((res) => res.json())
      .then((cardsOnBoard: Card[]) => {
        setCardsOnBoard(cardsOnBoard);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardCardsOnSecondRow_player1')
      .then((res) => res.json())
      .then((cardsOnSecondRow: Card[]) => {
        setCardsOnSecondRow(cardsOnSecondRow);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardCardsOnThirdRow_player1')
      .then((res) => res.json())
      .then((cardsOnThirdRow: Card[]) => {
        setCardsOnThirdRow(cardsOnThirdRow);
      })
      .catch(console.error);


    fetch('http://localhost:8000/Duel/getBoardPoints_player1')
      .then((res) => res.json())
      .then((pointsOnBoard: number) => {
        setPointsOnBoard(pointsOnBoard);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/isTurnOf_player1')
      .then((res) => res.json())
      .then((isTurnOfPlayer1: boolean) => {
        setIsTurnOfPlayer1(isTurnOfPlayer1);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getWonRounds_player1')
      .then((res) => res.json())
      .then((wonRounds: number) => {
        setWonRounds(wonRounds);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/didWon_player1')
      .then((res) => res.json())
      .then((didWon: boolean) => {
        setDidWon(didWon);
      })
      .catch(console.error);

      // PLAYER 2

      fetch('http://localhost:8000/Duel/getHandCards_player2')
      .then((res) => res.json())
      .then((cardsInHand2: Card[]) => {
        setCardsInHand2(cardsInHand2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getDeckCards_player2')
      .then((res) => res.json())
      .then((cardsInDeck2: Card[]) => {
        setCardsInDeck2(cardsInDeck2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardCards_player2')
      .then((res) => res.json())
      .then((cardsOnBoard2: Card[]) => {
        setCardsOnBoard2(cardsOnBoard2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardCardsOnSecondRow_player2')
      .then((res) => res.json())
      .then((cardsOnSecondRow2: Card[]) => {
        setCardsOnSecondRow2(cardsOnSecondRow2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardCardsOnThirdRow_player2')
      .then((res) => res.json())
      .then((cardsOnThirdRow2: Card[]) => {
        setCardsOnThirdRow2(cardsOnThirdRow2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getBoardPoints_player2')
      .then((res) => res.json())
      .then((pointsOnBoard2: number) => {
        setPointsOnBoard2(pointsOnBoard2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/isTurnOf_player2')
      .then((res) => res.json())
      .then((isTurnOfPlayer2: boolean) => {
        setIsTurnOfPlayer2(isTurnOfPlayer2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/getWonRounds_player2')
      .then((res) => res.json())
      .then((wonRounds2: number) => {
        setWonRounds2(wonRounds2);
      })
      .catch(console.error);

    fetch('http://localhost:8000/Duel/didWon_player2')
      .then((res) => res.json())
      .then((didWon2: boolean) => {
        setDidWon2(didWon2);
      })
      .catch(console.error);

    setRefresh(true);
  }






  const onDragEnd_player1 = (result:DropResult) => {
    const {destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    let cardDragged: Card = {name: result.draggableId, points: 0};
    if(destination.droppableId === "Board"){
      fetch('http://localhost:8000/Duel/playCard_player1', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    }
    else if(destination.droppableId === "BoardRow2"){
      fetch('http://localhost:8000/Duel/playCardOnSecondRow_player1', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    }
    else if(destination.droppableId === "BoardRow3"){
      fetch('http://localhost:8000/Duel/playCardOnThirdRow_player1', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    }
    
    fetchCardsData();
  }

  const onDragEnd_player2 = (result:DropResult) => {
    const {destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    let cardDragged: Card = {name: result.draggableId, points: 0};
    if(destination.droppableId === "Board"){
      fetch('http://localhost:8000/Duel/playCard_player2', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    }
    else if(destination.droppableId === "BoardRow2"){
      fetch('http://localhost:8000/Duel/playCardOnSecondRow_player2', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    }
    else if(destination.droppableId === "BoardRow3"){
      fetch('http://localhost:8000/Duel/playCardOnThirdRow_player2', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    }
    fetchCardsData();
  }

  const endRound_player1 = () => {
      fetch('http://localhost:8000/Duel/endRound_player1', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: null
      });
  }

  const endRound_player2 = () => {
    fetch('http://localhost:8000/Duel/endRound_player2', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: null
      });
  }



  return (
    
    <div>
      
      <div>
        <label>Let the battle begin</label>
        <button onClick={fetchCardsData}>Load data</button>
      </div>
      
      <button onClick={endRound_player1}>End round</button>
      <p>Did you won {didWon.toString()}</p>
      <p>Won rounds {wonRounds}</p>
      <label>Is your turn: {isTurnOfPlayer1.toString()}</label>

      <DragDropContext onDragEnd = {(onDragEnd_player1)}>
        
        <Droppable droppableId="Hand">
          {(provided) => (
            <div className = "HandContainerP1" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftHandContentP1">
                <h3>Hand</h3>
              </div>
              <div className="rightHandContainerP1">
                <ul>
                  {cardsInHand.map((card, index) =>(
                    <Draggable key={card.name} draggableId={card.name} index={index}>
                      {(provided) => (
                        <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                          {card.name}, {card.points}
                        </li>    
                      )}  
                    </Draggable>
                  ))}
                </ul>
              </div>
              {provided.placeholder}  
            </div>
          )}
        </Droppable>
        <Droppable droppableId="BoardRow3">
          {(provided) => (
            <div className="BoardContainerP1" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContentP1">
                <h3>Row 3: {pointsOnBoard} points</h3>
              </div>
              <div className="rightBoardContentP1">
                <ul>
                  {cardsOnThirdRow.map((card, index) =>(
                    <li>{card.name}, {card.points}</li>    
                  ))}
                </ul>
              </div>
              {provided.placeholder}    
            </div>
          )}
        </Droppable>
        <Droppable droppableId="BoardRow2">
          {(provided) => (
            <div className="BoardContainerP1" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContentP1">
                <h3>Row 2: {pointsOnBoard} points</h3>
              </div>
              <div className="rightBoardContentP1">
                <ul>
                  {cardsOnSecondRow.map((card, index) =>(
                    <li>{card.name}, {card.points}</li>    
                  ))}
                </ul>
              </div>
              {provided.placeholder}    
            </div>
          )}
        </Droppable>
        <Droppable droppableId="Board">
          {(provided) => (
            <div className="BoardContainerP1" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContentP1">
                <h3>Board: {pointsOnBoard} points</h3>
              </div>
              <div className="rightBoardContentP1">
                <ul>
                  {cardsOnBoard.map((card, index) =>(
                    <li>{card.name}, {card.points}</li>    
                  ))}
                </ul>
              </div>
              {provided.placeholder}    
            </div>
          )}
        </Droppable>
      </DragDropContext>  
        

      {/* |||||||||||||||| TO JEST GRACZ 2 |||||||||||||||| */}
      
      <DragDropContext onDragEnd = {(onDragEnd_player2)}>
        
              
      <Droppable droppableId="Board">
          {(provided) => (
            <div className="BoardContainerP2" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContentP2">
                <h3>Board: {pointsOnBoard2} points</h3>
              </div>
              <div className="rightBoardContentP2">
                <ul>
                  {cardsOnBoard2.map((card, index) =>(
                    <li>{card.name}, {card.points}</li>    
                  ))}
                </ul>
              </div>
              {provided.placeholder}    
            </div>
          )}
        </Droppable>

        <Droppable droppableId="BoardRow2">
          {(provided) => (
            <div className="BoardContainerP2" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContentP2">
                <h3>Row 2: {pointsOnBoard} points</h3>
              </div>
              <div className="rightBoardContentP2">
                <ul>
                  {cardsOnSecondRow2.map((card, index) =>(
                    <li>{card.name}, {card.points}</li>    
                  ))}
                </ul>
              </div>
              {provided.placeholder}    
            </div>
          )}
        </Droppable>
        <Droppable droppableId="BoardRow3">
          {(provided) => (
            <div className="BoardContainerP2" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContentP2">
                <h3>Row 3: {pointsOnBoard} points</h3>
              </div>
              <div className="rightBoardContentP2">
                <ul>
                  {cardsOnThirdRow2.map((card, index) =>(
                    <li>{card.name}, {card.points}</li>    
                  ))}
                </ul>
              </div>
              {provided.placeholder}    
            </div>
          )}
        </Droppable>

        <Droppable droppableId="Hand">
          {(provided) => (
            <div className = "HandContainerP2" ref={provided.innerRef} {...provided.droppableProps}>
            <div className="leftHandContentP2">
              <h3>Hand</h3>
            </div>
            <div className="rightHandContainerP2">
              <ul>
                {cardsInHand2.map((card, index) =>(
                  <Draggable key={card.name} draggableId={card.name} index={index}>
                    {(provided) => (
                      <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                        {card.name}, {card.points}
                      </li>    
                    )}  
                  </Draggable>
                ))}
              </ul>
            </div>
              {provided.placeholder}  
            </div>
          )}
        </Droppable>
          
      </DragDropContext>
      
      <label>Is your turn: {isTurnOfPlayer2.toString()}</label>
      <p>Won rounds {wonRounds2}</p>
      <p>Did you won {didWon2.toString()}</p>
      <button onClick={endRound_player2}>End round</button>
    </div>
  )
}

export default DuelPage