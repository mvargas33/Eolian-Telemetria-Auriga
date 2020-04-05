'use strict'

const express = require('express')
const bodyParser = require('body-parser') // Para acceder a cuerpos de peticiones
const mongoose = require('mongoose')

const Product = require('./models/product') // Importar schema


const app = express()
const port = process.env.PORT || 3000

app.use(bodyParser.urlencoded({ extended: false}))
app.use(bodyParser.json())

// Request de todos los productos
app.get('/api/product', (req, res) => {
  Product.find({}, (err, products) => { // find({}, ) significa buscar todos los productos
  if (err) return res.status(500).send({message: `Error al realizar la petición: ${err}`})
  if (!products) return res.status(404).send({message: `No existen productos`})

  res.status(200).send({products: products})
  })
})

// Request de objeto especifico
app.get('/api/product/:productId', (req,res) => {
  let productId = req.params.productId

  Product.findById(productId, (err, product) => { // Busca por Id ingresada en url
    if (err) return res.status(500).send({message: `Error al realizar la petición: ${err}`})
    if (!product) return res.status(404).send({message: `El producto no existe`})

    res.status(200).send({product: product})
  })
})

// Envia objetos nuevos
app.post('/api/product', (req, res) => {
  console.log('POST /api/product');
  console.log(req.body) // Se imprime objeto gracias a BodyParse

  let product = new Product() // Crear objeto
  // Añadir propiedades con objeto en request
  product.name = req.body.name // .Nombre de peticion (name)
  product.picture = req.body.picture
  product.price = req.body.price
  product.category = req.body.category
  product.description = req.body.description

  product.save((err, productStored) => {
    if (err) res.status(500).send({message: `Error al guardar en db: ${err}`})

    res.status(200).send({product: productStored})
  })

})

//Actualizar objeto
app.put('/api/product/:productId', (req, res) => {
  let productId = req.params.productId
  let update = req.body

  Product.findByIdAndUpdate(productId, update, (err, productUpdated) => {
    if (err) res.status(500).send({message: `Error al actualizar el producto: ${err}`})

    res.status(200).send({product: productUpdated})
  })
})

// Eliminar objeto
app.delete('/api/product/:productId', (req, res) => {
  let productId = req.params.productId

  Product.findById(productId, (err, product) => {
    if (err) res.status(500).send({message: `Error al borrar el producto: ${err}`})

    product.remove(err => {
      if (err) res.status(500).send({message: `Error al borrar el producto: ${err}`})
      res.status(200).send({message: `El producto ha sido eliminado`})
    })
  })


})


mongoose.connect('mongodb://localhost:27017/shop', (err, res) => {
  if (err){
    return console.log(`Error al conectar a la base de datos: ${err}`);;
  }
  console.log('Conexión a la base de datos establecida...')

  app.listen(port, () => {
    console.log(`Aplicacion corriendo en http://localhost:${port}`)
  })


})
