import React from 'react'
import {useNavigate} from "react-router-dom";
import SockJS from 'sockjs-client';
import {over} from 'stompjs';
import {useSelector, useDispatch} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';
import {Card} from './../Interfaces/Card';
import './MainPage.css';

var stompClient: any = null;
const MainPage = () => {

  const userName = useSelector<StateData, string>((state) => state.userName);
  const serverURL= useSelector<StateData, string>((state) => state.serverURL);
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


  const RedirectToDuel = () =>{
    let Sock = new SockJS(serverURL + '/ws');
    stompClient = over(Sock);
    stompClient.connect({}, onConnect);
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

  return (
    <div className="MainPageBody">
      <h1>Hello {userName}</h1>
      <button className="btn"onClick={RedirectToDeckBuilder}>Build your deck</button> <br />
      <button className="btn"onClick={RedirectToDuel}>Find enemy</button> <br />
    </div>
    
  )
}

export default MainPage