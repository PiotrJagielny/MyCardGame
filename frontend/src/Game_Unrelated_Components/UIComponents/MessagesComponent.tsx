import React from 'react'

interface Props{
    Messages: string[];
    refresh: boolean;
}

export const MessagesComponent: React.FC<Props> = ({Messages, refresh}) => {
  return (
    <div>
        {Messages.filter(message => message.length !== 0).map(message =>(
          <ul>
            <li>{message}</li>
          </ul>
        ))}
    </div>
  )
}
