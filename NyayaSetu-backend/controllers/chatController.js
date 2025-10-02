const Message = require('../models/Message');
const Case = require('../models/Case');
const Notification = require('../models/Notification');

// @desc    Get messages for a case
// @route   GET /api/chat/case/:caseId
// @access  Private
exports.getCaseMessages = async (req, res) => {
  try {
    const { caseId } = req.params;

    // Verify user has access to this case
    const caseData = await Case.findById(caseId);
    if (!caseData) {
      return res.status(404).json({
        success: false,
        message: 'Case not found'
      });
    }

    if (req.user.userType === 'client' && caseData.client.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied to this case'
      });
    }

    if (req.user.userType === 'lawyer' && caseData.lawyer && caseData.lawyer.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied to this case'
      });
    }

    const messages = await Message.find({ case: caseId })
      .populate('sender', 'firstName lastName userType profileImage')
      .populate('receiver', 'firstName lastName userType profileImage')
      .sort({ createdAt: 1 });

    res.json({
      success: true,
      count: messages.length,
      data: messages
    });
  } catch (error) {
    console.error('Get messages error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching messages'
    });
  }
};

// @desc    Send a message
// @route   POST /api/chat/send
// @access  Private
exports.sendMessage = async (req, res) => {
  try {
    const { caseId, receiverId, content, messageType = 'text', fileUrl, fileName } = req.body;

    // Verify case exists and user has access
    const caseData = await Case.findById(caseId);
    if (!caseData) {
      return res.status(404).json({
        success: false,
        message: 'Case not found'
      });
    }

    // Check access permissions
    if (req.user.userType === 'client' && caseData.client.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied to this case'
      });
    }

    if (req.user.userType === 'lawyer' && caseData.lawyer && caseData.lawyer.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied to this case'
      });
    }

    // Create message
    const message = await Message.create({
      case: caseId,
      sender: req.user.id,
      receiver: receiverId,
      content,
      messageType,
      fileUrl,
      fileName
    });

    // Populate sender info for response
    await message.populate('sender', 'firstName lastName userType profileImage');

    // Create notification for receiver
    await Notification.create({
      user: receiverId,
      title: 'New Message',
      message: `You have a new message from ${req.user.firstName} ${req.user.lastName}`,
      type: 'message',
      relatedCase: caseId,
      relatedUser: req.user.id,
      actionUrl: `/chat/${caseId}`
    });

    res.status(201).json({
      success: true,
      message: 'Message sent successfully',
      data: message
    });
  } catch (error) {
    console.error('Send message error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while sending message'
    });
  }
};

// @desc    Mark messages as read
// @route   PUT /api/chat/mark-read
// @access  Private
exports.markMessagesAsRead = async (req, res) => {
  try {
    const { messageIds } = req.body;

    await Message.updateMany(
      { _id: { $in: messageIds }, receiver: req.user.id },
      {
        isRead: true,
        readAt: new Date()
      }
    );

    res.json({
      success: true,
      message: 'Messages marked as read'
    });
  } catch (error) {
    console.error('Mark messages read error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while marking messages as read'
    });
  }
};

// @desc    Get user conversations
// @route   GET /api/chat/conversations
// @access  Private
exports.getConversations = async (req, res) => {
  try {
    const conversations = await Message.aggregate([
      {
        $match: {
          $or: [
            { senderId: req.user.id },
            { receiverId: req.user.id }
          ]
        }
      },
      {
        $sort: { timestamp: -1 }
      },
      {
        $group: {
          _id: {
            participants: {
              $cond: {
                if: { $eq: ['$senderId', req.user.id] },
                then: ['$senderId', '$receiverId'],
                else: ['$receiverId', '$senderId']
              }
            }
          },
          lastMessage: { $first: '$$ROOT' },
          unreadCount: {
            $sum: {
              $cond: [
                {
                  $and: [
                    { $ne: ['$senderId', req.user.id] },
                    { $eq: ['$isRead', false] }
                  ]
                },
                1,
                0
              ]
            }
          }
        }
      }
    ]);

    res.json({
      success: true,
      data: conversations
    });
  } catch (error) {
    console.error('Get conversations error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching conversations'
    });
  }
};

// @desc    Get messages for conversation
// @route   GET /api/chat/:conversationId/messages
// @access  Private
exports.getMessages = async (req, res) => {
  try {
    const { conversationId } = req.params;
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 50;

    const messages = await Message.find({
      conversationId: conversationId,
      $or: [
        { senderId: req.user.id },
        { receiverId: req.user.id }
      ]
    })
      .populate('senderId', 'firstName lastName')
      .populate('receiverId', 'firstName lastName')
      .sort({ timestamp: -1 })
      .limit(limit * 1)
      .skip((page - 1) * limit);

    res.json({
      success: true,
      count: messages.length,
      data: messages.reverse()
    });
  } catch (error) {
    console.error('Get messages error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching messages'
    });
  }
};

// @desc    Mark messages as read
// @route   PUT /api/chat/:conversationId/read
// @access  Private
exports.markAsRead = async (req, res) => {
  try {
    const { conversationId } = req.params;

    await Message.updateMany(
      {
        conversationId: conversationId,
        receiverId: req.user.id,
        isRead: false
      },
      {
        isRead: true,
        readAt: new Date()
      }
    );

    res.json({
      success: true,
      message: 'Messages marked as read'
    });
  } catch (error) {
    console.error('Mark as read error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while marking messages as read'
    });
  }
};

// @desc    Delete message
// @route   DELETE /api/chat/messages/:messageId
// @access  Private
exports.deleteMessage = async (req, res) => {
  try {
    const message = await Message.findById(req.params.messageId);

    if (!message) {
      return res.status(404).json({
        success: false,
        message: 'Message not found'
      });
    }

    // Only allow sender to delete their own messages
    if (message.senderId.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Not authorized to delete this message'
      });
    }

    await Message.findByIdAndDelete(req.params.messageId);

    res.json({
      success: true,
      message: 'Message deleted successfully'
    });
  } catch (error) {
    console.error('Delete message error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while deleting message'
    });
  }
};