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
  const [pointsOnBoard, setPointsOnBoard] = useState<number>();

  const [cardsInHand2, setCardsInHand2] = useState<Card[]>([]);
  const [cardsInDeck2, setCardsInDeck2] = useState<Card[]>([]);
  const [cardsOnBoard2, setCardsOnBoard2] = useState<Card[]>([]);
  const [pointsOnBoard2, setPointsOnBoard2] = useState<number>();



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
  
  useEffect(() => {
    const controller = new AbortController();
    if (deckData.length > 0) {
      fetch('http://localhost:8000/Duel/SetupDecks', {
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

    fetch('http://localhost:8000/Duel/getHandCards_player1')
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

    fetch('http://localhost:8000/Duel/getBoardPoints_player1')
      .then((res) => res.json())
      .then((pointsOnBoard: number) => {
        setPointsOnBoard(pointsOnBoard);
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

    fetch('http://localhost:8000/Duel/getBoardPoints_player2')
      .then((res) => res.json())
      .then((pointsOnBoard2: number) => {
        setPointsOnBoard2(pointsOnBoard2);
      })
      .catch(console.error);

    setRefresh(true);
  }






  const onDragEnd_player1 = (result:DropResult) => {
    const {destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    let cardDragged: Card = {name: result.draggableId};
    fetch('http://localhost:8000/Duel/playCard_player1', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
    fetchCardsData();
  }

  const onDragEnd_player2 = (result:DropResult) => {
    const {destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    let cardDragged: Card = {name: result.draggableId};
    fetch('http://localhost:8000/Duel/playCard_player2', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(cardDragged)
      });
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


      {/* |||||||||||||||| TO JEST GRACZ 1 |||||||||||||||| */}
      {/* <div>
      <h4>Cards in deck remaining</h4>
      <ul>
       {cardsInDeck.map(card =>(
            <li>{card.name}</li>
        ))}
      </ul>
      </div> */}
      <button onClick={endRound_player1}>End round</button>
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
                          {card.name}
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
          
        
        <Droppable droppableId="Board">
          {(provided) => (
            <div className="BoardContainerP1" ref={provided.innerRef} {...provided.droppableProps}>
              <div className="leftBoardContentP1">
                <h3>Board: {pointsOnBoard} points</h3>
              </div>
              <div className="rightBoardContentP1">
                <ul>
                  {cardsOnBoard.map((card, index) =>(
                    <li>{card.name}</li>    
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
                    <li>{card.name}</li>    
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
                        {card.name}
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

      <button onClick={endRound_player2}>End round</button>

      {/* <div>
      <h4>Cards in deck remaining</h4>
      <ul>
       {cardsInDeck2.map(card =>(
            <li>{card.name}</li>
        ))}
      </ul>
      </div> */}
    </div>
  )
}

export default DuelPage