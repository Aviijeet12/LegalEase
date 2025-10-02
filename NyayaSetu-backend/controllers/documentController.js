const Document = require('../models/Document');
const Case = require('../models/Case');
const Notification = require('../models/Notification');
const cloudinary = require('../config/cloudinary');
const fs = require('fs');
const path = require('path');

// @desc    Upload document
// @route   POST /api/documents/upload
// @access  Private
exports.uploadDocument = async (req, res) => {
  try {
    const { caseId, category, description, isPrivate = false } = req.body;

    if (!req.file) {
      return res.status(400).json({
        success: false,
        message: 'Please upload a file'
      });
    }

    // Verify case exists and user has access
    const caseData = await Case.findById(caseId);
    if (!caseData) {
      // Delete uploaded file
      fs.unlinkSync(req.file.path);
      return res.status(404).json({
        success: false,
        message: 'Case not found'
      });
    }

    // Check access permissions
    if (req.user.userType === 'client' && caseData.client.toString() !== req.user.id) {
      fs.unlinkSync(req.file.path);
      return res.status(403).json({
        success: false,
        message: 'Access denied to this case'
      });
    }

    // Upload to Cloudinary
    const result = await cloudinary.uploader.upload(req.file.path, {
      folder: `nyayasetu/cases/${caseId}`,
      resource_type: 'auto'
    });

    // Delete local file after upload
    fs.unlinkSync(req.file.path);

    // Create document record
    const document = await Document.create({
      name: req.file.originalname,
      originalName: req.file.originalname,
      fileUrl: result.secure_url,
      fileSize: req.file.size,
      fileType: req.file.mimetype,
      uploadedBy: req.user.id,
      case: caseId,
      category,
      isPrivate,
      description
    });

    // Add document to case
    await Case.findByIdAndUpdate(caseId, {
      $push: { documents: document._id }
    });

    // Create notification for other party
    const notificationUser = req.user.userType === 'client' ? caseData.lawyer : caseData.client;
    if (notificationUser) {
      await Notification.create({
        user: notificationUser,
        title: 'Document Uploaded',
        message: `${req.user.firstName} uploaded a new document: ${req.file.originalname}`,
        type: 'case_update',
        relatedCase: caseId,
        relatedUser: req.user.id,
        actionUrl: `/cases/${caseId}`
      });
    }

    res.status(201).json({
      success: true,
      message: 'Document uploaded successfully',
      data: document
    });
  } catch (error) {
    // Clean up uploaded file if error occurs
    if (req.file && fs.existsSync(req.file.path)) {
      fs.unlinkSync(req.file.path);
    }

    console.error('Upload document error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error during document upload'
    });
  }
};

// @desc    Get documents for a case
// @route   GET /api/documents/case/:caseId
// @access  Private
exports.getCaseDocuments = async (req, res) => {
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

    const documents = await Document.find({ case: caseId })
      .populate('uploadedBy', 'firstName lastName userType')
      .sort({ createdAt: -1 });

    res.json({
      success: true,
      count: documents.length,
      data: documents
    });
  } catch (error) {
    console.error('Get documents error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching documents'
    });
  }
};

// @desc    Delete document
// @route   DELETE /api/documents/:id
// @access  Private
exports.deleteDocument = async (req, res) => {
  try {
    const document = await Document.findById(req.params.id);

    if (!document) {
      return res.status(404).json({
        success: false,
        message: 'Document not found'
      });
    }

    // Check permissions
    if (document.uploadedBy.toString() !== req.user.id && req.user.userType !== 'admin') {
      return res.status(403).json({
        success: false,
        message: 'Not authorized to delete this document'
      });
    }

    // Delete from Cloudinary
    const publicId = document.fileUrl.split('/').pop().split('.')[0];
    await cloudinary.uploader.destroy(`nyayasetu/cases/${document.case}/${publicId}`);

    // Delete from database
    await Document.findByIdAndDelete(req.params.id);

    // Remove from case
    await Case.findByIdAndUpdate(document.case, {
      $pull: { documents: req.params.id }
    });

    res.json({
      success: true,
      message: 'Document deleted successfully'
    });
  } catch (error) {
    console.error('Delete document error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while deleting document'
    });
  }
};

// @desc    Get all user documents
// @route   GET /api/documents
// @access  Private
exports.getDocuments = async (req, res) => {
  try {
    const documents = await Document.find({ uploadedBy: req.user.id })
      .populate('caseId', 'title caseNumber')
      .sort({ uploadDate: -1 });

    res.json({
      success: true,
      count: documents.length,
      data: documents
    });
  } catch (error) {
    console.error('Get documents error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching documents'
    });
  }
};

// @desc    Get document by ID
// @route   GET /api/documents/:id
// @access  Private
exports.getDocument = async (req, res) => {
  try {
    const document = await Document.findById(req.params.id)
      .populate('caseId', 'title caseNumber')
      .populate('uploadedBy', 'firstName lastName');

    if (!document) {
      return res.status(404).json({
        success: false,
        message: 'Document not found'
      });
    }

    // Check if user has access to this document
    if (req.user.userType === 'client' && document.uploadedBy._id.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied'
      });
    }

    res.json({
      success: true,
      data: document
    });
  } catch (error) {
    console.error('Get document error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching document'
    });
  }
};

// @desc    Update document
// @route   PUT /api/documents/:id
// @access  Private
exports.updateDocument = async (req, res) => {
  try {
    const { name, description } = req.body;

    const document = await Document.findById(req.params.id);

    if (!document) {
      return res.status(404).json({
        success: false,
        message: 'Document not found'
      });
    }

    // Check if user owns this document
    if (document.uploadedBy.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied'
      });
    }

    const updatedDocument = await Document.findByIdAndUpdate(
      req.params.id,
      { fileName: name, description },
      { new: true, runValidators: true }
    );

    res.json({
      success: true,
      message: 'Document updated successfully',
      data: updatedDocument
    });
  } catch (error) {
    console.error('Update document error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while updating document'
    });
  }
};

// @desc    Download document
// @route   GET /api/documents/:id/download
// @access  Private
exports.downloadDocument = async (req, res) => {
  try {
    const document = await Document.findById(req.params.id);

    if (!document) {
      return res.status(404).json({
        success: false,
        message: 'Document not found'
      });
    }

    // Check if user has access to this document
    if (req.user.userType === 'client' && document.uploadedBy.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied'
      });
    }

    const filePath = path.join(__dirname, '..', document.filePath);
    
    // Check if file exists
    if (!fs.existsSync(filePath)) {
      return res.status(404).json({
        success: false,
        message: 'File not found on server'
      });
    }

    res.download(filePath, document.fileName);
  } catch (error) {
    console.error('Download document error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while downloading document'
    });
  }
};

// @desc    Get all documents
// @route   GET /api/documents
// @access  Private
exports.getDocuments = async (req, res) => {
  try {
    const query = req.user.userType === 'admin' ? {} : { uploadedBy: req.user.id };
    
    const documents = await Document.find(query)
      .populate('uploadedBy', 'firstName lastName email')
      .populate('caseId', 'title caseNumber')
      .sort({ uploadDate: -1 });

    res.json({
      success: true,
      count: documents.length,
      data: documents
    });
  } catch (error) {
    console.error('Get documents error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching documents'
    });
  }
};

// @desc    Get single document
// @route   GET /api/documents/:id
// @access  Private
exports.getDocument = async (req, res) => {
  try {
    const document = await Document.findById(req.params.id)
      .populate('uploadedBy', 'firstName lastName email')
      .populate('caseId', 'title caseNumber');

    if (!document) {
      return res.status(404).json({
        success: false,
        message: 'Document not found'
      });
    }

    // Check if user has access to this document
    if (req.user.userType !== 'admin' && 
        document.uploadedBy._id.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied'
      });
    }

    res.json({
      success: true,
      data: document
    });
  } catch (error) {
    console.error('Get document error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while fetching document'
    });
  }
};

// @desc    Update document
// @route   PUT /api/documents/:id
// @access  Private
exports.updateDocument = async (req, res) => {
  try {
    const { fileName, description, category } = req.body;

    const document = await Document.findById(req.params.id);

    if (!document) {
      return res.status(404).json({
        success: false,
        message: 'Document not found'
      });
    }

    // Check if user has access to update this document
    if (req.user.userType !== 'admin' && 
        document.uploadedBy.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied'
      });
    }

    const updatedDocument = await Document.findByIdAndUpdate(
      req.params.id,
      { fileName, description, category },
      { new: true, runValidators: true }
    );

    res.json({
      success: true,
      message: 'Document updated successfully',
      data: updatedDocument
    });
  } catch (error) {
    console.error('Update document error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while updating document'
    });
  }
};

// @desc    Download document
// @route   GET /api/documents/:id/download
// @access  Private
exports.downloadDocument = async (req, res) => {
  try {
    const document = await Document.findById(req.params.id);

    if (!document) {
      return res.status(404).json({
        success: false,
        message: 'Document not found'
      });
    }

    // Check if user has access to download this document
    if (req.user.userType !== 'admin' && 
        document.uploadedBy.toString() !== req.user.id) {
      return res.status(403).json({
        success: false,
        message: 'Access denied'
      });
    }

    // In a real application, you would serve the actual file
    // For now, we'll just return the document info
    res.json({
      success: true,
      message: 'Document download initiated',
      data: {
        fileName: document.fileName,
        fileUrl: document.fileUrl,
        fileSize: document.fileSize
      }
    });
  } catch (error) {
    console.error('Download document error:', error);
    res.status(500).json({
      success: false,
      message: 'Server error while downloading document'
    });
  }
};