
// const express = require('express')
// const app = express()
// const port = 3000


// app.use(express.static('spa'))
// // app.get('/', (req, res) => {
// //   res.send('Hello World!')
// // })

// app.listen(port, () => {
//   console.log(`Example app listening at http://localhost:${port}`)
// })
const
  express = require('express'),
  serveStatic = require('serve-static'),
  history = require('connect-history-api-fallback'),
  port = process.env.PORT || 5000

const app = express()

app.use(history())
app.use(serveStatic('spa'))
app.listen(port)