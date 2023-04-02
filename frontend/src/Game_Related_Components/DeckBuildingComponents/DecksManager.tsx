import React, {useState, useEffect} from 'react'

interface Props{
    OnDecksSwitched: () => void;
    addMessage: (message:string) => void;
}

export const DecksManager: React.FC<Props> = ({OnDecksSwitched, addMessage}) => {
  const [refresh, setRefresh] = useState(false);

  const [decksNames, setDecksNames] = useState<string[]>([]);
  const [inputNewDeckName, setNewDeckName] = useState<string>();

  const fetchDecksNames = () => {
    fetch('http://localhost:8000/DeckBuilder/GetDecksNames')
    .then((res) => res.json())
    .then((decksNames: string[]) => {
      setDecksNames(decksNames);
    })
    .catch(console.error);
    setRefresh(true);
  }

  useEffect(() => {
    fetchDecksNames();
  }, []);


  const handleSelectDeckPostRequest = (selectedDeckName: string) => {
    const response = fetch("http://localhost:8000/DeckBuilder/SelectDeck", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: selectedDeckName
    });
    OnDecksSwitched();
  }

  const handleNewDeckPostRequest = () => {

    const response = fetch("http://localhost:8000/DeckBuilder/CreateDeck", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: inputNewDeckName
    });

    fetchDecksNames();
  }

  const handleDeckDeletePostRequest = async () => {
    const response = await fetch("http://localhost:8000/DeckBuilder/DeleteDeck", {
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
