import React, { useState, useEffect } from 'react';
import { DragDropContext, DropResult } from 'react-beautiful-dnd';
import {DecksManager} from './DecksManager';
import {Card, createCardWithName, createEmptyCard} from './../Interfaces/Card';
import  './DeckBuilderPage.css';
import {useSelector} from 'react-redux';
import {CardsCollectionDisplay} from './CardsCollectionDisplay'; 
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';



const DeckBuilderPage = () => {
  const [cardsData, setCardsData] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);
  const [currentDeck, setCurrentDeck] = useState<string>("");

  useEffect(() => {
    if(currentDeck !== "" && currentDeck !== undefined) {
      fetchCardsData();
    }
  }, [currentDeck])


  const userName = useSelector<StateData, string>((state) => state.userName);
  const serverURL= useSelector<StateData, string>((state) => state.serverURL);

  const fetchCardsData = () => {
    Promise.all([
    fetch(`${serverURL}/DeckBuilder/GetAllCards/${userName}/${currentDeck}`)
      .then((res) => res.json())
      .then((cardsData: Card[]) => {
        setCardsData(cardsData);
      })
      .catch(console.error),

      fetch(`${serverURL}/DeckBuilder/GetCardsInDeck/${userName}/${currentDeck}`)
      .then((res) => res.json())
      .then((cardsInDeck: Card[]) => {
        setCardsInDeck(cardsInDeck);
      })
      .catch(console.error)
    ]);
  }

  useEffect(() => {
    const controller = new AbortController();
    fetchCardsData();
    return () => {
      controller.abort();
    };
  }, [userName]);

  const ChangeDecksState = (cardToPost: Card, PostURL: string) =>{
    fetch(PostURL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(cardToPost.name)
    }).then(() => {
      fetchCardsData();
    });
  };


  const onDragEnd = (result:DropResult) => {
    const {source, destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === source.droppableId && destination.index === source.index){return;}
    console.log(result.draggableId);

    let PostURL:string = '';

    let cardDragged: Card = createEmptyCard();
    if(destination.droppableId === "AllCards"){
      PostURL = `${serverURL}/DeckBuilder/PutCardFromDeckBack/${userName}/${currentDeck}`;
      cardDragged = cardsInDeck.find((card) => card.id === Number(result.draggableId)) || createEmptyCard();

    }
    else if(destination.droppableId === "CardsInDeck"){
      PostURL = `${serverURL}/DeckBuilder/PutCardToDeck/${userName}/${currentDeck}`
      cardDragged = cardsData.find((card) => card.id === Number(result.draggableId))|| createEmptyCard();
    }

    
    ChangeDecksState(cardDragged, PostURL); 
  }


  const sortAddableCardsBy = (criteria: string) => {
    fetch(`${serverURL}/DeckBuilder/SortAddableCardsBy/${userName}/${currentDeck}/${criteria}`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: null
    }).then((response) => response.json())
    .then((sortedCards: Card[]) => {
      setCardsData(sortedCards);
    }).catch((err) => console.log(err));
  }

  return (
    <div className="DeckBuilderPage">
      <h2>Build your deck! : Deck has to have 6-10 cards </h2>
      <h4>Left click on a deck to select</h4>
      

      <div className="Decks">

        <DragDropContext onDragEnd={onDragEnd}>
          <div>
            <h3>All cards</h3>
           <div className = "AllCards">
              <CardsCollectionDisplay Cards={cardsData}  droppableName="AllCards"></CardsCollectionDisplay>
           </div>
          </div>
          <div>
            <h3> Cards in deck : {currentDeck}</h3>
            <div className = "AllCardsInDeck">
              <CardsCollectionDisplay Cards={cardsInDeck}  droppableName="CardsInDeck"></CardsCollectionDisplay>
            </div>
          </div>
        </DragDropContext>



        <div className="PlayersDecks">
          <DecksManager  currentDeck={currentDeck} currentDeckSetter={setCurrentDeck} ></DecksManager>
          <div className="sortCriteria">
            <p >sort addable cards by 🡣</p>
            <ul>
              <li><button onClick={() => sortAddableCardsBy("points")} className="btn">Points</button></li>
              <li><button onClick={() => sortAddableCardsBy("color")} className="btn">Color</button></li>
              <li><button onClick={() => sortAddableCardsBy("name")} className="btn">Name</button></li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DeckBuilderPage;
