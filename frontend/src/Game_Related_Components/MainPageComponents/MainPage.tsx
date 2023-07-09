import React from 'react'
import {useNavigate} from "react-router-dom";
import SockJS from 'sockjs-client';
import {over} from 'stompjs';
import {useSelector, useDispatch} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';


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
      });
    navigate("/DeckBuilder");
  }


  const RedirectToDuel = () =>{
    let Sock = new SockJS(serverURL + '/ws');
    stompClient = over(Sock);
    stompClient.sonnect({}, onConnect);

    navigate("/Duel");
  }
  const onConnect = () => {
    stompClient.subscribe('/user/' + userName + '/private', onMessageReceived );
  }
  const onMessageReceived = (payload: any) => {
    if(payload.body.includes("Found enemy") ) {

      let gameID = payload.body.split(":")[1]; 
      dispatch({type:"SET_GAME_ID", payload: gameID});
      let message:string = userName + ":" + gameID;
      fetch(serverURL + '/button', {
        method: 'PUT',
        headers: {'Content-Type': 'text/plain',},
        body: message,
      });
      navigate("/Duel");
    }
  }

  return (
    <div>
      <h1>Hello {userName}</h1>
      This is game main page <br />
      <button onClick={RedirectToDeckBuilder}>Build your deck</button> <br />
      <button onClick={RedirectToDuel}>Duel</button> <br />
    </div>
    
  )
}

export default MainPage