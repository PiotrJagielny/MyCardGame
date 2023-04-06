import React from 'react'

interface Props{
    Messages: string[];
    refresh: boolean;
}

export const MessagesComponent: React.FC<Props> = ({Messages, refresh}) => {
  return (
    <div>
        <ul>
          {Messages.filter(message => message.length !== 0).map(message =>(
            
              <li>{message}</li>
            
          ))}
        </ul>
    </div>
  )
}
