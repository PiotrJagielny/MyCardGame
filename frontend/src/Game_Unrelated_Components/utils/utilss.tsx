export const renderWonRounds = (wonRoudnsOfPlayer: number) => {
    const wonRoundsDivs = [];
    for(let i = 0 ; i < wonRoudnsOfPlayer; i++) {
        wonRoundsDivs.push(<div key={i}><img src="https://cdn-icons-png.flaticon.com/512/6941/6941697.png" style={{width: 30, height: 30}} alt=""/></div>)
    }
    if(wonRoundsDivs.length === 0) {
        wonRoundsDivs.push(<div style={{width: 30, height: 30}} > </div>)
    }
    return wonRoundsDivs;
} 



