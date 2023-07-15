import React, {useState, useEffect} from 'react';
import {Card} from './../Interfaces/Card';
import { DragDropContext, DropResult } from 'react-beautiful-dnd';
import HandComponent from './HandComponent';
import RowComponent from './RowComponent';
import './DuelPage.css';
import Modal from 'react-modal';
import CardComponent from '../CardComponent';
import {useSelector} from 'react-redux';
import StateData from './../../Game_Unrelated_Components/reactRedux/reducer';
import SockJS from 'sockjs-client';
import {over} from 'stompjs';

var stompClient:any = null;
const DuelPage = () => {
  const [refresh, setRefresh] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isRowsModalOpen, setIsRowsModalOpen] = useState(false);

  const [cardsInHand, setCardsInHand] = useState<Card[]>([]);

  const [cardsOnBoard, setCardsOnBoard] = useState<Card[]>([]);
  const [cardsOnSecondRow, setCardsOnSecondRow] = useState<Card[]>([]);
  const [cardsOnThirdRow, setCardsOnThirdRow] = useState<Card[]>([]);

  const [pointsOnBoard, setPointsOnBoard] = useState<number>(0);
  const [wonRounds, setWonRounds] = useState<number>(0);
  const [isTurnOfPlayer1, setIsTurnOfPlayer1] = useState<boolean>(false);
  const [didWon, setDidWon] = useState<boolean>(false);

  const [cardsInHand2, setCardsInHand2] = useState<Card[]>([]);

  const [cardsOnBoard2, setCardsOnBoard2] = useState<Card[]>([]);
  const [cardsOnSecondRow2, setCardsOnSecondRow2] = useState<Card[]>([]);
  const [cardsOnThirdRow2, setCardsOnThirdRow2] = useState<Card[]>([]);

  const [pointsOnBoard2, setPointsOnBoard2] = useState<number>(0);
  const [wonRounds2, setWonRounds2] = useState<number>(0);
  const [isTurnOfPlayer2, setIsTurnOfPlayer2] = useState<boolean>(false);
  const [didWon2, setDidWon2] = useState<boolean>(false);

  const [targetableCards, setTargetableCards] = useState<Card[]>([]);
  const [affectableRows, setAffectableRows] = useState<number[]>([]);
  const [enemyName, setEnemyName] = useState<string>("");



  const gameID = useSelector<StateData, string>((state) => state.gameID);
  const userName= useSelector<StateData, string>((state) => state.userName);
  const serverURL= useSelector<StateData, string>((state) => state.serverURL);




  const connectToSocket= () =>{
    let Sock = new SockJS(serverURL + '/ws');
    stompClient = over(Sock);
    stompClient.connect({}, onConnect);
  }
  const onConnect = () => {
    stompClient.subscribe('/user/' + userName + '/game', onMessageReceived );
  }
  const onMessageReceived = (payload: any) => {
    console.log("RECEIVED");
    fetchCardsData();
  }


  useEffect(() => {
    connectToSocket();
    const controller = new AbortController();
      return () => {
        controller.abort();
      };
  }, []);
  let secondPlayer:string = "second";


  const fetchData = <T,>(url: string,data: T ,setter: React.Dispatch<React.SetStateAction<T>>) => {
    fetch(url)
      .then((res) => res.json())
      .then((data: T) => {
        setter(data);
      })
      .catch(console.error);
  }


  
  const fetchCardsData = () => {
    fetch(`${serverURL}/Duel/getEnemyOf/${userName}/${gameID}`)
      .then((res) => res.text())
      .then((data: string) => {
        setEnemyName(data);
        let userEnemy:string = data;
        console.log(userEnemy);
        fetchData<Card[]>(`${serverURL}/Duel/getHandCards/${userName}/${gameID}`, cardsInHand ,setCardsInHand);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userName}/${0}/${gameID}`,cardsOnBoard ,setCardsOnBoard);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userName}/${1}/${gameID}`, cardsOnSecondRow ,setCardsOnSecondRow);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userName}/${2}/${gameID}`, cardsOnThirdRow ,setCardsOnThirdRow);
        fetchData<number>(`${serverURL}/Duel/getBoardPoints/${userName}/${gameID}`, pointsOnBoard ,setPointsOnBoard);
       fetchData<boolean>(`${serverURL}/Duel/isTurnOf/${userName}/${gameID}`, isTurnOfPlayer1 ,setIsTurnOfPlayer1);
        fetchData<number>(`${serverURL}/Duel/getWonRounds/${userName}/${gameID}`, wonRounds ,setWonRounds);
       fetchData<boolean>(`${serverURL}/Duel/didWon/${userName}/${gameID}`, didWon ,setDidWon);

        fetchData<Card[]>(`${serverURL}/Duel/getHandCards/${userEnemy}/${gameID}`, cardsInHand2 ,setCardsInHand2);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userEnemy}/${0}/${gameID}`,cardsOnBoard2 ,setCardsOnBoard2);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userEnemy}/${1}/${gameID}`, cardsOnSecondRow2 ,setCardsOnSecondRow2);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userEnemy}/${2}/${gameID}`, cardsOnThirdRow2 ,setCardsOnThirdRow2);
        fetchData<number>(`${serverURL}/Duel/getBoardPoints/${userEnemy}/${gameID}`, pointsOnBoard2 ,setPointsOnBoard2);
       fetchData<boolean>(`${serverURL}/Duel/isTurnOf/${userEnemy}/${gameID}`, isTurnOfPlayer2 ,setIsTurnOfPlayer2);
        fetchData<number>(`${serverURL}/Duel/getWonRounds/${userEnemy}/${gameID}`, wonRounds2 ,setWonRounds2);
       fetchData<boolean>(`${serverURL}/Duel/didWon/${userEnemy}/${gameID}`, didWon2 ,setDidWon2);
      })
      .catch(console.error);

    setRefresh(true);
  }



  const [cardDragged, setCardDragged] = useState<Card>({name: "points", points: 0});
  const [postOnRowNumberOf, setPostOnRowNumberOf] = useState<number>(0);
  const [playerPlayer,setPlayerPlayer] = useState<string>("none");
  const [cardAffected, setCardAffected] = useState<Card>({name: "points", points: 0});
  const handleModalClose = (card: Card) => {
    setIsModalOpen(false);
    setCardAffected(card);
  };
  useEffect(() => {
    fetch(`${serverURL}/Duel/getPossibleRowsToAffect/${gameID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(cardDragged)
    }).then(async (response) => {

      const possibleAffectedRows = await response.json();

      await makeMove(possibleAffectedRows);

    });

  }, [cardAffected]);
  

  const makeMove = async (possibleAffectedRows: number[]) => {
    if(possibleAffectedRows.length === 0) {
      playDraggedCard(`${serverURL}/Duel/playCard?userName=${userName}&affectedRow=${-1}&rowNumber=${postOnRowNumberOf}&gameID=${gameID}`, cardDragged, cardAffected);
      console.log("card played");
      fetchCardsData();
    } 
    else {
      setAffectableRows(possibleAffectedRows);
      setIsRowsModalOpen(true);
    } 

  }
  const handleRowsModalClose = (affectedRow: number) => {
    
    playDraggedCard(`${serverURL}/Duel/playCard?userName=${userName}&affectedRow=${affectedRow}&rowNumber=${postOnRowNumberOf}&gameID=${gameID}`, cardDragged, cardAffected);
    fetchCardsData();
    setIsRowsModalOpen(false);
  }


  const onDragEndOf = (result:DropResult, player:string) => {
    const {destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    setCardDragged({name: result.draggableId, points: 0});
    setPostOnRowNumberOf(-1);
    if(destination.droppableId === "BoardRow1"){
      setPostOnRowNumberOf(0);
    }
    else if(destination.droppableId === "BoardRow2"){
      setPostOnRowNumberOf(1);
    }
    else if(destination.droppableId === "BoardRow3"){
      setPostOnRowNumberOf(2);
    }
    setPlayerPlayer(userName);



  }
  useEffect(() => {
    fetch(`${serverURL}/Duel/getPossibleTargets/${userName}/${gameID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(cardDragged)
    }).then(async (response) => {

      const targetableCardsResponse= await response.json();

      await ensure(targetableCardsResponse);

    });

  }, [playerPlayer]);
  const ensure = async (targetableCardsArg:Card[]) => {

      if(targetableCardsArg.length === 0) {
        setCardAffected({name: "noCard", points:0});
      }
      else {
        setTargetableCards(targetableCardsArg);
        setIsModalOpen(true);
      }
  }


  const playDraggedCard = async (postURL: string, cardDragged:Card, cardTargetted:Card) =>{
    const args = [cardDragged, cardTargetted];
    await fetch(postURL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(args)
      }).then( () => {
        stompClient.send('/app/sendTrigger', {}, userName);
        console.log("SENT");
      });
  }

  const endRoundFor = (player:string) => {
    fetch(`${serverURL}/Duel/endRound/${player}/${gameID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: null
    });
}

  return (
    
    <div>
      <div>
        <label>Let the battle begin</label>
        <button onClick={fetchCardsData}>Load data</button>
      </div>

      <div>
        <button onClick={() => endRoundFor(userName)}>End round</button>
        <label>|you: {userName}|</label>
        <label> |Did you won: {didWon.toString()}|</label>
        <label> |Won rounds: {wonRounds}| </label>
        <label> |Is your turn: {isTurnOfPlayer1.toString()}| </label>
      </div>
      
      <Modal isOpen={isModalOpen} onRequestClose={() => handleModalClose({name: "Not", points: 1})}style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose a card to target</h2>
        {targetableCards.map((card, index) =>(
          <button onClick= { () => {handleModalClose(card)} }><CardComponent color={'yellow'} image={'none'} name={card.name} points={card.points}></CardComponent></button>
        ))}
      </Modal>
      <Modal isOpen={isRowsModalOpen} onRequestClose={() => handleRowsModalClose(-1)} style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose a row to traget</h2>
        {affectableRows.map((row, index) =>(
          <button style={{fontSize: '30px',}} onClick= { () => {handleRowsModalClose(row)} }>{row + 1}</button>
        ))}
      </Modal>

      <DragDropContext onDragEnd = {(result) => onDragEndOf(result, userName)}>
        <HandComponent cardsInHand = {cardsInHand}></HandComponent>

        <RowComponent cardsOnRow = {cardsOnThirdRow} pointsOnRow={pointsOnBoard} rowDroppableId={"BoardRow3"}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnSecondRow} pointsOnRow={pointsOnBoard} rowDroppableId={"BoardRow2"}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnBoard} pointsOnRow={pointsOnBoard} rowDroppableId={"BoardRow1"}></RowComponent>
      </DragDropContext>  
        
      
      <DragDropContext onDragEnd = {(result) => onDragEndOf(result, secondPlayer)}>
        <RowComponent cardsOnRow = {cardsOnBoard2} pointsOnRow={pointsOnBoard2} rowDroppableId={"BoardRow1"}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnSecondRow2} pointsOnRow={pointsOnBoard2} rowDroppableId={"BoardRow2"}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnThirdRow2} pointsOnRow={pointsOnBoard2} rowDroppableId={"BoardRow3"}></RowComponent>
        
        {/* <HandComponent cardsInHand = {cardsInHand2}></HandComponent> */}
      </DragDropContext>
      
      
      <div>
        {/* <button onClick={() => endRoundFor(secondPlayer)}>End round</button> */}
        <label>|enemy: {enemyName}|</label>
        <label> |Did won: {didWon2.toString()}|</label>
        <label> |Won rounds: {wonRounds2}| </label>
        <label> |Is of: {isTurnOfPlayer2.toString()}| </label>
      </div>
    </div>
  )
}

export default DuelPage