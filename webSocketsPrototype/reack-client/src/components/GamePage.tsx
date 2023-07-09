import React, {useState, useEffect} from 'react'
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import {useSelector} from 'react-redux';
import StateData from '../nameReducer';

var stompClient: any = null;
const GamePage = () => {

    const [points, setPoints] = useState<number>(0);
    const [enemyPoints, setEnemyPoints] = useState<number>(0);
    const userName= useSelector<StateData, string>((state) =>state.userName);
    const serverURL= useSelector<StateData, string>((state) =>state.serverURL);
    const gameID= useSelector<StateData, string>((state) =>state.gameID);

    useEffect(() => {
        //Initialize
        connectToServer();

    }, []);

  const connectToServer= () => {
    let Sock=new SockJS(serverURL + '/ws');
    stompClient = over(Sock);
    stompClient.connect({}, onConnected);

  }
  const onConnected = () => {
    stompClient.subscribe('/user/' + userName + '/game', onTriggerRecieved);
  }
  const onTriggerRecieved = (payload: any) => {
      let enemyName = payload.body.split(":")[1]; 
      fetch(serverURL + `/button/getPoints/${enemyName}/${gameID}`)
      .then((res) => res.json())
      .then((data:number ) => {
        setEnemyPoints(data);
      }).catch(console.error);

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
      stompClient.send('/app/sendTrigger', {}, userName);

  }


  return (
    <div>
        <div>{userName}</div>
        <button onClick={increase}>Click</button>
        <p>You points: {points}</p>
        <p>Enemy points: {enemyPoints}</p>

    </div>
  )
}

export default GamePage