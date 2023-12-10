import React, { use } from 'react';
import * as ReactDOMClient from 'react-dom/client';
import App from './App'
import './css/style.css'
import './css/boss_list.css'
import './css/underline.css'
import './css/Table.css'
import './css/newTransport.css'
import './css/Precogs.css'

const app = ReactDOMClient.createRoot(document.getElementById("app"))

app.render(<App />)