const express = require('express');
const { body } = require('express-validator');
const auth = require('../middleware/auth');
const {
  getConversations,
  getMessages,
  sendMessage,
  markAsRead,
  deleteMessage
} = require('../controllers/chatController');

const router = express.Router();

// @route   GET /api/chat/conversations
// @desc    Get user conversations
// @access  Private
router.get('/conversations', auth, getConversations);

// @route   GET /api/chat/:conversationId/messages
// @desc    Get messages for a conversation
// @access  Private
router.get('/:conversationId/messages', auth, getMessages);

// @route   POST /api/chat/send
// @desc    Send message
// @access  Private
router.post('/send', auth, [
  body('receiverId', 'Receiver ID is required').not().isEmpty(),
  body('content', 'Message content is required').not().isEmpty()
], sendMessage);

// @route   PUT /api/chat/messages/:messageId/read
// @desc    Mark message as read
// @access  Private
router.put('/messages/:messageId/read', auth, markAsRead);

// @route   DELETE /api/chat/messages/:messageId
// @desc    Delete message
// @access  Private
router.delete('/messages/:messageId', auth, deleteMessage);

module.exports = router;
