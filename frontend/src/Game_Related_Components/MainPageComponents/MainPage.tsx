import React,{useState, useEffect} from 'react'
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
  const [chosenDeck, setChosenDeck] = useState<string>("");
  let navigate = useNavigate();
  let dispatch = useDispatch();
  const RedirectToDeckBuilder = () =>{

      fetch(serverURL + '/DeckBuilder/setupBuilder', {
        method: 'POST',
        headers: {'Content-Type': 'text/plain','Access-Control-Allow-Origin': '*'},
        body: userName,
      }).then(() => {
        navigate("/DeckBuilder");
      });
  }


  const handleModalClose= (deck:string) => {
    fetch(`${serverURL}/DeckBuilder/ValidateDeck/${userName}/${deck}`, {headers: {'Access-Control-Allow-Origin' : '*'}})
    .then((res) => res.json())
    .then((isDeckValid: boolean) => {
      if(isDeckValid) {
        setChosenDeck("");
        setChosenDeck(deck);
      }
      else {
        window.alert("This deck is not valid");
      }
    }).catch(console.error);

  }
  useEffect(() => {
    if(chosenDeck !== "") {
      console.log(chosenDeck);
      let Sock = new SockJS(serverURL + '/ws');
      stompClient = over(Sock);
      stompClient.connect({}, onConnect);
      setIsSearching(true);
      setIsModalOpen(false);
    }
  }, [chosenDeck])


  const RedirectToDuel = () =>{
    fetch(`${serverURL}/DeckBuilder/GetDecksNames/${userName}`, {headers: {'Access-Control-Allow-Origin' : '*'}})
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
    stompClient.subscribe('/user/' + userName + '/registerAfterEnemy', registerAfterEnemy);
    stompClient.send('/app/findEnemy', {}, userName);
  }
  const registerAfterEnemy = async(payload: any) => {
      registerUser(payload, "getIntoDuelPage");

  }
  const onMessageReceived = async (payload: any) => {
    if(payload.body.includes("Found enemy") ) {
      registerUser(payload, "enemyFound");
    }
    else if(payload.body.includes("Get into duel page")) {
      navigate("/Duel");
    }
  }

  const registerUser = (payload:any, topicToSendMessageTo: string) => {
      let gameID = payload.body.split(":")[1]; 
      dispatch({type:"SET_GAME_ID", payload: gameID});
      console.log(chosenDeck);

      Promise.all([
        new Promise((res, rej) => {
          fetch(`${serverURL}/DeckBuilder/GetCardsInDeck/${userName}/${chosenDeck}`, {headers: {'Access-Control-Allow-Origin' : '*'}})
            .then((res) => res.json())
            .then((deckData: Card[]) => {
              res(deckData);
            }).catch((err) => {
              console.log(err);
              rej(err);
            })
        }),
        new Promise((res, rej) => {
          fetch(`${serverURL}/DeckBuilder/GetDeckFraction/${userName}/${chosenDeck}`, {headers: {'Access-Control-Allow-Origin' : '*'}})
            .then((res) => res.text())
            .then((deckFraction: string) => {
              res(deckFraction);
            }).catch((err) => {
              console.log(err);
              rej(err);
            })
        })  
      ]).then((values) => {
        console.log(values);
          fetch(`${serverURL}/Duel/registerUser/${userName}/${gameID}/${values[1]}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Access-Control-Allow-Origin' : '*'},
            body: JSON.stringify(values[0]),
          }).then(() => {
            console.log("user registered-----------------------------------");
            stompClient.send(`/app/${topicToSendMessageTo}`, {}, userName);
            // stompClient.send(`/app/enemyFound`, {}, userName);
            
          });
      });

  }

  const [isSearching, setIsSearching] = useState<boolean>(false);


  return (
    <div className="MainPageBody">
      <Modal isOpen={isModalOpen} onRequestClose={() => handleModalClose("")} style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose deck</h2>
        {decks.map((deck, index) =>(
          <button className="btn" onClick= { () => handleModalClose(deck) }>{deck}</button>
        ))}
        <br/>
        <br/>
        <br/>
        <div>
          <button className="btn" onClick={() => setIsModalOpen(false)}>Close</button>
        </div>
      </Modal>
      <h1>Hello {userName}</h1>
      <button className="btn" onClick={RedirectToDeckBuilder}>Build your deck</button> <br />
      <button className="btn"onClick={RedirectToDuel}>Find enemy</button> <br />
      {isSearching && <label className="spinner"></label>}
    </div>
    
  )
}

export default MainPage