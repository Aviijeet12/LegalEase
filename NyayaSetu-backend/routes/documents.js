const express = require('express');
const { body } = require('express-validator');
const auth = require('../middleware/auth');
const upload = require('../middleware/upload');
const {
  uploadDocument,
  getDocuments,
  getDocument,
  updateDocument,
  deleteDocument,
  downloadDocument
} = require('../controllers/documentController');

const router = express.Router();

// @route   POST /api/documents/upload
// @desc    Upload document
// @access  Private
router.post('/upload', auth, upload.single('document'), uploadDocument);

// @route   GET /api/documents
// @desc    Get user documents
// @access  Private
router.get('/', auth, getDocuments);

// @route   GET /api/documents/:id
// @desc    Get document by ID
// @access  Private
router.get('/:id', auth, getDocument);

// @route   PUT /api/documents/:id
// @desc    Update document
// @access  Private
router.put('/:id', auth, [
  body('name', 'Document name is required').not().isEmpty()
], updateDocument);

// @route   DELETE /api/documents/:id
// @desc    Delete document
// @access  Private
router.delete('/:id', auth, deleteDocument);

// @route   GET /api/documents/:id/download
// @desc    Download document
// @access  Private
router.get('/:id/download', auth, downloadDocument);

module.exports = router;
