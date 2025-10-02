const express = require('express');
const { body } = require('express-validator');
const auth = require('../middleware/auth');
const {
  getCases,
  getCase,
  createCase,
  updateCase,
  deleteCase,
  assignLawyer,
  updateStatus,
  getCasesByUser,
  getCasesByLawyer
} = require('../controllers/caseController');

const router = express.Router();

// @route   GET /api/cases
// @desc    Get all cases (admin only)
// @access  Private
router.get('/', auth, getCases);

// @route   GET /api/cases/user
// @desc    Get cases by user
// @access  Private
router.get('/user', auth, getCasesByUser);

// @route   GET /api/cases/lawyer
// @desc    Get cases by lawyer
// @access  Private
router.get('/lawyer', auth, getCasesByLawyer);

// @route   GET /api/cases/:id
// @desc    Get case by ID
// @access  Private
router.get('/:id', auth, getCase);

// @route   POST /api/cases
// @desc    Create a case
// @access  Private
router.post('/', auth, [
  body('title', 'Title is required').not().isEmpty(),
  body('description', 'Description is required').not().isEmpty(),
  body('category', 'Category is required').not().isEmpty()
], createCase);

// @route   PUT /api/cases/:id
// @desc    Update case
// @access  Private
router.put('/:id', auth, [
  body('title', 'Title is required').not().isEmpty(),
  body('description', 'Description is required').not().isEmpty()
], updateCase);

// @route   DELETE /api/cases/:id
// @desc    Delete case
// @access  Private
router.delete('/:id', auth, deleteCase);

// @route   PUT /api/cases/:id/assign
// @desc    Assign lawyer to case
// @access  Private (Admin only)
router.put('/:id/assign', auth, [
  body('lawyerId', 'Lawyer ID is required').not().isEmpty()
], assignLawyer);

// @route   PUT /api/cases/:id/status
// @desc    Update case status
// @access  Private
router.put('/:id/status', auth, [
  body('status', 'Status is required').isIn(['pending', 'active', 'closed', 'cancelled'])
], updateStatus);

module.exports = router;
