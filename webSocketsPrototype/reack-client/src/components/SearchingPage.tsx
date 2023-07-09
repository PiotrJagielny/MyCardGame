import React, {useState} from 'react';
import SockJS from 'sockjs-client';
import {over} from 'stompjs';
import GamePage from './GamePage';
import {useDispatch, useSelector} from 'react-redux';





interface message {
  receiver:string;
  sender:string;
  message:string;
}


var serverURL: string = 'http://localhost:8082';
var stompClient: any = null;
var gameID:string= "";
const SearchingPage = () => {
    const [userName, setUserName] = useState<string>("");
    const [isConnected, setIsConnected] = useState<boolean>(false);
    const [isEnemyFound, setIsEnemyFound] = useState<boolean>(false);
    const [points, setPoints] = useState<number>(0);
    const [enemyPoints, setEnemyPoints] = useState<number>(0);

    const reduxUserName = useSelector<string, string>((state) =>state);


    const dispatch = useDispatch();



  const connectToServer= () => {
    dispatch({type:"CHANGE_NAME", payload: userName});
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
      let message:string = userName + ":" + gameID;
      setIsEnemyFound(true);
      fetch(serverURL + '/button', {
        method: 'PUT',
        headers: {'Content-Type': 'text/plain',},
        body: message,
      });
    }
    else if(payload.body.includes("Get data from server")) {
      let enemyName = payload.body.split(":")[1]; 
      fetch(serverURL + `/button/getPoints/${enemyName}/${gameID}`)
      .then((res) => res.json())
      .then((data:number ) => {
        setEnemyPoints(data);
      }).catch(console.error);
    }
  }


  const startSearching = () => {
    stompClient.send('/app/findEnemy', {}, userName);
  }

  const increase = () => {

      let message:string = userName + ":" + gameID;
      fetch(serverURL + '/button', {
        method: 'POST',
        headers: {'Content-Type': 'text/plain',},
        body: message,
      });
      fetch(serverURL + `/button/getPoints/${userName}/${gameID}`)
      .then((res) => res.json())
      .then((data:number ) => {
        setPoints(data);
      }).catch(console.error);
      stompClient.send('/app/sendToEnemy', {}, userName);

  }

  return (
    <div className="App">
      {isEnemyFound?
      <div>
        <GamePage userName={userName}  gameID={gameID} serverURL={serverURL}></GamePage>
      </div>
      :
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
      }


    </div>
  );
}


export default SearchingPage



