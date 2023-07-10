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


  const gameID = useSelector<StateData, string>((state) => state.gameID);



  const [deckData, setDeckData] = useState<Card[]>([]);


  useEffect(() => {
    console.log(gameID);
    const controller = new AbortController();
    fetch('http://localhost:8000/DeckBuilder/GetCardsInDeck')
      .then((res) => res.json())
      .then((deckData: Card[]) => {
        setDeckData(deckData);
      })
      .catch(console.error);

      return () => {
        controller.abort();
      };
  }, []);
  let firstPlayer:string = "first";
  let secondPlayer:string = "second";
  useEffect(() => {
    const controller = new AbortController();
    if (deckData.length > 0) {
      fetch(`http://localhost:8000/Duel/SetupDecks?firstUser=${"first"}&secondUser=${"second"}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(deckData),
        signal: controller.signal
      });
    }

    return () => {
      controller.abort();
    };

  }, [deckData]);


  const fetchData = <T,>(url: string,data: T ,setter: React.Dispatch<React.SetStateAction<T>>) => {
    fetch(url)
      .then((res) => res.json())
      .then((data: T) => {
        setter(data);
      })
      .catch(console.error);
  }


  
  const fetchCardsData = () => {
    fetchData<Card[]>(`http://localhost:8000/Duel/getHandCards/${firstPlayer}`, cardsInHand ,setCardsInHand);
    fetchData<Card[]>(`http://localhost:8000/Duel/getCardsOnRow/${firstPlayer}/${0}`,cardsOnBoard ,setCardsOnBoard);
    fetchData<Card[]>(`http://localhost:8000/Duel/getCardsOnRow/${firstPlayer}/${1}`, cardsOnSecondRow ,setCardsOnSecondRow);
    fetchData<Card[]>(`http://localhost:8000/Duel/getCardsOnRow/${firstPlayer}/${2}`, cardsOnThirdRow ,setCardsOnThirdRow);
    fetchData<number>(`http://localhost:8000/Duel/getBoardPoints/${firstPlayer}`, pointsOnBoard ,setPointsOnBoard);
    fetchData<boolean>(`http://localhost:8000/Duel/isTurnOf/${firstPlayer}`, isTurnOfPlayer1 ,setIsTurnOfPlayer1);
    fetchData<number>(`http://localhost:8000/Duel/getWonRounds/${firstPlayer}`, wonRounds ,setWonRounds);
    fetchData<boolean>(`http://localhost:8000/Duel/didWon/${firstPlayer}`, didWon ,setDidWon);

    fetchData<Card[]>(`http://localhost:8000/Duel/getHandCards/${secondPlayer}`, cardsInHand2 ,setCardsInHand2);
    fetchData<Card[]>(`http://localhost:8000/Duel/getCardsOnRow/${secondPlayer}/${0}`,cardsOnBoard2 ,setCardsOnBoard2);
    fetchData<Card[]>(`http://localhost:8000/Duel/getCardsOnRow/${secondPlayer}/${1}`, cardsOnSecondRow2 ,setCardsOnSecondRow2);
    fetchData<Card[]>(`http://localhost:8000/Duel/getCardsOnRow/${secondPlayer}/${2}`, cardsOnThirdRow2 ,setCardsOnThirdRow2);
    fetchData<number>(`http://localhost:8000/Duel/getBoardPoints/${secondPlayer}`, pointsOnBoard2 ,setPointsOnBoard2);
    fetchData<boolean>(`http://localhost:8000/Duel/isTurnOf/${secondPlayer}`, isTurnOfPlayer2 ,setIsTurnOfPlayer2);
    fetchData<number>(`http://localhost:8000/Duel/getWonRounds/${secondPlayer}`, wonRounds2 ,setWonRounds2);
    fetchData<boolean>(`http://localhost:8000/Duel/didWon/${secondPlayer}`, didWon2 ,setDidWon2);

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
    fetch(`http://localhost:8000/Duel/getPossibleRowsToAffect`, {
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
      playDraggedCard(`http://localhost:8000/Duel/playCard?userName=${playerPlayer}&affectedRow=${-1}&rowNumber=${postOnRowNumberOf}`, cardDragged, cardAffected);
      fetchCardsData();
    } 
    else {
      setAffectableRows(possibleAffectedRows);
      setIsRowsModalOpen(true);
    } 

  }
  const handleRowsModalClose = (affectedRow: number) => {
    
    playDraggedCard(`http://localhost:8000/Duel/playCard?userName=${playerPlayer}&affectedRow=${affectedRow}&rowNumber=${postOnRowNumberOf}`, cardDragged, cardAffected);
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
    setPlayerPlayer(player);



  }
  useEffect(() => {
    fetch(`http://localhost:8000/Duel/getPossibleTargets/${playerPlayer}`, {
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
      });
  }

  const endRoundFor = (player:string) => {
    fetch(`http://localhost:8000/Duel/endRound?userName=${player}`, {
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
        <button onClick={() => endRoundFor(firstPlayer)}>End round</button>
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

      <DragDropContext onDragEnd = {(result) => onDragEndOf(result, firstPlayer)}>
        <HandComponent cardsInHand = {cardsInHand}></HandComponent>

        <RowComponent cardsOnRow = {cardsOnThirdRow} pointsOnRow={pointsOnBoard} rowDroppableId={"BoardRow3"}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnSecondRow} pointsOnRow={pointsOnBoard} rowDroppableId={"BoardRow2"}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnBoard} pointsOnRow={pointsOnBoard} rowDroppableId={"BoardRow1"}></RowComponent>
      </DragDropContext>  
        
      
      <DragDropContext onDragEnd = {(result) => onDragEndOf(result, secondPlayer)}>
        <RowComponent cardsOnRow = {cardsOnBoard2} pointsOnRow={pointsOnBoard2} rowDroppableId={"BoardRow1"}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnSecondRow2} pointsOnRow={pointsOnBoard2} rowDroppableId={"BoardRow2"}></RowComponent>
        <RowComponent cardsOnRow = {cardsOnThirdRow2} pointsOnRow={pointsOnBoard2} rowDroppableId={"BoardRow3"}></RowComponent>
        
        <HandComponent cardsInHand = {cardsInHand2}></HandComponent>
      </DragDropContext>
      
      
      <div>
        <button onClick={() => endRoundFor(secondPlayer)}>End round</button>
        <label> |Did you won: {didWon2.toString()}|</label>
        <label> |Won rounds: {wonRounds2}| </label>
        <label> |Is your turn: {isTurnOfPlayer2.toString()}| </label>
      </div>
    </div>
  )
}

export default DuelPage