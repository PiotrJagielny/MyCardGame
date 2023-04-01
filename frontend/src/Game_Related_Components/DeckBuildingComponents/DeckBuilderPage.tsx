import React, { useState, useEffect } from 'react';
import { DragDropContext, Droppable, Draggable, DropResult } from 'react-beautiful-dnd';
import  './DeckBuilderPage.css';

interface Card {
  name: string;
}

let Messages: string[] = [];

const DeckBuilderPage = () => {
  const [cardsData, setCardsData] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);
  const [decksNames, setDecksNames] = useState<string[]>([]);
  const [inputNewDeckName, setNewDeckName] = useState<string>();


  useEffect(() => {
    fetch('http://localhost:8000/DeckBuilder/GetDecksNames')
      .then((res) => res.json())
      .then((decksNames: string[]) => {
        setDecksNames(decksNames);
      })
      .catch(console.error);
  }, []);

  useEffect(() => {
    fetch('http://localhost:8000/DeckBuilder/GetAllCards')
      .then((res) => res.json())
      .then((cardsData: Card[]) => {
        setCardsData(cardsData);
      })
      .catch(console.error);
  }, []);

  useEffect(() => {
    fetch('http://localhost:8000/DeckBuilder/GetCardsInDeck')
      .then((res) => res.json())
      .then((cardsInDeck: Card[]) => {
        setCardsInDeck(cardsInDeck);
      })
      .catch(console.error);
  }, []);




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

  const handleNewDeckPostRequest = () => {

    const response = fetch("http://localhost:8000/DeckBuilder/CreateDeck", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: inputNewDeckName
    });

    fetch('http://localhost:8000/DeckBuilder/GetDecksNames')
      .then((res) => res.json())
      .then((decksNames: string[]) => {
        setDecksNames(decksNames);
      })
      .catch(console.error);

  }

  const handleSelectDeckPostRequest = (selectedDeckName: string) => {
    const response = fetch("http://localhost:8000/DeckBuilder/SelectDeck", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: selectedDeckName
    });

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

  return (
    <div className="DeckBuilderPage">
      <h2>DeckBuilderPage</h2>

      

      <div className="Decks">

        <DragDropContext onDragEnd={onDragEnd}>
          <Droppable droppableId="AllCards">
            {(provided) => (
              <div className="AllCards" ref={provided.innerRef} {...provided.droppableProps}>
                <p>All Cards:</p>
                <ul>
                  {cardsData.map((item, index) => (
                    <Draggable key={item.name} draggableId={item.name} index={index}>
                      {(provided) => (
                        <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef}>
                          <div>{item.name} </div>
                          <br />
                        </li>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
                </ul>
              </div>
            )}
          </Droppable>

          <Droppable droppableId="CardsInDeck">
            {(provided) => (
              <div className="AllCardsInDeck" ref={provided.innerRef} {...provided.droppableProps}>
                <p>Cards In Your Deck:</p>
                <ul>
                  {cardsInDeck.map((item, index) => (
                    <Draggable key={item.name} draggableId={item.name} index={index}>
                      {(provided) => (
                        <li {...provided.draggableProps} {...provided.dragHandleProps} ref={provided.innerRef} className="ggg">
                          <div>{item.name} </div>
                          <br />
                        </li>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
                </ul>
              </div>
            )}
          </Droppable>

        </DragDropContext>
        <div className="PlayersDecks">
          <ul>
            {decksNames.map(name => (
              <li>
                <button onClick={() => handleSelectDeckPostRequest(name)}>{name}</button>
              </li>
            ))}
          </ul>
          <div className="CreateDeck">
            <input type="text" value={inputNewDeckName} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {setNewDeckName(event.target.value)}} />
            <button className="submitNewDeck" onClick={handleNewDeckPostRequest}>Create Deck</button>
          </div>
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
