import React, {useState, useEffect} from 'react';
import {Card, createCardWithName, createEmptyCard} from './../Interfaces/Card';
import { DragDropContext, DropResult } from 'react-beautiful-dnd';
import HandComponent from './HandComponent';
import EnemyHandComponent from './EnemyHandComponent';
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
var firstRowId:string = "Row 1";
var secondRowId:string = "Row 2";
var thirdRowId:string = "Row 3";
var rowStatusToImageUrl: Map<string,string> = new Map<string,string>([
  ["", ""],
  ["Rain", "https://parspng.com/wp-content/uploads/2022/06/rainpng.parspng.com-4.png"],
]);
const DuelPage = () => {

  let navigate = useNavigate();
  const [refresh, setRefresh] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isGraveyardModalOpen, setIsGraveyardModalOpen] = useState(false);
  const [isDeckCardsModalOpen, setIsDeckCardsModalOpen] = useState(false);
  const [isRowsModalOpen, setIsRowsModalOpen] = useState(false);

  const [cardsInHand, setCardsInHand] = useState<Card[]>([]);

  const [cardsOnBoard, setCardsOnBoard] = useState<Card[]>([]);
  const [graveyardCards, setGraveyardCards] = useState<Card[]>([]);
  const [cardsInDeck, setCardsInDeck] = useState<Card[]>([]);
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

  const [enemyPointsOnRows, setEnemyPointsOnRows] = useState<number[]>([]);
  const [enemyWonRounds, setenemyWonRounds] = useState<number>(0);
  const [isEnemyTurn, setisEnemyTurn] = useState<boolean>(false);
  const [didEnemyWon, setdidEnemyWon] = useState<boolean>(false);

  const [targetableCards, setTargetableCards] = useState<Card[]>([]);
  const [affectableRows, setAffectableRows] = useState<number[]>([]);
  const [enemyName, setEnemyName] = useState<string>("");
  const [enemyHandSize, setEnemyHandSize] = useState<number>(0);



  const [enemyEndRoundBackground, setEnemyEndRoundBackground] = useState<string>('');
  const [enemyEndRoundMessage, setEnemyEndRoundMessage] = useState<string>('');
  const [playerEndRoundBackground, setPlayerEndRoundBackground] = useState<string>('');
  const [playerEndRoundMessage, setPlayerEndRoundMessage] = useState<string>('');

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
    stompClient.subscribe('/user/' + userName + '/enemyEndRound', enemyEndRoundTrigger);
    stompClient.subscribe('/user/' + userName + '/newRoundStarted', newRoundStarted);
    stompClient.subscribe('/user/' + userName + '/mulligan', mulliganMessage);
    setIsMulliganModalOpen(true);
  }
  const onMessageReceived = (payload: any) => {
    fetchCardsData();
  }
  const enemyEndRoundTrigger = (payload: any) => {
    fetchCardsData();
    setEnemyEndRoundBackground('rgba(0,0,0,0.4');
    setEnemyEndRoundMessage("Enemy ended round");
  }
  const newRoundStarted = (payload: any) => {
    alertt("New round has started", "https://images.pexels.com/photos/326333/pexels-photo-326333.jpeg?cs=srgb&dl=pexels-pixabay-326333.jpg&fm=jpg", 3000, false);
    setEnemyEndRoundBackground('');
    setEnemyEndRoundMessage('');
    setPlayerEndRoundBackground('');
    setPlayerEndRoundMessage('');
    fetchCardsData();
  }

  const [mulliganedCards, setMulliganedCards] = useState<number>(1);
  const [didEnemyEndedMulligan, setDidEnemyEndedMulligan] = useState<boolean>(false);
  const [didPlayerEndedMulligan, setDidPlayerEndedMulligan] = useState<boolean>(false);
  const [isMulliganModalOpen, setIsMulliganModalOpen] = useState(false);
  const mulliganMessage= (payload: any) => {
    setDidEnemyEndedMulligan(true);
  }
  useEffect(() => {
    endMulligan();
  }, [didEnemyEndedMulligan]);
  useEffect(() => {
    endMulligan();
  }, [didPlayerEndedMulligan]);
  const endMulligan = () => {
    if(didEnemyEndedMulligan === true && didPlayerEndedMulligan === true) {
      setIsMulliganModalOpen(false);
    }
  }
  const mulliganCard = (cardToMulligan: Card) => {
    if(cardToMulligan.name !== "" && mulliganedCards <= 3) {
      fetch(serverURL + `/Duel/mulliganCard/${userName}/${gameID}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(cardToMulligan)
      }).then(() => {
        fetchData<Card[]>(`${serverURL}/Duel/getHandCards/${userName}/${gameID}`, cardsInHand ,setCardsInHand);
        setMulliganedCards(mulliganedCards + 1);
      }).then(() => {
        if(mulliganedCards === 3) {
          stompClient.send('/app/mulliganEnded', {}, userName);
          setDidPlayerEndedMulligan(true);
        }
      });
    }
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
        fetchData<string[]>(`${serverURL}/Duel/getRowsStatus/${userEnemy}/${gameID}`, rowsStatus,setRowsStatus);
        fetchData<number>(`${serverURL}/Duel/getHandSize/${userEnemy}/${gameID}`, enemyHandSize,setEnemyHandSize);

      }).then(() => {
        if(wonRounds === enemyWonRounds && wonRounds === 2) {
          alertt("Draw","https://c4.wallpaperflare.com/wallpaper/103/477/186/forest-light-nature-forest-wallpaper-preview.jpg", 0, true );
        }
        else if(wonRounds === 2) {
          alertt("You won!","https://png.pngtree.com/thumb_back/fh260/background/20220523/pngtree-stage-podium-with-rays-of-spotlights-for-award-ceremony-winner-with-image_1400291.jpg", 0, true );
        }
        else if(enemyWonRounds === 2) {
          alertt("You lost!","https://c4.wallpaperflare.com/wallpaper/33/477/228/rain-showers-forest-illustration-wallpaper-preview.jpg", 0, true );
        }

      })
      .catch(console.error);

    setRefresh(true);
  }

  const alertt= (msg:string, imageURL:string, timeout:number, appearButton: boolean) => {
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
     if(appearButton) {
      alert.appendChild(alertButton);
      alertButton.addEventListener('click',(e) => {
        alert.remove();
        navigate("/Main");
      });
     }
    if(timeout !== 0) {
      setTimeout(() => {
        alert.remove();
      }, Number(timeout))
    }
    document.body.appendChild(alert);
  }



  const [cardDragged, setCardDragged] = useState<Card>(createEmptyCard());
  const [playChainCard, setPlayChainCard] = useState<Card>(createEmptyCard());
  const [postOnRowNumberOf, setPostOnRowNumberOf] = useState<number>(0);
  const [cardAffected, setCardAffected] = useState<Card>(createEmptyCard());
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
    } 
    else {
      setAffectableRows(possibleAffectedRows);
      setIsRowsModalOpen(true);
    } 

  }
  const handleRowsModalClose = (affectedRow: number) => {
    playDraggedCard(`${serverURL}/Duel/playCard?userName=${userName}&affectedRow=${affectedRow}&rowNumber=${postOnRowNumberOf}&gameID=${gameID}`,  cardAffected);
    setIsRowsModalOpen(false);
  }


  const onDragEndOf = (result:DropResult, player:string) => {
    const {destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    setPostOnRowNumberOf(-1);
    if(destination.droppableId === firstRowId){
      setPostOnRowNumberOf(0);
    }
    else if(destination.droppableId === secondRowId){
      setPostOnRowNumberOf(1);
    }
    else if(destination.droppableId === thirdRowId){
      setPostOnRowNumberOf(2);
    }
    setCardDragged(createCardWithName(result.draggableId));



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
        setCardAffected(createEmptyCard());
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
        stompClient.send('/app/sendTrigger', {}, userName, userName);
        fetchCardsData();
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
      setPlayerEndRoundBackground('rgba(0,0,0,0.4');
      setPlayerEndRoundMessage("You ended round");
      fetchCardsData()
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
  const getEnemyHandBlankCards = () => {
    let cards: Card[] = [];
    for(let i = 0 ; i < enemyHandSize ; ++i ) {
      cards.push(createEmptyCard());
    }
    return cards;
  }
  const handleGraveyardOpen = () => {
    fetch(serverURL + `/Duel/getGraveyardCards/${userName}/${gameID}`)
      .then((res) => res.json())
      .then((data: Card[]) => {
        setGraveyardCards(data);
      }).then(() => {
        setIsGraveyardModalOpen(true);
      }).catch(console.error);
  }
  const handleDeckCardsOpen = () => {
    fetch(serverURL + `/Duel/getDeckCards/${userName}/${gameID}`)
      .then((res) => res.json())
      .then((data: Card[]) => {
        setCardsInDeck(data);
      }).then(() => {
        setIsDeckCardsModalOpen(true);
      }).catch(console.error);
  }
  


  return (
    
    <div>
      <div className="playerTag">{userName} : {pointsOnRows.reduce((sum, e) => sum + e, 0)}</div>
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
        <button className="btn"onClick={handleGraveyardOpen}>Show graveyard</button>
        <button className="btn"onClick={handleDeckCardsOpen}>Show cards in deck</button>
      </div>
      <div style={{width: 30, height: 50}} ></div>

      
      <Modal isOpen={isModalOpen} onRequestClose={() => handleModalClose(createEmptyCard())}style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose a card to target</h2>
        {targetableCards.map((card, index) =>(
          <button onClick= { () => {handleModalClose(card)} }><CardComponent  card={card}></CardComponent></button>
        ))}
      </Modal>
      <Modal isOpen={isMulliganModalOpen} onRequestClose={() => mulliganCard(createEmptyCard())}style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose a card to mulligan</h2>
        {cardsInHand.map((card, index) =>(
          <button onClick= { () => {mulliganCard(card)} }><CardComponent  card={card}></CardComponent></button>
        ))}
      </Modal>
      <Modal isOpen={isGraveyardModalOpen} style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Graveyard cards</h2>
        {graveyardCards.map((card, index) =>(
          <div><CardComponent  card={card}></CardComponent></div>
        ))}
        <button onClick={() => setIsGraveyardModalOpen(false)}>Close</button>
      </Modal>
      <Modal isOpen={isDeckCardsModalOpen} style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Cards in deck</h2>
        {cardsInDeck.map((card, index) =>(
          <div><CardComponent  card={card}></CardComponent></div>
        ))}
        <button onClick={() => setIsDeckCardsModalOpen(false)}>Close</button>
      </Modal>
      <Modal isOpen={isRowsModalOpen} onRequestClose={() => handleRowsModalClose(-1)} style={{content: {width:'300px', height:'200px', background:'gray',},}}>
        <h2>Choose a row to traget</h2>
        {affectableRows.map((row, index) =>(
          <button style={{fontSize: '30px',}} onClick= { () => {handleRowsModalClose(row)} }>{row + 1}</button>
        ))}
      </Modal>

      <DragDropContext onDragEnd = {(result) => onDragEndOf(result, userName)}>
        <HandComponent  cardsInHand = {cardsInHand} cardInPlayChain={playChainCard}></HandComponent>

        <RowComponent cardsOnRow = {cardsOnThirdRow} pointsOnRow={pointsOnRows[thirdRow]} rowDroppableId={thirdRowId} rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[thirdRow]) ||''}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnSecondRow} pointsOnRow={pointsOnRows[secondRow]} rowDroppableId={secondRowId} rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[secondRow]) ||''}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnBoard} pointsOnRow={pointsOnRows[firstRow]} rowDroppableId={firstRowId} rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[firstRow]) ||''}></RowComponent>
      </DragDropContext>  
        
        <div className="wonRounds" style={{background: playerEndRoundBackground|| ''}}>
          {renderWonRounds(wonRounds)} {playerEndRoundMessage}
        </div>
        <div className="boardMiddle">
          <div className="separator"></div>
          <div className="endRoundDiv"><button className="endRoundButton"onClick={() => endRoundFor(userName)}>End round</button></div>
        </div>
        <div className="wonRounds" style={{background: enemyEndRoundBackground || ''}}>
          {renderWonRounds(enemyWonRounds)} {enemyEndRoundMessage}
        </div>
      
      <DragDropContext onDragEnd = {() => {}}>
        <RowComponent cardsOnRow = {enemyCardsOnFirstRow} pointsOnRow={enemyPointsOnRows[firstRow]} rowDroppableId={firstRowId}rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[firstRow]) ||''}></RowComponent>
        <RowComponent cardsOnRow = {enemyCardsOnSecondRow} pointsOnRow={enemyPointsOnRows[secondRow]} rowDroppableId={secondRowId}rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[secondRow]) ||''}></RowComponent>
        <RowComponent cardsOnRow = {enemyCardsOnThirdRow} pointsOnRow={enemyPointsOnRows[thirdRow]} rowDroppableId={thirdRowId}rowStatusImageURL={rowStatusToImageUrl.get(rowsStatus[thirdRow]) ||''}></RowComponent>
        <EnemyHandComponent cardsInHand={getEnemyHandBlankCards()} cardInPlayChain={createEmptyCard()}></EnemyHandComponent>
      </DragDropContext>


      
      
      <div className="enemyTag"><div className="enemyName">{enemyName} </div>: {enemyPointsOnRows.reduce((sum, e) => sum + e, 0)} </div>
    </div>
  )
}

export default DuelPage;