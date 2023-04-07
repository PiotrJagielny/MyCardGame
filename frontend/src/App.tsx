import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import EntryPage from './Game_Unrelated_Components/EntryPageComponents/EntryPage';
import MainPage from './Game_Related_Components/MainPageComponents/MainPage';
import DeckBuilderPage from './Game_Related_Components/DeckBuildingComponents/DeckBuilderPage';
import DuelPage from './Game_Related_Components/DuelComponents/DuelPage';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<EntryPage />} />
          <Route path="/Main" element={<MainPage />} />
          <Route path="/DeckBuilder" element={<DeckBuilderPage />} />
          <Route path="/Duel" element={<DuelPage />} />
        </Routes>
      </Router>
    </div>
  );
};

export default App;
