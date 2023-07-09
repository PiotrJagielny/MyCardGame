import {createStore} from 'redux';
import {nameReducer} from './nameReducer';


export const store = createStore(nameReducer);