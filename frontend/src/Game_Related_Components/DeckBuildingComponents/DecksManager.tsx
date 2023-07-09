import React, {useState, useEffect} from 'react';
import {useSelector} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';

interface Props{
    OnDecksSwitched: () => void;
    addMessage: (message:string) => void;
}

export const DecksManager: React.FC<Props> = ({OnDecksSwitched, addMessage}) => {
  const [refresh, setRefresh] = useState(false);

  const [decksNames, setDecksNames] = useState<string[]>([]);
  const [inputNewDeckName, setNewDeckName] = useState<string>();

  const userName = useSelector<StateData, string>((state) => state.userName);
  const serverURL= useSelector<StateData, string>((state) => state.serverURL);

  const fetchDecksNames = () => {
    console.log(userName);
    fetch(`${serverURL}/DeckBuilder/GetDecksNames/${userName}`)
    .then((res) => res.json())
    .then((decksNames: string[]) => {
      setDecksNames(decksNames);
    })
    .catch(console.error);
    setRefresh(true);
  }

  useEffect(() => {
    const controller = new AbortController();
    fetchDecksNames();
    return () => {
      controller.abort();
    };
  }, []);


  const handleSelectDeckPostRequest = (selectedDeckName: string) => {
    const response = fetch(`${serverURL}/DeckBuilder/SelectDeck/${userName}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: selectedDeckName
    });
    OnDecksSwitched();
  }

  const handleNewDeckPostRequest = () => {

    const response = fetch(`${serverURL}/DeckBuilder/CreateDeck/${userName}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: inputNewDeckName
    });

    fetchDecksNames();
  }

  const handleDeckDeletePostRequest = async () => {
    const response = await fetch(`${serverURL}/DeckBuilder/DeleteDeck/${userName}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: ""
    });
    addMessage(await response.text());
    fetchDecksNames();
  }

  return (
    <div>
        <ul>
            {decksNames.map(name => (
              <li>
                <button onClick={() => (handleSelectDeckPostRequest(name))}>{name}</button>
              </li>
            ))}
          </ul>
          <div className="CreateDeck">
            <input type="text" value={inputNewDeckName} onChange={(event: React.ChangeEvent<HTMLInputElement>) => {setNewDeckName(event.target.value)}} />
            <button className="submitNewDeck" onClick={handleNewDeckPostRequest}>Create Deck</button>
          </div>
          <div className = "DeleteDeck">
              <button className="submitDeckDelete" onClick={handleDeckDeletePostRequest}>Delete current deck</button>
          </div>
    </div>
  )
}
