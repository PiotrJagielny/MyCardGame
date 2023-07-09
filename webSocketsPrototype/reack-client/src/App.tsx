import React from 'react';
import SearchingPage from './components/SearchingPage';
import GamePage from './components/GamePage';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';


function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<SearchingPage/>} />
          <Route path="/game" element={<GamePage/>} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
