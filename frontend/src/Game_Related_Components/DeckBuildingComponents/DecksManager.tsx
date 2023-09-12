import React, {useState, useEffect} from 'react';
import {useSelector} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';
import './DecksManager.css';
import Modal from 'react-modal';

interface Props{
    currentDeckSetter: React.Dispatch<React.SetStateAction<string>>;
    currentDeck: string;
}

export const DecksManager: React.FC<Props> = ({ currentDeckSetter, currentDeck}) => {

  const [decksNames, setDecksNames] = useState<string[]>([]);
  const [inputNewDeckName, setNewDeckName] = useState<string>();
  const [isCreateDeckModalOpen, setIsCreateDeckModalOpen] = useState<boolean>(false);
  const [fractions, setFractions] = useState<string[]>([]);

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
    fetch(`${serverURL}/DeckBuilder/GetFractions`)
    .then((res) => res.json())
    .then((fractionsRes: string[]) => {
      setFractions(fractionsRes);
    }).catch((err) => console.log(err));
  }, [])

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

  const handleNewDeckPostRequest = (choosenFraction:string) => {
    fetch(`${serverURL}/DeckBuilder/CreateDeck/${userName}/${choosenFraction}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: inputNewDeckName
    }).then(() => {
      fetchDecksNames();
      setIsCreateDeckModalOpen(false);
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
    <div className = "decksManager">

        <div>
          <ul className="decks">
           <p style={{fontSize:'19px'}}> Your Decks:</p>
            {decksNames.map(name => (
              <li>
                <button className="deckBtn" onClick={() => (currentDeckSetter(name))}>{name}</button>
              </li>
            ))}
          </ul>
        </div>

        <div  className = "DeleteDeck">
          <div>
            <button className="btn" onClick={handleDeckDeletePostRequest}>Delete current deck</button>
          </div>
          <div>
            <button className="btn" onClick={() => setIsCreateDeckModalOpen(true)}>Create deck</button>
          </div>
        </div>


        <Modal isOpen={isCreateDeckModalOpen} style={{content: {width:'300px', height:'300px', background:'gray',},}}>
          <div className="CreateDeck">
            <p>Enter deck name, and create deck by clicking desired fraction </p>
            <input type="text" className="inputDeckName" placeholder="Enter deck name" value={inputNewDeckName} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {setNewDeckName(event.target.value)}} />
            <p>Fractions: </p>
            {fractions.map(fraction => (
              <button className="btn" onClick={() => handleNewDeckPostRequest(fraction)}>{fraction}</button>
            ))}
          </div>
        </Modal>
    </div> 
  )
}
