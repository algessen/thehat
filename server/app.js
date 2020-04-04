const express = require('express');
const app = express();

const options = {
  host: 'redis',
  port: 6379,
  logErrors: true
};

app.get('/', (req, res) => {
  res.send(`Hello World!<br><p>I have been loaded num times.</p>`);
});

app.listen(3000, () => {
  console.log('Example app listening on port 3000!');
});
