import React, {useState, useEffect} from 'react';
import {Card} from './../Interfaces/Card';
import { DragDropContext, DropResult } from 'react-beautiful-dnd';
import HandComponent from './HandComponent';
import RowComponent from './RowComponent';
import './DuelPage.css';
import Modal from 'react-modal';
import CardComponent from '../CardComponent';

const DuelPage = () => {
  const [refresh, setRefresh] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);

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





  const [deckData, setDeckData] = useState<Card[]>([]);


  useEffect(() => {
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
  const handleModalClose = (card: Card) => {
    setIsModalOpen(false);
    playDraggedCard(`http://localhost:8000/Duel/playCard?userName=${playerPlayer}&rowNumber=${postOnRowNumberOf}`, cardDragged, {name:"nor", points: 1});
    console.log(playerPlayer);
    console.log(postOnRowNumberOf);
    console.log(cardDragged);
    fetchCardsData();


  };


  const onDragEndOf = async (result:DropResult, player:string) => {
    const {destination} = result;
    
    if(!destination){return;}
    if(destination.droppableId === "Hand"){return;}

    cardDragged = {name: result.draggableId, points: 0};
    postOnRowNumberOf= -1;
    playerPlayer = player;

    console.log(playerPlayer);
    console.log(postOnRowNumberOf);
    console.log(cardDragged);

    if(destination.droppableId === "BoardRow1"){
      postOnRowNumberOf = 0;
    }
    else if(destination.droppableId === "BoardRow2"){
      postOnRowNumberOf = 1;
    }
    else if(destination.droppableId === "BoardRow3"){
      postOnRowNumberOf = 2;
    }

    fetch(`http://localhost:8000/Duel/getPossibleTargets/${player}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(cardDragged)
    }).then(async (response) => {

      const targetableCardsResponse= await response.json();

      await ensure(player, postOnRowNumberOf, cardDragged, targetableCardsResponse);

    });



  }
  const ensure = async (player:string, postOnRowNumberOf:number, cardDragged:Card, targetableCardsArg:Card[]) => {

      if(targetableCardsArg.length === 0) {
        await playDraggedCard(`http://localhost:8000/Duel/playCard?userName=${player}&rowNumber=${postOnRowNumberOf}`, cardDragged, {name:"nor", points: 1});
        await fetchCardsData();
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
      
      <Modal isOpen={isModalOpen} onRequestClose={() => handleModalClose({name: "Not", points: 1})}>
        {targetableCards.map((card, index) =>(
          <button onClick= { () => {handleModalClose(card)} }><CardComponent color={'blue'} image={'none'} name={card.name} points={card.points}></CardComponent></button>
        ))}
        {/* <button onClick={handleModalClose}>Close</button> */}
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