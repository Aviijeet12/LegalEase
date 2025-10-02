const nodemailer = require('nodemailer');

// Create transporter
const transporter = nodemailer.createTransporter({
  service: process.env.EMAIL_SERVICE || 'gmail',
  auth: {
    user: process.env.EMAIL_USERNAME,
    pass: process.env.EMAIL_PASSWORD
  }
});

// Verify transporter configuration
transporter.verify((error, success) => {
  if (error) {
    console.log('Email transporter error:', error);
  } else {
    console.log('Email server is ready to send messages');
  }
});

// Email templates
const emailTemplates = {
  welcome: (name) => ({
    subject: 'Welcome to NyayaSetu - Your Legal Companion',
    html: `
      <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
        <h2 style="color: #2196F3;">Welcome to NyayaSetu!</h2>
        <p>Hello ${name},</p>
        <p>Thank you for joining NyayaSetu, your trusted legal companion. We're excited to help you manage your legal matters with ease and confidence.</p>
        <p>With NyayaSetu, you can:</p>
        <ul>
          <li>Connect with qualified lawyers</li>
          <li>Manage your legal cases efficiently</li>
          <li>Upload and organize legal documents</li>
          <li>Track case progress in real-time</li>
        </ul>
        <p>If you have any questions, feel free to contact our support team.</p>
        <br>
        <p>Best regards,<br>The NyayaSetu Team</p>
      </div>
    `
  }),

  caseAssigned: (clientName, lawyerName, caseTitle) => ({
    subject: `Your Case "${caseTitle}" Has Been Assigned`,
    html: `
      <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
        <h2 style="color: #2196F3;">Case Assignment Update</h2>
        <p>Hello ${clientName},</p>
        <p>We're pleased to inform you that your case <strong>"${caseTitle}"</strong> has been assigned to <strong>${lawyerName}</strong>.</p>
        <p>Your lawyer will review your case details and contact you shortly to discuss next steps.</p>
        <p>You can track your case progress and communicate with your lawyer through the NyayaSetu platform.</p>
        <br>
        <p>Best regards,<br>The NyayaSetu Team</p>
      </div>
    `
  }),

  passwordReset: (name, resetToken) => ({
    subject: 'Password Reset Request - NyayaSetu',
    html: `
      <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
        <h2 style="color: #2196F3;">Password Reset</h2>
        <p>Hello ${name},</p>
        <p>We received a request to reset your password for your NyayaSetu account.</p>
        <p>Use the following token to reset your password:</p>
        <div style="background: #f5f5f5; padding: 15px; border-radius: 5px; text-align: center; font-size: 18px; font-weight: bold; margin: 20px 0;">
          ${resetToken}
        </div>
        <p>This token will expire in 1 hour for security reasons.</p>
        <p>If you didn't request a password reset, please ignore this email.</p>
        <br>
        <p>Best regards,<br>The NyayaSetu Team</p>
      </div>
    `
  })
};

// Send email function
const sendEmail = async (to, templateName, templateData) => {
  try {
    if (!emailTemplates[templateName]) {
      throw new Error(`Email template '${templateName}' not found`);
    }

    const template = emailTemplates[templateName](...templateData);

    const mailOptions = {
      from: `"NyayaSetu" <${process.env.EMAIL_USERNAME}>`,
      to,
      subject: template.subject,
      html: template.html
    };

    const result = await transporter.sendMail(mailOptions);
    console.log('Email sent successfully:', result.messageId);
    return { success: true, messageId: result.messageId };
  } catch (error) {
    console.error('Email sending error:', error);
    return { success: false, error: error.message };
  }
};

module.exports = {
  sendEmail,
  emailTemplates
};