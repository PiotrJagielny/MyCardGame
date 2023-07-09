

type Action = {type: "CHANGE_NAME", payload:string};

export const nameReducer = (state:string = "", action:Action) => {
    switch(action.type) {
        case "CHANGE_NAME":
            return state = action.payload;
        default:
            return state;
    }

};