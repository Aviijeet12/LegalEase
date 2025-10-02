const Case = require('../models/Case');
const User = require('../models/User');

// @desc    Create new case
// @route   POST /api/cases
// @access  Private
exports.createCase = async (req, res) => {
  try {
    const { title, description, category, priority, budget, deadline } = req.body;

    const newCase = await Case.create({
      title,
      description,
      category,
      priority,
      budget,
      deadline,
      client: req.user.id,
      timeline: [{
        event: 'Case Created',
        description: 'Case was successfully created',
        createdBy: req.user.id
      }]
    });

    res.status(201).json({
      success: true,
      message: 'Case created successfully',
      data: newCase
    });
  } catch (error) {
    console.error('Create case error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error during case creation'
    });
  }
};

// @desc    Get all cases for user
// @route   GET /api/cases
// @access  Private
exports.getCases = async (req, res) => {
  try {
    let query = {};

    // Filter based on user type
    if (req.user.userType === 'client') {
      query.client = req.user.id;
    } else if (req.user.userType === 'lawyer') {
      query.lawyer = req.user.id;
    }

    const cases = await Case.find(query)
      .populate('client', 'firstName lastName email phone')
      .populate('lawyer', 'firstName lastName email specialization rating')
      .sort({ createdAt: -1 });

    res.json({
      success: true,
      count: cases.length,
      data: cases
    });
  } catch (error) {
    console.error('Get cases error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching cases'
    });
  }
};

// @desc    Get single case
// @route   GET /api/cases/:id
// @access  Private
exports.getCase = async (req, res) => {
  try {
    const caseData = await Case.findById(req.params.id)
      .populate('client', 'firstName lastName email phone')
      .populate('lawyer', 'firstName lastName email specialization rating')
      .populate('documents')
      .populate('timeline.createdBy', 'firstName lastName');

    if (!caseData) {
      return res.status(404).json({
        success: false,
        message: 'Case not found'
      });
    }

    // Check if user has access to this case
    if (req.user.userType === 'client' && caseData.client._id.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied to this case'
      });
    }

    res.json({
      success: true,
      data: caseData
    });
  } catch (error) {
    console.error('Get case error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching case'
    });
  }
};

// @desc    Update case
// @route   PUT /api/cases/:id
// @access  Private
exports.updateCase = async (req, res) => {
  try {
    const { status, progress, lawyer, timelineEvent } = req.body;

    const caseData = await Case.findById(req.params.id);

    if (!caseData) {
      return res.status(404).json({
        success: false,
        message: 'Case not found'
      });
    }

    // Update case
    const updatedCase = await Case.findByIdAndUpdate(
      req.params.id,
      {
        ...(status && { status }),
        ...(progress && { progress }),
        ...(lawyer && { lawyer }),
        $push: timelineEvent ? {
          timeline: {
            event: timelineEvent.event,
            description: timelineEvent.description,
            createdBy: req.user.id
          }
        } : {}
      },
      { new: true, runValidators: true }
    );

    res.json({
      success: true,
      message: 'Case updated successfully',
      data: updatedCase
    });
  } catch (error) {
    console.error('Update case error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error during case update'
    });
  }
};

// @desc    Delete case
// @route   DELETE /api/cases/:id
// @access  Private (Admin only)
exports.deleteCase = async (req, res) => {
  try {
    const caseData = await Case.findById(req.params.id);

    if (!caseData) {
      return res.status(404).json({
        success: false,
        message: 'Case not found'
      });
    }

    await Case.findByIdAndDelete(req.params.id);

    res.json({
      success: true,
      message: 'Case deleted successfully'
    });
  } catch (error) {
    console.error('Delete case error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error during case deletion'
    });
  }
};

// @desc    Assign lawyer to case
// @route   PUT /api/cases/:id/assign
// @access  Private (Admin only)
exports.assignLawyer = async (req, res) => {
  try {
    const { lawyerId } = req.body;

    const caseData = await Case.findById(req.params.id);
    const lawyer = await User.findById(lawyerId);

    if (!caseData) {
      return res.status(404).json({
        success: false,
        message: 'Case not found'
      });
    }

    if (!lawyer || lawyer.userType !== 'lawyer') {
      return res.status(404).json({
        success: false,
        message: 'Lawyer not found'
      });
    }

    const updatedCase = await Case.findByIdAndUpdate(
      req.params.id,
      {
        lawyer: lawyerId,
        status: 'assigned',
        $push: {
          timeline: {
            event: 'Lawyer Assigned',
            description: `Lawyer ${lawyer.firstName} ${lawyer.lastName} assigned to case`,
            createdBy: req.user.id
          }
        }
      },
      { new: true }
    ).populate('lawyer client', 'firstName lastName email');

    res.json({
      success: true,
      message: 'Lawyer assigned successfully',
      data: updatedCase
    });
  } catch (error) {
    console.error('Assign lawyer error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error during lawyer assignment'
    });
  }
};

// @desc    Update case status
// @route   PUT /api/cases/:id/status
// @access  Private
exports.updateStatus = async (req, res) => {
  try {
    const { status, notes } = req.body;

    const caseData = await Case.findById(req.params.id);

    if (!caseData) {
      return res.status(404).json({
        success: false,
        message: 'Case not found'
      });
    }

    const updatedCase = await Case.findByIdAndUpdate(
      req.params.id,
      {
        status,
        $push: {
          timeline: {
            event: 'Status Updated',
            description: notes || `Case status changed to ${status}`,
            createdBy: req.user.id
          }
        }
      },
      { new: true }
    );

    res.json({
      success: true,
      message: 'Case status updated successfully',
      data: updatedCase
    });
  } catch (error) {
    console.error('Update status error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error during status update'
    });
  }
};

// @desc    Get cases by user (client)
// @route   GET /api/cases/user/:userId
// @access  Private
exports.getCasesByUser = async (req, res) => {
  try {
    const cases = await Case.find({ client: req.params.userId })
      .populate('lawyer', 'firstName lastName email specialization')
      .populate('client', 'firstName lastName email')
      .sort({ createdAt: -1 });

    res.json({
      success: true,
      count: cases.length,
      data: cases
    });
  } catch (error) {
    console.error('Get user cases error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching user cases'
    });
  }
};

// @desc    Get cases by lawyer
// @route   GET /api/cases/lawyer/:lawyerId
// @access  Private
exports.getCasesByLawyer = async (req, res) => {
  try {
    const cases = await Case.find({ lawyer: req.params.lawyerId })
      .populate('lawyer', 'firstName lastName email specialization')
      .populate('client', 'firstName lastName email')
      .sort({ createdAt: -1 });

    res.json({
      success: true,
      count: cases.length,
      data: cases
    });
  } catch (error) {
    console.error('Get lawyer cases error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching lawyer cases'
    });
  }
};