package com.example.demo.Duel;

import com.example.demo.CardsServices.CardDisplay;
import com.example.demo.Consts;
import com.example.demo.Duel.Services.CardDuel;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(path = "/Duel")
public class NormalDuelController {

    private CardDuel duel;


    @GetMapping(path = "getHandCards_player1")
    @CrossOrigin
    public List<CardDisplay> getHand_player1(){
        return duel.getCardsInHandDisplayOf(Consts.firstPlayer);
    }
    @GetMapping(path = "getHandCards_player2")
    @CrossOrigin
    public List<CardDisplay> getHand_player2(){
        return duel.getCardsInHandDisplayOf(Consts.secondPlayer);
    }
    @GetMapping(path = "getHandCards/{userName}")
    @CrossOrigin
    public List<CardDisplay> getHand(@PathVariable String userName){
        return duel.getCardsInHandDisplayOf(userName);
    }


    @GetMapping(path = "getDeckCards_player1")
    @CrossOrigin
    public List<CardDisplay> getDeck_player1() {
        return duel.getCardsInDeckDisplayOf(Consts.firstPlayer);
    }
    @GetMapping(path = "getDeckCards_player2")
    @CrossOrigin
    public List<CardDisplay> getDeck_player2() {
        return duel.getCardsInDeckDisplayOf(Consts.secondPlayer);
    }
    @GetMapping(path = "getDeckCards/{userName}")
    @CrossOrigin
    public List<CardDisplay> getDeck(@PathVariable String userName) {
        return duel.getCardsInDeckDisplayOf(userName);
    }

    @GetMapping(path = "getBoardCards_player1")
    @CrossOrigin
    public List<CardDisplay> getBoard_player1(){
        return duel.getCardsOnBoardDisplayOf(Consts.firstPlayer, Consts.firstRow);
    }
    @GetMapping(path = "getBoardCardsOnSecondRow_player1")
    @CrossOrigin
    public List<CardDisplay> getBoardCardsOnSecondRow_player1(){
        return duel.getCardsOnBoardDisplayOf(Consts.firstPlayer, Consts.secondRow);
    }
    @GetMapping(path = "getBoardCardsOnThirdRow_player1")
    @CrossOrigin
    public List<CardDisplay> getBoardCardsOnThirdRow_player1(){
        return duel.getCardsOnBoardDisplayOf(Consts.firstPlayer, Consts.thirdRow);
    }

    @GetMapping(path = "getBoardCards_player2")
    @CrossOrigin
    public List<CardDisplay> getBoard_player2(){
        return duel.getCardsOnBoardDisplayOf(Consts.secondPlayer, Consts.firstRow);
    }
    @GetMapping(path = "getBoardCardsOnSecondRow_player2")
    @CrossOrigin
    public List<CardDisplay> getBoardCardsOnSecondRow_player2(){
        return duel.getCardsOnBoardDisplayOf(Consts.secondPlayer, Consts.secondRow);
    }
    @GetMapping(path = "getBoardCardsOnThirdRow_player2")
    @CrossOrigin
    public List<CardDisplay> getBoardCardsOnThirdRow_player2(){
        return duel.getCardsOnBoardDisplayOf(Consts.secondPlayer, Consts.thirdRow);
    }
    @GetMapping(path = "getCardsOnRow/{userName}/{rowNumber}")
    @CrossOrigin
    public List<CardDisplay> getRow(@PathVariable String userName, @PathVariable int rowNumber){
        return duel.getCardsOnBoardDisplayOf(userName, rowNumber);
    }



    @GetMapping(path = "getBoardPoints_player1")
    @CrossOrigin
    public int getPointsOnBoard_player1(){
        return duel.getBoardPointsOf(Consts.firstPlayer);
    }
    @GetMapping(path = "getBoardPoints_player2")
    @CrossOrigin
    public int getPointsOnBoard_player2(){
        return duel.getBoardPointsOf(Consts.secondPlayer);
    }
    @GetMapping(path = "getBoardPoints/{userName}")
    @CrossOrigin
    public int getPointsOnBoard(@PathVariable String userName){
        return duel.getBoardPointsOf(userName);
    }



    @GetMapping(path = "getWonRounds_player1")
    @CrossOrigin
    public int getWonRounds_player1(){
        return duel.getWonRoundsOf(Consts.firstPlayer);
    }
    @GetMapping(path = "getWonRounds_player2")
    @CrossOrigin
    public int getWonRounds_player2(){return duel.getWonRoundsOf(Consts.secondPlayer);}
    @GetMapping(path = "getWonRounds/{userName}")
    @CrossOrigin
    public int getWonRounds(@PathVariable String userName){return duel.getWonRoundsOf(userName);}



    @PostMapping(path = "playCard_player1")
    @CrossOrigin
    public void playCard_player1(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, Consts.firstPlayer, Consts.firstRow);
    }
    @PostMapping(path = "playCardOnSecondRow_player1")
    @CrossOrigin
    public void playCardOnSecondRow_player1(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, Consts.firstPlayer, Consts.secondRow);
    }
    @PostMapping(path = "playCardOnThirdRow_player1")
    @CrossOrigin
    public void playCardOnThirdRow_player1(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, Consts.firstPlayer, Consts.thirdRow);
    }

    @PostMapping(path = "playCard_player2")
    @CrossOrigin
    public void playCard_player2(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, Consts.secondPlayer, Consts.firstRow);
    }
    @PostMapping(path = "playCardOnSecondRow_player2")
    @CrossOrigin
    public void playCardOnSecondRow_player2(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, Consts.secondPlayer, Consts.secondRow);
    }
    @PostMapping(path = "playCardOnThirdRow_player2")
    @CrossOrigin
    public void playCardOnThirdRow_player2(@RequestBody CardDisplay cardPlayed){
        duel.playCardAs(cardPlayed, Consts.secondPlayer, Consts.thirdRow);
    }
    @PostMapping(path = "playCard")
    @CrossOrigin
    public void playCard(@RequestBody CardDisplay cardPlayed, @RequestParam String userName, @RequestParam int rowNumber){
        duel.playCardAs(cardPlayed, userName, rowNumber);
    }

    @PostMapping(path = "endRound_player1")
    @CrossOrigin
    public void endRound_player1(){
        duel.endRoundFor(Consts.firstPlayer);
    }
    @PostMapping(path = "endRound_player2")
    @CrossOrigin
    public void endRound_player2(){
        duel.endRoundFor(Consts.secondPlayer);
    }
    @PostMapping(path = "endRound")
    @CrossOrigin
    public void endRound(@RequestParam String userName){
        duel.endRoundFor(userName);
    }

    @GetMapping(path = "isTurnOf_player1")
    @CrossOrigin
    public boolean isTurnOf_player1(){
        return duel.isTurnOf(Consts.firstPlayer);
    }
    @GetMapping(path = "isTurnOf_player2")
    @CrossOrigin
    public boolean isTurnOf_player2(){
        return duel.isTurnOf(Consts.secondPlayer);
    }
    @GetMapping(path = "isTurnOf/{userName}")
    @CrossOrigin
    public boolean isTurnOf_player2(@PathVariable String userName){
        return duel.isTurnOf(userName);
    }

    @GetMapping(path = "didWon_player1")
    @CrossOrigin
    public boolean didWon_player1(){
        return duel.didWon(Consts.firstPlayer);
    }
    @GetMapping(path = "didWon_player2")
    @CrossOrigin
    public boolean didWon_player2(){
        return duel.didWon(Consts.secondPlayer);
    }
    @GetMapping(path = "didWon/{userName}")
    @CrossOrigin
    public boolean didWon_player2(@PathVariable String userName){
        return duel.didWon(userName);
    }

    @PostMapping(path = "SetupDecks")
    @CrossOrigin
    public void SetupDeck(@RequestBody List<CardDisplay> cardsInDeck, @RequestParam String firstUser,@RequestParam String secondUser ){
        duel = CardDuel.createDuel();
        duel.registerPlayerToDuel(firstUser);
        duel.registerPlayerToDuel(secondUser);
        duel.parseCardsFor(cardsInDeck, firstUser);
        duel.parseCardsFor(cardsInDeck, secondUser);
        duel.dealCards();
        duel.dealCards();
    }

}
