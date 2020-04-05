const express = require('express');
const router = express.Router();
const eolian_controller = require('../controllers/eolian_controller')

router.get('/', eolian_controller.show);
router.post('/add', eolian_controller.save);
router.get('/gauge', eolian_controller.gauge);

module.exports = router;
