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
import {useNavigate} from "react-router-dom";

var stompClient:any = null;
var firstRow: number = 0;
var secondRow: number = 1;
var thirdRow: number = 2;
var rowStatusToImageUrl: Map<string,string> = new Map<string,string>([
  ["", ""],
  ["Rain", "https://parspng.com/wp-content/uploads/2022/06/rainpng.parspng.com-4.png"],
]);
const DuelPage = () => {

  let navigate = useNavigate();
  const [refresh, setRefresh] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isRowsModalOpen, setIsRowsModalOpen] = useState(false);

  const [cardsInHand, setCardsInHand] = useState<Card[]>([]);

  const [cardsOnBoard, setCardsOnBoard] = useState<Card[]>([]);
  const [cardsOnSecondRow, setCardsOnSecondRow] = useState<Card[]>([]);
  const [cardsOnThirdRow, setCardsOnThirdRow] = useState<Card[]>([]);

  const [pointsOnRows, setPointsOnRows] = useState<number[]>([]);
  const [rowsStatus, setRowsStatus] = useState<string[]>([]);
  const [wonRounds, setWonRounds] = useState<number>(0);
  const [isTurnOfPlayer1, setIsTurnOfPlayer1] = useState<boolean>(false);
  const [didWon, setDidWon] = useState<boolean>(false);


  const [enemyCardsOnFirstRow, setenemyCardsOnFirstRow] = useState<Card[]>([]);
  const [enemyCardsOnSecondRow, setenemyCardsOnSecondRow] = useState<Card[]>([]);
  const [enemyCardsOnThirdRow, setCardsOnThirdRow2] = useState<Card[]>([]);

  const [enemyPointsOnBoard, setenemyPointsOnBoard] = useState<number>(0);
  const [enemyPointsOnRows, setEnemyPointsOnRows] = useState<number[]>([]);
  const [enemyWonRounds, setenemyWonRounds] = useState<number>(0);
  const [isEnemyTurn, setisEnemyTurn] = useState<boolean>(false);
  const [didEnemyWon, setdidEnemyWon] = useState<boolean>(false);
  const [enemyRowsStatus, setEnemyRowsStatus] = useState<string[]>([]);

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
    stompClient.subscribe('/user/' + userName + '/enemyEndRound', enemyEndRound);
  }
  const onMessageReceived = (payload: any) => {
    fetchCardsData();
  }


  useEffect(() => {
    connectToSocket();
    const controller = new AbortController();
      return () => {
        controller.abort();
      };
  }, []);


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
        fetchData<Card[]>(`${serverURL}/Duel/getHandCards/${userName}/${gameID}`, cardsInHand ,setCardsInHand);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userName}/${0}/${gameID}`,cardsOnBoard ,setCardsOnBoard);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userName}/${1}/${gameID}`, cardsOnSecondRow ,setCardsOnSecondRow);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userName}/${2}/${gameID}`, cardsOnThirdRow ,setCardsOnThirdRow);
        fetchData<boolean>(`${serverURL}/Duel/isTurnOf/${userName}/${gameID}`, isTurnOfPlayer1 ,setIsTurnOfPlayer1);
        fetchData<number>(`${serverURL}/Duel/getWonRounds/${userName}/${gameID}`, wonRounds ,setWonRounds);
        fetchData<boolean>(`${serverURL}/Duel/didWon/${userName}/${gameID}`, didWon ,setDidWon);
        fetchData<number[]>(`${serverURL}/Duel/getRowsPoints/${userName}/${gameID}`, pointsOnRows,setPointsOnRows);
        fetchData<string[]>(`${serverURL}/Duel/getRowsStatus/${userName}/${gameID}`, rowsStatus,setRowsStatus);

        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userEnemy}/${0}/${gameID}`,enemyCardsOnFirstRow ,setenemyCardsOnFirstRow);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userEnemy}/${1}/${gameID}`, enemyCardsOnSecondRow,setenemyCardsOnSecondRow);
        fetchData<Card[]>(`${serverURL}/Duel/getCardsOnRow/${userEnemy}/${2}/${gameID}`, enemyCardsOnThirdRow ,setCardsOnThirdRow2);
        fetchData<boolean>(`${serverURL}/Duel/isTurnOf/${userEnemy}/${gameID}`, isEnemyTurn ,setisEnemyTurn);
        fetchData<number>(`${serverURL}/Duel/getWonRounds/${userEnemy}/${gameID}`, enemyWonRounds ,setenemyWonRounds);
        fetchData<boolean>(`${serverURL}/Duel/didWon/${userEnemy}/${gameID}`, didEnemyWon ,setdidEnemyWon);
        fetchData<number[]>(`${serverURL}/Duel/getRowsPoints/${userEnemy}/${gameID}`, enemyPointsOnRows,setEnemyPointsOnRows);
        fetchData<string[]>(`${serverURL}/Duel/getRowsStatus/${userEnemy}/${gameID}`, enemyRowsStatus,setEnemyRowsStatus);

      }).then(() => {
        if(wonRounds === enemyWonRounds && wonRounds === 2) {
          alert("Draw","https://c4.wallpaperflare.com/wallpaper/103/477/186/forest-light-nature-forest-wallpaper-preview.jpg" );
        }
        else if(wonRounds === 2) {
          alert("You won!","https://png.pngtree.com/thumb_back/fh260/background/20220523/pngtree-stage-podium-with-rays-of-spotlights-for-award-ceremony-winner-with-image_1400291.jpg" );
        }
        else if(enemyWonRounds === 2) {
          alert("You lost!","https://c4.wallpaperflare.com/wallpaper/33/477/228/rain-showers-forest-illustration-wallpaper-preview.jpg" );
        }

      })
      .catch(console.error);

    setRefresh(true);
  }
  const alert= (msg:string, imageURL:string) => {
    const alert = document.createElement('div');
    alert.classList.add('alert');
    const alertButton = document.createElement('button');
    alertButton.innerText = 'Back to main menu';
    alert.setAttribute('style', `
      position: fixed;
      top: 30%;
      left:50%;
      padding:20px;
      border-radius: 10px;
      box-shadow: 0 10px 5px 0 #00000022; 
      display:flex;
      flex-direction:column;
      background-image: url(${imageURL});
      background-size: cover;
      background-position: center;
      height: 200px;
      width: 200px;
    `);
    alertButton.setAttribute('style', `
      border: 1px solidd #333;
      background:white;
      border-radius: 5px;
      padding: 5px;
    
    `);
    alert.innerHTML= `<span style="
      font-size: 20px;
      padding: 29%;
      padding-left: 59px;
      ">
     ${msg}
     </span>`;
    alert.appendChild(alertButton);
    alertButton.addEventListener('click',(e) => {
      alert.remove();
      navigate("/Main");
    });
    document.body.appendChild(alert);
  }



  const [cardDragged, setCardDragged] = useState<Card>({name: "", points: 0});
  const [playChainCard, setPlayChainCard] = useState<Card>({name: "", points: 0});
  const [postOnRowNumberOf, setPostOnRowNumberOf] = useState<number>(0);
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
      playDraggedCard(`${serverURL}/Duel/playCard?userName=${userName}&affectedRow=${-1}&rowNumber=${postOnRowNumberOf}&gameID=${gameID}`,  cardAffected);
      fetchCardsData();
    } 
    else {
      setAffectableRows(possibleAffectedRows);
      setIsRowsModalOpen(true);
    } 

  }
  const handleRowsModalClose = (affectedRow: number) => {
    playDraggedCard(`${serverURL}/Duel/playCard?userName=${userName}&affectedRow=${affectedRow}&rowNumber=${postOnRowNumberOf}&gameID=${gameID}`,  cardAffected);
    fetchCardsData();
    setIsRowsModalOpen(false);
  }


  const onDragEndOf = (result:DropResult, player:string) => {
    const {destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

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
    setCardDragged({name: result.draggableId, points: 0});



  }
  useEffect( () => {
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

  }, [cardDragged])

  const ensure = async (targetableCardsArg:Card[]) => {

      if(targetableCardsArg.length === 0) {
        setCardAffected({name: "noCard", points:0});
      }
      else {
        setTargetableCards(targetableCardsArg);
        setIsModalOpen(true);
      }
  }


  const playDraggedCard = async (postURL: string, cardTargetted:Card) =>{
    const args = [cardDragged, cardTargetted];
    fetch(postURL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(args)
      }).then((res) => res.json()).then( (cardChained: Card) => {
        setPlayChainCard(cardChained);
        stompClient.send('/app/sendTrigger', {}, userName);
      });
  }

  const endRoundFor = (player:string) => {
    fetch(`${serverURL}/Duel/endRound/${player}/${gameID}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: null
    }).then( () => {
        stompClient.send('/app/sendTrigger', {}, userName);
    });
  }
  const renderWonRounds = (wonRoudnsOfPlayer: number) => {
    const wonRoundsDivs = [];
    for(let i = 0 ; i < wonRoudnsOfPlayer; i++) {
      wonRoundsDivs.push(<div key={i}><img src="https://cdn-icons-png.flaticon.com/512/6941/6941697.png" style={{width: 30, height: 30}} alt=""/></div>)
    }
    if(wonRoundsDivs.length === 0) {
      wonRoundsDivs.push(<div style={{width: 30, height: 30}} > </div>)
    }
    return wonRoundsDivs;
  } 

  return (
    
    <div>
      <div className="playerName">{userName} : {pointsOnRows.reduce((sum, e) => sum + e, 0)}</div>
      <div className="playerInfo">
        {isTurnOfPlayer1?
        <div>
          <label >Your turn</label>
          <img src="https://cdn0.iconfinder.com/data/icons/crime-protection-people-rounded/110/Sword-512.png" style={{width: 70, height: 70}} alt=""/>
        </div>
        :
        <div>
          <label >Enemy turn</label>
          <img src="https://cdn-icons-png.flaticon.com/512/1377/1377064.png" style={{width: 70, height: 70}} alt=""/>
        </div>
        }

      </div>
      <div>
        <button className="btn"onClick={fetchCardsData}>Load data</button>
      </div>
      <div style={{width: 30, height: 50}} ></div>

      
      <Modal isOpen={isModalOpen} onRequestClose={() => handleModalClose({name: "", points: 1})}style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose a card to target</h2>
        {targetableCards.map((card, index) =>(
          <button onClick= { () => {handleModalClose(card)} }><CardComponent  name={card.name} points={card.points}></CardComponent></button>
        ))}
      </Modal>
      <Modal isOpen={isRowsModalOpen} onRequestClose={() => handleRowsModalClose(-1)} style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose a row to traget</h2>
        {affectableRows.map((row, index) =>(
          <button style={{fontSize: '30px',}} onClick= { () => {handleRowsModalClose(row)} }>{row + 1}</button>
        ))}
      </Modal>

      <DragDropContext onDragEnd = {(result) => onDragEndOf(result, userName)}>
        <HandComponent cardsInHand = {cardsInHand} cardInPlayChain={playChainCard}></HandComponent>

        <RowComponent cardsOnRow = {cardsOnThirdRow} pointsOnRow={pointsOnRows[thirdRow]} rowDroppableId={"BoardRow3"} rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[thirdRow]) ||''}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnSecondRow} pointsOnRow={pointsOnRows[secondRow]} rowDroppableId={"BoardRow2"} rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[secondRow]) ||''}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnBoard} pointsOnRow={pointsOnRows[firstRow]} rowDroppableId={"BoardRow1"} rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[firstRow]) ||''}></RowComponent>
      </DragDropContext>  
        
        <div className="wonRounds">
          {renderWonRounds(wonRounds)}
        </div>
        <div className="boardMiddle">
          <div className="separator"></div>
          <div className="endRoundDiv"><button className="endRoundButton"onClick={() => endRoundFor(userName)}>End round</button></div>
        </div>
        <div className="wonRounds">
          {renderWonRounds(enemyWonRounds)}
        </div>
      
      <DragDropContext onDragEnd = {() => {}}>
        <RowComponent cardsOnRow = {enemyCardsOnFirstRow} pointsOnRow={enemyPointsOnRows[firstRow]} rowDroppableId={"BoardRow1"}rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[firstRow]) ||''}></RowComponent>
        <RowComponent cardsOnRow = {enemyCardsOnSecondRow} pointsOnRow={enemyPointsOnRows[secondRow]} rowDroppableId={"BoardRow2"}rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[secondRow]) ||''}></RowComponent>
        <RowComponent cardsOnRow = {enemyCardsOnThirdRow} pointsOnRow={enemyPointsOnRows[thirdRow]} rowDroppableId={"BoardRow3"}rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[thirdRow]) ||''}></RowComponent>
      </DragDropContext>
      
      
      <div className="enemyName">{enemyName} : {enemyPointsOnRows.reduce((sum, e) => sum + e, 0)}</div>
    </div>
  )
}

export default DuelPage;