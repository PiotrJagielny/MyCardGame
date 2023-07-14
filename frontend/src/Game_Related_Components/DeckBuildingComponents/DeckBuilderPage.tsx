import React, { useState, useEffect } from 'react';
import { DragDropContext, DropResult } from 'react-beautiful-dnd';
import {AllCardsDisplay} from './AllCardsDisplay';
import {CardsInDeckDisplay} from './CardsInDeckDisplay';
import {DecksManager} from './DecksManager';
import {Card} from './../Interfaces/Card';
import {MessagesComponent} from './../../Game_Unrelated_Components/UIComponents/MessagesComponent';
import  './DeckBuilderPage.css';
import {useSelector} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';



const DeckBuilderPage = () => {
  const [refresh, setRefresh] = useState(false);
  const [messages, setMessages] = useState<string[]>([]);
  const [cardsData, setCardsData] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);


  const userName = useSelector<StateData, string>((state) => state.userName);
  const serverURL= useSelector<StateData, string>((state) => state.serverURL);

  const fetchCardsData = () => {
    fetch(`${serverURL}/DeckBuilder/GetAllCards/${userName}`)
      .then((res) => res.json())
      .then((cardsData: Card[]) => {
        setCardsData(cardsData);
      })
      .catch(console.error);

      fetch(`${serverURL}/DeckBuilder/GetCardsInDeck/${userName}`)
      .then((res) => res.json())
      .then((cardsInDeck: Card[]) => {
        setCardsInDeck(cardsInDeck);
      })
      .catch(console.error);

      setRefresh(true);
  }

  useEffect(() => {
    const controller = new AbortController();
    fetchCardsData();
    return () => {
      controller.abort();
    };
  }, [userName]);

  const ChangeDecksState = async (cardToPost: Card, PostURL: string) =>{
    let data = {name: cardToPost.name};
    const response = await fetch(PostURL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data.name)
    });

    
    addMessage(await response.text());

    if(!response.ok){
      throw new Error('Failed to change deck state');
    }

    fetchCardsData();
  };

  const addMessage = (message: string) => {
    messages.push(message);
    setMessages(messages);
    setRefresh(true);
  }

  const onDragEnd = (result:DropResult) => {
    const {source, destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === source.droppableId && destination.index === source.index){return;}

    let PostURL:string = '';

    if(destination.droppableId === "AllCards"){
      PostURL = `${serverURL}/DeckBuilder/PutCardFromDeckBack/${userName}`;
    }
    else if(destination.droppableId === "CardsInDeck"){
      PostURL = `${serverURL}/DeckBuilder/PutCardToDeck/${userName}`
    }

    let cardDragged: Card = {name: result.draggableId, points: 0};
    
    ChangeDecksState(cardDragged, PostURL); 
  }

  const handleDecksSwitch = () => {
    fetchCardsData();
  }

  return (
    <div className="DeckBuilderPage">
      <h2>DeckBuilderPage : {userName}</h2>

      

      <div className="Decks">

        <DragDropContext onDragEnd={onDragEnd}>
          <div className = "AllCards">
            <AllCardsDisplay Cards={cardsData} refresh={refresh}></AllCardsDisplay>
          </div>
          <div className = "AllCardsInDeck">
            <CardsInDeckDisplay Cards={cardsInDeck} refresh={refresh}></CardsInDeckDisplay>
          </div>
        </DragDropContext>
        <div className="PlayersDecks">
          <DecksManager OnDecksSwitched={handleDecksSwitch} addMessage={addMessage}></DecksManager>
        </div>
      </div>
      <div className="Messages">
        <MessagesComponent Messages = {messages} refresh={refresh}></MessagesComponent>
      </div>
    </div>
  );
};

export default DeckBuilderPage;
