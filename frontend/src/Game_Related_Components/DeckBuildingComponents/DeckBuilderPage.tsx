import React, { useState, useEffect } from 'react';
import { DragDropContext, Droppable, Draggable, DropResult } from 'react-beautiful-dnd';
import {AllCardsDisplay} from './AllCardsDisplay';
import {CardsInDeckDisplay} from './CardsInDeckDisplay';
import {DecksManager} from './DecksManager';
import {Card} from './../Interfaces/Card';
import  './DeckBuilderPage.css';


let Messages: string[] = [];

const DeckBuilderPage = () => {
  const [cardsData, setCardsData] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);

  useEffect(() => {
    fetchCardsData();
  }, []);

  const fetchCardsData = () => {
    fetch('http://localhost:8000/DeckBuilder/GetAllCards')
      .then((res) => res.json())
      .then((cardsData: Card[]) => {
        setCardsData(cardsData);
      })
      .catch(console.error);

      fetch('http://localhost:8000/DeckBuilder/GetCardsInDeck')
      .then((res) => res.json())
      .then((cardsInDeck: Card[]) => {
        setCardsInDeck(cardsInDeck);
      })
      .catch(console.error);
  }

  const fetchCardsCollection = () => {
    
  }

  const ChangeDecksState = async (cardNameToPost: string, PostURL: string) =>{
    
    const response = await fetch(PostURL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: cardNameToPost
    });

    Messages.push(await response.text());
    

    console.log(response.body);
    if(!response.ok){
      throw new Error('Failed to change deck state');
    }

    fetchCardsData();
  };

  const onDragEnd = (result:DropResult) => {
    const {source, destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === source.droppableId && destination.index === source.index){return;}

    let PostURL:string = '';

    if(destination.droppableId === "AllCards"){
      PostURL = "http://localhost:8000/DeckBuilder/PutCardFromDeckBack"
    }
    else if(destination.droppableId === "CardsInDeck"){
      PostURL = "http://localhost:8000/DeckBuilder/PutCardToDeck"
    }

    ChangeDecksState(result.draggableId, PostURL); 
  }

  const handleDecksSwitch = () => {
    fetchCardsData();
  }

  return (
    <div className="DeckBuilderPage">
      <h2>DeckBuilderPage</h2>

      

      <div className="Decks">

        <DragDropContext onDragEnd={onDragEnd}>
          <div className = "AllCards">
            <AllCardsDisplay Cards={cardsData}></AllCardsDisplay>
          </div>
          <div className = "AllCardsInDeck">
            <CardsInDeckDisplay Cards={cardsInDeck}></CardsInDeckDisplay>
          </div>
        </DragDropContext>
        <div className="PlayersDecks">
          <DecksManager OnDecksSwitched={handleDecksSwitch}></DecksManager>
        </div>
      </div>
      <div className="Messages">
        {Messages.filter(message => message.length !== 0).map(message =>(
          <ul>
            <li>{message}</li>
          </ul>
        ))}
      </div>
    </div>
  );
};

export default DeckBuilderPage;
