var io = null;
const controller = {};

controller.init = function(socketio) {
  io = socketio;
}

controller.list = (req, res) => {
  req.getConnection((err, conn) => {
    conn.query('SELECT * FROM customer', (err, customers) => {
      if (err) {
        res.json(err);
      }
      //console.log(customers); // arreglo de la bse de datos
      res.render('customers', { // en customers.ejs
        data: customers
      });
    })
  })
};

// Guarda en la database un objeto
controller.save = (req, res) => {
  const data = req.body;

  req.getConnection((err, conn) => {  // getConnection to batabase
    if (err) throw err;
    conn.query('INSERT INTO customer set ?', [data], (err, row) => {
      if (err) throw err;
      //console.log(row);

      io.emit('new_data', data);  // Emitir data a los sockets
      console.log('IO EMIT NEW_DATA EVENT');
      console.log(data);  // req.body gracias a urlencoded
      res.redirect('/');  // Redireccion a home
    });

  });
};

controller.edit = (req, res) => {
  const id = req.params.id;
  req.getConnection((err, conn) => {
    conn.query('SELECT * FROM customer WHERE id = ?', [id], (err, row) => {
      res.render('customer_edit', {
        data: row[0]
      });
    });
  });
};

controller.update = (req, res) => {
  const id = req.params.id;
  const newCustomer = req.body;
  req.getConnection((err, conn) => {
    conn.query('UPDATE customer set ? WHERE id = ?', [newCustomer, id], (err, rows) => {
      res.redirect('/');
    });
  });
};


controller.delete = (req, res) => {
  //console.log(req.params);  //  En params esta lo que sigue de ':' en la URL
  const id = req.params.id;

  req.getConnection((err, conn) => {
    conn.query('DELETE FROM customer WHERE id = ?', [id], (err, rows) => {
      res.redirect('/');
    });
  });

};

module.exports = controller;
