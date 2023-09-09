import React, {useState, useEffect} from 'react';
import {useSelector} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';
import './DecksManager.css';

interface Props{
    currentDeckSetter: React.Dispatch<React.SetStateAction<string>>;
    currentDeck: string;
}

export const DecksManager: React.FC<Props> = ({ currentDeckSetter, currentDeck}) => {

  const [decksNames, setDecksNames] = useState<string[]>([]);
  const [inputNewDeckName, setNewDeckName] = useState<string>();

  const userName = useSelector<StateData, string>((state) => state.userName);
  const serverURL= useSelector<StateData, string>((state) => state.serverURL);

  const fetchDecksNames = () => {
    return fetch(`${serverURL}/DeckBuilder/GetDecksNames/${userName}`)
    .then((res) => res.json())
    .then((decksNames: string[]) => {
      setDecksNames(decksNames);
    })
    .catch(console.error);
  }

  useEffect(() => {
    console.log(decksNames[0]);
    currentDeckSetter(decksNames[0]);
  }, [decksNames])

  useEffect(() => {
    const controller = new AbortController();
    fetchDecksNames();
    return () => {
      controller.abort();
    };
  }, [userName]);

  const handleNewDeckPostRequest = () => {
    fetch(`${serverURL}/DeckBuilder/CreateDeck/${userName}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: inputNewDeckName
    }).then(() => {
      return fetchDecksNames();
    });

  }

  const handleDeckDeletePostRequest = () => {
    fetch(`${serverURL}/DeckBuilder/DeleteDeck/${userName}/${currentDeck}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: "" 
    }).then(() => {
      return fetchDecksNames();
    });
  }

  return (
    <div>
        <ul className="decks">
            {decksNames.map(name => (
              <li>
                <button className="deckBtn" onClick={() => (currentDeckSetter(name))}>{name}</button>

              </li>
            ))}
          </ul>
          <div className="CreateDeck">
            <input type="text" className="inputDeckName" placeholder="Enter deck name" value={inputNewDeckName} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {setNewDeckName(event.target.value)}} />
            <button className="submitNewDeck" onClick={handleNewDeckPostRequest}>Create Deck</button>
          </div>
          <div className = "DeleteDeck">
              <button className="submitDeckDelete" onClick={handleDeckDeletePostRequest}>Delete current deck</button>
          </div>
    </div> 
  )
}
