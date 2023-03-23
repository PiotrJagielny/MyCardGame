import React from 'react'

interface Props{
  todo:string;
  setTodo: React.Dispatch<React.SetStateAction<string>>;
};

function InputField({todo,setTodo}: Props) {
  return (
    <div>
        <form className="input">
            <input type='input' placeholder='Enter' />
            <button className="buttonIn" type="submit"> Go </button>
            
        </form>
    </div>
  )
}

export default InputField