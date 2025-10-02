const express = require('express');
const { body } = require('express-validator');
const auth = require('../middleware/auth');
const {
  getUsers,
  getUser,
  updateUser,
  deleteUser,
  getLawyers,
  verifyLawyer,
  getUserStats
} = require('../controllers/userController');

const router = express.Router();

// @route   GET /api/users
// @desc    Get all users (admin only)
// @access  Private
router.get('/', auth, getUsers);

// @route   GET /api/users/lawyers
// @desc    Get all lawyers
// @access  Private
router.get('/lawyers', auth, getLawyers);

// @route   GET /api/users/stats/dashboard
// @desc    Get user dashboard stats
// @access  Private
router.get('/stats/dashboard', auth, getUserStats);

// @route   GET /api/users/:id
// @desc    Get user by ID
// @access  Private
router.get('/:id', auth, getUser);

// @route   PUT /api/users/:id
// @desc    Update user
// @access  Private
router.put('/:id', auth, [
  body('firstName', 'First name is required').not().isEmpty(),
  body('lastName', 'Last name is required').not().isEmpty(),
  body('email', 'Please include a valid email').isEmail()
], updateUser);

// @route   DELETE /api/users/:id
// @desc    Delete user (admin only)
// @access  Private
router.delete('/:id', auth, deleteUser);

// @route   PUT /api/users/:id/verify
// @desc    Verify lawyer (admin only)
// @access  Private
router.put('/:id/verify', auth, verifyLawyer);

module.exports = router;
