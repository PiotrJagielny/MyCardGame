import React, {useState} from 'react';
import SockJS from 'sockjs-client';
import {over} from 'stompjs';
import GamePage from './GamePage';
import {useDispatch, useSelector} from 'react-redux';
import {useNavigate} from "react-router-dom";


var serverURL: string = 'http://localhost:8082';
var stompClient: any = null;
var gameID:string= "";
const SearchingPage = () => {
    const [userName, setUserName] = useState<string>("");
    const [isConnected, setIsConnected] = useState<boolean>(false);


    const dispatch = useDispatch();

    let navigate = useNavigate();


  const connectToServer= () => {
    dispatch({type:"CHANGE_NAME", payload: userName});
    dispatch({type:"SET_SERVER_URL", payload: serverURL});
    let Sock=new SockJS(serverURL + '/ws');
    stompClient = over(Sock);
    stompClient.connect({}, onConnected);

  }
  const onConnected = () => {
    setIsConnected(true);
    stompClient.subscribe('/user/' + userName + '/private', onMessageReceived);

  }

  const onMessageReceived = (payload:any) => {

    if(payload.body.includes("Found enemy") ) {

      gameID = payload.body.split(":")[1]; 
      dispatch({type:"SET_GAME_ID", payload: gameID});
      let message:string = userName + ":" + gameID;
      fetch(serverURL + '/button', {
        method: 'PUT',
        headers: {'Content-Type': 'text/plain',},
        body: message,
      });
      navigate("/game");
    }
  }


  const startSearching = () => {
    stompClient.send('/app/findEnemy', {}, userName);
  }


  return (
    <div className="App">
      <div>
      {!isConnected? <div>connect to search</div>: <button onClick={startSearching}>search for opponent</button>}
      <div>
        {isConnected? <div> </div>:
        <div>
         <input id='user-name' placeholder='enter user name' value={userName} name='username' onChange={(event:any) => {setUserName(event.target.value)}}/>
         <button onClick={connectToServer}>connect to server</button>
        </div>
        }
      </div>

      </div>


    </div>
  );
}


export default SearchingPage



