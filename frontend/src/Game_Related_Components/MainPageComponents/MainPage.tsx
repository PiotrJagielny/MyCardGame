import React,{useState, useEffect} from 'react'
import {useNavigate} from "react-router-dom";
import {useSelector, useDispatch} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';
import {Card} from './../Interfaces/Card';
import './MainPage.css';
import Modal from 'react-modal';

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
    fetch(`${serverURL}/DeckBuilder/ValidateDeck/${userName}/${deck}`, 
    {headers: {'Access-Control-Allow-Origin' : '*'}}
    )
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
      //I disabled web sockets to be able to test duel with yourself
      // const socket = new WebSocket('wss://63mgnuyfr6.execute-api.eu-north-1.amazonaws.com/production/');
      // dispatch({type:"CONNECT_TO_SOCKET", payload: "", createdSocket: socket});
      // socket.addEventListener('open', e => {
      //   console.log('WebSocket is connected');
      //   sendMessage("findEnemy", socket);
      // })
      // socket.addEventListener('close', e => {
      //   console.log('WebSocket is disconnected');
      // })
      // socket.addEventListener('message', e => {
      //   let msg:string  = JSON.parse(e.data).message;
      //   if(msg.includes("gameid:")){
      //     registerUser(msg,"enemyFound", socket);
      //   }
      //   else if(msg.includes("to_duel_page")){
      //     console.log("to duel page");
      //     registerUser(msg,"getIntoDuelPage", socket);
      //   }
      //   else if(msg.includes("Go into duel page")) {
      //     navigate("/Duel");
      //   }
      // })

      // setIsSearching(true);
      registerUser(userName);
      setIsModalOpen(false);
    }
  }, [chosenDeck])

  const registerUser = (payload:string) => {
      let gameID = payload; 
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
            navigate("/Duel");
          });
      });

  }






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