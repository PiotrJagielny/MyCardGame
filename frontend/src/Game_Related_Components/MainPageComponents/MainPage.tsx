import React,{useState} from 'react'
import {useNavigate} from "react-router-dom";
import SockJS from 'sockjs-client';
import {over} from 'stompjs';
import {useSelector, useDispatch} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';
import {Card} from './../Interfaces/Card';
import './MainPage.css';
import Modal from 'react-modal';

var stompClient: any = null;
const MainPage = () => {

  const userName = useSelector<StateData, string>((state) => state.userName);
  const serverURL= useSelector<StateData, string>((state) => state.serverURL);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [decks, setDecks] = useState<string[]>([]);
  let navigate = useNavigate();
  let dispatch = useDispatch();
  const RedirectToDeckBuilder = () =>{

      fetch(serverURL + '/DeckBuilder/setupBuilder', {
        method: 'POST',
        headers: {'Content-Type': 'text/plain',},
        body: userName,
      }).then(() => {
        navigate("/DeckBuilder");
      });
  }


  const handleModalClose= (deck:string) => {
    fetch(`${serverURL}/DeckBuilder/ValidateDeck/${userName}/${deck}`)
    .then((res) => res.json())
    .then((isDeckValid: boolean) => {
      if(isDeckValid) {
        switchDecks(deck);
      }
      else {
        window.alert("This deck is not valid");
      }
    }).catch(console.error);

  }
  const switchDecks = (deck:string) => {
      fetch(`${serverURL}/DeckBuilder/SelectDeck/${userName}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body:deck 
      }).then(() => {
        let Sock = new SockJS(serverURL + '/ws');
        stompClient = over(Sock);
        stompClient.connect({}, onConnect);
        setIsSearching(true);
        setIsModalOpen(false);
      });

  }
  const RedirectToDuel = () =>{
    fetch(`${serverURL}/DeckBuilder/GetDecksNames/${userName}`)
    .then((res) => res.json())
    .then((decksNames: string[]) => {
      setDecks(decksNames);
    }).then(() => {
      setIsModalOpen(true);
    })
    .catch(console.error);
  }
  const onConnect = () => {
    stompClient.subscribe('/user/' + userName + '/private', onMessageReceived );
    stompClient.send('/app/findEnemy', {}, userName);
  }
  const onMessageReceived = (payload: any) => {
    if(payload.body.includes("Found enemy") ) {

      let gameID = payload.body.split(":")[1]; 
      dispatch({type:"SET_GAME_ID", payload: gameID});

      fetch(`${serverURL}/DeckBuilder/GetCardsInDeck/${userName}`)
        .then((res) => res.json())
        .then((deckData: Card[]) => {

          fetch(`${serverURL}/Duel/registerUser/${userName}/${gameID}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(deckData),
          }).then(() => {
          });
        })
        .catch(console.error);
    }
    else if(payload.body.includes("Get into duel page")) {
      navigate("/Duel");
    }
  }

  const [isSearching, setIsSearching] = useState<boolean>(false);


  return (
    <div className="MainPageBody">
      <Modal isOpen={isModalOpen} onRequestClose={() => handleModalClose("")} style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose deck</h2>
        {decks.map((deck, index) =>(
          <button style={{fontSize: '30px',}} onClick= { () => handleModalClose(deck) }>{deck}</button>
        ))}
      </Modal>
      <h1>Hello {userName}</h1>
      <button className="btn" onClick={RedirectToDeckBuilder}>Build your deck</button> <br />
      <button className="btn"onClick={RedirectToDuel}>Find enemy</button> <br />
      {isSearching && <label className="spinner"></label>}
    </div>
    
  )
}

export default MainPage