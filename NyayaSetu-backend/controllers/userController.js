const User = require('../models/User');
const Case = require('../models/Case');

// @desc    Get all lawyers
// @route   GET /api/users/lawyers
// @access  Private
exports.getLawyers = async (req, res) => {
  try {
    const { specialization, minRating, search } = req.query;

    let query = { userType: 'lawyer', isVerified: true };

    // Filter by specialization
    if (specialization) {
      query.specialization = { $in: [specialization] };
    }

    // Filter by minimum rating
    if (minRating) {
      query.rating = { $gte: parseFloat(minRating) };
    }

    // Search by name
    if (search) {
      query.$or = [
        { firstName: { $regex: search, $options: 'i' } },
        { lastName: { $regex: search, $options: 'i' } }
      ];
    }

    const lawyers = await User.find(query)
      .select('firstName lastName email specialization experience rating hourlyRate profileImage isVerified')
      .sort({ rating: -1, experience: -1 });

    res.json({
      success: true,
      count: lawyers.length,
      data: lawyers
    });
  } catch (error) {
    console.error('Get lawyers error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching lawyers'
    });
  }
};

// @desc    Get user profile
// @route   GET /api/users/profile/:id
// @access  Private
exports.getUserProfile = async (req, res) => {
  try {
    const user = await User.findById(req.params.id)
      .select('-password');

    if (!user) {
      return res.status(404).json({
        success: false,
        message: 'User not found'
      });
    }

    // Get user stats based on type
    let stats = {};
    if (user.userType === 'lawyer') {
      const cases = await Case.find({ lawyer: user._id });
      stats = {
        totalCases: cases.length,
        activeCases: cases.filter(c => c.status === 'active' || c.status === 'in_progress').length,
        completedCases: cases.filter(c => c.status === 'completed').length,
        winRate: cases.length > 0 ?
          (cases.filter(c => c.status === 'completed').length / cases.length * 100).toFixed(1) : 0
      };
    } else if (user.userType === 'client') {
      const cases = await Case.find({ client: user._id });
      stats = {
        totalCases: cases.length,
        activeCases: cases.filter(c => c.status === 'active' || c.status === 'in_progress').length,
        completedCases: cases.filter(c => c.status === 'completed').length
      };
    }

    res.json({
      success: true,
      data: {
        user,
        stats
      }
    });
  } catch (error) {
    console.error('Get user profile error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching user profile'
    });
  }
};

// @desc    Update user verification status (Admin only)
// @route   PUT /api/users/:id/verify
// @access  Private/Admin
exports.verifyUser = async (req, res) => {
  try {
    const user = await User.findByIdAndUpdate(
      req.params.id,
      { isVerified: true },
      { new: true }
    );

    if (!user) {
      return res.status(404).json({
        success: false,
        message: 'User not found'
      });
    }

    // Create notification for user
    await Notification.create({
      user: user._id,
      title: 'Profile Verified',
      message: 'Your lawyer profile has been verified by admin',
      type: 'system',
      priority: 'high'
    });

    res.json({
      success: true,
      message: 'User verified successfully',
      data: user
    });
  } catch (error) {
    console.error('Verify user error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while verifying user'
    });
  }
};

// @desc    Get user notifications
// @route   GET /api/users/notifications
// @access  Private
exports.getNotifications = async (req, res) => {
  try {
    const notifications = await Notification.find({ user: req.user.id })
      .populate('relatedCase', 'title')
      .populate('relatedUser', 'firstName lastName')
      .sort({ createdAt: -1 })
      .limit(50);

    const unreadCount = await Notification.countDocuments({
      user: req.user.id,
      isRead: false
    });

    res.json({
      success: true,
      data: {
        notifications,
        unreadCount
      }
    });
  } catch (error) {
    console.error('Get notifications error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching notifications'
    });
  }
};

// @desc    Mark notification as read
// @route   PUT /api/users/notifications/:id/read
// @access  Private
exports.markNotificationAsRead = async (req, res) => {
  try {
    await Notification.findByIdAndUpdate(req.params.id, {
      isRead: true,
      readAt: new Date()
    });

    res.json({
      success: true,
      message: 'Notification marked as read'
    });
  } catch (error) {
    console.error('Mark notification read error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while updating notification'
    });
  }
};

// @desc    Get all users (admin only)
// @route   GET /api/users
// @access  Private
exports.getUsers = async (req, res) => {
  try {
    if (req.user.userType !== 'admin') {
      return res.status(403).json({
        success: false,
        message: 'Access denied. Admin only.'
      });
    }

    const users = await User.find().select('-password');

    res.json({
      success: true,
      count: users.length,
      data: users
    });
  } catch (error) {
    console.error('Get users error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching users'
    });
  }
};

// @desc    Get user by ID
// @route   GET /api/users/:id
// @access  Private
exports.getUser = async (req, res) => {
  try {
    const user = await User.findById(req.params.id).select('-password');

    if (!user) {
      return res.status(404).json({
        success: false,
        message: 'User not found'
      });
    }

    res.json({
      success: true,
      data: user
    });
  } catch (error) {
    console.error('Get user error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching user'
    });
  }
};

// @desc    Update user
// @route   PUT /api/users/:id
// @access  Private
exports.updateUser = async (req, res) => {
  try {
    const { firstName, lastName, email, phone, address, userType } = req.body;

    // Check if user can update this profile
    if (req.user.userType !== 'admin' && req.user.id !== req.params.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied'
      });
    }

    const user = await User.findByIdAndUpdate(
      req.params.id,
      { firstName, lastName, email, phone, address, userType },
      { new: true, runValidators: true }
    ).select('-password');

    if (!user) {
      return res.status(404).json({
        success: false,
        message: 'User not found'
      });
    }

    res.json({
      success: true,
      message: 'User updated successfully',
      data: user
    });
  } catch (error) {
    console.error('Update user error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while updating user'
    });
  }
};

// @desc    Delete user
// @route   DELETE /api/users/:id
// @access  Private (Admin only)
exports.deleteUser = async (req, res) => {
  try {
    if (req.user.userType !== 'admin') {
      return res.status(403).json({
        success: false,
        message: 'Access denied. Admin only.'
      });
    }

    const user = await User.findById(req.params.id);

    if (!user) {
      return res.status(404).json({
        success: false,
        message: 'User not found'
      });
    }

    await User.findByIdAndDelete(req.params.id);

    res.json({
      success: true,
      message: 'User deleted successfully'
    });
  } catch (error) {
    console.error('Delete user error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while deleting user'
    });
  }
};

// @desc    Verify lawyer
// @route   PUT /api/users/:id/verify
// @access  Private (Admin only)
exports.verifyLawyer = async (req, res) => {
  try {
    if (req.user.userType !== 'admin') {
      return res.status(403).json({
        success: false,
        message: 'Access denied. Admin only.'
      });
    }

    const user = await User.findById(req.params.id);

    if (!user || user.userType !== 'lawyer') {
      return res.status(404).json({
        success: false,
        message: 'Lawyer not found'
      });
    }

    user.isVerified = true;
    await user.save();

    res.json({
      success: true,
      message: 'Lawyer verified successfully',
      data: user
    });
  } catch (error) {
    console.error('Verify lawyer error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while verifying lawyer'
    });
  }
};

// @desc    Get user stats
// @route   GET /api/users/stats
// @access  Private (Admin only)
exports.getUserStats = async (req, res) => {
  try {
    if (req.user.userType !== 'admin') {
      return res.status(403).json({
        success: false,
        message: 'Access denied. Admin only.'
      });
    }

    const totalUsers = await User.countDocuments();
    const totalClients = await User.countDocuments({ userType: 'client' });
    const totalLawyers = await User.countDocuments({ userType: 'lawyer' });
    const verifiedLawyers = await User.countDocuments({ userType: 'lawyer', isVerified: true });

    res.json({
      success: true,
      data: {
        totalUsers,
        totalClients,
        totalLawyers,
        verifiedLawyers,
        pendingVerifications: totalLawyers - verifiedLawyers
      }
    });
  } catch (error) {
    console.error('Get user stats error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching user stats'
    });
  }
};