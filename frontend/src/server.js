const express = require('express');
const path = require('path');
const app = express();

const port = process.env.PORT || 9001;

// Sirve los archivos de Angular compilados
app.use(express.static(path.join(__dirname, 'dist/frontend-erp')));

// Redirecciona todas las rutas a index.html (soporte para rutas de Angular)
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist/frontend-erp/index.html'));
});

app.listen(port, () => {
  console.log(`Servidor ejecutándose en http://localhost:${port}`);
});
